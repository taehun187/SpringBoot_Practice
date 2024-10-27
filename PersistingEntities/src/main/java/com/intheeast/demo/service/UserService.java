package com.intheeast.demo.service;

import org.springframework.stereotype.Service;

import com.intheeast.demo.dto.UserDTO;
import com.intheeast.demo.entity.User;
import com.intheeast.demo.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public long countByLastname(String lastname) {
        return userRepository.countByLastname(lastname);
    }

    @Transactional
    public long deleteByEmail(String email) { // 이메일로 삭제하는 메서드 추가
        return userRepository.deleteByEmail(email);
    }

    @Transactional
    public List<UserDTO> removeByEmail(String email) {
        List<User> users = userRepository.removeByEmail(email);
        return users.stream()
                    .map(UserDTO::fromEntity) // User -> UserDTO 변환
                    .collect(Collectors.toList());
    }

    public UserDTO saveUser(User user) {
    	if (user.isNew()) {
            System.out.println("New User(O): Persisting the user.");
        } else {
            System.out.println("Existing User: Updating the user.");
        }
    	////////////////////////////////////////////
        User savedUser = userRepository.save(user);
        ///////////////////////////////////////////
        
        //
        if (!savedUser.isNew()) {
            System.out.println("New User(X): Persisting the user.");
        } else {
            System.out.println("Existing User: Updating the user.");
        }
        
        return UserDTO.fromEntity(savedUser); // User -> UserDTO 변환
    }

    public List<UserDTO> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream()
                    .map(UserDTO::fromEntity) // User -> UserDTO 변환
                    .collect(Collectors.toList());
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && !user.isNew()) {
            System.out.println("User is already persisted.");
        }
        return UserDTO.fromEntity(user); 
    }


    @Transactional
    public void updateUser(String firstname, String lastname, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstname(firstname);
        user.setLastname(lastname);
        
    }
}