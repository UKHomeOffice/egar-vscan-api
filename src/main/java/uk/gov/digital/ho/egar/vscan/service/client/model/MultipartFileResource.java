package uk.gov.digital.ho.egar.vscan.service.client.model;

import java.io.InputStream;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class MultipartFileResource {

	private String filename;
	private InputStream inputStream;
}