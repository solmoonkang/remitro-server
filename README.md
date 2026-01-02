# 💸 REMITRO-SERVER: 금융 비즈니스 핵심 규칙을 MSA 스타일로 분석·구현하는 백엔드 프로젝트

### 🚀프로젝트 개요

REMITRO-SERVER는 금융 시스템에서 가장 중요한 정합성, 동시성, 트랜잭션 경계를
단순 CRUD가 아닌 실제 금융 비즈니스 관점에서 분석하고 구현하는 백엔드 프로젝트입니다.

해당 프로젝트의 목표는 "모든 기능을 구현하는 것"이 아니라,
- 금융 도메인의 핵심 비즈니스 규칙
- 정합성을 깨뜨리는 문제 상황과 그 해결 방식
- 이벤트 기반(MSA-style) 아키텍처 설계 사고

를 구조와 코드로 명확하게 증명하는 데 있습니다.

#### 프로젝트 범위

- 단일 레포 기반 멀티 모듈 구조
- DDD + Layered Architecture
- Core Domain / Support Service / Platform Service 분리
- Kafka 기반 이벤트 비동기 처리
- Outbox Pattern을 통한 이벤트 정합성 보장
- Redis(Redisson) 기반 분산 락 및 멱등성 제어

---

### 🛠️아키텍처 개요

해당 프로젝트는 MSA 사고방식을 따르되, 개인 개발 환경에 맞게 단일 레포 멀티 모듈 구조로 설계되었습니다.
```text
[ Client ]
    ↓
[ API Gateway ]
    ↓
────────────────────────────
 Core Domain Services
────────────────────────────
 Support Services
────────────────────────────
 Event Stream (Kafka)

```

#### 모듈 구성

| 모듈명                        | 역할                | 비고               |
| -------------------------- | ----------------- | ---------------- |
| **remitro-gateway**        | API Gateway       | 인증 필터, 라우팅       |
| **remitro-discovery**      | Service Discovery | Eureka Server    |
| **remitro-auth**           | 인증 서비스            | JWT 발급           |
| **remitro-member**         | 회원 도메인            | 회원 상태 관리         |
| **remitro-account**        | 계좌 도메인            | 잔액 변경, Outbox 적용 |
| **remitro-transaction**    | 거래 원장             | 불변 원장, 이벤트 소비    |
| **remitro-notification**   | 알림 서비스            | 이벤트 소비           |
| **remitro-audit**          | 감사 로그             | 이벤트 소비           |
| **remitro-common-support** | 공통 라이브러리          | 정책·유틸            |
| **remitro-common-event**   | 이벤트 계약            | 이벤트 스키마          |


#### 핵심 기술 스택

| 영역      | 기술                           | 목적         |
| ------- | ---------------------------- | ---------- |
| 서비스 인프라 | Spring Cloud Gateway, Eureka | 진입점·서비스 탐색 |
| 데이터     | JPA, H2                      | 도메인 모델링    |
| 메시징     | Apache Kafka                 | 이벤트 비동기 전파 |
| 동시성     | Redis, Redisson              | 분산 락       |
| 보안      | Spring Security, JWT         | 인증·인가      |
| 공통      | Lombok, Validation           | 생산성·유효성    |


---

### 📦 Outbox Pattern 적용 전략

Outbox Pattern은 모든 서비스에 적용되지 않습니다.
해당 프로젝트에서는 도메인 정합성이 가장 중요한 remitro-account 모듈에만 적용합니다.

#### 적용 이유

- 계좌 잔액 변경과 이벤트 발행은 반드시 함께 성공하거나 함께 실패해야 함
- DB 트랜잭션과 메시지 발행 간 불일치 방지

#### 적용 방식

- 잔액 변경 + `OutboxMessage` 기록을 단일 DB 트랜잭션으로 처리
- 별도의 Outbox Publisher가 테이블을 Polling하여 Kafka로 이벤트 전송

Outbox Pattern은 모든 이벤트 발행에 적용되는 패턴이 아니라, 도메인 상태 변경과 이벤트 발행이 강하게 결합되어야 하는
Account 도메인에 한해 선택적으로 적용되었습니다.

### 🔒 핵심 비즈니스 규칙

#### 데이터 정합성 & 동시성

- 하나의 계좌에 대한 잔액 변경은 분산 락을 통해 직렬화
- 송금 시 두 계좌 락을 계좌 ID 오름차순으로 획득하여 교착 상태 방지
- 거래 원장은 불변 데이터(UPDATE / DELETE 금지)

#### 보안 & 안정성

- API 접근: JWT 인증
- 거래 요청: 계좌 비밀번호 추가 검증
- 멱등성 보장: X-Idempotency-Key 기반 중복 실행 방지
- 모든 비밀번호는 BCrypt 단방향 해시로 저장

---

### ⚙️ 구현 가이드라인

- Gateway는 인증·라우팅만 담당, 비즈니스 로직 ❌
- Core Domain 서비스 간 직접 의존 ❌
- 서비스 간 통신은 이벤트 또는 API 계약으로만 수행 
- Support 서비스(notification, audit)는 이벤트 소비 전용

### 📌 프로젝트의 의의

이 프로젝트는 단순한 "기능 구현"이 아니라, 
- 왜 금융 시스템이 복잡한지
- 어디서 정합성이 꺠질 수 있는지
- 그 문제를 어떻게 구조적으로 해결하는지

를 설계 -> 구조 -> 코드로 설명할 수 있도록 구성된 금융 도메인 중심 백엔드 분석 프로젝트입니다.
