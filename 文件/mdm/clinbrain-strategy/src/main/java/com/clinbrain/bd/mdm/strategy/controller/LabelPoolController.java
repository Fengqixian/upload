/*
 *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clinbrain.bd.mdm.strategy.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.strategy.dto.LabelpoolDto;
import com.clinbrain.bd.mdm.strategy.entity.LabelLabelpool;
import com.clinbrain.bd.mdm.strategy.service.LabelPoolService;
import com.mzlion.easyokhttp.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 策略
 *
 * @author lengleng
 * @date 2019/2/1
 */
@RestController
//@AllArgsConstructor
@RequestMapping("/labelPool")
public class LabelPoolController {
    @Value("${LabelPool.data.url}")
    private String url;
    @Autowired
    private LabelPoolService labelPoolService;

    /**
     * 列表
     *
     * @param labelPool 参数集
     * @return 数据库表
     */
    @GetMapping("/page")
    public R<List<LabelLabelpool>> list(Page page, LabelLabelpool labelPool) {
        return new R(labelPoolService.queryPage(page, labelPool));
    }

    /**
     * 列表
     *
     * @param labelPool 参数集
     * @return 数据库表
     */
    @GetMapping("/pages")
    public Map<String, Object> lists(Page page, LabelpoolDto labelPool) {
        LabelLabelpool labelpool1 = new LabelLabelpool();
        labelpool1.setTlabelParentId(labelPool.getTlabelParentID());
        page.setSize(-1);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 20000);
        map.put("data", labelPoolService.queryPage(page, labelpool1).getRecords());
        return map;
    }


    /**
     * @param labelPool
     * @return
     */
    @PostMapping("/save")
    public R save(@RequestBody LabelLabelpool labelPool) {
        return new R(labelPoolService.save(labelPool));
    }

    @GetMapping("/getLabel")
    public R getLabelPoolList(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader, LabelpoolDto labelPool) {
        String Authorization = "";
        if (StrUtil.isBlank(authHeader)) {
            Authorization = "Basic cGlnOnBpZw==";
        } else {
            Authorization = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StrUtil.EMPTY).trim();
        }

        Map<String, Object> map = BeanUtil.beanToMap(labelPool);
        Map<String, String> params = new HashMap<>();
        if (!map.isEmpty()) {
            for (String key : map.keySet()) {
                params.put(key, map.get(key) != null ? map.get(key).toString() : "");
            }
        }

        String responseData = HttpClient.get(url).
                header("Authorization", Authorization).queryString(params)
                //设置编码
//                .charset("utf-8")
                .execute()
                .asString();
        System.out.printf(responseData.toString());
        List<LabelpoolDto> labelLabelpoolList = new ArrayList<>();
        if (StrUtil.isNotEmpty(responseData)) {
            Map<String, Object> object = JSONObject.parseObject(responseData, Map.class);
            Integer code = (Integer) object.get("code");
            if (object != null && code == 20000 && object.get("data") != null) {
                labelLabelpoolList = JSONArray.parseArray(object.get("data").toString(), LabelpoolDto.class);
                if (labelLabelpoolList != null && labelLabelpoolList.size() > 0) {
                    labelLabelpoolList.stream().forEach(l -> {
                        if (l.getTlabelorclass() == 0) {
                            l.setNodeType("category");
                        } else {
                            l.setNodeType("sign");
                        }
                    });
                }
            }
        }

        return new R(labelLabelpoolList);
    }

}
