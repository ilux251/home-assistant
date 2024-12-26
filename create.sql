CREATE DATABASE IF NOT EXISTS `home-assistant` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;

CREATE TABLE `home-assistant`.`task` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `summary` VARCHAR(300) NULL,
  `checked` TINYINT NULL,
  `subtask` INT NULL,
  `date` DATE,
  PRIMARY KEY (`id`));

CREATE USER `home-assistant`@localhost IDENTIFIED BY 'test1234';
GRANT ALL ON `home-assistant`.* TO `home-assistant`@localhost;