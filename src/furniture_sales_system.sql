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

 Date: 19/03/2026 20:04:59
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
INSERT INTO `activity` VALUES (2, '秒杀', '', '2026-03-18 16:00:00', '2026-03-24 16:00:00', 'flash_sale', 1, '2026-03-17 07:43:28', '2026-03-19 01:27:33');
INSERT INTO `activity` VALUES (3, '大促销', '', '2026-03-16 16:00:00', '2026-03-23 16:00:00', 'discount', 1, '2026-03-18 11:55:17', '2026-03-19 01:29:44');
INSERT INTO `activity` VALUES (4, '推荐新品', '', '2026-03-17 16:00:00', '2026-03-25 00:00:00', 'new_arrival', 1, '2026-03-19 01:39:36', '2026-03-19 06:43:36');

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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity_product
-- ----------------------------
INSERT INTO `activity_product` VALUES (3, 2, 9, 400.00, NULL, '2026-03-19 01:27:33');
INSERT INTO `activity_product` VALUES (4, 2, 3, 1000.00, NULL, '2026-03-19 01:27:33');
INSERT INTO `activity_product` VALUES (5, 3, 16, 199.00, NULL, '2026-03-19 01:29:44');
INSERT INTO `activity_product` VALUES (6, 3, 14, 299.00, NULL, '2026-03-19 01:29:44');
INSERT INTO `activity_product` VALUES (7, 3, 12, 588.00, NULL, '2026-03-19 01:29:44');
INSERT INTO `activity_product` VALUES (8, 4, 13, 2300.00, NULL, '2026-03-19 06:43:36');

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
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart_item
-- ----------------------------
INSERT INTO `cart_item` VALUES (19, 5, 9, 1, 1, '2026-03-19 01:38:00', '2026-03-19 18:15:40');
INSERT INTO `cart_item` VALUES (20, 5, 16, 1, 1, '2026-03-19 10:29:56', '2026-03-19 10:29:56');

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
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (13, '办公家居', NULL, NULL, NULL, NULL, '2026-03-13 07:14:16', NULL);
INSERT INTO `category` VALUES (14, '客厅家居', NULL, NULL, NULL, NULL, '2026-03-13 07:47:13', NULL);
INSERT INTO `category` VALUES (15, '餐厅家具', NULL, NULL, NULL, NULL, '2026-03-13 08:55:21', NULL);
INSERT INTO `category` VALUES (16, '卧室家居', NULL, NULL, NULL, NULL, '2026-03-13 08:55:43', NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (56, '202603130003', 3, '沙发', 'http://localhost:8080/images/e7415cb3-914a-4054-a063-3b68dbe0f318.png', 2, 1999.00, NULL);
INSERT INTO `order_item` VALUES (70, '202603130001', 14, '餐椅', 'http://localhost:8080/images/6ec5c6df-b22b-4132-89eb-ddd468279de0.png', 1, 399.00, NULL);
INSERT INTO `order_item` VALUES (71, '202603130001', 1, '办公椅', 'http://localhost:8080/images/1b77d7ed-f904-4ec6-a9a4-444f94e24d5d.png', 2, 400.00, NULL);
INSERT INTO `order_item` VALUES (72, '202603130001', 10, '餐桌', 'http://localhost:8080/images/b265d34f-6d53-4ba8-b0c6-f0a6f1040aaa.png', 1, 1500.00, NULL);
INSERT INTO `order_item` VALUES (74, '202603130002', 2, '办公桌', 'http://localhost:8080/images/578da18b-91e5-461a-aa53-ba8d82269bab.png', 2, 1200.00, NULL);
INSERT INTO `order_item` VALUES (75, '202603130002', 14, '餐椅', 'http://localhost:8080/images/6ec5c6df-b22b-4132-89eb-ddd468279de0.png', 1, 399.00, NULL);

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
INSERT INTO `orders` VALUES ('202603130001', 5, 2699.00, 2699.00, 3, '支付宝', NULL, '深圳市南山区科技园1号', '张三', '13811111111', NULL, NULL, '2026-03-13 18:54:51', '2026-03-18 03:12:40');
INSERT INTO `orders` VALUES ('202603130002', 6, 2799.00, 2799.00, 3, '微信', NULL, '广州市天河区体育西路100号', '李四', '13822222222', NULL, '2026-03-14 11:06:44', '2026-03-13 18:54:51', '2026-03-18 03:14:39');
INSERT INTO `orders` VALUES ('202603130003', 7, 3998.00, 3998.00, 3, '支付宝', NULL, '杭州市西湖区文三路88号', '王五', '13833333333', NULL, '2026-03-14 11:06:44', '2026-03-13 18:54:51', '2026-03-14 08:35:30');

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
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category`(`category_id`) USING BTREE,
  INDEX `idx_brand`(`brand_id`) USING BTREE,
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `product_ibfk_2` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, '办公椅', 13, NULL, 400.00, NULL, 58, 2, 5.00, '', NULL, 'http://localhost:8080/images/1b77d7ed-f904-4ec6-a9a4-444f94e24d5d.png', NULL, NULL, '2026-03-13 07:22:59', '2026-03-18 11:12:39');
INSERT INTO `product` VALUES (2, '办公桌', 13, NULL, 1200.00, NULL, 18, 2, 5.00, '办公用品', NULL, 'http://localhost:8080/images/84faa67d-fa80-452d-af6a-da9428431c70.png', NULL, NULL, '2026-03-13 07:36:29', '2026-03-18 02:35:46');
INSERT INTO `product` VALUES (3, '沙发', 14, NULL, 1999.00, NULL, 58, 2, 5.00, '', NULL, 'http://localhost:8080/images/1c8386be-4a41-4687-9e4b-f9344618d9c5.png', NULL, NULL, '2026-03-13 07:47:36', '2026-03-18 11:09:29');
INSERT INTO `product` VALUES (9, '实木床', 14, NULL, 700.00, NULL, 20, 0, 5.00, '', NULL, 'http://localhost:8080/images/854af08d-becb-418c-bb8b-3845e5b07429.png', NULL, NULL, '2026-03-13 08:08:56', '2026-03-18 02:36:41');
INSERT INTO `product` VALUES (10, '餐桌', 15, NULL, 1500.00, NULL, 14, 1, 5.00, '', NULL, 'http://localhost:8080/images/b265d34f-6d53-4ba8-b0c6-f0a6f1040aaa.png', NULL, NULL, '2026-03-18 02:08:14', '2026-03-18 11:12:39');
INSERT INTO `product` VALUES (11, '书柜', 13, 6, 999.00, 1199.00, 25, 0, 4.70, '多层书柜', '尺寸: 180x80cm, 材质: 人造板', 'http://localhost:8080/images/084ef819-96f1-4c49-b9b6-f8cdbe133499.png', NULL, 1, '2026-03-14 10:00:00', '2026-03-18 02:37:33');
INSERT INTO `product` VALUES (12, '茶几', 14, 7, 799.00, 999.00, 40, 0, 4.90, '现代茶几', '尺寸: 120x60cm, 材质: 岩板', 'http://localhost:8080/images/45d0dae9-a6bb-4817-bac1-a69c0d0c900d.png', NULL, 1, '2026-03-14 10:00:00', '2026-03-18 02:37:06');
INSERT INTO `product` VALUES (13, '衣柜', 16, 8, 2499.00, 2999.00, 15, 0, 4.60, '推拉门衣柜', '尺寸: 200x60cm, 材质: 密度板', 'http://localhost:8080/images/e14b9a94-6eb0-4282-bd77-be125d052516.png', NULL, 1, '2026-03-14 10:00:00', '2026-03-18 02:37:24');
INSERT INTO `product` VALUES (14, '餐椅', 15, 9, 399.00, 499.00, 58, 2, 4.50, '现代餐椅', '颜色: 灰色, 材质: 布艺', 'http://localhost:8080/images/6ec5c6df-b22b-4132-89eb-ddd468279de0.png', NULL, 1, '2026-03-14 10:00:00', '2026-03-18 11:14:38');
INSERT INTO `product` VALUES (16, '床头柜', 16, NULL, 300.00, NULL, 20, 0, 5.00, '', NULL, 'http://localhost:8080/images/b5cb4aa1-fcd8-453a-bbd9-232c3cfbd384.png', NULL, NULL, '2026-03-18 02:50:57', '2026-03-18 11:09:58');

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
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of review
-- ----------------------------
INSERT INTO `review` VALUES (1, 5, 1, '202603130001', 5, '椅子质量很好，坐着很舒服', NULL, 1, '2026-03-14 10:18:09', '2026-03-14 10:18:09');
INSERT INTO `review` VALUES (3, 7, 3, '202603130003', 5, '沙发很柔软，款式时尚', NULL, 1, '2026-03-14 10:18:09', '2026-03-14 10:18:09');

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
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (4, 'admin', '$2a$10$LtCPc7KYycJi3YDe4JEfF.YgKMHIWlWiLUA5.qIySe7sCieoXCzr.', 'wb liu', '11111111111', '1308631536@qq.com', '巢湖', 'ADMIN', 1, '/avatars/1773655699510-微信图片_20230213132631.jpg', '2026-03-13 07:08:11', '2026-03-16 10:08:21');
INSERT INTO `user` VALUES (5, 'user1', '$2a$10$F/tw/TcB9OrVHgVYUMAbf.ujIsvLpi7R3yYIxNk2/PB281i/8dVJK', '张三', '1301111111', 'user1@example.com', '广东省深圳市南山区', 'USER', 1, '/avatars/1773919567765-QQ图片20231026204125.jpg', '2026-03-13 18:54:36', '2026-03-19 11:32:21');
INSERT INTO `user` VALUES (6, 'user2', '$2a$10$LtCPc7KYycJi3YDe4JEfF.YgKMHIWlWiLUA5.qIySe7sCieoXCzr.', '李四', '13822222222', 'user2@example.com', '广东省广州市天河区', 'USER', 1, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=user%20avatar&size=200x200', '2026-03-13 18:54:36', '2026-03-13 18:54:36');
INSERT INTO `user` VALUES (7, 'user3', '$2a$10$LtCPc7KYycJi3YDe4JEfF.YgKMHIWlWiLUA5.qIySe7sCieoXCzr.', '王五', '13833333333', 'user3@example.com', '浙江省杭州市西湖区', 'USER', 1, 'https://neeko-copilot.bytedance.net/api/text2image?prompt=user%20avatar&size=200x200', '2026-03-13 18:54:36', '2026-03-13 18:54:36');

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_address
-- ----------------------------
INSERT INTO `user_address` VALUES (4, 5, '张三', '13811111111', '广东省', '深圳市', '南山区', '科技园1号', 1, '2026-03-13 18:54:36', '2026-03-13 18:54:36');
INSERT INTO `user_address` VALUES (5, 6, '李四', '13822222222', '广东省', '广州市', '天河区', '体育西路100号', 1, '2026-03-13 18:54:36', '2026-03-13 18:54:36');
INSERT INTO `user_address` VALUES (6, 7, '王五', '13833333333', '浙江省', '杭州市', '西湖区', '文三路88号', 1, '2026-03-13 18:54:36', '2026-03-13 18:54:36');

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
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_favorite
-- ----------------------------
INSERT INTO `user_favorite` VALUES (1, 5, 1, '2026-03-14 10:18:09');
INSERT INTO `user_favorite` VALUES (2, 5, 3, '2026-03-14 10:18:09');
INSERT INTO `user_favorite` VALUES (3, 6, 2, '2026-03-14 10:18:09');

SET FOREIGN_KEY_CHECKS = 1;
