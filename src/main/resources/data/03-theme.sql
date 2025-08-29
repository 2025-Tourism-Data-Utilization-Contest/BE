-- [THEME 더미 데이터]
-- 기존 데이터 삭제 및 시퀀스 초기화
TRUNCATE TABLE theme RESTART IDENTITY CASCADE;

-- [1. 순천만습지]
-- 1. Theme insert + theme_id 반환
WITH inserted_theme AS (
INSERT
INTO theme (title, origin_title, theme_image, address,
            location_x, location_y, location_intro,
            created_at, updated_at)
VALUES (
    '흑두루미가 모여드는 순천만 습지', '순천만 습지', '${S3_BASE_URL}/theme/suncheon.png', '전남 순천시 순천만길 513-25',
    34.8857970100738, 127.5092833426,
    '순천만은 전라남도 순천시에 있는 아주 넓은 습지예요. 바다와 강이 만나는 곳이라서 물이 깨끗하고, 갈대밭과 갯벌이 넓게 펼쳐져 있어요.', NOW(), NOW()
    )
    RETURNING theme_id
    ),

-- 2. highlight_points insert
    insert_highlights AS (
INSERT
INTO theme_highlight_points (theme_id, list_order, highlight_point)
SELECT theme_id, list_order, highlight_point
FROM inserted_theme, (
    VALUES
    (0, '순천만은 깨끗한 물과 넓은 갈대밭, 갯벌이 있어 흑두루미 같은 특별한 새들이 겨울을 보내는 곳이에요.'),
    (1, '사람들이 많이 찾아오면서 쓰레기와 소음, 매연 등으로 습지가 위험해졌지만, 모두가 힘을 합쳐 순천만을 지켰어요.'),
    (2, '전봇대를 없애고 논에 흑두루미 모양 벼를 심는 등 다양한 노력을 한 덕분에, 지금은 흑두루미와 철새들이 다시 돌아오는 소중한 습지가 되었어요.')
    ) AS highlights(list_order, highlight_point)
    ),

-- 3. season insert
    insert_season AS (
INSERT
INTO theme_season (theme_id, season)
SELECT theme_id, 'WINTER'
FROM inserted_theme
    ),

-- 4. daytime insert
    insert_daytime AS (
INSERT
INTO theme_daytime (theme_id, daytime)
SELECT theme_id, 'DAY'
FROM inserted_theme
    )

-- 5. description_blocks insert
INSERT
INTO theme_description_blocks (theme_id, list_order, description_title, description)
SELECT theme_id, block.list_order, block.description_title, block.description
FROM inserted_theme,
     LATERAL (
              VALUES (0, '순천만은 어떤 곳일까?',
                      '순천만은 전라남도 순천시에 있는 아주 넓은 습지예요. 바다와 강이 만나는 곳이라서 물이 깨끗하고, 갈대밭과 갯벌이 넓게 펼쳐져 있어요. 그래서 새들이 먹이를 찾고 쉴 수 있는 곳이 많아요. 순천만에는 200종류가 넘는 새들이 살고 있는데, 그중에는 흑두루미처럼 귀하고 특별한 새도 있어요. 흑두루미는 멀리 다른 나라에서 날아와 매년 겨울을 순천만에서 보내요. 그래서 순천만은 전 세계에서도 유명한 새들의 집이에요.'),
                     (1, '관광객이 몰려오면서 달라진 순천만',
                      '예전에는 순천만을 구경하러 오는 사람이 별로 없었어요. 그런데 순천만이 TV나 인터넷에 나오면서 많은 사람들이 놀러 오기 시작했어요. 몇 년 만에 순천만에 오는 사람이 10만 명에서 300만 명까지 늘어났어요. 차가 많아지고 사람들이 많이 오니까 순천만에 쓰레기, 소음, 자동차 매연 같은 문제가 생겼어요. 새들도 점점 오지 않게 되고, 근처에 사는 사람들도 불편해했어요.'),
                     (2, '순천만을 살리기 위한 모두의 아이디어',
                      '이런 일이 생기자 순천시에 사는 주민들과 여러 단체, 그리고 시청이 힘을 모았어요. 순천만을 보호하기 위해 여러 가지 아이디어를 냈어요. 전문가들과 함께 순천만을 어떻게 잘 관리할지 연구했어요. 차가 쉽게 습지까지 들어오지 못하게 막고, 순천만과 도시 사이에는 꽃과 나무를 많이 심었어요. 이곳이 바로 순천만국가정원이에요. 또, 순천만에서 국제정원박람회라는 큰 행사가 열리기도 했어요.'),
                     (3, '젓봇대와 흑두루미',
                      '전봇대가 흑두루미에게 위협이 된다는 사실 알고 있었나요? 논에 세워진 전봇대 때문에 흑두루미가 다치기도 했어요. 그래서 전봇대를 없애고, 대신 논에 흑두루미 모양으로 검은 벼를 심었어요. 이것을 경관농업이라고 해요. 그리고 사람들이 쓰레기를 줄이고, 새를 방해하지 않으려고 조용히 관찰하는 약속도 만들었어요.'),
                     (4, '앞으로도 계속 이어져야 할 순천만의 이야기',
                      '이렇게 많은 사람들이 힘을 모으자 순천만의 자연이 점점 살아나기 시작했어요. 흑두루미와 여러 철새들이 다시 순천만으로 돌아왔어요. 1996년에는 흑두루미가 59마리밖에 없었지만, 2018년에는 2,500마리가 넘게 왔어요. 순천만은 지금도 많은 새들과 사람들이 함께 지키고 있는 아름다운 습지예요. 앞으로도 순천만을 아끼고 보호하면, 더 많은 새들이 이곳에서 살아갈 수 있을 거예요. 우리도 순천만을 방문해서 자연을 보존하려는 사람들의 노력을 느끼고 아름다운 흑두루미를 구경하는 것은 어떨까요?')
         ) AS block(list_order, description_title, description);

-- [2. 창녕 우포늪]
-- Theme insert + theme_id 반환
WITH inserted_theme AS (
INSERT
INTO theme (title, origin_title, theme_image, address,
            location_x, location_y, location_intro,
            created_at, updated_at)
VALUES (
    '따오기가 돌아온 창녕 우포늪', '창녕 우포늪', '${S3_BASE_URL}/theme/changnyeong.png', '경상남도 창녕군 유어면 우포늪길 220',
    35.5445608846934, 128.41911947802,
    '우포늪은 경상남도 창녕에 있는 우리나라에서 가장 넓은 자연 늪지예요. 다양한 동식물이 살아가며, 사계절 내내 다양한 풍경과 새들을 만날 수 있는 생태 천국이에요.',
    NOW(), NOW()
    )
    RETURNING theme_id
    ),

-- highlight_points insert
    insert_highlights AS (
INSERT
INTO theme_highlight_points (theme_id, list_order, highlight_point)
SELECT theme_id, list_order, highlight_point
FROM inserted_theme, (
    VALUES
    (0, '우포늪은 다양한 새와 동식물이 사는 우리나라에서 가장 넓은 자연 늪지예요.'),
    (1, '옛날에는 개발 때문에 사라질 뻔했지만, 많은 사람들이 힘을 합쳐 지금은 소중하게 보호되고 있어요.'),
    (2, '따오기는 한때 완전히 사라졌다가, 우포늪에서 다시 만나게 된 특별한 새예요.')
    ) AS highlights(list_order, highlight_point)
    ),

-- season insert
    insert_season AS (
INSERT
INTO theme_season (theme_id, season)
SELECT theme_id, unnest(ARRAY['SUMMER'])
FROM inserted_theme
    ),

-- daytime insert
    insert_daytime AS (
INSERT
INTO theme_daytime (theme_id, daytime)
SELECT theme_id, 'DAY'
FROM inserted_theme
    )

-- description_blocks insert
INSERT
INTO theme_description_blocks (theme_id, list_order, description_title, description)
SELECT theme_id, block.list_order, block.description_title, block.description
FROM inserted_theme,
     LATERAL (
              VALUES (0, '우포늪은 어떤 곳일까?',
                      '우포늪은 경상남도 창녕에 있는 우리나라에서 가장 넓은 늪이에요. 바닥에 물이 가득하고, 여러 식물과 동물, 그리고 새들이 함께 살고 있어요. 계절마다 풍경이 달라져요. 봄과 가을에는 멀리서 온 새들이 잠깐 쉬었다 가고, 여름에는 연꽃과 개구리밥 같은 풀이 자라며, 하얀 백로가 물가에서 먹이를 찾아요. 겨울이 되면 북쪽에서 온 큰기러기와 고니 같은 새들이 우포늪에서 겨울을 보내요. 1년 내내 다양한 새들을 만날 수 있는 곳이에요.'),
                     (1, '우포늪을 지켜낸 이야기',
                      '예전에는 우포늪이 쓸모없는 땅이라고 생각해서, 논으로 만들거나 공장, 쓰레기 버리는 곳으로 바꾸려고 했어요. 정말로 논을 만들고 늪을 메우는 공사도 있었고, 나중에는 쓰레기 매립장이 생길 뻔한 적도 있었어요. 하지만 많은 사람들과 정부가 우포늪을 꼭 지키자고 힘을 합쳤어요. 처음에는 반대하는 사람들도 있었지만, 끝까지 노력해서 1997년에 자연을 보호하는 지역이 되었고, 1998년에는 세계적으로도 인정받는 람사르습지가 되었어요. 이제 우포늪은 모두가 아끼는 자연의 보물이 되었어요.'),
                     (2, '우포의 상징, 따오기',
                      '따오기는 예전에는 논과 습지에서 자주 볼 수 있었지만, 환경이 나빠지고 사냥이 많아지면서 1970년대에 우리나라에서 완전히 사라졌어요. 많은 사람들이 따오기를 다시 보고 싶어 했고, 중국에서 따오기를 데려와 우포늪에서 기르기 시작했어요. 지금은 400마리가 넘는 따오기가 우포늪에서 살고 있어요. 온몸이 하얗고 얼굴이 빨간 따오기는 봄에는 둥지를 짓고, 여름엔 새끼와 먹이를 찾아다녀요. 이제 따오기는 우포늪의 상징이자, 모두가 함께 지켜야 할 소중한 새가 되었어요.'),
                     (3, '따오기 노래에 담긴 이야기',
                      '우리나라에는 ‘따오기’라는 동요도 전해져요. 동요 ‘따오기’에는, 일제강점기 시절의 슬픈 현실과 그리움이 담겨 있어요. 노래 속 따오기는 ‘어머니가 가신 나라’이자 ‘해가 뜨는 일본’으로 떠나간다는 내용을 담고 있어요. 이것은 그때 많은 사람들이 힘든 조선의 현실을 살아가며, 고향을 떠나야 했던 아픔과 그리움을 상징적으로 표현한 거예요. 그래서 이 노래는 단순한 새 노래가 아니라, 조선 민족의 한과 슬픔이 느껴진다고 하여 일제강점기 때 금지곡이 되기도 했어요.')
         ) AS block(list_order, description_title, description);

-- [3. 서천 금강하구]
WITH inserted_theme AS (
INSERT
INTO theme (title, origin_title, theme_image, address,
            location_x, location_y, location_intro,
            created_at, updated_at)
VALUES (
    '가창오리의 군무가 펼쳐지는 서천 금강하구', '서천 금강하구', '${S3_BASE_URL}/theme/seocheon.png',
    '충청남도 서천군 마서면 장산로855번길 56-2', 36.0199362613608, 126.736710964485,
    '금강하구는 충남 서천과 전북 군산 사이에 펼쳐진 넓은 강과 습지예요. 겨울이면 시베리아에서 온 철새들이 이곳에 머물며, 논과 갈대밭, 갯벌이 어우러져 새들에게 먹이와 쉴 곳을 제공해요.',
    NOW(), NOW()
    )
    RETURNING theme_id
    ),

-- highlight_points
    insert_highlights AS (
INSERT
INTO theme_highlight_points (theme_id, list_order, highlight_point)
SELECT theme_id, list_order, highlight_point
FROM inserted_theme, (
    VALUES
    (0, '금강하구는 겨울이 되면 수십만 마리의 가창오리가 모여드는 전국 최고의 철새 명소예요.'),
    (1, '가창오리는 해질 무렵 하늘을 뒤덮으며 화려한 군무를 펼쳐요.'),
    (2, '가창오리는 전 세계적으로 개체수가 적은 희귀한 새예요.')
    ) AS highlights(list_order, highlight_point)
    ),

-- season
    insert_season AS (
INSERT
INTO theme_season (theme_id, season)
SELECT theme_id, 'WINTER'
FROM inserted_theme
    ),

-- daytime
    insert_daytime AS (
INSERT
INTO theme_daytime (theme_id, daytime)
SELECT theme_id, 'NIGHT'
FROM inserted_theme
    )

-- description_blocks
INSERT
INTO theme_description_blocks (theme_id, list_order, description_title, description)
SELECT theme_id, block.list_order, block.description_title, block.description
FROM inserted_theme,
     LATERAL (
              VALUES (0, '금강하구는 어떤 곳일까?',
                      '금강하구는 충남 서천과 전북 군산 사이에 펼쳐진 넓은 강과 습지예요. 겨울이면 시베리아에서 온 철새들이 이곳에 머물며, 논과 갈대밭, 갯벌이 어우러져 새들에게 먹이와 쉴 곳을 제공해요. 추수가 끝난 겨울 논에도 먹을 것이 많아, 금강하구는 새들에게 진짜 ‘겨울 천국’이랍니다.'),

                     (1, '겨울철새의 주인공, 가창오리',
                      '가창오리는 원래 시베리아에서 여름을 보내고, 겨울이 되면 따뜻한 한반도로 내려와 쉬어요. 이곳에선 수십만 마리가 모여 엄청난 떼를 이루며 살아가요. 낮에는 금강호 같은 넓은 호수에서 쉬고, 해가 지면 논으로 이동해 먹이를 찾아요. 이때 하늘을 가득 메우는 화려한 군무는 정말 잊을 수 없는 장관이에요.'),

                     (2, '가창오리는 왜 특별할까요?',
                      '가창오리는 흔해 보이지만 실제로는 전 세계에 40~50만 마리밖에 없는 멸종위기종이에요. 그 중 90%가 한반도에서 겨울을 보내요. 금강하구 외에도 고창 동림저수지, 해남 고천암호 등에서도 볼 수 있어요.'),

                     (3, '가창오리 군무를 보려면?',
                      '가창오리는 해질 무렵 논으로 이동하면서 하늘에서 장관을 이루는 군무를 펼쳐요. 오후 늦게 제방 근처에서 조용히 기다리면 하늘을 수놓는 장면을 볼 수 있어요. 너무 가까이 다가가면 스트레스를 줄 수 있으니, 조용히 지켜보는 매너가 필요해요.')
         ) AS block(list_order, description_title, description);

-- [4. 계룡산 호반새]
WITH inserted_theme AS (
INSERT
INTO theme (title, origin_title, theme_image, address,
            location_x, location_y, location_intro,
            created_at, updated_at)
VALUES (
    '호로로로~ 계룡산의 여름을 알리는 호반새', '계룡산', '${S3_BASE_URL}/theme/gyeryongsan.png',
    '충청남도 공주시 계룡면 갑사로 567-3', 36.3660798305223, 127.186445550903,
    '계룡산국립공원은 16개의 봉우리와 10여 개 계곡이 펼쳐진 충남의 대표 명산이에요. 여름이면 맑은 계곡 숲에서 ‘호로로로~’ 소리를 내는 호반새를 만날 수 있어요.',
    NOW(), NOW()
    )
    RETURNING theme_id
    ),

-- highlight_points
    insert_highlights AS (
INSERT
INTO theme_highlight_points (theme_id, list_order, highlight_point)
SELECT theme_id, list_order, highlight_point
FROM inserted_theme, (
    VALUES
    (0, '계룡산국립공원은 16개의 봉우리와 10여 개 계곡이 펼쳐진 충남의 대표 명산이에요.'),
    (1, '여름이면 맑은 계곡 숲에서 ‘호로로로~’ 소리를 내는 호반새를 만날 수 있어요.'),
    (2, '호반새가 사는 곳은 생물들이 다양하게 어울려 사는 건강한 숲이라는 뜻이에요.')
    ) AS highlights(list_order, highlight_point)
    ),

-- season
    insert_season AS (
INSERT
INTO theme_season (theme_id, season)
SELECT theme_id, 'SUMMER'
FROM inserted_theme
    ),

-- daytime
    insert_daytime AS (
INSERT
INTO theme_daytime (theme_id, daytime)
SELECT theme_id, 'DAY'
FROM inserted_theme
    )

-- description_blocks
INSERT
INTO theme_description_blocks (theme_id, list_order, description_title, description)
SELECT theme_id, block.list_order, block.description_title, block.description
FROM inserted_theme,
     LATERAL (
              VALUES (0, '계룡산은 어떤 곳일까?',
                      '계룡산국립공원은 충남에서 가장 유명한 산이에요. 1968년에 우리나라에서 두 번째로 국립공원이 되었어요. 정상인 천황봉을 중심으로 16개의 봉우리와 10개나 되는 계곡이 이어져 있어요. 산의 모양이 닭벼슬을 쓴 용을 닮았다고 해서 ‘계룡산’이라는 이름이 붙었어요. 옛날부터 나라의 제사를 지내는 신성한 산으로, 서울이나 대전에서도 하루 만에 다녀올 수 있어 많은 사람들이 찾는 곳이에요.'),

                     (1, '계룡산의 깃대종, 호반새',
                      '계룡산의 상징 같은 새는 바로 호반새예요. 호반새는 깨끗한 계곡과 숲이 만나는 곳에서만 살아요. 여름이 되면 푸른 날개와 붉은 부리를 가진 호반새가 계곡을 따라 나타나요. 호반새는 나무 구멍에 둥지를 틀고, 곤충, 작은 물고기, 무척추동물 등 여러 생물을 잡아먹어요. 그래서 호반새가 산다는 건 계룡산 숲과 계곡이 아주 건강하다는 뜻이에요. 특히 6월과 7월, 계곡을 걷다 보면 ‘호로로로~’ 특이한 소리를 들을 수 있는데, 바로 호반새의 울음소리예요. 소리를 따라 조심조심 걷다 보면 붉은 깃털의 호반새를 만날 수 있을 거예요.'),

                     (2, '계룡산의 전설',
                      '계룡산에는 재미있는 전설도 전해져요. 백제 시대 한 왕족이 호랑이를 구해주고 호랑이가 여인을 데려다 주었대요. 왕족과 여인은 의남매가 되어 불도를 닦았고, 나중에 훌륭한 스님이 되어 나란히 입적했다는 이야기예요. 이 전설을 기념해 세운 오뉘탑도 계룡산에 남아 있어요.')
         ) AS block(list_order, description_title, description);

-- [5. 시화호 도요새]
WITH inserted_theme AS (
INSERT
INTO theme (title, origin_title, theme_image, address,
            location_x, location_y, location_intro,
            created_at, updated_at)
VALUES (
    '갯벌 위 작은 여행자, 시화호의 도요새', '시화호', '${S3_BASE_URL}/theme/sihwaho.png', '경기도 안산시 단원구 대부황금로 1927번지',
    37.3111380309664, 126.606355977201, '시화호는 한때 오염으로 유명했지만, 자연의 힘과 사람들의 노력 덕분에 다시 새들의 쉼터가 되었어요.',
    NOW(), NOW()
    )
    RETURNING theme_id
    ),

-- highlight_points
    insert_highlights AS (
INSERT
INTO theme_highlight_points (theme_id, list_order, highlight_point)
SELECT theme_id, list_order, highlight_point
FROM inserted_theme, (
    VALUES
    (0, '시화호는 한때 오염으로 유명했지만, 자연의 힘과 사람들의 노력 덕분에 다시 새들의 쉼터가 되었어요.'),
    (1, '도요새는 호주와 시베리아를 오가며 봄·가을에 시화호 갯벌에 들러 힘을 충전하는 세계적인 여행자예요.'),
    (2, '시화호 갈대밭길을 천천히 달리며 도요새와 갯벌의 생명을 만나는 특별한 탐조 여행을 떠나볼 수 있어요.')
    ) AS highlights(list_order, highlight_point)
    ),

-- season
    insert_season AS (
INSERT
INTO theme_season (theme_id, season)
SELECT theme_id, unnest(ARRAY['SPRING'])
FROM inserted_theme
    ),

-- daytime
    insert_daytime AS (
INSERT
INTO theme_daytime (theme_id, daytime)
SELECT theme_id, 'DAY'
FROM inserted_theme
    )

-- description_blocks
INSERT
INTO theme_description_blocks (theme_id, list_order, description_title, description)
SELECT theme_id, block.list_order, block.description_title, block.description
FROM inserted_theme,
     LATERAL (
              VALUES (0, '시화호는 어떤 곳일까?',
                      '시화호는 원래 바다였던 곳에 커다란 방조제를 만들어 바닷물을 막아 만든 인공 호수예요. 처음엔 오염이 심해 걱정이 많았지만 지금은 자연이 스스로 살아나 새와 식물들이 함께 사는 소중한 생태 공간이 되었어요. 길게 이어진 방조제와 갈대밭 그리고 바닷물이 드나드는 갯벌 덕분에 수많은 철새들이 이곳을 찾아와요. 특히 간척지 도로를 따라 드라이브하면, TV 드라마나 영화에서 보던 멋진 갈대평원과 새들을 가까이에서 볼 수 있답니다.'),

                     (1, '시화호의 작은 여행자, 도요새',
                      '도요새는 부리가 길고 다리가 얇은 작은 새예요. 아주 멀리까지 날아가는 힘이 있어요. 봄과 가을에는 시베리아에서 태어나고 겨울이 되면 호주나 뉴질랜드로 떠나요. 여행을 하는 도중에 우리나라 시화호 갯벌에 들러요. 여기에서 잠깐 쉬고 먹이를 먹으면서 힘을 얻어요.'),

                     (2, '갯벌과 갈대밭, 새들의 천국이 된 시화호',
                      '시화호는 예전엔 바다였지만, 방조제를 만들면서 인공 호수와 간척지로 바뀌었어요. 처음엔 공장 오염수 때문에 ‘헬게이트’라는 별명까지 있었지만, 지금은 오염이 많이 줄었고 새들이 다시 돌아왔어요. 도로 옆에 차를 세우고 망원경으로 새들을 관찰하는 탐조가도 많아요. 새들이 놀라지 않게 조심히 다니고 차를 천천히 움직이면 더 가까이에서 도요새와 다른 철새들을 볼 수 있어요.'),

                     (3, '시화호에서 느끼는 자연과 여행',
                      '시화호에는 봄과 가을마다 수많은 도요새와 물새들이 찾아와요. 이 새들은 우리나라를 아주 잠깐 쉬어가는 ‘여행자’이기 때문에 조용히 바라보며 자연의 소중함을 함께 느껴보는 게 좋아요. 갈대밭과 갯벌 그리고 그 위를 바쁘게 움직이는 도요새들을 보면 매일 반복되는 일상에서 벗어난 특별한 여행이 된답니다. 망원경이나 카메라를 챙겨서 시화호만의 갯벌 여행을 떠나보세요! 여유롭게 드라이브하고, 근처 맛집이나 시장도 둘러보면 더 즐거운 하루가 될 거예요.')
         ) AS block(list_order, description_title, description);

-- [6. 백령도 검은머리물떼새]
WITH inserted_theme AS (
INSERT
INTO theme (title, origin_title, theme_image, address,
            location_x, location_y, location_intro,
            created_at, updated_at)
VALUES (
    '백령도, 봄과 가을에 만나는 검은머리물떼새', '백령도', '${S3_BASE_URL}/theme/baekryeongdo.png', '인천광역시 옹진군 백령면',
    37.9743612082699, 124.718710475439, '백령도는 절벽과 바다 그리고 신기한 지질이 가득한 서해의 멋진 섬이에요.', NOW(), NOW()
    )
    RETURNING theme_id
    ),

-- highlight_points
    insert_highlights AS (
INSERT
INTO theme_highlight_points (theme_id, list_order, highlight_point)
SELECT theme_id, list_order, highlight_point
FROM inserted_theme, (
    VALUES
    (0, '백령도는 절벽과 바다 그리고 신기한 지질이 가득한 서해의 멋진 섬이에요.'),
    (1, '봄과 가을이면 검은머리물떼새 같은 귀한 새들이 이 섬 갯벌에 찾아와요.'),
    (2, '배를 타고 떠나는 백령도 여행에서는 아름다운 풍경과 새들을 함께 만날 수 있어요.')
    ) AS highlights(list_order, highlight_point)
    ),

-- season
    insert_season AS (
INSERT
INTO theme_season (theme_id, season)
SELECT theme_id, unnest(ARRAY['AUTUMN'])
FROM inserted_theme
    ),

-- daytime
    insert_daytime AS (
INSERT
INTO theme_daytime (theme_id, daytime)
SELECT theme_id, 'DAY'
FROM inserted_theme
    )

-- description_blocks
INSERT
INTO theme_description_blocks (theme_id, list_order, description_title, description)
SELECT theme_id, block.list_order, block.description_title, block.description
FROM inserted_theme,
     LATERAL (
              VALUES (0, '백령도는 어떤 곳일까?',
                      '백령도는 우리나라 서해에서 가장 북쪽에 있는 큰 섬이에요. 인천에서 배를 타고 네 시간쯤 가야 도착해요. 날씨가 맑으면 북한 땅도 보일 만큼 가까운 곳이에요. 섬을 따라 걸으면 두무진이라는 큰 절벽과 사곶사빈처럼 고운 모래사장 그리고 콩돌해안과 같은 특이한 바닷가를 만날 수 있어요. 백령도에는 아주 오래된 돌들과 멋진 바위들이 있어서 섬 전체가 하나의 커다란 자연 박물관 같아요.'),

                     (1, '백령도에서 만나는 검은머리물떼새',
                      '검은머리물떼새는 머리는 까맣고 부리와 다리는 빨간색이라 멀리서도 잘 보여요. 이 새는 우리나라에서 보기 힘든 멸종위기종이에요. 봄과 가을에 백령도 갯벌을 찾는 검은머리물떼새는, 긴 다리로 갯벌을 걸으며 게와 조개 그리고 갯지렁이 같은 먹이를 찾아요. 백령도 갯벌은 이 검은머리물떼새에게 아주 소중한 집이에요.'),

                     (2, '백령도에서 즐기는 여행',
                      '백령도에서는 멋진 바위 절벽도 보고 고운 모래사장도 밟아볼 수 있어요. 바다를 따라 걷다 보면 귀한 새들과 만나는 특별한 순간이 찾아와요. 검은머리물떼새처럼 멋진 새를 가까이서 만나고 싶다면 봄이나 가을에 백령도로 여행을 떠나보는 건 어때요? 자연 속에서 신기한 경험을 할 수 있을 거예요.')
         ) AS block(list_order, description_title, description);

-- [7. 한려해상국립공원 - 팔색조]
WITH inserted_theme AS (
INSERT
INTO theme (title, origin_title, theme_image, address,
            location_x, location_y, location_intro,
            created_at, updated_at)
VALUES (
    '한려해상 숲에서 만나는 팔색조의 노래', '한려해상국립공원', '${S3_BASE_URL}/theme/hanryupark.png', '경상남도 통영시 용남면 용남해안로 116',
    34.849128, 128.418579, '한려해상국립공원은 바다와 섬, 숲이 어우러진 우리나라 최초의 해상국립공원이에요. 산책길과 해안길, 섬마다 아름다운 풍경이 펼쳐져 있어요.',
    NOW(), NOW()
    )
    RETURNING theme_id
    ),

-- highlight_points
    insert_highlights AS (
INSERT
INTO theme_highlight_points (theme_id, list_order, highlight_point)
SELECT theme_id, list_order, highlight_point
FROM inserted_theme, (
    VALUES
    (0, '한려해상국립공원은 바다와 섬, 숲이 어우러진 우리나라 최초의 해상국립공원이에요.'),
    (1, '팔색조는 온몸이 여러 가지 색으로 빛나는 아주 특별한 새예요. 봄과 여름에 한려해상 숲에서 노래하며 둥지를 틀어요.'),
    (2, '팔색조를 만나고 싶다면, 조용히 숲길을 걸으며 귀를 기울여보세요. “피윗! 피윗!” 하는 소리가 들리면 팔색조가 가까이에 있다는 신호예요.')
    ) AS highlights(list_order, highlight_point)
    ),

-- season
    insert_season AS (
INSERT
INTO theme_season (theme_id, season)
SELECT theme_id, unnest(ARRAY['SPRING'])
FROM inserted_theme
    ),

-- daytime
    insert_daytime AS (
INSERT
INTO theme_daytime (theme_id, daytime)
SELECT theme_id, 'DAY'
FROM inserted_theme
    )

-- description_blocks
INSERT
INTO theme_description_blocks (theme_id, list_order, description_title, description)
SELECT theme_id, block.list_order, block.description_title, block.description
FROM inserted_theme,
     LATERAL (
              VALUES (0, '한려해상국립공원은 어떤 곳일까?',
                      '한려해상국립공원은 경상남도와 전라남도 사이에 펼쳐진 바다와 섬, 그리고 숲이 어우러진 멋진 자연공원이예요. 이곳에는 작은 섬들이 수백 개나 흩어져 있고, 바다와 산, 숲을 함께 즐길 수 있어요. 산책로를 따라 걷다 보면 바다 풍경과 숲 속의 다양한 생물들을 만날 수 있어요.'),

                     (1, '봄과 여름에 찾아오는 팔색조',
                      '팔색조는 한국에서만 볼 수 있는 아주 희귀한 새예요. 등, 배, 날개에 여러 가지 색이 섞여 있어서 ‘숲의 보석’이라고도 불려요. 팔색조는 매년 봄과 여름이면 따뜻한 남쪽에서 한려해상국립공원 숲으로 날아와 둥지를 틀고, 알을 낳아요. 특히 “피윗!” 하고 부르는 소리가 숲속에 울려 퍼지는데, 이 소리를 들으면 팔색조가 근처에 있다는 뜻이에요. 팔색조는 나뭇가지 아래, 그늘진 곳에 숨듯이 살기 때문에 조용히 귀 기울여야 만날 수 있어요.'),

                     (2, '팔색조가 찾아오는 이유',
                      '팔색조는 곤충, 지렁이, 달팽이 같은 숲속 먹이를 찾아 이곳을 찾아와요. 한려해상국립공원은 숲이 울창하고, 바람이 시원해서 팔색조가 새끼를 키우기에 딱 좋은 곳이에요. 이 새는 우리나라 천연기념물이기도 해서 모두가 함께 보호하고 있어요. 한려해상국립공원에 가면, 바다도 보고 숲길도 걸으며 팔색조의 노래를 들을 수 있어요. 숲속에서 ‘피~윗!’ 소리가 들린다면, 바로 팔색조를 만날 기회니까 조용히 귀 기울여보세요!')
         ) AS block(list_order, description_title, description);

-- 시퀀스 재설정
SELECT setval('theme_theme_id_seq', (SELECT MAX(theme_id) FROM theme));
