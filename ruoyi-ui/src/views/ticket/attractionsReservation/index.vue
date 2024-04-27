<template>
  <div>
    <!-- 预约管理页面内容 -->
    <div v-if="activeTab === 'attractionsReservation'">
      <div class="app-container">
        <!-- 预约列表 -->
        <el-table :data="reservationList" v-loading="loading" style="width: 100%" border>
          <el-table-column label="预约ID" prop="reservationId" align="center"></el-table-column>
          <el-table-column label="景点名称" prop="attractionsName" align="center"></el-table-column>
          <el-table-column label="用户名称" prop="userName" align="center"></el-table-column>
          <el-table-column label="开始时间" prop="startTime" align="center"></el-table-column>
          <el-table-column label="结束时间" prop="endTime" align="center"></el-table-column>
          <el-table-column label="预约状态" prop="reservationStatus" align="center">
            <template slot-scope="scope">
              {{ scope.row.reservationStatus === 0 ? '已预约' : '已取消' }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="160px">
            <template slot-scope="scope">
              <el-button type="danger" size="mini" @click="cancelReservation(scope.row)" v-hasPermi="['ticket:reservation:cancel']">取消预约</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页组件 -->
        <pagination
          v-show="totalReservations > 0"
          :total="totalReservations"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="fetchReservations"
        />

      </div>
    </div>
  </div>
</template>

<script>
import { listReservations, cancelReservation } from '@/api/ticket/reservation'

export default {
  data() {
    return {
      loading: true,
      activeTab: 'attractionsReservation',
      reservationList: [],
      totalReservations: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      }
    };
  },
  created() {
    this.fetchReservations();
  },
  methods: {
    fetchReservations() {
      this.loading = true;
      listReservations(this.queryParams).then(response => {
        this.reservationList = response.rows;
        this.totalReservations = response.total;
        this.loading = false;
      });
    },
    cancelReservation(row) {
      this.$confirm('确认取消该预约吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        cancelReservation(row.reservationId).then(() => {
          this.$message.success('取消预约成功');
          this.fetchReservations();
        });
      });
    }
  }
};
</script>

<style scoped>

</style>

