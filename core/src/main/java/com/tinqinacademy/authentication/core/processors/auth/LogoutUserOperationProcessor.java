package com.tinqinacademy.authentication.core.processors.auth;


import com.tinqinacademy.authentication.api.exceptions.Errors;
import com.tinqinacademy.authentication.api.operations.logoutuser.LogoutUserInput;
import com.tinqinacademy.authentication.api.operations.logoutuser.LogoutUserOperation;
import com.tinqinacademy.authentication.api.operations.logoutuser.LogoutUserOutput;
import com.tinqinacademy.authentication.core.errorhandling.ErrorMapper;
import com.tinqinacademy.authentication.core.processors.BaseOperationProcessor;
import com.tinqinacademy.authentication.persistence.entities.BlacklistToken;
import com.tinqinacademy.authentication.persistence.repositories.BlacklistTokenRepository;
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
public class LogoutUserOperationProcessor extends BaseOperationProcessor implements LogoutUserOperation {
    private final BlacklistTokenRepository blackListTokenRepository;

    @Autowired
    public LogoutUserOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper, Validator validator, BlacklistTokenRepository blackListTokenRepository) {
        super(conversionService, errorMapper, validator);
        this.blackListTokenRepository = blackListTokenRepository;
    }

    @Override
    public Either<Errors, LogoutUserOutput> process(LogoutUserInput input) {
        return Try.of(() -> {
                    log.info("Start logoutUser input: {}", input);

                    validateInput(input);

                    checkIfTokenIsAlreadyBlacklisted(input);

                    BlacklistToken blacklistToken = conversionService.convert(input, BlacklistToken.class);
                    blackListTokenRepository.save(blacklistToken);

                    LogoutUserOutput output = LogoutUserOutput.builder().build();
                    log.info("End logoutUser output: {}", output);
                    return output;
                })
                .toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        Case($(instanceOf(IllegalArgumentException.class)), ex -> errorMapper.handleError(ex, HttpStatus.BAD_REQUEST))
                ));
    }

    private void checkIfTokenIsAlreadyBlacklisted(LogoutUserInput input){
        Optional<BlacklistToken> token = blackListTokenRepository.findByToken(input.getToken());
        if (token.isPresent()){
            throw new IllegalArgumentException("User has already logged out");
        }
    }
}