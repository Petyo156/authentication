package com.tinqinacademy.authentication.api.operations.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class LoginUserOutput implements OperationOutput {
    @JsonIgnore
    private String token;
}