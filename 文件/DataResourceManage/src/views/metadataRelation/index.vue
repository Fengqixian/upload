<template>
    <div class="metadata-relation cb-tree-show-operation-wrapper" ref="metadataRelation">
        <div class="tree-wrapper">
            <cb-tree lazy
                     searchFromHttp
                     @search="searchTree"
                     v-show="searchVal === ''"
                     @load-node="loadNode"
                     ref="tree1"
                     @node-click="nodeClick">
                <template slot-scope="{data,node}">
                    <div class="tree-name">
                        <span>
                            {{data.nameCn || data.nameEn || 'unknown'}}
                        </span>
                    </div>
                </template>
            </cb-tree>
            <cb-tree @search="searchTree"
                     searchFromHttp
                     v-show="searchVal !== ''"
                     :data="treeData"
                     ref="tree2"
                     @node-click="nodeClick">
                <template slot-scope="{data,node}">
                    <div class="tree-name">
                        <span>
                            {{data.nameCn || data.nameEn || 'unknown'}}
                        </span>
                    </div>
                </template>
            </cb-tree>
        </div>
        <div class="relation-wrapper cb-relation-wrapper show-operation-wrapper">
            <cb-tab v-model.trim="tabDataActive" :tabData="tabData">
                <template :slot="tab.name" v-for="tab in tabData">
                    <relation :id="tab.name"
                              :containerWidth="relationWidth"
                              :containerHeight="relationHeight"
                              :relationData="tab.relationData"
                              :headerId="tab.headerResourceId">
                    </relation>
                </template>
            </cb-tab>
        </div>
    </div>
</template>

<script lang="ts">
    import Vue from 'vue'
    import {Component} from 'vue-property-decorator'
    import config from "../../config";
    import Relation from './relation'

    @Component({components: {Relation}})
    export default class MetadataRelation extends Vue {
        name: string = 'MetadataRelation';
        public tabData: Array<object> = [];
        public tabDataActive: string = '';
        public relationHeight: number = 0;
        public relationWidth: number = 0;


        /**
         * tree 树区域
         */
        treeData: Array<object> = []; // 用于存储查询的数据
        searchVal: string = ''; // 搜索的值

        public mounted(): void {
            setTimeout(() => {
                /*const clientRect = this.$refs.metadataRelation['querySelector'](".el-tabs__content").getBoundingClientRect();
                this.relationWidth = clientRect['width'];
                this.relationHeight = clientRect['height'];*/

                /*let ret = {
                    "code": 0, "msg": "success", "data": {
                        "nodeDataArray": [
                            {
                                color: 1,
                                "id": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "name": "dataresflag",
                                "parentId": "5228179c-8c4d-4a2c-81ad-adf7db67ea5f",
                                "moduleId": "d64ce09e-4e71-40d7-b8e5-8b7bdcdefdee",
                                "type": "element_item",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "5d643850-5e48-4461-bd1b-d4b882feab8e",
                                "name": "dataresflag",
                                "parentId": "64662c23-b0b2-4bf8-914b-99492e125af9",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "93fe3934-e14b-4df8-a333-a584bec48864",
                                "name": "dataresflag",
                                "parentId": "8a273cb0-2c57-4929-8796-378ae3765cc0",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "2cdaa29c-db79-41f7-bf1a-86ec7d0d5479",
                                "name": "dataresflag",
                                "parentId": "ffb25245-40b2-40ff-80bd-6947ecedef49",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "7658faf0-2dd5-4fd6-909f-4ef67dbb00ca",
                                "name": "dataresflag",
                                "parentId": "b28df04a-56df-43d0-b309-a78d0e3bb85b",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "b8c13ae1-0063-4033-af7e-73b629562bd4",
                                "name": "dataresflag",
                                "parentId": "2501f3f2-819c-41bc-93ea-b3ccf7d9221b",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "39569ca9-a915-4395-a939-c515ec1c2806",
                                "name": "dataresflag",
                                "parentId": "932387cc-3bbf-4ab2-a412-d6d8b905da39",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "a5431cce-ab87-4a3d-b8c4-628f1e2dd488",
                                "name": "dataresflag",
                                "parentId": "a53678a4-df3d-4cb9-b528-2ed527d0e6bd",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "2a982463-3cd1-4e91-9871-342190c65407",
                                "name": "dataresflag",
                                "parentId": "77fdca6d-e9a9-462d-974f-6d2413403c4b",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "fd5ec7c5-236e-4d40-b0e1-f2677d35f9c9",
                                "name": "dataresflag",
                                "parentId": "fa35621c-2d9e-45b2-a7c3-6f26ef07da31",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "11dd79a3-d6fd-4658-8743-defdc4905660",
                                "name": "dataresflag",
                                "parentId": "7ee42ee8-24b2-4bbb-a87e-4fadbc5a611b",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "b2840aa5-07eb-431a-8fdd-bca16a087d1b",
                                "name": "dataresflag",
                                "parentId": "a72b938b-8dc1-469a-ba1c-4c42ca513a58",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "46f9e983-4b1e-488d-9f8e-6e9a2014a14b",
                                "name": "dataresflag",
                                "parentId": "b19ae28f-d3bf-48eb-bb85-06a8b4a3daa4",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "8142e8b3-8f66-40fa-b443-777fd3337618",
                                "name": "dataresflag",
                                "parentId": "8d3f74f9-9eff-4fdf-9787-01f014c25831",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "b222181b-09e1-4121-81e2-4de5fbcb1006",
                                "name": "dataresflag",
                                "parentId": "5198fbdb-91a0-43f6-8b5f-a5a57a50d115",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "594a1bd9-0110-4580-a3ee-e26684a0e249",
                                "name": "dataresflag",
                                "parentId": "86f53dc2-1fdc-49a0-995f-6ae3461e51b9",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "38bacdf4-3c10-4b4a-9347-f4a8fb1f15eb",
                                "name": "dataresflag",
                                "parentId": "232795ed-1969-4aa5-acc4-2d4113037972",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "acd3f856-7d7f-43e1-a7cb-0ae72c788ca8",
                                "name": "dataresflag",
                                "parentId": "bf788a25-2623-4100-b416-67206b28286b",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "755364f8-62d5-4a98-a91b-65ebe60f8cbd",
                                "name": "dataresflag",
                                "parentId": "3d7ee919-f150-41f1-9ffe-1a5057a1edea",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "875b43f5-025f-4d9d-96bb-524e332ee43f",
                                "name": "dataresflag",
                                "parentId": "3a66d211-c390-46cd-bb6a-30f985425c30",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "a8cc5578-ec0c-491f-aea8-974021092320",
                                "name": "dataresflag",
                                "parentId": "4c9e0031-fccd-45cf-9ad7-961212b32429",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "79a1d6b8-8419-45a6-8616-0f29c53b50a4",
                                "name": "dataresflag",
                                "parentId": "796be52c-0bec-4b37-b432-49e31bfe3450",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "3c38288d-ca55-49fe-b6b3-09c422570ca6",
                                "name": "dataresflag",
                                "parentId": "d14245bb-4e82-4eee-9175-087f39fc2145",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "462cba70-0323-44b3-9285-d3509b2167e0",
                                "name": "dataresflag",
                                "parentId": "52a61a0a-5648-4788-bc7d-9153e5428838",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "3b5a2e62-3cce-4b37-874f-7bb291eba2c0",
                                "name": "dataresflag",
                                "parentId": "b2759c7c-114b-4383-a871-82c03a11c2a5",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "47a2ea00-4d5d-4276-8552-e7110bc1695f",
                                "name": "dataresflag",
                                "parentId": "474dd585-1cc9-4974-b92f-5a5a2f2e4d03",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "d29cc59e-6274-4c4e-ad9a-25b2b9e91b05",
                                "name": "dataresflag",
                                "parentId": "554d676e-62f7-4a90-87ad-f39539fe71b4",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "80b16f89-6414-4729-9e30-b52a9372f8ad",
                                "name": "dataresflag",
                                "parentId": "1b350ba8-9397-4528-9cc1-9060c6080ff1",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "3510ecec-0686-4340-839f-4ed94580e239",
                                "name": "dataresflag",
                                "parentId": "2114a53b-e39d-4861-8a33-d5c1b0eaeb1e",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "355edb24-c8e3-4370-88dc-5740a8140fcc",
                                "name": "dataresflag",
                                "parentId": "0056a100-2288-4e00-825b-1f5bf365f546",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "92786a1c-9c66-4dd8-84dd-97c1ecb2ddb0",
                                "name": "dataresflag",
                                "parentId": "46c98695-506e-466f-a3e2-005564657ab7",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "abaeeae9-3265-4cda-9f23-c4eda2d55749",
                                "name": "dataresflag",
                                "parentId": "a07af39c-4032-4e85-9f80-78308224d0b7",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "b63c155f-44c3-453d-a5b5-477bf5ec1151",
                                "name": "dataresflag",
                                "parentId": "fdfe4859-2d2f-45a2-8740-c2f1b93d53a7",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "c44a88e5-0f2a-4c1d-bbba-04b50c41389c",
                                "name": "dataresflag",
                                "parentId": "d283daf8-44a4-4e90-944a-8d66b15f7e56",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "aadb7549-e5ae-4710-9cb2-2e759fe70503",
                                "name": "dataresflag",
                                "parentId": "56765d99-1821-4f4c-aca4-3e3bcc3ea60b",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "3284d0e4-355b-4f8a-a2cd-8e8ae488b825",
                                "name": "dataresflag",
                                "parentId": "b469518f-8d7a-47f9-ae67-5fd1a05b663b",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "982c0e57-6f81-4843-85a2-ed6309ba45c6",
                                "name": "dataresflag",
                                "parentId": "06caf84e-bc6b-450b-bfaa-30a7c9c3697c",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "2ae1ed74-fdbc-4d18-a0d3-df0c18f7f775",
                                "name": "dataresflag",
                                "parentId": "611f57ca-a795-4f04-804f-36d49c99344f",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "e5a90834-fa37-4ea9-aa00-435167885c75",
                                "name": "dataresflag",
                                "parentId": "20004152-4c07-4614-9a45-36c5d449e903",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "d6e8cd1f-b291-4774-b7ea-9da82ee58efe",
                                "name": "dataresflag",
                                "parentId": "bf82c093-99e8-410e-9e35-907b1f22818d",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "c2a811fb-06ea-4122-985f-7b4aa3c1d139",
                                "name": "dataresflag",
                                "parentId": "8054adc4-87de-4564-8447-520b86c57661",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "ac35517e-3f7b-4bad-89e8-3aa8a5d9d46f",
                                "name": "dataresflag",
                                "parentId": "56f629b5-0ae5-43fd-b041-f2105e7f8159",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "e483913a-82d9-4ecd-b4bf-764c04147a25",
                                "name": "dataresflag",
                                "parentId": "41828911-a05f-4f51-bc66-f860d01fb776",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "6766336e-16c4-4f65-8cb4-21d0cd187fa0",
                                "name": "dataresflag",
                                "parentId": "34c62859-97cd-4b36-bbe4-4706d46263f8",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "8b486a40-eacb-4ba1-8803-2104a188c954",
                                "name": "dataresflag",
                                "parentId": "36fbaf5a-124d-404c-9c5d-cc8931e07e77",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "6866a3b3-bdfc-40be-aaa6-7e68a0e9e3b6",
                                "name": "dataresflag",
                                "parentId": "f38f11c5-061e-469b-8819-90cacbcf33d9",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "0f2c020e-6de7-4e26-943e-caef016f89d5",
                                "name": "dataresflag",
                                "parentId": "204a51ab-9634-453a-941a-32faec89bfa5",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "4704b9d2-d460-4f8f-800f-357d26967240",
                                "name": "dataresflag",
                                "parentId": "3fb1def0-3526-4f94-82a5-5ff5e6925572",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "92d1eb02-edee-41d1-9cf8-c81761a22d0f",
                                "name": "dataresflag",
                                "parentId": "3e5502b1-0c39-4fbe-89b3-4c5787f0c88f",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "332ebc26-76f0-4b91-8272-dc66c57bdccb",
                                "name": "dataresflag",
                                "parentId": "70cfe5ed-d70a-4d6a-88cc-3ed6eb2de6b5",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "88194730-9277-4c5b-a6b0-f9f23f019e6d",
                                "name": "dataresflag",
                                "parentId": "2cf72428-9a77-46d1-bd62-7a48f5a3c735",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "361745a9-e6b7-40b7-99e6-1a680ad727f7",
                                "name": "dataresflag",
                                "parentId": "10653b98-4415-46ed-a2bb-db211d77ff2a",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }, {
                                color: 1,
                                "id": "d7d88325-9d22-4291-b1a1-1036021a90aa",
                                "name": "dataresflag",
                                "parentId": "476de5c2-0e83-4623-9c49-77010d329418",
                                "moduleId": "c22d1da2-afbb-4604-9f0e-54b2a1bd829a",
                                "type": "column",
                                "isGroup": null,
                                "comment": null
                            }],
                        "linkDataArray": [
                            {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "4704b9d2-d460-4f8f-800f-357d26967240",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 47
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "88194730-9277-4c5b-a6b0-f9f23f019e6d",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 50
                            }, {
                                "from": "3c38288d-ca55-49fe-b6b3-09c422570ca6",
                                "to": "6766336e-16c4-4f65-8cb4-21d0cd187fa0",
                                "comment": "etl-apply_lab-apply_lab",
                                "type": "etl",
                                "color": null,
                                "source": 22,
                                "target": 43
                            }, {
                                "from": "8142e8b3-8f66-40fa-b443-777fd3337618",
                                "to": "4704b9d2-d460-4f8f-800f-357d26967240",
                                "comment": "etl-mt_image-mt_image",
                                "type": "etl",
                                "color": null,
                                "source": 13,
                                "target": 47
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "462cba70-0323-44b3-9285-d3509b2167e0",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 23
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "2a982463-3cd1-4e91-9871-342190c65407",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 8
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "92d1eb02-edee-41d1-9cf8-c81761a22d0f",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 48
                            }, {
                                "from": "39569ca9-a915-4395-a939-c515ec1c2806",
                                "to": "e5a90834-fa37-4ea9-aa00-435167885c75",
                                "comment": "etl-mt_specimen-mt_specimen",
                                "type": "etl",
                                "color": null,
                                "source": 6,
                                "target": 38
                            }, {
                                "from": "355edb24-c8e3-4370-88dc-5740a8140fcc",
                                "to": "332ebc26-76f0-4b91-8272-dc66c57bdccb",
                                "comment": "etl-apply_ops-apply_ops",
                                "type": "etl",
                                "color": null,
                                "source": 29,
                                "target": 49
                            }, {
                                "from": "8b486a40-eacb-4ba1-8803-2104a188c954",
                                "to": "b222181b-09e1-4121-81e2-4de5fbcb1006",
                                "comment": "etl-mh_nisdataset-mh_nisdataset",
                                "type": "etl",
                                "color": null,
                                "source": 44,
                                "target": 14
                            }, {
                                "from": "ac35517e-3f7b-4bad-89e8-3aa8a5d9d46f",
                                "to": "c2a811fb-06ea-4122-985f-7b4aa3c1d139",
                                "comment": "etl-pers_extinfo-pers_extinfo",
                                "type": "etl",
                                "color": null,
                                "source": 41,
                                "target": 40
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "3b5a2e62-3cce-4b37-874f-7bb291eba2c0",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 24
                            }, {
                                "from": "a8cc5578-ec0c-491f-aea8-974021092320",
                                "to": "3510ecec-0686-4340-839f-4ed94580e239",
                                "comment": "etl-dict_hospdiag-dict_hospdiag",
                                "type": "etl",
                                "color": null,
                                "source": 20,
                                "target": 28
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "11dd79a3-d6fd-4658-8743-defdc4905660",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 10
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "3510ecec-0686-4340-839f-4ed94580e239",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 28
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "755364f8-62d5-4a98-a91b-65ebe60f8cbd",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 18
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "46f9e983-4b1e-488d-9f8e-6e9a2014a14b",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 12
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "c2a811fb-06ea-4122-985f-7b4aa3c1d139",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 40
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "332ebc26-76f0-4b91-8272-dc66c57bdccb",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 49
                            }, {
                                "from": "acd3f856-7d7f-43e1-a7cb-0ae72c788ca8",
                                "to": "b8c13ae1-0063-4033-af7e-73b629562bd4",
                                "comment": "etl-apply_consultationsub-apply_consultationsub",
                                "type": "etl",
                                "color": null,
                                "source": 17,
                                "target": 5
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "47a2ea00-4d5d-4276-8552-e7110bc1695f",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 25
                            }, {
                                "from": "982c0e57-6f81-4843-85a2-ed6309ba45c6",
                                "to": "875b43f5-025f-4d9d-96bb-524e332ee43f",
                                "comment": "etl-pers_basicinfo-pers_basicinfo",
                                "type": "etl",
                                "color": null,
                                "source": 36,
                                "target": 19
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "a5431cce-ab87-4a3d-b8c4-628f1e2dd488",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 7
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "b8c13ae1-0063-4033-af7e-73b629562bd4",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 5
                            }, {
                                "from": "7658faf0-2dd5-4fd6-909f-4ef67dbb00ca",
                                "to": "3b5a2e62-3cce-4b37-874f-7bb291eba2c0",
                                "comment": "etl-mh_nistemplate-mh_nistemplate",
                                "type": "etl",
                                "color": null,
                                "source": 4,
                                "target": 24
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "e5a90834-fa37-4ea9-aa00-435167885c75",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 38
                            }, {
                                "from": "2ae1ed74-fdbc-4d18-a0d3-df0c18f7f775",
                                "to": "fd5ec7c5-236e-4d40-b0e1-f2677d35f9c9",
                                "comment": "etl-dict_staffgroup-dict_staffgroup",
                                "type": "etl",
                                "color": null,
                                "source": 37,
                                "target": 9
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "6766336e-16c4-4f65-8cb4-21d0cd187fa0",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 43
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "d6e8cd1f-b291-4774-b7ea-9da82ee58efe",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 39
                            }, {
                                "from": "abaeeae9-3265-4cda-9f23-c4eda2d55749",
                                "to": "6866a3b3-bdfc-40be-aaa6-7e68a0e9e3b6",
                                "comment": "etl-visit_record-visit_record",
                                "type": "etl",
                                "color": null,
                                "source": 31,
                                "target": 45
                            }, {
                                "from": "79a1d6b8-8419-45a6-8616-0f29c53b50a4",
                                "to": "b63c155f-44c3-453d-a5b5-477bf5ec1151",
                                "comment": "etl-apply_consultation-apply_consultation",
                                "type": "etl",
                                "color": null,
                                "source": 21,
                                "target": 32
                            }, {
                                "from": "d7d88325-9d22-4291-b1a1-1036021a90aa",
                                "to": "2cdaa29c-db79-41f7-bf1a-86ec7d0d5479",
                                "comment": "etl-visit_nonhospreg-visit_nonhospreg",
                                "type": "etl",
                                "color": null,
                                "source": 52,
                                "target": 3
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "92786a1c-9c66-4dd8-84dd-97c1ecb2ddb0",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 30
                            }, {
                                "from": "594a1bd9-0110-4580-a3ee-e26684a0e249",
                                "to": "2a982463-3cd1-4e91-9871-342190c65407",
                                "comment": "etl-mh_cdamaindoc-mh_cdamaindoc",
                                "type": "etl",
                                "color": null,
                                "source": 15,
                                "target": 8
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "b63c155f-44c3-453d-a5b5-477bf5ec1151",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 32
                            }, {
                                "from": "c44a88e5-0f2a-4c1d-bbba-04b50c41389c",
                                "to": "92d1eb02-edee-41d1-9cf8-c81761a22d0f",
                                "comment": "etl-dict_hospops-dict_hospops",
                                "type": "etl",
                                "color": null,
                                "source": 33,
                                "target": 48
                            }, {
                                "from": "b2840aa5-07eb-431a-8fdd-bca16a087d1b",
                                "to": "a5431cce-ab87-4a3d-b8c4-628f1e2dd488",
                                "comment": "etl-ops_eventmain-ops_eventmain",
                                "type": "etl",
                                "color": null,
                                "source": 11,
                                "target": 7
                            }, {
                                "from": "e483913a-82d9-4ecd-b4bf-764c04147a25",
                                "to": "92786a1c-9c66-4dd8-84dd-97c1ecb2ddb0",
                                "comment": "etl-med_reportitemsub-med_reportitemsub",
                                "type": "etl",
                                "color": null,
                                "source": 42,
                                "target": 30
                            }, {
                                "from": "361745a9-e6b7-40b7-99e6-1a680ad727f7",
                                "to": "46f9e983-4b1e-488d-9f8e-6e9a2014a14b",
                                "comment": "etl-pers_adversereactioninfo-pers_adversereactioninfo",
                                "type": "etl",
                                "color": null,
                                "source": 51,
                                "target": 12
                            }, {
                                "from": "80b16f89-6414-4729-9e30-b52a9372f8ad",
                                "to": "d6e8cd1f-b291-4774-b7ea-9da82ee58efe",
                                "comment": "etl-visit_hospreg-visit_hospreg",
                                "type": "etl",
                                "color": null,
                                "source": 27,
                                "target": 39
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "6866a3b3-bdfc-40be-aaa6-7e68a0e9e3b6",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 45
                            }, {
                                "from": "38bacdf4-3c10-4b4a-9347-f4a8fb1f15eb",
                                "to": "d29cc59e-6274-4c4e-ad9a-25b2b9e91b05",
                                "comment": "etl-med_reportdetail-med_reportdetail",
                                "type": "etl",
                                "color": null,
                                "source": 16,
                                "target": 26
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "d29cc59e-6274-4c4e-ad9a-25b2b9e91b05",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 26
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "875b43f5-025f-4d9d-96bb-524e332ee43f",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 19
                            }, {
                                "from": "3284d0e4-355b-4f8a-a2cd-8e8ae488b825",
                                "to": "11dd79a3-d6fd-4658-8743-defdc4905660",
                                "comment": "etl-apply_obs-apply_obs",
                                "type": "etl",
                                "color": null,
                                "source": 35,
                                "target": 10
                            }, {
                                "from": "5d643850-5e48-4461-bd1b-d4b882feab8e",
                                "to": "47a2ea00-4d5d-4276-8552-e7110bc1695f",
                                "comment": "etl-med_report-med_report",
                                "type": "etl",
                                "color": null,
                                "source": 1,
                                "target": 25
                            }, {
                                "from": "93fe3934-e14b-4df8-a333-a584bec48864",
                                "to": "88194730-9277-4c5b-a6b0-f9f23f019e6d",
                                "comment": "etl-dict_dept-dict_dept",
                                "type": "etl",
                                "color": null,
                                "source": 2,
                                "target": 50
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "b222181b-09e1-4121-81e2-4de5fbcb1006",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 14
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "2cdaa29c-db79-41f7-bf1a-86ec7d0d5479",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 3
                            }, {
                                "from": "aadb7549-e5ae-4710-9cb2-2e759fe70503",
                                "to": "755364f8-62d5-4a98-a91b-65ebe60f8cbd",
                                "comment": "etl-order_master-order_master",
                                "type": "etl",
                                "color": null,
                                "source": 34,
                                "target": 18
                            }, {
                                "from": "780820aa-1169-4ca6-98cc-192698a1d1e7",
                                "to": "fd5ec7c5-236e-4d40-b0e1-f2677d35f9c9",
                                "comment": null,
                                "type": "column_column",
                                "color": null,
                                "source": 0,
                                "target": 9
                            }, {
                                "from": "0f2c020e-6de7-4e26-943e-caef016f89d5",
                                "to": "462cba70-0323-44b3-9285-d3509b2167e0",
                                "comment": "etl-mh_nisdetail-mh_nisdetail",
                                "type": "etl",
                                "color": null,
                                "source": 46,
                                "target": 23
                            }]
                    }
                };
                this.tabData.push({
                    label: '测试拖拽',
                    name: 'test',
                    relationData: ret.data
                });
                this.tabDataActive = 'test'*/
            }, 20)
        }

        /**
         * 节点被点击时的回调
         * @param data  新数据
         * @param node 需要刷新的node节点
         * @param component 当前组件
         * */
        public nodeClick(data, node, component): void {
            if ((data.modelType === 'element_item' || data.modelType === 'column') && data.type === 'INS') {
                let url = config.port('dataLineage') + '/getDataLineage/' + data['resourceId'];
                this.$http.get(url).then(response => {
                    const res = response.data;
                    if (res.code === 0 && res.data !== null) {
                        this.tabData.push({
                            label: data.nameCn || data.nameEn || 'unknown',
                            name: data.uuid,
                            headerResourceId: data['resourceId'],
                            relationData: res.data
                        });
                        const clientRect = this.$refs.metadataRelation['querySelector'](".el-tabs__content").getBoundingClientRect();
                        this.relationWidth = clientRect['width'];
                        this.relationHeight = clientRect['height'];
                        this.tabDataActive = data.uuid;
                    } else {
                        this.$message.warning('暂无此元数据血缘关系')
                    }
                })
            } else {
                this.$message.warning('只有基础元数据、字段有血缘关系')
            }

        }


        /**
         * 树搜索
         */
        public searchTree(queryString): void {
            this.$refs.tree1['emitSearch'](queryString);
            this.$refs.tree2['emitSearch'](queryString);
            if (queryString === '') {
                this.treeData = [];
                this.searchVal = queryString;
            } else {
                let url = config.port('metadatavalue') + '/tree/lazyTree';
                let params: object = {
                    queryString
                };
                this.$http.get(url, {params}).then(response => {
                    this.treeData = response.data.data;
                    this.searchVal = queryString;
                });
            }
        }

        /**
         * 懒加载树
         * @param node node节点
         * @param resolve
         */
        public loadNode(node, resolve): void {
            let url = config.port('metadatavalue') + '/tree/lazyTree';
            let params: object = {
                parentUuid: ''
            };
            if (node.level !== 0) {
                params['parentUuid'] = node.data.uuid
            }
            // 只有根模型过滤已审批数据
            if (node.data && node.data.isStandard === true) {
                params['status'] = 1
            }
            this.$http.get(url, {params}).then(response => {
                resolve(response.data.data);
            });
        }
    }
</script>

<style scoped lang="less">
    .metadata-relation {
        display: flex;

        //  tree盒子
        .tree-wrapper {
            width: 300px;
            height: 100%;
            // tree树展示名称区域
            .tree-name {
                width: 100%;

                /*
                 &:hover {
                     .operation-wrapper {
                         visibility: visible;
                     }
                 }

                 .operation-wrapper {
                     visibility: hidden;
                     position: absolute;
                     right: 10px;
                     top: 0;
                     bottom: 0;
                     display: flex;
                     align-items: center;

                     i {
                         margin: 0 5px;

                         &:hover {
                             color: #188bf5;
                         }
                     }
                 }
                 */
            }
        }

    }
</style>
