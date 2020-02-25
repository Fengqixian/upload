package com.clinbrain.bd.mdm.MetadataManage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.clinbrain.bd.mdm.MetadataManage.entity.ModelResourceTreeEntity;
import com.clinbrain.bd.mdm.MetadataManage.service.DataLineageService;
import com.clinbrain.bd.mdm.MetadataManage.service.IndexService;
import com.clinbrain.bd.mdm.MetadataManage.util.uuid.UuidGenerator;
import com.clinbrain.bd.mdm.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 数据血缘关系分析
 */
@RestController
@Api(value = "首页")
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private IndexService indexService;
    @ResponseBody
    @ApiOperation(response = R.class,value = "查询血缘关系",notes = "根据id查询")
    @GetMapping
    public R getIndex(String queryString) throws Exception{
        return indexService.getIndex(queryString);
        /*return new R("" +
                "{" +
                "\"dataArea\":{" +
                "\"renYuan\":88," +
                "\"ziYuan\":88," +
                "\"yiJi\":88," +
                "\"yuanGan\":88," +
                "\"yuanQianZhenDuan\":88," +
                "\"shenQing\":88," +
                "\"zhiLiao\":88," +
                "\"linChuangLuJing\":88," +
                "\"guaHao\":88," +
                "\"yiZu\":88," +
                "\"shouMa\":88," +
                "\"suiFang\":88," +
                "\"houZhen\":88," +
                "\"chuFang\":88," +
                "\"binAnHuLi\":88," +
                "\"ziChanZiYuan\":88," +
                "\"jiuZhen\":88," +
                "\"yaoShi\":88," +
                "\"yiBao\":88" +
                "}," +
                "\"omaha\":{" +
                "\"concept\":79," +
                "\"term\":101," +
                "\"relationship\":265," +
                "\"mapping\":54" +
                "}," +
                "\"snomedct\":{" +
                "\"concept\":53," +
                "\"term\":156," +
                "\"relationship\":479" +
                "}," +
                "\"pieData\":{" +
                "\"legendData\":[\"GB/T 14396-2016\",\"GB/T 2260-2007\",\"GB/T 2261.1-2003\",\"GB/T 2659-2000\"," +
                "\"GB/T 3304-1991\",\"GB/T 4658-2006\",\"GB/T 14396-2016\",\"GB/T 2260-2007\"," +
                "\"GB/T 2261.1-2003\",\"GB/T 2659-2000\",\"GB/T 3304-1991\",\"GB/T 4658-2006\"," +
                "\"GB/T 14396-2016\",\"GB/T 2260-2007\",\"GB/T 2261.1-2003\",\"GB/T 2659-2000\"," +
                "\"GB/T 3304-1991\",\"GB/T 4658-2006\"]," +
                "\"data\":[" +
                "{\"value\":335, \"name\":\"GB/T 14396-2016\"}," +
                "{\"value\":310, \"name\":\"GB/T 2260-2007\"}," +
                "{\"value\":234, \"name\":\"GB/T 2261.1-2003\"}," +
                "{\"value\":135, \"name\":\"GB/T 2659-2000\"}," +
                "{\"value\":1048, \"name\":\"GB/T 3304-1991\"}," +
                "{\"value\":251, \"name\":\"GB/T 4658-2006\"}," +
                "{\"value\":147, \"name\":\"GB/T 14396-2016\"}," +
                "{\"value\":102, \"name\":\"GB/T 2260-2007\"}," +
                "{\"value\":335, \"name\":\"GB/T 2261.1-2003\"}," +
                "{\"value\":310, \"name\":\"GB/T 2659-2000\"}," +
                "{\"value\":234, \"name\":\"GB/T 3304-1991\"}," +
                "{\"value\":135, \"name\":\"GB/T 4658-2006\"}," +
                "{\"value\":1048, \"name\":\"GB/T 14396-2016\"}," +
                "{\"value\":251, \"name\":\"GB/T 2260-2007\"}," +
                "{\"value\":147, \"name\":\"GB/T 2261.1-2003\"}," +
                "{\"value\":102, \"name\":\"GB/T 2659-2000\"}," +
                "{\"value\":335, \"name\":\"GB/T 3304-1991\"}," +
                "{\"value\":310, \"name\":\"GB/T 4658-2006\"}" +
                "]" +
                "}," +
                "\"treeMapData\":{" +
                "\"data\":[" +
                "{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "}," +
                "{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "},{" +
                "\"value\": 276," +
                "\"name\": \"FindiPhoto Items 2.action\"," +
                "\"path\": \"Automator/FindiPhoto Items 2.action\"" +
                "}" +
                "]" +
                "}" +
                "}");*/
    }
}
