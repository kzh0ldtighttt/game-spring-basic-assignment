package com.gamebasic.game.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RenameRequest {

    // 새 플레이어 이름
    // 공백만 입력할 수 없고, 2자 이상 12자 이하로 입력
    @NotBlank
    @Size(min = 2, max = 12)
    private String playerName;
}