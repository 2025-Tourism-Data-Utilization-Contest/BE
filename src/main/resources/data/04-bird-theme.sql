-- bird_theme 매핑 초기 데이터 입력
TRUNCATE TABLE bird_theme RESTART IDENTITY CASCADE;

INSERT INTO bird_theme (bird_id, theme_id)
VALUES (1, 1), -- 순천만 ↔ 흑두루미
       (2, 2), -- 우포늪 ↔ 따오기
       (7, 3), -- 금강하구 ↔ 가창오리
       (4, 4), -- 계룡산 ↔ 딱새
       (5, 5), -- 시화호 ↔ 도요새
       (6, 6), -- 백령도 ↔ 검은머리물떼새
       (3, 7); -- 한려해상 ↔ 팔색조
