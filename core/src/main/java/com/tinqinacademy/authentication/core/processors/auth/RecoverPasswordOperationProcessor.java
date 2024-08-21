package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.exceptions.Errors;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordInput;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordOperation;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordOutput;
import com.tinqinacademy.authentication.core.errorhandling.ErrorMapper;
import com.tinqinacademy.authentication.core.processors.BaseOperationProcessor;
import com.tinqinacademy.authentication.domain.EmailFeignClient;
import com.tinqinacademy.authentication.persistence.entities.User;
import com.tinqinacademy.authentication.persistence.repositories.UsersRepository;
import com.tinqinacademy.email.api.operations.emaildetails.EmailDetailsOperationInput;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Slf4j
@Service
public class RecoverPasswordOperationProcessor extends BaseOperationProcessor implements RecoverPasswordOperation {
    private final EmailFeignClient emailFeignClient;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RecoverPasswordOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper, Validator validator, EmailFeignClient emailFeignClient, UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        super(conversionService, errorMapper, validator);
        this.emailFeignClient = emailFeignClient;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Either<Errors, RecoverPasswordOutput> process(RecoverPasswordInput input) {
        return Try.of(() -> {
                    log.info("Start recoverPassword input: {}", input);

                    validateInput(input);

                    String newPassword = generateNewPassword();

                    EmailDetailsOperationInput emailDetailsOperation = EmailDetailsOperationInput.builder()
                            .recipient(input.getEmail())
                            .subject("Recover your password")
                            .msgBody("Your new password is " + newPassword)
                            .build();

                    emailFeignClient.sendMail(emailDetailsOperation);

                    Optional<User> userOptional = usersRepository.findByEmail(input.getEmail());
                    User user = userOptional.get();

                    user.setPassword(passwordEncoder.encode(newPassword));

                    usersRepository.save(user);

                    RecoverPasswordOutput output = RecoverPasswordOutput.builder().build();
                    log.info("End recoverPassword output: {}", output);
                    return output;
                })
                .toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        Case($(instanceOf(IllegalArgumentException.class)), errorMapper.handleError(throwable, HttpStatus.BAD_REQUEST))
                ));
    }
}
