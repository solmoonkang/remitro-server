-- 1. 테스트용 데이터 삽입
DELETE FROM member;

INSERT INTO member (email, password, nickname, phone_number, member_status, last_login_at, failed_count, created_at, updated_at)
VALUES ('testMember@example.com', 'password123!', 'testNickname', '01012345678', 'ACTIVE', DATEADD('YEAR', -2, CURRENT_TIMESTAMP), 0, now(), now());
