package validez.help;

import java.util.concurrent.ThreadLocalRandom;

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

}
