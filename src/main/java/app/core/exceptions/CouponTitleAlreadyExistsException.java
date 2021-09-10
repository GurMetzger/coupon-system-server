package app.core.exceptions;

public class CouponTitleAlreadyExistsException extends CompanyServiceException {

	private static final long serialVersionUID = 1L;

	public CouponTitleAlreadyExistsException() {
		super();
	}

	public CouponTitleAlreadyExistsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CouponTitleAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponTitleAlreadyExistsException(String message) {
		super(message);
	}

	public CouponTitleAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	
	
}
