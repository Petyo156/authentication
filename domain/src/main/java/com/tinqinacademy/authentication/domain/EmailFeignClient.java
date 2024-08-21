package com.tinqinacademy.authentication.domain;

import com.tinqinacademy.email.restexport.EmailClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "emailClient", url = "http://localhost:8083")
public interface EmailFeignClient extends EmailClient {
}
