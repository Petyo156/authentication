package com.tinqinacademy.authentication.api.base;

import com.tinqinacademy.authentication.api.exceptions.Errors;
import io.vavr.control.Either;

public interface OperationProcessor<S extends OperationInput, T extends OperationOutput> {
    Either<Errors, T> process(S input);
}
