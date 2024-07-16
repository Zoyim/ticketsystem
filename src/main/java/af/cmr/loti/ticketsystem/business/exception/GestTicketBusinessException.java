package af.cmr.loti.ticketsystem.business.exception;


/**
 * This exception class represents a custom business exception specific to the
 * Ticket System application. It extends the standard Exception class and provides
 * constructors to create exceptions with a message and an optional cause.
 */
public class GestTicketBusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6727006059974582763L;
	
	/**
	 * Constructs a new GestTicketBusinessException with the specified detail message
	 * and cause.
	 * 
	 * @param message The detail message (which is saved for later retrieval by the
	 *                getMessage() method)
	 * @param cause   The cause (which is saved for later retrieval by the
	 *                getCause() method)
	 */
	public GestTicketBusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new GestTicketBusinessException with the specified detail message.
	 * 
	 * @param message The detail message (which is saved for later retrieval by the
	 *                getMessage() method)
	 */
	public GestTicketBusinessException(String message) {
		super(message);
	}
}
