package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.exceptions.Errors;
import com.tinqinacademy.authentication.api.operations.promote.PromoteUserInput;
import com.tinqinacademy.authentication.api.operations.promote.PromoteUserOperation;
import com.tinqinacademy.authentication.api.operations.promote.PromoteUserOutput;
import com.tinqinacademy.authentication.core.errorhandling.ErrorMapper;
import com.tinqinacademy.authentication.core.processors.BaseOperationProcessor;
import io.vavr.control.Either;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PromoteUserOperationProcessor extends BaseOperationProcessor implements PromoteUserOperation {

    @Autowired
    public PromoteUserOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper, Validator validator) {
        super(conversionService, errorMapper, validator);
    }

    @Override
    public Either<Errors, PromoteUserOutput> process(PromoteUserInput input) {
        return null;
    }
}
