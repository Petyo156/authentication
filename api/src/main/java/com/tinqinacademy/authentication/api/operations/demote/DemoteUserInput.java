package com.tinqinacademy.authentication.api.operations.demote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
public class DemoteUserInput implements OperationInput {
    private String userId;

    @JsonIgnore
    private String loggedUserId;
}