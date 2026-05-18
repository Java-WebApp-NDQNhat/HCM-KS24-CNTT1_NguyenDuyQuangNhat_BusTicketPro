package com.re.trans_route.service;

import com.re.trans_route.dto.RegisterDTO;
import com.re.trans_route.entity.User;
import com.re.trans_route.mapper.UserMapper;
import com.re.trans_route.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public boolean isEmailExisted(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

}
