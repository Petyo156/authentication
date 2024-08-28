package com.tinqinacademy.authentication.domain;

import com.tinqinacademy.email.restexport.EmailClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "emailClient", url = "${email.service.url}")
public interface EmailFeignClient extends EmailClient {

}
