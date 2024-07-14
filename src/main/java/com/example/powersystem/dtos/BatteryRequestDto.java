package com.example.powersystem.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class BatteryRequestDto {
    @NotBlank
    @jakarta.validation.constraints.NotNull
    private String name;

    @NotNull
    @NotBlank
    private String postCode;

    @NotNull
    @Positive
    private Integer wattCapacity;
}
