-- 1. 테스트용 데이터 삽입
INSERT INTO member (email, nickname, phone_number, member_status, last_login_at, created_at, updated_at)
VALUES ('testMember@example.com', 'testNickname', '01012345678', 'ACTIVE', '2023-01-01T00:00:00', now(), now());

-- 2. 특정 데이터를 휴면 대상(1년 전)으로 변경
UPDATE member SET last_login_at = DATEADD('YEAR', -1, CURRENT_TIMESTAMP) WHERE email = 'testMember@example.com';
