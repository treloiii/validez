package validez.lib.api.data;

import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * Class for storing result of validation and returns from {@link validez.lib.api.Validator} methods
 */
@AllArgsConstructor
public class ValidationResult {

    private final boolean valid;
    private final ValidatorContext validatorContext;
    private final Map<String, ValidatorContext> invariantContext;

    /**
     * @return is validation success or not
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @return context for invalid field
     */
    public ValidatorContext getValidatorContext() {
        return validatorContext;
    }

    /**
     * @return context for invalid invariants, which represented by Map.
     * Each key of this map contains invariant member (field) name and store this field context value.
     */
    public Map<String, ValidatorContext> getInvariantContext() {
        return invariantContext;
    }
}
