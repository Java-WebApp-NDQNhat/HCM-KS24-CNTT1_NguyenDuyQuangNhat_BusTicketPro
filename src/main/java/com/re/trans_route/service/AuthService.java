package com.re.trans_route.service;

import com.re.trans_route.dto.RegisterDTO;
import com.re.trans_route.entity.User;
import com.re.trans_route.entity.Role;
import com.re.trans_route.mapper.UserMapper;
import com.re.trans_route.repository.UserRepository;
import com.re.trans_route.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    public void register(RegisterDTO registerDTO) {
        String hashedPassword = passwordEncoder.encode(registerDTO.getPassword());
        Role role = roleRepository.findBySlug("PASSENGER")
                .orElseThrow(() -> new IllegalStateException("Default role PASSENGER not found"));

        User user = userMapper.toEntity(registerDTO);
        user.setPasswordHash(hashedPassword);
        user.setRole(role);
        userRepository.save(user);
    }

}
