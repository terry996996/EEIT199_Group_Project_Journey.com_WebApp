IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'EEIT199travel')
BEGIN
    CREATE DATABASE EEIT199travel;
END
GO

USE EEIT199travel;
GO

/* =========== Tables Without FKs ===========*/
-----  Users ----- 
IF OBJECT_ID('user_types', 'U') IS NULL
BEGIN
    CREATE TABLE user_types(
        id INT PRIMARY KEY IDENTITY(1,1),
        [type] VARCHAR(10) UNIQUE NOT NULL
    );

    INSERT INTO user_types([type])
    VALUES ('user'),('admin'),('vendor');
END;
GO
-----  Users ----- 

-----  Locations ----- 
IF OBJECT_ID('continents', 'U') IS NULL
BEGIN
    CREATE TABLE continents(
        [name] VARCHAR(40) PRIMARY KEY,
        name_zh NVARCHAR(10) UNIQUE NOT NULL
    );

    INSERT INTO continents
    VALUES
    ('Africa', N'非洲'),('Asia', N'亞洲'),
    ('Europe', N'歐洲'),('NorthAmerica', N'北美洲'),
    ('Oceania', N'大洋洲'),('SouthAmerica', N'南美洲');
END;
GO
-----  Locations ----- 

-----  Attractions ----- 
IF OBJECT_ID('attraction_types', 'U') IS NULL
BEGIN
    CREATE TABLE attraction_types (
        id INT PRIMARY KEY IDENTITY(1,1),
        [type] NVARCHAR(50) NOT NULL UNIQUE 
    );

    INSERT INTO attraction_types([type])
    VALUES
    (N'景點通票'),(N'觀景台'),(N'展演活動'),
    (N'樂園'),(N'動物園或水族館'),(N'博物館&美術館'),
    (N'自然風景'),(N'農場&觀光工廠'),(N'室內遊戲'),
    (N'歷史景點'),(N'免費景點推薦');
END;
GO

IF OBJECT_ID('attraction_tags', 'U') IS NULL
BEGIN
    CREATE TABLE attraction_tags (
        id INT PRIMARY KEY IDENTITY(1,1), 
        tag_name NVARCHAR(50) NOT NULL UNIQUE  
    );

    INSERT INTO attraction_tags(tag_name)
    VALUES
    (N'即買即用'),(N'立即確認'),(N'15天免費退票'),
    (N'期間內隨時可用'),(N'出發前可免費取消'),(N'3天免費退票'),
    (N'免費'),(N'親子適合'),(N'熱門');
END;
GO
-----  Attractions ----- 

-----  Comments ----- 
IF OBJECT_ID('comment_reasons', 'U') IS NULL
BEGIN
    CREATE TABLE comment_reasons (
        id INT IDENTITY(1,1) PRIMARY KEY,
        reason_text NVARCHAR(100) NOT NULL,
        is_active BIT NOT NULL DEFAULT 1,
        created_at DATETIME DEFAULT GETDATE(),
        updated_at DATETIME DEFAULT GETDATE()
    );

    INSERT INTO comment_reasons (reason_text, is_active, created_at, updated_at)
    VALUES 
    (N'色情內容', 1, GETDATE(), GETDATE()),
    (N'廣告行為', 1, GETDATE(), GETDATE()),
    (N'人身攻擊', 1, GETDATE(), GETDATE()),
    (N'仇恨言論', 1, GETDATE(), GETDATE()),
    (N'不實資訊', 1, GETDATE(), GETDATE()),
    (N'垃圾留言', 1, GETDATE(), GETDATE()),
    (N'涉及違法', 1, GETDATE(), GETDATE());
END;
GO
-----  Comments ----- 

-----  Flights ----- 
IF OBJECT_ID('flight_classes', 'U') IS NULL
BEGIN
    CREATE TABLE flight_classes (
        class_id INT PRIMARY KEY IDENTITY(1,1),   -- 艙等 ID，自動遞增
        class_type VARCHAR(10) NOT NULL UNIQUE    -- 艙等類型（經濟艙、商務艙、頭等艙等）
    );
	INSERT INTO flight_classes (class_type) VALUES
		(N'頭等艙'),
		(N'商務艙'),
		(N'經濟艙');

END;
GO

IF OBJECT_ID('airlines', 'U') IS NULL
BEGIN
    CREATE TABLE airlines (
        airline_code VARCHAR(2) PRIMARY KEY NOT NULL,     -- 航空公司代碼（例如 BR、CI）
        airline_name VARCHAR(100) NOT NULL,               -- 航空公司名稱
        [image_url] VARCHAR(MAX),                         -- 圖片（儲存 LOGO） 7/2調整型別，存圖片路徑
        image_type VARCHAR(20)                            -- 圖片類型（例如 image/png、image/jpeg）
    );
		INSERT INTO airlines (airline_code, airline_name,image_url) VALUES ('BR', N'長榮航空','https://static.cdn-eztravel.com.tw/flight/static/images/air_icon/air_l/br_l.png');
		INSERT INTO airlines (airline_code, airline_name,image_url) VALUES ('CI', N'中華航空','https://static.cdn-eztravel.com.tw/flight/static/images/air_icon/air_l/ci_l.png');
		INSERT INTO airlines (airline_code, airline_name,image_url) VALUES ('OZ', N'韓亞航空','https://static.cdn-eztravel.com.tw/flight/static/images/air_icon/air_l/oz_l.png');
		INSERT INTO airlines (airline_code, airline_name,image_url) VALUES ('JL', N'日本航空','https://static.cdn-eztravel.com.tw/flight/static/images/air_icon/air_l/jl_l.png');
		INSERT INTO airlines (airline_code, airline_name,image_url) VALUES ('CX', N'國泰航空','https://static.cdn-eztravel.com.tw/flight/static/images/air_icon/air_l/cx_l.png');
		INSERT INTO airlines (airline_code, airline_name,image_url) VALUES ('JX', N'星宇航空','https://static.cdn-eztravel.com.tw/flight/static/images/air_icon/air_l/jx_l.png');
		INSERT INTO airlines (airline_code, airline_name,image_url) VALUES ('MM', N'樂桃航空','https://static.cdn-eztravel.com.tw/flight/static/images/air_icon/air_l/mm_l.png');

END;
GO
-----  Flights ----- 

-----  Lodgings ----- 
IF OBJECT_ID('lodgings_type', 'U') IS NULL
BEGIN
    CREATE TABLE lodgings_type (
        id INT IDENTITY(1,1) PRIMARY KEY,
        type_text NVARCHAR(50) UNIQUE,
        is_active BIT NOT NULL DEFAULT 1,
        created_at DATETIME DEFAULT GETDATE(),
        updated_at DATETIME DEFAULT GETDATE()
    );

    INSERT INTO lodgings_type (type_text)
    VALUES (N'飯店'),(N'民宿'),(N'青年旅館'),(N'酒店式公寓');
END;
GO

IF OBJECT_ID('facilities', 'U') IS NULL
BEGIN
    CREATE TABLE facilities (
        id INT IDENTITY(1,1) PRIMARY KEY,
        facility_name NVARCHAR(50) NOT NULL UNIQUE,
        is_active BIT NOT NULL DEFAULT 1,
        created_at DATETIME DEFAULT GETDATE(),
        updated_at DATETIME DEFAULT GETDATE()
    );

    INSERT INTO facilities (facility_name)
    VALUES
    (N'電視'),(N'洗衣機'),(N'廚房'),(N'免費wifi'),
    (N'停車場'),(N'游泳池'),(N'健身房'),(N'酒吧'),
    (N'接駁服務'),(N'會議室'),(N'免費行李寄放'),(N'餐廳'),(N'SPA');
END;
GO

IF OBJECT_ID('bed_types', 'U') IS NULL
BEGIN
    CREATE TABLE bed_types (
        id INT IDENTITY(1,1) PRIMARY KEY,
        [name] NVARCHAR(100) NOT NULL UNIQUE
    );

    INSERT INTO bed_types (name)
    VALUES (N'單人床'),(N'雙人床');
END;
GO
-----  Lodgings ----- 

-----  TypeEnums ----- 
IF OBJECT_ID('type_enum', 'U') IS NULL
BEGIN
    CREATE TABLE type_enum(
        [type_id] INT PRIMARY KEY IDENTITY(1,1),
        [type_name] NVARCHAR(500) NOT NULL UNIQUE 
    );

    INSERT INTO type_enum
    VALUES
    (N'飯店'),(N'機票'),(N'交通票券'),(N'景點票券'),
    (N'行程規劃'),(N'稅務與費用'),(N'平台相關');
END;
GO
-----  TypeEnums ----- 

-----  Payments ----- 
IF OBJECT_ID('payments', 'U') IS NULL
BEGIN
    CREATE TABLE payments(
        payment_tool_id INT PRIMARY KEY IDENTITY(1,1),
        payment_tool NVARCHAR(500) NOT NULL UNIQUE
    );

    INSERT INTO payments 
    VALUES('credit_card'),('line_pay'),('transfer');
END;
GO
-----  Payments ----- 

-----  Coupons ----- 
IF OBJECT_ID('coupon_type', 'U') IS NULL
BEGIN
    CREATE TABLE coupon_type(
        coupon_type_id INT PRIMARY KEY IDENTITY(1,1),
        coupon_type NVARCHAR(MAX) NOT NULL DEFAULT N'一般',
        discount_percent DECIMAL(38, 5) NOT NULL DEFAULT 0,
        discount_amount DECIMAL(38, 5) NOT NULL DEFAULT 0,
        order_amount_limit DECIMAL(38, 5) NOT NULL DEFAULT 0,
        detail NVARCHAR(MAX) 
    );

    -- will update more, may add more marketing elements 
    INSERT INTO coupon_type
    VALUES
    (N'NT$100 折價券', 0, 100, 1000, N'單筆訂單交易滿 NT$1,000，可折抵 NT$100。'),
    (N'NT$300 折價券', 0, 300, 3000, N'單筆訂單交易滿 NT$3,000，可折抵 NT$300。'),
    (N'NT$500 折價券', 0, 500, 5000, N'單筆訂單交易滿 NT$5,000，可折抵 NT$500。'),
    ('NT$1,000 折價券', 0, 1000, 7000, N'單筆訂單交易滿 NT$7,000，可折抵 NT$1,000。'),
    (N'NT$2,000 折價券', 0, 2000, 12000, N'單筆訂單交易滿 NT$12,000，可折抵 NT$2,000。'),

    -- 百分比折扣類型

    (N'單筆訂單 9 折優惠', 10, 0, 5000, N'單筆訂單滿 NT$5,000，可享 9 折優惠，限網頁平台下單可使用。'),
    (N'9 折住宿優惠券（10% OFF）', 10, 0, 0, N'適用於所有房型，最高折抵上限 NT$500，單筆訂單限以總金額折抵一次。'),
    (N'景點門票 8 折券', 20, 0, 0, N'所有景點皆可使用，單筆訂單限以總金額折抵一次。'),
    (N'機票專用 88 折優惠 12% OFF', 12, 0, 0, N'限定國際航線可使用(國內航線不適用此優惠)。'),
    (N'交通票券 9 折優惠', 10, 0, 0 , N'所有交通票券皆使用，單筆訂單限以總金額折抵一次。'),

    -- 活動、特定時機可使用
    (N'NT$300 新戶首購禮券', 0, 300, 1000, N'會員首次下單，該筆訂單交易金額滿NT$2,000，可折抵 NT$300(限定首次下單使用)。'),
    (N'日本住宿爆省 9 折券', 10, 0, 3000, N'下訂日本飯店房型，該筆訂單可享9折優惠,單筆訂單限以總金額折抵一次。'),
    (N'睡好一點！韓國星級飯店 9 折任你挑', 10, 0, 3000, N'下訂韓國飯店房型，該筆訂單可享9折優惠,單筆訂單限以總金額折抵一次。'),
    (N'歐洲線限定｜機票 9 折券', 10, 0, 0, N'限定歐洲航線可使用。'),
    (N'暑假出國必搶！單張機票現折 NT$500', 0, 500, 1000, N'所有航線皆可使用。');
END;
GO

IF OBJECT_ID('platform', 'U') IS NULL
BEGIN
    CREATE TABLE [platform](
        platform_id INT PRIMARY KEY IDENTITY(1,1),
        platform_type NVARCHAR(500) NOT NULL UNIQUE
    );
    INSERT INTO [platform]
    VALUES('web'), ('mobile device');
END;
GO
-----  Coupons ----- 

-----  PointCards ----- 
IF OBJECT_ID('rewards', 'U') IS NULL
BEGIN
    CREATE TABLE rewards(
        reward_id INT PRIMARY KEY IDENTITY(1,1),
        reward_name NVARCHAR(500) NOT NULL UNIQUE,
        point_required INT NOT NULL,
        point_discount DECIMAL(38, 5) NOT NULL DEFAULT 0,
        point_discount_percent DECIMAL(38, 5) NOT NULL DEFAULT 0,
        reward_description NVARCHAR(MAX)
    );

    INSERT INTO rewards
    -- 兌換品項(不限交易時兌換，可於禮物中心兌換)
    VALUES
    (N'NT$100 折價券', 10, 0, 0, N'使用10點會員點數兌換 NT$100 折價券。'),
    (N'NT$300 折價券', 30, 0, 0, N'使用30點會員點數兌換 NT$300 折價券。'),
    (N'NT$500 折價券', 50, 0, 0, N'使用50點會員點數兌換 NT$500 折價券。'),
    (N'NT$1,000 折價券',100, 0, 0, N'使用100點會員點數兌換 NT$1,000 折價券。'),
    (N'NT$2,000 折價券', 180, 0, 0, N'使用180點會員點數兌換 NT$2,000 折價券。'),
    (N'單筆訂單 9 折優惠', 60, 0, 0, N'使用60點會員點數兌換 單筆訂單 9 折優惠。'),
    (N'9 折住宿優惠券（10% OFF）', 50, 0, 0, N'使用50點會員點數兌換 9 折住宿優惠券（10% OFF）。'),
    (N'景點門票 8 折券', 120, 0, 0, N'使用120點會員點數兌換 景點門票 8 折券。'),
    (N'機票專用 88 折優惠 12% OFF', 80, 0, 0, N'使用80點會員點數兌換 機票專用 88 折優惠 12% OFF。'),
    (N'交通票券 9 折優惠', 50, 0, 0 , N'使用50點會員點數兌換 交通票券 9 折優惠。'),
    (N'歐洲線限定｜機票 9 折券', 50, 0, 0, N'使用50點會員點數兌換 歐洲線限定｜機票 9 折券。'),

    -- 兌換當次交易的折價(點數折扣)(限交易時兌換)
    (N'當次交易現折 NT$100', 15, 100, 0, N'扣除15點會員點數，當次交易滿 NT$700 即可現折 NT$100，限當次下單兌換使用。'),
    (N'當次交易現折 NT$500', 70, 500, 0, N'扣除50點會員點數，當次交易滿 NT$3,000 即可現折 NT$500，限當次下單兌換使用。'),
    (N'當次交易現折 NT$1,000', 120, 1000, 0, N'扣除50點會員點數，當次交易滿 NT$6,000 即可現折 NT$1,000，限當次下單兌換使用。'),
    (N'當次交易現享 9 折優惠', 70, 0, 10, N'扣除70點會員點數，當次交易滿 NT$4,000 即可現享 9 折優惠，限當次下單兌換使用。'),
    (N'當次交易現享 85 折', 100, 0, 15, N'扣除100點會員點數，當次交易滿 NT$8,000 可現享 85 折優惠，限當次下單兌換使用。'),
    (N'當次交易現享 8 折', 130, 0, 20, N'扣除130點會員點數，當次交易滿 NT$10,000 可現享 8 折優惠，限當次下單兌換使用。');
END;
GO
-----  PointCards ----- 

----- Consultation ----- 
IF OBJECT_ID('consult_type', 'U') IS NULL
BEGIN
    CREATE TABLE consult_type(
        consult_type_id INT PRIMARY KEY IDENTITY(1,1),
        consult_type_name NVARCHAR(500) NOT NULL UNIQUE,
        [description] NVARCHAR(MAX)
    );

    INSERT INTO consult_type(consult_type_name)
    VALUES
    (N'想要諮詢關於平台上販售的品項'),
    (N'想要進一步諮詢檔期活動與優惠細節'),(N'廠商上架聯繫'),
    (N'媒體聯絡'),(N'網站異常回報'),(N'其他');
END;
GO
-----  Consultation ----- 
/* =========== Tables Without FKs end ===========*/

---------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------

/* =========== Tables With FKs ===========*/
----- Locations -----
IF OBJECT_ID(N'countries', N'U') IS NULL
BEGIN
    CREATE TABLE countries (
        id          INT IDENTITY(1,1) PRIMARY KEY,
        continent   VARCHAR(40) NOT NULL,
        [name]      VARCHAR(150) NOT NULL,
        name_zh     NVARCHAR(100) NOT NULL,
        phone_code  VARCHAR(10) NOT NULL,        -- 允許含破折號，如 1-242
        CONSTRAINT fk_countries_continent
            FOREIGN KEY (continent) REFERENCES continents(name),
        CONSTRAINT uq_countries_name       UNIQUE ([name]),
        CONSTRAINT uq_countries_name_zh    UNIQUE (name_zh),
        CONSTRAINT uq_countries_phone_code UNIQUE (phone_code)
    );
END;
GO

MERGE INTO countries AS tgt
USING (VALUES
  /* 亞洲 */
  ('Asia',         'Taiwan',         N'台灣',     '886'),
  ('Asia',         'Japan',          N'日本',     '81'),
  ('Asia',         'South Korea',    N'南韓',     '82'),
  ('Asia',         'Singapore',      N'新加坡',   '65'),
  ('Asia',         'Thailand',       N'泰國',     '66'),
  ('Asia',         'Hong Kong',      N'香港',     '852'),

  /* 歐洲 */
  ('Europe',       'Italy',          N'義大利',   '39'),
  ('Europe',       'United Kingdom', N'英國',     '44'),
  ('Europe',       'Netherlands',    N'荷蘭',     '31'),
  ('Europe',       'Russia',         N'俄羅斯',   '7'),
  ('Europe',       'Switzerland',    N'瑞士',     '41'),
  ('Europe',       'Ukraine',        N'烏克蘭',   '380'),

  /* 北美洲 */
  ('NorthAmerica', 'United States',  N'美國',     '1'),
  ('NorthAmerica', 'Belize',         N'貝里斯',   '501'),
  ('NorthAmerica', 'Bahamas',        N'巴哈馬',   '1-242'),
  ('NorthAmerica', 'Mexico',         N'墨西哥',   '52'),
  ('NorthAmerica', 'Greenland',      N'格陵蘭',   '299'),
  ('NorthAmerica', 'Aruba',          N'阿魯巴',   '297'),

  /* 南美洲 */
  ('SouthAmerica', 'Chile',          N'智利',     '56'),
  ('SouthAmerica', 'Colombia',       N'哥倫比亞', '57'),
  ('SouthAmerica', 'Paraguay',       N'巴拉圭',   '595'),
  ('SouthAmerica', 'Peru',           N'秘魯',     '51'),
  ('SouthAmerica', 'Uruguay',        N'烏拉圭',   '598'),
  ('SouthAmerica', 'Suriname',       N'蘇利南',   '597')
) AS src (continent, [name], name_zh, phone_code)
ON  tgt.[name] = src.[name]          -- 以英文名稱判斷是否已存在
WHEN NOT MATCHED BY TARGET THEN
    INSERT (continent, [name], name_zh, phone_code)
    VALUES (src.continent, src.[name], src.name_zh, src.phone_code);
GO

IF OBJECT_ID('cities', 'U') IS NULL
BEGIN
    CREATE TABLE cities(
        id INT PRIMARY KEY IDENTITY(1,1),
        country_id INT NOT NULL FOREIGN KEY REFERENCES countries(id),
        [name] VARCHAR(MAX) NOT NULL,
        name_zh NVARCHAR(MAX)
    );
INSERT INTO cities (country_id, [name], name_zh)
VALUES
(1, 'Keelung', N'基隆'),
(1, 'Taipei', N'台北'),
(1, 'New Taipei', N'新北'),
(1, 'Taoyuan', N'桃園'),
(1, 'Hsinchu', N'新竹'),
(1, 'Hsinchu County', N'新竹'),
(1, 'Miaoli County', N'苗栗'),
(1, 'Taichung', N'台中'),
(1, 'Changhua County', N'彰化'),
(1, 'Nantou County', N'南投'),
(1, 'Yunlin County', N'雲林'),
(1, 'Chiayi City', N'嘉義'),
(1, 'Chiayi County', N'嘉義'),
(1, 'Tainan', N'台南'),
(1, 'Kaohsiung', N'高雄'),
(1, 'Pingtung County', N'屏東'),
(1, 'Taitung County', N'台東'),
(1, 'Hualien County', N'花蓮'),
(1, 'Yilan County', N'宜蘭'),
(1, 'Penghu County', N'澎湖'),
(1, 'Kinmen County', N'金門'),
(1, 'Lienchiang County', N'連江'),
(2, 'Sapporo',        N'札幌'),      -- 北海道
(2, 'Aomori',         N'青森'),      -- 青森縣
(2, 'Morioka',        N'盛岡'),      -- 岩手縣
(2, 'Sendai',         N'仙台'),      -- 宮城縣
(2, 'Akita',          N'秋田'),      -- 秋田縣
(2, 'Yamagata',       N'山形'),      -- 山形縣
(2, 'Fukushima',      N'福島'),      -- 福島縣
(2, 'Mito',           N'水戶'),      -- 茨城縣
(2, 'Utsunomiya',     N'宇都宮'),    -- 栃木縣
(2, 'Maebashi',       N'前橋'),      -- 群馬縣
(2, 'Saitama',        N'埼玉'),      -- 埼玉縣
(2, 'Chiba',          N'千葉'),      -- 千葉縣
(2, 'Tokyo',          N'東京'),      -- 東京都
(2, 'Yokohama',       N'橫濱'),      -- 神奈川縣
(2, 'Niigata',        N'新潟'),      -- 新潟縣
(2, 'Toyama',         N'富山'),      -- 富山縣
(2, 'Kanazawa',       N'金澤'),      -- 石川縣
(2, 'Fukui',          N'福井'),      -- 福井縣
(2, 'Kofu',           N'甲府'),      -- 山梨縣
(2, 'Nagano',         N'長野'),      -- 長野縣
(2, 'Gifu',           N'岐阜'),      -- 岐阜縣
(2, 'Shizuoka',       N'靜岡'),      -- 靜岡縣
(2, 'Nagoya',         N'名古屋'),    -- 愛知縣
(2, 'Tsu',            N'津'),        -- 三重縣
(2, 'Otsu',           N'大津'),      -- 滋賀縣
(2, 'Kyoto',          N'京都'),      -- 京都府
(2, 'Osaka',          N'大阪'),      -- 大阪府
(2, 'Kobe',           N'神戶'),      -- 兵庫縣
(2, 'Nara',           N'奈良'),      -- 奈良縣
(2, 'Wakayama',       N'和歌山'),    -- 和歌山縣
(2, 'Tottori',        N'鳥取'),      -- 鳥取縣
(2, 'Matsue',         N'松江'),      -- 島根縣
(2, 'Okayama',        N'岡山'),      -- 岡山縣
(2, 'Hiroshima',      N'廣島'),      -- 廣島縣
(2, 'Yamaguchi',      N'山口'),      -- 山口縣
(2, 'Tokushima',      N'德島'),      -- 德島縣
(2, 'Takamatsu',      N'高松'),      -- 香川縣
(2, 'Matsuyama',      N'松山'),      -- 愛媛縣
(2, 'Kochi',          N'高知'),      -- 高知縣
(2, 'Fukuoka',        N'福岡'),      -- 福岡縣
(2, 'Saga',           N'佐賀'),      -- 佐賀縣
(2, 'Nagasaki',       N'長崎'),      -- 長崎縣
(2, 'Kumamoto',       N'熊本'),      -- 熊本縣
(2, 'Oita',           N'大分'),      -- 大分縣
(2, 'Miyazaki',       N'宮崎'),      -- 宮崎縣
(2, 'Kagoshima',      N'鹿兒島'),    -- 鹿兒島縣
(2, 'Naha',           N'那霸'),      -- 沖繩縣
(3, 'Seoul', '首爾'),
(3, 'Busan', '釜山'),
(3, 'Daegu', '大邱'),
(3, 'Incheon', '仁川'),
(3, 'Gwangju', '光州'),
(3, 'Daejeon', '大田'),
(3, 'Ulsan', '蔚山'),
(3, 'Gyeonggi-do', '京畿道'),
(3, 'Gangwon', '江源'),
(3, 'Chungcheongbuk-do', '忠清北道'),
(3, 'Chungcheongnam-do', '忠清南道'),
(3, 'Jeollabuk-do', '全北'),
(3, 'Jeollanam-do', '全羅南道'),
(3, 'Gyeongsangbuk-do', '慶尚北道'),
(3, 'Gyeongsangnam-do', '慶尚南道'),
(3, 'Jeju', '濟州'),
(3, 'Sejong', '世宗');
END;
GO
----- Locations -----

-----  Users ----- 
IF OBJECT_ID('all_users', 'U') IS NULL
BEGIN
    CREATE TABLE all_users(
        id INT PRIMARY KEY IDENTITY(1,1),
        username VARCHAR(30) UNIQUE NOT NULL,
        password_hash VARCHAR(MAX) NOT NULL, -- 07/03 remove salt and change to varchar
        email VARCHAR(150) UNIQUE NOT NULL,
        icon VARCHAR(MAX) NOT NULL,                -- 07/02 update to varchar(max)
        icon_type VARCHAR(50) NOT NULL,
        user_type INT NOT NULL FOREIGN KEY REFERENCES user_types(id),
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        last_modified DATETIME NOT NULL DEFAULT GETDATE()
    );
	INSERT INTO all_users
    (username,           -- 使用者名稱
     password_hash,      -- 雜湊字串 (varchar)
     email,              -- 信箱
     icon,               -- 大頭貼路徑 / Base64
     icon_type,          -- MIME 類型
     user_type)          -- 1=會員、2=管理員、3=商家
VALUES
    /* 1) 會員 user */
    ('user',
     '$2a$12$WT9Xn05Xc0K/DiDKVj8suuDP71G.sncUwWn6n1Fc09FjHHh8UQksK',
     'czhiming158@gmail.com',
     'https://i.pravatar.cc/150?u=1',
     'image/png',
     1),
    -- user
    -- acc: user
    -- pwd: user@123
  
    /* 2) 管理員 admin123 */
    ('admin',
     '$2a$12$HSr8R6gDfXF6vf9cl.kvOuVJtz8pGzqM1LNSiIfX4HdTY7MQHEg0S',
     'admin@Journey.com',
     'https://i.pravatar.cc/150?u=2',
     'image/png',
     2),
    -- admin
    -- acc: admin
    -- pwd: Admin@Journey
  
    /* 3) 商家 vendor*/
    ('vendor',
     '$2a$12$kgDuq/HjS6aHtOhn7C25KeHYo6HIz5YKyorI5QFkH8dDYMMe.YGrW',
     'vendor123@gmail.com',
     'https://i.pravatar.cc/150?u=3',
     'image/png',
     3),
    -- vendor
    -- acc: vendor
    -- pwd: vendor@123

    /* 4) 會員 user */
    ('Kim0915',
     '$2a$12$3OKtkIGT/3kTNtFvBXdCuumMQm0kWbyC/88arrrEef1.Pp9kjkag6',
     'Kim0915@gmail.com',
     'https://i.pravatar.cc/150?u=4',
     'image/png',
     1),
    -- Kim0915
    -- acc: Kim0915
    -- pwd: Kim@0915
	/* 5) 會員 user */
    ('Taro0921',
     '$2a$12$AEfkGR8pn7GpLQAUpaWCG.IbzTbjogOGn/wNiaOtc9uJSBcP2YS9O',
     'Taro0921@gmail.com',
     'https://i.pravatar.cc/150?u=55',
     'image/png',
     1),
    -- Taro0921
    -- acc: Taro0921
    -- pwd: Taro@0921
		/* 6) 會員 user */
	   ('Lin1992',
     '$2a$12$sPMrtUngYAa61Dsx.xIohuRoQQhrnyIq706IsVRhO89LTwiM1sXUm',
     'Lin1992@gmail.com',
     'https://i.pravatar.cc/150?u=6',
     'image/png',
     1);
    -- Lin1992
    -- acc: Lin1992
    -- pwd: Lin@1992

END;
GO

IF OBJECT_ID('users', 'U') IS NULL
BEGIN
    CREATE TABLE users(
        id INT PRIMARY KEY FOREIGN KEY REFERENCES all_users(id),
        given_name NVARCHAR(MAX) NOT NULL,
        family_name NVARCHAR(MAX) NOT NULL,
        given_name_latin VARCHAR(MAX),
        family_name_latin VARCHAR(MAX),
        nationality INT NOT NULL FOREIGN KEY REFERENCES countries(id),
        birthday DATE NOT NULL,
        passport_no VARCHAR(MAX) NOT NULL,
        passport_expire VARCHAR(7) NOT NULL,
        gender INT NOT NULL,
        tel_number VARCHAR(MAX) NOT NULL,
        residence INT NOT NULL FOREIGN KEY REFERENCES countries(id),
        [address] NVARCHAR(MAX) NOT NULL
    );
	INSERT INTO users (
		id, given_name, family_name,
		given_name_latin, family_name_latin,
		nationality, birthday, passport_no, passport_expire,
		gender, tel_number, residence, [address]
	)
	VALUES (
		1, N'志明', N'陳',
		'Chih-Ming', 'Chen',
		1, '1985-07-20', '312345678', '2031-12',
		1, '958566297', 1, N'台中市西區民權路100號'
	),
		(
		4, N'智勳', N'金',
		'Ji-Hoon', 'Kim',
		3, '1991-09-15', 'KR7654321', '2032-02',
		1, '1087654321', 3, N'首爾特別市中區乙支路'
	),
		(
		5, N'太郎', N'山田',
		'Taro', 'Yamada',
		2, '1990-01-15', 'AB1234567', '2030-01',
		1, '8012345678', 2, N'東京都新宿區'
	),
		(
		6, N'怡君', N'林',
		'Yi-Chun', 'Lin',
		1, '1992-04-10', 'TW55667788', '2033-06',
		2, '933111222', 1, N'新北市板橋區文化路二段'
	);

END;
GO

IF OBJECT_ID('partners', 'U') IS NULL
BEGIN
    CREATE TABLE partners(
        id INT PRIMARY KEY IDENTITY(1,1),
        relates_to INT NOT NULL FOREIGN KEY REFERENCES users(id),
        given_name NVARCHAR(MAX) NOT NULL,
        family_name NVARCHAR(MAX) NOT NULL,
        given_name_latin VARCHAR(MAX),
        family_name_latin VARCHAR(MAX),
        nationality INT NOT NULL FOREIGN KEY REFERENCES countries(id),
        birthday DATE NOT NULL,
        passport_no VARCHAR(MAX) NOT NULL,
        passport_expire VARCHAR(7) NOT NULL,
        gender INT NOT NULL,
        tel_number VARCHAR(MAX),
        residence INT NOT NULL FOREIGN KEY REFERENCES countries(id),
        email VARCHAR(MAX) NOT NULL,
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        last_modified DATETIME NOT NULL DEFAULT GETDATE(),
        delete_time DATETIME,
        delete_status INT NOT NULL DEFAULT 1
    );
END;
GO

IF OBJECT_ID('admins', 'U') IS NULL
BEGIN
    CREATE TABLE admins(
        admin_id INT PRIMARY KEY FOREIGN KEY REFERENCES all_users(id),
        [role] VARCHAR(MAX) NOT NULL
    );
    INSERT INTO admins(admin_id,[role])
    VALUES (2,'admin');


END;
GO

IF OBJECT_ID('vendor_status', 'U') IS NULL
BEGIN
    CREATE TABLE vendor_status(
        status_id INT PRIMARY KEY IDENTITY(1,1),
        [type] VARCHAR(100) UNIQUE NOT NULL
    );

    INSERT INTO vendor_status([type])
    VALUES ('pending'),('active');
END;
GO

IF OBJECT_ID('vendors', 'U') IS NULL
BEGIN
    CREATE TABLE vendors(
        id INT PRIMARY KEY FOREIGN KEY REFERENCES all_users(id),
        vendor_name NVARCHAR(200) UNIQUE NOT NULL,
        [status] INT NOT NULL FOREIGN KEY REFERENCES vendor_status(status_id)
    );
INSERT INTO vendors (id, vendor_name, [status])
VALUES (
  3,
 'VendorHotels',
  2  -- 確保 vendor_status 有這個值
);
END;
GO
-----  Users ----- 

----- Collections -----
IF OBJECT_ID('collections', 'U') IS NULL
BEGIN
    CREATE TABLE collections(
        id INT PRIMARY KEY IDENTITY(1,1),
        [user_id] INT NOT NULL FOREIGN KEY REFERENCES users(id),
        title NVARCHAR(50) NOT NULL,
        [description] NVARCHAR(MAX),
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        last_modified DATETIME NOT NULL DEFAULT GETDATE(),
        delete_time DATETIME,
        delete_status INT NOT NULL DEFAULT 1
    );
END;
GO

IF OBJECT_ID('collection_items', 'U') IS NULL
BEGIN
    CREATE TABLE collection_items(
        id INT PRIMARY KEY IDENTITY(1,1),
        collection_id INT NOT NULL FOREIGN KEY REFERENCES collections(id),
        item_type INT NOT NULL FOREIGN KEY REFERENCES type_enum([type_id]),
        item_id INT NOT NULL,
        added_time DATETIME NOT NULL DEFAULT GETDATE(),
        delete_time DATETIME,
        delete_status INT NOT NULL DEFAULT 1
    );
END;
GO
----- Collections -----

----- Flights -----
IF OBJECT_ID('airports', 'U') IS NULL
BEGIN
    CREATE TABLE airports (
        airport_code VARCHAR(6) PRIMARY KEY  NOT NULL,                  -- 機場代碼，主鍵且唯一
        airport_name VARCHAR(100) NOT NULL,                             -- 機場名稱
        country_id INT NOT NULL FOREIGN KEY REFERENCES countries(id),   -- 國家 ID
        city_id INT NOT NULL FOREIGN KEY REFERENCES cities(id),         -- 城市 ID
    );
		-- 台灣
		INSERT INTO airports (airport_code, airport_name, country_id, city_id) VALUES ('TPE', 'Taiwan Taoyuan International Airport', 1, 2);
		INSERT INTO airports (airport_code, airport_name, country_id, city_id) VALUES ('KHH', 'Kaohsiung International Airport', 1, 15);
		-- 日本
		INSERT INTO airports (airport_code, airport_name, country_id, city_id) VALUES ('NRT', 'Narita International Airport', 2, 34);
		INSERT INTO airports (airport_code, airport_name, country_id, city_id) VALUES ('HND', 'Haneda Airport', 2, 35);
		INSERT INTO airports (airport_code, airport_name, country_id, city_id) VALUES ('KIX', 'Kansai International Airport', 2, 49);
		INSERT INTO airports (airport_code, airport_name, country_id, city_id) VALUES ('NGO', 'Chubu Centrair International Airport', 2, 45);
		INSERT INTO airports (airport_code, airport_name, country_id, city_id) VALUES ('FUK', 'Fukuoka Airport', 2, 62);
		INSERT INTO airports (airport_code, airport_name, country_id, city_id) VALUES ('CTS', 'New Chitose Airport', 2, 23);
		-- 韓國
		INSERT INTO airports (airport_code, airport_name, country_id, city_id) VALUES ('ICN', 'Incheon International Airport', 3, 73);
		INSERT INTO airports (airport_code, airport_name, country_id, city_id) VALUES ('GMP', 'Gimpo International Airport', 3, 70);
END;
GO

IF OBJECT_ID('flights', 'U') IS NULL
BEGIN
    CREATE TABLE flights (
        flight_id INT PRIMARY KEY IDENTITY(1,1),                                                -- 航班id，自動增加
        airline_code VARCHAR(2) NOT NULL FOREIGN KEY REFERENCES airlines(airline_code),         -- 航空公司代碼，如 BR、CI、SQ
        flight_number VARCHAR(10) NOT NULL UNIQUE,                                              -- 航班號碼，例如 BR105
        departure_airport VARCHAR(6) NOT NULL FOREIGN KEY REFERENCES airports(airport_code),    -- 出發機場代碼，如 TPE
        arrival_airport VARCHAR(6) NOT NULL FOREIGN KEY REFERENCES airports(airport_code),      -- 抵達機場代碼，如 HND
        departure_time DATETIME NOT NULL,                -- 起飛時間
        arrival_time DATETIME NOT NULL,                  -- 抵達時間
        duration INT NOT NULL,                           -- 飛行時間（分鐘）
        created_at DATETIME NOT NULL DEFAULT GETDATE(),  -- 建立時間
        updated_at DATETIME NULL,                        -- 更新時間
        departure_terminal VARCHAR(MAX) NOT NULL,        --出發航廈(6/30)新增
        arrival_terminal VARCHAR(MAX) NOT NULL           --抵達航廈(6/30)新增
    
    );
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('CI', 'CI845', 'GMP', 'KHH', '2025-08-01 05:00:00', '2025-08-01 09:00:00', 300, 'Terminal I', 'Terminal I');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('JX', 'JX242', 'CTS', 'GMP', '2025-08-16 01:30:00', '2025-08-16 04:30:00', 180, 'Terminal I', 'Terminal I');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('JX', 'JX438', 'GMP', 'NRT', '2025-08-06 13:30:00', '2025-08-06 18:00:00', 270, 'Terminal I', 'Terminal 2');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('CX', 'CX451', 'CTS', 'ICN', '2025-08-11 15:30:00', '2025-08-11 19:30:00', 240, 'Terminal I', 'Terminal 1');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('CI', 'CI960', 'KHH', 'NGO', '2025-08-15 03:00:00', '2025-08-15 06:30:00', 150, 'Terminal I', 'Terminal 1');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('OZ', 'OZ388', 'TPE', 'KIX', '2025-08-18 11:00:00', '2025-08-18 17:00:00', 300, 'Terminal 2', 'Terminal 1');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('CX', 'CX120', 'TPE', 'GMP', '2025-08-24 00:00:00', '2025-08-24 06:00:00', 300, 'Terminal 2', 'Terminal I');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('BR', 'BR132', 'NRT', 'TPE', '2025-08-13 10:30:00', '2025-08-13 13:30:00', 240, 'Terminal 2', 'Terminal 2');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('MM', 'MM601', 'NGO', 'ICN', '2025-08-02 09:30:00', '2025-08-02 13:30:00', 240, 'Terminal 1', 'Terminal 1');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('JX', 'JX273', 'ICN', 'KHH', '2025-08-22 22:00:00', '2025-08-23 00:00:00', 180, 'Terminal 1', 'Terminal I');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('JL', 'JL859', 'NRT', 'TPE', '2025-08-09 10:00:00', '2025-08-09 12:00:00', 180, 'Terminal 2', 'Terminal 2');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('CI', 'CI664', 'CTS', 'TPE', '2025-08-01 11:30:00', '2025-08-01 14:00:00', 210, 'Terminal I', 'Terminal 2');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('MM', 'MM976', 'KHH', 'NRT', '2025-08-08 23:00:00', '2025-08-09 04:00:00', 240, 'Terminal I', 'Terminal 2');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('BR', 'BR749', 'GMP', 'KHH', '2025-08-22 17:00:00', '2025-08-22 20:00:00', 240, 'Terminal I', 'Terminal I');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('CI', 'CI673', 'CTS', 'ICN', '2025-08-10 02:30:00', '2025-08-10 07:30:00', 300, 'Terminal I', 'Terminal 1');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('OZ', 'OZ406', 'ICN', 'NGO', '2025-08-23 21:00:00', '2025-08-23 23:00:00', 120, 'Terminal 1', 'Terminal 1');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('CX', 'CX920', 'TPE', 'CTS', '2025-08-06 21:00:00', '2025-08-07 00:30:00', 150, 'Terminal 2', 'Terminal I');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('CI', 'CI540', 'NRT', 'KHH', '2025-08-18 21:00:00', '2025-08-18 22:30:00', 150, 'Terminal 2', 'Terminal I');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('BR', 'BR201', 'TPE', 'HND', '2025-08-10 09:00:00', '2025-08-10 13:30:00', 210, 'Terminal 2', 'Terminal 3');
			INSERT INTO flights (airline_code, flight_number, departure_airport, arrival_airport, departure_time, arrival_time, duration, departure_terminal, arrival_terminal)
			VALUES ('BR', 'BR202', 'HND', 'TPE', '2025-08-14 15:00:00', '2025-08-14 17:30:00', 150, 'Terminal 3', 'Terminal 2');
END;
GO

IF OBJECT_ID('flights_details', 'U') IS NULL
BEGIN
    CREATE TABLE flights_details (
        flights_details_id INT PRIMARY KEY IDENTITY(1,1),                           -- 主鍵，自動遞增
        flight_id INT NOT NULL FOREIGN KEY REFERENCES flights(flight_id),           -- 航班 ID
        class_id INT NOT NULL FOREIGN KEY REFERENCES flight_classes(class_id),      -- 艙等 ID
        class_price INT NOT NULL,                                                   -- 艙等票價
        class_capacity INT NOT NULL,                                                -- 艙等座位數
        class_available INT NOT NULL,                                               -- 可用座位數
        baggage_allowance INT NOT NULL                                              -- 行李額度 (kg)
    );

    -- 建立複合唯一索引，限制 flight_id 與 class_id 的組合不可重複
    CREATE UNIQUE INDEX idx_flight_class_unique
    ON flights_details(flight_id, class_id);

		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (1, 1, 15788, 12, 11, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (1, 2, 12758, 18, 13, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (1, 3, 7662, 133, 104, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (2, 1, 15323, 7, 6, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (2, 2, 10971, 15, 7, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (2, 3, 6814, 183, 173, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (3, 1, 24835, 7, 6, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (3, 2, 11714, 26, 25, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (3, 3, 4708, 194, 97, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (4, 1, 23847, 11, 11, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (4, 2, 14871, 28, 26, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (4, 3, 7215, 151, 106, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (5, 1, 16522, 12, 10, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (5, 2, 12971, 18, 9, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (5, 3, 7280, 92, 52, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (6, 1, 24640, 11, 8, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (6, 2, 13701, 28, 27, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (6, 3, 3347, 137, 96, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (7, 1, 21077, 9, 5, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (7, 2, 12951, 30, 24, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (7, 3, 3157, 94, 85, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (8, 1, 24981, 12, 7, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (8, 2, 14465, 14, 14, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (8, 3, 6425, 196, 124, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (9, 1, 23877, 6, 4, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (9, 2, 8054, 17, 14, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (9, 3, 5043, 195, 120, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (10, 1, 22502, 11, 10, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (10, 2, 11795, 16, 13, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (10, 3, 7158, 166, 114, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (11, 1, 15278, 7, 5, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (11, 2, 10721, 24, 18, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (11, 3, 6233, 115, 82, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (12, 1, 22740, 12, 6, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (12, 2, 14297, 28, 15, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (12, 3, 5310, 141, 105, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (13, 1, 15093, 8, 5, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (13, 2, 12122, 23, 13, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (13, 3, 5277, 113, 90, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (14, 1, 22792, 10, 6, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (14, 2, 9551, 16, 8, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (14, 3, 3311, 169, 92, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (15, 1, 16170, 12, 8, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (15, 2, 12800, 23, 12, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (15, 3, 5312, 174, 100, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (16, 1, 19856, 11, 8, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (16, 2, 11007, 17, 15, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (16, 3, 6018, 96, 57, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (17, 1, 23548, 11, 9, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (17, 2, 11389, 19, 9, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (17, 3, 5987, 176, 125, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (18, 1, 23222, 12, 8, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (18, 2, 11183, 12, 7, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (18, 3, 7651, 156, 100, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (19, 1, 19229, 10, 7, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (19, 2, 12365, 20, 20, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (19, 3, 4940, 133, 89, 23);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (20, 1, 18175, 8, 4, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (20, 2, 9558, 24, 21, 32);
		insert into flights_details (flight_id, class_id, class_price, class_capacity, class_available, baggage_allowance)
		values (20, 3, 7169, 113, 102, 23);

END;
GO
----- Flights -----

----- Lodgings -----
IF OBJECT_ID('lodgings', 'U') IS NULL
BEGIN
    CREATE TABLE lodgings (
        id INT IDENTITY(1,1) PRIMARY KEY,
        vendor_id INT NOT NULL FOREIGN KEY REFERENCES vendors(id),
        lodging_name NVARCHAR(100),
        lodging_type INT NOT NULL FOREIGN KEY REFERENCES lodgings_type(id),
        [description] NVARCHAR(255) NOT NULL,
        [address] NVARCHAR(100) NOT NULL,
        city_id INT NOT NULL FOREIGN KEY REFERENCES cities(id),
        country_id INT NOT NULL FOREIGN KEY REFERENCES countries(id),
        latitude DECIMAL(9,4) NOT NULL,
        longitude DECIMAL(9,4) NOT NULL,
        lodging_tel VARCHAR(20) NOT NULL,
        email VARCHAR(100) NOT NULL,
        is_active BIT NOT NULL DEFAULT 1,
        deleted_time DATETIME NULL,
        delete_status INT NOT NULL DEFAULT 1,
        created_at DATETIME DEFAULT GETDATE(),
        updated_at DATETIME DEFAULT GETDATE(),
        updated_by INT NOT NULL FOREIGN KEY REFERENCES vendors(id)
    );

    -- 建立條件唯一索引（只對 delete_status = 1 的資料）
    CREATE UNIQUE INDEX uq_city_lodging_name
    ON lodgings(city_id, lodging_name)
    WHERE delete_status = 1;
END;
GO

IF OBJECT_ID('room_types', 'U') IS NULL
BEGIN
    CREATE TABLE room_types (
        id INT IDENTITY(1,1) PRIMARY KEY,
        lodging_id INT NOT NULL FOREIGN KEY REFERENCES lodgings(id),
        [name] NVARCHAR(100) NOT NULL,
        [description] NVARCHAR(255) NOT NULL,
        price_per_night INT NOT NULL,
        MAX_guests INT NOT NULL,
        is_active BIT NOT NULL DEFAULT 1,
        deleted_time DATETIME NULL,
        delete_status INT NOT NULL DEFAULT 1,
        created_at DATETIME DEFAULT GETDATE(),
        updated_at DATETIME DEFAULT GETDATE(),
        created_by INT NOT NULL FOREIGN KEY REFERENCES vendors(id),
        updated_by INT NOT NULL FOREIGN KEY REFERENCES vendors(id)
    );
END;
GO

IF OBJECT_ID('room_type_beds', 'U') IS NULL
BEGIN
    CREATE TABLE room_type_beds (
        room_type_id INT NOT NULL FOREIGN KEY REFERENCES room_types(id),
        bed_type_id INT NOT NULL FOREIGN KEY REFERENCES bed_types(id),
        quantity INT NOT NULL DEFAULT 1,

        -- 確保同一房型內不重複同一床型
        CONSTRAINT PK_room_type_beds PRIMARY KEY (room_type_id, bed_type_id)
    );
END;
GO

IF OBJECT_ID('room_facilities', 'U') IS NULL
BEGIN
    CREATE TABLE room_facilities (
        room_type_id INT NOT NULL FOREIGN KEY REFERENCES room_types(id),
        facility_id INT NOT NULL FOREIGN KEY REFERENCES facilities(id),

        PRIMARY KEY (room_type_id, facility_id)
    );
END;
GO

IF OBJECT_ID('room_availability', 'U') IS NULL
BEGIN
    CREATE TABLE room_availability (
        id INT IDENTITY(1,1) PRIMARY KEY,
        room_type_id INT NOT NULL FOREIGN KEY REFERENCES room_types(id),
        [date] DATETIME NOT NULL,
        available_rooms INT NOT NULL,
        created_by INT NOT NULL FOREIGN KEY REFERENCES vendors(id),
        updated_by INT NOT NULL FOREIGN KEY REFERENCES vendors(id),
        created_at DATETIME DEFAULT GETDATE(),
        updated_at DATETIME DEFAULT GETDATE(),

        CONSTRAINT uq_room_date UNIQUE (room_type_id, date)
    );
END;
GO

IF OBJECT_ID('lodging_images', 'U') IS NULL
    BEGIN
    CREATE TABLE lodging_images (
        image_id INT IDENTITY(1,1) PRIMARY KEY,
        image_type VARCHAR(50) NOT NULL CHECK (image_type IN ('lodging', 'room')),
        lodging_id INT NULL FOREIGN KEY REFERENCES lodgings(id),
        room_type_id INT NULL FOREIGN KEY REFERENCES room_types(id),
        image_url NVARCHAR(500) NOT NULL,
        mime_type VARCHAR(100) NOT NULL,
        sort_order INT NOT NULL DEFAULT 0,
        is_active BIT NOT NULL DEFAULT 1,
        deleted_time DATETIME NULL,
        delete_status INT NOT NULL DEFAULT 1,
        uploaded_at DATETIME DEFAULT GETDATE()
);
END;
GO
----- Lodgings -----

----- Traffic Tickets -----
IF OBJECT_ID('traffic_tickets', 'U') IS NULL
BEGIN
    CREATE TABLE traffic_tickets (
        ticket_id INT PRIMARY KEY IDENTITY(1,1),                        -- 唯一識別碼，自動流水號
        country_id INT NOT NULL FOREIGN KEY REFERENCES countries(id),   -- 所屬國家 ID
        [name] VARCHAR(225) NOT NULL,                                   -- 票券名稱
        transport_type VARCHAR(50) NOT NULL,                            -- 交通類型（如：火車、船、巴士）
        valid_days INT NOT NULL,                                        -- 有效天數
        [description] NVARCHAR(MAX) NOT NULL,                           -- 描述
        area VARCHAR(50) NOT NULL,                                      -- 適用地區
        price INT NOT NULL,                                             -- 單張票價
        stock_quantity INT NOT NULL,                                    -- 庫存
        is_active BIT NOT NULL,                                         -- 是否上架
        --valid_start DATETIME NOT NULL,                                -- 可啟用的開始日 6/29發現用不到
        --valid_end DATETIME NOT NULL,                                  -- 可啟用的結束日 6/29發現用不到
        created_at DATETIME NOT NULL DEFAULT GETDATE(),                 -- 建立時間
        updated_at DATETIME ,                                           -- 更新時間 6/29可允許null
        image_url VARCHAR(MAX) NOT NULL,                                -- 圖片
        --image_type VARCHAR(50) NOT NULL                               -- 圖片類型 6/29儲存url不須使用type
    );
insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(2,'鐵路','全日本鐵路通票 JR PASS 全國版 7日版',7,'可連續 7 / 14 / 21 天無限次搭乘 JR 鐵路所有路線，包括新幹線、特急列車、急行列車、快車及慢車，還可乘坐 JR 巴士各地方線、部分高速線路與宮島 JR 渡輪，一券在手，輕鬆暢遊全日本國境，適合大範圍旅行、長途多點出差的旅客選購。',
                            '全日本',10060,'100','1','https://image.kkday.com/v2/image/get/c_fill%2Ch_600%2Cq_55%2Ct_webp/s1.kkday.com/product_22133/20241126072344_t939t/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(2,'鐵路','全日本鐵路通票 JR PASS 全國版 14日版',14,'可連續 7 / 14 / 21 天無限次搭乘 JR 鐵路所有路線，包括新幹線、特急列車、急行列車、快車及慢車，還可乘坐 JR 巴士各地方線、部分高速線路與宮島 JR 渡輪，一券在手，輕鬆暢遊全日本國境，適合大範圍旅行、長途多點出差的旅客選購。',
                            '全日本',16096,'100','1','https://image.kkday.com/v2/image/get/c_fill%2Ch_600%2Cq_55%2Ct_webp/s1.kkday.com/product_22133/20241126072344_t939t/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(2,'鐵路','全日本鐵路通票 JR PASS 全國版 21日版',7,'可連續 7 / 14 / 21 天無限次搭乘 JR 鐵路所有路線，包括新幹線、特急列車、急行列車、快車及慢車，還可乘坐 JR 巴士各地方線、部分高速線路與宮島 JR 渡輪，一券在手，輕鬆暢遊全日本國境，適合大範圍旅行、長途多點出差的旅客選購。',
                            '全日本',20120,'100','1','https://image.kkday.com/v2/image/get/c_fill%2Ch_600%2Cq_55%2Ct_webp/s1.kkday.com/product_22133/20241126072344_t939t/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(11,'鐵路','瑞士旅行通行證 Swiss Travel Pass 3日版',3,'瑞士旅行通行證有不同天數供您彈性選擇，您可不限次數搭乘瑞士境內 90 個城鎮的大眾運輸工具，包括國鐵火車 (SBB)、公車及遊湖船泊，部分景觀列車享有特別優惠，更可免費參觀超過 500 間博物館或城堡。依照自己的旅行步調，安排最適合的行程，省下大筆交通費用及門票。',
                            '全瑞士',8910,'100','1','https://image.kkday.com/v2/image/get/c_fill%2Ch_600%2Cq_55%2Ct_webp/s1.kkday.com/product_2524/20250627023500_PwPl9/png');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(11,'鐵路','瑞士旅行通行證 Swiss Travel Pass 6日版',6,'瑞士旅行通行證有不同天數供您彈性選擇，您可不限次數搭乘瑞士境內 90 個城鎮的大眾運輸工具，包括國鐵火車 (SBB)、公車及遊湖船泊，部分景觀列車享有特別優惠，更可免費參觀超過 500 間博物館或城堡。依照自己的旅行步調，安排最適合的行程，省下大筆交通費用及門票。',
                            '全瑞士',13839,'100','1','https://image.kkday.com/v2/image/get/c_fill%2Ch_600%2Cq_55%2Ct_webp/s1.kkday.com/product_2524/20250627023500_PwPl9/png');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(11,'鐵路','瑞士旅行通行證 Swiss Travel Pass 15日版',15,'瑞士旅行通行證有不同天數供您彈性選擇，您可不限次數搭乘瑞士境內 90 個城鎮的大眾運輸工具，包括國鐵火車 (SBB)、公車及遊湖船泊，部分景觀列車享有特別優惠，更可免費參觀超過 500 間博物館或城堡。依照自己的旅行步調，安排最適合的行程，省下大筆交通費用及門票。',
                            '全瑞士',16761,'100','1','https://image.kkday.com/v2/image/get/c_fill%2Ch_600%2Cq_55%2Ct_webp/s1.kkday.com/product_2524/20250627023500_PwPl9/png');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵法國通行證 Eurail France Pass 3日版',3,'歐鐵法國通行證是乘坐火車探索法國不同城市、海岸線和鄉村的理想方式，可以彈性地在一個月內自由選擇搭乘的天數，根據需求規劃旅程相應的天數通行證，且在旅程中無限次搭乘法國國鐵 SNCF 營運的火車。同時，透過此通行證，享有多種交通及景點折扣優惠，讓您輕鬆暢遊法國，省下繁複的交通規劃作業與費用！',
                            '全法國',5633,'100','1','https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_119920/20220107062603_c7JBq/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵法國通行證 Eurail France Pass 6日版',6,'歐鐵法國通行證是乘坐火車探索法國不同城市、海岸線和鄉村的理想方式，可以彈性地在一個月內自由選擇搭乘的天數，根據需求規劃旅程相應的天數通行證，且在旅程中無限次搭乘法國國鐵 SNCF 營運的火車。同時，透過此通行證，享有多種交通及景點折扣優惠，讓您輕鬆暢遊法國，省下繁複的交通規劃作業與費用！',
                            '全法國',8484,'100','1','https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_119920/20220107062603_c7JBq/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵法國通行證 Eurail France Pass 8日版',8,'歐鐵法國通行證是乘坐火車探索法國不同城市、海岸線和鄉村的理想方式，可以彈性地在一個月內自由選擇搭乘的天數，根據需求規劃旅程相應的天數通行證，且在旅程中無限次搭乘法國國鐵 SNCF 營運的火車。同時，透過此通行證，享有多種交通及景點折扣優惠，讓您輕鬆暢遊法國，省下繁複的交通規劃作業與費用！',
                            '全法國',10005,'100','1','https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_119920/20220107062603_c7JBq/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵義大利通行證 Eurail Italy Pass 3日版',3,'歐鐵義大利通行證提供多種有效期限選擇，您可以在一個月內非連續的日子使用，天數可根據您的個人喜好而定。享受無限次乘坐 Trenitalia 和 Trenord 列車的樂趣，以最有效的方式探索義大利的精華。立即預訂，體驗此通票提供的便利，並以優惠價格以自己喜歡的步調探索義大利。',
                            '全義大利',4461,'100','1','https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_119919/20220107062642_832Pg/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵義大利通行證 Eurail Italy Pass 6日版',6,'歐鐵義大利通行證提供多種有效期限選擇，您可以在一個月內非連續的日子使用，天數可根據您的個人喜好而定。享受無限次乘坐 Trenitalia 和 Trenord 列車的樂趣，以最有效的方式探索義大利的精華。立即預訂，體驗此通票提供的便利，並以優惠價格以自己喜歡的步調探索義大利。',
                            '全義大利',6658,'100','1','https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_119919/20220107062642_832Pg/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵義大利通行證 Eurail Italy Pass 3日版',3,'歐鐵義大利通行證提供多種有效期限選擇，您可以在一個月內非連續的日子使用，天數可根據您的個人喜好而定。享受無限次乘坐 Trenitalia 和 Trenord 列車的樂趣，以最有效的方式探索義大利的精華。立即預訂，體驗此通票提供的便利，並以優惠價格以自己喜歡的步調探索義大利。',
                            '全義大利',7875,'100','1','https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_119919/20220107062642_832Pg/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵義大利通行證 Eurail Italy Pass 3日版',3,'歐鐵義大利通行證提供多種有效期限選擇，您可以在一個月內非連續的日子使用，天數可根據您的個人喜好而定。享受無限次乘坐 Trenitalia 和 Trenord 列車的樂趣，以最有效的方式探索義大利的精華。立即預訂，體驗此通票提供的便利，並以優惠價格以自己喜歡的步調探索義大利。',
                            '全義大利',7875,'100','1','https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_119919/20220107062642_832Pg/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵西班牙通行證 Eurail Spain Pass 3日版',3,'歐鐵西班牙通行證是乘坐火車探索西班牙不同城市豐富歷史、可口美食和精彩文化的完美方式。在一個月內彈性地自由選擇搭乘的天數，根據需求規劃旅程相應的天數通行證，且在旅程中無限次搭乘西班牙國鐵 Renfe 與 FEVE 鐵路公司營運的火車。同時，透過此通行證，享有多種交通及景點折扣優惠，讓您輕鬆暢遊西班牙，省下繁複的交通規劃作業與費用！',
'全西班牙',5205,'100','1','https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_119922/20220107062503_nt36i/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵西班牙通行證 Eurail Spain Pass 6日版',6,'歐鐵西班牙通行證是乘坐火車探索西班牙不同城市豐富歷史、可口美食和精彩文化的完美方式。在一個月內彈性地自由選擇搭乘的天數，根據需求規劃旅程相應的天數通行證，且在旅程中無限次搭乘西班牙國鐵 Renfe 與 FEVE 鐵路公司營運的火車。同時，透過此通行證，享有多種交通及景點折扣優惠，讓您輕鬆暢遊西班牙，省下繁複的交通規劃作業與費用！',
'全西班牙',7402,'100','1','https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_119922/20220107062503_nt36i/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵西班牙通行證 Eurail Spain Pass 8日版',8,'歐鐵西班牙通行證是乘坐火車探索西班牙不同城市豐富歷史、可口美食和精彩文化的完美方式。在一個月內彈性地自由選擇搭乘的天數，根據需求規劃旅程相應的天數通行證，且在旅程中無限次搭乘西班牙國鐵 Renfe 與 FEVE 鐵路公司營運的火車。同時，透過此通行證，享有多種交通及景點折扣優惠，讓您輕鬆暢遊西班牙，省下繁複的交通規劃作業與費用！',
'全西班牙',8585,'100','1','https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_119922/20220107062503_nt36i/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵葡萄牙通行證 Eurail Portugal Pass 3日版',3,'歐鐵葡萄牙通行證是乘坐火車探索葡萄牙不同城市豐富歷史、可口美食和精彩文化的完美方式。在一個月內彈性地自由選擇搭乘的天數，根據需求規劃旅程相應的天數通行證，且在旅程中無限次搭乘葡萄牙國鐵 CP 營運的火車。同時，透過此通行證，享有多種交通及景點折扣優惠，讓您輕鬆暢遊葡萄牙，省下繁複的交通規劃作業與費用！',
'全葡萄牙',3585,'100','1','https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_119956/20220107060120_dSVuO/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵葡萄牙通行證 Eurail Portugal Pass 6日版',6,'歐鐵葡萄牙通行證是乘坐火車探索葡萄牙不同城市豐富歷史、可口美食和精彩文化的完美方式。在一個月內彈性地自由選擇搭乘的天數，根據需求規劃旅程相應的天數通行證，且在旅程中無限次搭乘葡萄牙國鐵 CP 營運的火車。同時，透過此通行證，享有多種交通及景點折扣優惠，讓您輕鬆暢遊葡萄牙，省下繁複的交通規劃作業與費用！',
'全葡萄牙',4732,'100','1','https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_119956/20220107060120_dSVuO/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵葡萄牙通行證 Eurail Portugal Pass 8日版',8,'歐鐵葡萄牙通行證是乘坐火車探索葡萄牙不同城市豐富歷史、可口美食和精彩文化的完美方式。在一個月內彈性地自由選擇搭乘的天數，根據需求規劃旅程相應的天數通行證，且在旅程中無限次搭乘葡萄牙國鐵 CP 營運的火車。同時，透過此通行證，享有多種交通及景點折扣優惠，讓您輕鬆暢遊葡萄牙，省下繁複的交通規劃作業與費用！',
'全葡萄牙',5813,'100','1','https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_119956/20220107060120_dSVuO/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵奧地利通行證 Eurail Austria Pass 3日版',3,'歐鐵奧地利通行證是乘坐火車探索奧地利不同城市豐富歷史、可口美食和精彩文化的完美方式。在一個月內彈性地自由選擇搭乘的天數，根據需求規劃旅程相應的天數通行證，且在旅程中無限次搭乘奧地利國鐵 ÖBB 與 ROeEE/GYSEV 和 Westbahn 鐵路公司營運的火車。同時，透過此通行證，享有多種交通及景點折扣優惠，讓您輕鬆暢遊奧地利，省下繁複的交通規劃作業與費用！',
'全奧地利',4461,'100','1','https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_119923/20220107062355_mIBNO/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵奧地利通行證 Eurail Austria Pass 6日版',6,'歐鐵奧地利通行證是乘坐火車探索奧地利不同城市豐富歷史、可口美食和精彩文化的完美方式。在一個月內彈性地自由選擇搭乘的天數，根據需求規劃旅程相應的天數通行證，且在旅程中無限次搭乘奧地利國鐵 ÖBB 與 ROeEE/GYSEV 和 Westbahn 鐵路公司營運的火車。同時，透過此通行證，享有多種交通及景點折扣優惠，讓您輕鬆暢遊奧地利，省下繁複的交通規劃作業與費用！',
'全奧地利',6658,'100','1','https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_119923/20220107062355_mIBNO/jpg');

insert into traffic_tickets (country_id,transport_type,name,valid_days,description,area,price,stock_quantity,is_active,image_url)
values(24,'鐵路','歐鐵奧地利通行證 Eurail Austria Pass 8日版',8,'歐鐵奧地利通行證是乘坐火車探索奧地利不同城市豐富歷史、可口美食和精彩文化的完美方式。在一個月內彈性地自由選擇搭乘的天數，根據需求規劃旅程相應的天數通行證，且在旅程中無限次搭乘奧地利國鐵 ÖBB 與 ROeEE/GYSEV 和 Westbahn 鐵路公司營運的火車。同時，透過此通行證，享有多種交通及景點折扣優惠，讓您輕鬆暢遊奧地利，省下繁複的交通規劃作業與費用！',
'全奧地利',7875,'100','1','https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_119923/20220107062355_mIBNO/jpg');


END;
GO
----- Traffic Tickets -----

----- Attraction Tickets -----
IF OBJECT_ID('attraction_tickets', 'U') IS NULL
BEGIN
    CREATE TABLE attraction_tickets (
        id INT PRIMARY KEY IDENTITY(1,1),
        country_id INT NOT NULL FOREIGN KEY REFERENCES countries(id),
        city INT NOT NULL FOREIGN KEY REFERENCES cities(id),
        [address] NVARCHAR(255) NOT NULL,
        [name] NVARCHAR(50) NOT NULL UNIQUE,
        [description] NVARCHAR(500) NOT NULL,
        image_url VARCHAR(MAX) NOT NULL,
        image_type VARCHAR(50) NOT NULL,
        [views] INT DEFAULT 0,
        [state] BIT NOT NULL, 
        start_time DATETIME NOT NULL DEFAULT GETDATE(),
        end_time DATETIME,
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME,
        created_by INT NOT NULL,
        updated_by INT
    );
END;
GO

IF OBJECT_ID('attraction_tickets_middle_types', 'U') IS NULL
BEGIN
    CREATE TABLE attraction_tickets_middle_types (
        ticket_id INT NOT NULL FOREIGN KEY REFERENCES attraction_tickets(id),
        attraction_type_id INT NOT NULL FOREIGN KEY REFERENCES attraction_types(id),

        PRIMARY KEY (ticket_id, attraction_type_id),
    );
END;
GO

IF OBJECT_ID('attraction_tickets_middle_attraction_tags', 'U') IS NULL
BEGIN
    CREATE TABLE attraction_tickets_middle_attraction_tags  (
        ticket_id INT NOT NULL FOREIGN KEY REFERENCES attraction_tickets(id),
        tag_id INT NOT NULL FOREIGN KEY REFERENCES attraction_tags(id),

        PRIMARY KEY (ticket_id, tag_id)
    );
END;
GO


IF OBJECT_ID('ticket_packages', 'U') IS NULL
BEGIN
    CREATE TABLE ticket_packages (
        id INT PRIMARY KEY IDENTITY(1,1),  
        ticket_id INT NOT NULL FOREIGN KEY REFERENCES attraction_tickets(id),
        [name] NVARCHAR(100) NOT NULL UNIQUE, 
        [description] NVARCHAR(500),          
        image_url VARCHAR(255) NOT NULL,
        image_type VARCHAR(50) NOT NULL,
        start_time DATETIME NOT NULL DEFAULT GETDATE(),
        end_time DATETIME,
        created_by INT NOT NULL,
        updated_by INT,
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME,
        [state] BIT NOT NULL               -- BOOLEAN → BIT
    );
END;
GO

IF OBJECT_ID('ticket_types', 'U') IS NULL
BEGIN
    CREATE TABLE ticket_types (
        id INT PRIMARY KEY IDENTITY(1,1),
        package_id INT NOT NULL FOREIGN KEY REFERENCES ticket_packages(id),
        price INT NOT NULL,                
        ticket_name NVARCHAR(50) NOT NULL,
        quantity INT NOT NULL,      
        [date] DATETIME NOT NULL
    );
END;
GO

IF OBJECT_ID('ticket_contents', 'U') IS NULL
BEGIN
    CREATE TABLE ticket_contents (
        content_id INT PRIMARY KEY IDENTITY(1,1),           
        ticket_id INT NOT NULL FOREIGN KEY REFERENCES attraction_tickets(id), 
        title NVARCHAR(255) NOT NULL,                        
        subtitle NVARCHAR(255),                              
        [description] NVARCHAR(MAX) NOT NULL
    );
END;
GO

IF OBJECT_ID('ticket_images', 'U') IS NULL
BEGIN
    CREATE TABLE ticket_images (
        image_id INT PRIMARY KEY IDENTITY(1,1),             
        ticket_id INT NOT NULL FOREIGN KEY REFERENCES attraction_tickets(id),
        image_url VARCHAR(255) NOT NULL,                   
        image_type VARCHAR(50) NOT NULL,                    
        alt_text NVARCHAR(500),                             
        sort_order INT NOT NULL
    );
END;
GO

IF OBJECT_ID('ticket_faqs', 'U') IS NULL
BEGIN
    CREATE TABLE ticket_faqs (
        faq_id INT PRIMARY KEY IDENTITY(1,1),              
        ticket_id INT NOT NULL FOREIGN KEY REFERENCES attraction_tickets(id),
        question NVARCHAR(500) NOT NULL,                    
        answer NVARCHAR(1000) NOT NULL,                     
        sort_order INT NOT NULL
    );
END;
GO

IF OBJECT_ID('attraction', 'U') IS NULL
BEGIN
    CREATE TABLE attraction (
        id INT PRIMARY KEY IDENTITY(1,1),
        country_id INT NOT NULL FOREIGN KEY REFERENCES countries(id),
        city INT NOT NULL FOREIGN KEY REFERENCES cities(id),
        [address] NVARCHAR(500) NOT NULL,
        [name] NVARCHAR(50) NOT NULL UNIQUE,
        [description] NVARCHAR(500) NOT NULL,
        [views] INT DEFAULT 0,
        attraction_url VARCHAR(MAX),
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        created_by INT NOT NULL,
        updated_at DATETIME,
        updated_by INT,
        image_url VARCHAR(255) NOT NULL,
        image_type VARCHAR(50) NOT NULL
    );
END;
GO

IF OBJECT_ID('attraction_middle_attraction_types', 'U') IS NULL
BEGIN
    CREATE TABLE attraction_middle_attraction_types  (
        attraction_id INT NOT NULL FOREIGN KEY REFERENCES attraction(id),
        attraction_type_id INT NOT NULL FOREIGN KEY REFERENCES attraction_types(id),
    
        PRIMARY KEY (attraction_id, attraction_type_id)
    );
END;
GO

IF OBJECT_ID('attraction_middle_attraction_tags', 'U') IS NULL
BEGIN
    CREATE TABLE attraction_middle_attraction_tags (
        attraction_id INT NOT NULL FOREIGN KEY REFERENCES attraction(id),
        attraction_tag_id INT NOT NULL FOREIGN KEY REFERENCES attraction_tags(id),

        PRIMARY KEY (attraction_id, attraction_tag_id)
    );
END;
GO

IF OBJECT_ID('attraction_images', 'U') IS NULL
BEGIN
    CREATE TABLE attraction_images (
        image_id INT PRIMARY KEY IDENTITY(1,1),
        attraction_id INT NOT NULL FOREIGN KEY REFERENCES attraction(id),
        image_url VARCHAR(255) NOT NULL,
        image_type VARCHAR(50) NOT NULL,
        alt_text NVARCHAR(255),
        sort_order INT NOT NULL
    );
END;
GO

IF OBJECT_ID('attraction_contents', 'U') IS NULL
BEGIN
    CREATE TABLE attraction_contents (
        content_id INT PRIMARY KEY IDENTITY(1,1),
        attraction_id INT NOT NULL FOREIGN KEY REFERENCES attraction(id),
        title NVARCHAR(255) NOT NULL,
        subtitle NVARCHAR(255),
        [description] NVARCHAR(MAX) NOT NULL
    );
END;
GO
----- Attraction Tickets -----

----- Plans -----
IF OBJECT_ID('self_defined_spots', 'U') IS NULL
BEGIN
    CREATE TABLE self_defined_spots(
        id INT PRIMARY KEY IDENTITY(1,1),
        created_by INT NOT NULL FOREIGN KEY REFERENCES users(id),
        [name] NVARCHAR(MAX) NOT NULL,
        [description] NVARCHAR(MAX),
        country INT NOT NULL FOREIGN KEY REFERENCES countries(id),
        city INT FOREIGN KEY REFERENCES cities(id),
        [address] NVARCHAR(MAX),
        lat NUMERIC(9,4),
        lon NUMERIC(9,4),
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        last_modified DATETIME NOT NULL DEFAULT GETDATE(),
        delete_time DATETIME,
        delete_status INT NOT NULL DEFAULT 1,

        CONSTRAINT check_location 
        CHECK (
            ([address] IS NOT NULL)
            OR (lat IS NOT NULL AND lon IS NOT NULL)
        )
    );
END;
GO

IF OBJECT_ID('trip_plans', 'U') IS NULL
BEGIN
    CREATE TABLE trip_plans(
        id INT PRIMARY KEY IDENTITY(1,1),
        [created_by] INT NOT NULL FOREIGN KEY REFERENCES users(id),
        title NVARCHAR(50) NOT NULL,
        [description] NVARCHAR(MAX),
        [start_date] DATETIME NOT NULL,
        [end_date] DATETIME NOT NULL,
        is_public BIT NOT NULL DEFAULT 0,
        [views] INTEGER NOT NULL DEFAULT 0,
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        last_modified DATETIME NOT NULL DEFAULT GETDATE(),
        delete_time DATETIME,
        delete_status INT NOT NULL DEFAULT 1,
    );
	Insert INTO trip_plans(created_by, title, [description], start_date, end_date)
	VALUES(1, '東京五天四夜', N'迪士尼、淺草、上野、台場、新宿、澀谷','2025-08-10', '2025-08-14')
END;
GO

IF OBJECT_ID('trip_days', 'U') IS NULL
BEGIN
    CREATE TABLE trip_days(
        id INT PRIMARY KEY IDENTITY(1,1),
        trip_id INT NOT NULL FOREIGN KEY REFERENCES trip_plans(id),
        day_num INT NOT NULL,
        actual_date DATETIME NOT NULL,
        delete_time DATETIME,
        delete_status INT NOT NULL DEFAULT 1,
    );
END;
GO

IF OBJECT_ID('trip_activities', 'U') IS NULL
BEGIN
    CREATE TABLE trip_activities(
        id INT PRIMARY KEY IDENTITY(1,1),
        day_id INT NOT NULL FOREIGN KEY REFERENCES trip_days(id),
        title NVARCHAR(50) NOT NULL,
        [description] NVARCHAR(MAX),
        location_type VARCHAR(50),
        flight INT FOREIGN KEY REFERENCES flights(flight_id),
        room_type INT FOREIGN KEY REFERENCES room_types(id),
        traffic INT FOREIGN KEY REFERENCES traffic_tickets(ticket_id),
        attraction_ticket INT FOREIGN KEY REFERENCES attraction_tickets(id),
        attraction INT FOREIGN KEY REFERENCES attraction(id),
        self_defined INT FOREIGN KEY REFERENCES self_defined_spots(id),
        start_time DATETIME NOT NULL,
        end_time DATETIME NOT NULL,
        ordinal INT NOT NULL,
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        last_modified DATETIME NOT NULL DEFAULT GETDATE(),
        delete_time DATETIME,
        delete_status INT NOT NULL DEFAULT 1,

        CONSTRAINT ensure_item 
        CHECK (
            (CASE WHEN flight IS NOT NULL THEN 1 ELSE 0 END) +
            (CASE WHEN room_type IS NOT NULL THEN 1 ELSE 0 END) +
            (CASE WHEN traffic IS NOT NULL THEN 1 ELSE 0 END) +
            (CASE WHEN attraction_ticket IS NOT NULL THEN 1 ELSE 0 END) +
            (CASE WHEN attraction IS NOT NULL THEN 1 ELSE 0 END) +
            (CASE WHEN self_defined IS NOT NULL THEN 1 ELSE 0 END) = 1
        )
    );
END;
GO

IF OBJECT_ID('trip_transportations', 'U') IS NULL
BEGIN
    CREATE TABLE trip_transportations(
        id INT PRIMARY KEY IDENTITY(1,1),
        mode VARCHAR(50) NOT NULL,
        day_id INT NOT NULL FOREIGN KEY REFERENCES trip_days(id),
        from_activity_id INT NOT NULL FOREIGN KEY REFERENCES trip_activities(id),
        to_activity_id INT NOT NULL FOREIGN KEY REFERENCES trip_activities(id),
        departure_time DATETIME NOT NULL,
        arrival_time DATETIME NOT NULL,
        notes NVARCHAR(MAX),
        delete_time DATETIME,
        delete_status INT NOT NULL DEFAULT 1,
    );
END;
GO

IF OBJECT_ID('trip_shared_links', 'U') IS NULL
BEGIN
    CREATE TABLE trip_shared_links(
        id INT PRIMARY KEY IDENTITY(1,1),
        trip_id INT NOT NULL FOREIGN KEY REFERENCES trip_plans(id),
        uuid UNIQUEIDENTIFIER NOT NULL,
        created_at DATETIME NOT NULL DEFAULT GETDATE(),
        expired_at DATETIME,
        delete_status INT NOT NULL DEFAULT 1,
    );
END;
GO
----- Plans -----

----- Coupons -----
IF OBJECT_ID('coupon_instances', 'U') IS NULL
BEGIN
    CREATE TABLE coupon_instances(
        coupon_instance_id UNIQUEIDENTIFIER PRIMARY KEY NOT NULL DEFAULT NEWID(),
        coupon_type_id INT NOT NULL FOREIGN KEY REFERENCES coupon_type(coupon_type_id),
        coupon_name NVARCHAR(MAX) NOT NULL DEFAULT '優惠券',
        [user_id] INT NOT NULL FOREIGN KEY REFERENCES users(id),
        get_from NVARCHAR(MAX) NOT NULL,
        use_at DATETIME,
        issued_time DATETIME NOT NULL DEFAULT GETDATE(),
        start_time DATETIME NOT NULL DEFAULT GETDATE(),
        expire_time DATETIME
    );
	INSERT INTO coupon_instances(coupon_type_id, coupon_name, user_id, get_from, use_at, issued_time, start_time, expire_time)
	VALUES
	
	-- 有效
	(1, '新的一年送你 NT$100 折價券', 1, '活動贈送', NULL, '2025-01-01', '2025-01-01', '2025-12-31'),                             --  1  有效 有期限
	(1, '夏日慶典活動 NT$100 折價券', 1, '活動贈送', NULL, '2025-06-05', '2025-06-05', '2025-12-31'),                             --  2  有效 有期限
	(3, 'Journey周年慶 全品項 NT$500 折價券', 1, '活動贈送', NULL, '2025-02-02', '2025-02-02', '2026-02-02'),                     --  3  有效 有期限
	(6, '單筆訂單 9 折優惠', 1, '點數商城兌換', NULL, '2024-03-03', '2024-03-03', NULL),                                          --  4  有效
	(5, '國際旅遊展合作活動抽獎 全品項 NT$2,000 折價券', 1, '活動贈送', NULL, '2025-06-22', '2025-06-22', NULL),                   --  5  有效
	(5, 'NT$2,000 折價券', 1, '點數商城兌換', NULL, '2023-02-04', '2023-02-04', NULL),                                             --  6  有效
	(3, 'NT$500 折價券', 1, '點數商城兌換', NULL, '2025-04-11', '2025-04-11', NULL),                                              --  7  有效
	(2, 'NT$300 折價券', 1, '交易回饋', NULL, '2025-02-03', '2025-02-03', NULL),                                                  --  8  有效
	(1, 'NT$100 折價券', 1, '交易回饋', NULL, '2025-01-05', '2025-01-05', NULL),                                                  --  9  有效
	(1, 'NT$100 折價券', 1, '點數商城兌換', NULL, '2025-02-07', '2025-02-07', NULL),                                              --  10 有效
	
	-- 已使用
	(8, '景點門票 8 折券', 1, '點數商城兌換', '2022-03-04', '2022-01-01', '2022-01-01', NULL),                                    --  11 已使用
	(7, '9 折住宿優惠券（10% OFF）', 1, '點數商城兌換', '2023-05-09', '2023-02-02', '2023-02-02', NULL),                          --  12 已使用
	(14, '歐洲線限定｜機票 9 折券', 1, '活動贈送', '2023-07-07', '2022-09-30', '2022-09-30', '2023-09-30'),                       --  13 已使用 有期限
	(6, '單筆訂單 9 折優惠', 1, '點數商城兌換', '2023-09-09', '2023-01-11', '2023-01-11', NULL),                                  --  14 已使用
	(13, '睡好一點！韓國星級飯店 9 折任你挑', 1, '活動贈送', '2023-12-12', '2023-10-04', '2023-10-04', '2024-10-04'),             --  15 已使用 有期限
	(9, '機票專用 88 折優惠 12% OFF', 1, '點數商城兌換', '2024-03-17', '2023-07-07', '2023-07-07',  NULL),                        --  16 已使用
	(10, '交通票券 9 折優惠', 1, '點數商城兌換', '2025-03-03', '2024-11-12', '2024-11-12', NULL),                                 --  17 已使用
	(1, 'NT$100 折價券', 1, '交易回饋', '2025-06-06', '2025-02-03', '2025-02-03', NULL),                                          --  18 已使用
	
	--過期
	(12, '日本住宿爆省 9 折券', 1, '活動贈送', NULL, '2024-04-03', '2024-04-03', '2025-04-03'),                                   --  19 過期 有期限
	(15, '暑假出國必搶！單張機票現折 NT$500', 1, '活動贈送', NULL, '2024-06-22', '2024-06-22', '2025-06-22')                      --  20 過期 有期限
	
	
	-- temp	 
	-- Insert 排序:
	-- A. 有效 --> 已使用 --> 過期
	-- 有效:
	-- 1. 有期限(到期日近到遠) --> 無期限 
	-- 2. 無期限: 折扣多到少
	-- 3. 折扣多到少: % --> 金額
	-- 已使用: (使用時間遠到近)
	-- 過期: (過期日遠到近)
END;
GO

IF OBJECT_ID('coupon_platform', 'U') IS NULL
BEGIN
    CREATE TABLE coupon_platform(
        coupon_type_id INT FOREIGN KEY REFERENCES coupon_type(coupon_type_id),
        platform_id INT FOREIGN KEY REFERENCES platform(platform_id),

        PRIMARY KEY(coupon_type_id, platform_id),
    );

    INSERT INTO coupon_platform
    VALUES
    (1,1),(1,2),
    (2,1),(2,2),
    (3,1),(3,2),
    (4,1),(4,2),
    (5,1),(5,2),
    (6,1),
    (7,1),(7,2),
    (8,1),(8,2),
    (9,1),(9,2),
    (10,1), (10,2),
    (11,1), (11,2),
    (12,1), (12,2),
    (13,1), (13,2),
    (14,1), (14,2),
    (15,1), (15,2);
END;
GO
----- Coupons -----

----- Payments & PointCards -----
IF OBJECT_ID('point_cards', 'U') IS NULL
BEGIN
    CREATE TABLE point_cards(
        point_card_id UNIQUEIDENTIFIER PRIMARY KEY NOT NULL DEFAULT NEWID(),
        [user_id] INT NOT NULL UNIQUE FOREIGN KEY REFERENCES users(id),
        release_date DATETIME NOT NULL DEFAULT GETDATE(),
        point_card_rule NVARCHAR(MAX)
    );
END;
GO

IF OBJECT_ID('payments_record', 'U') IS NULL
BEGIN
    CREATE TABLE payments_record(
        payment_id INT PRIMARY KEY IDENTITY(1,1),
        [type_id] INT NOT NULL FOREIGN KEY REFERENCES type_enum([type_id]),
        order_id INT NOT NULL,
        coupon_instance_id UNIQUEIDENTIFIER FOREIGN KEY REFERENCES coupon_instances(coupon_instance_id),
        point_card_id UNIQUEIDENTIFIER FOREIGN KEY REFERENCES point_cards(point_card_id),
        reward_id INT FOREIGN KEY REFERENCES rewards(reward_id),
        currency NVARCHAR(MAX) NOT NULL,
        payment_tool_id INT NOT NULL FOREIGN KEY REFERENCES payments(payment_tool_id),
        payment_status NVARCHAR(MAX) NOT NULL DEFAULT 'pending',
        transaction_number NVARCHAR(500) UNIQUE,
        record_time DATETIME NOT NULL DEFAULT GETDATE(),
        note NVARCHAR(MAX)
    );
END;
GO

IF OBJECT_ID('point_cards_status', 'U') IS NULL
BEGIN
    CREATE TABLE point_cards_status(
        change_record_id BIGINT PRIMARY KEY IDENTITY(1,1),
        point_card_id UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES point_cards(point_card_id),
        point_change INT NOT NULL,
        point_source NVARCHAR(MAX) NOT NULL DEFAULT '交易回饋',
        payment_id INT FOREIGN KEY REFERENCES payments_record(payment_id),
        updated_at DATETIME NOT NULL DEFAULT GETDATE(),

        CHECK(point_change <> 0)
    );
	INSERT INTO point_cards(user_id, release_date)
	VALUES(1, getDate())
	
	
	INSERT INTO point_cards_status (point_card_id, point_change, point_source, updated_at)
	SELECT
	    pc.point_card_id, 
	    12500,            
	    N'交易回饋',      
	    GETDATE()         
	FROM
	    point_cards AS pc
	WHERE
	    pc.user_id = 1;
	
	
	-- 如果要測試就執行這行，或者把數字9999999改成其他夠大的數值
	-- UPDATE point_cards_status SET point_change = 9999999 
	-- WHERE change_record_id = 1
END;
GO

IF OBJECT_ID('rewards_record', 'U') IS NULL
BEGIN
    CREATE TABLE rewards_record(
        reward_record_id BIGINT PRIMARY KEY IDENTITY(1,1),
        point_card_id UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES point_cards(point_card_id),
        reward_id INT NOT NULL FOREIGN KEY REFERENCES rewards(reward_id),
        payment_id INT FOREIGN KEY REFERENCES payments_record(payment_id),
        reward_date DATETIME NOT NULL DEFAULT GETDATE()
    );
END;
GO
----- Payments & PointCards -----

----- Orders -----
IF OBJECT_ID('booking_orders', 'U') IS NULL
BEGIN
    CREATE TABLE booking_orders (
        id INT IDENTITY(1,1) PRIMARY KEY, -- 訂單 ID
        trip_id INT NOT NULL FOREIGN KEY REFERENCES trip_plans(id), -- 行程規劃 ID
        [status] BIT NOT NULL DEFAULT 0, -- 訂單狀態
        payment_id INT FOREIGN KEY REFERENCES payments_record(payment_id), -- 付款記錄編號
        order_no NVARCHAR(300) NOT NULL UNIQUE, -- 訂單編號
        updated_by INT NULL FOREIGN KEY REFERENCES all_users(id), -- 最後修改者 ID
        created_at DATETIME NOT NULL, -- 訂單建立時間
        updated_at DATETIME NOT NULL -- 最後更新時間
    );
END;
GO

IF OBJECT_ID('booking_order_items', 'U') IS NULL
BEGIN
    CREATE TABLE booking_order_items (
        id INT IDENTITY(1,1) PRIMARY KEY, -- 明細 ID
        booking_id INT NOT NULL FOREIGN KEY REFERENCES booking_orders(id), -- 訂單 ID
        room_type_id INT NOT NULL FOREIGN KEY REFERENCES room_types(id), -- 房型 ID
        check_in_date DATETIME NOT NULL, -- 入住日期
        check_out_date DATETIME NOT NULL, -- 退房日期
        guest_count INT NOT NULL, -- 入住人數
        price_per_night INT NOT NULL -- 每晚價格
    );
END;
GO

IF OBJECT_ID('flights_orders', 'U') IS NULL
BEGIN
    CREATE TABLE flights_orders (
        order_id INT PRIMARY KEY IDENTITY(1,1),                                                 -- 訂單 ID，自動遞增
        trip_id INT NOT NULL FOREIGN KEY REFERENCES trip_plans(id),                             -- 行程規劃 ID（外鍵）
        order_no NVARCHAR(50) NOT NULL UNIQUE,                                                  -- 訂單編號（供第三方支付）
        order_date DATETIME NOT NULL DEFAULT GETDATE(),                                         -- 訂單建立時間，預設為當前時間
        payment_id INT FOREIGN KEY REFERENCES payments_record(payment_id),                      -- 支付記錄 ID（外鍵）
        [status] BIT NOT NULL DEFAULT 0,                                                        -- 訂單狀態（可用 1=有效/已付款，0=無效/未付款）
        updated_by INT NULL FOREIGN KEY REFERENCES admins(admin_id),                            -- 最後修改者 ID（可為 NULL）
        created_at DATETIME NOT NULL DEFAULT GETDATE(),                                         -- 建立時間
        updated_at DATETIME NOT NULL DEFAULT GETDATE(),                                         -- 最後更新時間
    );
END;
GO

IF OBJECT_ID('flight_order_details', 'U') IS NULL
BEGIN
    CREATE TABLE flight_order_details (
        order_detail_id INT PRIMARY KEY IDENTITY(1,1),                                              -- 訂單明細 ID，自動遞增
        order_id INT NOT NULL FOREIGN KEY REFERENCES flights_orders(order_id),                      -- 訂單 ID
        flights_details_id INT NOT NULL FOREIGN KEY REFERENCES flights_details(flights_details_id), -- 航班艙等明細 ID
        passenger_type VARCHAR(10) NOT NULL,                                                        -- 乘客類別（成人、兒童、嬰兒）
        seat_number VARCHAR(10) ,                                                                   -- 座位號碼
        baggage_weight INT NOT NULL DEFAULT 0,                                                      -- 行李重量（預設 0）
        baggage_fee INT NOT NULL DEFAULT 0,                                                         -- 行李加購費用（預設 0）
        partner_id INT NOT NULL FOREIGN KEY REFERENCES partners(id),                                -- 合作夥伴 ID
        price INT NOT NULL,                                                                         -- 單張票價    
    );
END;
GO

IF OBJECT_ID('ticket_order', 'U') IS NULL
BEGIN
    CREATE TABLE ticket_order (
        order_id INT PRIMARY KEY IDENTITY(1,1),                                     -- 訂單編號，自動遞增
        trip_id INT NOT NULL FOREIGN KEY REFERENCES trip_plans(id),                 -- 關聯行程計劃
        order_no NVARCHAR(50) NOT NULL UNIQUE,                                      -- 訂單編號（供第三方支付用）
        payment_id INT FOREIGN KEY REFERENCES payments_record(payment_id),          -- 付款紀錄編號
        order_date DATETIME NOT NULL DEFAULT GETDATE(),                             -- 訂單成立時間，預設當前時間
        [status] BIT NOT NULL,                                                      -- 訂單狀態（布林值）
        updated_by INT NULL FOREIGN KEY REFERENCES admins(admin_id),                -- 最後修改者 ID
        created_at DATETIME NOT NULL DEFAULT GETDATE(),                             -- 建立時間
        updated_at DATETIME NOT NULL DEFAULT GETDATE()                              -- 最後更新時間
    );
END;
GO

IF OBJECT_ID('ticket_order_detail', 'U') IS NULL
BEGIN
    CREATE TABLE ticket_order_detail (
        detail_id INT PRIMARY KEY IDENTITY(1,1),                                                -- 明細 ID，自動遞增
        order_id INT NOT NULL FOREIGN KEY REFERENCES ticket_order(order_id),                    -- 訂單 ID
        ticket_id INT NOT NULL FOREIGN KEY REFERENCES traffic_tickets(ticket_id),               -- 購買票券 ID
        quantity INT NOT NULL,                                                                  -- 購買數量
        unit_price INT NOT NULL,                                                                -- 單價
        activated_at DATETIME NOT NULL,                                                         -- 啟用時間
        expired_at DATETIME NOT NULL                                                            -- 到期時間
    );
END;
GO

IF OBJECT_ID('attraction_ticket_orders', 'U') IS NULL
BEGIN
    CREATE TABLE attraction_ticket_orders (
        order_id INT PRIMARY KEY IDENTITY(1,1),                                                 -- 主鍵，自動遞增
        trip_id INT NOT NULL FOREIGN KEY REFERENCES trip_plans(id),                             -- 對應 trip_plans(id)
        [status] BIT NOT NULL DEFAULT 0,                                                        -- 訂單狀態（BOOLEAN → BIT）
        payment_id INT FOREIGN KEY REFERENCES payments_record(payment_id),                      -- 對應付款紀錄
        order_no NVARCHAR(100) NOT NULL UNIQUE,                                                 -- 訂單編號，供第三方使用
        updated_by INT FOREIGN KEY REFERENCES admins(admin_id),                                 -- 最後修改人（admins）
        created_at DATETIME NOT NULL DEFAULT GETDATE(),                                         -- 建立時間
        updated_at DATETIME NOT NULL DEFAULT GETDATE()                                          -- 更新時間
    );
END;
GO

IF OBJECT_ID('attraction_ticket_order_items', 'U') IS NULL
BEGIN
    CREATE TABLE attraction_ticket_order_items (
        item_id INT PRIMARY KEY IDENTITY(1,1),                                                  -- 自動遞增主鍵
        order_id INT NOT NULL FOREIGN KEY REFERENCES attraction_ticket_orders(order_id),        -- 所屬訂單
        attraction_id INT NOT NULL FOREIGN KEY REFERENCES attraction_tickets(id),               -- 景點票
        ticket_packages_id INT NOT NULL FOREIGN KEY REFERENCES ticket_packages(id),             -- 套票
        option_id INT NOT NULL FOREIGN KEY REFERENCES ticket_types(id),                         -- 票種（成人/兒童）
        quantity INT NOT NULL,                                                                  -- 數量
        use_date DATETIME,                                                                      -- 使用日
        unit_price INT NOT NULL                                                                 -- 單價
    );
END;
GO
----- Orders -----

----- Comments -----
IF OBJECT_ID('comments', 'U') IS NULL
BEGIN
    CREATE TABLE comments (
        id INT IDENTITY(1,1) PRIMARY KEY,
        lodging_id INT NULL FOREIGN KEY REFERENCES lodgings(id),
        room_type_id INT NULL FOREIGN KEY REFERENCES room_types(id),
        traffic_ticket_id INT NULL FOREIGN KEY REFERENCES traffic_tickets(ticket_id),
        attraction_ticket_id INT NULL FOREIGN KEY REFERENCES attraction_tickets(id),
        trip_plan_id INT NULL FOREIGN KEY REFERENCES trip_plans(id),
        [user_id] INT NOT NULL FOREIGN KEY REFERENCES all_users(id),
        content NVARCHAR(500) NOT NULL,
        parent_comment_id INT NULL FOREIGN KEY REFERENCES comments(id),
        rating TINYINT NULL DEFAULT NULL CHECK (rating BETWEEN 1 AND 5),
        is_verified BIT NOT NULL DEFAULT 0,
        is_active BIT NOT NULL DEFAULT 1,
        deleted_time DATETIME NULL,
        delete_status INT NOT NULL DEFAULT 1,
        created_at DATETIME DEFAULT GETDATE(),
        updated_at DATETIME DEFAULT GETDATE()
    );
END;
GO

IF OBJECT_ID('comment_images', 'U') IS NULL
BEGIN
    CREATE TABLE comment_images (
        id INT IDENTITY(1,1) PRIMARY KEY,
        comment_id INT NOT NULL FOREIGN KEY REFERENCES comments(id),
        image_url NVARCHAR(500) NOT NULL,
        mime_type VARCHAR(100) NOT NULL,
        sort_order INT NOT NULL DEFAULT 0,
        is_active BIT NOT NULL DEFAULT 1,
        deleted_time DATETIME NULL,
        delete_status INT NOT NULL DEFAULT 1,
        uploaded_at DATETIME DEFAULT GETDATE()
    );
END;
GO

IF OBJECT_ID('comment_feedbacks', 'U') IS NULL
BEGIN
    CREATE TABLE comment_feedbacks (
        comment_id INT NOT NULL FOREIGN KEY REFERENCES comments(id) ON DELETE CASCADE,
        [user_id] INT NOT NULL FOREIGN KEY REFERENCES all_users(id) ON DELETE CASCADE,
        feedback BIT NOT NULL -- 1 = like，可擴充其他值

        PRIMARY KEY (comment_id, [user_id])
    );
END;
GO

IF OBJECT_ID('comment_reports', 'U') IS NULL
BEGIN
    CREATE TABLE comment_reports (
        id INT IDENTITY(1,1) PRIMARY KEY,
        comment_id INT NOT NULL FOREIGN KEY REFERENCES comments(id),
        reason_id INT NOT NULL FOREIGN KEY REFERENCES comment_reasons(id),
        created_by INT NOT NULL FOREIGN KEY REFERENCES all_users(id),
        created_at DATETIME DEFAULT GETDATE(),
    -- 新增欄位
     [status] VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK ([status] IN ('pending', 'resolved', 'rejected')),
        reviewed_by INT NULL FOREIGN KEY REFERENCES admins(admin_id),
        reviewed_at DATETIME NULL,
        note NVARCHAR(MAX) NULL,
    CONSTRAINT uq_comment_user UNIQUE (comment_id, created_by)   -- ★ 唯一鍵
    );
END;
GO
----- Comments -----

----- Customer Services -----
IF OBJECT_ID('customer_service_chatroom', 'U') IS NULL
BEGIN
    CREATE TABLE customer_service_chatroom(
        chat_room_id INT PRIMARY KEY IDENTITY(1,1),
        chat_room_name NVARCHAR(MAX) NOT NULL,
        [user_id] INT NOT NULL FOREIGN KEY REFERENCES users(id),
        [type_id] INT FOREIGN KEY REFERENCES type_enum(type_id),
        chat_room_create_at DATETIME NOT NULL DEFAULT GETDATE()
    );
END;
GO

IF OBJECT_ID('customer_service_question', 'U') IS NULL
BEGIN
    CREATE TABLE customer_service_question(
        question_template_id INT PRIMARY KEY IDENTITY(1,1),
        question_template NVARCHAR(500) NOT NULL UNIQUE,
        reply_template NVARCHAR(MAX) NOT NULL,
        [type_id] INT NOT NULL FOREIGN KEY REFERENCES type_enum([type_id])
    );

    INSERT INTO customer_service_question
    VALUES
    (N'請問可以指定入住哪一間飯店嗎？', N'可以的，您可以在搜尋頁面使用篩選條件選擇特定飯店名稱或地區進行查詢與預訂。', 1),
    (N'我可以提早入住或延後退房嗎？', N'可於訂房完成後聯繫飯店詢問是否能提供提早入住或延後退房服務，可能會需額外費用。', 1),
    (N'如何知道飯店以及特定房型有提供哪些設施？', N'可於飯店頁面以及下方的房型資訊查看到該飯店本身和各類房型提供的設施清單。', 1),
    (N'飯店地址在哪裡？周邊有哪些車站或鄰近哪個機場?', N'當您點進飯店頁面後可看到詳細地址，同時也可查看到周邊鄰近的機場與車站，以及他們與飯店的距離', 1),

    (N'為什麼票價和昨天看到的不一樣？', N'機票價格會隨供需與匯率變動，即時報價可能與前一天不同，建議盡早預訂。', 2),
    (N'小孩/嬰兒的機票怎麼訂？', N'訂票時可新增兒童或嬰兒乘客，系統會自動套用兒童票價與嬰兒政策。', 2),
    (N'可以帶幾公斤行李？超重怎麼辦？', N'每家航空公司行李規則不同，請在票價說明中查閱，超重部分通常需現場加購。', 2),
    (N'航班延誤了怎麼處理？我會收到通知嗎？', N'若航班異動，我們將透過 email 或簡訊通知您，也建議您下載航空公司 App 即時追蹤。', 2),

    (N'交通票券可以改日期嗎？', N'多數交通票券發出後不可更改日期，建議您預訂前再次確認行程。', 3),
    (N'我想同時買票給多人可以嗎？怎麼操作？', N'可以，請在訂購頁面選擇購買數量後系統會自動計算價格。', 3),
    (N'有販售台灣高鐵票嗎？可以折扣嗎？', N'有的，我們平台提供台灣高鐵電子票，部分期間享有優惠，歡迎使用。', 3),
    (N'交通票可用在哪些地區？範圍怎麼查？', N'每張票券適用區域不同，請參考交通票券頁面中的使用國家/地區範圍說明。', 3),

    (N'使用票券當天景點如果關閉怎麼辦？', N'若遇到不可抗力停業，請聯繫我們協助辦理延期或退款（依景點規定）。', 4),
    (N'有哪些景點可以套票一起買更便宜？', N'我們提供多種套票組合（如海洋館、陸地館、含VIP通行證），歡迎至套票專區選購。', 4),
    (N'票券需要提前預約入場時間嗎？', N'有些景點需預約時段，請依訂購頁提示選擇日期與時間，或於票券上查閱。', 4),
    (N'小孩也需要門票嗎？有優惠嗎？', N'部分景點對兒童或嬰兒免費或優惠，詳細請見產品說明的票種說明欄位。', 4),

    (N'一定要建立行程規劃才能夠下訂單嗎？', N'是的，您必續於行程規劃頁面中選擇與編輯您的行程安排後，方可於訂購頁面下單。', 5),
    (N'行程規劃中能夠同時排定飯店、造訪景點與交通方式嗎？', N'是的，您可於行程規劃頁面中排定整套旅程規劃。', 5),
    (N'行程規劃後如何下訂單', N'您可於行程排妥後，針對飯店、景點、交通票券的部分系統提供整筆下單與付款，機票的部分，不同航班您則必須分開下單與完成結帳。', 5),
    (N'行程排定之後可以改嗎？', N'在正式下單前，所有您安排的行程規劃都可以做修改，於正式下單後，則必須透過聯繫客服取消訂單後，重新規劃行程。', 5),

    (N'訂單金額中包含哪些國際稅費？會另加收嗎？', N'訂單金額已包含各國基本稅費（如消費稅、住宿稅等），若有額外費用如地區附加稅或服務費，將於結帳前清楚列示，無隱藏費用。', 6),
    (N'我可以取得報帳用的正式收據或發票嗎？', N'可以，請聯繫客服提供訂單號碼，我們會開立發票或報帳憑證給您。', 6),
    (N'國際訂單可以退稅嗎？怎麼申請？', N'一般線上平台訂單因為不屬於「現場購物」，多數無法申請傳統退稅。若飯店或票券具退稅條件，品項頁面中會標明並附上說明。', 6),
    (N'為什麼我被收取了境外交易手續費？這筆費用是平台收的嗎？', N'境外交易手續費為您的信用卡發卡銀行依國際交易規則額外加收，並非由本平台收取。建議您聯繫銀行確認手續費計算規則。', 6),

    (N'我要怎麼查詢我的訂單？', N'請登入會員後進入「會員中心，歷史訂單紀錄」頁面即可查詢所有訂單紀錄。', 7),
    (N'忘記密碼了怎麼辦？可以重設嗎？', N'點選登入頁「忘記密碼」可重設密碼，系統會寄送重設連結至您的信箱。', 7),
    (N'如何使用優惠券或集點卡？', N'於下單時您可選擇符合使用條件的優惠券與進行點數折扣，亦可於禮物中心依據對應的點數要求，兌換指定品項。', 7),
    (N'支援哪些付款方式？可以分期嗎？', N'我們支援信用卡、LINE Pay，部分卡片可分期，請見結帳頁面說明。', 7);
END;
GO


IF OBJECT_ID('customer_service_message', 'U') IS NULL
BEGIN
    CREATE TABLE customer_service_message(
        message_id INT PRIMARY KEY IDENTITY(1,1),
        message_content NVARCHAR(MAX) NOT NULL,
        chat_room_id INT NOT NULL FOREIGN KEY REFERENCES customer_service_chatroom(chat_room_id),
        question_id INT FOREIGN KEY REFERENCES customer_service_question(question_template_id),
        sent_time DATETIME NOT NULL DEFAULT GETDATE(),
        admin_id INT FOREIGN KEY REFERENCES admins(admin_id)
    );
END;
GO

IF OBJECT_ID('message_feedback', 'U') IS NULL
BEGIN
    CREATE TABLE message_feedback(
        feedback_id INT PRIMARY KEY IDENTITY(1,1),
        message_id INT NOT NULL FOREIGN KEY REFERENCES customer_service_message(message_id),
        is_good BIT NOT NULL,
        feedback_at DATETIME NOT NULL DEFAULT GETDATE()
    );
END;
GO
----- Customer Services -----


----- Consultation -----
IF OBJECT_ID('consults', 'U') IS NULL
BEGIN
CREATE TABLE consults(
    consult_id INT PRIMARY KEY IDENTITY(1,1),
    [user_id] INT FOREIGN KEY REFERENCES users(id),
    [name] NVARCHAR(MAX) NOT NULL,
    gender INT NOT NULL,
    email VARCHAR(MAX) NOT NULL,
    phone VARCHAR(MAX) NOT NULL,
    consult_content NVARCHAR(MAX) NOT NULL,
    consult_type_id INT NOT NULL DEFAULT 1 FOREIGN KEY REFERENCES consult_type(consult_type_id),
    consult_time DATETIME NOT NULL DEFAULT GETDATE()
);
END;
GO

IF OBJECT_ID('reply', 'U') IS NULL
BEGIN
    CREATE TABLE reply(
        reply_id INT PRIMARY KEY IDENTITY(1,1),
        consult_id INT NOT NULL FOREIGN KEY REFERENCES consults(consult_id),
        admin_id INT NOT NULL FOREIGN KEY REFERENCES admins(admin_id),
        reply_by NVARCHAR(MAX) NOT NULL,
        reply_time DATETIME NOT NULL DEFAULT GETDATE()
    );
END;
GO
----- Consultation -----
/* =========== Tables With FKs end ===========*/

SET NOCOUNT ON;

/* ==== 基本變數 ==== */
DECLARE @vendor_id     INT = 3;      -- 測試商家
DECLARE @city_id       INT = 35;     -- Tokyo
DECLARE @country_id    INT = 2;    -- Japan (可改)
DECLARE @updated_by    INT = 3;
DECLARE @start_date    DATE = '2025-07-01';
DECLARE @end_date      DATE = '2025-12-31';
/* ===== 新增旅宿：東京飯店式公寓 ===== */
IF NOT EXISTS (SELECT 1 FROM lodgings WHERE lodging_name = N'東京飯店式公寓')
BEGIN
    DECLARE @lodging_id        INT,
            @room_family_deluxe  INT,
            @room_family_premium INT;

    INSERT INTO lodgings (
        vendor_id, lodging_name, lodging_type,
        [description], [address],
        city_id, country_id,
        latitude, longitude,
        lodging_tel, email,
        is_active, delete_status,
        created_at, updated_at, updated_by
    )
    VALUES (
        @vendor_id, N'東京飯店式公寓', 4,
        N'位於東京西多摩地區的大型飯店式公寓，設有多種家庭房型與完善的設施。',
        N'東京西多摩地區青梅市',
        @city_id, @country_id,
        35.7881, 139.2636,
        '06-8765-4321', 'hotel_apartment@example.com',
        1, 1, GETDATE(), GETDATE(), @updated_by
    );

    SET @lodging_id = SCOPE_IDENTITY();

    /* ==== 房型 ==== */
    INSERT INTO room_types (
        lodging_id, [name], [description],
        price_per_night, max_guests,
        created_by, updated_by
    )
    VALUES
    (@lodging_id, N'豪華家庭房',
     N'廚房設施齊全，配備爐子、冰箱、廚具和微波爐。此家庭房空間寬敞，設有空調、洗衣機和私人衛浴，衛浴內附浴缸。此住宿空間配有 4 張床。',
     14000, 8, @vendor_id, @vendor_id),
    (@lodging_id, N'高級家庭房',
     N'廚房配備爐子、冰箱、廚具和微波爐，可供住客準備餐點。此家庭房空間寬敞，設有空調、洗衣機和私人衛浴，衛浴內附浴缸。此住宿空間配有 1 張床。',
     8000, 2, @vendor_id, @vendor_id);

    SELECT @room_family_deluxe  = id FROM room_types WHERE lodging_id = @lodging_id AND [name] = N'豪華家庭房';
    SELECT @room_family_premium = id FROM room_types WHERE lodging_id = @lodging_id AND [name] = N'高級家庭房';

    /* ==== 房型圖片 (示範) ==== */
    INSERT INTO lodging_images (
        image_type, lodging_id, room_type_id,
        image_url, mime_type, sort_order,
        is_active, delete_status
    )
VALUES
-- 豪華家庭房
('room', @lodging_id, @room_family_deluxe, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496742974.jpg?k=acb4c5cb4b2eea9f3fef5ee9e9af9aeee25d46e41a4b75b5d99a8b4dd51e2e40&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id, @room_family_deluxe, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496758387.jpg?k=e6ac461706407997993bd752d587fe468458ba273710e3d6facfccaf67c6afd6&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id, @room_family_deluxe, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496764668.jpg?k=e43e82b0e500d9e2d2af994ba8f5a9e16f425c83adc3ea2240d8a2b0f466b169&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id, @room_family_deluxe, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496743150.jpg?k=0ef0268a14b6945b13f8482ecb271d5dbff1d6f91d180bf2749bb732fa41ee60&o=', 'image/jpeg', 3, 1, 1),
('room', @lodging_id, @room_family_deluxe, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496758425.jpg?k=3f6d9200313e59a39f3780c8b5247425e002b2da78298f77ec36aad278f9d467&o=', 'image/jpeg', 4, 1, 1),	    

-- 高級家庭房
('room', @lodging_id, @room_family_premium, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496743014.jpg?k=818f8ffb1098d3b4d7aee7106ee4815f170d7d85582eca6f1795501b95801c30&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id, @room_family_premium, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496743159.jpg?k=64062507a462453eaed0312cb6b45a8831793104c523e60531f233abba4944e4&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id, @room_family_premium, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496743024.jpg?k=0d2854a3bf2cf41d946bdb9e47c00360dd4e691012d0d81e1689fa52d4d74b2f&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id, @room_family_premium, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496764701.jpg?k=3750cf402a177dfd249ca415ecb087f794d43d789c31e9e81b99e3e7372f45de&o=', 'image/jpeg', 3, 1, 1),	    
('room', @lodging_id, @room_family_premium, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496760477.jpg?k=31c877d4d10f749b8b24c292740d3a1010c177a01c2bd99dda617431536b1e3b&o=', 'image/jpeg', 4, 1, 1);


    /* ==== 床型 ==== */
    INSERT INTO room_type_beds (room_type_id, bed_type_id, quantity) VALUES
    (@room_family_deluxe,  2, 4),  -- 雙人床 ×4
    (@room_family_premium, 2, 1);  -- 雙人床 ×1

    /* ==== 設施：1=電視 2=洗衣機 3=廚房 4=免費wifi 11=免費行李寄放 === */
    INSERT INTO room_facilities (room_type_id, facility_id) VALUES
    (@room_family_deluxe, 1), (@room_family_deluxe, 2), (@room_family_deluxe, 3), (@room_family_deluxe, 4),
    (@room_family_premium, 1), (@room_family_premium, 2), (@room_family_premium, 3), (@room_family_premium, 4),(@room_family_premium, 11);

    /* ==== 房量 (每日 3 間) ==== */
    ;WITH d AS (
        SELECT @start_date AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_deluxe, d, 3, @vendor_id, @vendor_id FROM d OPTION (MAXRECURSION 0);

    ;WITH d AS (
        SELECT @start_date AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_premium, d, 3, @vendor_id, @vendor_id FROM d OPTION (MAXRECURSION 0);

    /* ==== 旅宿外觀圖片 (示範 1 張) ==== */
	 INSERT INTO lodging_images (
    image_type, lodging_id, room_type_id,
    image_url, mime_type, sort_order,
    is_active, delete_status
)
VALUES
('lodging', @lodging_id, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496760541.jpg?k=bf4bae4d5c531173af1b27757a9487a3f1f4231d83ac3bbba8282f97f7720db0&o=', 'image/jpeg', 0, 1, 1),
('lodging', @lodging_id, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496764697.jpg?k=7712d58be2db77239e154cd46afa225064104a9b2e717cbde5dc146ddf46a24e&o=', 'image/jpeg', 1, 1, 1),
('lodging', @lodging_id, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496760545.jpg?k=d7796f74c8cbb7e69a3f506bbab35fd66e22e181e27229e7b6b0b6c0c8297a99&o=', 'image/jpeg', 2, 1, 1),
('lodging', @lodging_id, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496760544.jpg?k=8ed0a696dbbdb2efb7fd5b4ac4d3b6e990b8318b812860b11f20f49b0e3af904&o=', 'image/jpeg', 3, 1, 1),
('lodging', @lodging_id, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/496760481.jpg?k=200310b0683dac665342f5038e2484f9d194d99dd791d446efae7d320049f6a1&o=', 'image/jpeg', 4, 1, 1);

    /* ======================================================
       ★ 自動產生 10 筆 4★ / 5★ 留言  ★
       - 偶數迴圈插 5 星，奇數 4 星
       - user_id 先固定為 4 (可改變)
    ====================================================== */
    DECLARE @i INT = 1;
    WHILE @i <= 10
    BEGIN
        INSERT INTO comments (lodging_id, user_id, content, rating, created_at)
        VALUES (
            @lodging_id,
            4,
            N'#' + CAST(@i AS NVARCHAR) + N'：住宿體驗超棒！',
            CASE WHEN @i % 2 = 0 THEN 5 ELSE 4 END,
            DATEADD(DAY, -@i, GETDATE())  -- 時間向前推，較真實
        );
        SET @i += 1;
    END;

    PRINT N'東京飯店式公寓 已建立 (ID=' + CAST(@lodging_id AS NVARCHAR) + N')';
END
ELSE
    PRINT N'東京飯店式公寓 已存在，略過新增';

/* ===== 新增旅宿：東京民宿 ===== */
IF NOT EXISTS (SELECT 1 FROM lodgings WHERE lodging_name = N'東京民宿')
BEGIN
    DECLARE @lodging_id1        INT,
            @room_family_deluxe1  INT,
            @room_family_premium1 INT;

    INSERT INTO lodgings (
        vendor_id, lodging_name, lodging_type,
        [description], [address],
        city_id, country_id,
        latitude, longitude,
        lodging_tel, email,
        is_active, delete_status,
        created_at, updated_at, updated_by
    )
    VALUES (
        @vendor_id, N'東京民宿', 2,
        N'位於東京鬧區的溫馨日式民宿，提供和式客房與自助廚房。',
        N'東京都千代田區',
        @city_id, @country_id,
        34.7089, 135.5016,
        '06-1234-5678', 'osakabb@example.com',
        1, 1, GETDATE(), GETDATE(), @updated_by
    );

    SET @lodging_id1 = SCOPE_IDENTITY();

    /* ==== 房型 ==== */
    INSERT INTO room_types (
        lodging_id, [name], [description],
        price_per_night, max_guests,
        created_by, updated_by
    )
    VALUES
    (@lodging_id1, N'四人和式房',
     N'榻榻米地板、日式被褥，可容納 4 人。附私人衛浴。',
     6000, 4, @vendor_id, @vendor_id),
    (@lodging_id1, N'雙人和式房',
     N'舒適榻榻米與木質裝潢，適合 2 人入住。',
     3500, 2, @vendor_id, @vendor_id);

    SELECT @room_family_deluxe1  = id FROM room_types WHERE lodging_id = @lodging_id1 AND [name] = N'四人和式房';
    SELECT @room_family_premium1 = id FROM room_types WHERE lodging_id = @lodging_id1 AND [name] = N'雙人和式房';

    /* ==== 房型圖片 (示範) ==== */
    INSERT INTO lodging_images (
        image_type, lodging_id, room_type_id,
        image_url, mime_type, sort_order,
        is_active, delete_status
    )
VALUES
-- 四人和式房
('room', @lodging_id1, @room_family_deluxe1, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/94268495.jpg?k=b64252153c91108fe39d84bdd805ae04195fdee6bf63d41c318e0d17513e354c&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id1, @room_family_deluxe1, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/137445520.jpg?k=a3750d7e4a4ab66d73689ffdbb71b8dea493d5adddd0a2b776ee55d0c39b2f59&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id1, @room_family_deluxe1, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/131121201.jpg?k=67a46157804155d1e47313af9015b5bb2b9c8e850ba811bda90b24bbc4edf2b1&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id1, @room_family_deluxe1, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/137445243.jpg?k=ddd440d07cc4c8e0f82f866ab4ab281812590c89692e80548f6ffb842f042859&o=', 'image/jpeg', 3, 1, 1),
('room', @lodging_id1, @room_family_deluxe1, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/131120540.jpg?k=469037bc8005321e04d335755fe38b268d3d6a41bd4178a6e50c65bfbcc5aafe&o=', 'image/jpeg', 4, 1, 1),

-- 雙人和式房
('room', @lodging_id1, @room_family_premium1, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/225384439.jpg?k=431a4671987219d4d33c76c0179420f0922f62025c730b0462b645742639881f&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id1, @room_family_premium1, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/222992715.jpg?k=a989dbd38a44fc0dde86dde5b687f7cf241707d4b296cf5ffa8fc357b7928736&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id1, @room_family_premium1, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/575275821.jpg?k=1cd9e1aa5f27a6c6d394d46db93ad8071f4dd755dd3223eb80fe43a89d3cf7e2&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id1, @room_family_premium1, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/683430362.jpg?k=e17e5b0117cb9747808f55c7491b5832406be5e26d47717f940c58790c83314f&o=', 'image/jpeg', 3, 1, 1),
('room', @lodging_id1, @room_family_premium1, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/683430377.jpg?k=b2a1dcf6a055c791a9aa019ca0dc6e9522149acfdc770335ccd48000042b9504&o=', 'image/jpeg', 4, 1, 1);


    /* ==== 床型 ==== */
    INSERT INTO room_type_beds (room_type_id, bed_type_id, quantity) VALUES
    (@room_family_deluxe1,  1, 4),  -- 單人床 ×4
    (@room_family_premium1, 2, 1);  -- 雙人床 ×1

    /* ==== 設施：1=電視 2=洗衣機 3=廚房 4=免費行李寄放 9=接駁服務 === */
    INSERT INTO room_facilities (room_type_id, facility_id) VALUES
    (@room_family_deluxe1, 1), (@room_family_deluxe1, 2), (@room_family_deluxe1, 3), (@room_family_deluxe1, 4), (@room_family_deluxe1, 9),
    (@room_family_premium1, 1), (@room_family_premium1, 2), (@room_family_premium1, 3), (@room_family_premium1, 4), (@room_family_premium1, 9);

    /* ==== 房量 (每日 3 間) ==== */
    ;WITH d AS (
        SELECT @start_date AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_deluxe1, d, 3, @vendor_id, @vendor_id FROM d OPTION (MAXRECURSION 0);

    ;WITH d AS (
        SELECT @start_date AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_premium1, d, 3, @vendor_id, @vendor_id FROM d OPTION (MAXRECURSION 0);

    /* ==== 旅宿外觀圖片 (示範 1 張) ==== */
	 INSERT INTO lodging_images (
    image_type, lodging_id, room_type_id,
    image_url, mime_type, sort_order,
    is_active, delete_status
)
VALUES
('lodging', @lodging_id1, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/445798573.jpg?k=fe9f245e3e9a279b96fe2d25a04e76d06f9c6348de14c92a7f486b15b14c6134&o=', 'image/jpeg', 0, 1, 1),
('lodging', @lodging_id1, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/171408348.jpg?k=43c1385a3b1cedb79fc6c62b7fd1fb6d2c4077d4ac97b31fd1b2e2ec4a0805f0&o=', 'image/jpeg', 1, 1, 1),
('lodging', @lodging_id1, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/90132859.jpg?k=d9c330add40f77af50caed55d78ff10913b362017c54a934e406f518ebb817ca&o=', 'image/jpeg', 2, 1, 1),
('lodging', @lodging_id1, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/171408358.jpg?k=5bf37ddc2898fe9ae8ce1e864372095aff5e02c593ee940ad1f58f6bb0367eaa&o=', 'image/jpeg', 3, 1, 1),
('lodging', @lodging_id1, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/171408346.jpg?k=de948682b09c3d3f8ac2b3cd7347320901029d97f1c99905cf97f9eba6808e04&o=', 'image/jpeg', 4, 1, 1);

    /* ======================================================
       ★ 自動產生 10 筆 2★ / 5★ 留言  ★
       - 偶數迴圈插 5 星，奇數 2 星
       - user_id 先固定為 4 (可改變)
    ====================================================== */
    DECLARE @i1 INT = 1;
    WHILE @i1 <= 10
    BEGIN
        INSERT INTO comments (lodging_id, user_id, content, rating, created_at)
        VALUES (
            @lodging_id1,
            4,
            N'#' + CAST(@i AS NVARCHAR) + N'：東京最強，住宿體驗超棒！',
            CASE WHEN @i1 % 2 = 0 THEN 5 ELSE 2 END,
            DATEADD(DAY, -@i1, GETDATE())  -- 時間向前推，較真實
        );
        SET @i1 += 1;
    END;

    PRINT N'東京民宿 已建立 (ID=' + CAST(@lodging_id1 AS NVARCHAR) + N')';
END
ELSE
    PRINT N'東京民宿 已存在，略過新增';

/* ===== 新增旅宿：東京台場大飯店 ===== */
IF NOT EXISTS (SELECT 1 FROM lodgings WHERE lodging_name = N'東京台場大飯店')
BEGIN
    DECLARE @lodging_id2        INT,
            @room_family_deluxe2  INT,
            @room_family_premium2 INT;

    INSERT INTO lodgings (
        vendor_id, lodging_name, lodging_type,
        [description], [address],
        city_id, country_id,
        latitude, longitude,
        lodging_tel, email,
        is_active, delete_status,
        created_at, updated_at, updated_by
    )
    VALUES (
        @vendor_id, N'東京台場大飯店', 1,
        N'位於東京，距離台場海灘 1.9 公里，設有共用休息室、餐廳和酒吧等多種設施。',
        N'東京都港區',
        @city_id, @country_id,
        35.6402, 139.7601,
        '06-1234-1234', 'OdaibaBeachHotel@example.com',
        1, 1, GETDATE(), GETDATE(), @updated_by
    );

    SET @lodging_id2 = SCOPE_IDENTITY();

    /* ==== 房型 ==== */
    INSERT INTO room_types (
        lodging_id, [name], [description],
        price_per_night, max_guests,
        created_by, updated_by
    )
    VALUES
    (@lodging_id2, N'大雙人床房',
     N'這間雙人房提供免費盥洗用品和浴袍，以及附浴缸、淋浴和坐浴盆的私人衛浴。空調雙人房配有平面電視、隔音牆、茶/咖啡壺和衣櫃，享有城市景觀。客房提供一張床。',
     12000, 2, @vendor_id, @vendor_id),
    (@lodging_id2, N'豪華雙床房',
     N'雙人房空間寬敞，設有空調、隔音牆和私人衛浴（附浴缸與淋浴設施）。此住宿空間設有 2 張床。',
     18000, 2, @vendor_id, @vendor_id);

    SELECT @room_family_deluxe2  = id FROM room_types WHERE lodging_id = @lodging_id2 AND [name] = N'大雙人床房';
    SELECT @room_family_premium2 = id FROM room_types WHERE lodging_id = @lodging_id2 AND [name] = N'豪華雙床房';

    /* ==== 房型圖片 (示範) ==== */
    INSERT INTO lodging_images (
        image_type, lodging_id, room_type_id,
        image_url, mime_type, sort_order,
        is_active, delete_status
    )
VALUES
-- 大雙人床房
('room', @lodging_id2, @room_family_deluxe2, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/636807277.jpg?k=484b1a1a0bf929bfaef6e707868d85d04a85a5b0921307a55c2dbbd835a8ff84&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id2, @room_family_deluxe2, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/629573718.jpg?k=ae807fda008a82cbf17205bf9824002fac5aaaec3455671eedda20fdaeaf9da2&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id2, @room_family_deluxe2, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/626635880.jpg?k=c44a46bddbf455ed112ee668bb24694f69183aeb525a230125b49b39a40ce742&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id2, @room_family_deluxe2, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/640366929.jpg?k=2def1ff51151884cbd3c133008bcef11b5efcacf1f4a3435f07149fce8d1ae8f&o=', 'image/jpeg', 3, 1, 1),
('room', @lodging_id2, @room_family_deluxe2, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/628721552.jpg?k=a35da755b7bd0a7788709273955f7f67d435cc75ee4202c6ecc27c71efbc52e5&o=', 'image/jpeg', 4, 1, 1),

-- 豪華雙床房
('room', @lodging_id2, @room_family_premium2, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/640919262.jpg?k=b39eb39376d36217c7edbf956c276ccd233605493bddb5a08074454229ed82d7&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id2, @room_family_premium2, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/629573718.jpg?k=ae807fda008a82cbf17205bf9824002fac5aaaec3455671eedda20fdaeaf9da2&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id2, @room_family_premium2, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/626635880.jpg?k=c44a46bddbf455ed112ee668bb24694f69183aeb525a230125b49b39a40ce742&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id2, @room_family_premium2, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/640366929.jpg?k=2def1ff51151884cbd3c133008bcef11b5efcacf1f4a3435f07149fce8d1ae8f&o=', 'image/jpeg', 3, 1, 1),
('room', @lodging_id2, @room_family_premium2, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/628721552.jpg?k=a35da755b7bd0a7788709273955f7f67d435cc75ee4202c6ecc27c71efbc52e5&o=', 'image/jpeg', 4, 1, 1);


    /* ==== 床型 ==== */
    INSERT INTO room_type_beds (room_type_id, bed_type_id, quantity) VALUES
    (@room_family_deluxe2,  2, 1),  -- 雙人床 ×2
    (@room_family_premium2, 1, 2);  -- 單人床 ×2

    /* ==== 設施 === */
    INSERT INTO room_facilities (room_type_id, facility_id) VALUES
    (@room_family_deluxe2, 1), (@room_family_deluxe2, 2), (@room_family_deluxe2, 4), (@room_family_deluxe2, 5), (@room_family_deluxe2, 6),(@room_family_deluxe2, 7), (@room_family_deluxe2, 8), (@room_family_deluxe2, 9), (@room_family_deluxe2, 10), (@room_family_deluxe2, 11), (@room_family_deluxe2, 12), (@room_family_deluxe2, 13),
    (@room_family_premium2, 1), (@room_family_premium2, 2), (@room_family_premium2, 4), (@room_family_premium2, 5),(@room_family_premium2, 6), (@room_family_premium2, 7), (@room_family_premium2, 8), (@room_family_premium2, 9),  (@room_family_premium2, 10), (@room_family_premium2, 11), (@room_family_premium2, 12),  (@room_family_premium2, 13);

    /* ==== 房量 (每日 3 間) ==== */
    ;WITH d AS (
        SELECT @start_date AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_deluxe2, d, 3, @vendor_id, @vendor_id FROM d OPTION (MAXRECURSION 0);

    ;WITH d AS (
        SELECT @start_date AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_premium2, d, 3, @vendor_id, @vendor_id FROM d OPTION (MAXRECURSION 0);

    /* ==== 旅宿外觀圖片 (示範 1 張) ==== */
	 INSERT INTO lodging_images (
    image_type, lodging_id, room_type_id,
    image_url, mime_type, sort_order,
    is_active, delete_status
)
VALUES
('lodging', @lodging_id2, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/628016263.jpg?k=d08c4b1e663242f2b179aa779d1e4ee451e07d4d303a5b36dd1b51f3305ca088&o=', 'image/jpeg', 0, 1, 1),
('lodging', @lodging_id2, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/626838113.jpg?k=ae6bcbf15aee357f60cb8fb2daa75622f103994634f20d161b390af569b8f72c&o=', 'image/jpeg', 1, 1, 1),
('lodging', @lodging_id2, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/626635883.jpg?k=81998ca3bbe77111ae0e53618ed616f2c085bdccfa002ecd1e8d69f0bbd49efe&o=', 'image/jpeg', 2, 1, 1),
('lodging', @lodging_id2, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/626838696.jpg?k=58b551756316067bca78be109a44f40190e564dd9c53679a75a3a6a4494e43c2&o=', 'image/jpeg', 3, 1, 1),
('lodging', @lodging_id2, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/171408346.jpg?k=de948682b09c3d3f8ac2b3cd7347320901029d97f1c99905cf97f9eba6808e04&o=', 'image/jpeg', 4, 1, 1);

    /* ======================================================
       ★ 自動產生 10 筆 5★ 留言  ★
       - user_id 先固定為 4 韓文　5日文　(可改變)
    ====================================================== */
	DECLARE @i2 INT = 1;
	DECLARE @current_user_id INT;
	DECLARE @current_content NVARCHAR(500);

	WHILE @i2 <= 10
	BEGIN
		-- 偶數為日文 user_id 5，奇數為韓文 user_id 4
		SET @current_user_id = CASE WHEN @i2 % 2 = 0 THEN 5 ELSE 4 END;

		-- 根據 user_id 給不同語言留言
		SET @current_content =
			CASE
				WHEN @current_user_id = 4 THEN N'#' + CAST(@i2 AS NVARCHAR) + N'：서울 최고의 호텔이에요! 매우 만족했습니다.'  -- 韓文
				WHEN @current_user_id = 5 THEN N'#' + CAST(@i2 AS NVARCHAR) + N'：東京で最高のホテル体験でした！'              -- 日文
			END;

		INSERT INTO comments (
			lodging_id, user_id, content, rating, is_verified, created_at
		)
		VALUES (
			@lodging_id2,
			@current_user_id,
			@current_content,
			5,
			1,
			DATEADD(DAY, -@i2, GETDATE())
		);

		SET @i2 += 1;
	END;

    PRINT N'東京台場大飯店 已建立 (ID=' + CAST(@lodging_id2 AS NVARCHAR) + N')';
END
ELSE
    PRINT N'東京台場大飯店 已存在，略過新增';

/* ===== 新增旅宿：東京青年旅館 ===== */
IF NOT EXISTS (SELECT 1 FROM lodgings WHERE lodging_name = N'東京青年旅館')
BEGIN
    DECLARE @lodging_id3        INT,
            @room_family_deluxe3  INT,
            @room_family_premium3 INT;

    INSERT INTO lodgings (
        vendor_id, lodging_name, lodging_type,
        [description], [address],
        city_id, country_id,
        latitude, longitude,
        lodging_tel, email,
        is_active, delete_status,
        created_at, updated_at, updated_by
    )
    VALUES (
        @vendor_id, N'東京青年旅館', 3,
        N'位於東京中心，有空調客房，有餐廳和 WiFi（免費）。這間 2.5 星級青年旅館提供共用廚房。這間住宿的部分房間有市景陽台。',
        N'東京都千代田區',
        @city_id, @country_id,
        35.6852, 139.7528,
        '06-1234-4321', 'TurnTable@example.com',
        1, 1, GETDATE(), GETDATE(), @updated_by
    );

    SET @lodging_id3 = SCOPE_IDENTITY();

    /* ==== 房型 ==== */
    INSERT INTO room_types (
        lodging_id, [name], [description],
        price_per_night, max_guests,
        created_by, updated_by
    )
    VALUES
    (@lodging_id3, N'混合宿舍房',
     N'這張宿舍床位可使用空調、簡易廚房和微波爐。',
     1200, 1, @vendor_id, @vendor_id),
    (@lodging_id3, N'女性宿舍房',
     N'這張宿舍床位可使用空調、簡易廚房和微波爐。',
     1200, 1, @vendor_id, @vendor_id);

    SELECT @room_family_deluxe3  = id FROM room_types WHERE lodging_id = @lodging_id3 AND [name] = N'混合宿舍房';
    SELECT @room_family_premium3 = id FROM room_types WHERE lodging_id = @lodging_id3 AND [name] = N'女性宿舍房';

    /* ==== 房型圖片 (示範) ==== */
    INSERT INTO lodging_images (
        image_type, lodging_id, room_type_id,
        image_url, mime_type, sort_order,
        is_active, delete_status
    )
VALUES
-- 混合宿舍房
('room', @lodging_id3, @room_family_deluxe3, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/696859504.jpg?k=94db37a390098f7086419ad022888ec14091f30f8cd66e641b79e610df8ac8e9&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id3, @room_family_deluxe3, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/213815029.jpg?k=51e2941f48aba1e5aa2fac18a66a65c6424aeeef900c0a23d0c08ddf6e54451b&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id3, @room_family_deluxe3, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/213815038.jpg?k=cdca4cc7b12c67d0cf974141f8d3106e52decc4f3312018a9988dcd04ad6243d&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id3, @room_family_deluxe3, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/213815025.jpg?k=f0bbb4c8bf4414d7278dc110bf49e45f59a65d246611776f78ba13ca45800acc&o=', 'image/jpeg', 3, 1, 1),
('room', @lodging_id3, @room_family_deluxe3, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/213808994.jpg?k=6c9937ca7df6e408072a62681418865c29d6f5a9c0404881baf12fd08d7eab94&o=', 'image/jpeg', 4, 1, 1),

-- 女性宿舍房
('room', @lodging_id3, @room_family_premium3, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/516483038.jpg?k=964c791638302dcac1bb0d3072968be6f2afbe4cbd2b5843607142ad582483e6&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id3, @room_family_premium3, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/213815029.jpg?k=51e2941f48aba1e5aa2fac18a66a65c6424aeeef900c0a23d0c08ddf6e54451b&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id3, @room_family_premium3, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/213815038.jpg?k=cdca4cc7b12c67d0cf974141f8d3106e52decc4f3312018a9988dcd04ad6243d&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id3, @room_family_premium3, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/213815025.jpg?k=f0bbb4c8bf4414d7278dc110bf49e45f59a65d246611776f78ba13ca45800acc&o=', 'image/jpeg', 3, 1, 1),
('room', @lodging_id3, @room_family_premium3, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/213808994.jpg?k=6c9937ca7df6e408072a62681418865c29d6f5a9c0404881baf12fd08d7eab94&o=', 'image/jpeg', 4, 1, 1);


    /* ==== 床型 ==== */
    INSERT INTO room_type_beds (room_type_id, bed_type_id, quantity) VALUES
    (@room_family_deluxe3,  1, 4),  -- 單人床 ×4
    (@room_family_premium3, 2, 1);  -- 雙人床 ×1

    /* ==== 設施：1=電視 2=洗衣機 3=廚房 4=免費wifi  === */
    INSERT INTO room_facilities (room_type_id, facility_id) VALUES
    (@room_family_deluxe3, 1), (@room_family_deluxe3, 2), (@room_family_deluxe3, 3), (@room_family_deluxe3, 4), 
    (@room_family_premium3, 1), (@room_family_premium3, 2), (@room_family_premium3, 3), (@room_family_premium3, 4);

    /* ==== 房量 (每日 3 間) ==== */
    ;WITH d AS (
        SELECT @start_date AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_deluxe3, d, 3, @vendor_id, @vendor_id FROM d OPTION (MAXRECURSION 0);

    ;WITH d AS (
        SELECT @start_date AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_premium3, d, 3, @vendor_id, @vendor_id FROM d OPTION (MAXRECURSION 0);

    /* ==== 旅宿外觀圖片 (示範 1 張) ==== */
	 INSERT INTO lodging_images (
    image_type, lodging_id, room_type_id,
    image_url, mime_type, sort_order,
    is_active, delete_status
)
VALUES
('lodging', @lodging_id3, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/426813102.jpg?k=cd4673776692a9981b368355e1025143e0c070dcb521c7ec921b52b38227e2d4&o=', 'image/jpeg', 0, 1, 1),
('lodging', @lodging_id3, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/290650959.jpg?k=c07845844d95b6b58a70ff9c3ab4ead7231f3c6bf8f9e562b589abc420469a58&o=', 'image/jpeg', 1, 1, 1),
('lodging', @lodging_id3, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/290651208.jpg?k=3ce0a96a91b80cc2e7f7915d947f6228b9727f0783578a5e5d767f99bc7eb8a2&o=', 'image/jpeg', 2, 1, 1),
('lodging', @lodging_id3, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/425482783.jpg?k=4b1b6815c2b5c642b03240a028c62708bb3bfa2ae243fb60640983db461c233e&o=', 'image/jpeg', 3, 1, 1),
('lodging', @lodging_id3, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/425482771.jpg?k=624b1cae0f0ee3984913c172875d894c4e937f4a93b382776098e9c80403891f&o=', 'image/jpeg', 4, 1, 1);

    /* ======================================================
       ★ 自動產生 10 筆 1★ / 4★ 留言  ★
       - 偶數迴圈插 5 星，奇數 3 星
       - user_id 先固定為 4 (可改變)
    ====================================================== */
    DECLARE @i3 INT = 1;
    WHILE @i3 <= 10
    BEGIN
        INSERT INTO comments (lodging_id, user_id, content, rating, created_at)
        VALUES (
            @lodging_id3,
            4,
            N'#' + CAST(@i AS NVARCHAR) + N'：東京青年旅館，住宿體驗超棒！',
            CASE WHEN @i3 % 2 = 0 THEN 4 ELSE 1 END,
            DATEADD(DAY, -@i3, GETDATE())  -- 時間向前推，較真實
        );
        SET @i3 += 1;
    END;

    PRINT N'東京青年旅館 已建立 (ID=' + CAST(@lodging_id3 AS NVARCHAR) + N')';
END
ELSE
    PRINT N'東京青年旅館 已存在，略過新增';

SET NOCOUNT ON;

/* ==== 基本變數 ==== */
DECLARE @vendor_id1     INT = 3;      -- 測試商家
DECLARE @city_id1       INT = 70;     -- 首爾
DECLARE @country_id1    INT = 3;    -- 韓國
DECLARE @updated_by1    INT = 3;
DECLARE @start_date1    DATE = '2025-07-01';
DECLARE @end_date1      DATE = '2025-12-31';
/* ===== 新增旅宿：首爾大飯店 ===== */
IF NOT EXISTS (SELECT 1 FROM lodgings WHERE lodging_name = N'首爾大飯店')
BEGIN
    DECLARE @lodging_id4        INT,
            @room_family_deluxe4  INT,
            @room_family_premium4 INT;

    INSERT INTO lodgings (
        vendor_id, lodging_name, lodging_type,
        [description], [address],
        city_id, country_id,
        latitude, longitude,
        lodging_tel, email,
        is_active, delete_status,
        created_at, updated_at, updated_by
    )
    VALUES (
        @vendor_id1, N'首爾大飯店', 1,
        N'飯店位於首爾江南商業區，提供現代化空調客房、室內溫水游泳池與高爾夫練習設施，距離新論峴地鐵站（9號線）4號出口僅340公尺。',
        N'首爾江南商業區',
        @city_id1, @country_id1,
        37.5051, 127.0250,
        '010-8765-4321', 'Ambassador@example.com',
        1, 1, GETDATE(), GETDATE(), @updated_by1
    );

    SET @lodging_id4 = SCOPE_IDENTITY();

    /* ==== 房型 ==== */
    INSERT INTO room_types (
        lodging_id, [name], [description],
        price_per_night, max_guests,
        created_by, updated_by
    )
    VALUES
    (@lodging_id4, N'高級雙床房',
     N'這間客房鋪設地毯，提供平面電視、沙發和迷你吧。',
     2400, 2, @vendor_id1, @vendor_id1),
    (@lodging_id4, N'豪華加大雙人床房',
     N'廚房配備爐子、冰箱、廚具和微波爐，可供住客準備餐點。此家庭房空間寬敞，設有空調、洗衣機和私人衛浴，衛浴內附浴缸。此住宿空間配有 1 張床。',
     4000, 2, @vendor_id1, @vendor_id1);

    SELECT @room_family_deluxe4  = id FROM room_types WHERE lodging_id = @lodging_id4 AND [name] = N'高級雙床房';
    SELECT @room_family_premium4 = id FROM room_types WHERE lodging_id = @lodging_id4 AND [name] = N'豪華加大雙人床房';

    /* ==== 房型圖片 (示範) ==== */
    INSERT INTO lodging_images (
        image_type, lodging_id, room_type_id,
        image_url, mime_type, sort_order,
        is_active, delete_status
    )
VALUES
-- 高級雙床房
('room', @lodging_id4, @room_family_deluxe4, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/704207628.jpg?k=1eded9e96fe8bf764fc52666ce05be9d9e3532c5fd824f37d85f4a4f76fb6fce&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id4, @room_family_deluxe4, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/704716494.jpg?k=8013847e96c06ab1eb70c4501532c9be8b7dc43a6dd9e49e1944e73a391405a5&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id4, @room_family_deluxe4, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/560916060.jpg?k=6e2bc9b1cf523ccaad34154427c8a634bc1235f14878a5d4af94e77196b082a0&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id4, @room_family_deluxe4, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/560916066.jpg?k=961ab7ff5fa37f5f807d68f5080f6b86115611e0ebde20523e971f5512a32e20&o=', 'image/jpeg', 3, 1, 1),
('room', @lodging_id4, @room_family_deluxe4, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/562800182.jpg?k=b9aecf903210b4cd9520a81096c588aca021238693929ff680c30c70eaeea289&o=', 'image/jpeg', 4, 1, 1),	    

-- 豪華加大雙人床房
('room', @lodging_id4, @room_family_premium4, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/704207633.jpg?k=1094e8fb6ce407023ea32673bd0a963b4782d63db85a73e698c46045d4991ae1&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id4, @room_family_premium4, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/704716494.jpg?k=8013847e96c06ab1eb70c4501532c9be8b7dc43a6dd9e49e1944e73a391405a5&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id4, @room_family_premium4, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/560916060.jpg?k=6e2bc9b1cf523ccaad34154427c8a634bc1235f14878a5d4af94e77196b082a0&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id4, @room_family_premium4, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/560916066.jpg?k=961ab7ff5fa37f5f807d68f5080f6b86115611e0ebde20523e971f5512a32e20&o=', 'image/jpeg', 3, 1, 1),	    
('room', @lodging_id4, @room_family_premium4, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/562800182.jpg?k=b9aecf903210b4cd9520a81096c588aca021238693929ff680c30c70eaeea289&o=', 'image/jpeg', 4, 1, 1);


    /* ==== 床型 ==== */
    INSERT INTO room_type_beds (room_type_id, bed_type_id, quantity) VALUES
    (@room_family_deluxe4,  2, 1),  -- 雙人床 ×1
    (@room_family_premium4, 2, 1);  -- 雙人床 ×1

    /* ==== 設施：1=電視 2=洗衣機 3=廚房 4=免費wifi 11=免費行李寄放 === */
    INSERT INTO room_facilities (room_type_id, facility_id) VALUES
    (@room_family_deluxe4, 1), (@room_family_deluxe4, 2), (@room_family_deluxe4, 4), (@room_family_deluxe4, 5),(@room_family_deluxe4, 6), (@room_family_deluxe4, 7), (@room_family_deluxe4, 8), (@room_family_deluxe4, 9),(@room_family_deluxe4, 10), (@room_family_deluxe4, 11), (@room_family_deluxe4, 12), (@room_family_deluxe4, 13),
    (@room_family_premium4, 1), (@room_family_premium4, 2), (@room_family_premium4, 4), (@room_family_premium4, 5),(@room_family_premium4, 6), (@room_family_premium4, 7), (@room_family_premium4, 8), (@room_family_premium4, 9),(@room_family_premium4, 10), (@room_family_premium4, 11), (@room_family_premium4, 12), (@room_family_premium4, 13);

    /* ==== 房量 (每日 3 間) ==== */
    ;WITH d AS (
        SELECT @start_date1 AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date1
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_deluxe4, d, 3, @vendor_id1, @vendor_id1 FROM d OPTION (MAXRECURSION 0);

    ;WITH d AS (
        SELECT @start_date1 AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date1
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_premium4, d, 3, @vendor_id1, @vendor_id1 FROM d OPTION (MAXRECURSION 0);

    /* ==== 旅宿外觀圖片 (示範 1 張) ==== */
	 INSERT INTO lodging_images (
    image_type, lodging_id, room_type_id,
    image_url, mime_type, sort_order,
    is_active, delete_status
)
VALUES
('lodging', @lodging_id4, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/236886390.jpg?k=207b659edacda94ff4eb0b09926e23ea6a64b604f90a7dc3a0fd4489abc03ec8&o=', 'image/jpeg', 0, 1, 1),
('lodging', @lodging_id4, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/419909876.jpg?k=8747f24a8a36621e362269f50dadca81abb45a3bb33b32154e57e25e1cea7cb9&o=', 'image/jpeg', 1, 1, 1),
('lodging', @lodging_id4, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/419909879.jpg?k=cbd06ad2126af6afedb115eb60fc428f7b712566e0f26806306f109491d6ddae&o=', 'image/jpeg', 2, 1, 1),
('lodging', @lodging_id4, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/704716562.jpg?k=e53b366aeb191d24e0d5aee2690eb4bc60c5bc77d26e944b67fd9559efd8b083&o=', 'image/jpeg', 3, 1, 1),
('lodging', @lodging_id4, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/419909926.jpg?k=c11795d67722fb6c7d021af60bc0e7938f9d0790a738a94544614a5c91f6448e&o=', 'image/jpeg', 4, 1, 1);

    /* ======================================================
       ★ 自動產生 10 筆 4★ / 5★ 留言  ★
       - 偶數迴圈插 5 星，奇數 4 星
       - user_id 先固定為 4 (可改變)
    ====================================================== */
    DECLARE @i4 INT = 1;
    WHILE @i4 <= 10
    BEGIN
        INSERT INTO comments (lodging_id, user_id, content, rating, created_at)
        VALUES (
            @lodging_id4,
            4,
            N'#' + CAST(@i4 AS NVARCHAR) + N'：首爾大飯店超棒！',
            CASE WHEN @i4 % 2 = 0 THEN 5 ELSE 4 END,
            DATEADD(DAY, -@i4, GETDATE())  -- 時間向前推，較真實
        );
        SET @i4 += 1;
    END;

    PRINT N'首爾大飯店 已建立 (ID=' + CAST(@lodging_id4 AS NVARCHAR) + N')';
END
ELSE
    PRINT N'首爾大飯店 已存在，略過新增';

/* ===== 新增旅宿：南韓世界酒店 ===== */
IF NOT EXISTS (SELECT 1 FROM lodgings WHERE lodging_name = N'南韓世界酒店')
BEGIN
    DECLARE @lodging_id5        INT,
            @room_family_deluxe5  INT,
            @room_family_premium5 INT;

    INSERT INTO lodgings (
        vendor_id, lodging_name, lodging_type,
        [description], [address],
        city_id, country_id,
        latitude, longitude,
        lodging_tel, email,
        is_active, delete_status,
        created_at, updated_at, updated_by
    )
    VALUES (
        @vendor_id1, N'南韓世界酒店', 1,
        N'坐落於蠶室地鐵站之上，毗鄰世界最大的主題樂園。館內設有 3 家餐飲場所，提供免費 WiFi，並附設免費停車位。',
        N'東京都千代田區',
        @city_id1, @country_id1,
        34.7089, 135.5016,
        '06-1234-5678', 'osakabb@example.com',
        1, 1, GETDATE(), GETDATE(), @updated_by1
    );

    SET @lodging_id5 = SCOPE_IDENTITY();

    /* ==== 房型 ==== */
    INSERT INTO room_types (
        lodging_id, [name], [description],
        price_per_night, max_guests,
        created_by, updated_by
    )
    VALUES
    (@lodging_id5, N'豪華雙床房',
     N'時尚的現代化客房可欣賞市景，設有空調、37 吋平面電視、個人保險箱、茶／咖啡沖泡設備和私人衛浴，衛浴內附淋浴設施。住宿提供免費瓶裝水和 24 小時免費網路服務。',
     6000, 4, @vendor_id1, @vendor_id1),
    (@lodging_id5, N'豪華雙人房',
     N'這間雙人房提供空調。此住宿單位提供 1 張床。',
     3500, 2, @vendor_id1, @vendor_id1);

    SELECT @room_family_deluxe5  = id FROM room_types WHERE lodging_id = @lodging_id5 AND [name] = N'豪華雙床房';
    SELECT @room_family_premium5 = id FROM room_types WHERE lodging_id = @lodging_id5 AND [name] = N'豪華雙人房';

    /* ==== 房型圖片 (示範) ==== */
    INSERT INTO lodging_images (
        image_type, lodging_id, room_type_id,
        image_url, mime_type, sort_order,
        is_active, delete_status
    )
VALUES
-- 豪華雙床房
('room', @lodging_id5, @room_family_deluxe5, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/713025832.jpg?k=a07b0937e6c1cc90e1a31e51f259ce38b532157f34740e2640dd9a0e0832d1a6&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id5, @room_family_deluxe5, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/303470095.jpg?k=092286b04c8f19ea360668b9e10011b6a3c71fd098a6d319c57783b0ed4badbd&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id5, @room_family_deluxe5, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/303479033.jpg?k=377c96ccae52c329f8ad719d41948f8415eea9272e0967f8a9444169e95f9b9c&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id5, @room_family_deluxe5, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/301782064.jpg?k=3dec6035e78f3e1673cc9cb7f7fd4d05d628b9af9f9f9c6a6877dd216cbcb0cb&o=', 'image/jpeg', 3, 1, 1),
('room', @lodging_id5, @room_family_deluxe5, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/602112449.jpg?k=75392cf89eedfea35a64eb0f0bd0066090bb2a6dd14fc1d9122facfb69829179&o=', 'image/jpeg', 4, 1, 1),

-- 豪華雙人房
('room', @lodging_id5, @room_family_premium5, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/301779609.jpg?k=3c845c3a53e76aedbc8f3e65c710df3a65fabdc105a5702932f1eb154de655ca&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id5, @room_family_premium5, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/303470095.jpg?k=092286b04c8f19ea360668b9e10011b6a3c71fd098a6d319c57783b0ed4badbd&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id5, @room_family_premium5, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/303479033.jpg?k=377c96ccae52c329f8ad719d41948f8415eea9272e0967f8a9444169e95f9b9c&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id5, @room_family_premium5, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/301782064.jpg?k=3dec6035e78f3e1673cc9cb7f7fd4d05d628b9af9f9f9c6a6877dd216cbcb0cb&o=', 'image/jpeg', 3, 1, 1),
('room', @lodging_id5, @room_family_premium5, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/602112449.jpg?k=75392cf89eedfea35a64eb0f0bd0066090bb2a6dd14fc1d9122facfb69829179&o=', 'image/jpeg', 4, 1, 1);


    /* ==== 床型 ==== */
    INSERT INTO room_type_beds (room_type_id, bed_type_id, quantity) VALUES
    (@room_family_deluxe5,  1, 4),  -- 單人床 ×4
    (@room_family_premium5, 2, 1);  -- 雙人床 ×1

    /* ==== 設施：1=電視 4=免費wifi 9=接駁服務 === */
    INSERT INTO room_facilities (room_type_id, facility_id) VALUES
    (@room_family_deluxe5, 1), (@room_family_deluxe5, 4), (@room_family_deluxe5, 9), (@room_family_deluxe5, 11), (@room_family_deluxe5, 12),
    (@room_family_premium5, 1), (@room_family_premium5, 4), (@room_family_premium5, 9), (@room_family_premium5, 11), (@room_family_premium5, 12);

    /* ==== 房量 (每日 3 間) ==== */
    ;WITH d AS (
        SELECT @start_date1 AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date1
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_deluxe5, d, 3, @vendor_id1, @vendor_id1 FROM d OPTION (MAXRECURSION 0);

    ;WITH d AS (
        SELECT @start_date1 AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date1
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_premium5, d, 3, @vendor_id1, @vendor_id1 FROM d OPTION (MAXRECURSION 0);

    /* ==== 旅宿外觀圖片 (示範 1 張) ==== */
	 INSERT INTO lodging_images (
    image_type, lodging_id, room_type_id,
    image_url, mime_type, sort_order,
    is_active, delete_status
)
VALUES
('lodging', @lodging_id5, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/333969450.jpg?k=0b0781fa9a6800a200c37278af605780ccae77f85fe8f21cd5b873e48b38227d&o=', 'image/jpeg', 0, 1, 1),
('lodging', @lodging_id5, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/359479813.jpg?k=01d897a089ec50913155264fd436b32034614a43d646633ffa04cb64ffc8243a&o=', 'image/jpeg', 1, 1, 1),
('lodging', @lodging_id5, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/360102676.jpg?k=d32498d64c5b1313efa6c0dcf49b9848b497f255d3181612d95cc441243bf9d4&o=', 'image/jpeg', 2, 1, 1),
('lodging', @lodging_id5, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/301792375.jpg?k=528368d93d589ae45358e3d3abe403fa7c00ad667d51486113b7528f1fdb530c&o=', 'image/jpeg', 3, 1, 1),
('lodging', @lodging_id5, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/438891091.jpg?k=44a384e879060c6d5b65de072693d20e9738827830dfe91b7018b6215249b442&o=', 'image/jpeg', 4, 1, 1);

    /* ======================================================
       ★ 自動產生 10 筆 4★ / 5★ 留言  ★
       - 偶數迴圈插 5 星，奇數 4 星
       - user_id 先固定為 4 (可改變)
    ====================================================== */
    DECLARE @i5 INT = 1;
    WHILE @i5 <= 10
    BEGIN
        INSERT INTO comments (lodging_id, user_id, content, rating, created_at)
        VALUES (
            @lodging_id5,
            4,
            N'#' + CAST(@i5 AS NVARCHAR) + N'：南韓最強，住宿體驗超棒！',
            CASE WHEN @i5 % 2 = 0 THEN 5 ELSE 4 END,
            DATEADD(DAY, -@i5, GETDATE())  -- 時間向前推，較真實
        );
        SET @i5 += 1;
    END;

    PRINT N'南韓世界酒店 已建立 (ID=' + CAST(@lodging_id5 AS NVARCHAR) + N')';
END
ELSE
    PRINT N'南韓世界酒店 已存在，略過新增';

SET NOCOUNT ON;

/* ==== 基本變數 ==== */
DECLARE @vendor_id2     INT = 3;      -- 測試商家
DECLARE @city_id2       INT = 2;     -- 台北
DECLARE @country_id2    INT = 1;    -- 台灣
DECLARE @updated_by2    INT = 3;
DECLARE @start_date2    DATE = '2025-07-01';
DECLARE @end_date2      DATE = '2025-12-31';
/* ===== 新增旅宿：台北台北大飯店 ===== */
IF NOT EXISTS (SELECT 1 FROM lodgings WHERE lodging_name = N'台北台北大飯店')
BEGIN
    DECLARE @lodging_id6        INT,
            @room_family_deluxe6  INT,
            @room_family_premium6 INT;

    INSERT INTO lodgings (
        vendor_id, lodging_name, lodging_type,
        [description], [address],
        city_id, country_id,
        latitude, longitude,
        lodging_tel, email,
        is_active, delete_status,
        created_at, updated_at, updated_by
    )
    VALUES (
        @vendor_id2, N'台北台北大飯店', 1,
        N'位於台北，設有室外游泳池、健身中心、餐廳和酒吧。',
        N'台北市中正區',
        @city_id2, @country_id2,
        25.0422, 121.5198,
        '02-1111-4321', 'TaipeiTaipei@example.com',
        1, 1, GETDATE(), GETDATE(), @updated_by2
    );

    SET @lodging_id6 = SCOPE_IDENTITY();

    /* ==== 房型 ==== */
    INSERT INTO room_types (
        lodging_id, [name], [description],
        price_per_night, max_guests,
        created_by, updated_by
    )
    VALUES
    (@lodging_id6, N'高級雙床房',
     N'這間雙人房包含私人衛浴，內附浴缸、淋浴設施和沖洗座，並提供免費盥洗用品與浴袍。此雙人房空間寬敞，提供空調、平面電視、迷你吧、茶／咖啡沖泡設備和休息區，並享有市景。此住宿單位設有 2 張床。',
     2400, 2, @vendor_id2, @vendor_id2),
    (@lodging_id6, N'豪華加大雙人床房',
     N'這間雙人房包含私人衛浴，內附浴缸、淋浴設施和沖洗座，並提供免費盥洗用品與浴袍。此雙人房空間寬敞，提供空調、平面電視、迷你吧和茶／咖啡沖泡設備，享有市景，且入住可使用行政貴賓廳。此住宿單位設有 1 張床。',
     4000, 2, @vendor_id2, @vendor_id2);

    SELECT @room_family_deluxe6  = id FROM room_types WHERE lodging_id = @lodging_id6 AND [name] = N'高級雙床房';
    SELECT @room_family_premium6 = id FROM room_types WHERE lodging_id = @lodging_id6 AND [name] = N'豪華加大雙人床房';

    /* ==== 房型圖片 (示範) ==== */
    INSERT INTO lodging_images (
        image_type, lodging_id, room_type_id,
        image_url, mime_type, sort_order,
        is_active, delete_status
    )
VALUES
-- 高級雙床房
('room', @lodging_id6, @room_family_deluxe6, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/473064067.jpg?k=4290075646b691c2e4a5b8e96ea3cf03b49a919aa43c31f900cd7764fc85db2a&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id6, @room_family_deluxe6, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/519382472.jpg?k=00cdb873456f1ceddba6e12668b4243a6d207083d7527f1f0368b89cff89fff2&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id6, @room_family_deluxe6, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/519384264.jpg?k=2dff041e789a8f9b97872184dc0ba0dccb9b52718e1f10e2affba277bd127446&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id6, @room_family_deluxe6, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/519384258.jpg?k=8f4dba79222a0dd5746397b80b180efbbe990ca88823b19e992049f2b782815b&o=', 'image/jpeg', 3, 1, 1),
('room', @lodging_id6, @room_family_deluxe6, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/519384254.jpg?k=870796ba56aa76b5154cd474cfb71d3e2159fd4acb89192e40c435b28eba1e1e&o=', 'image/jpeg', 4, 1, 1),	    

-- 豪華加大雙人床房
('room', @lodging_id6, @room_family_premium6, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/519383331.jpg?k=5516515f76d5c7bb4862a4bc296a056fcd24cfd3c4ef9f2bb7056316fc282596&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id6, @room_family_premium6, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/519382472.jpg?k=00cdb873456f1ceddba6e12668b4243a6d207083d7527f1f0368b89cff89fff2&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id6, @room_family_premium6, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/519384264.jpg?k=2dff041e789a8f9b97872184dc0ba0dccb9b52718e1f10e2affba277bd127446&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id6, @room_family_premium6, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/519384258.jpg?k=8f4dba79222a0dd5746397b80b180efbbe990ca88823b19e992049f2b782815b&o=', 'image/jpeg', 3, 1, 1),	    
('room', @lodging_id6, @room_family_premium6, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/519384254.jpg?k=870796ba56aa76b5154cd474cfb71d3e2159fd4acb89192e40c435b28eba1e1e&o=', 'image/jpeg', 4, 1, 1);


    /* ==== 床型 ==== */
    INSERT INTO room_type_beds (room_type_id, bed_type_id, quantity) VALUES
    (@room_family_deluxe6,  2, 1),  -- 雙人床 ×1
    (@room_family_premium6, 2, 1);  -- 雙人床 ×1

    /* ==== 設施：1=電視 2=洗衣機 3=廚房 4=免費wifi 11=免費行李寄放 === */
    INSERT INTO room_facilities (room_type_id, facility_id) VALUES
    (@room_family_deluxe6, 1), (@room_family_deluxe6, 2), (@room_family_deluxe6, 4), (@room_family_deluxe6, 5),(@room_family_deluxe6, 6), (@room_family_deluxe6, 7), (@room_family_deluxe6, 8), (@room_family_deluxe6, 9),(@room_family_deluxe6, 10), (@room_family_deluxe6, 11), (@room_family_deluxe6, 12), (@room_family_deluxe6, 13),
    (@room_family_premium6, 1), (@room_family_premium6, 2), (@room_family_premium6, 4), (@room_family_premium6, 5),(@room_family_premium6, 6), (@room_family_premium6, 7), (@room_family_premium6, 8), (@room_family_premium6, 9),(@room_family_premium6, 10), (@room_family_premium6, 11), (@room_family_premium6, 12), (@room_family_premium6, 13);

    /* ==== 房量 (每日 3 間) ==== */
    ;WITH d AS (
        SELECT @start_date2 AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date2
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_deluxe6, d, 3, @vendor_id2, @vendor_id2 FROM d OPTION (MAXRECURSION 0);

    ;WITH d AS (
        SELECT @start_date2 AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date2
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_premium6, d, 3, @vendor_id2, @vendor_id2 FROM d OPTION (MAXRECURSION 0);

    /* ==== 旅宿外觀圖片 (示範 1 張) ==== */
	 INSERT INTO lodging_images (
    image_type, lodging_id, room_type_id,
    image_url, mime_type, sort_order,
    is_active, delete_status
)
VALUES
('lodging', @lodging_id6, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/651377246.jpg?k=b06eb3b024dee08e9d5703818ce179499c386c3629dfeea381a0228404e5b40e&o=', 'image/jpeg', 0, 1, 1),
('lodging', @lodging_id6, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/643761138.jpg?k=7ce539302954ad2b8683f2d5db09f3047d4a830eb049d6aefa45c7887d1b2780&o=', 'image/jpeg', 1, 1, 1),
('lodging', @lodging_id6, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/643513958.jpg?k=a460346a15239c2402ef8886a7a4dc39ffa8af25523b2ca79973e7a3b12eee9a&o=', 'image/jpeg', 2, 1, 1),
('lodging', @lodging_id6, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/643513960.jpg?k=f76d7dce1ba0b02f81250f4304d65e28885cd43610d1fe2c61957c236918ff31&o=', 'image/jpeg', 3, 1, 1),
('lodging', @lodging_id6, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/643513962.jpg?k=767533ad9095d499fec4a4f58e6206f1e9fcb34e136a9604259032d2edff3c7a&o=', 'image/jpeg', 4, 1, 1);

    /* ======================================================
       ★ 自動產生 10 筆 4★ / 5★ 留言  ★
       - 偶數迴圈插 5 星，奇數 4 星
       - user_id 先固定為 4 (可改變)
    ====================================================== */
    DECLARE @i6 INT = 1;
    WHILE @i6 <= 10
    BEGIN
        INSERT INTO comments (lodging_id, user_id, content, rating, created_at)
        VALUES (
            @lodging_id6,
            4,
            N'#' + CAST(@i6 AS NVARCHAR) + N'：住宿體驗超棒！',
            CASE WHEN @i6 % 2 = 0 THEN 5 ELSE 4 END,
            DATEADD(DAY, -@i6, GETDATE())  -- 時間向前推，較真實
        );
        SET @i6 += 1;
    END;

    PRINT N'台北台北大飯店 已建立 (ID=' + CAST(@lodging_id6 AS NVARCHAR) + N')';
END
ELSE
    PRINT N'台北台北大飯店 已存在，略過新增';

/* ===== 新增旅宿：台灣酒店 ===== */
IF NOT EXISTS (SELECT 1 FROM lodgings WHERE lodging_name = N'台灣酒店')
BEGIN
    DECLARE @lodging_id7        INT,
            @room_family_deluxe7  INT,
            @room_family_premium7 INT;

    INSERT INTO lodgings (
        vendor_id, lodging_name, lodging_type,
        [description], [address],
        city_id, country_id,
        latitude, longitude,
        lodging_tel, email,
        is_active, delete_status,
        created_at, updated_at, updated_by
    )
    VALUES (
        @vendor_id2, N'台灣酒店', 2,
        N'位於南港區，座落於捷運南港站、台灣高鐵南港站和南港火車站三鐵共構大樓的 7 至 30 樓。館內提供免費 WiFi 和私人停車位。',
        N'台北市南港區',
        @city_id2, @country_id2,
        25.0566, 121.6096,
        '02-8765-5678', '8765@example.com',
        1, 1, GETDATE(), GETDATE(), @updated_by2
    );

    SET @lodging_id7 = SCOPE_IDENTITY();

    /* ==== 房型 ==== */
    INSERT INTO room_types (
        lodging_id, [name], [description],
        price_per_night, max_guests,
        created_by, updated_by
    )
    VALUES
    (@lodging_id7, N'豪華雙床房',
     N'這間雙人房設有私人衛浴，內附免費盥洗用品、浴袍、浴缸、淋浴設施和沖洗座。寬敞的雙人房享有山景，並提供空調、平面電視（附串流服務）、隔音牆、迷你吧和茶／咖啡沖泡設備。此住宿空間設有 2 張床。',
     6000, 4, @vendor_id2, @vendor_id2),
    (@lodging_id7, N'豪華雙人房',
     N'飯店的豪華套房可欣賞令人屏息的山景和市景，讓住宿體驗更加美好。迷你吧、冰箱、衛星電視和愜意的休息區等便利設施帶來無與倫比的舒適享受。私人衛浴配備淋浴設施、幫助恢復活力的熱水按摩池、免費盥洗用品和吹風機。',
     3500, 2, @vendor_id2, @vendor_id2);

    SELECT @room_family_deluxe7  = id FROM room_types WHERE lodging_id = @lodging_id7 AND [name] = N'豪華雙床房';
    SELECT @room_family_premium7 = id FROM room_types WHERE lodging_id = @lodging_id7 AND [name] = N'豪華雙人房';

    /* ==== 房型圖片 (示範) ==== */
    INSERT INTO lodging_images (
        image_type, lodging_id, room_type_id,
        image_url, mime_type, sort_order,
        is_active, delete_status
    )
VALUES
-- 豪華雙床房
('room', @lodging_id7, @room_family_deluxe7, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/645241680.jpg?k=08d7c371aec978febe83aba9a222e30c5815b5ba6c420f4d79210348cd8ee662&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id7, @room_family_deluxe7, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/645241732.jpg?k=0b6250d8614759e9362b10cb558464df64685f16f4a465ae7bf186e94094b0e0&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id7, @room_family_deluxe7, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/303479033.jpg?k=377c96ccae52c329f8ad719d41948f8415eea9272e0967f8a9444169e95f9b9c&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id7, @room_family_deluxe7, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/301782064.jpg?k=3dec6035e78f3e1673cc9cb7f7fd4d05d628b9af9f9f9c6a6877dd216cbcb0cb&o=', 'image/jpeg', 3, 1, 1),
('room', @lodging_id7, @room_family_deluxe7, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/602112449.jpg?k=75392cf89eedfea35a64eb0f0bd0066090bb2a6dd14fc1d9122facfb69829179&o=', 'image/jpeg', 4, 1, 1),

-- 豪華雙人房
('room', @lodging_id7, @room_family_premium7, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/645241800.jpg?k=6a56094387f01a2f832f2becad329f70fd83febfea295c6b9d3ed1f15f4f7217&o=', 'image/jpeg', 0, 1, 1),
('room', @lodging_id7, @room_family_premium7, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/645241732.jpg?k=0b6250d8614759e9362b10cb558464df64685f16f4a465ae7bf186e94094b0e0&o=', 'image/jpeg', 1, 1, 1),
('room', @lodging_id7, @room_family_premium7, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/303479033.jpg?k=377c96ccae52c329f8ad719d41948f8415eea9272e0967f8a9444169e95f9b9c&o=', 'image/jpeg', 2, 1, 1),
('room', @lodging_id7, @room_family_premium7, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/301782064.jpg?k=3dec6035e78f3e1673cc9cb7f7fd4d05d628b9af9f9f9c6a6877dd216cbcb0cb&o=', 'image/jpeg', 3, 1, 1),
('room', @lodging_id7, @room_family_premium7, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/602112449.jpg?k=75392cf89eedfea35a64eb0f0bd0066090bb2a6dd14fc1d9122facfb69829179&o=', 'image/jpeg', 4, 1, 1);


    /* ==== 床型 ==== */
    INSERT INTO room_type_beds (room_type_id, bed_type_id, quantity) VALUES
    (@room_family_deluxe7,  1, 4),  -- 單人床 ×4
    (@room_family_premium7, 2, 1);  -- 雙人床 ×1

    /* ==== 設施：1=電視 4=免費wifi 9=接駁服務 === */
    INSERT INTO room_facilities (room_type_id, facility_id) VALUES
    (@room_family_deluxe7, 1), (@room_family_deluxe7, 4), (@room_family_deluxe7, 9), (@room_family_deluxe7, 11), (@room_family_deluxe7, 12),
    (@room_family_premium7, 1), (@room_family_premium7, 4), (@room_family_premium7, 9), (@room_family_premium7, 11), (@room_family_premium7, 12);

    /* ==== 房量 (每日 3 間) ==== */
    ;WITH d AS (
        SELECT @start_date2 AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date2
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_deluxe7, d, 3, @vendor_id2, @vendor_id2 FROM d OPTION (MAXRECURSION 0);

    ;WITH d AS (
        SELECT @start_date2 AS d UNION ALL
        SELECT DATEADD(DAY,1,d) FROM d WHERE d < @end_date2
    )
    INSERT INTO room_availability (room_type_id, [date], available_rooms, created_by, updated_by)
    SELECT  @room_family_premium7, d, 3, @vendor_id2, @vendor_id2 FROM d OPTION (MAXRECURSION 0);

    /* ==== 旅宿外觀圖片 (示範 1 張) ==== */
	 INSERT INTO lodging_images (
    image_type, lodging_id, room_type_id,
    image_url, mime_type, sort_order,
    is_active, delete_status
)
VALUES
('lodging', @lodging_id7, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/645241828.jpg?k=1a1c10c80e453c9dec24ea3e9385ab9fae117957799dd2be32a5849c74d4e6cc&o=', 'image/jpeg', 0, 1, 1),
('lodging', @lodging_id7, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/645241574.jpg?k=259f97a62d6c787e23e2eb60470a3234267a3bd41c5bb9c77434c3b59ab0fb45&o=', 'image/jpeg', 1, 1, 1),
('lodging', @lodging_id7, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/110320475.jpg?k=36fa2512a9da078974ce555c3030b6b94fd8afc2608cd8876cb5a7e1b58ae134&o=', 'image/jpeg', 2, 1, 1),
('lodging', @lodging_id7, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/645241834.jpg?k=f623f4bfe1a544ed95b0758958d4d8a32c3f1624302c0373fd5b10ce6f2e61f0&o=', 'image/jpeg', 3, 1, 1),
('lodging', @lodging_id7, NULL, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/110320831.jpg?k=92d83f8a562faf83223f9d62c551574303fe3edb4b82cb58b5a33859b5471d62&o=', 'image/jpeg', 4, 1, 1);

    /* ======================================================
       ★ 自動產生 10 筆 4★ / 5★ 留言  ★
       - 偶數迴圈插 5 星，奇數 4 星
       - user_id 先固定為 4 (可改變)
    ====================================================== */
    DECLARE @i7 INT = 1;
    WHILE @i7 <= 10
    BEGIN
        INSERT INTO comments (lodging_id, user_id, content, rating, created_at)
        VALUES (
            @lodging_id7,
            4,
            N'#' + CAST(@i7 AS NVARCHAR) + N'：東京最強，住宿體驗超棒！',
            CASE WHEN @i7 % 2 = 0 THEN 5 ELSE 4 END,
            DATEADD(DAY, -@i7, GETDATE())  -- 時間向前推，較真實
        );
        SET @i7 += 1;
    END;

    PRINT N'台灣酒店 已建立 (ID=' + CAST(@lodging_id7 AS NVARCHAR) + N')';
END
ELSE
    PRINT N'台灣酒店 已存在，略過新增';

-- 留言 + 圖片資料初始化區塊
PRINT '開始插入留言與留言圖片資料';
GO

IF NOT EXISTS (
    SELECT 1 FROM comments
    WHERE lodging_id = 3 AND user_id = 6 AND content = N'這間旅館超棒，服務人員很親切！'
)
BEGIN
DECLARE @comment_id1 INT;
DECLARE @comment_id2 INT;

INSERT INTO comments (
    lodging_id, room_type_id, traffic_ticket_id, attraction_ticket_id, trip_plan_id,
    user_id, content, parent_comment_id, rating, is_verified, is_active,
    deleted_time, delete_status, created_at, updated_at
)
VALUES (
    3, NULL, NULL, NULL, NULL,
    6, N'這間旅館超棒，服務人員很親切！', NULL, 5, 1, 1,
    NULL, 1, GETDATE(), GETDATE()
);
SET @comment_id1 = SCOPE_IDENTITY();

INSERT INTO comment_images (comment_id, image_url, mime_type, sort_order)
VALUES
(@comment_id1, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/582507529.jpg?k=d2eafc2a08c7d0e1e180ee1e2834c4fd531c2ce44a4c57ba83717592d2e458da&o=', 'image/jpeg', 0),
(@comment_id1, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/582507527.jpg?k=d0ffc2658de598b079c59101ca583dd9e02b8dcae9162f3fe42be8db1dc2ef17&o=', 'image/png', 1),
(@comment_id1, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/582507525.jpg?k=62f77477ea79817bfd69e47d591bd68597f302b1f68d3d78f22e8fa4d04975a0&o=', 'image/jpeg', 2);

-- 第1則留言被使用者 3,4 按讚
INSERT INTO comment_feedbacks (comment_id, [user_id], feedback)
VALUES
(@comment_id1, 3, 1),
(@comment_id1, 4, 1);

INSERT INTO comments (
    lodging_id, room_type_id, traffic_ticket_id, attraction_ticket_id, trip_plan_id,
    user_id, content, parent_comment_id, rating, is_verified, is_active,
    deleted_time, delete_status, created_at, updated_at
)
VALUES (
    3, NULL, NULL, NULL, NULL,
    3, N'星星大飯店也是不錯選擇!!推薦你們~', @comment_id1, NULL, 0, 1,
    NULL, 1, GETDATE(), GETDATE()
);

SET @comment_id2 = SCOPE_IDENTITY();

INSERT INTO comment_images (comment_id, image_url, mime_type, sort_order)
VALUES
(@comment_id2, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/584495611.jpg?k=e5b17b028cf073b493c10521a1f9efa0d59bde719f4749e3426d5fcf675ed78b&o=', 'image/jpeg', 0),
(@comment_id2, N'https://cf.bstatic.com/xdata/images/hotel/max1024x768/523292298.jpg?k=4612a752e9c4d6a7de144e994248020aec1d0607506e06ddd2fb05c689f4bad0&o=', 'image/png', 1);

-- 第2則留言被使用者 3,4 按讚
INSERT INTO comment_feedbacks (comment_id, [user_id], feedback)
VALUES
(@comment_id2, 3, 1);
	
    PRINT N'留言與圖片初始化完成';
END
ELSE
BEGIN
    PRINT N'已有留言存在，略過插入';
END


--=======================景點票商品資料------------------------------


--------------------晴空塔(日本東京)-------------------
--DELETE FROM dbo.attraction_tickets WHERE id = 1010;
INSERT INTO attraction_tickets (
    name, description, address, image_url, image_type,
    views, state, start_time, end_time,
    created_at, created_by, city, country_id
) VALUES (
    N'東京晴空塔門票',
    N'在KKday訂購東京晴空塔門票比官網價格還便宜。 東京晴空塔又名東京天空樹，是世界第二高塔。 東京晴空塔是日本著名地標，白天俯瞰東京街景、晚上欣賞浪漫夜景。',
    N'日本-關東 , 東京 , 東京晴空塔',
    'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_38352/20200306091143_LNpF4/jpg',
    'jpg',
    45, 1,
    '2025-07-01T18:30:00', '2025-08-31T22:00:00',
    '2025-06-30T15:00:00', 3,35, 2
);
DECLARE @ticketId INT;
SET @ticketId = SCOPE_IDENTITY();

-- 插入 tag 關聯
INSERT INTO dbo.attraction_tickets_middle_attraction_tags (ticket_id, tag_id) VALUES
(@ticketId, 1),
(@ticketId, 2);

-- 插入 type 關聯
INSERT INTO dbo.attraction_tickets_middle_types (ticket_id, attraction_type_id) VALUES
(@ticketId, 2);
---插入圖片
INSERT INTO dbo.ticket_images (ticket_id, image_url, image_type, sort_order) VALUES
(@ticketId, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_38352/20221025114451_jzYG9/jpg', 'jpg', 1),
(@ticketId, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_38352/20221025114452_X0r9w/jpg', 'jpg', 2),
(@ticketId, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_38352/20200306091111_2fEoT/jpg', 'jpg', 3),
(@ticketId, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_38352/20230614101500_evNQx/jpg', 'jpg', 4);
--插入內容
INSERT INTO dbo.ticket_contents (ticket_id, title, subtitle, description) VALUES
(@ticketId, N'商品說明', NULL, N'由專業星空老師帶領，提供望遠鏡觀測與解說。'),
(@ticketId, N'購買須知', NULL, N'從埔里出發，含上下山接駁服務與保險。'),
(@ticketId, N'如何使用', NULL, N'從埔里出發，含上下山接駁服務與保險。'),
(@ticketId, N'購買體驗地點須知', NULL, N'從埔里出發，含上下山接駁服務與保險。');
--套票1
INSERT INTO dbo.ticket_packages (
    ticket_id, name, description, image_url, image_type,
    start_time, end_time, created_by, state,created_at
) VALUES (
    @ticketId,
    N'預售票｜ 天望甲板(350m) ・ 天望回廊(450m) 入場券套票',
    N'出發前請於晴空塔官網事先確認當天營業狀況，如有臨時異動恕不另行通知。僅訂購天望甲板（350m），欲購買天望回廊（450m）的旅客，可於 350m 展望台內的櫃檯購買票券。',
    'https://image.kkday.com/v2/image/get/c_fill%2Ch_300%2Cq_55%2Ct_webp/s1.kkday.com/product_38352/20230614102432_ziF1p/jpg',
    'jpg',
    '2025-07-01T18:00:00', '2025-08-31T22:00:00',
    1, 1,'2025-08-31T22:00:00'
);
DECLARE @packageId1 INT = SCOPE_IDENTITY();

INSERT INTO dbo.ticket_types (package_id, ticket_name, price, quantity, date) VALUES
(@packageId1, N'一般票', 421, 30, '2025-07-01'),
(@packageId1, N'兒童票', 280, 20, '2025-07-01');
--套票2
INSERT INTO dbo.ticket_packages (
    ticket_id, name, description, image_url, image_type,
    start_time, end_time, created_by, state,created_at
) VALUES (
    @ticketId,
    N'東京晴空塔｜早安/夜景門票',
    N'早安套票」包含進入東京晴空塔天望甲板 (350m) 的門票，以及可在 SKYTREE CAFE 350 享用的一杯無酒精飲料和一份小吃。您可一邊品嚐美味餐點，一邊從 350 m 高的觀景台上欣賞東京早晨的全景。早安套票請選擇下列輕食之一：① 起司貝果（小麥、牛奶）② 糖霜甜甜圈（蛋、牛奶、小麥）③ 星星銅鑼燒（蛋、牛奶、小麥）',
    'https://image.kkday.com/v2/image/get/c_fill%2Ch_300%2Cq_55%2Ct_webp/s1.kkday.com/product_38352/20221025114451_jzYG9/jpg',
    'jpg',
    '2025-07-01T18:00:00', '2025-08-31T22:00:00',
    1, 1,'2025-07-01T18:00:00'
);
DECLARE @packageId2 INT = SCOPE_IDENTITY();

INSERT INTO dbo.ticket_types (package_id, ticket_name, price, quantity, date) VALUES
(@packageId2, N'早安一般票', 561, 40, '2025-07-01'),
(@packageId2, N'夜景一般票', 541, 40, '2025-07-01');
--===========================================================================
-------澀谷SHIBUYA SKY展望台門票｜即買即用(日本-東京)------------------------
INSERT INTO attraction_tickets (
    name, description, address, image_url, image_type,
    views, state, start_time, end_time,
    created_at, created_by, city, country_id
) VALUES (
    N'澀谷SHIBUYA SKY展望台門票｜即買即用',
    N'東京澀谷人氣景點「SHIBUYA SKY 展望台」，讓你享受從澀谷sky俯瞰東京美景的浪漫',
    N'日本-關東 , 東京 , 澀谷',
    'https://image.kkday.com/v2/image/get/c_fill%2Ch_600%2Cq_55%2Ct_webp/s1.kkday.com/product_133300/20220915065553_dvR7F/jpg',
    'jpg',
    46, 1,
    '2025-07-01T18:30:00', '2025-08-31T22:00:00',
    '2025-06-30T15:00:00', 3, 35,2
);
DECLARE @ticketId1 INT;
SET @ticketId1 = SCOPE_IDENTITY();

-- 插入 tag 關聯
INSERT INTO dbo.attraction_tickets_middle_attraction_tags (ticket_id, tag_id) VALUES
(@ticketId1, 1),
(@ticketId1, 9);

-- 插入 type 關聯
INSERT INTO dbo.attraction_tickets_middle_types (ticket_id, attraction_type_id) VALUES
(@ticketId1, 2);
---插入圖片
INSERT INTO dbo.ticket_images (ticket_id, image_url, image_type, sort_order) VALUES
(@ticketId1, 'https://image.kkday.com/v2/image/get/c_fill%2Ch_300%2Cq_55%2Ct_webp/s1.kkday.com/product_133300/20220915065552_qbjbV/jpg', 'jpg', 1),
(@ticketId1, 'https://image.kkday.com/v2/image/get/c_fill%2Ch_300%2Cq_55%2Ct_webp/s1.kkday.com/product_133300/20220915065552_O6JcT/jpg', 'jpg', 2),
(@ticketId1, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_133300/20220923072327_PlGvK/jpg', 'jpg', 3),
(@ticketId1, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_133300/20220915065554_via3M/jpg', 'jpg', 4);
--插入內容
INSERT INTO dbo.ticket_contents (ticket_id, title, subtitle, description) VALUES
(@ticketId1, N'商品說明', NULL, N'SHIBUYA SKY 展望台位於東京澀谷的複合式商業設施「SHIBUYA SCRAMBLE SQUARE」中，可搭電梯直達14樓展望台入口。設施內分為三個區域，分別是從售票處14樓搭乘電梯上升到45樓的移動空間「SKY GATE」、45樓到46樓的室內展望迴廊「SKY GALLERY」以及46樓的室外展望露台「SKY STAGE」。設施中的最大亮點「SKY STAGE」展望露台，能夠360度無死角俯瞰東京都心美景，搭配全透明玻璃的設計，讓您登上露台後能體驗人空一體的漂浮感，是最具人氣的打卡景點！從澀谷上空229公尺高的展望露台，不論是東京鐵塔，東京晴空塔，甚至是富士山等都能盡收眼底，且白天夜晚皆有不同面貌，值得一行。。'),
(@ticketId1, N'購買須知', NULL, N'5歲以下（含6歲的學齡前兒童）可免費入場。'),
(@ticketId1, N'如何使用', NULL, N'從埔里出發，含上下山接駁服務與保險。'),
(@ticketId1, N'購買體驗地點須知', NULL, N'從埔里出發，含上下山接駁服務與保險。');
--套票1
INSERT INTO dbo.ticket_packages (
    ticket_id, name, description, image_url, image_type,
    start_time, end_time, created_by, state,created_at
) VALUES (
    @ticketId1,
    N'SHIBUYA SKY 展望台門票',
    N'逾時者將可能被拒絕入場。
本人同意頂樓及46樓室外可能因下雨，打雷，強風等惡劣天候，或因高溫的防中暑措施等理由不開放入場。
本人已確認入場時的注意事項：禁止攜帶規定物品到屋頂、禁止攜帶規定物品（包括行李箱等大件行李）進入設施。
本人同意入場後無法退票，並同意取消條款。
本人保證不轉賣、加工或複製門票。
本人保證不進入禁止進入區域。
本人保證遵守本設施入場時的注意事項及利用時的各種規則。
本人保證遵守工作人員、警衛的指示。
所有同行者已理解並同意上述事項後入場。',
    'https://image.kkday.com/v2/image/get/c_fill%2Ch_600%2Cq_55%2Ct_webp/s1.kkday.com/product_133300/20220915065553_dvR7F/jpg',
    'jpg',
    '2025-07-01T18:00:00', '2025-08-31T22:00:00',
    1, 1,'2025-08-31T22:00:00'
);
DECLARE @packageId INT = SCOPE_IDENTITY();

INSERT INTO dbo.ticket_types (package_id, ticket_name, price, quantity, date) VALUES
(@packageId, N'一般票', 541, 30, '2025-07-01')
--3====================================================================================
-------------------東京豐洲新型態美術館門票 teamLab Planets TOKYO---------------------
INSERT INTO attraction_tickets (
    name, description, address, image_url, image_type,
    views, state, start_time, end_time,
    created_at, created_by, city, country_id
) VALUES (
    N'東京豐洲新型態美術館門票 teamLab Planets TOKYO',
    N'東京澀谷人氣景點「SHIBUYA SKY 展望台」，讓你享受從澀谷sky俯瞰東京美景的浪漫東京豐洲teamLab Planets TOKYO全新展區——運動森林 2025 年盛大開幕。',
    N'日本-關東 , 東京 , 豐洲 , 東京晴空塔',
    'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_22396/20210630091546_4V2Zz/jpg',
    'jpg',
    48, 1,
    '2025-07-01T18:30:00', '2025-08-31T22:00:00',
    '2025-06-30T15:00:00', 3, 35,2
);
DECLARE @ticketId2 INT;
SET @ticketId2 = SCOPE_IDENTITY();

-- 插入 tag 關聯
INSERT INTO dbo.attraction_tickets_middle_attraction_tags (ticket_id, tag_id) VALUES
(@ticketId2, 1),
(@ticketId2, 3);

-- 插入 type 關聯
INSERT INTO dbo.attraction_tickets_middle_types (ticket_id, attraction_type_id) VALUES
(@ticketId2, 6);
---插入圖片
INSERT INTO dbo.ticket_images (ticket_id, image_url, image_type, sort_order) VALUES
(@ticketId2, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_22396/20241121083535_dVmRT/jpg', 'jpg', 1),
(@ticketId2, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_22396/20241121085959_l8ahz/png', 'jpg', 2),
(@ticketId2, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_22396/20250304092206_mmkdh/jpg', 'jpg', 3),
(@ticketId2, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_22396/20241121083530_L5QR0/jpg', 'jpg', 4);
--插入內容
INSERT INTO dbo.ticket_contents (ticket_id, title, subtitle, description) VALUES
(@ticketId2, N'商品說明', NULL, N'SHIBUYA SKY 展望台位於東京澀谷的複合式商業設施「SHIBUYA SCRAMBLE SQUARE」中，可搭電梯直達14樓展望台入口。設施內分為三個區域，分別是從售票處14樓搭乘電梯上升到45樓的移動空間「SKY GATE」、45樓到46樓的室內展望迴廊「SKY GALLERY」以及46樓的室外展望露台「SKY STAGE」。設施中的最大亮點「SKY STAGE」展望露台，能夠360度無死角俯瞰東京都心美景，搭配全透明玻璃的設計，讓您登上露台後能體驗人空一體的漂浮感，是最具人氣的打卡景點！從澀谷上空229公尺高的展望露台，不論是東京鐵塔，東京晴空塔，甚至是富士山等都能盡收眼底，且白天夜晚皆有不同面貌，值得一行。。'),
(@ticketId2, N'購買須知', NULL, N'5歲以下（含6歲的學齡前兒童）可免費入場。'),
(@ticketId2, N'如何使用', NULL, N'從埔里出發，含上下山接駁服務與保險。'),
(@ticketId2, N'購買體驗地點須知', NULL, N'從埔里出發，含上下山接駁服務與保險。');
--套票1
INSERT INTO dbo.ticket_packages (
    ticket_id, name, description, image_url, image_type,
    start_time, end_time, created_by, state,created_at
) VALUES (
    @ticketId2,
    N'【優先入場免排隊】teamLab Planets TOKYO 門票高級通行證',
    N'本票券為可直接快速進入東京・豐洲teamLab Planets TOKYO DMM（東京豐洲）參觀，無需在入口處排隊的高級通行證。',
    'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_22396/20241121090339_WDzYp/png',
    'jpg',
    '2025-07-01T18:00:00', '2025-08-31T22:00:00',
    1, 1,'2025-08-31T22:00:00'
);
DECLARE @packageId3 INT = SCOPE_IDENTITY();

INSERT INTO dbo.ticket_types (package_id, ticket_name, price, quantity, date) VALUES
(@packageId3, N'一般票', 2401, 30, '2025-07-01')

INSERT INTO dbo.ticket_packages (
    ticket_id, name, description, image_url, image_type,
    start_time, end_time, created_by, state,created_at
) VALUES (
    @ticketId2,
    N'teamLab Planets TOKYO 門票',
    N'購買「國中生・高中生（青少年）」票入場時請出示學生證及身分證。',
    'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_22396/20241121085959_l8ahz/png',
    'jpg',
    '2025-07-01T18:00:00', '2025-08-31T22:00:00',
    1, 1,'2025-08-31T22:00:00'
);
DECLARE @packageId4 INT = SCOPE_IDENTITY();

INSERT INTO dbo.ticket_types (package_id, ticket_name, price, quantity, date) VALUES
(@packageId4, N'成人', 761, 30, '2025-07-01'),
(@packageId4, N'青少年', 561, 30, '2025-07-01'),
(@packageId4, N'兒童', 300, 30, '2025-07-01')
--4==========================================================================================
--------------------------東京哈利波特影城門票----------------------------------------------
INSERT INTO attraction_tickets (
    name, description, address, image_url, image_type,
    views, state, start_time, end_time,
    created_at, created_by, city, country_id
) VALUES (
    N'東京哈利波特影城門票',
    N'東京哈利波特影城是全球最大型《哈利波特》主題室內設施。 ',
    N'〒179 - 0074 東京都練馬區春日町 1 - 1 - 7 Warner Bros. Studio Tour Tokyo',
    'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_144383/20230802085219_igCRN/jpg',
    'jpg',
    44, 1,
    '2025-07-01T18:30:00', '2025-08-31T22:00:00',
    '2025-06-30T15:00:00', 3, 35,2
);
DECLARE @ticketId3 INT;
SET @ticketId3 = SCOPE_IDENTITY();

-- 插入 tag 關聯
INSERT INTO dbo.attraction_tickets_middle_attraction_tags (ticket_id, tag_id) VALUES
(@ticketId3, 2),
(@ticketId3, 3);

-- 插入 type 關聯
INSERT INTO dbo.attraction_tickets_middle_types (ticket_id, attraction_type_id) VALUES
(@ticketId3, 3);
---插入圖片
INSERT INTO dbo.ticket_images (ticket_id, image_url, image_type, sort_order) VALUES
(@ticketId3, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_144383/20230524011755_lvShO/jpg', 'jpg', 1),
(@ticketId3, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_144383/20230524023109_5jv1G/jpg', 'jpg', 2),
(@ticketId3, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_144383/20230524022840_6NUeS/jpg', 'jpg', 3),
(@ticketId3, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_144383/20230524011758_GysEc/jpg', 'jpg', 4);
--插入內容
INSERT INTO dbo.ticket_contents (ticket_id, title, subtitle, description) VALUES
(@ticketId3, N'商品說明', NULL, N'SHIBUYA SKY 展望台位於東京澀谷的複合式商業設施「SHIBUYA SCRAMBLE SQUARE」中，可搭電梯直達14樓展望台入口。設施內分為三個區域，分別是從售票處14樓搭乘電梯上升到45樓的移動空間「SKY GATE」、45樓到46樓的室內展望迴廊「SKY GALLERY」以及46樓的室外展望露台「SKY STAGE」。設施中的最大亮點「SKY STAGE」展望露台，能夠360度無死角俯瞰東京都心美景，搭配全透明玻璃的設計，讓您登上露台後能體驗人空一體的漂浮感，是最具人氣的打卡景點！從澀谷上空229公尺高的展望露台，不論是東京鐵塔，東京晴空塔，甚至是富士山等都能盡收眼底，且白天夜晚皆有不同面貌，值得一行。。'),
(@ticketId3, N'購買須知', NULL, N'5歲以下（含6歲的學齡前兒童）可免費入場。'),
(@ticketId3, N'如何使用', NULL, N'從埔里出發，含上下山接駁服務與保險。'),
(@ticketId3, N'購買體驗地點須知', NULL, N'從埔里出發，含上下山接駁服務與保險。');
--套票1
INSERT INTO dbo.ticket_packages (
    ticket_id, name, description, image_url, image_type,
    start_time, end_time, created_by, state,created_at
) VALUES (
    @ticketId3,
    N'東京華納兄弟哈利波特影城門票 + 百貨公司免稅優惠券（西武/崇光百貨）',
    N'每張憑證都有不同的效期。 憑證的包含內容可能會更動，請留意',
    'https://image.kkday.com/v2/image/get/c_fill%2Ch_600%2Cq_55%2Ct_webp/s1.kkday.com/product_144383/20230802085219_igCRN/jpg',
    'jpg',
    '2025-07-01T18:00:00', '2025-08-31T22:00:00',
    1, 1,'2025-08-31T22:00:00'
);
DECLARE @packageId5 INT = SCOPE_IDENTITY();

INSERT INTO dbo.ticket_types (package_id, ticket_name, price, quantity, date) VALUES
(@packageId5, N'成人', 1394, 30, '2025-07-01'),
(@packageId5, N'青少年', 1100, 30, '2025-07-01'),
(@packageId5, N'小學生', 800, 30, '2025-07-01'),
(@packageId5, N'幼童', 300, 30, '2025-07-01')
--5======================================================================================
-----------東京迪士尼度假區｜東京迪士尼樂園 & 東京迪士尼海洋門票------------------------
INSERT INTO attraction_tickets (
    name, description, address, image_url, image_type,
    views, state, start_time, end_time,
    created_at, created_by, city, country_id
) VALUES (
    N'東京迪士尼度假區｜東京迪士尼樂園 & 東京迪士尼海洋門票',
    N'購買東京迪士尼度假區一日護照，保證入園輕鬆暢遊東京迪士尼度假區的夢幻世界 ',
    N'日本-關東 , 千葉 , 東京 , 浦安 , 東京迪士尼度假區 , 東京迪士尼樂園',
    'https://image.kkday.com/v2/image/get/c_fill%2Ch_600%2Cq_55%2Ct_webp/s1.kkday.com/product_32488/20220706120224_fLhdh/jpg',
    'jpg',
    48, 1,
    '2025-07-01T18:30:00', '2025-08-31T22:00:00',
    '2025-06-30T15:00:00', 3, 35,2
);
DECLARE @ticketId4 INT;
SET @ticketId4 = SCOPE_IDENTITY();

-- 插入 tag 關聯
INSERT INTO dbo.attraction_tickets_middle_attraction_tags (ticket_id, tag_id) VALUES
(@ticketId4, 9),
(@ticketId4, 1);

-- 插入 type 關聯
INSERT INTO dbo.attraction_tickets_middle_types (ticket_id, attraction_type_id) VALUES
(@ticketId4, 4);
---插入圖片
INSERT INTO dbo.ticket_images (ticket_id, image_url, image_type, sort_order) VALUES
(@ticketId4, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20210311083908_DPiSx/jpg', 'jpg', 1),
(@ticketId4, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20210311083904_QjfAr/jpg', 'jpg', 2),
(@ticketId4, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20250519023916_vm7LG/jpg', 'jpg', 3),
(@ticketId4, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20220706124018_AcD2V/jpg', 'jpg', 4);
--插入內容
INSERT INTO dbo.ticket_contents (ticket_id, title, subtitle, description) VALUES
(@ticketId4, N'商品說明', NULL, N'東京迪士尼度假區介紹
東京迪士尼度假區是ㄧ座擁有多家設施的大型主題度假區。包括「東京迪士尼樂園」、「東京迪士尼海洋」這兩座風格各異的迪士尼主題樂園、三家迪士尼飯店、多家官方飯店、ㄧ座綜合型商業設施「伊克斯皮兒莉」以及環繞度假區運行的便捷單軌電車等。不僅能為廣大遊客提供暢遊園區的歡樂體驗，同時透過住宿服務讓遊客得到購物、美食等多元化的度假體驗。
－ 設施介紹 －
東京迪士尼度假區介紹
東京迪士尼度假區是ㄧ座擁有多家設施的大型主題度假區。包括「東京迪士尼樂園」、「東京迪士尼海洋」這兩座風格各異的迪士尼主題樂園、三家迪士尼飯店、多家官方飯店、ㄧ座綜合型商業設施「伊克斯皮兒莉」以及環繞度假區運行的便捷單軌電車等。不僅能為廣大遊客提供暢遊園區的歡樂體驗，同時透過住宿服務讓遊客得到購物、美食等多元化的度假體驗。
東京迪士尼樂園
東京迪士尼樂園包含七個主題區：
・世界市集：大街兩旁維多利亞時代的建築鱗萃比櫛，優美典雅。20 世紀初期，華特・迪士尼出生並成長的美國將以溫馨氛圍迎接您的到來。
・明日樂園：在這座率先實現人類夢想的城市，您可體驗遨遊宇宙之旅。
・卡通城：米奇和他的夥伴們生活在這個快樂無比的小城。
・夢幻樂園：夢想成真的童話王國。有新開幕的美女與野獸及白雪公主、小飛俠和小熊維尼等耳熟能詳的童話世界主人公帶您踏上充滿魔法的冒險之旅。
・動物天地：美國河畔的紅土山，周邊是小動物們棲息的家園。
・西部樂園：完美重現大西部拓荒時代和昔日美國風情。拓荒者們的夢想和傳奇由此展開！
・探險樂園：這裡是冒險和浪漫的世界。叢林深處有兇猛野獸、黑暗海上有加勒比海盜，他們正等待著與您在驚險中相遇！
在驚喜不斷的七個主題園區，您可以乘坐遊樂設施、觀賞娛樂表演，還可以與您最喜愛的迪士尼明星合影，無盡的歡樂正等待著您！
東京迪士尼海洋
以各種與海洋相關的故事、傳說為靈感而誕生的主題樂園東京迪士尼海洋。在七大主題海港中，有各式充滿冒險與想像的遊樂設施和娛樂表演，讓人流連忘返、歡樂無窮！
・失落河三角洲：探索中美洲加勒比海沿岸神祕莫測、危機四伏的神殿，揭祕悠遠古老的叢林傳說。
・發現港：超越時空的未來港口
・美國海濱：20世紀初期的紐約港和鱈魚岬在此重現，風情各異、充滿懷舊氣息。
・阿拉伯海岸： 一起來體驗充滿神祕與魔法的《天方夜譚》的世界吧！
・美人魚礁湖：小美人魚和夥伴們快樂的海底王國。
・神祕島：活火山深處、充滿驚奇的祕密基地。通往冒險之旅的大門就隠藏其中，神祕的天才科學家尼莫船長將帶您踏上一段段旅程。
・地中海港灣：浪漫古老的南歐海港小鎮。溫馨質樸的餐廳鱗次櫛比，漁村風景美麗如畫。運河兩岸還有多家餐廳和商店，各具魅力。'),
(@ticketId4, N'設施介紹', NULL, N'左圖：東京迪士尼樂園中以迪士尼電影「美女與野獸」為主題的區域「夢幻樂園」。右圖：在大型遊樂設施「美女與野獸『城堡奇緣』」中，遊客能盡情體驗迪士尼電影「美女與野獸」世界的魅力風采。在野獸所居住的城堡中，搭上隨著電影經典配樂搖擺、旋轉的魔法茶杯，重溫貝兒與野獸之間真愛的浪漫時刻。
左圖：米妮時尚工作室：服裝設計師米妮專屬的工作室歡迎您大駕光臨！這裡有米妮的辦公室、作業房等繽紛空間，展現她於時尚界積極活躍的一面。在您參觀之後，還可與身穿亮麗服飾的米妮拍照留念呢！右圖：巨雷山：在淘金熱過後數十年，這座曾經人聲鼎沸的礦山，僅剩無人駕駛的採礦列車在坑道中瘋狂奔馳。由眼前呼嘯而過的褐色山壁、左右傾斜又急速下降的陡坡，一連串驚險無比的高速體驗將令您不禁放聲尖叫！
'),
(@ticketId4, N'如何使用', NULL, N'憑證使用方式:現場請出示 QR code。
使用期限:需要按照預訂日期及當天開放時間內使用，逾期失效');
--套票1
INSERT INTO dbo.ticket_packages (
    ticket_id, name, description, image_url, image_type,
    start_time, end_time, created_by, state,created_at
) VALUES (
    @ticketId4,
    N'東京迪士尼度假區一日護照',
    N'一日護照：指定日期票券、保證入園。價格：園區票券採浮動價格制。請遊客先確認各日期的票券價格，再購買指定日期票券。 ※3 歲以下的孩童可免費入園。',
    'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20210311083900_nd0m7/jpg',
    'jpg',
    '2025-07-01T18:00:00', '2025-08-31T22:00:00',
    1, 1,'2025-08-31T22:00:00'
);
DECLARE @packageId6 INT = SCOPE_IDENTITY();

INSERT INTO dbo.ticket_types (package_id, ticket_name, price, quantity, date) VALUES
(@packageId6, N'成人', 1761, 30, '2025-07-01'),
(@packageId6, N'青少年', 1472, 30, '2025-07-01'),
(@packageId6, N'幼童', 1059, 30, '2025-07-01')
--套票2
INSERT INTO dbo.ticket_packages (
    ticket_id, name, description, image_url, image_type,
    start_time, end_time, created_by, state,created_at
) VALUES (
    @ticketId4,
    N'東京迪士尼度假區VIP快速通關',
    N'VIP快速通關護照：指定日期票券、保證入園。價格：園區票券採浮動價格制。請遊客先確認各日期的票券價格，再購買指定日期票券。 ※3 歲以下的孩童可免費入園。',
    'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20220706120224_fLhdh/jpg',
    'jpg',
    '2025-07-01T18:00:00', '2025-08-31T22:00:00',
    1, 1,'2025-08-31T22:00:00'
);
DECLARE @packageId12 INT = SCOPE_IDENTITY();

INSERT INTO dbo.ticket_types (package_id, ticket_name, price, quantity, date) VALUES
(@packageId12, N'成人', 3761, 30, '2025-07-01'),
(@packageId12, N'青少年', 3472, 30, '2025-07-01'),
(@packageId12, N'幼童', 3059, 30, '2025-07-01')
-----==================================================================================================
--6======================================================================================
----------------------------------淺草寺-------------------------
INSERT INTO attraction_tickets (
    name, description, address, image_url, image_type,
    views, state, start_time, end_time,
    created_at, created_by, city, country_id
) VALUES (
    N'淺草寺',
    N'購買東京迪士尼度假區一日護照，保證入園輕鬆暢遊東京迪士尼度假區的夢幻世界 ',
    N'日本-關東 , 千葉 , 東京 , 浦安 , 東京迪士尼度假區 , 東京迪士尼樂園',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQHiI9LWh5vFQnc5ok0_OPEVOIBfXIbcjbSwQ&s',
    'jpg',
    45, 1,
    '2025-07-01T18:30:00', '2025-08-31T22:00:00',
    '2025-06-30T15:00:00', 3, 35,2
);
DECLARE @ticketId5 INT;
SET @ticketId5 = SCOPE_IDENTITY();

-- 插入 tag 關聯
INSERT INTO dbo.attraction_tickets_middle_attraction_tags (ticket_id, tag_id) VALUES
(@ticketId5, 7),
(@ticketId5, 9);

-- 插入 type 關聯
INSERT INTO dbo.attraction_tickets_middle_types (ticket_id, attraction_type_id) VALUES
(@ticketId5, 11);
---插入圖片
INSERT INTO dbo.ticket_images (ticket_id, image_url, image_type, sort_order) VALUES
(@ticketId5, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20210311083908_DPiSx/jpg', 'jpg', 1),
(@ticketId5, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20210311083904_QjfAr/jpg', 'jpg', 2),
(@ticketId5, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20250519023916_vm7LG/jpg', 'jpg', 3),
(@ticketId5, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20220706124018_AcD2V/jpg', 'jpg', 4);
--插入內容
INSERT INTO dbo.ticket_contents (ticket_id, title, subtitle, description) VALUES
(@ticketId5, N'商品說明', NULL, N'東京迪士尼度假區介紹
東京迪士尼度假區是ㄧ座擁有多家設施的大型主題度假區。包括「東京迪士尼樂園」、「東京迪士尼海洋」這兩座風格各異的迪士尼主題樂園、三家迪士尼飯店、多家官方飯店、ㄧ座綜合型商業設施「伊克斯皮兒莉」以及環繞度假區運行的便捷單軌電車等。不僅能為廣大遊客提供暢遊園區的歡樂體驗，同時透過住宿服務讓遊客得到購物、美食等多元化的度假體驗。'),
(@ticketId5, N'購買須知', NULL, N'以各種與海洋相關的故事、傳說為靈感而誕生的主題樂園東京迪士尼海洋。在七大主題海港中，有各式充滿冒險與想像的遊樂設施和娛樂表演，讓人流連忘返、歡樂無窮！
・失落河三角洲：探索中美洲加勒比海沿岸神祕莫測、危機四伏的神殿，揭祕悠遠古老的叢林傳說。
・發現港：超越時空的未來港口
・美國海濱：20世紀初期的紐約港和鱈魚岬在此重現，風情各異、充滿懷舊氣息。
・阿拉伯海岸： 一起來體驗充滿神祕與魔法的《天方夜譚》的世界吧！
・美人魚礁湖：小美人魚和夥伴們快樂的海底王國。
・神祕島：活火山深處、充滿驚奇的祕密基地。通往冒險之旅的大門就隠藏其中，神祕的天才科學家尼莫船長將帶您踏上一段段旅程。
・地中海港灣：浪漫古老的南歐海港小鎮。溫馨質樸的餐廳鱗次櫛比，漁村風景美麗如畫。運河兩岸還有多家餐廳和商店，各具魅力。'),
(@ticketId5, N'如何使用', NULL, N'從埔里出發，含上下山接駁服務與保險。'),
(@ticketId5, N'購買體驗地點須知', NULL, N'從埔里出發，含上下山接駁服務與保險。');
--套票1
INSERT INTO dbo.ticket_packages (
    ticket_id, name, description, image_url, image_type,
    start_time, end_time, created_by, state,created_at
) VALUES (
    @ticketId5,
    N'淺草寺',
    N'一日護照：指定日期票券、保證入園。價格：園區票券採浮動價格制。請遊客先確認各日期的票券價格，再購買指定日期票券。 ※3 歲以下的孩童可免費入園。',
    'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20210311083900_nd0m7/jpg',
    'jpg',
    '2025-07-01T18:00:00', '2025-08-31T22:00:00',
    1, 1,'2025-08-31T22:00:00'
);
DECLARE @packageId7 INT = SCOPE_IDENTITY();

INSERT INTO dbo.ticket_types (package_id, ticket_name, price, quantity, date) VALUES
(@packageId7, N'成人', 0, 30, '2025-07-01'),
(@packageId7, N'青少年', 0, 30, '2025-07-01'),
(@packageId7, N'幼童', 0, 30, '2025-07-01')
-----==================================================================================================
--7======================================================================================
----------------------------------明治神宮-------------------------
INSERT INTO attraction_tickets (
    name, description, address, image_url, image_type,
    views, state, start_time, end_time,
    created_at, created_by, city, country_id
) VALUES (
    N'明治神宮',
    N'購買東京迪士尼度假區一日護照，保證入園輕鬆暢遊東京迪士尼度假區的夢幻世界 ',
    N'日本-關東 , 千葉 , 東京 , 浦安 , 東京迪士尼度假區 , 東京迪士尼樂園',
    'https://lh3.googleusercontent.com/gps-cs-s/AC9h4no6szdL8zId1aM5AhqIkKu2m3bQXq6SWH7DnZQ1J3YspsSnO4X_RZyrITXVmHZYI5RE1zRw3hplihoI9OmMZYdg57RQLvEyOWI9HEOB4rj4SWNLrS5ln7PgNaiOIHNktGNL9aE=w270-h312-n-k-no',
    'jpg',
    45, 1,
    '2025-07-01T18:30:00', '2025-08-31T22:00:00',
    '2025-06-30T15:00:00', 3, 35,2
);
DECLARE @ticketId6 INT;
SET @ticketId6 = SCOPE_IDENTITY();

-- 插入 tag 關聯
INSERT INTO dbo.attraction_tickets_middle_attraction_tags (ticket_id, tag_id) VALUES
(@ticketId6, 7),
(@ticketId6, 9);

-- 插入 type 關聯
INSERT INTO dbo.attraction_tickets_middle_types (ticket_id, attraction_type_id) VALUES
(@ticketId6, 11);
---插入圖片
INSERT INTO dbo.ticket_images (ticket_id, image_url, image_type, sort_order) VALUES
(@ticketId6, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20210311083908_DPiSx/jpg', 'jpg', 1),
(@ticketId6, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20210311083904_QjfAr/jpg', 'jpg', 2),
(@ticketId6, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20250519023916_vm7LG/jpg', 'jpg', 3),
(@ticketId6, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20220706124018_AcD2V/jpg', 'jpg', 4);
--插入內容
INSERT INTO dbo.ticket_contents (ticket_id, title, subtitle, description) VALUES
(@ticketId6, N'商品說明', NULL, N'東京迪士尼度假區介紹
東京迪士尼度假區是ㄧ座擁有多家設施的大型主題度假區。包括「東京迪士尼樂園」、「東京迪士尼海洋」這兩座風格各異的迪士尼主題樂園、三家迪士尼飯店、多家官方飯店、ㄧ座綜合型商業設施「伊克斯皮兒莉」以及環繞度假區運行的便捷單軌電車等。不僅能為廣大遊客提供暢遊園區的歡樂體驗，同時透過住宿服務讓遊客得到購物、美食等多元化的度假體驗。'),
(@ticketId6, N'購買須知', NULL, N'以各種與海洋相關的故事、傳說為靈感而誕生的主題樂園東京迪士尼海洋。在七大主題海港中，有各式充滿冒險與想像的遊樂設施和娛樂表演，讓人流連忘返、歡樂無窮！
・失落河三角洲：探索中美洲加勒比海沿岸神祕莫測、危機四伏的神殿，揭祕悠遠古老的叢林傳說。
・發現港：超越時空的未來港口
・美國海濱：20世紀初期的紐約港和鱈魚岬在此重現，風情各異、充滿懷舊氣息。
・阿拉伯海岸： 一起來體驗充滿神祕與魔法的《天方夜譚》的世界吧！
・美人魚礁湖：小美人魚和夥伴們快樂的海底王國。
・神祕島：活火山深處、充滿驚奇的祕密基地。通往冒險之旅的大門就隠藏其中，神祕的天才科學家尼莫船長將帶您踏上一段段旅程。
・地中海港灣：浪漫古老的南歐海港小鎮。溫馨質樸的餐廳鱗次櫛比，漁村風景美麗如畫。運河兩岸還有多家餐廳和商店，各具魅力。'),
(@ticketId6, N'如何使用', NULL, N'從埔里出發，含上下山接駁服務與保險。'),
(@ticketId6, N'購買體驗地點須知', NULL, N'從埔里出發，含上下山接駁服務與保險。');
--套票1
INSERT INTO dbo.ticket_packages (
    ticket_id, name, description, image_url, image_type,
    start_time, end_time, created_by, state,created_at
) VALUES (
    @ticketId6,
    N'明治神宮',
    N'一日護照：指定日期票券、保證入園。價格：園區票券採浮動價格制。請遊客先確認各日期的票券價格，再購買指定日期票券。 ※3 歲以下的孩童可免費入園。',
    'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20210311083900_nd0m7/jpg',
    'jpg',
    '2025-07-01T18:00:00', '2025-08-31T22:00:00',
    1, 1,'2025-08-31T22:00:00'
);
DECLARE @packageId8 INT = SCOPE_IDENTITY();

INSERT INTO dbo.ticket_types (package_id, ticket_name, price, quantity, date) VALUES
(@packageId8, N'成人', 0, 30, '2025-07-01'),
(@packageId8, N'青少年', 0, 30, '2025-07-01'),
(@packageId8, N'幼童', 0, 30, '2025-07-01')
-----==================================================================================================
--8======================================================================================
----------------------------------上野恩賜公園-------------------------
INSERT INTO attraction_tickets (
    name, description, address, image_url, image_type,
    views, state, start_time, end_time,
    created_at, created_by, city, country_id
) VALUES (
    N'上野恩賜公園',
    N'購買東京迪士尼度假區一日護照，保證入園輕鬆暢遊東京迪士尼度假區的夢幻世界 ',
    N'日本-關東 , 千葉 , 東京 , 浦安 , 東京迪士尼度假區 , 東京迪士尼樂園',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS8-rDhFELe0KURVmI86jB8WQ-l_xc7oOcIDA&s',
    'jpg',
    46, 1,
    '2025-07-01T18:30:00', '2025-08-31T22:00:00',
    '2025-06-30T15:00:00', 3, 35,2
);
DECLARE @ticketId7 INT;
SET @ticketId7 = SCOPE_IDENTITY();

-- 插入 tag 關聯
INSERT INTO dbo.attraction_tickets_middle_attraction_tags (ticket_id, tag_id) VALUES
(@ticketId7, 7),
(@ticketId7, 9);

-- 插入 type 關聯
INSERT INTO dbo.attraction_tickets_middle_types (ticket_id, attraction_type_id) VALUES
(@ticketId7, 11);
---插入圖片
INSERT INTO dbo.ticket_images (ticket_id, image_url, image_type, sort_order) VALUES
(@ticketId7, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20210311083908_DPiSx/jpg', 'jpg', 1),
(@ticketId7, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20210311083904_QjfAr/jpg', 'jpg', 2),
(@ticketId7, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20250519023916_vm7LG/jpg', 'jpg', 3),
(@ticketId7, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20220706124018_AcD2V/jpg', 'jpg', 4);
--插入內容
INSERT INTO dbo.ticket_contents (ticket_id, title, subtitle, description) VALUES
(@ticketId7, N'商品說明', NULL, N'東京迪士尼度假區介紹
東京迪士尼度假區是ㄧ座擁有多家設施的大型主題度假區。包括「東京迪士尼樂園」、「東京迪士尼海洋」這兩座風格各異的迪士尼主題樂園、三家迪士尼飯店、多家官方飯店、ㄧ座綜合型商業設施「伊克斯皮兒莉」以及環繞度假區運行的便捷單軌電車等。不僅能為廣大遊客提供暢遊園區的歡樂體驗，同時透過住宿服務讓遊客得到購物、美食等多元化的度假體驗。'),
(@ticketId7, N'購買須知', NULL, N'以各種與海洋相關的故事、傳說為靈感而誕生的主題樂園東京迪士尼海洋。在七大主題海港中，有各式充滿冒險與想像的遊樂設施和娛樂表演，讓人流連忘返、歡樂無窮！
・失落河三角洲：探索中美洲加勒比海沿岸神祕莫測、危機四伏的神殿，揭祕悠遠古老的叢林傳說。
・發現港：超越時空的未來港口
・美國海濱：20世紀初期的紐約港和鱈魚岬在此重現，風情各異、充滿懷舊氣息。
・阿拉伯海岸： 一起來體驗充滿神祕與魔法的《天方夜譚》的世界吧！
・美人魚礁湖：小美人魚和夥伴們快樂的海底王國。
・神祕島：活火山深處、充滿驚奇的祕密基地。通往冒險之旅的大門就隠藏其中，神祕的天才科學家尼莫船長將帶您踏上一段段旅程。
・地中海港灣：浪漫古老的南歐海港小鎮。溫馨質樸的餐廳鱗次櫛比，漁村風景美麗如畫。運河兩岸還有多家餐廳和商店，各具魅力。'),
(@ticketId7, N'如何使用', NULL, N'從埔里出發，含上下山接駁服務與保險。'),
(@ticketId7, N'購買體驗地點須知', NULL, N'從埔里出發，含上下山接駁服務與保險。');
--套票1
INSERT INTO dbo.ticket_packages (
    ticket_id, name, description, image_url, image_type,
    start_time, end_time, created_by, state,created_at
) VALUES (
    @ticketId7,
    N'上野恩賜公園',
    N'一日護照：指定日期票券、保證入園。價格：園區票券採浮動價格制。請遊客先確認各日期的票券價格，再購買指定日期票券。 ※3 歲以下的孩童可免費入園。',
    'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_32488/20210311083900_nd0m7/jpg',
    'jpg',
    '2025-07-01T18:00:00', '2025-08-31T22:00:00',
    1, 1,'2025-08-31T22:00:00'
);
DECLARE @packageId9 INT = SCOPE_IDENTITY();

INSERT INTO dbo.ticket_types (package_id, ticket_name, price, quantity, date) VALUES
(@packageId9, N'成人', 0, 30, '2025-07-01'),
(@packageId9, N'青少年', 0, 30, '2025-07-01'),
(@packageId9, N'幼童', 0, 30, '2025-07-01')
-----==================================================================================================
--9======================================================================================
----------------------------------池袋陽光水族館門票・陽光60瞭望台展望公園-------------------------
INSERT INTO attraction_tickets (
    name, description, address, image_url, image_type,
    views, state, start_time, end_time,
    created_at, created_by, city, country_id
) VALUES (
    N'池袋陽光水族館門票・陽光60瞭望台展望公園',
    N'陽光水族館設於高樓之頂，KKday電子票免排隊暢游都市海洋世界
陽光水族館展出500種以上、高達23,000隻海洋生物，必看巨大水母水槽、飛天企鵝
陽光60瞭望台從海拔高度251公尺的展望公園，俯視東京360度都市風景
可單獨購買陽光水族館或陽光60瞭望台入場門票，亦有優惠套票可供選擇
於KKday線上購買票券，將無需另外申請指定日期時間入場整理券，於所選擇的日期直接前往水族館或瞭望台入口，出示QR Code憑證即可。 ',
    N'日本 - 關東 , 東京 , 池袋',
    'https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_166262/20240404064423_r1IBX/jpg',
    'jpg',
    47, 1,
    '2025-07-01T18:30:00', '2025-08-31T22:00:00',
    '2025-06-30T15:00:00', 3, 35,2
);
DECLARE @ticketId8 INT;
SET @ticketId8 = SCOPE_IDENTITY();

-- 插入 tag 關聯
INSERT INTO dbo.attraction_tickets_middle_attraction_tags (ticket_id, tag_id) VALUES
(@ticketId8, 8),
(@ticketId8, 2);

-- 插入 type 關聯
INSERT INTO dbo.attraction_tickets_middle_types (ticket_id, attraction_type_id) VALUES
(@ticketId8, 5);
---插入圖片
INSERT INTO dbo.ticket_images (ticket_id, image_url, image_type, sort_order) VALUES
(@ticketId8, 'https://image.kkday.com/v2/image/get/h_300%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_166262/20240404064421_iQ2DN/jpg', 'jpg', 1),
(@ticketId8, 'https://image.kkday.com/v2/image/get/h_300%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_166262/20250403061302_W6rwC/jpg', 'jpg', 2),
(@ticketId8, 'https://image.kkday.com/v2/image/get/h_300%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_166262/20240404064422_T6gnv/jpg', 'jpg', 3),
(@ticketId8, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_166262/20250403061303_I8B3v/jpg', 'jpg', 4);
--插入內容
INSERT INTO dbo.ticket_contents (ticket_id, title, subtitle, description) VALUES
(@ticketId8, N'商品說明', NULL, N'東京迪士尼度假區介紹
東京迪士尼度假區是ㄧ座擁有多家設施的大型主題度假區。包括「東京迪士尼樂園」、「東京迪士尼海洋」這兩座風格各異的迪士尼主題樂園、三家迪士尼飯店、多家官方飯店、ㄧ座綜合型商業設施「伊克斯皮兒莉」以及環繞度假區運行的便捷單軌電車等。不僅能為廣大遊客提供暢遊園區的歡樂體驗，同時透過住宿服務讓遊客得到購物、美食等多元化的度假體驗。'),
(@ticketId8, N'購買須知', NULL, N'以各種與海洋相關的故事、傳說為靈感而誕生的主題樂園東京迪士尼海洋。在七大主題海港中，有各式充滿冒險與想像的遊樂設施和娛樂表演，讓人流連忘返、歡樂無窮！
・失落河三角洲：探索中美洲加勒比海沿岸神祕莫測、危機四伏的神殿，揭祕悠遠古老的叢林傳說。
・發現港：超越時空的未來港口
・美國海濱：20世紀初期的紐約港和鱈魚岬在此重現，風情各異、充滿懷舊氣息。
・阿拉伯海岸： 一起來體驗充滿神祕與魔法的《天方夜譚》的世界吧！
・美人魚礁湖：小美人魚和夥伴們快樂的海底王國。
・神祕島：活火山深處、充滿驚奇的祕密基地。通往冒險之旅的大門就隠藏其中，神祕的天才科學家尼莫船長將帶您踏上一段段旅程。
・地中海港灣：浪漫古老的南歐海港小鎮。溫馨質樸的餐廳鱗次櫛比，漁村風景美麗如畫。運河兩岸還有多家餐廳和商店，各具魅力。'),
(@ticketId8, N'如何使用', NULL, N'從埔里出發，含上下山接駁服務與保險。'),
(@ticketId8, N'購買體驗地點須知', NULL, N'從埔里出發，含上下山接駁服務與保險。');
--套票1
INSERT INTO dbo.ticket_packages (
    ticket_id, name, description, image_url, image_type,
    start_time, end_time, created_by, state,created_at
) VALUES (
    @ticketId8,
    N'東京池袋 SUNSHINE 陽光水族館入場券',
    N'於線上購買本票券的遊客，將無需另外申請水族館的網路指定日期時間入場整理券。 ',
    'https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_166262/20240404064423_r1IBX/jpg',
    'jpg',
    '2025-07-01T18:00:00', '2025-08-31T22:00:00',
    1, 1,'2025-08-31T22:00:00'
);
DECLARE @packageId10 INT = SCOPE_IDENTITY();

INSERT INTO dbo.ticket_types (package_id, ticket_name, price, quantity, date) VALUES
(@packageId10, N'成人', 514, 30, '2025-07-01'),
(@packageId10, N'青少年', 257, 30, '2025-07-01'),
(@packageId10, N'幼童', 158, 30, '2025-07-01')
-----==================================================================================================
--10======================================================================================
----------------------------------【數量限定】吉伊卡哇麵包店購物券預約（東京）-------------------------
INSERT INTO attraction_tickets (
    name, description, address, image_url, image_type,
    views, state, start_time, end_time,
    created_at, created_by, city, country_id
) VALUES (
    N'【數量限定】吉伊卡哇麵包店購物券預約（東京）',
    N'陽光水族館設於高樓之頂，KKday電子票免排隊暢游都市海洋世界
陽光水族館展出500種以上、高達23,000隻海洋生物，必看巨大水母水槽、飛天企鵝
陽光60瞭望台從海拔高度251公尺的展望公園，俯視東京360度都市風景
可單獨購買陽光水族館或陽光60瞭望台入場門票，亦有優惠套票可供選擇
於KKday線上購買票券，將無需另外申請指定日期時間入場整理券，於所選擇的日期直接前往水族館或瞭望台入口，出示QR Code憑證即可。 ',
    N'日本 - 關東 , 東京 , 池袋',
    'https://image.kkday.com/v2/image/get/c_fill%2Ch_600%2Cq_55%2Ct_webp/s1.kkday.com/product_270304/20250313052544_35BWE/jpg',
    'jpg',
    42, 1,
    '2025-07-01T18:30:00', '2025-08-31T22:00:00',
    '2025-06-30T15:00:00', 3, 35,2
);
DECLARE @ticketId9 INT;
SET @ticketId9 = SCOPE_IDENTITY();

-- 插入 tag 關聯
INSERT INTO dbo.attraction_tickets_middle_attraction_tags (ticket_id, tag_id) VALUES
(@ticketId9, 8),
(@ticketId9, 2);

-- 插入 type 關聯
INSERT INTO dbo.attraction_tickets_middle_types (ticket_id, attraction_type_id) VALUES
(@ticketId9, 5);
---插入圖片
INSERT INTO dbo.ticket_images (ticket_id, image_url, image_type, sort_order) VALUES
(@ticketId9, 'https://image.kkday.com/v2/image/get/h_300%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_166262/20240404064421_iQ2DN/jpg', 'jpg', 1),
(@ticketId9, 'https://image.kkday.com/v2/image/get/h_300%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_166262/20250403061302_W6rwC/jpg', 'jpg', 2),
(@ticketId9, 'https://image.kkday.com/v2/image/get/h_300%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_166262/20240404064422_T6gnv/jpg', 'jpg', 3),
(@ticketId9, 'https://image.kkday.com/v2/image/get/h_650%2Cc_fit/s1.kkday.com/product_166262/20250403061303_I8B3v/jpg', 'jpg', 4);
--插入內容
INSERT INTO dbo.ticket_contents (ticket_id, title, subtitle, description) VALUES
(@ticketId9, N'商品說明', NULL, N'東京迪士尼度假區介紹
東京迪士尼度假區是ㄧ座擁有多家設施的大型主題度假區。包括「東京迪士尼樂園」、「東京迪士尼海洋」這兩座風格各異的迪士尼主題樂園、三家迪士尼飯店、多家官方飯店、ㄧ座綜合型商業設施「伊克斯皮兒莉」以及環繞度假區運行的便捷單軌電車等。不僅能為廣大遊客提供暢遊園區的歡樂體驗，同時透過住宿服務讓遊客得到購物、美食等多元化的度假體驗。'),
(@ticketId9, N'購買須知', NULL, N'以各種與海洋相關的故事、傳說為靈感而誕生的主題樂園東京迪士尼海洋。在七大主題海港中，有各式充滿冒險與想像的遊樂設施和娛樂表演，讓人流連忘返、歡樂無窮！
・失落河三角洲：探索中美洲加勒比海沿岸神祕莫測、危機四伏的神殿，揭祕悠遠古老的叢林傳說。
・發現港：超越時空的未來港口
・美國海濱：20世紀初期的紐約港和鱈魚岬在此重現，風情各異、充滿懷舊氣息。
・阿拉伯海岸： 一起來體驗充滿神祕與魔法的《天方夜譚》的世界吧！
・美人魚礁湖：小美人魚和夥伴們快樂的海底王國。
・神祕島：活火山深處、充滿驚奇的祕密基地。通往冒險之旅的大門就隠藏其中，神祕的天才科學家尼莫船長將帶您踏上一段段旅程。
・地中海港灣：浪漫古老的南歐海港小鎮。溫馨質樸的餐廳鱗次櫛比，漁村風景美麗如畫。運河兩岸還有多家餐廳和商店，各具魅力。'),
(@ticketId9, N'如何使用', NULL, N'從埔里出發，含上下山接駁服務與保險。'),
(@ticketId9, N'購買體驗地點須知', NULL, N'從埔里出發，含上下山接駁服務與保險。');
--套票1
INSERT INTO dbo.ticket_packages (
    ticket_id, name, description, image_url, image_type,
    start_time, end_time, created_by, state,created_at
) VALUES (
    @ticketId9,
    N'吉伊卡哇麵包店購物券預約',
    N'於線上購買本票券的遊客，將無需另外申請水族館的網路指定日期時間入場整理券。 ',
    'https://image.kkday.com/v2/image/get/h_600%2Cc_fill%2Cq_55%2Ct_webp/s1.kkday.com/product_166262/20240404064423_r1IBX/jpg',
    'jpg',
    '2025-07-01T18:00:00', '2025-08-31T22:00:00',
    1, 1,'2025-08-31T22:00:00'
);
DECLARE @packageId11 INT = SCOPE_IDENTITY();

INSERT INTO dbo.ticket_types (package_id, ticket_name, price, quantity, date) VALUES
(@packageId11, N'成人', 514, 30, '2025-07-01'),
(@packageId11, N'青少年', 257, 30, '2025-07-01'),
(@packageId11, N'幼童', 158, 30, '2025-07-01')
-----==================================================================================================
