package com.tinqinacademy.authentication.api.operations.logoutuser;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
public class LogoutUserInput implements OperationInput {
    @JsonIgnore
    private String token;
}