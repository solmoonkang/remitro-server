## 💸 REMITRO (계좌 기반 Mock 결제 플랫폼)

REMITRO는 사용자 간 간편 송금을 안전하게 처리하기 위한 시스템입니다.

금융 OpenAPI 제공자 관점에서 계좌 개설부터 입출금, 이체 이력 관리까지 결제 서비스의 핵심 기능을 직접 설계하였습니다.

찰나의 순간에 발생하는 거래들 사이에서 데이터의 정합성과 신뢰성을 유지하는 것을 우선 목표로 구현하였습니다.

<br>

### 🏗 시스템 아키텍처

각 서비스가 독립적인 책임을 갖도록 분리하고, 모든 요청이 `Gateway`를 거치도록 단일화하여 통합적인 인증과 라우팅 환경을 구축했습니다. 

- **Gateway & Discovery**: 인프라의 관문으로서 라우팅과 각 서비스의 가용성을 관리합니다.
- **Member Service**: 별도의 Auth 서비스를 두는 대신, 결제 도메인에 집중하기 위해 사용자 관리와 JWT 인증 책임을 하나로 통합하였습니다.
- **Account Service**: 계좌 상태 관리와 잔액 증감을 처리하는 핵심 비즈니스 로직을 담당합니다.
- **Transaction Service**: 모든 거래 결과를 불변(Immutable) 데이터로 기록하여 금융 서비스의 신뢰성을 확보합니다.

#### 서비스 흐름

`Client` → `Gateway (Routing/Auth)` → `Microservices (Member/Account/Transaction)` → `Isolated DB`

<br>

### ✨ 주요 설계 판단 (Key Points)

#### 1. 비관적 락(Pessimistic Lock) 기반의 정합성 확보

금융 서비스에서 잔액의 불일치는 치명적입니다.

충돌 가능성이 높은 입출금 및 이체 상황에서 데이터 일관성을 보장하기 위해, 낙관적 락보다 확실한 제어가 가능한 비관적 락을 사용하여 레이스 컨디션을 방지하였습니다.

#### 2. Kafka를 활용한 비동기 이벤트 처리

계좌 잔액의 변경(Account)과 거래 내역의 기록(Transaction) 사이의 물리적 결합도를 낮추기 위해 Kafka 기반 비동기 통신을 수행합니다.

이를 통해 각 서비스는 자신의 책임에만 집중하며 시스템 전체의 응답 성능을 최적화합니다. (단, 실시간 조회가 필요한 구간은 OpenFeign을 활용한 동기 통신을 병행합니다.)

#### 3. API 멱등성(Idempotency) 보장

중복 요청이나 네트워크 재시도 상황에서 중복 결제 등 부정 거래가 발생하지 않도록 Idempotency-Key를 도입하였습니다.

이를 통해 동일한 요청에 대해 언제나 일관된 결과를 보장합니다.

<br>

### 🛠 기술 스택 및 개발 환경

- **Framework**: Spring Boot 3.4.1, Spring Cloud 2023.0.3
- **Language**: Java 17 (Toolchain)
- **Build Tool**: Gradle
- **Infrastructure**: Infrastructure: Netflix Eureka (Discovery), Spring Cloud Gateway, Spring Security
- **Database**: Spring Data JPA (H2), Redis
- **Messaging**: Apache Kafka (Spring Kafka 4.0.1)
- **Security**: JJWT (JSON Web Token) 0.12.7
- **Documentation**: SpringDoc OpenAPI 2.8.10 (Swagger)

<br>

### 📂 패키지 구조 (Standard Layered Architecture)

```text
[ Service Name ]
 ├─ presentation    # API 엔드포인트 및 전역 예외 처리 (Controller, Advice)
 ├─ application     # 유즈케이스 구현 (Command/Query 서비스 분리 및 DTO)
 ├─ domain          # 핵심 비즈니스 로직 및 도메인 규칙 (Entity, Policy, Interface)
 └─ infrastructure  # 외부 기술 연동 및 저장소 구현체 (Persistence, Messaging, Config)
```

<br>

### 🔍 Swagger API 명세

각 서비스는 **Swagger(SpringDoc)**를 통해 API 명세를 자동화합니다.

- **Swagger UI**: `http://localhost:{service-port}/swagger-ui.html`

| 서비스 모듈                  | 기본 포트  | 주요 API 범위                 |
|:------------------------|:-------|:--------------------------|
| **remitro-member**      | `8081` | 회원 가입, 로그인, 내 정보 관리       |
| **remitro-account**     | `8082` | 계좌 생성, 잔액 증감(입/출금), 계좌 조회 |
| **remitro-transaction** | `8083` | 거래 내역 이력 조회               |

<br>

### 📑 주요 API 명세 요약

#### 1. Member API (회원 및 인증)

인증 관련 로직을 Member 서비스 내 `/auth` 경로로 통합하여 관리합니다.

| 기능          |   메서드   | 엔드포인트                | 설명                   |
|:------------|:-------:|:---------------------|:---------------------|
| **회원가입**    | `POST`  | `/api/v1/members`    | 금융 플랫폼 사용자 신규 등록     |
| **로그인**     | `POST`  | `/api/v1/auth/login` | 이메일/비밀번호 인증 및 JWT 발급 |
| **내 정보 조회** |  `GET`  | `/api/v1/members/me` | 로그인된 사용자 프로필 조회      |
| **내 정보 수정** | `PATCH` | `/api/v1/members/me` | 사용자 정보(닉네임 등) 변경     |

#### 2. Account API (계좌 및 거래 실행)

금융 거래의 핵심인 잔액 변경 및 계좌 상태를 관리합니다.

| 기능           |  메서드   | 엔드포인트                                       | 비고                     |
|:-------------|:------:|:--------------------------------------------|:-----------------------|
| **계좌 생성**    | `POST` | `/api/v1/accounts`                          | 신규 가상 계좌 개설            |
| **계좌 목록 조회** | `GET`  | `/api/v1/accounts`                          | 사용자가 보유한 모든 계좌 리스트     |
| **계좌 단건 조회** | `GET`  | `/api/v1/accounts/{accountId}`              | 특정 계좌의 상세 정보 및 잔액 확인   |
| **입금 / 출금**  | `POST` | `/api/v1/accounts/{accountId}/transactions` | **Idempotency-Key 필수** |

#### 3. Transaction API (거래 이력)

결과 데이터를 기반으로 한 신뢰성 있는 이력 조회 기능을 제공합니다.

| 기능           |  메서드  | 엔드포인트                                       | 설명                  |
|:-------------|:-----:|:--------------------------------------------|:--------------------|
| **거래 내역 조회** | `GET` | `/api/v1/accounts/{accountId}/transactions` | 특정 계좌의 모든 입출금 이력 조회 |
