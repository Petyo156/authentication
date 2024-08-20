package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.exceptions.Errors;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordInput;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordOperation;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordOutput;
import com.tinqinacademy.authentication.core.errorhandling.ErrorMapper;
import com.tinqinacademy.authentication.core.processors.BaseOperationProcessor;
import com.tinqinacademy.authentication.persistence.entities.User;
import com.tinqinacademy.authentication.persistence.repositories.UsersRepository;
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
public class ChangePasswordOperationProcessor extends BaseOperationProcessor implements ChangePasswordOperation {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ChangePasswordOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper, Validator validator,
                                            UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        super(conversionService, errorMapper, validator);
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Either<Errors, ChangePasswordOutput> process(ChangePasswordInput input) {
        return Try.of(() -> {
                    log.info("Start demoteUser input: {}", input);

                    validateInput(input);

                    User user = getUserIfExistsOrElseThrow(input);

                    throwIfOldPassIsInvalid(input, user);
                    user.setPassword(passwordEncoder.encode(input.getNewPassword()));

                    usersRepository.save(user);

                    ChangePasswordOutput output = ChangePasswordOutput.builder().build();

                    log.info("End demoteUser output: {}", output);
                    return output;
                })
                .toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        Case($(instanceOf(IllegalArgumentException.class)), errorMapper.handleError(throwable, HttpStatus.BAD_REQUEST))
                ));
    }

    private void throwIfOldPassIsInvalid(ChangePasswordInput input, User user) {
        if(!passwordEncoder.matches(input.getOldPassword(), user.getPassword())){
            throw new IllegalArgumentException("Invalid password.");
        }
    }

    private User getUserIfExistsOrElseThrow(ChangePasswordInput input) {
        Optional<User> userOptional = usersRepository.findByEmail(input.getEmail());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with given email does not exist.");
        }
        return userOptional.get();
    }
}
