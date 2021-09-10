package app.core.exceptions;

public class CouponAlreadyOwnedException extends CustomerServiceException {

	private static final long serialVersionUID = 1L;

	public CouponAlreadyOwnedException() {
		super();
	}

	public CouponAlreadyOwnedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CouponAlreadyOwnedException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponAlreadyOwnedException(String message) {
		super(message);
	}

	public CouponAlreadyOwnedException(Throwable cause) {
		super(cause);
	}

	
	
}
