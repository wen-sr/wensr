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

    ServerResponse setSaleStatus(Integer productId, Integer status);

    ServerResponse getManageProductList(Integer pageNum, Integer pageSize);

    ServerResponse getManageProductSearch(Integer productId, String productName, int pageNum, int pageSize);

    ServerResponse getManageProductDetailById(Integer productId);
}
