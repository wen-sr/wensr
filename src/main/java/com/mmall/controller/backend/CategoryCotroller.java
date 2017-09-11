package com.mmall.controller.backend;

import com.mmall.common.Constant;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Description:
 * User: wen-sr
 * Date: 2017-09-11  9:29
 */
@RestController
@RequestMapping("/manage/category")
public class CategoryCotroller {

    @Autowired
    ICategoryService iCateGoryService;

    /**
     * 获得平级子节点
     * @param categoryId
     * @param session
     * @return
     */
    @RequestMapping(value = "get_category.do", method = RequestMethod.POST)
    public ServerResponse getCategoryByParentId(@RequestParam(value = "categoryId", defaultValue = "0") int categoryId, HttpSession session){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请先登录");
        }

       return iCateGoryService.getCategoryByParentId(categoryId);
    }

    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    public ServerResponse addCategory(@RequestParam(value = "categoryId", defaultValue = "0") int categoryId, String categoryName){
        if(StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "节点名称参数不合法");
        }
        ServerResponse response = iCateGoryService.addCategory(categoryId, categoryName);
        return response;
    }

    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    public ServerResponse setCategoryName(@RequestParam(value = "categoryId") int categoryId, String categoryName){
        if(StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "传入参数不合法");
        }
        return iCateGoryService.setCategoryName(categoryId, categoryName);
    }

    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.POST)
    public ServerResponse getDeepCategory(@RequestParam(value = "categoryId", defaultValue = "0") int categoryId){
        return iCateGoryService.getDeepCategory(categoryId);
    }
}
