-- MySQL Script generated by MySQL Workbench
-- 05/04/18 18:51:09
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema finman
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `finman` ;

-- -----------------------------------------------------
-- Schema finman
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `finman` DEFAULT CHARACTER SET latin1 ;
USE `finman` ;

-- -----------------------------------------------------
-- Table `finman`.`stockcategory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `finman`.`stockcategory` ;

CREATE TABLE IF NOT EXISTS `finman`.`stockcategory` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_en` VARCHAR(255) NOT NULL,
  `name_de` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `finman`.`currency`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `finman`.`currency` ;

CREATE TABLE IF NOT EXISTS `finman`.`currency` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `symbol` VARCHAR(45) NOT NULL,
  `name` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `finman`.`exchange`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `finman`.`exchange` ;

CREATE TABLE IF NOT EXISTS `finman`.`exchange` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `short_name` VARCHAR(45) NOT NULL,
  `full_name` VARCHAR(255) NULL,
  `timezone` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `finman`.`stockitem`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `finman`.`stockitem` ;

CREATE TABLE IF NOT EXISTS `finman`.`stockitem` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'The primary key of a stock item',
  `symbol` VARCHAR(45) NOT NULL,
  `short_name` VARCHAR(128) NULL,
  `full_name` VARCHAR(255) NULL,
  `stockcategory_id` INT NOT NULL,
  `currency_id` INT NOT NULL,
  `exchange_id` INT NOT NULL,
  `last_modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `symbol_UNIQUE` (`symbol` ASC),
  INDEX `stockitem_fkstockcategory_idx` (`stockcategory_id` ASC),
  INDEX `stockitem_fkcurrency_idx` (`currency_id` ASC),
  INDEX `stockitem_fkexchange_idx` (`exchange_id` ASC),
  CONSTRAINT `stockitem_fkstockcategory`
    FOREIGN KEY (`stockcategory_id`)
    REFERENCES `finman`.`stockcategory` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `stockitem_fkcurrency`
    FOREIGN KEY (`currency_id`)
    REFERENCES `finman`.`currency` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `stockitem_fkexchange`
    FOREIGN KEY (`exchange_id`)
    REFERENCES `finman`.`exchange` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `finman`.`portfolio`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `finman`.`portfolio` ;

CREATE TABLE IF NOT EXISTS `finman`.`portfolio` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `short_name` VARCHAR(45) NOT NULL,
  `full_name` TEXT(512) NULL,
  `last_modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `finman`.`assets`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `finman`.`assets` ;

CREATE TABLE IF NOT EXISTS `finman`.`assets` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `portfolio_id` INT NOT NULL,
  `stockitem_id` INT NOT NULL,
  `transaction_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` MEDIUMTEXT NOT NULL,
  `price` DECIMAL(19,4) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `stockitem_id_idx` (`stockitem_id` ASC),
  INDEX `portfolio_id_idx` (`portfolio_id` ASC),
  CONSTRAINT `assets_fkportfolio`
    FOREIGN KEY (`portfolio_id`)
    REFERENCES `finman`.`portfolio` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `assets_fkstockitem`
    FOREIGN KEY (`stockitem_id`)
    REFERENCES `finman`.`stockitem` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `finman`.`stockhistory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `finman`.`stockhistory` ;

CREATE TABLE IF NOT EXISTS `finman`.`stockhistory` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `stockitem_id` INT NOT NULL,
  `price_date` DATETIME NOT NULL,
  `open` DECIMAL(19,4) NULL,
  `high` DECIMAL(19,4) NULL,
  `low` DECIMAL(19,4) NULL,
  `close` DECIMAL(19,4) NULL,
  `volume` MEDIUMTEXT NULL,
  `lastModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `stockitem_id_idx` (`stockitem_id` ASC),
  CONSTRAINT `stockhistory_fkstockitem`
    FOREIGN KEY (`stockitem_id`)
    REFERENCES `finman`.`stockitem` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `finman`.`stockquote`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `finman`.`stockquote` ;

CREATE TABLE IF NOT EXISTS `finman`.`stockquote` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `stockitem_id` INT NOT NULL,
  `ask` DECIMAL(19,4) NULL,
  `ask_size` MEDIUMTEXT NULL,
  `bid` DECIMAL(19,4) NULL,
  `bid_size` MEDIUMTEXT NULL,
  `price` DECIMAL(19,4) NULL,
  `open` DECIMAL(19,4) NULL,
  `last_close` DECIMAL(19,4) NULL,
  `high` DECIMAL(19,4) NULL,
  `low` DECIMAL(19,4) NULL,
  `year_high` DECIMAL(19,4) NULL,
  `year_low` DECIMAL(19,4) NULL,
  `price_avg50` DECIMAL(19,4) NULL,
  `price_avg200` DECIMAL(19,4) NULL,
  `trade_volume` MEDIUMTEXT NULL,
  `trade_avg_volume` MEDIUMTEXT NULL,
  `last_trade_time` DATETIME NULL,
  `last_trade_size` MEDIUMTEXT NULL,
  `last_modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `stockquote_fkstockitem_idx` (`stockitem_id` ASC),
  CONSTRAINT `stockquote_fkstockitem`
    FOREIGN KEY (`stockitem_id`)
    REFERENCES `finman`.`stockitem` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SET SQL_MODE = '';
GRANT USAGE ON *.* TO finman;
 DROP USER finman;
SET SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';
CREATE USER 'finman' IDENTIFIED BY 'finmanfidos';

GRANT ALL ON `finman`.* TO 'finman';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `finman`.`stockcategory`
-- -----------------------------------------------------
START TRANSACTION;
USE `finman`;
INSERT INTO `finman`.`stockcategory` (`id`, `name_en`, `name_de`) VALUES (1, 'Quote', 'Aktie');
INSERT INTO `finman`.`stockcategory` (`id`, `name_en`, `name_de`) VALUES (2, 'Index', 'Index');
INSERT INTO `finman`.`stockcategory` (`id`, `name_en`, `name_de`) VALUES (3, 'Currency', 'Währung');
INSERT INTO `finman`.`stockcategory` (`id`, `name_en`, `name_de`) VALUES (4, 'Commodity', 'Rohstoff');
INSERT INTO `finman`.`stockcategory` (`id`, `name_en`, `name_de`) VALUES (5, 'Bond', 'Obligation');
INSERT INTO `finman`.`stockcategory` (`id`, `name_en`, `name_de`) VALUES (6, 'Future', 'Option');

COMMIT;
