package com.gamebasic.game.service;

import com.gamebasic.game.dto.CreateRequest;
import com.gamebasic.game.dto.GameDetailResponse;
import com.gamebasic.game.dto.ProgressRequest;
import com.gamebasic.game.entity.Game;
import com.gamebasic.game.repository.GameRepository;
import com.gamebasic.runcard.dto.CardResponse;
import com.gamebasic.runcard.dto.RunCardRequest;
import com.gamebasic.runcard.entity.RunCard;
import com.gamebasic.runcard.repository.RunCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service; // 추가
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.gamebasic.game.dto.GameSummaryResponse;

import com.gamebasic.game.dto.RenameRequest;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service    // Spring이 GameService를 Bean으로 등록
public class GameService {

    private final GameRepository gameRepository;
    private final RunCardRepository runCardRepository;

    @Transactional(readOnly = false) // Connection is read-only. 에러 확인 후 true > false 수정
    public GameDetailResponse createGame(CreateRequest request) {
        Game game = gameRepository.save(new Game(request.getPlayerName()));
        saveDeck(game, request.getDeck());
        List<RunCard> cards = runCardRepository.findAllByGameOrderByIdAsc(game);
        List<CardResponse> deck = new ArrayList<>();
        for (RunCard card : cards) {
            deck.add(new CardResponse(card.getId(), card.getCardType(), card.getAcquiredFloor()));
        }
        return new GameDetailResponse(
                game.getId(),
                game.getPlayerName(),
                game.getCurrentHp(),
                game.getCurrentFloor(),
                game.getPhase(),
                game.getStatus(),
                deck
        );
    }

    private void saveDeck(Game game, List<RunCardRequest> deck) {
        List<RunCard> cards = new ArrayList<>();
        for (RunCardRequest card : deck) {
            cards.add(new RunCard(game, card.getCardType(), card.getAcquiredFloor()));
        }
        runCardRepository.saveAll(cards);
    }

    private Game findGame(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public GameDetailResponse updateProgress(Long gameId, ProgressRequest request) {
        Game game = findGame(gameId);
        game.updateProgress(
                request.getCurrentHp(),
                request.getCurrentFloor(),
                request.getPhase(),
                request.getStatus()
        );
        // 요청의 deck은 저장할 덱 전체이므로 기존 카드를 모두 지우고 요청 순서대로 다시 저장합니다.
        runCardRepository.deleteAllByGame(game);
        saveDeck(game, request.getDeck());
        List<RunCard> cards = runCardRepository.findAllByGameOrderByIdAsc(game);
        List<CardResponse> deck = new ArrayList<>();
        for (RunCard card : cards) {
            deck.add(new CardResponse(card.getId(), card.getCardType(), card.getAcquiredFloor()));
        }
        return new GameDetailResponse(
                game.getId(),
                game.getPlayerName(),
                game.getCurrentHp(),
                game.getCurrentFloor(),
                game.getPhase(),
                game.getStatus(),
                deck
        );
    }


    // 저장된 게임 목록 조회
    @Transactional(readOnly = true)
    public List<GameSummaryResponse> getGames() {

        // 게임 목록을 id 기준 내림차순으로 조회
        List<Game> games = gameRepository.findAllByOrderByIdDesc();

        // 조회한 게임 정보를 담을 응답 목록 생성
        List<GameSummaryResponse> responses = new ArrayList<>();

        // Game 데이터를 하나씩 GameSummaryResponse로 바꿔서 추가
        for (Game game : games) {
            responses.add(new GameSummaryResponse(
                    game.getId(),
                    game.getPlayerName(),
                    game.getCurrentFloor(),
                    game.getCurrentHp(),
                    game.getPhase(),
                    game.getStatus()
            ));
        }

        // 완성된 게임 목록 반환
        return responses;
    }

    // 게임 상세 조회.
    @Transactional(readOnly = true)
    public GameDetailResponse getGame(Long gameId) {

        // gameId에 해당하는 게임 조회
        // 없는 게임이면 기존 findGame()에서 404 처리
        Game game = findGame(gameId);

        // 해당 게임의 카드를 id 기준 오름차순으로 조회
        List<RunCard> cards = runCardRepository.findAllByGameOrderByIdAsc(game);

        // 카드 정보를 담을 응답 목록 생성
        List<CardResponse> deck = new ArrayList<>();

        // 조회한 카드를 하나씩 CardResponse로 바꿔서 추가
        for (RunCard card : cards) {
            deck.add(new CardResponse(
                    card.getId(),
                    card.getCardType(),
                    card.getAcquiredFloor()
            ));
        }

        // 게임 정보와 덱을 담아서 상세 조회 결과 반환
        return new GameDetailResponse(
                game.getId(),
                game.getPlayerName(),
                game.getCurrentHp(),
                game.getCurrentFloor(),
                game.getPhase(),
                game.getStatus(),
                deck
        );
    }

    // 플레이어 이름 변경
    @Transactional
    public void renameGame(Long gameId, RenameRequest request) {

        // gameId에 해당하는 게임 조회
        Game game = findGame(gameId);

        // 요청으로 받은 새 이름으로 변경
        game.rename(request.getPlayerName());
    }

    // 게임 삭제
    @Transactional
    public void deleteGame(Long gameId) {

        // gameId에 해당하는 게임 조회
        Game game = findGame(gameId);

        // 게임에 속한 카드들을 먼저 삭제
        runCardRepository.deleteAllByGame(game);

        // 카드 삭제 후 게임 삭제
        gameRepository.delete(game);
    }
}
