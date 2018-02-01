package uk.gov.digital.ho.egar.vscan.service.client.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.SetObjectTaggingRequest;
import com.amazonaws.services.s3.model.Tag;

import uk.gov.digital.ho.egar.vscan.api.exceptions.FileClientVSException;
import uk.gov.digital.ho.egar.vscan.config.file.S3Config;
import uk.gov.digital.ho.egar.vscan.model.FileRequest;
import uk.gov.digital.ho.egar.vscan.model.ScanResult;
import uk.gov.digital.ho.egar.vscan.service.client.FileClient;
import uk.gov.digital.ho.egar.vscan.service.client.model.MultipartFileResource;

@Profile("!file-mocks")
@Component
public class S3FileClient implements FileClient {

	private final Log logger = LogFactory.getLog(S3FileClient.class);
	@Autowired
	private AmazonS3 s3Client;

	@Autowired
	private S3Config s3Config;

	@Override
	public MultipartFileResource retrieveFile(FileRequest fileRequest) throws FileClientVSException {

		AmazonS3URI uri = null;
		MultipartFileResource file;

		try {
			uri = new AmazonS3URI(fileRequest.getFileLink());
			String[] fName = uri.getKey().split("[\\\\/]");
			S3Object object = s3Client.getObject(new GetObjectRequest(uri.getBucket(), uri.getKey()));
			file = MultipartFileResource.builder()
					.filename(fName[fName.length - 1])
					.inputStream(object.getObjectContent())
					.build();
		} catch (AmazonClientException e) {
            String message = String.format("Unable to get file for link '%s'.",fileRequest.getFileLink());
            if (uri != null) {
                message = String.format("Unable to get file '%s' from S3 bucket '%s'", uri.getKey(), uri.getBucket());
            }
            logger.error(message);
            throw new FileClientVSException(message, e);
        }
		return file;
	}

	@Override
	/**
	 * Update tags of object on S3
	 */
	public void updateObjectTags(FileRequest fileRequest, ScanResult result) {
		AmazonS3URI objectUri = new AmazonS3URI(fileRequest.getFileLink());
		String scannedValue = "Infected";
		if (result.isClean())
			scannedValue = "Clean";

		List<com.amazonaws.services.s3.model.Tag> tags = new ArrayList<>();
		tags.add(new Tag("Scanned Date", result.getDate().toString()));
		tags.add(new Tag("Scanned Value", scannedValue));

		s3Client.setObjectTagging(
				new SetObjectTaggingRequest(objectUri.getBucket(), objectUri.getKey(), new ObjectTagging(tags)));
	}

	/**
	 * Move to new bucket
	 */
	//TODO Remove this function, we will use File APIs' method
	@Override
	public URL moveToNewBucket(String fileLink) {

		AmazonS3URI uri = new AmazonS3URI(fileLink);
		try {
			CopyObjectRequest copyObject = new CopyObjectRequest(uri.getBucket(), uri.getKey(),
					s3Config.getCleanbucket(), uri.getKey());
			s3Client.copyObject(copyObject);
			s3Client.deleteObject(uri.getBucket(), uri.getKey());

		} catch (AmazonClientException e) {
			logger.info("Error Message: " + e.getMessage());
			// throw something
		}
		return s3Client.getUrl(s3Config.getCleanbucket(), uri.getKey());
	}
	
}
