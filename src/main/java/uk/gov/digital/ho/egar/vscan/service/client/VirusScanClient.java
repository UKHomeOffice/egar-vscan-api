package uk.gov.digital.ho.egar.vscan.service.client;

import uk.gov.digital.ho.egar.vscan.api.exceptions.ScanClientVSException;
import uk.gov.digital.ho.egar.vscan.model.ScanResult;
import uk.gov.digital.ho.egar.vscan.service.client.model.MultipartFileResource;

public interface VirusScanClient {

	public ScanResult scan(MultipartFileResource file) throws ScanClientVSException;
}
