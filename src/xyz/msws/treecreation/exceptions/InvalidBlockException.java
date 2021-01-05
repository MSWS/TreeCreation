package xyz.msws.treecreation.exceptions;

/**
 * Called when a block is attempted to be used with invalid or missing data
 * 
 * @author imodm
 *
 */
public class InvalidBlockException extends Exception {
	private static final long serialVersionUID = 5194053330263180123L;

	public InvalidBlockException(String msg) {
		super(msg);
	}

}
