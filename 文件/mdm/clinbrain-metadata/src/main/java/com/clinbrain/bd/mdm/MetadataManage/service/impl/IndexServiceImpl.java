package com.clinbrain.bd.mdm.MetadataManage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.clinbrain.bd.mdm.MetadataManage.mapper.IndexMapper;
import com.clinbrain.bd.mdm.MetadataManage.service.IndexService;
import com.clinbrain.bd.mdm.common.core.factory.ConnectionFactory;
import com.clinbrain.bd.mdm.common.core.util.ConnectionParam;
import com.clinbrain.bd.mdm.common.core.util.DbTypeEnum;
import com.clinbrain.bd.mdm.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IndexServiceImpl implements IndexService {
    @Value("${index.data.db-type}")
    private String dbType;
    @Value("${index.data.ip-addr}")
    private String ipAddr;
    @Value("${index.data.port}")
    private String port;
    @Value("${index.data.username}")
    private String username;
    @Value("${index.data.password}")
    private String password;
    @Value("${index.data.database}")
    private String database;


    @Autowired
    private IndexMapper indexMapper;


    @Override
    public R getIndex(String resourceId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        Map<String, Object> dataAreaMap = new HashMap<>();
        resultMap.put("dataArea", dataAreaMap);
        Map<String, Object> omahaMap = new HashMap<>();
        resultMap.put("omaha", omahaMap);
        Map<String, Object> snomedctMap = new HashMap<>();
        resultMap.put("snomedct", snomedctMap);
        Map<String, Object> pieDataMap = new HashMap<>();
        resultMap.put("pieData", pieDataMap);
        List<String> legendDatas = new ArrayList<>();
        pieDataMap.put("legendData", legendDatas);

        Map<String, Object> treeMapDataMap = new HashMap<>();
        resultMap.put("treeMapData", treeMapDataMap);

        List<Map<String, Object>> keyValues = indexMapper.getKeyValueData();
        for (Map<String, Object> kv : keyValues) {
            if (StringUtils.equalsIgnoreCase("dataArea", (String) kv.get("category"))) {
                dataAreaMap.put((String) kv.get("key"), kv.get("value"));
            } else if (StringUtils.equalsIgnoreCase("omaha", (String) kv.get("category"))) {
                omahaMap.put((String) kv.get("key"), kv.get("value"));
            } else if (StringUtils.equalsIgnoreCase("snomedct", (String) kv.get("category"))) {
                snomedctMap.put((String) kv.get("key"), kv.get("value"));
            }
        }
        List<Map<String, Object>> pieDatas = indexMapper.getPieDataMap();
        for (Map<String, Object> p : pieDatas) {
            legendDatas.add((String) p.get("name"));
        }
        pieDataMap.put("data", pieDatas);

        List<Map<String, Object>> treemapDatas = indexMapper.getTreeMapDataMap();
        List<Map<String, Object>> treemaps = new ArrayList<>();
        String name = null;
        List<Map<String, Object>> children = null;
        for (Map<String, Object> tree : treemapDatas) {
            if (!StringUtils.equalsIgnoreCase(name, (String) tree.get("name"))) {
                name = (String) tree.get("name");
                treemaps.add(tree);
                children = new ArrayList<>();
                tree.put("children", children);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("name", tree.get("ch_name"));
            map.put("value", tree.get("ch_value"));
            children.add(map);
        }

        treeMapDataMap.put("data", treemaps);
        return new R(resultMap);
    }


    @Override
    public void getIndexCount() throws Exception {
        //获取所以需要查询的表
        String sql = "SELECT count('*') AS tableSize FROM ";

        List<Map<String, Object>> tableList = indexMapper.getIndexTableList(null);

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            ConnectionParam comParam = new ConnectionParam();
            comParam.setDbType(DbTypeEnum.parseValue(dbType));
            comParam.setDatabase(database);
            comParam.setIpAddr(ipAddr);
            comParam.setPassword(password);
            comParam.setPort(port);
            comParam.setUsername(username);
            conn = ConnectionFactory.getConnectionFactory().createConnection(comParam);

            if (tableList != null && tableList.size() > 0) {
                for (Map<String, Object> map : tableList) {
                    String tableName = map.get("index_table_name").toString();
                    preparedStatement = conn.prepareStatement(sql + tableName);
                    rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        int count = rs.getInt("tableSize");
                        int id = Integer.valueOf(map.get("id").toString());
                        if (count > 0) {
                            //修改数据
                            indexMapper.updateTableIdByCount(count + "", id);
                        }
                    }
                }

                //然后在更新
                this.updateKeyValue();
            }

        } catch (Exception e) {
            log.error("提取数据失败,失败原因" + e.getMessage(), e);
        } finally {
            if (rs != null) rs.close();
            if (preparedStatement != null) preparedStatement.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * 拉取数据完成，计算数据和后更新
     */
    private void updateKeyValue() {
        List<Map<String, Object>> keyValues = indexMapper.getKeyValueData();

        if (keyValues != null && keyValues.size() > 0) {
            for (Map<String, Object> map : keyValues) {
                List<Map<String, Object>> tableList = indexMapper.getIndexTableList(Integer.valueOf(map.get("id").toString()));
                if (tableList != null && tableList.size() > 0) {
                    Long count = tableList.stream()
                            .filter(t -> t.get("table_data_count") != null && StringUtils.isNumeric(t.get("table_data_count").toString()))
                            .mapToLong(t -> Long.valueOf(t.get("table_data_count").toString())).sum();
                    if (count > 0) {
                        //更新数据表
                        indexMapper.updateKeyValueData(count.toString(), Integer.valueOf(map.get("id").toString()));
                    }

                }
            }
        }
    }


}

