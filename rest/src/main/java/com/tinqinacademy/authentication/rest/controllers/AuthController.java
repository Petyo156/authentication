package com.tinqinacademy.authentication.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.authentication.api.apimapping.RestApiMappingAuthentication;
import com.tinqinacademy.authentication.api.exceptions.Errors;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordInput;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationInput;
import com.tinqinacademy.authentication.api.operations.demote.DemoteUserInput;
import com.tinqinacademy.authentication.api.operations.login.LoginUserInput;
import com.tinqinacademy.authentication.api.operations.logoutuser.LogoutUserInput;
import com.tinqinacademy.authentication.api.operations.logoutuser.LogoutUserOutput;
import com.tinqinacademy.authentication.api.operations.promote.PromoteUserInput;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordInput;
import com.tinqinacademy.authentication.api.operations.registeruser.RegisterUserInput;
import com.tinqinacademy.authentication.api.operations.validateuser.ValidateUserInput;
import com.tinqinacademy.authentication.api.operations.validateuser.ValidateUserOutput;
import com.tinqinacademy.authentication.core.processors.auth.*;
import com.tinqinacademy.authentication.rest.context.LoggedUser;
import io.vavr.control.Either;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController extends BaseController {

    private final ChangePasswordOperationProcessor changePasswordOperationProcessor;
    private final ConfirmRegistrationOperationProcessor confirmRegistrationOperationProcessor;
    private final DemoteUserOperationProcessor demoteUserOperationProcessor;
    private final LoginUserOperationProcessor loginUserOperationProcessor;
    private final PromoteUserOperationProcessor promoteUserOperationProcessor;
    private final RecoverPasswordOperationProcessor recoverPasswordOperationProcessor;
    private final RegisterUserOperationProcessor registerUserOperationProcessor;
    private final ValidateUserOperationProcessor validateUserOperationProcessor;
    private final LogoutUserOperationProcessor logoutUserOperationProcessor;

    private final LoggedUser loggedUser;

    private final ObjectMapper objectMapper;

    @Autowired
    public AuthController(ChangePasswordOperationProcessor changePasswordOperationProcessor, ConfirmRegistrationOperationProcessor confirmRegistrationOperationProcessor,
                          DemoteUserOperationProcessor demoteUserOperationProcessor, LoginUserOperationProcessor loginUserOperationProcessor, PromoteUserOperationProcessor promoteUserOperationProcessor,
                          RecoverPasswordOperationProcessor recoverPasswordOperationProcessor, RegisterUserOperationProcessor registerUserOperationProcessor, ValidateUserOperationProcessor validateUserOperationProcessor, LogoutUserOperationProcessor logoutUserOperationProcessor, LoggedUser loggedUser, ObjectMapper objectMapper) {
        this.changePasswordOperationProcessor = changePasswordOperationProcessor;
        this.confirmRegistrationOperationProcessor = confirmRegistrationOperationProcessor;
        this.demoteUserOperationProcessor = demoteUserOperationProcessor;
        this.loginUserOperationProcessor = loginUserOperationProcessor;
        this.promoteUserOperationProcessor = promoteUserOperationProcessor;
        this.recoverPasswordOperationProcessor = recoverPasswordOperationProcessor;
        this.registerUserOperationProcessor = registerUserOperationProcessor;
        this.validateUserOperationProcessor = validateUserOperationProcessor;
        this.logoutUserOperationProcessor = logoutUserOperationProcessor;
        this.loggedUser = loggedUser;
        this.objectMapper = objectMapper;
    }

    @PostMapping(RestApiMappingAuthentication.POST_CHANGEPASSWORD_PATH)
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordInput input) {

        ChangePasswordInput changePasswordInput = ChangePasswordInput.builder()
                .oldPassword(input.getOldPassword())
                .newPassword(input.getNewPassword())
                .email(input.getEmail())
                .build();

        return handleOperation(changePasswordOperationProcessor.process(changePasswordInput), HttpStatus.BAD_REQUEST);
    }

    @PostMapping(RestApiMappingAuthentication.POST_CONFIRMREGISTRATION_PATH)
    public ResponseEntity<?> confirmRegistration(
            @Valid @RequestBody ConfirmRegistrationInput input) {

        ConfirmRegistrationInput confirmRegistrationInput = ConfirmRegistrationInput.builder()
                .confirmationCode(input.getConfirmationCode())
                .build();

        return handleOperation(confirmRegistrationOperationProcessor.process(confirmRegistrationInput), HttpStatus.BAD_REQUEST);
    }

    @PostMapping(RestApiMappingAuthentication.POST_DEMOTE_PATH)
    public ResponseEntity<?> demoteUser(
            @Valid @RequestBody DemoteUserInput input) {

        DemoteUserInput demoteUserInput = input.toBuilder()
                .userId(input.getUserId())
                .loggedUserId(loggedUser.getLoggedUser().getUserId().toString())
                .build();

        return handleOperation(demoteUserOperationProcessor.process(demoteUserInput), HttpStatus.BAD_REQUEST);
    }

    @PostMapping(RestApiMappingAuthentication.POST_LOGIN_PATH)
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginUserInput input) {
        return loginUserOperationProcessor.process(input)
                .map(output -> ResponseEntity.ok()
                        .header("Authorization", "Bearer " + output.getToken())
                        .build())
                .getOrElseGet(error -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error));
    }

    @PostMapping(RestApiMappingAuthentication.POST_PROMOTE_PATH)
    public ResponseEntity<?> promoteUser(
            @Valid @RequestBody PromoteUserInput input) {

        PromoteUserInput promoteUserInput = PromoteUserInput.builder()
                .userId(input.getUserId())
                .loggedUserId(loggedUser.getLoggedUser().getUserId().toString())
                .build();

        return handleOperation(promoteUserOperationProcessor.process(promoteUserInput), HttpStatus.BAD_REQUEST);
    }

    @PostMapping(RestApiMappingAuthentication.POST_RECOVERPASSWORD_PATH)
    public ResponseEntity<?> recoverPassword(
            @Valid @RequestBody RecoverPasswordInput input) {

        RecoverPasswordInput recoverPasswordInput = RecoverPasswordInput.builder()
                .email(input.getEmail())
                .build();

        return handleOperation(recoverPasswordOperationProcessor.process(recoverPasswordInput), HttpStatus.BAD_REQUEST);
    }

    @PostMapping(RestApiMappingAuthentication.POST_REGISTER_PATH)
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterUserInput input) {

        RegisterUserInput registerUserInput = RegisterUserInput.builder()
                .username(input.getUsername())
                .password(input.getPassword())
                .birthDate(input.getBirthDate())
                .phoneNumber(input.getPhoneNumber())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getEmail())
                .build();

        return handleOperation(registerUserOperationProcessor.process(registerUserInput), HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = RestApiMappingAuthentication.POST_VALIDATETOKEN_PATH)
    public ResponseEntity<?> validateUser(@RequestBody ValidateUserInput input) {
        Either<Errors, ValidateUserOutput> output = validateUserOperationProcessor.process(input);

        return handleOperation(output, HttpStatus.CREATED);
    }

    @PostMapping(value = RestApiMappingAuthentication.POST_LOGOUT_USER)
    public ResponseEntity<?> logoutUser(@RequestBody LogoutUserInput request) {
        LogoutUserInput input = request.toBuilder()
                .token(loggedUser.getToken())
                .build();

        Either<Errors, LogoutUserOutput> output = logoutUserOperationProcessor.process(input);

        return handleOperation(output, HttpStatus.CREATED);
    }
}
