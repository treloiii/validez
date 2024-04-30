import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.NotEmpty;

@Validate
public class NoPackage {

    @NotEmpty
    private String name;

    public String getName() {
        return this.name;
    }

}