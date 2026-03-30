SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE database if NOT EXISTS `xfg_frame_archetype` default character set utf8mb4 collate utf8mb4_0900_ai_ci;
use `xfg_frame_archetype`;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(64) NOT NULL COMMENT '用户唯一标识',
  `open_id` varchar(128) NOT NULL COMMENT '微信OpenID',
  `display_name` varchar(64) DEFAULT NULL COMMENT '显示名称',
  `wechat_id` varchar(64) DEFAULT NULL COMMENT '微信号',
  `phone` varchar(16) DEFAULT NULL COMMENT '手机号',
  `avatar_url` varchar(256) DEFAULT NULL COMMENT '头像地址',
  `verified` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否认证 (0:未认证, 1:已认证)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`user_id`),
  UNIQUE KEY `idx_open_id` (`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';


-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
                         `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                         `order_id` varchar(64) NOT NULL COMMENT '订单唯一标识',
                         `type` varchar(32) NOT NULL COMMENT '订单类型 (快递, 外卖)',
                         `status` varchar(32) NOT NULL DEFAULT 'new' COMMENT '订单状态 (new:待抢单, grabbed:已抢单, completed:已完成)',
                         `reward` decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '赏金',
                         `time_limit` varchar(64) DEFAULT NULL COMMENT '时间限制 (如:30分钟)',
                         `pickup_location` varchar(256) DEFAULT NULL COMMENT '取货地点',
                         `dropoff_location` varchar(256) DEFAULT NULL COMMENT '送货地点',
                         `remarks` text DEFAULT NULL COMMENT '备注信息',
                         `creator_user_id` varchar(64) NOT NULL COMMENT '发单人ID',
                         `taker_user_id` varchar(64) DEFAULT NULL COMMENT '抢单人ID',
                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `idx_order_id` (`order_id`),
                         KEY `idx_type_status` (`type`, `status`),
                         KEY `idx_creator_user_id` (`creator_user_id`),
                         KEY `idx_taker_user_id` (`taker_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单信息表';

-- ----------------------------
-- 测试数据 (可选)
-- ----------------------------
INSERT INTO `order` (`order_id`, `type`, `status`, `reward`, `time_limit`, `pickup_location`, `dropoff_location`, `remarks`, `creator_user_id`)
VALUES ('O1001', '快递', 'new', 2.00, '30分钟', '学子超市', '图书馆', '取件码 1234', 'system');


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 创建并使用数据库
CREATE DATABASE IF NOT EXISTS `xfg_frame_archetype` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `xfg_frame_archetype`;

-- 2. 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                        `user_id` varchar(64) NOT NULL COMMENT '用户唯一标识',
                        `open_id` varchar(128) NOT NULL COMMENT '微信OpenID',
                        `display_name` varchar(64) DEFAULT NULL COMMENT '显示名称',
                        `wechat_id` varchar(64) DEFAULT NULL COMMENT '微信号',
                        `phone` varchar(16) DEFAULT NULL COMMENT '手机号',
                        `avatar_url` varchar(256) DEFAULT NULL COMMENT '头像地址',
                        `verified` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否认证 (0:未认证, 1:已认证)',
                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `idx_user_id` (`user_id`),
                        UNIQUE KEY `idx_open_id` (`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- 3. 订单表
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
                         `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                         `order_id` varchar(64) NOT NULL COMMENT '订单唯一标识',
                         `type` varchar(32) NOT NULL COMMENT '订单类型 (快递, 外卖)',
                         `status` varchar(32) NOT NULL DEFAULT 'new' COMMENT '订单状态 (new:待抢单, grabbed:已抢单, completed:已完成)',
                         `reward` decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '赏金',
                         `time_limit` varchar(64) DEFAULT NULL COMMENT '时间限制',
                         `pickup_location` varchar(256) DEFAULT NULL COMMENT '取货地点',
                         `dropoff_location` varchar(256) DEFAULT NULL COMMENT '送货地点',
                         `remarks` text DEFAULT NULL COMMENT '备注信息',
                         `creator_user_id` varchar(64) NOT NULL COMMENT '发单人ID',
                         `taker_user_id` varchar(64) DEFAULT NULL COMMENT '抢单人ID',
                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `idx_order_id` (`order_id`),
                         KEY `idx_type_status` (`type`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单信息表';

-- 4. 地址表
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
                           `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                           `address_id` varchar(64) NOT NULL COMMENT '地址唯一标识',
                           `user_id` varchar(64) NOT NULL COMMENT '用户ID',
                           `name` varchar(128) NOT NULL COMMENT '地址名称',
                           `detail` varchar(256) DEFAULT NULL COMMENT '详细地址',
                           `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认 (0:否, 1:是)',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `idx_address_id` (`address_id`),
                           KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户地址表';

-- 5. 初始测试数据
INSERT INTO `order` (`order_id`, `type`, `status`, `reward`, `time_limit`, `pickup_location`, `dropoff_location`, `remarks`, `creator_user_id`)
VALUES ('O1001', '快递', 'new', 2.00, '30分钟', '学子超市', '图书馆', '取件码 1234', 'system');

SET FOREIGN_KEY_CHECKS = 1;