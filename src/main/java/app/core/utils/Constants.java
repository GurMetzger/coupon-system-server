package app.core.utils;

public final class Constants {
	
	// Coupon Cleanup Job's Deadtime in Milliseconds
	public static final long JOB_DEADTIME_IN_MILLIS = 86_400_000; 
	
	// Login Failure Message
	public static final String LOGIN_FAIL_MESSAGE = "Incorrect Email and/or Password! Try Again...";
	
	// Email Regex Pattern [ ___@__.com ]
	public static final String EMAIL_PATTERN = "^((\"[\\w-\\s]+\")|([\\w-]+(?:\\.[\\w-]+)"
			+ "*)|(\"[\\w-\\s]+\")([\\w-]+(?:\\.[\\w-]+)*))(@((?:[\\w-]+\\.)*\\w[\\"
			+ "w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$)|(@\\[?((25[0-5]\\.|2[0-4"
			+ "][0-9]\\.|1[0-9]{2}\\.|[0-9]{1,2}\\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2"
			+ "}|[0-9]{1,2})\\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\]?$)";
	
	// Password Minimum Length
	public static final int PASSWORD_MIN_LENGTH = 5;
	
	// Coupon Images' Storage Directory
	public static final String IMAGE_STORAGE_DIR = "src/main/resources/static/pics";
	
}
