package af.cmr.loti.ticketsystem.business.entity;

public class ApiResponse<T> {
	
	private int status;
	private String message;
	private Object result;
	
	/**
	 * @param status
	 * @param message
	 * @param result
	 */
	public ApiResponse(int status, String message, Object result) {
		super();
		this.status = status;
		this.message = message;
		this.result = result;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}

}
