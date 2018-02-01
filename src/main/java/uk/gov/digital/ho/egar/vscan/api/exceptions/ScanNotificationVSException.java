package uk.gov.digital.ho.egar.vscan.api.exceptions;

/**
 *  
 */
public class ScanNotificationVSException extends VSException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public ScanNotificationVSException() {
	}

	/**
	 * @param message
	 */
	public ScanNotificationVSException(String message) {
		super(message);
	}

	public ScanNotificationVSException(String message, Exception e) {
		super(message, e);
	}


}
