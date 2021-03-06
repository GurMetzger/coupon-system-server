package app.core.exceptions;

public class ClientServiceException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public ClientServiceException() {
		super();
	}

	public ClientServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ClientServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientServiceException(String message) {
		super(message);
	}

	public ClientServiceException(Throwable cause) {
		super(cause);
	}
	
	
	
}
