-- [MEMBER 더미 데이터]
-- 기존 데이터 삭제 및 시퀀스 초기화
TRUNCATE TABLE member RESTART IDENTITY CASCADE;

-- 회원 데이터 삽입
INSERT INTO member (member_id, name, nickname, email, role, login_type, profile_image, created_at, updated_at)
VALUES (1, '김민상', '민상', 'mdk9901@gmail.com', 'ADMIN', 'NAVER', '${S3_BASE_URL}/profile/poo.png', NOW(), NOW()),
       (2, '홍원택', '원택', 'wontaek@example.com', 'ADMIN', 'NAVER', '${S3_BASE_URL}/profile/boy.png', NOW(), NOW()),
       (3, '박성민', '성민', 'sungmin@example.com', 'ADMIN', 'NAVER', '${S3_BASE_URL}/profile/woonyoung.png', NOW(), NOW()),
       (4, '김루시아', '시아', 'sia@example.com', 'ADMIN', 'NAVER', '${S3_BASE_URL}/profile/karina.png', NOW(), NOW());

-- 시퀀스 재설정
SELECT setval('member_member_id_seq', (SELECT MAX(member_id) FROM member));
