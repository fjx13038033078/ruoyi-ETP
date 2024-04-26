import request from '@/utils/request'

// 查询所有景点列表
export function listAttractions(query) {
  return request({
    url: '/ticket/attractions/listAll',
    method: 'get',
    params: query
  })
}

// 查询景点详细信息
export function getAttractions(attractionsId) {
  return request({
    url: '/ticket/attractions/detail',
    method: 'get',
    params: { attractionsId }
  })
}

// 添加景点
export function addAttractions(data) {
  return request({
    url: '/ticket/attractions/add',
    method: 'post',
    data: data
  })
}

// 更新景点信息
export function updateAttractions(data) {
  return request({
    url: '/ticket/attractions/update',
    method: 'post',
    data: data
  })
}

// 删除景点
export function deleteAttractions(attractionsId) {
  return request({
    url: '/ticket/attractions/delete',
    method: 'get',
    params: { attractionsId }
  })
}
