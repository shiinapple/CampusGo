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
