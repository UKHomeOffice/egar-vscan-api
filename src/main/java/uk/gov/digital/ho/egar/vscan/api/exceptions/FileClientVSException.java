package uk.gov.digital.ho.egar.vscan.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *  
 */
@ResponseStatus(value=HttpStatus.BAD_GATEWAY)
public class FileClientVSException extends VSException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public FileClientVSException() {
	}

	/**
	 * @param message
	 */
	public FileClientVSException(String message) {
		super(message);
	}

	public FileClientVSException(String message, Exception e) {
		super(message, e);
	}


}
