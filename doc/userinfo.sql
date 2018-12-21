/*
Navicat MySQL Data Transfer

Source Server         : hyht_demo
Source Server Version : 50634
Source Host           : rm-uf6ak4u0270j42slto.mysql.rds.aliyuncs.com:3306
Source Database       : hyht_demo

Target Server Type    : MYSQL
Target Server Version : 50634
File Encoding         : 65001

Date: 2018-12-19 22:15:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo` (
  `id` varchar(255) NOT NULL DEFAULT '',
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
INSERT INTO `userinfo` VALUES ('7463ecb6836111e88cba7cd30ae015b0', 'zhangxb', '5cmCD7oCI9xItH89iRWakuu0RL0lBkPe36jXI3d0w6A=');
