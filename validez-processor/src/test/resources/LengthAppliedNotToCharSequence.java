package test;

import validez.lib.annotation.Validate;
import validez.lib.annotation.validators.Length;

import java.util.concurrent.CompletableFuture;

@Validate
public class LengthAppliedNotToCharSequence {

    @Length(max = 100)
    private CompletableFuture future;

    public CompletableFuture getFuture() {
        return future;
    }
}