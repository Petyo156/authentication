package com.tinqinacademy.authentication.persistence.initialize;

import com.tinqinacademy.authentication.persistence.entities.ConfirmationCode;
import com.tinqinacademy.authentication.persistence.entities.User;
import com.tinqinacademy.authentication.persistence.models.RoleType;
import com.tinqinacademy.authentication.persistence.repositories.ConfirmationCodesRepository;
import com.tinqinacademy.authentication.persistence.repositories.UsersRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class CustomApplicationRunner implements ApplicationRunner {

    private final UsersRepository usersRepository;
    private final ConfirmationCodesRepository confirmationCodesRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomApplicationRunner(UsersRepository usersRepository, ConfirmationCodesRepository confirmationCodesRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.confirmationCodesRepository = confirmationCodesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (usersRepository.count() == 0) {
            usersRepository.save(User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .email("admin@gmail.com")
                    .firstName("Admin")
                    .lastName("Admin")
                    .roleType(RoleType.ADMIN)
                    .build());
        }
        if (confirmationCodesRepository.count() == 0) {
            User admin = usersRepository.findByUsername("admin").get();

            confirmationCodesRepository.save(ConfirmationCode.builder()
                    .userId(admin.getUserId())
                    .isUsed(true)
                    .code("code")
                    .build());
        }

        log.info("Fully working!!!");
    }
}