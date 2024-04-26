package validez.lib.annotation.conditions;

/**
 * Annotation for indicate fields that are part of {@link Invariant}
 * @see Invariant
 */
public @interface Fields {

    /**
     * @return Array of fields that forms invariant
     */
    String[] value();

}
