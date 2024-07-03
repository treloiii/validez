package validez.processor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public static String deCapitalize(String val) {
        char[] chars = val.toCharArray();
        char loweredFirst = Character.toLowerCase(chars[0]);
        chars[0] = loweredFirst;
        return new String(chars);
    }

}
