package com.tinqinacademy.authentication.core.processors.auth;

import com.tinqinacademy.authentication.api.exceptions.Errors;
import com.tinqinacademy.authentication.api.operations.login.LoginUserInput;
import com.tinqinacademy.authentication.api.operations.login.LoginUserOperation;
import com.tinqinacademy.authentication.api.operations.login.LoginUserOutput;
import com.tinqinacademy.authentication.core.errorhandling.ErrorMapper;
import com.tinqinacademy.authentication.core.processors.BaseOperationProcessor;
import com.tinqinacademy.authentication.core.security.JwtUtil;
import com.tinqinacademy.authentication.persistence.entities.ConfirmationCode;
import com.tinqinacademy.authentication.persistence.entities.User;
import com.tinqinacademy.authentication.persistence.repositories.ConfirmationCodesRepository;
import com.tinqinacademy.authentication.persistence.repositories.UsersRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Slf4j
@Service
public class LoginUserOperationProcessor extends BaseOperationProcessor implements LoginUserOperation {

    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;
    private final ConfirmationCodesRepository confirmationCodesRepository;

    @Autowired
    public LoginUserOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper, Validator validator,
                                       JwtUtil jwtUtil, UsersRepository usersRepository, ConfirmationCodesRepository confirmationCodesRepository) {
        super(conversionService, errorMapper, validator);
        this.jwtUtil = jwtUtil;
        this.usersRepository = usersRepository;
        this.confirmationCodesRepository = confirmationCodesRepository;
    }

    @Override
    public Either<Errors, LoginUserOutput> process(LoginUserInput input) {
        return Try.of(() -> {
                    log.info("Start loginUser input: {}", input);

                    Optional<User> userOptional = usersRepository.findByUsername(input.getUsername());
                    if(userOptional.isEmpty()){
                        throw new IllegalArgumentException("User not found");
                    }

                    User user = userOptional.get();

                    Optional<ConfirmationCode> confirmationCodeOptional = confirmationCodesRepository.findByUserId(user.getUserId());
                    if(confirmationCodeOptional.isEmpty()){
                        throw new IllegalArgumentException("Confirmation code is not found");
                    }

                    ConfirmationCode confirmationCode = confirmationCodeOptional.get();
                    if(!confirmationCode.isUsed()){
                        throw new IllegalArgumentException("Confirmation code is not used");
                    }

                    Map<String, String> claims = new HashMap<>();
                    claims.put("user_id", user.getUserId().toString());
                    claims.put("username", user.getUsername());

                    String jwt = jwtUtil.generateToken(claims);
                    LoginUserOutput output = LoginUserOutput.builder()
                            .token(jwt)
                            .build();

                    log.info("End loginUser output: {}", output);
                    return output;
                })
                .toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        Case($(instanceOf(IllegalArgumentException.class)),
                                errorMapper.handleError(throwable, HttpStatus.BAD_REQUEST))
                ));
    }
}
