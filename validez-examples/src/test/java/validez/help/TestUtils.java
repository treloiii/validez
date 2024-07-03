package validez.help;

import validez.lib.api.data.ValidationResult;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtils {

    public static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static String stringOfLen(int len) {
        byte[] byteArr = new byte[len];
        for (int i = 0; i < len; i++) {
            int intC = RANDOM.nextInt('Z', 'a');
            byteArr[i] = (byte) intC;
        }
        return new String(byteArr);
    }

    public static String stringOfRandomLen(int min, int max) {
        int len = RANDOM.nextInt(min, max);
        return stringOfLen(len);
    }

    @SafeVarargs
    public static <T> T randomFrom(T...values) {
        int index = RANDOM.nextInt(0, values.length);
        return values[index];
    }

    public static void assertNotValid(Supplier<ValidationResult> resultSupplier) {
        assertFalse(resultSupplier.get().isValid());
    }

    public static void assertValid(Supplier<ValidationResult> resultSupplier) {
        assertTrue(resultSupplier.get().isValid());
    }

}
