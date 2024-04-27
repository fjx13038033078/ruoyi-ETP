import request from '@/utils/request'

// 查询所有预约列表
export function listReservations(query) {
  return request({
    url: '/ticket/reservation/listAll',
    method: 'get',
    params: query
  })
}

// 查询预约详细信息
export function getReservation(reservationId) {
  return request({
    url: '/ticket/reservation/detail',
    method: 'get',
    params: { reservationId }
  })
}

// 添加预约
export function addReservation(data) {
  return request({
    url: '/ticket/reservation/add',
    method: 'post',
    data: data
  })
}

// 更新预约信息
export function cancelReservation(reservationId) {
  return request({
    url: '/ticket/reservation/cancel',
    method: 'get',
    params: { reservationId }
  })
}
