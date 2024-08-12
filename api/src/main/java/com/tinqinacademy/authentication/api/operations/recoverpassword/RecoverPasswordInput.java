package com.tinqinacademy.authentication.api.operations.recoverpassword;

import com.tinqinacademy.authentication.api.base.OperationInput;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RecoverPasswordInput implements OperationInput {
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;
}