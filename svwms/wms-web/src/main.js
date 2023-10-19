import Vue from 'vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue'
import './assets/global.css';
import axios from "axios";
import VueRouter from "vue-router";
import router from './router';
import store from './store'

Vue.prototype.$axios=axios;
Vue.prototype.$httpUrl='http://localhost:8083'
Vue.config.productionTip = false
Vue.use(ElementUI,{size:'small'});
Vue.use(VueRouter);
import lang from 'element-ui/lib/locale/lang/ja'
    import locale from 'element-ui/lib/locale'
    locale.use(lang)

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
