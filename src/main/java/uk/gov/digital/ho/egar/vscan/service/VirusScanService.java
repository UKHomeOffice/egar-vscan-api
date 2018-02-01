package uk.gov.digital.ho.egar.vscan.service;

import uk.gov.digital.ho.egar.vscan.model.FileRequest;

public interface VirusScanService {
    void performVirusScan(FileRequest fileRequest) throws Exception;
}
