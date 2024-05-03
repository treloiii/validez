package test;

import validez.lib.annotation.Validate;
import validez.lib.annotation.conditions.Exclude;

@Validate
public class ExcludeNotFailing {

    @Exclude
    private NotFailingNoValidatorsFields excluded;

}