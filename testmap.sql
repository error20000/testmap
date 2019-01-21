/*
Navicat MySQL Data Transfer

Source Server         : lj
Source Server Version : 50515
Source Host           : localhost:3306
Source Database       : testmap

Target Server Type    : MYSQL
Target Server Version : 50515
File Encoding         : 65001

Date: 2019-01-21 18:50:30
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of s_content
-- ----------------------------
INSERT INTO `s_content` VALUES ('1', '1', '2019-01-21 18:00:11', '', '', '0', '1', '');

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of s_user
-- ----------------------------
INSERT INTO `s_user` VALUES ('1', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin', '1');
INSERT INTO `s_user` VALUES ('2', 'test', '098f6bcd4621d373cade4e832627b4f6', 'test', '0');
