/**
 * 第一步：递归算出每个group的高度和宽度(适用于n个group包裹)
 * 第二步：将每个shape学找到自己的group并呈现在group内
 **/
import Shape from './shape'
import Line from './line'
import * as d3 from "d3";

class Svg {
    constructor(arg) {
        /*
        * @params:arg
        * select 容器
        * data 数据
        * */
        this.select = arg.select; // 容器
        this.container = d3.select(arg.select); // d3获取容器
        this.container.attr('style', 'position:relative;');
        this.select = this.select.replace(/^#/, '');
        this.data = arg.data; // 数据
        this.shapeHInterval = arg.shapeHInterval || 100; // 图形与图形水平方向之间的间隔距离
        this.shapeVInterval = arg.shapeVInterval || 100; // 图形与图形纵向之间的间隔距离
        this.shapeTitleSize = arg.shapeTitleSize || 10; // 图形的title
        this.originShapeHInterval = this.shapeHInterval; // 图形与图形水平方向之间的间隔距离
        this.originShapeVInterval = this.shapeVInterval; // 图形与图形纵向之间的间隔距离
        this.originShapeTitleSize = this.shapeTitleSize; // 图形的title

        /*
        * @params 图形的属性
        * @iconSize  图标的尺寸
        * @iconColor 图形颜色
        * @textStyle 图形名称
        * */
        this.iconSize = parseFloat(arg.iconSize) || 50;
        this.originIconSize = this.iconSize;
        this.iconColor = arg.iconColor || '#2c3e50';
        this.textStyle = arg.textStyle || {};


        /*
      * @stroke  line的颜色
      * @strokeWidth  line的宽度
      * @shapeHeight  被链接物体的高度
      * @ballRadius  // 小球的半径
      * @ballColor  // 小球的颜色
      * @ballSpeed  // 小球的运行速度
      * */
        this.lineColor = arg.lineColor || '#262f52';
        this.strokeWidth = arg.strokeWidth || 1;
        this.ballRadius = arg.ballRadius || 2;
        this.ballSpeed = arg.ballSpeed || 30;
        this.ballColor = arg.ballColor || 'red';

        /*
        * @group
        * */
        this.groupPadding = {top: 20, right: 50, bottom: 20, left: 50};
        this.originGroupPadding = JSON.parse(JSON.stringify(this.groupPadding));
        this.groupMargin = {top: 10, right: 10, bottom: 10, left: 10};
        this.originGroupMargin = JSON.parse(JSON.stringify(this.groupMargin));
        this.groupTitleSize = 14;
        this.originGroupTitleSize = this.groupTitleSize;

        /* *
        * 处理shape group位置变量
        * originalShape：存放源头shape
        * sortNum：一级group排序编号
        * fatherGroup：存放一级group
        * linkOtherOneLevelGroup：存放link的to和from不在同一个一级group中
        * */
        this.originalShape = [];
        this.sortNum = 0;
        this.fatherGroup = [];
        this.linkOtherOneLevelGroup = []
    }

    // 渲染
    render() {
        this.svgSize();
        this.shapesToGroups();
        this.sortOneLevelGroups();  // 处理一级group的排序入口函数（根据link进行排序）
        this.sortTwoLevelGroups();  // 处理二级group的排序入口函数（根据link进行排序）
        this.fitScreen();  // 通过比例尺让整个svg图在一个屏幕中显示
        this.calcLoc(); // 计算每个每个元素的坐标
        this.zoom();
    }

    // 放大缩小
    zoom() {

        const svg = d3.select(`#${this.select}_container`);
        svg.call(this.d3.behavior.zoom()
            .scaleExtent([1, 10])
            .on("zoom", zoomed));

        function zoomed() {
            circles_group.attr("transform",
                "translate(" + this.d3.event.translate + ")scale(" + this.d3.event.scale + ")");
        }
    }

    resize() {
        this.svgSize();
        this.fitScreen();  // 通过比例尺让整个svg图在一个屏幕中显示
        this.calcLoc(); // 计算每个每个元素的坐标
    }

    // 解析将data数据中的shape放到对应的groups中
    shapesToGroups() {
        // 抽取每个组中group的shape
        const {groups, shapes} = this.data;
        groups.map(group => {
            const groupShapes = shapes.filter(shape => {
                return group.group === shape.group
            });
            if (groupShapes.length) {
                return group.shapes = groupShapes
            } else {
                return group.shapes = []
            }
        });
    }

    // 处理group的入口函数（根据link进行排序）
    sortOneLevelGroups() {
        this.originalShape = []; // 初始化存放源头shape数据
        this.fatherGroup = []; // 初始化存放源头shape数据
        const {shapes, links, groups} = this.data;
        // 寻找最源头的shape  存放到 originalShape 中
        links.forEach(link => {
            this.searchOriginalShape(link)
        });
        this.sortNum = 0;
        groups.forEach(group => {
            group.sortNum = 0;  // 将每个group初始化一个记录此group中每个子一级group的排序编号
            this.searchOneLevelGroup(group);  // 寻找出一级group
        });
        // 根据源头shape依次给一级 group 排序横向顺序
        this.originalShape.forEach(shape => this.sortOneLevelGroupsNum(shape.id));
    }

    // 根据源头shape依次给一级 group 排序横向顺序
    sortOneLevelGroupsNum(shapeId) {
        const {shapes, links, groups} = this.data;
        const originLinks = links.filter(link => link.from === shapeId);
        originLinks.forEach(link => {
            // 判断link的from和to对应的是不是同一个一级group
            const isSameGroup = this.isSameOneLevelGroup(link);
            if (isSameGroup) {
                const nextLinks = links.filter(item => item.from === link.to);
                nextLinks.forEach(nextLink => this.sortOneLevelGroupsNum(nextLink.from));

                /* *
                * link的to下面如果没有对应的下级link的话，此时to对应的一级设置 horizontal 添加横向排序，否则继续递归
                * */
                const nextOriginLinks = links.filter(nextOriginalLink => nextOriginalLink.from === link.to);
                if (nextOriginLinks.length === 0) {
                    const group = groups.find(groupItem => {
                        const {shapes: groupShapes} = groupItem;
                        return groupShapes.find(item => item.id === link.to) !== undefined
                    });
                    const originGroup = {group: null};
                    this.searchOriginalOneLevelGroup(group, originGroup);
                    // 寻找出这个源头shape对应的一级group，给此 horizontal 添加横向排序,如果已经排过序了就不重新排序了
                    if (originGroup.group.horizontal === undefined) {
                        originGroup.group.horizontal = this.sortNum;
                        this.sortNum++;
                    }
                } else {
                    nextOriginLinks.forEach(item => this.sortOneLevelGroupsNum(item.from))
                }
            } else {
                /* *
                * 处理临界点，from对应的一级group
                * */
                const group = groups.find(groupItem => {
                    const {shapes: groupShapes} = groupItem;
                    return groupShapes.find(item => item.id === link.from) !== undefined;
                });
                const originGroup = {group: null};
                this.searchOriginalOneLevelGroup(group, originGroup);
                // 寻找出这个源头shape对应的一级group，给此 horizontal 添加横向排序,如果已经排过序了就不重新排序了
                if (originGroup.group.horizontal === undefined) {
                    originGroup.group.horizontal = this.sortNum;
                    this.sortNum++;
                }
                /* *
                * link的to下面如果没有对应的下级link的话，此时to对应的一级设置 horizontal 添加横向排序，否则继续递归
                * */
                const nextOriginLinks = links.filter(nextOriginalLink => nextOriginalLink.from === link.to);
                if (nextOriginLinks.length === 0) {
                    const group = groups.find(groupItem => {
                        const {shapes: groupShapes} = groupItem;
                        return groupShapes.find(item => item.id === link.to) !== undefined
                    });
                    const originGroup = {group: null};
                    this.searchOriginalOneLevelGroup(group, originGroup);
                    // 寻找出这个源头shape对应的一级group，给此 horizontal 添加横向排序,如果已经排过序了就不重新排序了
                    if (originGroup.group.horizontal === undefined) {
                        originGroup.group.horizontal = this.sortNum;
                        this.sortNum++;
                    }
                } else {
                    nextOriginLinks.forEach(item => this.sortOneLevelGroupsNum(item.from))
                }
            }
        });
    }

    // 判断link的from和to所指向的一级group是不是同一个group
    isSameOneLevelGroup(link) {
        const toOriginGroup = {group: null};
        const fromOriginGroup = {group: null};
        // 判断from shape 和to shape 是否在同一个一级group中
        this.searchOriginalOneLevelGroup(this.searchShapeIdGroup(link.from), toOriginGroup);
        this.searchOriginalOneLevelGroup(this.searchShapeIdGroup(link.to), fromOriginGroup);
        // 返回是否相同
        return toOriginGroup.group.group === fromOriginGroup.group.group
    }

    // 寻找出link的to和from不在同一个一级group中
    searchLinkOtherOneLevelGroup() {
        const {shapes, links, groups} = this.data;
        links.forEach(link => {
            const toOriginGroup = {group: null};
            const fromOriginGroup = {group: null};
            // 判断from shape 和to shape 是否在同一个一级group中
            this.searchOriginalOneLevelGroup(this.searchShapeIdGroup(link.from), toOriginGroup);
            this.searchOriginalOneLevelGroup(this.searchShapeIdGroup(link.to), fromOriginGroup);
            if (toOriginGroup.group.group !== fromOriginGroup.group.group) {
                this.linkOtherOneLevelGroup.push(link)
            }
        })
    }

    // 根据shape寻找自己对应的group
    searchShapeIdGroup(shapeId) {
        const {shapes, links, groups} = this.data;
        // 将shape对应的group跑出去
        const groupShape = groups.find(group => {
            const {shapes: groupShapes} = group;
            // 此group中存在此shape将group return出去
            return groupShapes.find(item => item.id === shapeId) !== undefined
        });
        return groupShape
    }

    // 根据源头shape寻找出一级group
    searchOriginalOneLevelGroup(group, originGroup) {
        const {shapes, links, groups} = this.data;
        // 通过源头shape获取到的group，看是否存在一级group中，如果不存在递归寻找出源头shape对应的一级group
        if (this.fatherGroup.find(item => item.group === group.group) === undefined) {
            this.searchOriginalOneLevelGroup(groups.find(item => item.group === group.parent), originGroup)
        } else {
            // 如果是源头shape对应的一级group，根据object渐层复制将源头复制给originGroup的origin中
            originGroup.group = group
        }
    }

    // 寻找出最源头的shape
    searchOriginalShape(link) {
        const {shapes, links, groups} = this.data;
        const originalLinks = links.filter(item => item.to === link.from);
        // originalLinks.length > 0 代表link不是源头，继续寻找源头
        if (originalLinks.length) {
            originalLinks.forEach(item => this.searchOriginalShape(item))
        } else {
            // 从shapes中获取link from对应的shape
            const shape = shapes.find(item => item.id === link.from);
            // 判断shape是否存在 originalShape 中
            const isExitShapeInOriginalShape = this.originalShape.find(item => item.id === shape.id)
            // 不存在，将源头shape push到存放源头shape的数组中
            if (!isExitShapeInOriginalShape) {
                this.originalShape.push(shape)
            }
        }
    }

    // 寻找出一级group
    searchOneLevelGroup(group) {
        const {shapes, links, groups} = this.data;
        const parentGroup = groups.filter(item => item.group === group.parent);
        // 如果 parentGroup.length > 0 代表有group不是一级group，递归继续寻找
        if (parentGroup.length) {
            parentGroup.forEach(item => this.searchOneLevelGroup(item))
        } else {
            // 走到这里代表已经寻找到一级group了，将这个检测oneLevelGroup是否存在group，不存在的话，将此group存放到oneLevelGroup中
            const exitGroup = this.fatherGroup.find(item => item.group === group.group)
            if (exitGroup === undefined) this.fatherGroup.push(group)
        }
    }

    // 处理二级group的排序入口函数（根据link进行排序）
    sortTwoLevelGroups() {
        const {shapes, links, groups} = this.data;
        // 将每个group初始化一个记录此group中每个子一级group的排序编号
        groups.forEach(group => {
            group.sortNum = 0;
        });
        // 寻找出link的to和from不在同一个一级group中  存储在linkOtherOneLevelGroup中
        this.searchLinkOtherOneLevelGroup();
        this.originalShape.forEach(item => item.sortNum = 0);
        groups.forEach(groupItem => {
            /* *
            * 处理group内的布局排序分为两种
            * 第一种：group 内直接就是 shape
            * 第二种：group 内嵌套就是 group
            * */
            // 第一种：group 内直接就是 shape
            if (groupItem.shapes && groupItem.shapes.length > 0) {
                this.sortShape(groupItem) // 给shape排序布局
            }
            // 第二种：group 内嵌套就是 group
            else if (groupItem.shapes && groupItem.shapes.length === 0) {
                const {shapes, links, groups} = this.data;
                // 通过存放link的to和from不在同一个一级group中的link末端shape的id获取其所在组的，并赋值为0
                this.linkOtherOneLevelGroup.forEach(link => {
                    const linkToGroup = groups.find(group => {
                        const {shapes: groupShapes} = group;
                        return groupShapes.find(shape => shape.id === link.to)
                    });
                    linkToGroup.horizontal === undefined ? linkToGroup.horizontal = 0 : null
                });
                this.linkOtherOneLevelGroup.forEach(link => {
                    // link的to作为from时候的link 比如group横向排序为0时里面shape的作为from的link
                    const fromLink = links.filter(item => item.from === link.to);
                    // 给一级group内的上一个group的通过shape的link寻找出下一个group的横向排序设置为上一个group的horizontal+1
                    fromLink.forEach(item => this.sortGroupInnerGroup(item));
                });
            }
        });
    }

    // 给shape排序布局
    sortShape(group) {
        const {shapes, links, groups} = this.data;
        const {serializeTypes, shapes: groupShapes} = group;
        /* *
        * shape排序有两种
        * 第一种：根据 serializeTypes 进行排序
        * 第二种：根据 link来进行排序
        * */
        // 第一种：根据 serializeTypes 进行排序
        if (serializeTypes && serializeTypes.length) {
            // 将 serializeTypes 类型顺序的 index 依次给对应的 shape.horizontal
            // 以此确定shape的横向顺序
            serializeTypes.forEach((item, index) => {
                groupShapes.forEach(shape => {
                    if (shape.type === item) shape.horizontal = index
                })
            })
        }
        // 第二种：根据 link来进行排序
        else {
            // 寻找link的to和from都在此group中所有的link
            const groupAllLInks = links.filter(link => {
                const fromShape = groupShapes.find(item => item.id === link.from) !== undefined;
                const toShape = groupShapes.find(item => item.id === link.to) !== undefined;
                return fromShape && toShape
            });
            if (groupAllLInks.length === 0) return;  // 二级组内没有关系不处理内部shape横向排列顺序
            // group 这个 group 是确保根据 link 寻找源头 shape 并且必须在此 group 中
            const originalShape = this.searchSameOriginalShape(groupAllLInks, group);
            originalShape.forEach(item => {
                // 给个 group shape 的源头初始化横向序列号
                item.horizontal = 0;
                this.sortSameGroupShape(item, group)
            });
            // 上面的横向排序只是处理同一个group中有link关系的shape进行横向排序，有部分shape在group内没有连接其他的shape，导致没有进行横向排序，所以需要将他初始化横向为0
            // shapes.map(item => item.horizontal === undefined ? item.horizontal = 0 : null)
            shapes.forEach(item => {
                if (item.horizontal === undefined) item.horizontal = 0
            })
        }
    }

    // 寻找同一个group中所有的源shape
    searchSameOriginalShape(groupAllLInks, group) {
        // 在同一个 group 中，根据 link 来获取源 shape
        // group 这个 group 是确保根据 link 寻找源头 shape 必须在此 group 中
        let originalShape = [];
        groupAllLInks.forEach(link => this.sameGroupSearchOriginalShape(link, group, originalShape));
        return originalShape
    }

    // 在同一个 group 中，根据 link 来获取源 shape
    // group 这个 group 是确保根据 link 寻找源头 shape 必须在此 group 中
    sameGroupSearchOriginalShape(link, group, originalShape) {
        const {shapes, links, groups} = this.data;
        // 寻找在同一个group中的上一个link
        const fromLink = links.filter(item => {
            // 本link的始端和上个link的末端相等
            if (link.from === item.to) {
                // 获取上个 link 的末端对应的 shape
                const shape = shapes.find(shape => shape.id === item.from);
                return shape.group === group.group
            }
        });
        if (fromLink.length === 0) {
            const shape = shapes.find(shape => shape.id === link.from);
            if (originalShape.find(item => item.id === shape.id) === undefined) {
                originalShape.push(shape)
            }
        } else {
            fromLink.forEach(link => this.sameGroupSearchOriginalShape(link, group, originalShape))
        }
    }

    // 给同一group中link下一个shape添加序号
    sortSameGroupShape(shape, group) {
        const {shapes, links, groups} = this.data;
        // 获取在同一个 group 的下一个link
        const toLinks = links.filter(link => {
            // 寻找shape连接的下一个shape
            if (shape.id === link.from) {
                // 寻找出link的末端的shape
                const findToShape = shapes.find(shape => shape.id === link.to);
                return findToShape.group === group.group
            }
        });
        if (toLinks.length) {
            toLinks.forEach(item => {
                // 寻找 link 末端的 shape 并将始端 sortNum 加一赋值给末端 shape 中
                const findFromShape = shapes.find(shape => shape.id === item.from);
                const findToShape = shapes.find(shape => shape.id === item.to);
                // findToShape.horizontal === undefined ? findToShape.horizontal = (findFromShape.horizontal + 1) : null;
                findToShape.horizontal = (findFromShape.horizontal + 1);
                this.sortSameGroupShape(findToShape, group);
            })
        }

    }

    // 给一级group内的上一个group的通过shape的link寻找出下一个group的横向排序设置为上一个group的horizontal+1
    sortGroupInnerGroup(link) {
        const {shapes, links, groups} = this.data;
        // link 相当于group横向排序为0时里面shape的作为from的link
        // 寻找link末端对应的group
        const linkToGroups = groups.find(item => {
            const {shapes: groupShapes} = item;
            return groupShapes.find(shape => shape.id === link.to) !== undefined
        });

        // 寻找link始端对应的group
        const linkFromGroups = groups.find(group => {
            const {shapes: groupShapes} = group;
            return groupShapes.find(shape => shape.id === link.from) !== undefined
        });
        if (linkToGroups.parent === linkFromGroups.parent) {
            linkToGroups.horizontal === undefined ? linkToGroups.horizontal = linkFromGroups.horizontal + 1 : null
        }

        // 继续通过link向下寻找
        const fromLink = links.filter(item => item.from === link.to);
        fromLink.forEach(item => this.sortGroupInnerGroup(item))
    }

    // 整个svg图在一个屏幕中显示
    fitScreen() {
        const {shapes, links, groups} = this.data;
        // 向每一group中添加group中包含横向排序所有的shape个数（horizontalShapeCount），横向间隔数（horizontalGutterCount），每个一级group中包含的横向二级group数（horizontalTwoGroupGutterCount）
        this.calcGroupHorizontalClassCount();
        // 添加比例尺，便于svg按照原数据超宽，通过比例尺处理数据让其在整个页面呈现
        this.fitScreenScaleLinear();
        // 计算每一组的宽度
        this.calcGroupWidth();
        // 向每一组中添加纵向最大高度和纵向最大的shape数量
        this.calcGroupVerticalClassCount();
        // 将每个shape数据上和group一样绑定修正之后的宽度和高度
        shapes.forEach(shape => {
            shape.horizontalMaxWidth = this.iconSize;
            shape.verticalMaxHeight = this.iconSize;
        })

    }

    // 向每一group中添加group中包含横向排序所有的shape个数（horizontalShapeCount），横向间隔数（horizontalGutterCount），每个一级group中包含的横向二级group数（horizontalTwoGroupGutterCount）
    calcGroupHorizontalClassCount() {
        const {shapes, links, groups} = this.data;
        // 存储 二级group中横向 shape 个数
        // 将每组中横向shape的个数存储到其group中
        groups.forEach(group => {
            if (group.shapes && group.shapes.length) {
                const {shapes: groupShapes} = group;
                // 将每个group中的horizontal去重，获取横向个数
                let shapeSet = new Set();
                groupShapes.forEach(shape => shapeSet.add(shape.horizontal));
                // 将每组中横向shape的个数存储到其group中
                group.horizontalShapeCount = shapeSet.size;
                group.horizontalGutterCount = shapeSet.size - 1;
            }
        });
        // 查出所有的二级group
        // const twoLevelGroups = groups.filter(group => group.parent !== undefined);
        // 查出所有的一级group
        const oneLevelGroups = groups.filter(group => group.parent === undefined);
        // 获取一级group，将二级的中每列中横向最多的累加起来存储在一级中
        oneLevelGroups.forEach(fatherGroup => {
            // 寻找出此一级下所有的二级group集合
            const twoLevelGroups = groups.filter(group => group.parent === fatherGroup.group);
            if (twoLevelGroups.length === 0) return;
            // 将二级中横向排序作为字段，值为此列二级group中最大的shape横向个数
            let horizontalNumJson = {};
            twoLevelGroups.forEach(sonGroup => {
                // 如果 horizontalNumJson 中没有横向排序，将此group中的横向shape个数赋值它
                if (horizontalNumJson[sonGroup.horizontal] === undefined) {
                    horizontalNumJson[sonGroup.horizontal] = sonGroup.horizontalShapeCount
                }
                // 如果有的话 horizontalNumJson 存储最大的那个横向排序shape的个数
                else {
                    horizontalNumJson[sonGroup.horizontal] = Math.max(sonGroup.horizontalShapeCount, horizontalNumJson[sonGroup.horizontal])
                }
            });
            fatherGroup.horizontalShapeCount = 0;
            // 计算出一级下所有的group和shape之间的间隙和
            // fatherGroup.horizontalGutterCount = Object.values(horizontalNumJson).length - 1;
            // 二级group横向个数
            fatherGroup.horizontalTwoGroupGutterCount = Object.values(horizontalNumJson).length - 1;
            for (let k in horizontalNumJson) {
                fatherGroup.horizontalShapeCount += horizontalNumJson[k];
                // fatherGroup.horizontalGutterCount += horizontalNumJson[k] - 1;
            }
        });
        // 计算一级内所有shape和shape之间的间隙个数(这里计算的是所以二级shape和shape之间的间隙个数总和)
        oneLevelGroups.forEach(item => {
            // 如果此一级group中有shapes的话  就不需要从新计算shape和shape之间的间隙个数了
            if (item.shapes.length) return;
            const sonGroup = groups.filter(group => group.parent === item.group);
            let shapeGutterCount = 0;
            sonGroup.forEach(group => {
                shapeGutterCount += group.horizontalGutterCount
            });
            item.horizontalGutterCount = shapeGutterCount;
        })
    }

    // 计算每一组的宽度
    calcGroupWidth() {
        const {groups} = this.data;
        // 查出所有的一级group
        const oneLevelGroups = groups.filter(group => group.parent === undefined);
        // 查询含有 shapes 的 group
        const hasShapesGroups = groups.filter(group => group.shapes.length);
        // 获取每组中有子组的groups
        const hasChildrenGroups = groups.filter(group => !group.shapes.length);
        // 给含有shapes的group添加宽度
        hasShapesGroups.forEach(group => {
            group.horizontalMaxWidth =
                this.groupPadding.left
                + this.groupPadding.right
                + group.horizontalShapeCount * this.iconSize
                + group.horizontalGutterCount * this.shapeHInterval
        });
        hasChildrenGroups.forEach(group => {
            const childrenGroups = groups.filter(item => item.parent === group.group);
            // 存放每列的最大宽度
            let horizontalGroupWidthMap = new Map();
            childrenGroups.forEach(item => {
                const width = horizontalGroupWidthMap.get(item.horizontal);
                if (width === undefined) {
                    horizontalGroupWidthMap.set(item.horizontal, item.horizontalMaxWidth);
                } else {
                    horizontalGroupWidthMap.set(item.horizontal, Math.max(item.horizontalMaxWidth, width));
                }
            })
            let width = 0;
            for (let value of horizontalGroupWidthMap.values()) {
                width += value;
            }
            group.horizontalMaxWidth =
                width
                + this.groupPadding.left
                + this.groupPadding.right
                + horizontalGroupWidthMap.size * (
                    +this.groupMargin.left
                    + this.groupMargin.right
                )
                + (horizontalGroupWidthMap.size - 1) * this.shapeHInterval
        });
    }

    // 计算svg需要的宽度
    calcSvgWidth() {
        const {groups} = this.data;
        // 查出所有的一级group
        const oneLevelGroups = groups.filter(group => group.parent === undefined);
        // 总宽度
        let allWidth = 0;
        const zhiBiao = oneLevelGroups.filter(item => item.shapes.length);
        const hasChildGroup = oneLevelGroups.filter(item => !item.shapes.length);
        // 类似指标，没有子group 直接包裹shapes
        zhiBiao.forEach(item => {
            allWidth += item.horizontalShapeCount * this.iconSize;
            allWidth += (item.horizontalShapeCount - 1) * this.shapeHInterval;
            allWidth += this.groupPadding.left + this.groupPadding.right;
        });
        hasChildGroup.forEach(item => {
            allWidth += this.groupPadding.left + this.groupPadding.right;
            allWidth += (item.horizontalTwoGroupGutterCount + 1) * (this.groupPadding.left + this.groupPadding.right + this.groupMargin.left + this.groupMargin.right);
            allWidth += item.horizontalShapeCount * this.iconSize;
            allWidth += (item.horizontalShapeCount - 1) * this.shapeHInterval;
        });
        allWidth += (oneLevelGroups.length) * (this.groupMargin.left + this.groupMargin.right);
        allWidth += (oneLevelGroups.length - 1) * this.shapeHInterval;
        return allWidth;
    }

    // 计算svg原始数据需要的宽度
    calcOriginalSvgWidth() {
        const {groups} = this.data;
        // 查出所有的一级group
        const oneLevelGroups = groups.filter(group => group.parent === undefined);
        // 总宽度
        let allWidth = 0;
        const zhiBiao = oneLevelGroups.filter(item => item.shapes.length);
        const hasChildGroup = oneLevelGroups.filter(item => !item.shapes.length);
        // 类似指标，没有子group 直接包裹shapes
        zhiBiao.forEach(item => {
            allWidth += item.horizontalShapeCount * this.originIconSize;
            allWidth += (item.horizontalShapeCount - 1) * this.originShapeHInterval;
            allWidth += this.originGroupPadding.left + this.originGroupPadding.right;
        });
        hasChildGroup.forEach(item => {
            allWidth += this.originGroupPadding.left + this.originGroupPadding.right;
            allWidth += (item.horizontalTwoGroupGutterCount + 1) * (this.originGroupPadding.left + this.originGroupPadding.right + this.originGroupMargin.left + this.originGroupMargin.right);
            allWidth += item.horizontalShapeCount * this.originIconSize;
            allWidth += (item.horizontalShapeCount - 1) * this.originShapeHInterval;
        });
        allWidth += (oneLevelGroups.length) * (this.originGroupMargin.left + this.originGroupMargin.right);
        allWidth += (oneLevelGroups.length - 1) * this.originShapeHInterval;
        return allWidth;
    }


    // 添加比例尺，便于svg按照原数据超宽，通过比例尺处理数据让其在整个页面呈现
    fitScreenScaleLinear() {
        // 计算svg宽度
        const allWidth = this.calcOriginalSvgWidth();
        // 获取容器的高宽
        const {width: containerWidth} = this.container.node().getBoundingClientRect();
        if (containerWidth >= allWidth) return;
        // 所有的shape按照原数据超出等比例缩放
        // const linear = d3.scaleLinear()
        const linear = d3.scale.linear()
            .domain([0, allWidth])
            .range([0, containerWidth]);
        this.shapeHInterval = linear(this.originShapeHInterval);
        this.shapeVInterval = linear(this.originShapeVInterval);
        this.shapeTitleSize = linear(this.originShapeTitleSize);
        this.iconSize = linear(this.originIconSize);
        this.groupPadding.right = linear(this.originGroupPadding.right);
        this.groupPadding.top = linear(this.originGroupPadding.top);
        this.groupPadding.left = linear(this.originGroupPadding.left);
        this.groupPadding.bottom = linear(this.originGroupPadding.bottom);
        this.groupMargin.right = linear(this.originGroupMargin.right);
        this.groupMargin.top = linear(this.originGroupMargin.top);
        this.groupMargin.left = linear(this.originGroupMargin.left);
        this.groupMargin.bottom = linear(this.originGroupMargin.bottom);
        this.groupTitleSize = linear(this.originGroupTitleSize);
    }

// 向每一组中添加纵向最大高度和纵向最大的shape数量
    calcGroupVerticalClassCount() {
        const {shapes, links, groups} = this.data;
        // 查出所有的含有shape的group
        const hasShapeGroups = groups.filter(group => group.shapes && group.shapes.length);
        // 将每组中纵向shape个数最大的保存在对应的group中，保存在字段verticalShapesMaxCount中，以及group需要最大的高度verticalMaxHeight
        hasShapeGroups.forEach(group => {
            // 存储 shape纵向含有最大的数量的shapes
            let verticalShapeCountJson = {};
            const {shapes: groupShapes} = group;
            groupShapes.forEach(shape => {
                if (verticalShapeCountJson[shape.horizontal] === undefined) {
                    verticalShapeCountJson[shape.horizontal] = groupShapes.filter(item => item.horizontal === shape.horizontal).length
                }
            });
            group.verticalShapesMaxCount = Math.max(...Object.values(verticalShapeCountJson));
            // group的最大高度
            group.verticalMaxHeight =
                group.verticalShapesMaxCount * this.iconSize
                + (group.verticalShapesMaxCount - 1) * this.shapeVInterval
                + this.groupPadding.top
                + this.groupPadding.bottom
        });

        // 获取所有含有二级group的一级group
        const hasTwoLevelGroupDeOneLevelGroup = groups.filter(group => {
            return groups.find(item => item.parent === group.group) !== undefined
        });
        // 一级group需要最大的高度verticalMaxHeight
        hasTwoLevelGroupDeOneLevelGroup.forEach(group => {
            // 寻找出此一级group中所有的二级group
            const sonGroup = groups.filter(item => item.parent === group.group);
            // 存储 纵向每列的高度和
            let verticalTwoLevelGroupCountJson = {};
            sonGroup.forEach(item => {
                // 判断 verticalTwoLevelGroupCountJson 是否已经存储此类纵向高度，如果没存储 初始化为0
                verticalTwoLevelGroupCountJson[item.horizontal] === undefined ? verticalTwoLevelGroupCountJson[item.horizontal] = 0 : null;
                verticalTwoLevelGroupCountJson[item.horizontal] +=
                    this.groupPadding.top
                    + this.groupPadding.bottom
                    + this.groupMargin.top
                    + this.groupMargin.bottom
                    + item.verticalShapesMaxCount * this.iconSize
                    + (item.verticalShapesMaxCount - 1) * this.shapeVInterval;
            });

            const horizontalSet = new Set();
            sonGroup.forEach(item => {
                horizontalSet.add(item.horizontal);
            });
            horizontalSet.forEach(item => {
                // 判断 verticalTwoLevelGroupCountJson 是否已经存储此类纵向高度，如果没存储 初始化为0
                verticalTwoLevelGroupCountJson[item] === undefined ? verticalTwoLevelGroupCountJson[item] = 0 : null;

                // 每一列含有的group的个数
                const verticalGroupCount = sonGroup.filter(gro => gro.horizontal === item).length;
                verticalTwoLevelGroupCountJson[item] += verticalGroupCount * this.shapeVInterval + this.groupPadding.top + this.groupPadding.bottom
            });
            group.verticalMaxHeight = Math.max(...Object.values(verticalTwoLevelGroupCountJson)) - 3
        })
    }

    // 计算每个每个元素的坐标
    calcLoc() {
        const {shapes, links, groups} = this.data;
        // 设置svg的宽高
        this.setSvgSize();
        // 先布局一级group
        this.oneLevelGroupLayout();
        // 布局二级group
        this.secondLevelGroupLayout();
        // 布局所有的shapes
        this.shapesLayout();
        // group title 布局
        this.groupTitleLayout();
    }

    // 设置svg的宽高
    setSvgSize() {
        const {shapes, links, groups} = this.data;
        // 寻找一级group
        const onLevelGroup = groups.filter(item => item.parent === undefined);
        // 获取容器的高宽
        const {height: containerHeight, width: containerWidth} = this.container.node().getBoundingClientRect();
        const svg = d3.select(`#${this.select}_container`);
        svg.attr('xmlns', 'http://www.w3.org/2000/svg');
        svg.attr('xmlns:xlink', 'http://www.w3.org/1999/xlink');
        // 设置svg宽度
        svg.attr('width', Math.max(this.calcSvgWidth(), containerWidth));
        const verticalMaxHeightArr = [];
        onLevelGroup.forEach(item => verticalMaxHeightArr.push(item.verticalMaxHeight));
        svg.attr('height', Math.max(containerHeight, ...verticalMaxHeightArr) - 3);
    }

    // 布局一级group
    oneLevelGroupLayout() {
        const {shapes, links, groups} = this.data;
        // 寻找一级group
        const onLevelGroup = groups.filter(item => item.parent === undefined);
        // const svg = d3.select('svg');
        const svg = d3.select(`#${this.select}_container`);
        const svgWidth = svg.attr('width');
        const svgHeight = svg.attr('height');
        // 一级group横向序列总宽度
        let horizontalWidth = 0;
        onLevelGroup.forEach(group => {
            horizontalWidth +=
                this.groupMargin.left
                + this.groupMargin.right
                + group.horizontalMaxWidth
        });
        horizontalWidth += (onLevelGroup.length - 1) * this.shapeHInterval;
        // 获取一级高度组
        const verticalMaxHeightArr = [];
        onLevelGroup.forEach(item => verticalMaxHeightArr.push(item.verticalMaxHeight));
        const oneLevelGroupMaxHeight = Math.max(...verticalMaxHeightArr);
        // 计算一级左边起始X轴坐标
        let baseXLoc = (svgWidth - horizontalWidth) / 2 + this.groupMargin.left;
        onLevelGroup.sort((a, b) => a.horizontal - b.horizontal);
        onLevelGroup.forEach((group, index) => {
            baseXLoc += ((index - 1) >= 0 ?
                onLevelGroup[index - 1].horizontalMaxWidth
                + this.shapeHInterval
                + this.groupMargin.left
                + this.groupMargin.right : 0);
            group.x = baseXLoc;
            group.y = (svgHeight - group.verticalMaxHeight) / 2;
        });
        onLevelGroup.forEach(item => {
            svg.append('rect')
                .attr('id', `${item.group}`)
                .attr('y', item.y + 1.5)
                .attr('x', item.x)
                .attr('width', item.horizontalMaxWidth)
                .attr('height', item.verticalMaxHeight - 3)
                // .attr('fill', 'rgba(255,255,255,0.3)')
                .attr('fill', 'none')
                .attr('fill', 'none')
                .attr('stroke', 'rgba(255,255,255,1)')
                .attr('stroke-dasharray', '5,5')
                .attr('stroke-width', '0.5')
        })
    }

    // 布局二级group
    secondLevelGroupLayout() {
        const {shapes, links, groups} = this.data;
        // 寻找含有二级group的一级group
        const fatherGroup = groups.filter(group => {
            return groups.find(item => item.parent === group.group)
        });
        fatherGroup.forEach(fatherGroup => {
            // 寻找此一级group下所有的二级group
            const sonGroup = groups.filter(group => group.parent === fatherGroup.group);
            sonGroup.sort((a, b) => a.horizontal - b.horizontal);
            this.setXLoc(fatherGroup, sonGroup);
            this.setYLoc(fatherGroup, sonGroup);
            // const svg = d3.select('svg');
            const svg = d3.select(`#${this.select}_container`);
            sonGroup.forEach(item => {
                svg.append('rect')
                    .attr('id', `${item.group}`)
                    .attr('y', item.y)
                    .attr('x', item.x)
                    .attr('width', item.horizontalMaxWidth)
                    .attr('height', item.verticalMaxHeight)
                    .attr('fill', 'none')
                    .attr('stroke', 'rgba(255,255,255,1)')
                    .attr('stroke-dasharray', '5,5')
                    .attr('stroke-width', '0.5')
            })
        });
    }

    // 布局所有的shape
    shapesLayout() {
        const {shapes, links, groups} = this.data;
        // 获取含有shape的group
        const hasShapesGroup = groups.filter(group => group.shapes && group.shapes.length);
        hasShapesGroup.forEach(group => {
            const {shapes: groupShapes} = group;
            this.setXLoc(group, groupShapes);
            this.setYLoc(group, groupShapes)
        });
        // const svg = d3.select('svg');
        const svg = d3.select(`#${this.select}_container`);
        this.renderShapes();
        this.renderLinks();
    }

    // 设置x轴坐标
    setXLoc(fatherGroup, sonGroup) {
        // 存储每一列的二级group
        let horizontalGroupsJson = {};
        sonGroup.forEach(item => {
            horizontalGroupsJson[item.horizontal] === undefined ? horizontalGroupsJson[item.horizontal] = [] : null;
            horizontalGroupsJson[item.horizontal].push(item)
        });
        let baseXLoc = this.groupPadding.left + fatherGroup.x + (fatherGroup.shapes.length ? 0 : this.groupMargin.left);
        // 存储 horizontal序列号Set
        let horizontalSet = new Set();
        sonGroup.forEach(item => horizontalSet.add(item.horizontal));
        // 存储 horizontal序列号Arr
        let horizontalArr = [...horizontalSet];
        horizontalArr.sort((a, b) => a - b);
        // 设置x坐标
        for (let k in horizontalGroupsJson) {
            const item = horizontalGroupsJson[k];
            const index = horizontalArr.indexOf(parseFloat(k));
            if (index - 1 >= 0) {
                // 存储每列中所有group的宽度，方便查询每列中最大宽的group
                let groupWidth = [];
                horizontalGroupsJson[horizontalArr[index - 1]].forEach(ite => groupWidth.push(ite.horizontalMaxWidth));
                item.forEach(ite => ite.x = baseXLoc + Math.max(...groupWidth) + this.shapeHInterval + (fatherGroup.shapes.length ? 0 : this.groupMargin.left));
                baseXLoc = baseXLoc + Math.max(...groupWidth) + this.shapeHInterval + (fatherGroup.shapes.length ? 0 : (this.groupMargin.left + this.groupMargin.right));
                //
            } else {
                item.forEach(ite => ite.x = baseXLoc)
            }
        }
    }

    // 设置y轴坐标
    setYLoc(fatherGroup, sonGroup) {
        // 存储 horizontal序列号Set
        let horizontalSet = new Set();
        sonGroup.forEach(item => horizontalSet.add(item.horizontal));
        // 存储 horizontal序列号Arr
        let horizontalArr = [...horizontalSet];
        horizontalArr.sort((a, b) => a - b);
        // 存储每一列的二级group
        let horizontalGroupsJson = {};
        sonGroup.forEach(item => {
            horizontalGroupsJson[item.horizontal] === undefined ? horizontalGroupsJson[item.horizontal] = [] : null;
            horizontalGroupsJson[item.horizontal].push(item)
        });
        // 设置y坐标
        for (let k in horizontalGroupsJson) {
            // 每列所有的group
            const item = horizontalGroupsJson[k];
            // 获取在此一级group横向排列编号
            const index = horizontalArr.indexOf(parseFloat(k));
            // 存储每列所需高度
            let allHeight = 0;
            item.forEach(gro => allHeight += gro.verticalMaxHeight);
            allHeight += (item.length - 1) * this.shapeVInterval;
            let baseYLoc = fatherGroup.y + (fatherGroup.verticalMaxHeight - allHeight) / 2;
            item.forEach((gro, index) => {
                if (index - 1 >= 0) {
                    baseYLoc += item[index - 1].verticalMaxHeight + this.shapeVInterval
                }
                gro.y = baseYLoc
            })
        }
    }

    // group title 布局
    groupTitleLayout() {
        const {shapes, links, groups} = this.data;
        // groups
        // const svg = d3.select('svg');
        const svg = d3.select(`#${this.select}_container`);
        groups.forEach(item => {
            svg.append('text')
                .attr('id', `text_${item.group}`)
                .text(item.title)
                .attr('x', item.x + item.horizontalMaxWidth / 2)
                .attr('y', this.groupTitleSize + item.y + 5)
                .attr('text-anchor', 'middle')
                .attr('fill', '#fff')
                .attr('font-size', this.groupTitleSize)

        });
    }

    svgSize() {
        // 清空容器
        this.container.selectAll('*').remove();
        // 获取容器的高宽
        const {width: containerWidth, height: containerHeight} = this.container.node().getBoundingClientRect();
        this.svg = this.container.append('svg')
            .attr('id', `${this.select}_container`)
            .attr('width', containerWidth)
            .attr('height', containerHeight - 3);
        return this.svg
    }

    renderShapes() {
        /*
        * server 服务器 icon-fuwuqi
        * database 数据库 icon-shujuku
        * table 表 icon-biao
        * column 字段 icon-ziduan
        * */
        // 获取解析之后的shapes数据和links数据
        // this.layout();
        const {shapes} = this.data;
        shapes.forEach(item => {
            item.loc = {
                x: item.x,
                y: item.y || 10
            };
            const shape = new Shape({
                loc: item.loc,
                svg: this.svg,
                icon: item.icon,
                name: item.name,
                iconSize: item.iconSize || this.iconSize,
                iconColor: item.iconColor || this.iconColor,
                textStyle: item.textStyle || this.textStyle,
                ballRadius: item.ballRadius || this.ballRadius,
                data: item,
                d3: d3,
                allData: this.data,
                groupPadding: this.groupPadding,
                groupMargin: this.groupMargin,
                groupTitleSize: this.groupTitleSize,
                select: this.select,
                titleSize: this.shapeTitleSize

            });
            shape.createShape();
            shape.createName()
        })
    }

    renderLinks() {
        // 获取解析之后的shapes数据和links数据
        const {shapes, links, groups} = this.data;
        let linksArr = [];
        links.forEach(linkItem => {
            const fromShapes = shapes.find(shape => shape.id === linkItem.from);
            const toShapes = shapes.find(shape => shape.id === linkItem.to);
            if (!toShapes || !fromShapes) return;
            linksArr.push({
                data: [{
                    x: fromShapes.x,
                    y: fromShapes.y
                }, {
                    x: toShapes.x,
                    y: toShapes.y
                }],
                linkInfo: linkItem
            })
        });

        this.data.linksArr = linksArr;
        linksArr.forEach(item => {
            const line = new Line({
                d3,
                svg: this.svg,
                container: this.container,
                data: item.data,
                linkInfo: item.linkInfo,
                iconSize: item.iconSize || this.iconSize,
                lineColor: item.lineColor || this.lineColor,
                strokeWidth: item.strokeWidth || this.strokeWidth,
                ballRadius: item.ballRadius || this.ballRadius,
                ballSpeed: item.ballSpeed || this.ballSpeed,
                ballColor: item.ballColor || this.ballColor,
            });
            line.line();
            line.ball();
        })
    }
}

export default Svg
