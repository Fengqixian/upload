package com.clinbrain.bd.mdm.MetadataManage.service;
import com.clinbrain.bd.mdm.common.core.util.R;

import java.util.Map;

public interface MetaDataExportService {
    
    /**
      * 导出表或数据库脚本
      * @param paramMap  
      * @return 
      * @author yjt
      * @date  2019/9/9 9:17 
     */
    String exportMetaData(Map<String,Object> paramMap);

    /**
      * 导出表或数据库脚本并执行到指定目标
      * @param paramMap  
      * @return 
      * @author yjt
      * @date  2019/9/9 9:18 
     */
    R exportMetaDataToTarget(Map<String,Object> paramMap);
}
