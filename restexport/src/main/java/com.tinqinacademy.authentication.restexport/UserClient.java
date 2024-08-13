package com.tinqinacademy.authentication.restexport;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "userClient", url = "http://localhost:8082")
public interface UserClient {

}
