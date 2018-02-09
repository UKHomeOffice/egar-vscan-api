package uk.gov.digital.ho.egar.vscan.service.client.impl;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
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

	public static final String UTF_8 = "UTF-8";

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
			String key = urlDecodeKey(uri);
			String[] fName = key.split("[\\\\/]");
			S3Object object = s3Client.getObject(new GetObjectRequest(uri.getBucket(), key));
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
		String key = urlDecodeKey(objectUri);
		String scannedValue = "Infected";
		if (result.isClean())
			scannedValue = "Clean";

		List<com.amazonaws.services.s3.model.Tag> tags = new ArrayList<>();
		tags.add(new Tag("Scanned Date", result.getDate().toString()));
		tags.add(new Tag("Scanned Value", scannedValue));

		s3Client.setObjectTagging(
				new SetObjectTaggingRequest(objectUri.getBucket(), key, new ObjectTagging(tags)));
	}

	public String urlDecodeKey(AmazonS3URI uri){
		String urlDecodedKey = uri.getKey();
		try {
			urlDecodedKey = URLDecoder.decode(urlDecodedKey, UTF_8);
		} catch (UnsupportedEncodingException e) {
			logger.error(String.format("Unable to decode '%s' with format '%s'", urlDecodedKey, UTF_8), e);
		}
		return urlDecodedKey;
	}

}
