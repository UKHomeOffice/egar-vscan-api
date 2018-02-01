package uk.gov.digital.ho.egar.vscan.config.queue;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Getter
@Configuration
@Profile("!jms-disabled")
public class SqsConfig {

	@Value("${aws.sqs.region}")
	private String region;

	@Value("${aws.sqs.access.key}")
	private String accessKey;

	@Value("${aws.sqs.secret.key}")
	private String secretKey;
	
}
