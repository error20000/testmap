/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50560
Source Host           : localhost:3306
Source Database       : testmap

Target Server Type    : MYSQL
Target Server Version : 50560
File Encoding         : 65001

Date: 2019-01-22 22:08:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `s_content`
-- ----------------------------
DROP TABLE IF EXISTS `s_content`;
CREATE TABLE `s_content` (
  `pid` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user` int(11) DEFAULT '0' COMMENT '用户pid',
  `date` varchar(20) DEFAULT '' COMMENT '日期',
  `local` varchar(255) DEFAULT '' COMMENT '用户位置',
  `path` text COMMENT '用户绘制区域',
  `type` tinyint(4) DEFAULT '0' COMMENT '用户评价类型',
  `option` varchar(10) DEFAULT '' COMMENT '用户选择的评价',
  `content` varchar(255) DEFAULT '' COMMENT '用户自定义的评价',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of s_content
-- ----------------------------

-- ----------------------------
-- Table structure for `s_user`
-- ----------------------------
DROP TABLE IF EXISTS `s_user`;
CREATE TABLE `s_user` (
  `pid` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `username` varchar(20) DEFAULT '' COMMENT '用户名',
  `password` varchar(32) DEFAULT '' COMMENT '密码',
  `nick` varchar(20) DEFAULT '' COMMENT '昵称',
  `admin` tinyint(4) DEFAULT '0' COMMENT '超管  0：否，1：是',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of s_user
-- ----------------------------
INSERT INTO `s_user` VALUES ('1', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin', '1');
INSERT INTO `s_user` VALUES ('2', 'test', '098f6bcd4621d373cade4e832627b4f6', 'test', '0');
INSERT INTO `s_user` VALUES ('3', 'test1', '098f6bcd4621d373cade4e832627b4f6', 'test1', '0');
INSERT INTO `s_user` VALUES ('4', 'test2', '098f6bcd4621d373cade4e832627b4f6', 'test2', '0');
INSERT INTO `s_user` VALUES ('5', 'test3', '098f6bcd4621d373cade4e832627b4f6', 'test3', '0');
INSERT INTO `s_user` VALUES ('6', 'test4', '098f6bcd4621d373cade4e832627b4f6', 'test4', '0');
INSERT INTO `s_user` VALUES ('7', 'test5', '', '', '0');
