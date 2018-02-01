package uk.gov.digital.ho.egar.vscan.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.gov.digital.ho.egar.vscan.api.exceptions.ScanNotificationVSException;
import uk.gov.digital.ho.egar.vscan.constants.QueueNames;
import uk.gov.digital.ho.egar.vscan.model.ScanNotification;
import uk.gov.digital.ho.egar.vscan.service.NotificationService;

@Service
public class JmsNotificationService implements NotificationService{
	
	private final Log logger = LogFactory.getLog(JmsNotificationService.class);

    private final JmsTemplate defaultJmsTemplate;

    private final ObjectMapper objectMapper;

    private final QueueNames queueNames;

	public JmsNotificationService(@Autowired JmsTemplate defaultJmsTemplate, @Autowired ObjectMapper objectMapper, @Autowired QueueNames queueNames) {
        this.defaultJmsTemplate = defaultJmsTemplate;
        this.objectMapper = objectMapper;
        this.queueNames = queueNames;
    }

    @Override
    public void scanComplete(final ScanNotification notification) throws ScanNotificationVSException {
    	
    	logger.info("Sending message to SQS queue: " + queueNames.getVscanResponseQueue());
    	try {
            String notifyValue = objectMapper.writeValueAsString(notification);

            defaultJmsTemplate.convertAndSend(queueNames.getVscanResponseQueue(),
                    notifyValue);
        }catch (JsonProcessingException e){
    	    logger.error("Unable to convert notification to a string.");
    	    throw new ScanNotificationVSException("Unable to convert notification to string.");
        }
    }
}
