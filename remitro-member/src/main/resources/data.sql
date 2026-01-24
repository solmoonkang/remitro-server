-- 기존 데이터 초기화
DELETE
FROM member;

-- 시스템 관리자 (ADMIN)
INSERT INTO member (email, password_hash, nickname, phone_number, phone_number_hash, member_status,
                    login_security_status, role, failed_count, created_at, updated_at, version)
VALUES ('adminMember@example.com', 'adminP@ss0rd!', 'adminNickname', '01012345678', 'ADMIN_PHONE_HASH_VALUE',
        'ACTIVE', 'NORMAL', 'ADMIN', 0, now(), now(), 0);
