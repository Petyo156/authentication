package com.tinqinacademy.authentication.restexport;

import com.tinqinacademy.authentication.api.apimapping.RestApiMappingAuthentication;
import com.tinqinacademy.authentication.api.operations.validateuser.ValidateUserInput;
import com.tinqinacademy.authentication.api.operations.validateuser.ValidateUserOutput;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@Headers({"Content-Type: application/json"})
@FeignClient(name = "AuthenticationRestClient", url = "http://localhost:8082")
public interface AuthenticationRestClient {
    @PostMapping(RestApiMappingAuthentication.POST_VALIDATETOKEN_PATH)
    ValidateUserOutput validateUser(ValidateUserInput input);
}
