package com.intheeast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.intheeast.demo.entity.Role;
import com.intheeast.demo.entity.User;
import com.intheeast.demo.repository.RoleRepository;
import com.intheeast.demo.repository.UserRepository;

@SpringBootApplication
public class TransactionalityApplication {

	@Value("${first.names}")
    private String firstNames;

    @Value("${last.names}")
    private String lastNames;

    @Value("${ages}")
    private String ages;

    @Value("${active}")
    private String activeStatuses;
    
	public static void main(String[] args) {
		SpringApplication.run(TransactionalityApplication.class, args);
	}
	
	@Bean
    CommandLineRunner init(UserRepository userRepository, RoleRepository roleRepository) {
    	return args -> {
    		
    		Role userRole = new Role("USER");
            Role adminRole = new Role("ADMIN");
            
            roleRepository.save(userRole);
            roleRepository.save(adminRole);
            
            Random random = new Random();
            List<String> firstNameList = Arrays.asList(firstNames.split(","));
            List<String> lastNameList = Arrays.asList(lastNames.split(","));
            List<Integer> ageList = Arrays.stream(ages.split(","))
                    .map(Integer::parseInt)
                    .toList();
            List<Boolean> activeStatusList = Arrays.stream(activeStatuses.split(","))
                            .map(Boolean::parseBoolean)
                            .toList();

            List<User> users = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                String firstName = firstNameList.get(random.nextInt(firstNameList.size()));
                String lastName = lastNameList.get(random.nextInt(lastNameList.size()));
                Integer age = ageList.get(random.nextInt(ageList.size()));
                Boolean activeStatus = activeStatusList.get(random.nextInt(activeStatusList.size()));
                
                users.add(new User(firstName, lastName, age, activeStatus));
            }

            userRepository.saveAll(users);
        };
    }
}
