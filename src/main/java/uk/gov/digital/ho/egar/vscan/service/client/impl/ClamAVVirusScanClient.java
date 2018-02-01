package uk.gov.digital.ho.egar.vscan.service.client.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

import fi.solita.clamav.ClamAVClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import uk.gov.digital.ho.egar.vscan.config.ClamAVConfig;
import uk.gov.digital.ho.egar.vscan.model.ScanResult;
import uk.gov.digital.ho.egar.vscan.service.client.VirusScanClient;
import uk.gov.digital.ho.egar.vscan.service.client.model.MultipartFileResource;

@Profile("!scan-mocks")
@Component
public class ClamAVVirusScanClient implements VirusScanClient{

	@Autowired
	private ClamAVConfig clamAVConfig;

	@Override
	public ScanResult scan(MultipartFileResource file) {

		// Send file to clam av 
		ClamAVClient a = new ClamAVClient(clamAVConfig.getHostname(), clamAVConfig.getPort());

		byte[] rawResponse = new byte[0];
		try {
			rawResponse = a.scan(file.getInputStream());
		} catch (IOException e) {
			//Unable to scan file
		}
		boolean clean = false;
		try {
			clean = ClamAVClient.isCleanReply(rawResponse);
		} catch (UnsupportedEncodingException e) {
			//unable to read response
		}

		ScanResult scanResult = ScanResult.builder()
				.clean(clean)
				.date(LocalDate.now())
				.build();

		return scanResult;
	}

}
