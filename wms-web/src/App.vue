<template>
  <div id="app">
      <router-view/>
  </div>
</template>

<script>


export default {
  name: 'App',
  components: {},
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('CurUser')),
    }
  },
  watch:{
    '$store.state.menu':{
      handler(val,old){
        console.log(val,old)
        //旧的数据没有的情况下，且现在用户有，现在用户的账户有的情况下
        if(!old && this.user && this.user.account){
          this.$store.commit("setRouter",val)
        }
      },
      immediate: true
    }
  }
}
</script>

<style>
#app{
  height: 100%;
}
</style>
