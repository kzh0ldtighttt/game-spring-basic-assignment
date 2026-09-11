# game-spring-basic-assignment

Spring Boot와 JPA를 사용하여 게임 서버의 기본 API를 구현한 과제입니다.


## 구현 기능

### Lv1
Docker MySQL과 Spring Boot를 연결하기 위한 설정 파일을 작성했습니다.

### Lv2
`GameService`를 Spring Bean으로 등록하여 의존성 주입 문제를 해결했습니다.

### Lv3
게임 목록 조회 API의 경로를 수정했습니다.

- `GET /games`

### Lv4
게임 생성 과정에서 발생한 읽기 전용 트랜잭션 문제를 수정했습니다.

### Lv5
게임 생성 요청과 카드 응답에 필요한 DTO를 구현했습니다.

### Lv6
게임의 진행 상태와 덱을 저장하는 API를 구현했습니다.

- `PUT /games/{gameId}/progress`

### Lv7
저장된 게임 목록과 게임 상세 정보를 조회하는 API를 구현했습니다.

- `GET /games`
- `GET /games/{gameId}`

게임 목록은 `Game`의 `id` 기준 내림차순으로 조회하고,  
게임 상세 조회의 덱은 `RunCard`의 `id` 기준 오름차순으로 조회하도록 구현했습니다.

### Lv8
플레이어 이름 변경과 게임 삭제 API를 구현했습니다.

- `PATCH /games/{gameId}`
- `DELETE /games/{gameId}`

이름 변경은 JPA의 변경 감지를 이용했고,  
게임 삭제 시 해당 게임의 카드 데이터를 먼저 삭제한 뒤 게임을 삭제하도록 구현했습니다.

### DB Pw
docker run --name game-mysql -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=game -p 3306:3306 -d mysql:8.0

