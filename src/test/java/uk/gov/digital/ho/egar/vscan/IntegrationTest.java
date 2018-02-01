package uk.gov.digital.ho.egar.vscan;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.civica.microservice.util.testing.utils.ConditionalIgnoreRule;
import uk.co.civica.microservice.util.testing.utils.IgnoreWhenNotIntegration;
import uk.gov.digital.ho.egar.vscan.model.FileRequest;
import uk.gov.digital.ho.egar.vscan.service.VirusScanService;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Requires the following parameters:
 * aws.s3.region
 * aws.s3.bucket
 * aws.s3.secret.key
 * aws.s3.access.key
 *
 * And clamav on localhost port 3310
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties
        ={
        "spring.profiles.active=jms-disabled",
        "clamav.host=localhost",
        "clamav.port=3310"
})
@ConditionalIgnoreRule.ConditionalIgnore( condition = IgnoreWhenNotIntegration.class )
public class IntegrationTest {

    @Rule
    public ConditionalIgnoreRule rule = new ConditionalIgnoreRule();

    @Rule
    public ExpectedException expectedException;

    @MockBean
    private JmsTemplate template;

    @Autowired
    private VirusScanService service;

    @Value("{integration.clean.file.link}")
    private String cleanFileLink;
    //"https://egar-file-upload-cleanTest.s3.eu-west-2.amazonaws.com/53e32000-fb87-11e7-8934-73e8044b20e3/cleanTest.txt"

    @Value("{integration.infected.file.link}")
    private String infectedFileLink;

    @Test
    public void cleanTest() throws Exception {

        FileRequest request = new FileRequest(UUID.randomUUID(), cleanFileLink);

        service.performVirusScan(request);

        verify(template, times(1)).convertAndSend("vscan_response","{\"file_uuid\":\"5c74e283-5b0f-4614-bb20-5efd7ce50deb\",\"file_status\":\"CLEAN\"}");
    }

    @Test
    public void quarantineTest() throws Exception {

        FileRequest request = new FileRequest(UUID.randomUUID(), infectedFileLink);
        service.performVirusScan(request);

        verify(template, times(1)).convertAndSend("vscan_response","{\"file_uuid\":\"5c74e283-5b0f-4614-bb20-5efd7ce50deb\",\"file_status\":\"INFECTED\"}");
    }

}
