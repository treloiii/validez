package validez.examples.custom.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface AllPossibleTypes {

    int integer() default 30;

    double double_() default 34.0;

    long long_() default 3333L;

    float float_() default 3.45F;

    boolean boolean_() default false;

    byte byte_() default (byte) 123;

    int[] integers() default {1, 2, 3};

    double[] doubles() default {3.0, 4.0};

    long[] longs() default {1L, 2L, 45L, 334L};

    float[] floats() default {0.00f, 0.32f, 1.18f};

    boolean[] booleans() default {false, false, true, true};

    byte[] bytes() default {(byte) 34, (byte) 35, (byte) 99};

    Class<?> clazz() default Integer.class;

    Class<?>[] classes() default {String.class, Integer.class};

    String string() default "default val";

    String[] stringArray() default {"one s", "two s", "three s"};

    DumbEnum enum_() default DumbEnum.TWO;

    DumbEnum[] enumArray() default {DumbEnum.ONE, DumbEnum.TWO};

    InternalAnnotation annotation() default @InternalAnnotation("is");

    InternalAnnotation[] annotationsArray() default {@InternalAnnotation, @InternalAnnotation("not default")};

    SuperInternalAnnotation[] annotationsArrayOfAnnotations() default {
            @SuperInternalAnnotation({
                    @InternalAnnotation("C"),
                    @InternalAnnotation("D")
            }),
            @SuperInternalAnnotation
    };

    enum DumbEnum {
        ONE,
        TWO
    }

    @interface InternalAnnotation {
        String value() default "internal value";
    }

    @interface SuperInternalAnnotation {

        InternalAnnotation[] value() default {@InternalAnnotation("A"), @InternalAnnotation("B")};

    }

}
