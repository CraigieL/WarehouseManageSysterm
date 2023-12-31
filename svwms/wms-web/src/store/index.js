import vue from 'vue'
import Vuex from 'vuex'
import router,{resetRouter} from "../router";
vue.use(Vuex)
import createPersistedState from 'vuex-persistedstate'
//动态路由
function addNewRoute(menuList) {
    console.log(menuList)
    let routes = router.options.routes //获取路由列表
    console.log(routes)
    //组装路由
    routes.forEach(routeItem=>{
        if(routeItem.path=="/Index"){
            menuList.forEach(menu=>{
                let childRoute =  {
                    path:'/'+menu.menuclick,
                    name:menu.menuname,
                    meta:{
                        title:menu.menuname
                    },
                    component:()=>import('../components/'+menu.menucomponent)
                }

                routeItem.children.push(childRoute)
            })
        }
    })

    resetRouter()
    //合并路由
    router.addRoutes(routes)
}

export default new Vuex.Store({
    state: {
        menu: []
    },
    mutations: {
        setMenu(state,menuList) {
            state.menu = menuList
            //添加路由
            addNewRoute(menuList)
        },
        setRouter(state,menuList) {

            addNewRoute(menuList)
        }
    },
    getters: {
        getMenu(state) {
            return state.menu
        }
    },
    plugins:[createPersistedState()]
})
