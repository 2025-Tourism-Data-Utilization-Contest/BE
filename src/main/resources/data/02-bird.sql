-- [BIRD 더미 데이터]
-- 기존 데이터 삭제 및 시퀀스 초기화
TRUNCATE TABLE bird RESTART IDENTITY CASCADE;

-- 새 데이터 삽입
INSERT INTO bird (name_kr, name_en, bird_image, description)
VALUES
-- 1. 흑두루미
('흑두루미', 'Grus monacha Temminck',
 '${S3_BASE_URL}/bird/black-crane.png',
 '멸종위기 야생생물 II급에 속하는 겨울철새로, 몸 전체는 회색빛을 띠며 이마에는 선명한 붉은색이 있다. 순천만, 철원 등 국내 주요 습지에서 월동하며, 논과 습지에서 먹이를 찾는다.'),

-- 2. 호반새
('호반새', 'Halcyon coromanda',
 '${S3_BASE_URL}/bird/ruddy-kingfisher.jpeg',
 '짙은 적갈색의 깃털과 굵은 붉은 부리가 특징인 여름 철새. 울창한 숲이나 계곡에서 서식하며 곤충, 도마뱀, 작은 어류 등을 포식한다. 특유의 맑은 울음소리로도 잘 알려져 있다.'),

-- 3. 팔색조
('팔색조', 'Pitta nympha',
 '${S3_BASE_URL}/bird/fairy-pitta.png',
 '빨강, 초록, 파랑 등 다양한 색의 깃털을 지닌 여름 철새로 “날지 않는 보석”이라고도 불린다. 낙엽이 많은 숲 속에서 주로 지렁이나 곤충을 먹으며 번식한다.'),

-- 4. 딱새
('딱새', 'Phoenicurus auroreus',
 '${S3_BASE_URL}/bird/daurian-redstart.png',
 '수컷은 검은 머리와 주황빛 가슴, 암컷은 회갈색의 부드러운 색감을 띠는 텃새. 도심 공원, 산책로, 농촌 등에서 자주 관찰되며, 작은 곤충이나 열매를 섭취한다.'),

-- 5. 도요새
('도요새', 'Calidris alpina',
 '${S3_BASE_URL}/bird/dunlin.png',
 '작고 민첩한 체구를 지닌 이동성 철새로, 얕은 갯벌이나 하구에서 벌레, 갑각류 등을 먹으며 무리지어 행동한다. 깃털은 계절에 따라 색이 달라지는 특징이 있다.'),

-- 6. 검은머리물떼새
('검은머리물떼새', 'Haematopus ostralegus',
 '${S3_BASE_URL}/bird/oystercatcher.png',
 '검은 머리와 등, 흰 배, 긴 주황색 부리가 특징인 도요과 새. 주로 해안, 갯벌, 섬 지역에서 조개류나 갯지렁이 등을 쪼아 먹으며 살아간다. 날카로운 울음소리를 낸다.'),

-- 7. 가창오리
('가창오리', 'Anas formosa',
 '${S3_BASE_URL}/bird/baikal-teal.png',
 '녹색과 금빛이 섞인 화려한 깃털을 가진 겨울 철새로, 수십만 마리가 떼지어 하늘을 수놓는 장관을 연출한다. 충남 서산 천수만, 경기 화성호 등에서 대규모로 월동한다.');

-- 시퀀스 재설정
SELECT setval('bird_bird_id_seq', (SELECT MAX(bird_id) FROM bird));
