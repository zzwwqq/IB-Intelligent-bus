/*
SQLyog Ultimate v10.00 Beta1
MySQL - 5.7.21-log : Database - ib
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ib` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `ib`;

/*Table structure for table `bus` */

DROP TABLE IF EXISTS `bus`;

CREATE TABLE `bus` (
  `busCode` int(11) NOT NULL AUTO_INCREMENT,
  `busLicense` varchar(20) NOT NULL,
  `busType` varchar(25) DEFAULT NULL,
  `busStatus` varchar(2) DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  PRIMARY KEY (`busCode`),
  KEY `AK_Key_2` (`busLicense`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `bus` */

insert  into `bus`(`busCode`,`busLicense`,`busType`,`busStatus`,`startTime`) values (3,'鄂A-12345','小客车','1','2018-07-18 00:00:00'),(4,'鄂A-89DB7','中客车','1','2018-07-25 00:00:00'),(5,'辽W-5798A','大客车','1','2018-07-18 00:00:00'),(6,'豫C-8907B','超大客车','1','2018-07-25 00:00:00'),(7,'京A-LO565','小客车','1','2018-07-11 00:00:00');

/*Table structure for table `line` */

DROP TABLE IF EXISTS `line`;

CREATE TABLE `line` (
  `lineCode` int(11) NOT NULL AUTO_INCREMENT,
  `lineName` varchar(20) DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL,
  `startLineTime` datetime DEFAULT NULL,
  `direction` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`lineCode`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `line` */

insert  into `line`(`lineCode`,`lineName`,`status`,`startLineTime`,`direction`) values (4,'101','0','2018-07-22 00:00:00','1'),(5,'202','1','2018-07-17 00:00:00','0'),(6,'101','0','2018-07-22 00:00:00','1'),(7,'202','1','2018-07-17 00:00:00','0'),(8,'303','1','2018-07-11 00:00:00','0');

/*Table structure for table `line_station_ref` */

DROP TABLE IF EXISTS `line_station_ref`;

CREATE TABLE `line_station_ref` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lineCode` int(11) DEFAULT NULL,
  `stationCode` int(11) DEFAULT NULL,
  `stationOrder` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Reference_8` (`lineCode`),
  KEY `FK_Reference_9` (`stationCode`),
  CONSTRAINT `FK_Reference_8` FOREIGN KEY (`lineCode`) REFERENCES `line` (`lineCode`),
  CONSTRAINT `FK_Reference_9` FOREIGN KEY (`stationCode`) REFERENCES `station` (`stationCode`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

/*Data for the table `line_station_ref` */

insert  into `line_station_ref`(`id`,`lineCode`,`stationCode`,`stationOrder`) values (13,5,9,0),(14,5,8,1),(18,7,9,0),(19,7,8,1),(20,8,8,0),(21,8,9,1),(22,8,11,2),(23,4,11,0),(24,4,7,1),(25,4,10,2),(26,6,11,0),(27,6,7,1),(28,6,10,2);

/*Table structure for table `permission` */

DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
  `permissionCode` int(11) NOT NULL AUTO_INCREMENT,
  `permissionName` varchar(50) DEFAULT NULL,
  `permissionDescribe` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`permissionCode`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

/*Data for the table `permission` */

insert  into `permission`(`permissionCode`,`permissionName`,`permissionDescribe`) values (1,'user_change_pass_home_menu',NULL),(2,'user_add',NULL),(3,'user_del',NULL),(4,'user_save',NULL),(5,'bus_add',NULL),(6,'bus_del',NULL),(7,'bus_save',NULL),(8,'line_add',NULL),(9,'line_del',NULL),(10,'line_save',NULL),(11,'station_add',NULL),(12,'station_del',NULL),(13,'station_save',NULL),(14,'scheduling_add',NULL),(15,'scheduling_del',NULL),(16,'scheduling_save',NULL),(17,'scheduling_read',NULL),(18,'scheduling_write',NULL),(19,'scheduling_home_menu',NULL),(20,'station_home_menu',NULL),(21,'line_home_menu',NULL),(22,'bus_home_menu',NULL),(23,'user_home_menu',NULL),(24,'base_data',NULL),(25,'scheduling',NULL),(26,'user_manager',NULL);

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `roleCode` int(11) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(50) DEFAULT NULL,
  `roledescribe` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`roleCode`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `role` */

insert  into `role`(`roleCode`,`roleName`,`roledescribe`) values (1,'管理员','管理员角色'),(2,'调度员','调度员角色'),(3,'驾驶员','驾驶员角色');

/*Table structure for table `role_permission_ref` */

DROP TABLE IF EXISTS `role_permission_ref`;

CREATE TABLE `role_permission_ref` (
  `relationCode` int(11) NOT NULL AUTO_INCREMENT,
  `roleCode` int(11) DEFAULT NULL,
  `permissionCode` int(11) DEFAULT NULL,
  PRIMARY KEY (`relationCode`),
  KEY `FK_Reference_1` (`roleCode`),
  KEY `FK_Reference_2` (`permissionCode`),
  CONSTRAINT `FK_Reference_1` FOREIGN KEY (`roleCode`) REFERENCES `role` (`roleCode`),
  CONSTRAINT `FK_Reference_2` FOREIGN KEY (`permissionCode`) REFERENCES `permission` (`permissionCode`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

/*Data for the table `role_permission_ref` */

insert  into `role_permission_ref`(`relationCode`,`roleCode`,`permissionCode`) values (1,1,1),(2,1,2),(3,1,3),(4,1,4),(5,1,5),(6,1,6),(7,1,7),(8,1,8),(9,1,9),(10,1,10),(11,1,11),(12,1,12),(13,1,13),(14,1,14),(15,1,15),(16,1,16),(17,1,17),(18,1,18),(19,1,19),(20,1,20),(21,1,21),(22,1,22),(23,1,23),(24,1,24),(25,1,25),(26,1,26),(27,2,1),(28,2,14),(29,2,16),(31,2,17),(32,2,18),(33,2,19),(34,2,20),(35,2,21),(36,2,22),(37,2,23),(38,2,25),(40,3,1),(41,3,17),(42,3,19);

/*Table structure for table `scheduling` */

DROP TABLE IF EXISTS `scheduling`;

CREATE TABLE `scheduling` (
  `code` int(20) NOT NULL AUTO_INCREMENT,
  `lineCode` int(11) DEFAULT NULL,
  `tcNumber` varchar(25) DEFAULT NULL,
  `tcTime` varchar(20) DEFAULT NULL,
  `userCode` int(11) DEFAULT NULL,
  `startStation` int(11) DEFAULT NULL,
  `endStation` int(11) DEFAULT NULL,
  `busLicense` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`code`),
  KEY `FK_Reference_4` (`lineCode`),
  KEY `FK_Reference_5` (`userCode`),
  KEY `FK_SCH_REF_STATION_START` (`startStation`),
  KEY `FK_SCH_REF_STATION_END` (`endStation`),
  KEY `FK_Reference_3` (`busLicense`),
  CONSTRAINT `FK_Reference_3` FOREIGN KEY (`busLicense`) REFERENCES `bus` (`busLicense`),
  CONSTRAINT `FK_Reference_4` FOREIGN KEY (`lineCode`) REFERENCES `line` (`lineCode`),
  CONSTRAINT `FK_Reference_5` FOREIGN KEY (`userCode`) REFERENCES `sysuser` (`code`),
  CONSTRAINT `FK_SCH_REF_STATION_END` FOREIGN KEY (`endStation`) REFERENCES `station` (`stationCode`),
  CONSTRAINT `FK_SCH_REF_STATION_START` FOREIGN KEY (`startStation`) REFERENCES `station` (`stationCode`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Data for the table `scheduling` */

insert  into `scheduling`(`code`,`lineCode`,`tcNumber`,`tcTime`,`userCode`,`startStation`,`endStation`,`busLicense`) values (2,6,'3','30',6,7,10,'鄂A-89DB7'),(3,6,'3','30',6,7,10,'鄂A-89DB7'),(4,7,'6','45',2,9,8,'辽W-5798A'),(5,6,'3','30',6,7,10,'鄂A-89DB7'),(6,7,'2','45',2,9,8,'辽W-5798A'),(7,8,'4','25',5,11,8,'豫C-8907B'),(8,6,'3','30',6,7,10,'鄂A-89DB7'),(9,7,'2','45',2,9,8,'辽W-5798A'),(10,8,'4','25',5,8,11,'豫C-8907B');

/*Table structure for table `station` */

DROP TABLE IF EXISTS `station`;

CREATE TABLE `station` (
  `stationCode` int(11) NOT NULL AUTO_INCREMENT,
  `stationName` varchar(50) DEFAULT NULL,
  `longitude` varchar(50) DEFAULT NULL,
  `latitude` varchar(50) DEFAULT NULL,
  `region` varchar(50) DEFAULT NULL,
  `street` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`stationCode`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

/*Data for the table `station` */

insert  into `station`(`stationCode`,`stationName`,`longitude`,`latitude`,`region`,`street`) values (7,'通顺公司','46.2532','49.2325','黄石港区','步行街'),(8,'汇龙大厦','46.2532','49.2325','西塞山区','朱雀街'),(9,'京华路','36.2589','55.3609','下陆区','长安街'),(10,'南岳村','65.2697','88.6458','阳新县','德福巷'),(11,'爱康医院','125.3603','99.8745','铁山区','花湖街');

/*Table structure for table `sysuser` */

DROP TABLE IF EXISTS `sysuser`;

CREATE TABLE `sysuser` (
  `code` int(11) NOT NULL AUTO_INCREMENT,
  `loginName` varchar(25) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `name` varchar(25) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `idCard` varchar(25) DEFAULT NULL,
  `role` int(11) DEFAULT NULL,
  `driving` decimal(10,0) DEFAULT NULL,
  `status` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`code`),
  KEY `FK_Reference_10` (`role`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `sysuser` */

insert  into `sysuser`(`code`,`loginName`,`password`,`name`,`phone`,`idCard`,`role`,`driving`,`status`) values (1,'admin','e3c9c2bd447e767f8fd6f87409ba3d65','管理员','13122334455','135487198010220011',1,'0','1'),(2,'zzwwqq','e3c9c2bd447e767f8fd6f87409ba3d65','郑文庆','13264860490','422235647896547896',2,'10','1'),(5,'zhangsan','e3c9c2bd447e767f8fd6f87409ba3d65','张三','13256478965','420364589789456789',3,'15','1'),(6,'lichao','e3c9c2bd447e767f8fd6f87409ba3d65','李超','13652412122','422236452123547896',3,'5','1');

/* Procedure structure for procedure `PRO_DEL_LINE` */

/*!50003 DROP PROCEDURE IF EXISTS  `PRO_DEL_LINE` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `PRO_DEL_LINE`(IN `in_lineCode` varchar(20),OUT `out_code` int(1))
BEGIN
  #声明使用线路code查询排班数据结果个数
	declare countScheduling int default 0;
	#查询当前线路是否在排班表中使用
	select count(lineCode) countScheduling from scheduling where scheduling.lineCode = in_lineCode;
	#判断线路是否被使用
	if countScheduling = 0
	then
    #删除线路与站点关联关系
		delete from line_station_ref where line_station_ref.lineCode = in_lineCode;
		#删除线路
		delete from line where line.lineCode = in_lineCode;
		#返回成功信息编码 线路删除成功！
		set out_code = 1;
	else
	    #返回失败信息编码 线路数据已被排班，删除失败！
		set out_code = 0;
  end if;
END */$$
DELIMITER ;

/*Table structure for table `v_permission` */

DROP TABLE IF EXISTS `v_permission`;

/*!50001 DROP VIEW IF EXISTS `v_permission` */;
/*!50001 DROP TABLE IF EXISTS `v_permission` */;

/*!50001 CREATE TABLE  `v_permission`(
 `loginName` varchar(25) ,
 `permissionName` varchar(50) 
)*/;

/*View structure for view v_permission */

/*!50001 DROP TABLE IF EXISTS `v_permission` */;
/*!50001 DROP VIEW IF EXISTS `v_permission` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `v_permission` AS select distinct `sysuser`.`loginName` AS `loginName`,`permission`.`permissionName` AS `permissionName` from (((`sysuser` join `role` on((`sysuser`.`role` = `role`.`roleCode`))) join `role_permission_ref` on((`role_permission_ref`.`roleCode` = `role`.`roleCode`))) join `permission` on((`role_permission_ref`.`permissionCode` = `permission`.`permissionCode`))) */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
