DROP SCHEMA IF EXISTS `Llamabank`;
CREATE SCHEMA IF NOT EXISTS `Llamabank` DEFAULT CHARACTER SET utf8 ;
USE `Llamabank` ;
-- Create user and grant access
CREATE USER 'userLlamabank'@'%' IDENTIFIED BY 'pwLlamabank';
GRANT ALL PRIVILEGES ON Llamabank. * TO 'userLlamabank'@'%';