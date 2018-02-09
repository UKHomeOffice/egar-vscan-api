package uk.gov.digital.ho.egar.vscan.service.client.impl.dummy;

import java.net.URL;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import uk.gov.digital.ho.egar.vscan.api.exceptions.FileClientVSException;
import uk.gov.digital.ho.egar.vscan.model.FileRequest;
import uk.gov.digital.ho.egar.vscan.model.ScanResult;
import uk.gov.digital.ho.egar.vscan.service.client.FileClient;
import uk.gov.digital.ho.egar.vscan.service.client.model.MultipartFileResource;

@Profile("file-mocks")
@Component
public class DummyS3FileClient implements FileClient {

	private boolean retrieveFileException = false;

	private boolean updateObjectTagsException = false;

	@Override
    public MultipartFileResource retrieveFile(FileRequest fileRequest) throws FileClientVSException {

    	if (retrieveFileException){
			throw new FileClientVSException("Error getting file.");
		}

    	return MultipartFileResource.builder()
				.filename(null)
				.inputStream(null)
				.build();
    }


	@Override
	public void updateObjectTags(FileRequest fileRequest, ScanResult result) throws FileClientVSException {
		if (updateObjectTagsException){
			throw new FileClientVSException("Error getting file.");
		}
	}


	public void setRetrieveFileException(boolean retrieveFileException) {
		this.retrieveFileException = retrieveFileException;
	}

	public void setUpdateObjectTagsException(boolean updateObjectTagsException) {
		this.updateObjectTagsException = updateObjectTagsException;
	}

}
