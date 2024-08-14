package com.tinqinacademy.authentication.api.operations.registeruser;

import com.tinqinacademy.authentication.api.base.OperationInput;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RegisterUserInput implements OperationInput {
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Email(message = "Email must be valid")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @Past(message = "Birth date cannot be in the future")
    private LocalDate birthDate;

    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;
}