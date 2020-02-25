CREATE TABLE `mdm_element_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `element_name` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '元素项字段名称',
  `element_datatype` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '元素项数据类型',
  `element_length` bigint(21) DEFAULT NULL COMMENT '元素项长度',
  `element_remarks` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '元素项备注',
  `resource_id` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL,
  `parent_resource_id` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `inx_element_name` (`element_name`) USING BTREE COMMENT '元素项名称唯一索引'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='元素项表';