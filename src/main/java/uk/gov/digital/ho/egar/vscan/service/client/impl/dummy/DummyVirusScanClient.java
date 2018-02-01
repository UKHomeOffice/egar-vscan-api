package uk.gov.digital.ho.egar.vscan.service.client.impl.dummy;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import uk.gov.digital.ho.egar.vscan.api.exceptions.ScanClientVSException;
import uk.gov.digital.ho.egar.vscan.model.ScanResult;
import uk.gov.digital.ho.egar.vscan.service.client.VirusScanClient;
import uk.gov.digital.ho.egar.vscan.service.client.model.MultipartFileResource;

@Profile("scan-mocks")
@Component
public class DummyVirusScanClient implements VirusScanClient{

    private boolean response = true;

    private boolean exception = false;

    @Override
    public ScanResult scan(MultipartFileResource file) throws ScanClientVSException {

        if (exception) {
            throw new ScanClientVSException("error");
        }

        return ScanResult.builder().clean(response).build();
    }

    public void setResponse(boolean response) {
        this.response = response;
    }


    public void setException(boolean exception) {
        this.exception = exception;
    }
}
