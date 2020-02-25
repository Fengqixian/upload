import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);
let routes = [
    {
        path: '/login',
        component: () => import('../views/login/index.vue')
    }, {
        path: '/content',
        component: () => import('../views/layout/index.vue'),
        children: []
    }
];
const menuList = JSON.parse(sessionStorage.getItem('menuList') || '[]')
menuList.forEach(menu => {
    routes[1].children.push({
        path: menu.routerPath,
        name: menu.name,
        component: () => import(`../views${menu.routerPath}/index.vue`),
    });
    menu.subMenus && menu.subMenus.forEach(sub => {
        routes[1].children.push({
            path: sub.routerPath,
            name: sub.name,
            component: () => import(`../views${sub.routerPath}/index.vue`),
        });
        sub.subMenus && sub.subMenus.forEach(item => {
            routes[1].children.push({
                path: item.routerPath,
                name: item.name,
                component: () => import(`../views${item.routerPath}/index.vue`),
            })
        })
    })
})
// 根目录重定向菜单中首个目录
routes.push({
    path: '/',
    redirect: '/login'
});
const router = new Router({
    mode: 'history',
    routes
});

export default router
