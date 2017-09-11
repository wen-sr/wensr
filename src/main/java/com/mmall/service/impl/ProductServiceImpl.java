package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Proc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * User: wen-sr
 * Date: 2017-09-11  11:45
 */
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductMapper productMapper;

    @Override
    public ServerResponse list(Integer categoryId, String keyword, int pageNum, int pageSize, String orderBy) {
        if(categoryId == null && StringUtils.isBlank(keyword)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "请求的类目id和关键字错误");
        }
        List<Product> productList = Lists.newArrayList();
        if (categoryId != null ){
            productList = productMapper.selectByCategoryId(categoryId);
        }

        return null;
    }
}
