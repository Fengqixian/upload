package com.clinbrain.bd.mdm.common.datasource.annotation;

import com.clinbrain.bd.mdm.common.datasource.config.DynamicDataSourceConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lianglele
 * @date 2019-12-26 10:22
 * 开启动态数据源
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({DynamicDataSourceConfig.class})
public @interface EnableDynamicDataSource {
}
