package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * Description:
 * User: wen-sr
 * Date: 2017-09-11  9:31
 */
public interface ICategoryService {
    ServerResponse getCategoryByParentId(int categoryId);

    ServerResponse addCategory(int categoryId, String categoryName);

    ServerResponse setCategoryName(int categoryId, String categoryName);

    ServerResponse getDeepCategory(int categoryId);
}