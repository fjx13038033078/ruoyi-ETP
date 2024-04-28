<template>
  <div>
    <!-- 预约管理页面内容 -->
    <div v-if="activeTab === 'tickets'">
      <div class="app-container">
        <!-- 预约列表 -->
        <el-table :data="ticketList" v-loading="loading" style="width: 100%" border>
          <el-table-column label="门票记录ID" prop="ticketId" align="center"></el-table-column>
          <el-table-column label="景点名称" prop="attractionsName" align="center"></el-table-column>
          <el-table-column label="用户名称" prop="userName" align="center"></el-table-column>
          <el-table-column label="门票状态" prop="ticketType" align="center">
            <template slot-scope="scope">
              {{ scope.row.ticketType === 0 ? '全价票' : '优惠票' }}
            </template>
          </el-table-column>
          <el-table-column label="门票状态" prop="ticketStatus" align="center">
            <template slot-scope="scope">
              {{ scope.row.ticketStatus === 0 ? '已购票' : '已退票' }}
            </template>
          </el-table-column>
          <el-table-column label="交易金额" prop="ticketAmount" align="center"></el-table-column>
          <el-table-column label="交易时间" prop="transactionTime" align="center"></el-table-column>
          <el-table-column label="用户余额" prop="balance" align="center"></el-table-column>
          <el-table-column label="操作" align="center" width="230px">
            <template slot-scope="scope">
              <el-button type="success" size="mini" @click="handleRefund(scope.row)" v-hasPermi="['ticket:ticket:refund']">
                退款
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页组件 -->
        <pagination
          v-show="totalTickets > 0"
          :total="totalTickets"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="fetchTickets"
        />

      </div>
    </div>
  </div>
</template>

<script>
import { listTickets, refundTicket} from "@/api/ticket/ticket";

export default {
  data() {
    return {
      loading: true,
      activeTab: 'tickets',
      ticketList: [],
      totalTickets: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      }
    };
  },
  created() {
    this.fetchTickets();
  },
  methods: {
    fetchTickets() {
      this.loading = true;
      listTickets(this.queryParams).then(response => {
        this.ticketList = response.rows;
        this.totalTickets = response.total;
        this.loading = false;
      });
    },

    // 处理退款操作
    handleRefund(row) {
      this.$confirm('确认退票吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        refundTicket(row.ticketId).then(() => {
          this.$message.success('退票成功');
          this.fetchTickets();
        });
      });
    },
  }
};
</script>

<style scoped>

</style>

