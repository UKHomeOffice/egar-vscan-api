package uk.gov.digital.ho.egar.vscan.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *  
 */
@ResponseStatus(value=HttpStatus.BAD_GATEWAY)
public class ScanClientVSException extends VSException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public ScanClientVSException() {
	}

	/**
	 * @param message
	 */
	public ScanClientVSException(String message) {
		super(message);
	}

	public ScanClientVSException(String message, Exception e) {
		super(message, e);
	}


}
