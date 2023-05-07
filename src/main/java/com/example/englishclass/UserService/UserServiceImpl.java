package com.example.englishclass.UserService;

import com.example.englishclass.Entity.Role;
import com.example.englishclass.Entity.User;
import com.example.englishclass.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public void createUser(User user){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        Role userRole = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        user.setStatus("active");
        userRepository.save(user);
        System.out.println("work");
    }

    public void  update(User user){
        userRepository.save(user);
    }

    public User getMemberByEmail(String login) {
        return userRepository.getUserByEmail(login);
    }
}
