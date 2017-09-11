package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * Description:
 * User: wen-sr
 * Date: 2017-09-11  11:45
 */
public interface IProductService {
    ServerResponse list(Integer categoryId, String keyword, int pageNum, int pageSize, String orderBy);
}
