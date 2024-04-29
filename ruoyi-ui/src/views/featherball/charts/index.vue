<template>
  <el-row>
    <el-col :span="12">
      <el-card style="margin-right: 20px; height: 420px;">
        <!-- echarts 饼图容器 -->
        <div ref="pieChart"  class="pie-chart" ></div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import * as echarts from 'echarts'
import {getBalanceTotalByType} from "@/api/featherball/balanceRecord";

export default {
  data() {
    return {
      // echarts 实例
      chartInstance: null
    };
  },
  mounted() {
    // 初始化 echarts 实例
    this.chartInstance = echarts.init(this.$refs.pieChart);

    // 加载数据并绘制饼图
    this.loadDataAndDrawPieChart();
  },
  methods: {
    // 加载数据并绘制饼图
    loadDataAndDrawPieChart() {
      // 调用后端接口获取数据
      getBalanceTotalByType().then(response => {
        console.log('Response data:', response.data); // 添加这行日志
        // 数据处理
        const data = response.data;
        const legendData = [];
        const seriesData = [];
        const keys = Object.keys(data);
        // 筛选出type为1-3的数据并构造echarts的series数据格式
        for (const key  in keys) {
          const type = parseInt(key); // 将键转换为整数
          if (type >= 1 && type <= 3) {
            let typeName = '';
            switch (type) {
              case 1:
                typeName = '购买';
                break;
              case 2:
                typeName = '维修';
                break;
              case 3:
                typeName = '预约';
                break;
            }
            legendData.push(typeName);
            seriesData.push({ value: data[type], name: typeName });
          }
        }

        // echarts配置项
        const option = {
          title: {
            text: '交易类型消费额分布',
            x: 'center',
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 10,
            data: legendData,
            textStyle: {
              fontSize: 14, // 根据需要调整字体大小
              fontFamily: 'Arial, sans-serif' // 指定字体
            },
            formatter: function(name) {
              return name;
            }
          },
          series: [
            {
              name: '消费额',
              type: 'pie',
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '30',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: true
              },
              data: seriesData
            }
          ]
        };

        // 使用配置项绘制饼图
        this.chartInstance.setOption(option);
      }).catch(error => {
        console.error('Error fetching data:', error);
      });
    }
  }
};

</script>

<style scoped lang="scss">
.pie-chart {
  width: 100%;
  height: 300px;
  margin: 50px auto 0 auto; /* 上方调整为50像素 */
}
</style>
