/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50625
 Source Host           : localhost:3306
 Source Schema         : demo_webank

 Target Server Type    : MySQL
 Target Server Version : 50625
 File Encoding         : 65001

 Date: 19/10/2020 23:32:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_customer
-- ----------------------------
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `gender` bit(1) NULL DEFAULT NULL COMMENT '性别(0: 女， 1: 男)',
  `age` tinyint(10) UNSIGNED NULL DEFAULT NULL COMMENT '年龄',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '住址',
  `data_status` bit(1) NOT NULL DEFAULT 1 COMMENT '有效标识(0: 无效, 1: 有效)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_customer
-- ----------------------------
INSERT INTO `t_customer` VALUES (1, '张三', b'1', 120, '13637855354', 'seanzhxi@126.com', '深圳市南山区', b'1');

CREATE TABLE `t_http_request` (
  `id` varchar(32) NOT NULL,
  `start_time` datetime DEFAULT NULL COMMENT '请求接收时间',
  `uri` varchar(150) DEFAULT NULL COMMENT '请求uri',
  `request_method` varchar(10) DEFAULT NULL COMMENT '请求方式',
  `request_ip` varchar(50) DEFAULT NULL COMMENT '请求ip',
  `exception_message` varchar(200) DEFAULT NULL COMMENT '异常返回信息',
  `duration` bigint(20) DEFAULT NULL COMMENT '请求持续时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `t_database_operate` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `request_id` varchar(32) DEFAULT NULL COMMENT '对应t_http_request表 id',
  `table_name` varchar(100) DEFAULT NULL COMMENT '操作表名',
  `operate_type` varchar(20) DEFAULT NULL COMMENT '操作类型(INSERT, UPDATE, DELETE)',
  `operate_sql` varchar(255) DEFAULT NULL COMMENT '操作的SQL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
