<template>

  <div class="table-responsive">
    <table class="table table-hover">
      <thead>
        <tr>
          <th>訂單編號</th>
          <th>商品名稱</th>
          <th>訂購日期</th>
          <th>使用日期</th>
          <th>金額</th>
          <th>狀態</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in orders" :key="order.id">
          <td>{{ order.id }}</td>
          <td>{{ order.productName }}</td>
          <td>{{ formatDate(order.orderDate) }}</td>
          <td>{{ formatDate(order.useDate) }}</td>
          <td>NT$ {{ order.amount }}</td>
          <td>
            <span :class="getStatusClass(order.status)">{{
              order.status
            }}</span>
          </td>
          <td>
            <button
              class="btn btn-sm btn-outline-primary me-2"
              @click="handleView(order)"
            >
              查看詳情
            </button>
            <button
              v-if="order.status === '已付款'"
              class="btn btn-sm btn-outline-danger"
              @click="handleCancel(order)"
            >
              取消訂單
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

</template>

<script setup>
</script>

<style scoped>
.table th {
  background-color: #f8f9fa;
}
.badge {
  padding: 0.5em 0.8em;
}
</style>