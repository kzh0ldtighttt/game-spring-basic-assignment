package com.gamebasic.runcard.dto;

import lombok.Getter;

@Getter
public class CardResponse {

    // DB에 저장된 카드의 고유 ID
    private Long id;

    // 카드 종류
    private String cardType;

    // 카드를 획득한 층
    private Integer acquiredFloor;

    // 카드 정보를 받아 응답 DTO 생성
    public CardResponse(Long id, String cardType, int acquiredFloor) {
        this.id = id;
        this.cardType = cardType;
        this.acquiredFloor = acquiredFloor;
    }
}
