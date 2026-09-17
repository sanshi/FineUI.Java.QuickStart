-- 电影种子数据：仅当 movie 表为空时才插入。
-- 幂等：文件模式数据库持久化，data.sql 每次启动都会执行，用「表空才插」避免重复插入，
-- 同时保留用户后来新增/编辑的数据（不像 DELETE / create-drop 那样每次抹掉）。
-- 不指定 id：让 IDENTITY 自增分配，避免显式插 id 导致自增序列不同步、后续新增撞主键。
INSERT INTO movie (title, release_date, genre, price)
SELECT * FROM (
    SELECT CAST('回到未来' AS VARCHAR(200)) AS title,
           CAST('1985-07-03' AS DATE) AS release_date,
           CAST('科幻' AS VARCHAR(100)) AS genre,
           CAST(8.99 AS DECIMAL(18, 2)) AS price
    UNION ALL SELECT '回到未来2', '1989-11-22', '科幻', 10.99
    UNION ALL SELECT '星球大战', '1977-05-25', '科幻', 12.99
    UNION ALL SELECT '星球大战2：帝国反击战', '1980-05-21', '科幻', 16.99
    UNION ALL SELECT '星球大战3：绝地归来', '1983-05-25', '科幻', 20.99
    UNION ALL SELECT '星球大战8：最后的绝地武士', '2018-01-05', '科幻', 50.99
    UNION ALL SELECT '星球大战9：天行者崛起', '2019-12-18', '科幻', 88.99
) seed
WHERE NOT EXISTS (SELECT 1 FROM movie);
