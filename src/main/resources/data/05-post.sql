-- [POST 더미 데이터]
-- 기존 데이터 삭제 및 시퀀스 초기화
TRUNCATE TABLE post RESTART IDENTITY CASCADE;

-- 게시글 더미 데이터 5개 삽입
INSERT INTO post (title, content, post_type, visibility, like_count, created_at, updated_at, member_id)
VALUES
    -- 김민상 (1)
    ('흑두루미, 실제로 보니까 너무 감동이에요',
     '순천만 갈대밭에서 이른 아침에 흑두루미 무리를 봤어요. 조용히 울음소리 듣고 있으니 마음이 차분해졌어요. 겨울 새벽엔 꼭 방한 잘 하고 가세요!',
     'NORMAL', 'VISIBLE_ALL', 0, NOW(), NOW(), 1),

    -- 홍원택 (2)
    ('우포늪에서 만난 따오기, 눈물이 날 뻔했어요',
     '사진으로만 보던 따오기를 진짜 보니 감회가 남다르더라고요. 하얀 날개가 햇빛에 반짝이는 모습이 아직도 눈에 아른거려요.',
     'NORMAL', 'VISIBLE_ALL', 0, NOW(), NOW(), 2),

    -- 박성민 (3)
    ('가창오리 군무는 진짜 꼭 봐야 합니다',
     '서천 금강하구 제방 근처에서 해질 무렵 가창오리 떼를 봤어요. 하늘을 가득 메우는데 숨이 멎는 줄 알았어요. 영상 찍었는데 소름...!',
     'NORMAL', 'VISIBLE_ALL', 0, NOW(), NOW(), 3),

    -- 김루시아 (4)
    ('백령도 검은머리물떼새, 드디어 직접 봤어요 🖤',
     '사진으로만 보던 그 새를... 진짜 갯벌에서 만났어요. 생각보다 작고 귀엽고, 움직임도 섬세해요. 백령도 가는 길은 멀지만 그럴만한 가치 있어요!',
     'NORMAL', 'VISIBLE_ALL', 0, NOW(), NOW(), 4),

    -- 김민상 (1) 두 번째
    ('시화호 갈대길에서 만난 도요새 이야기',
     '따뜻한 봄날, 시화호 갈대길을 걷다가 도요새 무리를 만났어요. 부리가 길고 바쁘게 움직이던 모습이 인상적이었어요. 한참을 망원경으로 관찰했네요.',
     'NORMAL', 'VISIBLE_ALL', 0, NOW(), NOW(), 1);

-- 시퀀스 재설정
SELECT setval('post_post_id_seq', (SELECT MAX(post_id) FROM post));
