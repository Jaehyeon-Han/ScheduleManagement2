-- 테스트에서 사용하기 위한 초기 입력값

INSERT INTO users (email, password, name, created_at, last_modified_at)
VALUES ('god@heaven.world', 'iamgod', 'God', '1970-01-01 00:00:00', '1970-01-01 00:00:00');

INSERT INTO `Schedules` (`author_id`, `title`, `content`, `created_at`, `last_modified_at`)
VALUES ( 1, 'Create world', 'Let there be world.', '1970-01-01 00:00:00', '1970-01-01 00:00:00');