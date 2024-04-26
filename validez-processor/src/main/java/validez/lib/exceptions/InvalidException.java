package validez.lib.exceptions;

/**
 * Default exception which validator implements
 * when no {@link validez.lib.annotation.ValidatorThrows}
 * or validez.properties provided
 */
public class InvalidException extends Exception {

    public InvalidException(String message) {
        super(message);
    }

    public InvalidException() {
        super();
    }

}
