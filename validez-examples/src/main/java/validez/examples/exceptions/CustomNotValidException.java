package validez.examples.exceptions;

public class CustomNotValidException extends Exception {

    public CustomNotValidException(String message) {
        super(message);
    }

    public CustomNotValidException() {
        super();
    }

}
