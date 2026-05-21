package com.re.trans_route.service;

import com.re.trans_route.dto.ProfileUpdateDTO;
import com.re.trans_route.entity.User;
import com.re.trans_route.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User updateProfile(String email, ProfileUpdateDTO dto) {
        User user = requireUser(email);
        user.setUserName(dto.getUserName().trim());
        user.setPhone(dto.getPhone().trim());
        user.setAddress(dto.getAddress() != null ? dto.getAddress().trim() : null);
        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = requireUser(email);
        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Mật khẩu hiện tại không đúng");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public ProfileUpdateDTO toProfileDto(User user) {
        return ProfileUpdateDTO.builder()
                .userName(user.getUserName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build();
    }

    private User requireUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalStateException("Không tìm thấy người dùng");
        }
        return user;
    }
}
