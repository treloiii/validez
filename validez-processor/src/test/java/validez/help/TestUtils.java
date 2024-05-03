package validez.help;

import javax.tools.JavaFileObject;
import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TestUtils {

    public static void assertEqualsJavaFileObjects(JavaFileObject expected, JavaFileObject actual)
            throws IOException {
        try (BufferedReader expectedReader = new BufferedReader(expected.openReader(true));
             BufferedReader actualReader = new BufferedReader(actual.openReader(true))) {
            int generatedDateIndex = -1;
            int index = 0;
            String expectedLine;
            while ((expectedLine = expectedReader.readLine()) != null) {
                String actualLine = actualReader.readLine();
                if (actualLine.contains("@Generated")) {
                    generatedDateIndex = index + 2;
                }
                if (generatedDateIndex != index) {
                    assertEquals(expectedLine.trim(), actualLine.trim());
                }
                index++;
            }
        }
    }

    public static void printJavaFileObject(JavaFileObject object) throws IOException {
        try (BufferedReader reader = new BufferedReader(object.openReader(true))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

}
