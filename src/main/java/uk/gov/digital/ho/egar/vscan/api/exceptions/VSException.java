package uk.gov.digital.ho.egar.vscan.api.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

import uk.gov.digital.ho.egar.shared.util.exceptions.NoCallStackException;

/**
 * A base exception type that does not pick uo the stack trace.
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY) // Suppress empty arrays & nulls
public class VSException extends NoCallStackException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VSException() {
		this(null,null);		
	}

	public VSException(String message) {
		this(message,null);
	}

	public VSException(Throwable cause) {
		this(null,cause);
	}

	public VSException(String message, Throwable cause) {
		super(message, cause);
        }

}

