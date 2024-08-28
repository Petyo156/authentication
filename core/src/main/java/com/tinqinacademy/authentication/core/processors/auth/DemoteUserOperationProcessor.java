package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.exceptions.Errors;
import com.tinqinacademy.authentication.api.operations.demote.DemoteUserInput;
import com.tinqinacademy.authentication.api.operations.demote.DemoteUserOperation;
import com.tinqinacademy.authentication.api.operations.demote.DemoteUserOutput;
import com.tinqinacademy.authentication.core.errorhandling.ErrorMapper;
import com.tinqinacademy.authentication.core.processors.BaseOperationProcessor;
import com.tinqinacademy.authentication.persistence.entities.User;
import com.tinqinacademy.authentication.persistence.models.RoleType;
import com.tinqinacademy.authentication.persistence.repositories.UsersRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Slf4j
@Service
public class DemoteUserOperationProcessor extends BaseOperationProcessor implements DemoteUserOperation {

    private final UsersRepository usersRepository;

    @Autowired
    public DemoteUserOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper, Validator validator, UsersRepository usersRepository) {
        super(conversionService, errorMapper, validator);
        this.usersRepository = usersRepository;
    }

    @Override
    public Either<Errors, DemoteUserOutput> process(DemoteUserInput input) {
        return Try.of(() -> {
                    log.info("Start demoteUser input: {}", input);

                    validateInput(input);

                    Optional<User> loggedUserOptional = getLoggedUserOptionalIfUserExists(input);
                    User loggedUser = loggedUserOptional.get();

                    throwIfUserIsNotAdmin(loggedUser);

                    Optional<User> userOptional = getUserOptionalIfUserExists(input);
                    User user = userOptional.get();

                    throwIfUserTriesToDemoteHimself(loggedUser, user);

                    user.setRoleType(RoleType.USER);
                    usersRepository.save(user);

                    DemoteUserOutput output = DemoteUserOutput.builder().build();
                    log.info("End demoteUser output: {}", output);
                    return output;
                })
                .toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        Case($(instanceOf(IllegalArgumentException.class)), errorMapper.handleError(throwable, HttpStatus.BAD_REQUEST))
                ));
    }

    private static void throwIfUserTriesToDemoteHimself(User loggedUser, User user) {
        if (loggedUser.getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("User cannot demote himself.");
        }
    }

    private Optional<User> getUserOptionalIfUserExists(DemoteUserInput input) {
        Optional<User> userOptional = usersRepository.findByUserId(UUID.fromString(input.getUserId()));
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User does not exist.");
        }
        return userOptional;
    }

    private static void throwIfUserIsNotAdmin(User loggedUser) {
        if (loggedUser.getRoleType().equals(RoleType.USER)) {
            throw new IllegalArgumentException("Logged user does not have admin privileges.");
        }
    }

    private Optional<User> getLoggedUserOptionalIfUserExists(DemoteUserInput input) {
        Optional<User> loggedUserOptional = usersRepository.findByUserId(UUID.fromString(input.getLoggedUserId()));
        if (loggedUserOptional.isEmpty()) {
            throw new IllegalArgumentException("Logged user does not exist.");
        }
        return loggedUserOptional;
    }
}
