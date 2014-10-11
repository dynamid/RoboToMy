package  fr.insa_lyon.citi.robotomy.tools;

public abstract class AssertUtils {
	/**
	 * verify that the object is not null. If it is, throws a new
	 * IllegalStateException with the given message
	 * 
	 * @param object
	 * @param message
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalStateException(message);
		}
	}

	public static void fail(String message) {
		throw new IllegalStateException(message);
	}

}
