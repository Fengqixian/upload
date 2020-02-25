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

package com.clinbrain.bd.mdm.admin.api.feign;

import com.clinbrain.bd.mdm.common.core.constant.ServiceNameConstants;
import com.clinbrain.bd.mdm.admin.api.feign.factory.RemoteMetadataServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lengleng
 * @date 2019/2/1
 */
@FeignClient(value = ServiceNameConstants.META_SERVICE+"-${spring.profiles.active}", fallbackFactory = RemoteMetadataServiceFallbackFactory.class)
public interface RemoteMetadataService {


    /**
     * 根据父节点返回子节点
     * 懒加载树结构使用
     *
     * @return 树形菜单
     */
    @GetMapping(value = "/metadatavalue/tree/lazyTree")
    String listModelResourceLazyTree(@RequestParam("parentUuid") String parentUuid, @RequestParam("queryString") String queryString);

    /**
     * 根据父节点返回子节点
     * 懒加载树结构使用
     *
     * @return 树形菜单
     */
    @GetMapping(value = "/metadatavalue/tree/modelTree/{modelId}")
    String listModelResourceTree(@PathVariable("modelId") String modelId);

    /**
     * 查询技术视图
     *
     * @return
     */
    @GetMapping("/technologyView/tree")
    String getTechnologyViewTree();

    /**
     * 查询数据库
     *
     * @return
     */
    @GetMapping("/technologyView/database")
    String getTechnologyViewDatabase(@RequestParam(required = false, value = "size") Integer size);

    /**
     * 查询数据表
     *
     * @return
     */
    @GetMapping("/technologyView/table")
    String getTechnologyViewTable(@RequestParam(required = false, value = "size") Integer size, @RequestParam(required = false, value = "databaseId") Integer databaseId);

    /**
     * 查询字段
     *
     * @param page
     * @param tree
     * @return
     */
    @GetMapping("/technologyView/info")
    String getTableInfoByTreeNode(@RequestParam(required = false, value = "size") Integer size, @RequestParam(required = false, value = "id") Integer id, @RequestParam(required = false, value = "nameEn") String nameEn, @RequestParam(required = false, value = "nameCn") String nameCn);

}
