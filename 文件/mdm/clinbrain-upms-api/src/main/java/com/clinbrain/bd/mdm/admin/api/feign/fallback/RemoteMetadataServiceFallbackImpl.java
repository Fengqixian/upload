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

package com.clinbrain.bd.mdm.admin.api.feign.fallback;

import com.clinbrain.bd.mdm.admin.api.feign.RemoteMetadataService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lengleng
 * @date 2019/2/1
 */
@Slf4j
@Component
public class RemoteMetadataServiceFallbackImpl implements RemoteMetadataService {
    @Setter
    private Throwable cause;


    /**
     * 根据父节点返回子节点
     * 懒加载树结构使用
     *
     * @return 树形菜单
     */
    @Override
    public String listModelResourceLazyTree(String parentUuid, String queryString) {
        log.error("feign 查询树节点信息失败:{}", parentUuid, queryString, cause);
        return null;
    }

    @Override
    public String listModelResourceTree(String modelId) {
        log.error("feign 查询树信息失败:{}", modelId, cause);
        return null;
    }

    /**
     * 查询技术视图
     *
     * @return
     */
    @Override
    public String getTechnologyViewTree() {
        log.error("feign 查询树信息失败:{}", cause);
        return null;
    }

    /**
     * 查询数据库
     *
     * @return
     */
    @Override
    public String getTechnologyViewDatabase(@RequestParam(required = false, value = "size") Integer size) {
        log.error("feign 查询数据库信息失败:{}", cause);
        return null;
    }

    /**
     * 查询数据表
     *
     * @return
     */
    @Override
    public String getTechnologyViewTable(@RequestParam(required = false, value = "size") Integer size, @RequestParam(required = false, value = "databaseId") Integer databaseId) {
        log.error("feign 查询数据表信息失败:{}", databaseId, cause);
        return null;
    }


    /**
     * @param size   -1不分页
     * @param id     表id
     * @param nameEn 字段名称
     * @param nameCn 字段中文
     * @return
     */
    @Override
    public String getTableInfoByTreeNode(Integer size, Integer id, String nameEn, String nameCn) {
        log.error("feign 查询树字段信息失败:{}", size, id, nameEn, nameCn, cause);
        return null;
    }

}
