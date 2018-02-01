package uk.gov.digital.ho.egar.vscan.config.file;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration values required for S3.
 */
@Getter
@Configuration
@Profile("!file-mocks")
public class S3Config {

	@Value("${aws.s3.region}")
	private String region;

	@Value("${aws.s3.bucket}")
	private String bucket;

	@Value("${aws.s3.access.key}")
	private String accessKey;

	@Value("${aws.s3.secret.key}")
	private String secretKey;
	
	@Value("${aws.s3.quarantinebucket}")
	private String quarantinebucket;
	
	@Value("${aws.s3.cleanbucket}")
	private String cleanbucket;
	
}
