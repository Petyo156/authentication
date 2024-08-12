package com.tinqinacademy.authentication.api.operations.confirmregistration;

import com.tinqinacademy.authentication.api.base.OperationInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ConfirmRegistrationInput implements OperationInput {
    @NotBlank(message = "Confirmation code cannot be blank")
    @Size(max = 10, min = 10, message = "Code must be valid")
    private String confirmationCode;
}