package uk.gov.digital.ho.egar.vscan;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import uk.gov.digital.ho.egar.vscan.api.jms.VScanService;
import uk.gov.digital.ho.egar.vscan.service.client.FileClient;
import uk.gov.digital.ho.egar.vscan.service.client.VirusScanClient;
import uk.gov.digital.ho.egar.vscan.service.client.impl.dummy.DummyS3FileClient;
import uk.gov.digital.ho.egar.vscan.service.client.impl.dummy.DummyVirusScanClient;

import javax.jms.JMSException;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties
        ={
        "spring.profiles.active=file-mocks,scan-mocks,jms-disabled"
})
public class EndpointTest {

    @Rule
    public ExpectedException expectedException;

    @MockBean
    private JmsTemplate template;

    @Autowired
    private VScanService service;

    @Autowired
    private VirusScanClient virusScanClient;

    @Autowired
    private FileClient fileClient;

    @Before
    public void setup(){
        setVirusScanResponse(true);
        setUpdateTagsException(false);
        setVirusScanException(false);
        setRetrieveFileException(false);

        expectedException = ExpectedException.none();
    }

    @Test
    public void testFileCleanFlow() throws JMSException {
        String request = "{\"file_uuid\":\"9856506f-c225-41a8-afcb-8a01fd00edec\",\"file_link\":\"abc123\"}";

        service.requestVirusScan(request);

        String expectedResponse = "{\"file_uuid\":\"9856506f-c225-41a8-afcb-8a01fd00edec\",\"file_status\":\"CLEAN\"}";
        verify(template, times(1)).convertAndSend("vscan_response", expectedResponse);
    }

    @Test
    public void testFileInfectedFlow() throws JMSException {
        setVirusScanResponse(false);
        String request = "{\"file_uuid\":\"9856506f-c225-41a8-afcb-8a01fd00edec\",\"file_link\":\"abc123\"}";

        service.requestVirusScan(request);

        String expectedResponse = "{\"file_uuid\":\"9856506f-c225-41a8-afcb-8a01fd00edec\",\"file_status\":\"INFECTED\"}";
        verify(template, times(1)).convertAndSend("vscan_response", expectedResponse);
    }

    @Test(expected = JMSException.class)
    public void testFileInvalidRequest() throws JMSException {
        expectedException.expect(JMSException.class);
        expectedException.expectMessage("Encountered error when attempting to perform virus scan.");

        String request = "{}";

        service.requestVirusScan(request);
    }

    @Test(expected = JMSException.class)
    public void testDeserialisationFailure() throws JMSException {
        expectedException.expect(JMSException.class);
        expectedException.expectMessage("Encountered error when attempting to perform virus scan.");

        String request = "{\"something\":false}";

        service.requestVirusScan(request);
    }

    @Test(expected = JMSException.class)
    public void testRuntimeException() throws JMSException {
        expectedException.expect(JMSException.class);
        expectedException.expectMessage("Encountered error when attempting to perform virus scan for file '9856506f-c225-41a8-afcb-8a01fd00edec'.");

        String request = "{\"file_uuid\":\"9856506f-c225-41a8-afcb-8a01fd00edec\"}";
        String expectedResponse = "{\"file_uuid\":\"9856506f-c225-41a8-afcb-8a01fd00edec\",\"file_status\":\"CLEAN\"}";

        doThrow(new RuntimeException("boom")).when(template).convertAndSend(eq("vscan_response"), eq(expectedResponse));

        service.requestVirusScan(request);
    }

    @Test(expected = JMSException.class)
    public void testRetrieveFileError() throws JMSException {
        setRetrieveFileException(true);
        expectedException.expect(JMSException.class);
        expectedException.expectMessage("Encountered error when attempting to perform virus scan for file '9856506f-c225-41a8-afcb-8a01fd00edec'.");

        String request = "{\"file_uuid\":\"9856506f-c225-41a8-afcb-8a01fd00edec\",\"file_link\":\"abc123\"}";

        service.requestVirusScan(request);
    }

    @Test(expected = JMSException.class)
    public void testScanError() throws JMSException {
        setVirusScanException(true);
        expectedException.expect(JMSException.class);
        expectedException.expectMessage("Encountered error when attempting to perform virus scan for file '9856506f-c225-41a8-afcb-8a01fd00edec'.");

        String request = "{\"file_uuid\":\"9856506f-c225-41a8-afcb-8a01fd00edec\",\"file_link\":\"abc123\"}";

        service.requestVirusScan(request);
    }

    @Test(expected = JMSException.class)
    public void testUpdateTagsError() throws JMSException {
        setUpdateTagsException(true);
        expectedException.expect(JMSException.class);
        expectedException.expectMessage("Encountered error when attempting to perform virus scan for file '9856506f-c225-41a8-afcb-8a01fd00edec'.");

        String request = "{\"file_uuid\":\"9856506f-c225-41a8-afcb-8a01fd00edec\",\"file_link\":\"abc123\"}";

        service.requestVirusScan(request);
    }

    private void setVirusScanResponse(boolean virusScanResult ) {
        if (virusScanClient instanceof DummyVirusScanClient){
            ((DummyVirusScanClient)virusScanClient).setResponse(virusScanResult);
        }
    }

    private void setVirusScanException(boolean exception ) {
        if (virusScanClient instanceof DummyVirusScanClient){
            ((DummyVirusScanClient)virusScanClient).setException(exception);
        }
    }

    private void setRetrieveFileException(boolean exception ) {
        if (fileClient instanceof DummyS3FileClient){
            ((DummyS3FileClient)fileClient).setRetrieveFileException(exception);
        }
    }

    private void setUpdateTagsException(boolean exception ) {
        if (fileClient instanceof DummyS3FileClient){
            ((DummyS3FileClient)fileClient).setUpdateObjectTagsException(exception);
        }
    }
}
