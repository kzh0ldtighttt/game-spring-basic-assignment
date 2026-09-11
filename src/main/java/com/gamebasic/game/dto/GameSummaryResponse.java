package com.gamebasic.game.dto;

import com.gamebasic.game.entity.GamePhase;
import com.gamebasic.game.entity.GameStatus;
import lombok.Getter;

@Getter
public class GameSummaryResponse {

    // 게임 ID
    private Long id;

    // 플레이어 이름
    private String playerName;

    // 현재 층
    private int currentFloor;

    // 현재 HP
    private int currentHp;

    // 현재 게임 단계
    private GamePhase phase;

    // 현재 게임 상태
    private GameStatus status;

    // 게임 목록 조회에 필요한 정보를 받아서 저장
    public GameSummaryResponse(
            Long id,
            String playerName,
            int currentFloor,
            int currentHp,
            GamePhase phase,
            GameStatus status
    ) {
        this.id = id;
        this.playerName = playerName;
        this.currentFloor = currentFloor;
        this.currentHp = currentHp;
        this.phase = phase;
        this.status = status;
    }
}