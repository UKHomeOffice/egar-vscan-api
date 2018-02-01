package uk.gov.digital.ho.egar.vscan.service;

import uk.gov.digital.ho.egar.vscan.api.exceptions.ScanNotificationVSException;
import uk.gov.digital.ho.egar.vscan.model.ScanNotification;

public interface NotificationService {
    void scanComplete(ScanNotification notification) throws ScanNotificationVSException;
}
