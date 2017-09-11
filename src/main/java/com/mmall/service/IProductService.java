package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

/**
 * Description:
 * User: wen-sr
 * Date: 2017-09-11  11:45
 */
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);


    ServerResponse list(Integer categoryId, String keyword, int pageNum, int pageSize, String orderBy);

    ServerResponse getProductDetailById(Integer productId);
}
