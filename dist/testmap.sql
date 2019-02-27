/*
Navicat MySQL Data Transfer

Source Server         : lj
Source Server Version : 50515
Source Host           : localhost:3306
Source Database       : testmap

Target Server Type    : MYSQL
Target Server Version : 50515
File Encoding         : 65001

Date: 2019-02-19 18:43:32
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
  `type` tinyint(4) DEFAULT '0' COMMENT '用户绘制类型',
  `path` text COMMENT '用户绘制区域',
  `option` varchar(255) DEFAULT '' COMMENT '用户选择的评价',
  `content` text COMMENT '用户自定义的评价',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of s_content
-- ----------------------------
-- ----------------------------
-- Table structure for `s_options`
-- ----------------------------
DROP TABLE IF EXISTS `s_options`;
CREATE TABLE `s_options` (
  `pid` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(255) DEFAULT '' COMMENT '名称',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态  0：禁用，1：启用',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of s_options
-- ----------------------------

-- ----------------------------
-- Table structure for `s_trace`
-- ----------------------------
DROP TABLE IF EXISTS `s_trace`;
CREATE TABLE `s_trace` (
  `pid` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user` int(11) DEFAULT '0' COMMENT '用户pid',
  `date` varchar(20) DEFAULT '' COMMENT '日期',
  `local` varchar(255) DEFAULT '' COMMENT '用户位置',
  `path` longtext COMMENT '追踪轨迹',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of s_trace
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
  `color` varchar(10) DEFAULT '' COMMENT '颜色',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of s_user
-- ----------------------------
INSERT INTO `s_user` VALUES ('1', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin', '1', '#000000');
INSERT INTO `s_user` VALUES ('2', 'test', '098f6bcd4621d373cade4e832627b4f6', 'test', '0', '#9A5959');
