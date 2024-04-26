package validez.lib.annotation.validators;

/**
 * Defines the strategy by which it will be determined what to do with the field value if it is null
 */
public enum NullValueStrategy {

    /**
     * Tells processor what null value is not valid, and validation must fail
     */
    NULL_NOT_ALLOWED,
    /**
     * Tells processor what null value allowed, so it will end validation for field and accept it as valid
     */
    NULL_ALLOWED

}
