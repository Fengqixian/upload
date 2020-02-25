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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.log.annotation.SysLog;
import com.clinbrain.bd.mdm.admin.api.feign.RemoteMetadataService;
import com.clinbrain.bd.mdm.admin.api.feign.RemoteUserService;
import com.clinbrain.bd.mdm.strategy.dto.StrategyInfoDto;
import com.clinbrain.bd.mdm.strategy.entity.StrategyInfo;
import com.clinbrain.bd.mdm.strategy.service.StrategyInfoService;
import com.clinbrain.bd.mdm.strategy.vo.StrategyLabelVO;
import com.clinbrain.bd.mdm.strategy.vo.StrategyVO;
import com.clinbrain.bd.mdm.strategy.vo.ViewTreeVO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 策略
 *
 * @author lengleng
 * @date 2019/2/1
 */
@RestController
@AllArgsConstructor
@RequestMapping("/strategyInfo")
public class StrategyInfoController {
    private final StrategyInfoService strategyInfoService;

    private final RemoteMetadataService remoteMetadataService;

    private final RemoteUserService remoteUserInfoService;

    /**
     * 列表
     *
     * @param strategyInfo 参数集
     * @return 数据库表
     */
    @GetMapping("/page")
    public R<IPage> list(Page page, StrategyInfo strategyInfo) {
        return new R<>(strategyInfoService.queryPage(page, strategyInfo));
    }

    /**
     * 列表
     *
     * @param strategyInfo 参数集
     * @return 数据库表
     */
    @GetMapping("/pages")
    public R<IPage> lists(Page page, StrategyInfoDto strategyInfo) {
        if (strategyInfo.getType() == 2) {
            return new R<>(strategyInfoService.selectStrategyLabelVo(page, strategyInfo));
        }
        return new R<>(strategyInfoService.selectStrategyVo(page, strategyInfo));
    }


    /**
     * @param strategyVO
     * @return
     */
    @SysLog("添加策略信息")
    @PostMapping("/save")
    public R save(@RequestBody StrategyVO strategyVO) {
        return new R<>(strategyInfoService.saveStrategy(strategyVO));
    }

    @SysLog("修改策略信息")
    @PutMapping("/update")
    public R update(@RequestBody StrategyVO strategyVO) {
        return new R<>(strategyInfoService.updateStrategy(strategyVO));
    }

    /**
     * @param strategyLabelVO
     * @return
     */
    @SysLog("添加标签策略信息")
    @PostMapping("/saveLabel")
    public R save(@RequestBody StrategyLabelVO strategyLabelVO) {
        return new R<>(strategyInfoService.saveStrategyLabel(strategyLabelVO));
    }

    @SysLog("修改策略信息")
    @PutMapping("/updateLabel")
    public R update(@RequestBody StrategyLabelVO strategyLabelVO) {
        return new R<>(strategyInfoService.updateStrategyLabel(strategyLabelVO));
    }

    @SysLog("修改策略状态信息")
    @PutMapping("/updateStatus")
    public R updateStatus(@RequestBody StrategyInfo strategyInfo) {
        return new R<>(strategyInfoService.updateById(strategyInfo));
    }

    @SysLog("删除策略信息")
    @DeleteMapping("/{id}")
    public R delete(@PathVariable String id) {
        return new R<>(strategyInfoService.removeById(id));
    }


    /**
     * 获取模型信息
     *
     * @return
     */
    @GetMapping("/modelTree")
    public String listModelResourceLazyTree(String parentUuid, String queryString) {
        String json = remoteMetadataService.listModelResourceLazyTree(parentUuid, queryString);
        return json;
//       return JSON.parseObject(json);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/userPage")
    public String getUserPage(String userName) {
        return remoteUserInfoService.getUserList(userName);
    }

    /**
     * 分页查询角色信息
     *
     * @return 分页对象
     */
    @GetMapping("/rolePage")
    public String getRolePage() {
        return remoteUserInfoService.listRoles();
    }


    /**
     * 加密脱敏返回数据
     *
     * @return 数据对象
     */
    @PostMapping("/getRegData/{roleId}/{systemId}")
    public Object getRegDataList(@RequestBody() Object data, @PathVariable String roleId, @PathVariable String systemId) {
        return strategyInfoService.getRegDataList(data, roleId, systemId, true);
    }

    /**
     * 解密返回数据
     *
     * @return 数据对象
     */
    @PostMapping("/getDataListDncode/{roleId}/{systemId}")
    public Object getDataListDncode(@RequestBody() Object data, @PathVariable String roleId, @PathVariable String systemId) {
        return strategyInfoService.getRegDataList(data, roleId, systemId, false);
    }

    /**
     * 根据标签标识和值获取标签sql
     *
     * @param labelId    标签标识
     * @param labelValue 替换值
     * @return
     */
    @GetMapping("/public/getLabelSql/{labelId}")
    public R getLabelDataSql(@PathVariable String labelId, Map<String, Object> labelValue) {
        return strategyInfoService.getLabelSql(labelId, labelValue);
    }


    /**
     * 查询技术视图
     *
     * @return
     */
    @GetMapping("/tree")
    public R getTechnologyViewTree(ViewTreeVO tree) {

        List<ViewTreeVO> viewList = new ArrayList<>();

        String jsonStr = "";
        if (tree == null || "database".equalsIgnoreCase(tree.getNodeType())) {
            jsonStr = remoteMetadataService.getTechnologyViewDatabase(-1);

        } else if (tree != null && tree.getDatabaseId() != null) {
            jsonStr = remoteMetadataService.getTechnologyViewTable(-1, tree.getDatabaseId());
        } else if (tree != null && tree.getTableId() != null) {
            jsonStr = remoteMetadataService.getTableInfoByTreeNode(-1, tree.getTableId(), tree.getNameEn(), tree.getNameCn());
        }

        JSONObject ject = JSON.parseObject(jsonStr);

        if (ject != null) {
            JSONObject jects = ject.getJSONObject("data");
            if (jects == null) {
                return null;
            }
            JSONArray list = JSON.parseArray(jects.getString("records"));
            if (list != null && list.size() > 0) {
                list.stream().forEach(e -> {
                    JSONObject obj = (JSONObject) e;
                    ViewTreeVO showTree = new ViewTreeVO();
                    showTree.setUuid(UUID.randomUUID().toString());
                    showTree.setId(obj.getInteger("id"));
                    showTree.setDatabaseId(obj.getInteger("databaseId"));
                    showTree.setTableId(obj.getInteger("tableId"));
                    if (showTree.getDatabaseId() == null && showTree.getTableId() == null) {
                        showTree.setNodeType("database");
                    } else if (showTree.getDatabaseId() != null) {
                        showTree.setNodeType("table");
                    } else if (showTree.getTableId() != null) {
                        showTree.setNodeType("column");
                    }
                    showTree.setNameCn(obj.getString("nameCn"));
                    showTree.setNameEn(obj.getString("nameEn"));
                    viewList.add(showTree);
                });
            }

        }

        return new R(viewList);
    }

    /**
     * 查询字段
     *
     * @return
     */
    @GetMapping("/info")
    public String getTableInfoByTreeNode(ViewTreeVO tree) {

        return remoteMetadataService.getTableInfoByTreeNode(-1, tree.getId(), tree.getNameEn(), tree.getNameCn());
    }

}
