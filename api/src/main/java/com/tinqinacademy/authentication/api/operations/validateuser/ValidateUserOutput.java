package com.tinqinacademy.authentication.api.operations.validateuser;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ValidateUserOutput implements OperationOutput {
    private Boolean validity;
}
