package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.exceptions.Errors;
import com.tinqinacademy.authentication.api.operations.validateuser.ValidateUserInput;
import com.tinqinacademy.authentication.api.operations.validateuser.ValidateUserOperation;
import com.tinqinacademy.authentication.api.operations.validateuser.ValidateUserOutput;
import com.tinqinacademy.authentication.core.errorhandling.ErrorMapper;
import com.tinqinacademy.authentication.core.processors.BaseOperationProcessor;
import com.tinqinacademy.authentication.core.security.JwtUtil;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidateUserOperationProcessor extends BaseOperationProcessor implements ValidateUserOperation {
    private final JwtUtil jwtUtil;

    @Autowired
    public ValidateUserOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper, Validator validator, JwtUtil jwtUtil) {
        super(conversionService, errorMapper, validator);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Either<Errors, ValidateUserOutput> process(ValidateUserInput input) {
        return Try.of(() -> {
                    log.info("Start validateUser input: {}", input);

                    validateInput(input);

                    Boolean isValid = jwtUtil.isTokenValid(input.getToken());

                    ValidateUserOutput output = ValidateUserOutput.builder()
                            .validity(isValid)
                            .build();

                    log.info("End validateUser output: {}", output);
                    return output;
                })
                .toEither()
                .mapLeft(throwable -> errorMapper.handleError(throwable, HttpStatus.BAD_REQUEST));
    }
}