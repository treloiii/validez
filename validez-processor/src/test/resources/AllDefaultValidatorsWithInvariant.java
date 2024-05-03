package test;

import validez.lib.annotation.Validate;
import validez.lib.annotation.conditions.Fields;
import validez.lib.annotation.conditions.Invariant;
import validez.lib.annotation.validators.IntBound;
import validez.lib.annotation.validators.IntRange;
import validez.lib.annotation.validators.Length;
import validez.lib.annotation.validators.LongRange;
import validez.lib.annotation.validators.NotEmpty;
import validez.lib.annotation.validators.NotNull;
import validez.lib.annotation.validators.StringRange;

import java.util.List;

@Validate
@Invariant(name = "inv1", members = {@Fields("s1"), @Fields("s2")})
public class AllDefaultValidatorsWithInvariant {

    @NotNull
    private String s1;
    @NotEmpty
    private String s2;
    @NotEmpty
    private List<String> l1;
    @StringRange({"A", "B"})
    private String s3;
    @Length(min = 10, max = 30)
    private String s4;
    @Length(equals = 111)
    private String s5;
    @IntBound(max = 40)
    private int i1;
    @IntBound(min = 3)
    private Integer i2;
    @IntRange({1, 4, 5})
    private Integer i3;
    @LongRange({4L, 5L})
    private Long ln1;

    public String getS1() {
        return s1;
    }

    public String getS2() {
        return s2;
    }

    public List<String> getL1() {
        return l1;
    }

    public String getS3() {
        return s3;
    }

    public String getS4() {
        return s4;
    }

    public String getS5() {
        return s5;
    }

    public int getI1() {
        return i1;
    }

    public Integer getI2() {
        return i2;
    }

    public Integer getI3() {
        return i3;
    }

    public Long getLn1() {
        return ln1;
    }
}