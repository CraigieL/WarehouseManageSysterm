const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: null
    // port: 8082, // 这是 dev server 监听的端口
    // proxy: {
    //   '/': {
    //     // target: 'http://wmssboot:8083', // Docker化运行配置
    //     target: 'http://localhost:8083',//本地化运行配置
    //     changeOrigin: true
    //   }
    // }
  }
})
