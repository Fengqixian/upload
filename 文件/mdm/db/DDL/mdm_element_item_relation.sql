CREATE TABLE `mdm_element_item_relation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source_id` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'value表中resource_id',
  `parent_source_id` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'value表中parent_resource_id',
  `target_id` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `parent_target_id` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '元素项子表' ROW_FORMAT = Dynamic;