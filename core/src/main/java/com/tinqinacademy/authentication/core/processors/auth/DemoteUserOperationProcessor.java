package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.exceptions.Errors;
import com.tinqinacademy.authentication.api.operations.demote.DemoteUserInput;
import com.tinqinacademy.authentication.api.operations.demote.DemoteUserOperation;
import com.tinqinacademy.authentication.api.operations.demote.DemoteUserOutput;
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
public class DemoteUserOperationProcessor extends BaseOperationProcessor implements DemoteUserOperation {

    @Autowired
    public DemoteUserOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper, Validator validator) {
        super(conversionService, errorMapper, validator);
    }

    @Override
    public Either<Errors, DemoteUserOutput> process(DemoteUserInput input) {
        return null;
    }
}
