package com.re.trans_route.dto;

import com.re.trans_route.annotation.PasswordMatch;
import com.re.trans_route.annotation.UniqueEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
@PasswordMatch
public class RegisterDTO {
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @UniqueEmail
    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotNull(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải quá ngắn")
    private String password;

    @NotBlank(message = "Mật khẩu xác nhận không được để trống")
    private String rePass;
}
