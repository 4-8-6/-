<template>
  <div>
   <Header />

  <div style="display: flex">
    <left_side />
    <router-view style="flex: 1"/>
  </div>
  </div>
</template>

<script>

import Header from "../components/Header"
import Left_side from "@/components/left_side";

export default {
  name: "consumer",
  components:
      {
        Left_side,
        Header,
      }
}
</script>

<style scoped>

</style>