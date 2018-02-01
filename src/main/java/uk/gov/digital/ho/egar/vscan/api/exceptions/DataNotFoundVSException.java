package uk.gov.digital.ho.egar.vscan.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *  
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST) 
public abstract class DataNotFoundVSException extends VSException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public DataNotFoundVSException() {
	}

	/**
	 * @param message
	 */
	public DataNotFoundVSException(String message) {
		super(message);
	}

}
