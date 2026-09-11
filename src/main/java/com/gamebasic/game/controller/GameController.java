package com.gamebasic.game.controller;

import com.gamebasic.game.dto.CreateRequest;
import com.gamebasic.game.dto.GameDetailResponse;
import com.gamebasic.game.dto.GameSummaryResponse;
import com.gamebasic.game.dto.ProgressRequest;
import com.gamebasic.game.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping("/games") // /game >> /games로 수정
    public ResponseEntity<List<GameSummaryResponse>> getGames() {

        // Service에서 게임 목록을 가져와 반환
        return ResponseEntity.ok(gameService.getGames());
    }

    // 저장된 게임 목록 조회
    @PostMapping("/games")
    public ResponseEntity<GameDetailResponse> createGame(@Valid @RequestBody CreateRequest request) {
        GameDetailResponse created = gameService.createGame(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 특정 게임의 상세 정보 조회
    @GetMapping("/games/{gameId}")
    public ResponseEntity<GameDetailResponse> getGame(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameService.getGame(gameId));
    }



    // 특정 게임의 진행 상태와 전체 덱을 저장하는 API
    @PutMapping("/games/{gameId}/progress")
    public ResponseEntity<GameDetailResponse> updateProgress(

            // 주소의 {gameId} 값을 가져와 어떤 게임인지 확인
            @PathVariable Long gameId,

            // 요청으로 받은 진행 정보를 ProgressRequest에 담고
            // @Valid로 값이 조건에 맞는지 확인
            @Valid @RequestBody ProgressRequest request
    ) {
        // gameId와 진행 정보를 Service로 보내 저장
        // updateProgress의 결과가 GameDetailResponse이므로 응답 타입도 동일하게 작성
        return ResponseEntity.ok(
                gameService.updateProgress(gameId, request)
        );
    }
}
