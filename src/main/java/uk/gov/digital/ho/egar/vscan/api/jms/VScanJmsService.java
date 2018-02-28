package uk.gov.digital.ho.egar.vscan.api.jms;

import java.util.Set;

import javax.jms.JMSException;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import uk.gov.digital.ho.egar.vscan.constants.QueueNames;
import uk.gov.digital.ho.egar.vscan.model.FileRequest;
import uk.gov.digital.ho.egar.vscan.service.VirusScanService;

@Service
public class VScanJmsService implements VScanService {

    private static final Logger logger = LoggerFactory.getLogger(VScanJmsService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VirusScanService virusScanService;

    @Autowired
    private Validator validator;

    @Override
    @JmsListener(destination = QueueNames.VSCAN_REQUEST_QUEUE_REF)
    public void requestVirusScan(final String requestJSON) throws JMSException {
        logger.info("Received virus scan request");

        FileRequest fileRequest = null;
        try {
            fileRequest = objectMapper.readValue(requestJSON, FileRequest.class);

            validateRequest(fileRequest);

            virusScanService.performVirusScan(fileRequest);

        } catch (Exception e){
            String errorMessage = "Encountered error when attempting to perform virus scan.";
            if (fileRequest!=null){
                errorMessage = String.format("Encountered error when attempting to perform virus scan on file '%s'.", fileRequest.getFileUuid());
            }
            logger.error(errorMessage, e);
            logger.error(e.getMessage());
            
//          throw new JMSException(errorMessage);

        }
    }

    /**
     * Validates the incoming request.
     * @param request the request
     */
    private void validateRequest(final FileRequest request){
        Set<ConstraintViolation<FileRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()){
            throw new IllegalArgumentException("File request was invalid. " + getErrorMessage(violations));
        }

    }

    /**
     * Builds an error message from the violations
     * @param violations The violations
     * @return The error message.
     */
    private String getErrorMessage(Set<ConstraintViolation<FileRequest>> violations) {
        StringBuilder messageBuilder = new StringBuilder();

        boolean first = true;

        for (ConstraintViolation<FileRequest> violation: violations){
            messageBuilder.append(violation.getPropertyPath()).append(": ").append(violation.getMessage());

            if (first) {
                messageBuilder.append(", ");
            }
            first = false;
        }
        return messageBuilder.toString();
    }
}
