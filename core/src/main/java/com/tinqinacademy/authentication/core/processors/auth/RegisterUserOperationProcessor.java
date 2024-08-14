package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.exceptions.Errors;
import com.tinqinacademy.authentication.api.operations.registeruser.RegisterUserInput;
import com.tinqinacademy.authentication.api.operations.registeruser.RegisterUserOperation;
import com.tinqinacademy.authentication.api.operations.registeruser.RegisterUserOutput;
import com.tinqinacademy.authentication.core.errorhandling.ErrorMapper;
import com.tinqinacademy.authentication.core.processors.BaseOperationProcessor;
import com.tinqinacademy.authentication.persistence.entities.ConfirmationCode;
import com.tinqinacademy.authentication.persistence.entities.User;
import com.tinqinacademy.authentication.persistence.repositories.ConfirmationCodesRepository;
import com.tinqinacademy.authentication.persistence.repositories.UsersRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Slf4j
@Service
public class RegisterUserOperationProcessor extends BaseOperationProcessor implements RegisterUserOperation {

    private final UsersRepository usersRepository;
    private final ConfirmationCodesRepository confirmationCodesRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterUserOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper, Validator validator, UsersRepository usersRepository, ConfirmationCodesRepository confirmationCodesRepository, PasswordEncoder passwordEncoder) {
        super(conversionService, errorMapper, validator);
        this.usersRepository = usersRepository;
        this.confirmationCodesRepository = confirmationCodesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Either<Errors, RegisterUserOutput> process(RegisterUserInput input) {
        return Try.of(() -> {
                    log.info("Start registerUser input: {}", input);

                    Optional<User> existingUser = usersRepository.findByUsername(input.getUsername());
                    if (existingUser.isPresent()) {
                        throw new IllegalArgumentException("User already exists");
                    }

                    User user = User.builder()
                            .firstName(input.getFirstName())
                            .lastName(input.getLastName())
                            .username(input.getUsername())
                            .password(passwordEncoder.encode(input.getPassword()))
                            .email(input.getEmail())
                            .build();

                    usersRepository.save(user);

                    ConfirmationCode confirmationCode = ConfirmationCode.builder()
                            .code(generateConfirmationCode())
                            .userId(user.getUserId())
                            .build();

                    confirmationCodesRepository.save(confirmationCode);

                    RegisterUserOutput output = RegisterUserOutput.builder()
                            .id(user.getUserId())
                            .build();

                    log.info("End registerUser output: {}", output);
                    return output;
                })
                .toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        Case($(instanceOf(IllegalArgumentException.class)),
                                errorMapper.handleError(throwable, HttpStatus.BAD_REQUEST))
                ));
    }
}
