<template>
  <div class="edit-role">
    <div class="outer-wrapper">
      <h2 class="title">菜单授权</h2>
      <div class="wrapper">
        <div class="left">
          <div class="menu-btn">
            <el-button-group>
              <el-button type="primary" icon="el-icon-plus" @click="handlerAdd">添加</el-button>
              <el-button type="primary" icon="el-icon-edit" @click="handlerEdit">修改</el-button>
              <el-button type="primary" icon="el-icon-delete" @click="handleDelete">删除</el-button>
            </el-button-group>
          </div>
          <div class="menu-tree">
            <el-input
                    placeholder="输入关键字进行过滤"
                    v-model.trim="filterText">
            </el-input>
            <el-tree
                    node-key="id"
                    highlight-current
                    :expand-on-click-node="false"
                    class="filter-tree"
                    :data="menuTreedata"
                    :default-expanded-keys="aExpandedKeys"
                    :props="defaultProps"
                    :filter-node-method="filterNode"
                    ref="tree"
                    @node-click="getNodeData"
                    @node-expand="nodeExpand"
                    @node-collapse="nodeCollapse">
            </el-tree>

          </div>
        </div>
        <div class="right">
          <div class="form-wrapper">
            <el-form label-position="right"
                     label-width="80px"
                     :model="form"
                     ref="form">
              <el-form-item label="父级节点"
                            prop="parentId">
                <el-input v-model.trim="form.parentId"
                          :disabled="true"
                          placeholder="请输入父级节点"></el-input>
              </el-form-item>
              <el-form-item label="节点ID"
                            prop="menuId">
                <el-input v-model.trim="form.menuId" type="number"
                          :disabled="formEdit || formStatus === 'update'"
                          placeholder="请输入节点ID"></el-input>
              </el-form-item>
              <el-form-item label="标题"
                            prop="name">
                <el-input v-model.trim="form.name"
                          :disabled="formEdit"
                          placeholder="请输入标题"></el-input>
              </el-form-item>
              <el-form-item label="类型"
                            prop="type">
                <el-select class="filter-item"
                           v-model.trim="form.type"
                           :disabled="formEdit"
                           placeholder="请输入资源请求类型">
                  <el-option v-for="item in  typeOptions"
                             :key="item"
                             :label="item | typeFilter"
                             :value="item"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="权限标识"
                            prop="permission" v-if="form.type === '1'">
                <el-input v-model.trim="form.permission"
                          :disabled="formEdit"
                          placeholder="请输入权限标识"></el-input>
              </el-form-item>
              <el-form-item label="图标" v-if="form.type === '0'"
                            prop="icon">
                <el-input v-model.trim="form.icon"
                          :disabled="formEdit"
                          placeholder="请输入图标"></el-input>
              </el-form-item>
              <el-form-item label="排序" v-if="form.type === '0'"
                            prop="sort">
                <el-input type="number"
                          v-model.trim="form.sort"
                          :disabled="formEdit"
                          placeholder="请输入排序"></el-input>
              </el-form-item>
              <el-form-item label="前端组件" v-if="form.type === '0'"
                            prop="component">
                <el-input v-model.trim="form.component"
                          :disabled="formEdit"
                          placeholder="请输入描述"></el-input>
              </el-form-item>
              <el-form-item label="前端地址" v-if="form.type === '0'"
                            prop="component">
                <el-input v-model.trim="form.path"
                          :disabled="formEdit"
                          placeholder="iframe嵌套地址"></el-input>
              </el-form-item>
              <el-form-item label="路由缓冲" v-if="form.type === '0'"
                            prop="component">
                <el-switch v-model.trim="form.keepAlive"
                           :disabled="formEdit"
                           active-color="#13ce66"
                           inactive-color="#ff4949"
                           :active-value='"0"'
                           :inactive-value='"1"'>
                </el-switch>
              </el-form-item>
              <el-form-item v-if="formStatus === 'update'">
                <el-button type="primary"
                           @click="update">更 新
                </el-button>
                <el-button @click="onCancel">取 消</el-button>
              </el-form-item>
              <el-form-item v-if="formStatus === 'create'">
                <el-button type="primary"
                           @click="create">保 存
                </el-button>
                <el-button @click="onCancel">取 消</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </div>
    <!--操作-->
    <div>
      <span class="close" @click="close">
          <i class="el-icon-close"></i>
        </span>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import {Component, Watch} from 'vue-property-decorator'
  import Tree from './Tree'


  @Component({
    components: {Tree},
    filters: {
      typeFilter(type) {
        const typeMap = {
          0: '菜单',
          1: '按钮'
        };
        return typeMap[type]
      }
    }
  })
  export default class EditRole extends Vue {
    name: string = 'EditRole';

    filterText: string = '';
    menuTreedata: Array<Object> = [];
    defaultProps: Object = {
      children: 'children',
      label: 'label'
    };

    // 左侧form表单
    form: Object = {
      permission: undefined,
      name: undefined,
      menuId: undefined,
      parentId: undefined,
      icon: undefined,
      sort: undefined,
      component: undefined,
      type: undefined,
      path: undefined
    };
    // form表单能否编辑
    formEdit: boolean = true;
    // form处于的状态
    formStatus: string = '';
    // 当前被选中的node id
    currentId: number = -1;
    showElement: boolean = false;
    typeOptions: Array<string> = ['0', '1'];
    aExpandedKeys: Array<any> = [];
    oExpandedKey: Object = {
      // key (from tree id) : expandedOrNot boolean
    };
    oTreeNodeChildren: Object = {
      // id1 : [children] (from tree node id1)
      // id2 : [children] (from tree node id2)
    };

    @Watch('filterText')
    public filterTextChange(val): void {
      this.$refs.tree['filter'](val);
    }

    public mounted(): void {
      this.getMenuTreeData();
    }


    nodeExpand(data) {
      let aChildren = data.children
      if (aChildren.length > 0) {
        this.oExpandedKey[data.id] = true
        this.oTreeNodeChildren[data.id] = aChildren
      }
      this.setExpandedKeys()
    }

    nodeCollapse(data) {
      this.oExpandedKey[data.id] = false
      // 如果有子节点
      this.treeRecursion(this.oTreeNodeChildren[data.id], (oNode) => {
        this.oExpandedKey[oNode.id] = false
      });
      this.setExpandedKeys()
    }

    treeRecursion(aChildren, fnCallback) {
      if (aChildren) {
        for (let i = 0; i < aChildren.length; ++i) {
          let oNode = aChildren[i]
          fnCallback && fnCallback(oNode)
          this.treeRecursion(oNode.children, fnCallback)
        }
      }
    }

    setExpandedKeys() {
      let oTemp = this.oExpandedKey;
      this.aExpandedKeys = []
      for (let sKey in oTemp) {
        if (oTemp[sKey]) {
          this.aExpandedKeys.push(parseInt(sKey));
        }
      }
    }

    // 菜单删除按钮
    public handleDelete() {
      this.$confirm('此操作将永久删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let url = `/MetadataManage/menu/${this.currentId}`;
        this.$http.delete(url).then(response => {
          if (response.data.code === 0) {
            this.$message.success('删除成功');
            this.getMenuTreeData();
            this.onCancel();
          } else {
            this.$message.error(response.data.msg)
          }
        })
      })
    }


    // 菜单编辑按钮
    public handlerEdit(): void {
      if (this.form['menuId']) {
        this.formEdit = false
        this.formStatus = 'update'
      }
    }

    // 重置form表单
    public resetForm(): void {
      this.form = {
        permission: undefined,
        name: undefined,
        menuId: undefined,
        parentId: this.currentId,
        icon: undefined,
        sort: undefined,
        component: undefined,
        type: undefined,
        path: undefined
      }
    }


    // 新增
    public create(): void {
      let url = 'admin/menu';
      let form = JSON.parse(JSON.stringify(this.form));
      for (let key in form) {
        if (form[key] === undefined) {
          delete form[key];
        }
      }
      this.$http.post(url, form).then(response => {
        if (response.data.code === 0) {
          this.$message.success('新增成功');
          this.getMenuTreeData();
          this.onCancel();
        } else {
          this.$message.error(response.data.msg)
        }
      })
    }

    // 更新
    public update(): void {
      let url = 'admin/menu';
      let form = JSON.parse(JSON.stringify(this.form));
      for (let key in form) {
        if (form[key] === undefined) {
          delete form[key];
        }
      }
      this.$http.put(url, form).then(response => {
        if (response.data.code === 0) {
          this.$message.success('更新成功');
          this.getMenuTreeData();
          this.onCancel();
        } else {
          this.$message.error(response.data.msg)
        }
      })
    }

    // 菜单添加按钮
    public handlerAdd(): void {
      this.resetForm();
      this.formEdit = false;
      this.formStatus = 'create'
    }

    // 取消按钮
    public onCancel(): void {
      this.formEdit = true;
      this.formStatus = '';
    }


    // 点击菜单节点获取菜单详情数据
    public getNodeData(data): void {
      this.onCancel();
      if (!this.formEdit) {
        this.formStatus = 'update'
      }
      this.currentId = data.id;
      this.showElement = true;
      let url = `/MetadataManage/menu/${data['id']}`;
      this.$http.get(url).then(response => {
        this.form = response.data.data
      })
    }


    // 获取菜单tree树数据
    public getMenuTreeData(): void {
      let url = '/MetadataManage/menu/tree';
      this.$http.get(url).then(response => {
        const res = response.data;
        if (res.code === 0) {
          this.menuTreedata = res.data;
        }
      })
    }

    // 搜索框过滤
    public filterNode(value, data): boolean {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    }

    // 关闭模态框
    public close(): void {
      this.$emit('close')
    }

  }
</script>

<style scoped lang="less">
  .edit-role {
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    z-index: 1000;
    /*padding: 15px;*/
    background-color: #fff;

    // 授权区域wrapper
    .outer-wrapper {
      width: 100%;
      height: 100%;
      display: flex;
      flex-direction: column;
      // 标题
      .title {
        text-align: center;
        line-height: 60px;
        font-size: 20px;
        font-weight: bold;
      }

      .wrapper {
        margin: 0 15px 15px 15px;
        flex: 1;
        box-shadow: 0 0 10px #cccccc;
        overflow: auto;
        display: flex;
        // 左侧菜单区域
        .left {
          flex: 2;
          background-color: #fff;
          padding: 15px;
          position: relative;

          .menu-btn {
            position: absolute;
            top: 15px;
            left: 15px;
            right: 0;
            z-index: 10;
            background-color: #fff;
          }

          // 菜单tree
          .menu-tree {
            height: 100%;
            width: 100%;
            padding-top: 40px;
            overflow: auto;
          }
        }

        // 右侧授权区域
        .right {
          flex: 5;
          background-color: #d1dbe5;
          padding: 15px;
          display: flex;

          .form-wrapper {
            flex: 1;
            box-shadow: 0 0 10px #ccc;
            padding: 15px;
            background-color: #fff;
            overflow: auto;
          }
        }

      }
    }

    // 关闭
    .close {
      position: absolute;
      top: 18px;
      right: 18px;
      width: 20px;
      height: 20px;
      border-radius: 50%;
      // background-color: rgba(0, 0, 0, 0.3);
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #909399;

      .el-icon-close {
        font-size: 18px;
      }

      &:hover {
        color: #188bf5;
      }
    }
  }
</style>
