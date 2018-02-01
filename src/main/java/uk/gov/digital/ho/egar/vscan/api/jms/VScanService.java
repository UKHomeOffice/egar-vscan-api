package uk.gov.digital.ho.egar.vscan.api.jms;

import javax.jms.JMSException;

public interface VScanService {
    void requestVirusScan(String requestJSON) throws JMSException;
}
