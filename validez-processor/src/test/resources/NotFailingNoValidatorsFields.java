package test;

import validez.lib.annotation.Validate;

@Validate
public class NotFailingNoValidatorsFields {

    private String one;
    private Integer two;
    private Long three;
    private Object object;

}