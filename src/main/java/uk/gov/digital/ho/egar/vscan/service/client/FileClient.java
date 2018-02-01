package uk.gov.digital.ho.egar.vscan.service.client;

import uk.gov.digital.ho.egar.vscan.api.exceptions.DataNotFoundVSException;
import uk.gov.digital.ho.egar.vscan.api.exceptions.FileClientVSException;
import uk.gov.digital.ho.egar.vscan.model.FileRequest;
import uk.gov.digital.ho.egar.vscan.model.ScanResult;
import uk.gov.digital.ho.egar.vscan.service.client.model.MultipartFileResource;

import java.net.URL;

public interface FileClient {
	MultipartFileResource retrieveFile(FileRequest fileRequest) throws DataNotFoundVSException, FileClientVSException;

	void updateObjectTags(FileRequest fileRequest, ScanResult result) throws FileClientVSException;

	@Deprecated
	URL moveToNewBucket(String fileLink);
}
