package uk.gov.digital.ho.egar.vscan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.egar.vscan.api.exceptions.VSException;
import uk.gov.digital.ho.egar.vscan.model.File;
import uk.gov.digital.ho.egar.vscan.model.FileRequest;
import uk.gov.digital.ho.egar.vscan.model.ScanNotification;
import uk.gov.digital.ho.egar.vscan.model.ScanResult;
import uk.gov.digital.ho.egar.vscan.model.ScannedFileStatus;
import uk.gov.digital.ho.egar.vscan.service.NotificationService;
import uk.gov.digital.ho.egar.vscan.service.VirusScanService;
import uk.gov.digital.ho.egar.vscan.service.client.FileClient;
import uk.gov.digital.ho.egar.vscan.service.client.VirusScanClient;
import uk.gov.digital.ho.egar.vscan.service.client.model.MultipartFileResource;

@Service
public class VirusScanServiceImpl implements VirusScanService {

    @Autowired
    private VirusScanClient virusScanClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void performVirusScan(FileRequest fileRequest) throws VSException {

        //Get file 
    	MultipartFileResource file = fileClient.retrieveFile(fileRequest);

        //Request virus scan to some service
        ScanResult result = virusScanClient.scan(file);//requires name,  MultipartFile file
        
        File updatedFile = File.builder()
        			 	 .fileUuid(fileRequest.getFileUuid())
        			 	 .fileLink(fileRequest.getFileLink())
        			 	 .clean(result.isClean())
        			 	 .date(result.getDate())
        			 	 .build();
        
        //Update tags on S3
        fileClient.updateObjectTags(fileRequest, result);

        //Send notification to SQS
        ScanNotification notification = createNotification(updatedFile);
        notificationService.scanComplete(notification);
    }

    private ScanNotification createNotification(final File file) {
        return ScanNotification.builder()
                .fileUuid(file.getFileUuid())
                .fileStatus(file.isClean()?ScannedFileStatus.CLEAN:ScannedFileStatus.INFECTED)
                .build();
    }
}
