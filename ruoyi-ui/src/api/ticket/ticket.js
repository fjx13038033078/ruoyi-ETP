import request from '@/utils/request'

// 查询所有门票列表
export function listTickets(query) {
  return request({
    url: '/ticket/ticket/listAll',
    method: 'get',
    params: query
  })
}

// 查询门票详细信息
export function getTicket(ticketId) {
  return request({
    url: '/ticket/ticket/detail',
    method: 'get',
    params: { ticketId }
  })
}

// 添加门票
export function addTicket(data) {
  return request({
    url: '/ticket/ticket/add',
    method: 'post',
    data: data
  })
}

// 退票
export function refundTicket(ticketId) {
  return request({
    url: '/ticket/ticket/refund',
    method: 'get',
    params: { ticketId }
  })
}

// 更新门票信息
export function updateTicket(data) {
  return request({
    url: '/ticket/ticket/update',
    method: 'post',
    data: data
  })
}
