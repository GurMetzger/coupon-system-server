package app.core.exceptions;

public class LoginManagerException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public LoginManagerException() {
		super();
	}

	public LoginManagerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LoginManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginManagerException(String message) {
		super(message);
	}

	public LoginManagerException(Throwable cause) {
		super(cause);
	}
	
}
