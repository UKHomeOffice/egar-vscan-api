package uk.gov.digital.ho.egar.vscan.api.exceptions;

public class UnsupportedClientException extends DataNotFoundVSException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedClientException (final String message) {
		super(message);
	}
}
