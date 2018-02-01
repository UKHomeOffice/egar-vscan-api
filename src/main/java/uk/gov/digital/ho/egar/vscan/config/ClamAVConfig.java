/**
 * 
 */
package uk.gov.digital.ho.egar.vscan.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Data
@Profile("!scan-mocks")
public class ClamAVConfig {

	@Value("${clamav.host}")
	private String hostname;

	@Value("${clamav.port}")
	private int port;

}
