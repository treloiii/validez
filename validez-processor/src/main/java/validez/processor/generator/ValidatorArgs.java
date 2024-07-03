package validez.processor.generator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ValidatorArgs {

    VALIDATE_ARGS("object", "includes", "excludes");

    private final String delegateName;
    private final String includesName;
    private final String excludesName;

    public String[] args() {
        return new String[]{delegateName, includesName, excludesName};
    }

}
