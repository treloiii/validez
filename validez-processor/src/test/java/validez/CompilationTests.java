package validez;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import validez.processor.ValidatorProcessor;
import validez.processor.config.ConfigHolder;

import java.util.HashMap;
import java.util.stream.Stream;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

class CompilationTests {

    @BeforeEach
    void setup() {
        ConfigHolder.config = new HashMap<>();
    }

    @Test
    void noPackageProvided() {
        Compilation compilation = javac()
                .withProcessors(new ValidatorProcessor())
                .compile(
                        JavaFileObjects.forResource("NoPackage.java")
                );
        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining("Class NoPackage doesn't have package");
    }

    @ParameterizedTest
    @ValueSource(strings = {"RegisterWithoutInterface.java", "RegisterWithoutTypeParameter.java"})
    void noValidateNotFailed(String resource) {
        Compilation compilation = javac()
                .withProcessors(new ValidatorProcessor())
                .compile(
                        JavaFileObjects.forResource(resource)
                );
        assertThat(compilation).succeeded();
    }

    @Test
    void emptyValidatorsValidateNotFails() {
        Compilation compilation = javac()
                .withProcessors(new ValidatorProcessor())
                .compile(
                        JavaFileObjects.forResource("NotFailingEmptyValidate.java")
                );
        assertThat(compilation).succeeded();
    }

    public static Stream<Arguments> registeredIncorrectSource() {
        return Stream.of(
                Arguments.of("RegisterWithoutInterface.java", "Class marked with @Register must implement" +
                        " validez.lib.api.external.ExternalValidator interface"),
                Arguments.of("RegisterWithoutTypeParameter.java", "Class marked with @Register " +
                        "must implement validez.lib.api.external.ExternalValidator interface with type parameter")

        );
    }

    @ParameterizedTest
    @MethodSource("registeredIncorrectSource")
    void registeredIncorrect(String resource, String error) {
        Compilation compilation = javac()
                .withProcessors(new ValidatorProcessor())
                .compile(
                        JavaFileObjects.forResource(resource),
                        JavaFileObjects.forResource("NotFailingEmptyValidate.java")
                );
        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining(error);
    }

    @Test
    void registerCorrect() {
        Compilation compilation = javac()
                .withProcessors(new ValidatorProcessor())
                .compile(
                        JavaFileObjects.forResource("RegisterCorrect.java"),
                        JavaFileObjects.forResource("NotFailingEmptyValidate.java")
                );
        assertThat(compilation).succeeded();
    }

    @Test
    void allDefaultValidatorsCompiles() {
        Compilation compilation = javac()
                .withProcessors(new ValidatorProcessor())
                .compile(
                        JavaFileObjects.forResource("AllDefaultValidatorsCompile.java")
                );
        compilation.generatedFiles();
        assertThat(compilation).succeeded();
    }

    @Test
    void allDefaultValidatorsWithInvariantCompiles() {
        Compilation compilation = javac()
                .withProcessors(new ValidatorProcessor())
                .compile(
                        JavaFileObjects.forResource("AllDefaultValidatorsWithInvariant.java")
                );
        assertThat(compilation).succeeded();
    }

    @Test
    void notFailingNoValidatorsFields() {
        Compilation compilation = javac()
                .withProcessors(new ValidatorProcessor())
                .compile(
                        JavaFileObjects.forResource("NotFailingNoValidatorsFields.java")
                );
        assertThat(compilation).succeeded();
    }

    @Test
    void excludeNotFailing() {
        Compilation compilation = javac()
                .withProcessors(new ValidatorProcessor())
                .compile(
                        JavaFileObjects.forResource("ExcludeNotFailing.java"),
                        JavaFileObjects.forResource("NotFailingNoValidatorsFields.java")
                );
        assertThat(compilation).succeeded();
    }

    public static Stream<Arguments> incorrectConsumesSource() {
        return Stream.of(
                Arguments.of("ByteBoundAppliedNotToByte.java", "@ByteBound can be placed only on Byte value types or it subtypes"),
                Arguments.of("ShortBoundAppliedNotToShort.java", "@ShortBound can be placed only on Short value types or it subtypes"),
                Arguments.of("IntBoundAppliedNotToInteger.java", "@IntBound can be placed only on Integer value types or it subtypes"),
                Arguments.of("LongBoundAppliedNotToLong.java", "@LongBound can be placed only on Long value types or it subtypes"),
                Arguments.of("IntRangeAppliedNotToInt.java", "@IntRange can be placed only on Integer value types or it subtypes"),
                Arguments.of("LongRangeAppliedNotToLong.java", "@LongRange can be placed only on Long value types or it subtypes"),
                Arguments.of("StringRangeAppliedNotToString.java", "@StringRange can be placed only on String value types or it subtypes"),
                Arguments.of("LengthAppliedNotToCharSequence.java", "@Length can be placed only on CharSequence value types or it subtypes")
        );
    }

    @ParameterizedTest
    @MethodSource("incorrectConsumesSource")
    void incorrectConsumesTest(String resource, String error) {
        Compilation compilation = javac()
                .withProcessors(new ValidatorProcessor())
                .compile(
                        JavaFileObjects.forResource(resource)
                );
        assertThat(compilation).failed();
        assertThat(compilation)
                .hadErrorContaining(error);
    }

}
