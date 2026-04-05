/*
 Navicat Premium Data Transfer

 Source Server         : mysql-8.0-fun
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : furniture_sales_system

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 06/04/2026 03:33:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `start_time` datetime(0) NOT NULL,
  `end_time` datetime(0) NOT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'flash_sale: 限时秒杀, discount: 满减活动, new_arrival: 新品推荐',
  `status` int(11) NULL DEFAULT 1,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity
-- ----------------------------
INSERT INTO `activity` VALUES (2, '秒杀', '', '2026-03-18 16:00:00', '2026-03-31 16:00:00', 'flash_sale', 1, '2026-03-17 07:43:28', '2026-03-25 02:24:50');
INSERT INTO `activity` VALUES (3, '大促销', '', '2026-03-16 16:00:00', '2026-03-31 16:00:00', 'discount', 1, '2026-03-18 11:55:17', '2026-03-25 02:24:42');
INSERT INTO `activity` VALUES (4, '推荐新品', '', '2026-03-17 16:00:00', '2026-04-01 00:00:00', 'new_arrival', 1, '2026-03-19 01:39:36', '2026-03-25 02:24:32');

-- ----------------------------
-- Table structure for activity_product
-- ----------------------------
DROP TABLE IF EXISTS `activity_product`;
CREATE TABLE `activity_product`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `activity_price` decimal(10, 2) NULL DEFAULT NULL,
  `sort_order` int(11) NULL DEFAULT 0,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_activity`(`activity_id`) USING BTREE,
  INDEX `idx_product`(`product_id`) USING BTREE,
  CONSTRAINT `activity_product_ibfk_1` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `activity_product_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_product
-- ----------------------------
INSERT INTO `activity_product` VALUES (12, 4, 13, 2300.00, NULL, '2026-03-25 02:24:32');
INSERT INTO `activity_product` VALUES (13, 3, 16, 199.00, NULL, '2026-03-25 02:24:42');
INSERT INTO `activity_product` VALUES (14, 3, 14, 299.00, NULL, '2026-03-25 02:24:42');
INSERT INTO `activity_product` VALUES (15, 3, 12, 588.00, NULL, '2026-03-25 02:24:42');
INSERT INTO `activity_product` VALUES (16, 2, 9, 400.00, NULL, '2026-03-25 02:24:50');
INSERT INTO `activity_product` VALUES (17, 2, 3, 1000.00, NULL, '2026-03-25 02:24:50');

-- ----------------------------
-- Table structure for brand
-- ----------------------------
DROP TABLE IF EXISTS `brand`;
CREATE TABLE `brand`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT 1,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of brand
-- ----------------------------
INSERT INTO `brand` VALUES (6, '宜家', 'http://localhost:8080/images/brand_ikea.png', '全球知名家具品牌', 1, '2026-03-13 18:54:36', '2026-03-13 18:54:36');
INSERT INTO `brand` VALUES (7, '全友家居', 'http://localhost:8080/images/brand_quanyou.png', '中国家具品牌', 1, '2026-03-13 18:54:36', '2026-03-13 18:54:36');
INSERT INTO `brand` VALUES (8, '顾家家居', 'http://localhost:8080/images/brand_kujia.png', '沙发品牌', 1, '2026-03-13 18:54:36', '2026-03-13 18:54:36');
INSERT INTO `brand` VALUES (9, '林氏木业', 'http://localhost:8080/images/brand_linshi.png', '现代家具品牌', 1, '2026-03-13 18:54:36', '2026-03-13 18:54:36');
INSERT INTO `brand` VALUES (10, '红苹果', 'http://localhost:8080/images/brand_redapple.png', '高端家具品牌', 1, '2026-03-13 18:54:36', '2026-03-13 18:54:36');

-- ----------------------------
-- Table structure for carousel
-- ----------------------------
DROP TABLE IF EXISTS `carousel`;
CREATE TABLE `carousel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `product_id` int(11) NOT NULL,
  `sort_order` int(11) NULL DEFAULT 0,
  `status` int(11) NULL DEFAULT 1,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product`(`product_id`) USING BTREE,
  CONSTRAINT `carousel_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of carousel
-- ----------------------------
INSERT INTO `carousel` VALUES (16, '办公椅', '', 'http://localhost:8080/images/1b77d7ed-f904-4ec6-a9a4-444f94e24d5d.png', 1, 0, 1, '2026-03-18 02:38:13', '2026-03-18 02:38:13');
INSERT INTO `carousel` VALUES (17, '餐椅', '现代餐椅', 'http://localhost:8080/images/6ec5c6df-b22b-4132-89eb-ddd468279de0.png', 14, 0, 1, '2026-03-18 02:40:42', '2026-03-18 02:40:42');
INSERT INTO `carousel` VALUES (18, '沙发', '', 'http://localhost:8080/images/1c8386be-4a41-4687-9e4b-f9344618d9c5.png', 3, 0, 1, '2026-03-18 02:40:46', '2026-03-18 02:40:46');
INSERT INTO `carousel` VALUES (19, '餐桌', '', 'http://localhost:8080/images/b265d34f-6d53-4ba8-b0c6-f0a6f1040aaa.png', 10, 0, 1, '2026-03-18 02:40:52', '2026-03-18 02:40:52');
INSERT INTO `carousel` VALUES (20, '衣柜', '推拉门衣柜', 'http://localhost:8080/images/e14b9a94-6eb0-4282-bd77-be125d052516.png', 13, 0, 1, '2026-03-18 02:40:57', '2026-03-18 02:40:57');
INSERT INTO `carousel` VALUES (25, '床头柜', '', 'http://localhost:8080/images/b5cb4aa1-fcd8-453a-bbd9-232c3cfbd384.png', 16, 0, 1, '2026-03-18 02:52:56', '2026-03-18 02:52:56');

-- ----------------------------
-- Table structure for cart_item
-- ----------------------------
DROP TABLE IF EXISTS `cart_item`;
CREATE TABLE `cart_item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `selected` int(11) NULL DEFAULT 1,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user`(`user_id`) USING BTREE,
  INDEX `idx_product`(`product_id`) USING BTREE,
  CONSTRAINT `cart_item_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cart_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart_item
-- ----------------------------
INSERT INTO `cart_item` VALUES (42, 7, 12, 1, 1, '2026-03-23 14:59:17', '2026-03-23 14:59:17');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `parent_id` int(11) NULL DEFAULT 0,
  `sort_order` int(11) NULL DEFAULT 0,
  `status` int(11) NULL DEFAULT 1,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (13, '办公家居', NULL, NULL, NULL, NULL, '2026-03-13 07:14:16', NULL);
INSERT INTO `category` VALUES (14, '客厅家居', NULL, NULL, NULL, NULL, '2026-03-13 07:47:13', NULL);
INSERT INTO `category` VALUES (15, '餐厅家居', NULL, NULL, NULL, NULL, '2026-03-13 08:55:21', '2026-03-25 10:40:25');
INSERT INTO `category` VALUES (16, '卧室家居', NULL, NULL, NULL, NULL, '2026-03-13 08:55:43', NULL);
INSERT INTO `category` VALUES (17, '厨房家居', NULL, NULL, NULL, NULL, '2026-03-25 03:15:16', NULL);
INSERT INTO `category` VALUES (18, '卫生间家居', NULL, NULL, NULL, NULL, '2026-03-25 03:15:31', NULL);
INSERT INTO `category` VALUES (19, '阳台家居', NULL, NULL, NULL, NULL, '2026-03-25 03:15:40', NULL);
INSERT INTO `category` VALUES (20, '智能家居', NULL, NULL, NULL, NULL, '2026-03-25 03:15:48', NULL);
INSERT INTO `category` VALUES (21, '装饰家居', NULL, NULL, NULL, NULL, '2026-03-25 03:15:56', NULL);
INSERT INTO `category` VALUES (22, '收纳家居', NULL, NULL, NULL, NULL, '2026-03-25 03:16:07', NULL);

-- ----------------------------
-- Table structure for customer_service_message
-- ----------------------------
DROP TABLE IF EXISTS `customer_service_message`;
CREATE TABLE `customer_service_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` int(11) NULL DEFAULT 0,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_created_at`(`created_at`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer_service_message
-- ----------------------------
INSERT INTO `customer_service_message` VALUES (1, 4, '用户', '1', 1, '2026-03-31 07:19:29');
INSERT INTO `customer_service_message` VALUES (2, 4, '用户', '1', 1, '2026-04-04 11:24:26');
INSERT INTO `customer_service_message` VALUES (3, 0, '客服', '你好', 0, '2026-04-04 11:29:11');
INSERT INTO `customer_service_message` VALUES (4, 5, 'user1', '1', 1, '2026-04-05 17:26:26');
INSERT INTO `customer_service_message` VALUES (5, 5, 'user1', '1', 1, '2026-04-05 17:46:51');
INSERT INTO `customer_service_message` VALUES (6, 5, 'user1', '1', 1, '2026-04-05 17:50:25');
INSERT INTO `customer_service_message` VALUES (7, 5, 'user1', '1', 1, '2026-04-05 18:31:31');
INSERT INTO `customer_service_message` VALUES (8, 5, 'user1', '您好，我想咨询关于订单 0398b0ec39664407a359f394a6a048f9 的问题。\n订单金额：¥1588\n下单时间：2026-03-20 20:11', 1, '2026-04-05 18:52:59');
INSERT INTO `customer_service_message` VALUES (9, 5, 'user1', '您好，我想咨询关于订单 0398b0ec39664407a359f394a6a048f9 的问题。\n订单金额：¥1588\n下单时间：2026-03-20 20:11', 1, '2026-04-05 19:23:43');

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort_order` int(11) NULL DEFAULT 0,
  `status` int(11) NULL DEFAULT 1,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dict
-- ----------------------------
INSERT INTO `dict` VALUES (1, 'order_status', '0', '待支付', 0, 1, '2026-03-14 10:18:09', '2026-03-14 10:18:09');
INSERT INTO `dict` VALUES (2, 'order_status', '1', '待发货', 1, 1, '2026-03-14 10:18:09', '2026-03-14 10:18:09');
INSERT INTO `dict` VALUES (3, 'order_status', '2', '待收货', 2, 1, '2026-03-14 10:18:09', '2026-03-14 10:18:09');
INSERT INTO `dict` VALUES (4, 'order_status', '3', '已完成', 3, 1, '2026-03-14 10:18:09', '2026-03-14 10:18:09');
INSERT INTO `dict` VALUES (5, 'order_status', '4', '已取消', 4, 1, '2026-03-14 10:18:09', '2026-03-14 10:18:09');
INSERT INTO `dict` VALUES (6, 'payment_method', 'alipay', '支付宝', 0, 1, '2026-03-14 10:18:09', '2026-03-14 10:18:09');
INSERT INTO `dict` VALUES (7, 'payment_method', 'wechat', '微信支付', 1, 1, '2026-03-14 10:18:09', '2026-03-14 10:18:09');
INSERT INTO `dict` VALUES (8, 'payment_method', 'card', '银行卡', 2, 1, '2026-03-14 10:18:09', '2026-03-14 10:18:09');

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `product_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10, 2) NULL DEFAULT NULL,
  `subtotal` decimal(10, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order`(`order_id`) USING BTREE,
  INDEX `idx_product`(`product_id`) USING BTREE,
  CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 125 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (56, '202603130003', 3, '沙发', 'http://localhost:8080/images/e7415cb3-914a-4054-a063-3b68dbe0f318.png', 2, 1999.00, NULL);
INSERT INTO `order_item` VALUES (82, '202603130001', 14, '餐椅', 'http://localhost:8080/images/6ec5c6df-b22b-4132-89eb-ddd468279de0.png', 1, 399.00, NULL);
INSERT INTO `order_item` VALUES (83, '202603130001', 1, '办公椅', 'http://localhost:8080/images/1b77d7ed-f904-4ec6-a9a4-444f94e24d5d.png', 2, 400.00, NULL);
INSERT INTO `order_item` VALUES (84, '202603130001', 10, '餐桌', 'http://localhost:8080/images/b265d34f-6d53-4ba8-b0c6-f0a6f1040aaa.png', 1, 1500.00, NULL);
INSERT INTO `order_item` VALUES (85, '407f1dd0d2304c77b5a5a794bc538b1c', 9, '实木床', 'http://localhost:8080/images/854af08d-becb-418c-bb8b-3845e5b07429.png', 1, 400.00, NULL);
INSERT INTO `order_item` VALUES (86, '407f1dd0d2304c77b5a5a794bc538b1c', 16, '床头柜', 'http://localhost:8080/images/b5cb4aa1-fcd8-453a-bbd9-232c3cfbd384.png', 1, 199.00, NULL);
INSERT INTO `order_item` VALUES (89, '44259c8f6ca0434f84ad705b4d0f5018', 9, '实木床', 'http://localhost:8080/images/854af08d-becb-418c-bb8b-3845e5b07429.png', 1, 400.00, NULL);
INSERT INTO `order_item` VALUES (90, '44259c8f6ca0434f84ad705b4d0f5018', 16, '床头柜', 'http://localhost:8080/images/b5cb4aa1-fcd8-453a-bbd9-232c3cfbd384.png', 1, 199.00, NULL);
INSERT INTO `order_item` VALUES (93, 'd431bae6f0e64cfcb15435b036eebd3e', 9, '实木床', 'http://localhost:8080/images/854af08d-becb-418c-bb8b-3845e5b07429.png', 1, 400.00, NULL);
INSERT INTO `order_item` VALUES (94, 'd431bae6f0e64cfcb15435b036eebd3e', 16, '床头柜', 'http://localhost:8080/images/b5cb4aa1-fcd8-453a-bbd9-232c3cfbd384.png', 1, 199.00, NULL);
INSERT INTO `order_item` VALUES (97, '0398b0ec39664407a359f394a6a048f9', 3, '沙发', 'http://localhost:8080/images/1c8386be-4a41-4687-9e4b-f9344618d9c5.png', 1, 1000.00, NULL);
INSERT INTO `order_item` VALUES (98, '0398b0ec39664407a359f394a6a048f9', 12, '茶几', 'http://localhost:8080/images/45d0dae9-a6bb-4817-bac1-a69c0d0c900d.png', 1, 588.00, NULL);
INSERT INTO `order_item` VALUES (100, '6cfcfc469b5944bcad312b7aa1cc4965', 14, '餐椅', 'http://localhost:8080/images/6ec5c6df-b22b-4132-89eb-ddd468279de0.png', 3, 299.00, NULL);
INSERT INTO `order_item` VALUES (101, 'b1564ceb934f4ceeb2bf3bf9d778f82e', 14, '餐椅', 'http://localhost:8080/images/6ec5c6df-b22b-4132-89eb-ddd468279de0.png', 2, 299.00, NULL);
INSERT INTO `order_item` VALUES (104, 'a2124b507a3a41b6b7283d02b647e05f', 16, '床头柜', 'http://localhost:8080/images/b5cb4aa1-fcd8-453a-bbd9-232c3cfbd384.png', 1, 199.00, NULL);
INSERT INTO `order_item` VALUES (105, 'a2124b507a3a41b6b7283d02b647e05f', 1, '办公椅', 'http://localhost:8080/images/1b77d7ed-f904-4ec6-a9a4-444f94e24d5d.png', 1, 400.00, NULL);
INSERT INTO `order_item` VALUES (109, '92c828ab3e314764ae294831072f264c', 10, '餐桌', 'http://localhost:8080/images/b265d34f-6d53-4ba8-b0c6-f0a6f1040aaa.png', 1, 1500.00, NULL);
INSERT INTO `order_item` VALUES (110, '92c828ab3e314764ae294831072f264c', 9, '实木床', 'http://localhost:8080/images/854af08d-becb-418c-bb8b-3845e5b07429.png', 1, 400.00, NULL);
INSERT INTO `order_item` VALUES (111, '92c828ab3e314764ae294831072f264c', 3, '沙发', 'http://localhost:8080/images/1c8386be-4a41-4687-9e4b-f9344618d9c5.png', 1, 1000.00, NULL);
INSERT INTO `order_item` VALUES (112, '1609459760ff441ea98607c32462c181', 13, '衣柜', 'http://localhost:8080/images/e14b9a94-6eb0-4282-bd77-be125d052516.png', 1, 2300.00, NULL);
INSERT INTO `order_item` VALUES (113, '202603130002', 14, '餐椅', 'http://localhost:8080/images/6ec5c6df-b22b-4132-89eb-ddd468279de0.png', 1, 399.00, NULL);
INSERT INTO `order_item` VALUES (116, 'cabcfbba7c4c427cb71cbfaf67958252', 13, '衣柜', 'http://localhost:8080/images/e14b9a94-6eb0-4282-bd77-be125d052516.png', 1, 2300.00, NULL);
INSERT INTO `order_item` VALUES (117, 'cabcfbba7c4c427cb71cbfaf67958252', 12, '茶几', 'http://localhost:8080/images/45d0dae9-a6bb-4817-bac1-a69c0d0c900d.png', 1, 588.00, NULL);
INSERT INTO `order_item` VALUES (118, '232ef21052ac47bab0db1e3b180c7cc0', 14, '餐椅', 'http://localhost:8080/images/6ec5c6df-b22b-4132-89eb-ddd468279de0.png', 1, 299.00, NULL);
INSERT INTO `order_item` VALUES (121, 'b65ad1fee6594b9b87da417c5be8a2c2', 11, '书柜', 'http://localhost:8080/images/084ef819-96f1-4c49-b9b6-f8cdbe133499.png', 1, 999.00, NULL);
INSERT INTO `order_item` VALUES (122, 'b65ad1fee6594b9b87da417c5be8a2c2', 13, '衣柜', 'http://localhost:8080/images/e14b9a94-6eb0-4282-bd77-be125d052516.png', 1, 2300.00, NULL);
INSERT INTO `order_item` VALUES (124, '9df9910efdf543a2b614a555c4b804c2', 16, '床头柜', 'http://localhost:8080/images/b5cb4aa1-fcd8-453a-bbd9-232c3cfbd384.png', 1, 199.00, NULL);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  `total_price` decimal(10, 2) NOT NULL,
  `actual_price` decimal(10, 2) NOT NULL,
  `status` int(11) NOT NULL,
  `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `payment_time` datetime(0) NULL DEFAULT NULL,
  `shipping_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shipping_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shipping_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shipping_time` datetime(0) NULL DEFAULT NULL,
  `finish_time` datetime(0) NULL DEFAULT NULL,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user`(`user_id`) USING BTREE,
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES ('0398b0ec39664407a359f394a6a048f9', 5, 1588.00, 1588.00, 4, 'alipay', NULL, '张三 13811111111, 广东省深圳市南山区科技园1号', NULL, NULL, NULL, NULL, '2026-03-20 12:11:05', '2026-03-21 09:57:12');
INSERT INTO `orders` VALUES ('1609459760ff441ea98607c32462c181', 6, 2300.00, 2300.00, 4, 'alipay', NULL, '李四 13822222222, 广东省广州市天河区体育西路100号', '李四', '13822222222', NULL, NULL, '2026-03-21 07:45:09', '2026-03-21 16:14:41');
INSERT INTO `orders` VALUES ('202603130001', 5, 2699.00, 2699.00, 3, '支付宝', NULL, '深圳市南山区科技园1号', '张三', '13811111111', NULL, NULL, '2026-03-13 18:54:51', '2026-03-19 12:17:33');
INSERT INTO `orders` VALUES ('202603130002', 6, 399.00, 399.00, 3, '微信', NULL, '广州市天河区体育西路100号', '李四', '13822222222', NULL, '2026-03-14 11:06:44', '2026-03-13 18:54:51', '2026-03-21 08:12:03');
INSERT INTO `orders` VALUES ('202603130003', 7, 3998.00, 3998.00, 3, '支付宝', NULL, '杭州市西湖区文三路88号', '王五', '13833333333', NULL, '2026-03-14 11:06:44', '2026-03-13 18:54:51', '2026-03-14 08:35:30');
INSERT INTO `orders` VALUES ('232ef21052ac47bab0db1e3b180c7cc0', 6, 299.00, 299.00, 0, 'alipay', NULL, '李四 13822222222, 广东省广州市天河区体育西路100号', '李四', '13822222222', NULL, NULL, '2026-03-21 08:18:30', '2026-03-21 08:18:30');
INSERT INTO `orders` VALUES ('407f1dd0d2304c77b5a5a794bc538b1c', 5, 599.00, 599.00, 4, 'alipay', NULL, '张三 13811111111, 广东省深圳市南山区科技园1号', NULL, NULL, NULL, NULL, '2026-03-20 08:22:56', '2026-03-20 16:31:00');
INSERT INTO `orders` VALUES ('44259c8f6ca0434f84ad705b4d0f5018', 5, 599.00, 599.00, 3, 'alipay', NULL, '张三 13811111111, 广东省深圳市南山区科技园1号', NULL, NULL, NULL, NULL, '2026-03-20 08:27:01', '2026-03-20 16:45:41');
INSERT INTO `orders` VALUES ('6cfcfc469b5944bcad312b7aa1cc4965', 5, 897.00, 897.00, 3, 'alipay', NULL, '张三 13811111111, 北京市北京市朝阳区中南海', NULL, NULL, NULL, NULL, '2026-03-21 01:40:55', '2026-03-24 10:26:27');
INSERT INTO `orders` VALUES ('92c828ab3e314764ae294831072f264c', 9, 2900.00, 2900.00, 2, 'wechat', NULL, '小乔 13800138000, 上海市上海市浦东新区浦东', '小乔', '13800138000', NULL, NULL, '2026-03-21 06:44:16', '2026-03-21 14:49:14');
INSERT INTO `orders` VALUES ('9df9910efdf543a2b614a555c4b804c2', 5, 199.00, 199.00, 3, 'alipay', NULL, '张三 13811111111, 北京市北京市朝阳区中南海', '张三', '13811111111', NULL, NULL, '2026-03-24 02:01:33', '2026-03-24 10:26:28');
INSERT INTO `orders` VALUES ('a2124b507a3a41b6b7283d02b647e05f', 9, 599.00, 599.00, 2, 'alipay', NULL, '天天 13031561651, 北京市北京市朝阳区中南海', '天天', '13031561651', NULL, NULL, '2026-03-21 03:32:15', '2026-03-21 14:49:15');
INSERT INTO `orders` VALUES ('b1564ceb934f4ceeb2bf3bf9d778f82e', 5, 598.00, 598.00, 0, 'alipay', NULL, '张三 13811111111, 广东省深圳市南山区科技园1号', '张三', '13811111111', NULL, NULL, '2026-03-21 02:14:18', '2026-03-21 02:14:18');
INSERT INTO `orders` VALUES ('b65ad1fee6594b9b87da417c5be8a2c2', 10, 3299.00, 3299.00, 2, 'alipay', NULL, '张三 13811111111, 广东省深圳市南山区科技园1号', '张三', '13811111111', NULL, NULL, '2026-03-23 15:08:04', '2026-03-23 23:11:27');
INSERT INTO `orders` VALUES ('cabcfbba7c4c427cb71cbfaf67958252', 6, 2888.00, 2888.00, 2, 'alipay', NULL, '李四 13822222222, 广东省广州市天河区体育西路100号', '李四', '13822222222', NULL, NULL, '2026-03-21 08:15:12', '2026-03-21 16:15:56');
INSERT INTO `orders` VALUES ('d431bae6f0e64cfcb15435b036eebd3e', 5, 599.00, 599.00, 3, 'alipay', NULL, '张三 13811111111, 广东省深圳市南山区科技园1号', NULL, NULL, NULL, NULL, '2026-03-20 08:44:40', '2026-03-20 16:45:43');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `category_id` int(11) NOT NULL,
  `brand_id` int(11) NULL DEFAULT NULL,
  `price` decimal(10, 2) NOT NULL,
  `original_price` decimal(10, 2) NULL DEFAULT NULL,
  `stock` int(11) NOT NULL,
  `sales` int(11) NULL DEFAULT 0,
  `rating` decimal(3, 2) NULL DEFAULT 5.00,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `specs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `status` int(11) NULL DEFAULT 1,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `size` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '产品尺寸，如120x60x80cm',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category`(`category_id`) USING BTREE,
  INDEX `idx_brand`(`brand_id`) USING BTREE,
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `product_ibfk_2` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, '办公椅', 13, NULL, 400.00, NULL, 57, 3, 5.00, '', NULL, 'http://localhost:8080/images/1b77d7ed-f904-4ec6-a9a4-444f94e24d5d.png', NULL, NULL, '2026-03-13 07:22:59', '2026-03-21 11:32:15', NULL);
INSERT INTO `product` VALUES (2, '办公桌', 13, NULL, 1200.00, NULL, 20, 0, 5.00, '办公用品', NULL, 'http://localhost:8080/images/84faa67d-fa80-452d-af6a-da9428431c70.png', NULL, NULL, '2026-03-13 07:36:29', '2026-03-21 16:12:03', NULL);
INSERT INTO `product` VALUES (3, '沙发', 14, NULL, 1999.00, NULL, 57, 3, 5.00, '', NULL, 'http://localhost:8080/images/1c8386be-4a41-4687-9e4b-f9344618d9c5.png', NULL, NULL, '2026-03-13 07:47:36', '2026-03-21 14:44:15', NULL);
INSERT INTO `product` VALUES (9, '实木床', 14, NULL, 700.00, NULL, 17, 3, 5.00, '', NULL, 'http://localhost:8080/images/854af08d-becb-418c-bb8b-3845e5b07429.png', NULL, NULL, '2026-03-13 08:08:56', '2026-03-21 14:44:15', NULL);
INSERT INTO `product` VALUES (10, '餐桌', 15, NULL, 1500.00, NULL, 13, 2, 5.00, '', NULL, 'http://localhost:8080/images/b265d34f-6d53-4ba8-b0c6-f0a6f1040aaa.png', NULL, NULL, '2026-03-18 02:08:14', '2026-03-21 14:44:15', NULL);
INSERT INTO `product` VALUES (11, '书柜', 13, 6, 999.00, 1199.00, 24, 1, 4.70, '多层书柜', '尺寸: 180x80cm, 材质: 人造板', 'http://localhost:8080/images/084ef819-96f1-4c49-b9b6-f8cdbe133499.png', NULL, 1, '2026-03-14 10:00:00', '2026-03-23 23:08:03', NULL);
INSERT INTO `product` VALUES (12, '茶几', 14, 7, 799.00, 999.00, 39, 1, 4.90, '现代茶几', '尺寸: 120x60cm, 材质: 岩板', 'http://localhost:8080/images/45d0dae9-a6bb-4817-bac1-a69c0d0c900d.png', NULL, 1, '2026-03-14 10:00:00', '2026-03-21 16:15:12', NULL);
INSERT INTO `product` VALUES (13, '衣柜', 16, 8, 2499.00, 2999.00, 13, 2, 4.60, '推拉门衣柜', '尺寸: 200x60cm, 材质: 密度板', 'http://localhost:8080/images/e14b9a94-6eb0-4282-bd77-be125d052516.png', NULL, 1, '2026-03-14 10:00:00', '2026-03-23 23:08:03', NULL);
INSERT INTO `product` VALUES (14, '餐椅', 15, 9, 399.00, 499.00, 52, 8, 4.50, '现代餐椅', '颜色: 灰色, 材质: 布艺', 'http://localhost:8080/images/6ec5c6df-b22b-4132-89eb-ddd468279de0.png', NULL, 1, '2026-03-14 10:00:00', '2026-03-21 16:18:29', NULL);
INSERT INTO `product` VALUES (16, '床头柜', 16, NULL, 300.00, NULL, 16, 4, 5.00, '', NULL, 'http://localhost:8080/images/b5cb4aa1-fcd8-453a-bbd9-232c3cfbd384.png', NULL, NULL, '2026-03-18 02:50:57', '2026-03-24 10:01:33', NULL);

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `rating` int(11) NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `status` int(11) NULL DEFAULT 1,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  INDEX `order_id`(`order_id`) USING BTREE,
  CONSTRAINT `review_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `review_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `review_ibfk_3` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review` VALUES (1, 5, 1, '202603130001', 5, '椅子质量很好，坐着很舒服', NULL, 1, '2026-03-14 10:18:09', '2026-03-14 10:18:09');
INSERT INTO `review` VALUES (3, 7, 3, '202603130003', 5, '沙发很柔软，款式时尚', NULL, 1, '2026-03-14 10:18:09', '2026-03-14 10:18:09');
INSERT INTO `review` VALUES (11, 5, 9, 'd431bae6f0e64cfcb15435b036eebd3e', 3, '一般', '', 1, '2026-03-20 09:22:51', '2026-03-20 09:22:51');
INSERT INTO `review` VALUES (13, 6, 16, NULL, 4, '还行，质量一般，发货很快', '[]', 1, '2026-03-21 08:39:13', '2026-03-21 08:39:41');
INSERT INTO `review` VALUES (14, 5, 11, NULL, 5, '好，质量不错。非常喜欢这个柜子。', '', 1, '2026-03-23 14:41:56', '2026-03-23 14:41:56');

-- ----------------------------
-- Table structure for sales_statistics
-- ----------------------------
DROP TABLE IF EXISTS `sales_statistics`;
CREATE TABLE `sales_statistics`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stat_date` date NOT NULL,
  `order_count` int(11) NULL DEFAULT 0,
  `product_count` int(11) NULL DEFAULT 0,
  `total_sales` decimal(12, 2) NULL DEFAULT NULL,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_date`(`stat_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sales_statistics
-- ----------------------------
INSERT INTO `sales_statistics` VALUES (1, '2026-03-13', 3, 3, 3599.00, '2026-03-14 10:18:09');
INSERT INTO `sales_statistics` VALUES (2, '2026-03-12', 2, 2, 1600.00, '2026-03-14 10:18:09');
INSERT INTO `sales_statistics` VALUES (3, '2026-03-11', 1, 1, 400.00, '2026-03-14 10:18:09');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'USER',
  `status` int(11) NULL DEFAULT 1,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (4, 'admin', '$2a$10$LtCPc7KYycJi3YDe4JEfF.YgKMHIWlWiLUA5.qIySe7sCieoXCzr.', 'wb liu', '11111111111', '1308631536@qq.com', '巢湖', 'ADMIN', 1, '/avatars/1773655699510-微信图片_20230213132631.jpg', '2026-03-13 07:08:11', '2026-03-16 10:08:21');
INSERT INTO `user` VALUES (5, 'user1', '$2a$10$F/tw/TcB9OrVHgVYUMAbf.ujIsvLpi7R3yYIxNk2/PB281i/8dVJK', '张三', '1301111111', 'user1@example.com', '广东省深圳市南山区', 'USER', 1, '/avatars/1773919567765-QQ图片20231026204125.jpg', '2026-03-13 18:54:36', '2026-03-19 11:32:21');
INSERT INTO `user` VALUES (6, 'user2', '$2a$10$8g4mVD98lhOo9VrdOv/df.0HtN3QMSGMRAeTZfTUIDCRrZKhDPuFC', '李四', '1380013800', 'test2@example.com', '广东省广州市天河区', 'USER', 1, '/avatars/1774076946268-QQ图片20231120204022.jpg', '2026-03-13 18:54:36', '2026-03-21 07:10:40');
INSERT INTO `user` VALUES (7, 'user3', '$2a$10$LtCPc7KYycJi3YDe4JEfF.YgKMHIWlWiLUA5.qIySe7sCieoXCzr.', '王五', '13833333333', 'user3@example.com', '浙江省杭州市西湖区', 'USER', 1, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=user%20avatar&size=200x200', '2026-03-13 18:54:36', '2026-03-13 18:54:36');
INSERT INTO `user` VALUES (9, 'test', '$2a$10$R6fJa3d0.YIpyZg9JdMbOOhhUXr1dfzkkGAcf8OrVanA.j4/TMPeG', '王者', '1380013800', '130789456@example.com', NULL, 'USER', NULL, '/avatars/1774063293599-QQ图片20231026204125.jpg', '2026-03-21 03:13:18', '2026-03-21 06:54:45');
INSERT INTO `user` VALUES (10, 'test1', '$2a$10$9WJ4CNRrKDGUqdPhE4zdWu2fmEgBa5xTnMeGN.o3HKe.gyDHnaj9q', '宇智波鼬', '123123123123', '130111111@example.com', NULL, 'USER', NULL, '/avatars/1774278418626-微信图片_20230213132631.jpg', '2026-03-23 15:05:34', '2026-03-23 15:07:06');
INSERT INTO `user` VALUES (11, 'test3', '$2a$10$5NrrvMWf2PqUkYNdinLj7uEXb46QqANPnkoy8CFr7BUjOmVaXZHLW', '', '1231231231', '130111111@example.com', NULL, 'USER', NULL, NULL, '2026-03-23 15:06:34', '2026-03-23 15:06:34');

-- ----------------------------
-- Table structure for user_address
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_default` int(11) NULL DEFAULT 0,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `user_address_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_address
-- ----------------------------
INSERT INTO `user_address` VALUES (4, 5, '张三', '13811111111', '广东省', '深圳市', '南山区', '科技园1号', 1, '2026-03-13 18:54:36', '2026-03-13 18:54:36');
INSERT INTO `user_address` VALUES (5, 6, '李四', '13822222222', '广东省', '广州市', '天河区', '体育西路100号', 1, '2026-03-13 18:54:36', '2026-03-13 18:54:36');
INSERT INTO `user_address` VALUES (6, 7, '王五', '13833333333', '浙江省', '杭州市', '西湖区', '文三路88号', 1, '2026-03-13 18:54:36', '2026-03-13 18:54:36');
INSERT INTO `user_address` VALUES (7, 5, '张三', '13811111111', '北京市', '北京市', '朝阳区', '中南海', 0, '2026-03-21 01:40:37', '2026-03-21 01:40:37');
INSERT INTO `user_address` VALUES (8, 9, '天天', '13031561651', '北京市', '北京市', '朝阳区', '中南海', 0, '2026-03-21 03:31:22', '2026-03-21 03:31:22');
INSERT INTO `user_address` VALUES (9, 9, '二柱子', '13800138000', '上海市', '上海市', '浦东新区', '浦东', 0, '2026-03-21 06:43:59', '2026-03-21 06:45:26');

-- ----------------------------
-- Table structure for user_behavior
-- ----------------------------
DROP TABLE IF EXISTS `user_behavior`;
CREATE TABLE `user_behavior`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `behavior_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'view/click/cart/buy',
  `style` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'å®¶å…·é£Žæ ¼',
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user`(`user_id`) USING BTREE,
  INDEX `idx_product`(`product_id`) USING BTREE,
  CONSTRAINT `user_behavior_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_behavior_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 112 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_behavior
-- ----------------------------
INSERT INTO `user_behavior` VALUES (1, 5, 1, 'view', '现代', '2026-03-14 10:18:09');
INSERT INTO `user_behavior` VALUES (2, 5, 1, 'cart', '现代', '2026-03-14 10:18:09');
INSERT INTO `user_behavior` VALUES (3, 5, 1, 'buy', '现代', '2026-03-14 10:18:09');
INSERT INTO `user_behavior` VALUES (4, 6, 2, 'view', '简约', '2026-03-14 10:18:09');
INSERT INTO `user_behavior` VALUES (5, 6, 2, 'buy', '简约', '2026-03-14 10:18:09');
INSERT INTO `user_behavior` VALUES (6, 7, 3, 'view', '北欧', '2026-03-14 10:18:09');
INSERT INTO `user_behavior` VALUES (7, 7, 3, 'buy', '北欧', '2026-03-14 10:18:09');
INSERT INTO `user_behavior` VALUES (9, 4, 9, 'view', NULL, '2026-03-20 19:36:29');
INSERT INTO `user_behavior` VALUES (10, 4, 2, 'view', NULL, '2026-03-20 19:38:17');
INSERT INTO `user_behavior` VALUES (11, 4, 10, 'view', NULL, '2026-03-20 19:38:30');
INSERT INTO `user_behavior` VALUES (12, 4, 3, 'view', NULL, '2026-03-20 19:40:35');
INSERT INTO `user_behavior` VALUES (13, 4, 16, 'view', NULL, '2026-03-20 20:03:46');
INSERT INTO `user_behavior` VALUES (14, 4, 14, 'view', NULL, '2026-03-20 20:09:41');
INSERT INTO `user_behavior` VALUES (15, 5, 3, 'view', NULL, '2026-03-20 20:10:23');
INSERT INTO `user_behavior` VALUES (16, 5, 16, 'view', NULL, '2026-03-20 20:10:35');
INSERT INTO `user_behavior` VALUES (17, 5, 12, 'view', NULL, '2026-03-20 20:10:39');
INSERT INTO `user_behavior` VALUES (18, 5, 11, 'view', NULL, '2026-03-20 20:11:48');
INSERT INTO `user_behavior` VALUES (19, 5, 11, 'view', NULL, '2026-03-20 20:13:56');
INSERT INTO `user_behavior` VALUES (20, 5, 11, 'view', NULL, '2026-03-20 20:14:03');
INSERT INTO `user_behavior` VALUES (21, 5, 11, 'view', NULL, '2026-03-20 20:18:01');
INSERT INTO `user_behavior` VALUES (22, 5, 11, 'view', NULL, '2026-03-20 20:19:01');
INSERT INTO `user_behavior` VALUES (23, 5, 11, 'view', NULL, '2026-03-20 20:19:02');
INSERT INTO `user_behavior` VALUES (24, 5, 11, 'view', NULL, '2026-03-20 20:21:15');
INSERT INTO `user_behavior` VALUES (25, 5, 11, 'view', NULL, '2026-03-20 20:26:00');
INSERT INTO `user_behavior` VALUES (26, 5, 2, 'view', NULL, '2026-03-21 09:25:02');
INSERT INTO `user_behavior` VALUES (27, 5, 14, 'view', NULL, '2026-03-21 09:25:24');
INSERT INTO `user_behavior` VALUES (28, 5, 14, 'view', NULL, '2026-03-21 10:14:09');
INSERT INTO `user_behavior` VALUES (29, 9, 16, 'view', NULL, '2026-03-21 11:31:50');
INSERT INTO `user_behavior` VALUES (30, 9, 1, 'view', NULL, '2026-03-21 11:32:01');
INSERT INTO `user_behavior` VALUES (31, 9, 16, 'view', NULL, '2026-03-21 14:42:14');
INSERT INTO `user_behavior` VALUES (32, 9, 14, 'view', NULL, '2026-03-21 14:42:34');
INSERT INTO `user_behavior` VALUES (33, 9, 2, 'view', NULL, '2026-03-21 14:47:41');
INSERT INTO `user_behavior` VALUES (34, 9, 10, 'view', NULL, '2026-03-21 14:47:45');
INSERT INTO `user_behavior` VALUES (35, 9, 13, 'view', NULL, '2026-03-21 14:48:08');
INSERT INTO `user_behavior` VALUES (36, 6, 16, 'view', NULL, '2026-03-21 15:41:48');
INSERT INTO `user_behavior` VALUES (37, 6, 14, 'view', NULL, '2026-03-21 16:17:59');
INSERT INTO `user_behavior` VALUES (38, 6, 16, 'view', NULL, '2026-03-21 16:19:10');
INSERT INTO `user_behavior` VALUES (39, 6, 16, 'view', NULL, '2026-03-21 16:19:22');
INSERT INTO `user_behavior` VALUES (40, 6, 16, 'view', NULL, '2026-03-21 16:20:29');
INSERT INTO `user_behavior` VALUES (41, 6, 16, 'view', NULL, '2026-03-21 16:27:42');
INSERT INTO `user_behavior` VALUES (42, 6, 16, 'view', NULL, '2026-03-21 16:27:49');
INSERT INTO `user_behavior` VALUES (43, 6, 16, 'view', NULL, '2026-03-21 16:28:07');
INSERT INTO `user_behavior` VALUES (44, 6, 16, 'view', NULL, '2026-03-21 16:34:48');
INSERT INTO `user_behavior` VALUES (45, 6, 16, 'view', NULL, '2026-03-21 16:35:17');
INSERT INTO `user_behavior` VALUES (46, 6, 16, 'view', NULL, '2026-03-21 16:38:55');
INSERT INTO `user_behavior` VALUES (47, 6, 16, 'view', NULL, '2026-03-21 16:39:29');
INSERT INTO `user_behavior` VALUES (48, 5, 16, 'view', NULL, '2026-03-21 16:59:51');
INSERT INTO `user_behavior` VALUES (49, 5, 16, 'view', NULL, '2026-03-21 17:00:24');
INSERT INTO `user_behavior` VALUES (50, 5, 16, 'view', NULL, '2026-03-21 17:02:42');
INSERT INTO `user_behavior` VALUES (51, 5, 14, 'view', NULL, '2026-03-21 17:10:02');
INSERT INTO `user_behavior` VALUES (52, 5, 11, 'view', NULL, '2026-03-23 21:05:53');
INSERT INTO `user_behavior` VALUES (53, 5, 9, 'view', NULL, '2026-03-23 21:06:06');
INSERT INTO `user_behavior` VALUES (54, 4, 13, 'view', NULL, '2026-03-23 21:22:22');
INSERT INTO `user_behavior` VALUES (55, 5, 14, 'view', NULL, '2026-03-23 21:22:52');
INSERT INTO `user_behavior` VALUES (56, 5, 10, 'view', NULL, '2026-03-23 21:23:32');
INSERT INTO `user_behavior` VALUES (57, 5, 13, 'view', NULL, '2026-03-23 21:23:39');
INSERT INTO `user_behavior` VALUES (58, 5, 14, 'view', NULL, '2026-03-23 21:23:44');
INSERT INTO `user_behavior` VALUES (59, 5, 1, 'view', NULL, '2026-03-23 21:23:46');
INSERT INTO `user_behavior` VALUES (60, 5, 9, 'view', NULL, '2026-03-23 21:23:49');
INSERT INTO `user_behavior` VALUES (61, 5, 9, 'view', NULL, '2026-03-23 21:23:59');
INSERT INTO `user_behavior` VALUES (62, 5, 1, 'view', NULL, '2026-03-23 21:31:11');
INSERT INTO `user_behavior` VALUES (63, 5, 1, 'view', NULL, '2026-03-23 21:38:30');
INSERT INTO `user_behavior` VALUES (64, 5, 1, 'view', NULL, '2026-03-23 21:38:31');
INSERT INTO `user_behavior` VALUES (65, 5, 1, 'view', NULL, '2026-03-23 21:53:28');
INSERT INTO `user_behavior` VALUES (66, 5, 2, 'view', NULL, '2026-03-23 22:00:15');
INSERT INTO `user_behavior` VALUES (67, 5, 16, 'view', NULL, '2026-03-23 22:02:28');
INSERT INTO `user_behavior` VALUES (68, 5, 3, 'view', NULL, '2026-03-23 22:05:33');
INSERT INTO `user_behavior` VALUES (69, 5, 14, 'view', NULL, '2026-03-23 22:10:15');
INSERT INTO `user_behavior` VALUES (70, 5, 1, 'view', NULL, '2026-03-23 22:10:22');
INSERT INTO `user_behavior` VALUES (71, 5, 1, 'view', NULL, '2026-03-23 22:15:56');
INSERT INTO `user_behavior` VALUES (72, 5, 3, 'view', NULL, '2026-03-23 22:17:06');
INSERT INTO `user_behavior` VALUES (73, 5, 11, 'view', NULL, '2026-03-23 22:17:23');
INSERT INTO `user_behavior` VALUES (74, 5, 13, 'view', NULL, '2026-03-23 22:17:45');
INSERT INTO `user_behavior` VALUES (75, 5, 12, 'view', NULL, '2026-03-23 22:17:58');
INSERT INTO `user_behavior` VALUES (76, 5, 13, 'view', NULL, '2026-03-23 22:18:01');
INSERT INTO `user_behavior` VALUES (77, 9, 13, 'view', NULL, '2026-03-23 22:19:15');
INSERT INTO `user_behavior` VALUES (78, 9, 3, 'view', NULL, '2026-03-23 22:19:26');
INSERT INTO `user_behavior` VALUES (79, 9, 9, 'view', NULL, '2026-03-23 22:19:45');
INSERT INTO `user_behavior` VALUES (80, 5, 16, 'view', NULL, '2026-03-23 22:30:59');
INSERT INTO `user_behavior` VALUES (81, 5, 11, 'view', NULL, '2026-03-23 22:33:59');
INSERT INTO `user_behavior` VALUES (82, 5, 10, 'view', NULL, '2026-03-23 22:34:17');
INSERT INTO `user_behavior` VALUES (83, 5, 2, 'view', NULL, '2026-03-23 22:34:50');
INSERT INTO `user_behavior` VALUES (84, 5, 11, 'view', NULL, '2026-03-23 22:38:34');
INSERT INTO `user_behavior` VALUES (85, 5, 2, 'view', NULL, '2026-03-23 22:38:37');
INSERT INTO `user_behavior` VALUES (86, 5, 11, 'view', NULL, '2026-03-23 22:38:39');
INSERT INTO `user_behavior` VALUES (87, 5, 1, 'view', NULL, '2026-03-23 22:38:43');
INSERT INTO `user_behavior` VALUES (88, 5, 12, 'view', NULL, '2026-03-23 22:38:58');
INSERT INTO `user_behavior` VALUES (89, 5, 11, 'view', NULL, '2026-03-23 22:41:34');
INSERT INTO `user_behavior` VALUES (90, 5, 11, 'view', NULL, '2026-03-23 22:42:09');
INSERT INTO `user_behavior` VALUES (91, 5, 13, 'view', NULL, '2026-03-23 22:42:20');
INSERT INTO `user_behavior` VALUES (92, 5, 11, 'view', NULL, '2026-03-23 22:42:45');
INSERT INTO `user_behavior` VALUES (93, 5, 13, 'view', NULL, '2026-03-23 22:44:23');
INSERT INTO `user_behavior` VALUES (94, 5, 1, 'view', NULL, '2026-03-23 22:50:18');
INSERT INTO `user_behavior` VALUES (95, 9, 12, 'view', NULL, '2026-03-23 22:51:21');
INSERT INTO `user_behavior` VALUES (96, 7, 9, 'view', NULL, '2026-03-23 22:58:48');
INSERT INTO `user_behavior` VALUES (97, 7, 3, 'view', NULL, '2026-03-23 22:58:56');
INSERT INTO `user_behavior` VALUES (98, 7, 12, 'view', NULL, '2026-03-23 22:59:15');
INSERT INTO `user_behavior` VALUES (99, 10, 11, 'view', NULL, '2026-03-23 23:07:15');
INSERT INTO `user_behavior` VALUES (100, 10, 13, 'view', NULL, '2026-03-23 23:07:23');
INSERT INTO `user_behavior` VALUES (101, 10, 12, 'view', NULL, '2026-03-23 23:07:50');
INSERT INTO `user_behavior` VALUES (102, 4, 13, 'view', NULL, '2026-03-24 09:48:20');
INSERT INTO `user_behavior` VALUES (103, 4, 10, 'view', NULL, '2026-03-24 09:50:55');
INSERT INTO `user_behavior` VALUES (104, 5, 11, 'view', NULL, '2026-03-24 09:59:19');
INSERT INTO `user_behavior` VALUES (105, 5, 16, 'view', NULL, '2026-03-24 10:00:19');
INSERT INTO `user_behavior` VALUES (106, 4, 14, 'view', NULL, '2026-03-25 09:55:15');
INSERT INTO `user_behavior` VALUES (107, 4, 16, 'view', NULL, '2026-03-25 10:20:51');
INSERT INTO `user_behavior` VALUES (108, 4, 10, 'view', NULL, '2026-03-25 10:39:03');
INSERT INTO `user_behavior` VALUES (109, 4, 10, 'view', NULL, '2026-03-25 15:14:44');
INSERT INTO `user_behavior` VALUES (110, 5, 16, 'view', NULL, '2026-03-29 00:51:16');
INSERT INTO `user_behavior` VALUES (111, 4, 16, 'view', NULL, '2026-03-31 15:21:24');

-- ----------------------------
-- Table structure for user_browse_history
-- ----------------------------
DROP TABLE IF EXISTS `user_browse_history`;
CREATE TABLE `user_browse_history`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `browse_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user`(`user_id`) USING BTREE,
  INDEX `idx_product`(`product_id`) USING BTREE,
  CONSTRAINT `user_browse_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_browse_history_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_browse_history
-- ----------------------------
INSERT INTO `user_browse_history` VALUES (1, 5, 1, '2026-03-14 10:18:09');
INSERT INTO `user_browse_history` VALUES (2, 5, 2, '2026-03-14 10:18:09');
INSERT INTO `user_browse_history` VALUES (3, 5, 3, '2026-03-14 10:18:09');

-- ----------------------------
-- Table structure for user_favorite
-- ----------------------------
DROP TABLE IF EXISTS `user_favorite`;
CREATE TABLE `user_favorite`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_user_product`(`user_id`, `product_id`) USING BTREE,
  INDEX `idx_product`(`product_id`) USING BTREE,
  CONSTRAINT `user_favorite_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_favorite_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_favorite
-- ----------------------------
INSERT INTO `user_favorite` VALUES (3, 6, 2, '2026-03-14 10:18:09');
INSERT INTO `user_favorite` VALUES (6, 5, 11, '2026-03-23 22:17:24');
INSERT INTO `user_favorite` VALUES (7, 5, 13, '2026-03-23 22:17:47');
INSERT INTO `user_favorite` VALUES (9, 9, 13, '2026-03-23 22:19:17');
INSERT INTO `user_favorite` VALUES (12, 7, 12, '2026-03-23 22:59:17');
INSERT INTO `user_favorite` VALUES (13, 10, 11, '2026-03-23 23:07:17');
INSERT INTO `user_favorite` VALUES (14, 10, 13, '2026-03-23 23:07:25');

SET FOREIGN_KEY_CHECKS = 1;
