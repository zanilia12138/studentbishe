-- 旧版资料路径：/uploads/文件名（根下无子路径），迁移为 /uploads/loads/doc/文件名
-- 在备份库后执行；仅影响 material 表。
UPDATE `material`
SET `file_path` = CONCAT('/uploads/loads/doc/', SUBSTRING(`file_path`, 10))
WHERE `file_path` LIKE '/uploads/%'
  AND `file_path` NOT LIKE '/uploads/loads/%'
  AND LOCATE('/', SUBSTRING(`file_path`, 10)) = 0;
