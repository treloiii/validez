package validez.processor.generator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ValidatorArgs {

    VALIDATE_ARGS("object", "includes",
            "excludes", "messageHandler_");

    private final String delegateName;
    private final String includesName;
    private final String excludesName;
    private final String messageHandlerName;

    public String[] args() {
        return new String[]{delegateName, includesName, excludesName};
    }

}
