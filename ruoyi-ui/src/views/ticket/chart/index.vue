<template>
  <el-row>
    <el-col :span="12">
      <el-card style="margin-right: 20px; height: 420px;">
        <!-- echarts 饼图容器 -->
        <div ref="pieChart" class="pie-chart"></div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import * as echarts from 'echarts';

import {getReservationCountsByAttractions} from "@/api/ticket/reservation";

export default {
  data() {
    return {
      // echarts 实例
      chartInstance: null,
      chartData: {}, // 存储从接口获取的数据
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
      getReservationCountsByAttractions().then(response => {
        console.log('Response data:', response.data); // 添加这行日志
        // 存储数据
        this.chartData = response.data;
        // 绘制饼图
        this.renderPieChart();
      }).catch(error => {
        console.error('Error fetching data:', error);
      });
    },
    // 绘制饼图
    renderPieChart() {
      // 数据处理
      const data = this.chartData;
      const legendData = Object.keys(data);
      const seriesData = legendData.map(name => ({ value: data[name], name }));

      // echarts配置项
      const option = {
        title: {
          text: '景点预约次数统计',
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
        },
        series: [
          {
            name: '预约次数',
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
