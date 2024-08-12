package com.tinqinacademy.authentication.api.operations.demote;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DemoteUserInput implements OperationInput {
    private String userId;
}