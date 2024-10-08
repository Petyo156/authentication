package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.exceptions.Errors;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationInput;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationOperation;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationOutput;
import com.tinqinacademy.authentication.core.errorhandling.ErrorMapper;
import com.tinqinacademy.authentication.core.processors.BaseOperationProcessor;
import com.tinqinacademy.authentication.persistence.entities.ConfirmationCode;
import com.tinqinacademy.authentication.persistence.repositories.ConfirmationCodesRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Slf4j
@Service
public class ConfirmRegistrationOperationProcessor extends BaseOperationProcessor implements ConfirmRegistrationOperation {

    private final ConfirmationCodesRepository confirmationCodesRepository;

    @Autowired
    public ConfirmRegistrationOperationProcessor(ConversionService conversionService,
                                                 ErrorMapper errorMapper,
                                                 Validator validator,
                                                 ConfirmationCodesRepository confirmationCodesRepository) {
        super(conversionService, errorMapper, validator);
        this.confirmationCodesRepository = confirmationCodesRepository;
    }

    @Override
    public Either<Errors, ConfirmRegistrationOutput> process(ConfirmRegistrationInput input) {
        return Try.of(() -> {
                    log.info("Start confirmRegistration input: {}", input);

                    validateInput(input);

                    ConfirmationCode confirmationCode = getConfirmationCodeIfValidOrElseThrow(input);

                    confirmationCode.setUsed(true);

                    confirmationCodesRepository.save(confirmationCode);

                    ConfirmRegistrationOutput output = ConfirmRegistrationOutput.builder()
                            .build();

                    log.info("End confirmRegistration output: {}", output);
                    return output;
                })
                .toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        Case($(instanceOf(IllegalArgumentException.class)), errorMapper.handleError(throwable, HttpStatus.BAD_REQUEST))
                ));
    }

    private ConfirmationCode getConfirmationCodeIfValidOrElseThrow(ConfirmRegistrationInput input) {
        Optional<ConfirmationCode> codeOptional = confirmationCodesRepository.findByCode(input.getConfirmationCode());
        if (codeOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid confirmation code");
        }
        ConfirmationCode confirmationCode = codeOptional.get();

        if (confirmationCode.isUsed()) {
            throw new IllegalArgumentException("Confirmation code already used");
        }
        return confirmationCode;
    }
}