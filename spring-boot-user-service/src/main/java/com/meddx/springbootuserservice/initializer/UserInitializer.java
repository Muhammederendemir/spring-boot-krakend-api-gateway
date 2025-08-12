package com.meddx.springbootuserservice.initializer;

import com.meddx.springbootuserservice.entity.User;
import com.meddx.springbootuserservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.IntStream;

@Configuration
public class UserInitializer {

    @Bean
    CommandLineRunner initializeDatabase(UserRepository userRepository) {
        return args -> {
            userRepository.deleteAll(); // Her başlangıçta veritabanını temizle

            // Entity'deki constructor'a uygun şekilde veri oluşturma
            IntStream.range(1, 4)
                    .mapToObj(i -> new User(0, "user" + i, "user" + i + "@example.com", "password" + i))
                    .forEach(userRepository::save);

            System.out.println("Users loaded: " + userRepository.findAll().size());
        };
    }
}