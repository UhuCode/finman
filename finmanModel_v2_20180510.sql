-- MySQL Script generated by MySQL Workbench
-- 05/10/18 22:13:20
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
CREATE SCHEMA IF NOT EXISTS `finman` DEFAULT CHARACTER SET latin1 COLLATE latin1_bin ;
USE `finman` ;

-- -----------------------------------------------------
-- Table `finman`.`portfolio`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `finman`.`portfolio` ;

CREATE TABLE IF NOT EXISTS `finman`.`portfolio` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `short_name` VARCHAR(45) NOT NULL,
  `full_name` TEXT(512) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `finman`.`stock`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `finman`.`stock` ;

CREATE TABLE IF NOT EXISTS `finman`.`stock` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `symbol` VARCHAR(45) NOT NULL,
  `name` VARCHAR(255) NULL,
  `currency` VARCHAR(45) NULL,
  `exchange` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `symbol_UNIQUE` (`symbol` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `finman`.`asset`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `finman`.`asset` ;

CREATE TABLE IF NOT EXISTS `finman`.`asset` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `portfolio_id` INT NOT NULL,
  `stock_id` INT NOT NULL,
  `transaction_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` MEDIUMTEXT NOT NULL,
  `price` DECIMAL(19,4) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_asset_portfolio_idx` (`portfolio_id` ASC),
  INDEX `fk_asset_stock_idx` (`stock_id` ASC),
  CONSTRAINT `fk_asset_portfolio`
    FOREIGN KEY (`portfolio_id`)
    REFERENCES `finman`.`portfolio` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_asset_stock`
    FOREIGN KEY (`stock_id`)
    REFERENCES `finman`.`stock` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `finman`.`stockhistory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `finman`.`stockhistory` ;

CREATE TABLE IF NOT EXISTS `finman`.`stockhistory` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `stock_id` INT NOT NULL,
  `history_date` DATETIME NOT NULL,
  `open` DECIMAL(19,4) NULL,
  `high` DECIMAL(19,4) NULL,
  `low` DECIMAL(19,4) NULL,
  `close` DECIMAL(19,4) NULL,
  `volume` MEDIUMTEXT NULL,
  `lastModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_stockhistory_stock_idx` (`stock_id` ASC),
  CONSTRAINT `fk_stockhistory_stock`
    FOREIGN KEY (`stock_id`)
    REFERENCES `finman`.`stock` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `finman`.`stockquote`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `finman`.`stockquote` ;

CREATE TABLE IF NOT EXISTS `finman`.`stockquote` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `stock_id` INT NOT NULL,
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
  INDEX `fk_stockquote_stock_idx` (`stock_id` ASC),
  CONSTRAINT `fk_stockquote_stock`
    FOREIGN KEY (`stock_id`)
    REFERENCES `finman`.`stock` (`id`)
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
-- Data for table `finman`.`portfolio`
-- -----------------------------------------------------
START TRANSACTION;
USE `finman`;
INSERT INTO `finman`.`portfolio` (`id`, `short_name`, `full_name`) VALUES (1, 'NONE', 'Default portfolio ');
INSERT INTO `finman`.`portfolio` (`id`, `short_name`, `full_name`) VALUES (2, 'MB', 'Migros Bank');

COMMIT;

