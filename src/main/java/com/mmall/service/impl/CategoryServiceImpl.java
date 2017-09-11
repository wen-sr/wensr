package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Description:
 * User: wen-sr
 * Date: 2017-09-11  9:31
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse getCategoryByParentId(int categoryId) {
        List<Category> categoryList = categoryMapper.getCategoryByParentId(categoryId);
        if(org.apache.commons.collections.CollectionUtils.isEmpty(categoryList)){
            return ServerResponse.createByErrorMessage("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse addCategory(int categoryId, String categoryName) {
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(categoryId);
        category.setStatus(true);
        int resultCount = categoryMapper.insert(category);
        if(resultCount > 0) {
            return ServerResponse.createBySuccessMsg("添加品类成功");
        }
        return ServerResponse.createBySuccessMsg("添加品类失败");
    }

    @Override
    public ServerResponse setCategoryName(int categoryId, String categoryName) {
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int resultCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(resultCount > 0 ){
            return ServerResponse.createBySuccessMsg("修改品类名称成功");
        }
        return ServerResponse.createByErrorMessage("修改品类名称失败");
    }

    @Override
    public ServerResponse getDeepCategory(int categoryId) {
        List<Integer> categoryIdList = Lists.newArrayList();


        Set<Category> categorySet = Sets.newHashSet();

        findDeepChildrenCategory(categorySet, categoryId);

        for(Category c : categorySet){
            categoryIdList.add(c.getId());
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }


    private Set<Category> findDeepChildrenCategory(Set<Category> categorySet, int categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null ){
            categorySet.add(category);
        }
        //查找子节点
//        List<Category> categoryList = (List<Category>) getCategoryByParentId(categoryId).getData();
//        if(CollectionUtils.isEmpty(categoryList)){
//            return categorySet;
//        }
//        使用mybatis不会返回null，返回的是size为0的对象，所以不需要判断null
        List<Category> categoryList = categoryMapper.getCategoryByParentId(categoryId);
        for(Category c : categoryList){
            findDeepChildrenCategory(categorySet, c.getId());
        }
        return categorySet;
    }
}
