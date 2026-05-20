package com.re.trans_route.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class TripDTO {
    @NotBlank(message = "Điểm đi không được để trống")
    private String fromPlace;

    @NotBlank(message = "Điểm đến không được để trống")
    private String toPlace;

    @NotNull(message = "Ngày đi không được để trống")
    private LocalDate travelDate;
}
