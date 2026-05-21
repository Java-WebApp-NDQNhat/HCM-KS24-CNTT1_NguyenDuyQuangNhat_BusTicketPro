package com.re.trans_route.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateDTO {
    @NotBlank(message = "Tên người dùng không được để trống")
    private String userName;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    private String address;
}
