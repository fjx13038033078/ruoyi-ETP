<template>
  <div>
    <!-- 景点管理页面内容 -->
    <div v-if="activeTab === 'attractions'">
      <div class="app-container">
        <!-- 添加景点按钮 -->
        <el-row :gutter="20" class="mb-20" style="margin-bottom: 20px;">
          <el-col>
            <el-button type="primary" @click="handleAddAttractions" v-hasPermi="['ticket:attractions:add']"
            >新增景点
            </el-button>
          </el-col>
        </el-row>

        <!-- 景点列表 -->
        <el-table :data="attractionsList" v-loading="loading" style="width: 100%" border>
          <el-table-column label="景点ID" prop="attractionsId" align="center"></el-table-column>
          <el-table-column label="景点名称" prop="attractionsName" align="center"></el-table-column>
          <el-table-column label="景点位置" prop="location" align="center"></el-table-column>
          <el-table-column label="景点描述" prop="description" align="center"></el-table-column>
          <el-table-column label="门票价格" prop="ticketPrice" align="center"></el-table-column>
          <el-table-column label="操作" align="center" width="310px">
            <template slot-scope="scope">
              <el-button type="info" size="mini" @click="handleReserve(scope.row)"
                         v-hasPermi="['ticket:attractions:reserve']">
                预约
              </el-button>
              <el-button type="success" size="mini" @click="handleView(scope.row)"
                         v-hasPermi="['ticket:attractions:detail']">
                查看
              </el-button>
              <el-button type="primary" size="mini" @click="handleEdit(scope.row)"
                         v-hasPermi="['ticket:attractions:edit']">
                编辑
              </el-button>
              <el-button type="danger" size="mini" @click="handleDelete(scope.row)"
                         v-hasPermi="['ticket:attractions:delete']">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页组件 -->
        <pagination
          v-show="totalAttractions > 0"
          :total="totalAttractions"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="fetchAttractions"
        />

        <!-- 添加/编辑商品对话框 -->
        <el-dialog :visible.sync="dialogVisible" :title="dialogTitle" width="30%" @close="handleCloseDialog">
          <!-- 对话框内容 -->
          <div>
            <el-form :model="attractionsForm" label-width="100px">
              <!-- 景点名称 -->
              <el-form-item label="景点名称">
                <el-input v-model="attractionsForm.attractionsName" :disabled="isReadOnly"></el-input>
              </el-form-item>
              <!-- 景点位置 -->
              <el-form-item label="景点位置">
                <el-input v-model="attractionsForm.location" :disabled="isReadOnly"></el-input>
              </el-form-item>
              <!-- 景点描述 -->
              <el-form-item label="景点描述">
                <el-input v-model="attractionsForm.description" :disabled="isReadOnly"></el-input>
              </el-form-item>
              <!-- 景点价格 -->
              <el-form-item label="景点价格">
                <el-input v-model="attractionsForm.ticketPrice" :disabled="isReadOnly"></el-input>
              </el-form-item>
              <!-- 根据当前模式决定显示 ImageUpload 或 ImagePreview -->
              <el-form-item label="景点图片">
                <template v-if="!isReadOnly">
                  <ImageUpload v-model="attractionsForm.image" :disabled="isReadOnly"><</ImageUpload>
                </template>
                <template v-else>
                  <ImagePreview :src="attractionsForm.image" :disabled="isReadOnly"></ImagePreview>
                </template>
              </el-form-item>
            </el-form>
          </div>
          <!-- 对话框按钮 -->
          <div slot="footer" class="dialog-footer" style="text-align: center;">
            <el-button @click="handleCloseDialog">取消</el-button>
            <el-button type="primary" @click="handleSubmit">{{ dialogButtonText }}</el-button>
          </div>
        </el-dialog>

        <!-- 预约对话框 -->
        <el-dialog :visible.sync="reserveDialogVisible" title="预约场地" width="30%" @close="handleCloseDialog">
          <!-- 预约对话框内容 -->
          <div>
            <!-- 预约起止时间选择器 -->
            <div class="block">
              <span class="demonstration" style="display: block; margin-bottom: 10px;">选择预约起止时间：</span>
              <el-date-picker
                v-model="reservationTime"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期时间"
                end-placeholder="结束日期时间"
                :picker-options="pickerOptions"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-ddTHH:mm:ssZ">
              </el-date-picker>
            </div>
          </div>
          <!-- 预约对话框底部按钮 -->
          <div slot="footer" class="dialog-footer" style="text-align: center;">
            <el-button @click="handleCloseDialog">取消</el-button>
            <el-button type="primary" @click="confirmReservation">确定预约</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script>
import {
  addAttractions,
  deleteAttractions,
  getAttractions,
  listAttractions,
  updateAttractions
} from "@/api/ticket/attractions";
import {addReservation} from "@/api/ticket/reservation";

export default {
  data() {
    return {
      loading: true,// 遮罩层
      activeTab: 'attractions', // 当前激活的选项卡，默认为景点管理
      attractionsList: [], // 景点列表数据
      totalAttractions: 0,// 总条数
      dialogVisible: false, // 控制新增/编辑景点对话框的显示与隐藏
      dialogTitle: '', // 对话框标题
      dialogButtonText: '', // 对话框按钮文本
      attractionsForm: { // 新增/编辑景点表单
        attractionsName: '',
        location: '',
        description: '',
        ticketPrice: null,
        image: ''
      },
      isReadOnly: false, // 是否只读模式
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 预约对话框
      reserveDialogVisible: false, // 控制预约对话框的显示与隐藏
      reservationTime: [],// 存储预约起止时间的数组，数组的第一个元素为开始时间，第二个元素为结束时间
      selectedAttractionsId: null,// 选中的场地ID，默认为null
      pickerOptions: {
        disabledDate(time) {
          // 获取当前日期
          const today = new Date();
          // 获取一周后的日期
          const oneWeekLater = new Date(today.getTime() + 8 * 24 * 60 * 60 * 1000);
          // 将时间戳转换为年月日格式的字符串
          const currentDate = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
          const oneWeekLaterDate = oneWeekLater.getFullYear() + '-' + (oneWeekLater.getMonth() + 1) + '-' + oneWeekLater.getDate();
          // 将当前日期转换为毫秒数
          const currentTime = new Date(currentDate).getTime();
          // 将一周后日期转换为毫秒数
          const oneWeekLaterTime = new Date(oneWeekLaterDate).getTime();
          // 将传入的时间参数转换为毫秒数
          const targetTime = time.getTime();
          // 如果传入时间小于等于当前时间或者大于一周后的时间，则禁用
          return targetTime <= currentTime || targetTime >= oneWeekLaterTime;
        }
      }
    }
  },
  created() {
    // 在页面加载时获取景点列表
    this.fetchAttractions()
  },
  methods: {
    // 获取景点列表
    fetchAttractions() {
      this.loading = true
      listAttractions(this.queryParams).then(response => {
        this.attractionsList = response.rows
        this.totalAttractions = response.total
        this.loading = false
      })
    },

    // 清空表单数据
    clearForm() {
      this.attractionsForm = {
        attractionsName: '',
        location: '',
        description: '',
        ticketPrice: null,
      }
    },

    // 处理预约按钮点击事件
    handleReserve(row) {
      // 获取场地ID
      const attractionsId = row.attractionsId;
      this.attractionsForm.description = row.description
      this.attractionsForm.location = row.location
      this.attractionsForm.attractionsName = row.attractionsName
      this.attractionsForm.ticketPrice = row.ticketPrice
      this.dialogTitle = '预约场地'; // 设置对话框标题为预约场地
      this.reserveDialogVisible = true; // 打开预约对话框
      // 将选中的场地ID赋值给selectedAttractionsId
      this.selectedAttractionsId = attractionsId;
    },

    // 添加景点
    handleAddAttractions() {
      this.dialogTitle = "新增景点"
      this.dialogButtonText = "添加"
      this.isReadOnly = false // 设置为可编辑模式
      this.dialogVisible = true // 打开对话框
    },

    // 添加景点
    addAttractions() {
      // 调用后端接口添加景点
      addAttractions(this.attractionsForm).then(() => {
        // 添加成功后重新获取景点列表
        this.fetchAttractions()
        this.dialogVisible = false // 关闭对话框
        // 清空表单数据
        this.clearForm()
      })
    },

    // 更新景点
    updateAttractions() {
      // 调用后端接口更新景点
      updateAttractions(this.attractionsForm).then(() => {
        // 更新成功后重新获取景点列表
        this.fetchAttractions()
        this.dialogVisible = false // 关闭对话框
        // 清空表单数据
        this.clearForm()
        // 将对话框按钮文本设置为其他值，避免再次触发更新操作
        this.dialogButtonText = '更新成功'
      })
    },

    // 查看景点详情
    handleView(row) {
      // 设置对话框标题为查看景点详情
      this.dialogTitle = '查看景点详情';
      // 设置对话框按钮文本为关闭
      this.dialogButtonText = '关闭';
      // 将表单设置为只读模式
      this.isReadOnly = true;
      // 调用后端接口获取景点详细信息
      getAttractions(row.attractionsId).then(response => {
        // 将获取到的景点详情填充到表单中
        this.attractionsForm = response.data;
        // 打开对话框
        this.dialogVisible = true;
      }).catch(error => {
        // 处理获取景点详情失败的情况
        console.error('获取景点详情失败:', error);
      });
    },

    // 删除景点
    handleDelete(row) {
      // 弹出确认框，确认删除后调用删除景点方法
      this.$confirm('确认删除该景点吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 调用后端接口删除景点
        deleteAttractions(row.attractionsId).then(() => {
          // 删除成功后重新获取景点列表
          this.fetchAttractions();
          // 弹出删除成功提示
          this.$message.success('景点删除成功！');
        }).catch(error => {
          // 弹出删除失败提示
          this.$message.error('景点删除失败，请重试！');
        });
      })
    },

    // 编辑按钮点击事件
    handleEdit(row) {
      // 将编辑的景点数据填充到表单中
      this.attractionsForm = Object.assign({}, row)
      this.dialogTitle = '编辑景点' // 设置对话框标题为编辑景点
      this.dialogButtonText = '更新' // 设置对话框按钮文本为更新
      this.isReadOnly = false // 设置为可编辑模式
      this.dialogVisible = true // 打开对话框
    },

    // 提交表单
    handleSubmit() {
      // 根据表单是否有景点ID来判断是添加还是更新
      if (this.attractionsForm.attractionsId) {
        this.updateAttractions() // 更新景点
      } else {
        this.addAttractions() // 添加景点
      }
    },

    // 关闭对话框
    handleCloseDialog() {
      this.dialogVisible = false
      this.reserveDialogVisible = false
      // 清空表单数据
      this.clearForm()
    },

    // 确定预约
    confirmReservation() {
      // 获取预约起止时间
      const [startTime, endTime] = this.reservationTime;
      // 执行预约操作，这里假设调用后端接口进行预约
      addReservation({
        attractionsId: this.selectedAttractionsId, // 传入景点ID
        startTime, // 传入预约开始时间
        endTime // 传入预约结束时间
      }).then(() => {
        // 预约成功后的处理逻辑
        this.$message.success('预约成功！');
        // 关闭预约对话框
        this.reserveDialogVisible = false;
        // 清空预约起止时间
        this.reservationTime = [];
      })
    }
  }
}
</script>
