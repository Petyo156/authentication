package com.tinqinacademy.authentication.api.operations.promote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PromoteUserInput implements OperationInput {
    private String userId;

    @JsonIgnore
    private String loggedUserId;
}