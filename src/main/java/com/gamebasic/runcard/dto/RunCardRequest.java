package com.gamebasic.runcard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RunCardRequest {

    @NotBlank // null, 빈 문자열, 공백 문자열 모두 거부
    private String cardType;

    @NotNull // null 값 거부
    private Integer acquiredFloor;
}
