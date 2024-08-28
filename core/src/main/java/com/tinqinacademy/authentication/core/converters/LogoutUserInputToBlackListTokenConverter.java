package com.tinqinacademy.authentication.core.converters;

import com.tinqinacademy.authentication.api.operations.logoutuser.LogoutUserInput;
import com.tinqinacademy.authentication.persistence.entities.BlacklistToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogoutUserInputToBlackListTokenConverter implements Converter<LogoutUserInput, BlacklistToken> {
    @Override
    public BlacklistToken convert(LogoutUserInput input) {
        return BlacklistToken.builder()
                .token(input.getToken())
                .build();
    }
}