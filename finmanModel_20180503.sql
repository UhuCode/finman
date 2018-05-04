CREATE DATABASE  IF NOT EXISTS `finman` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `finman`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: finman
-- ------------------------------------------------------
-- Server version	5.7.16-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `portfolio`
--

DROP TABLE IF EXISTS `portfolio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portfolio` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `portfolioShortName` varchar(45) DEFAULT NULL,
  `portfolioFullName` varchar(45) DEFAULT NULL,
  `lastModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portfolio`
--

LOCK TABLES `portfolio` WRITE;
/*!40000 ALTER TABLE `portfolio` DISABLE KEYS */;
INSERT INTO `portfolio` VALUES (1,'mb','Migros Bank','2018-05-03 20:21:34'),(2,'default','default','2018-05-03 20:21:34');
/*!40000 ALTER TABLE `portfolio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portfolio_stockitem`
--

DROP TABLE IF EXISTS `portfolio_stockitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portfolio_stockitem` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `portfolioID` int(11) NOT NULL,
  `stockItemID` int(11) NOT NULL,
  `purchaseDate` date DEFAULT NULL,
  `purchaseAmount` int(11) DEFAULT NULL,
  `purchasePrice` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `stockItemID_idx` (`stockItemID`),
  KEY `portfolioID_idx` (`portfolioID`),
  CONSTRAINT `portfolio_stockitem_fkportfolio` FOREIGN KEY (`portfolioID`) REFERENCES `portfolio` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `portfolio_stockitem_fkstockitem` FOREIGN KEY (`stockItemID`) REFERENCES `stockitem` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portfolio_stockitem`
--

LOCK TABLES `portfolio_stockitem` WRITE;
/*!40000 ALTER TABLE `portfolio_stockitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `portfolio_stockitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stockitem`
--

DROP TABLE IF EXISTS `stockitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stockitem` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'The primary key of a stock item',
  `symbol` varchar(45) DEFAULT NULL,
  `stockName` varchar(64) DEFAULT NULL,
  `lastModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stockitem`
--

LOCK TABLES `stockitem` WRITE;
/*!40000 ALTER TABLE `stockitem` DISABLE KEYS */;
INSERT INTO `stockitem` VALUES (1,'UBS','UBS ','2018-05-03 20:23:34');
/*!40000 ALTER TABLE `stockitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stockquote`
--

DROP TABLE IF EXISTS `stockquote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stockquote` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stockItemID` int(11) NOT NULL,
  `priceDate` datetime NOT NULL,
  `priceOpen` double DEFAULT NULL,
  `priceHigh` double DEFAULT NULL,
  `priceLow` double DEFAULT NULL,
  `priceClose` double DEFAULT NULL,
  `tradeVolume` int(11) DEFAULT NULL,
  `lastModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `stockItemID_idx` (`stockItemID`),
  CONSTRAINT `fkstockitem` FOREIGN KEY (`stockItemID`) REFERENCES `stockitem` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stockquote`
--

LOCK TABLES `stockquote` WRITE;
/*!40000 ALTER TABLE `stockquote` DISABLE KEYS */;
/*!40000 ALTER TABLE `stockquote` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-03 22:30:47
