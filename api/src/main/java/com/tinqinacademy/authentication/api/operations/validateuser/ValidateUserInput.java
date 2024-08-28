package com.tinqinacademy.authentication.api.operations.validateuser;

import com.tinqinacademy.authentication.api.base.OperationInput;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ValidateUserInput implements OperationInput {
    @NotBlank(message = "Token cannot be null")
    private String token;
}
