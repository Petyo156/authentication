package com.tinqinacademy.authentication.persistence.initialize;

import com.tinqinacademy.authentication.persistence.entities.User;
import com.tinqinacademy.authentication.persistence.repositories.UsersRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class CustomApplicationRunner implements ApplicationRunner {

    private final UsersRepository usersRepository;

    @Autowired
    public CustomApplicationRunner(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (usersRepository.count() == 0) {
            usersRepository.save(User.builder()
                    .username("smeshka1")
                    .password("password")
                    .email("smesh@gmail.com")
                    .firstName("Ivan")
                    .lastName("Ivanov")
                    .build());
        }

        log.info("Fully working!!!");
    }
}