package com.mmall.controller.backend;

import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * User: wen-sr
 * Date: 2017-09-11  13:25
 */
@RestController
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    IProductService iProductService;

    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    public ServerResponse getProductDetailById(Integer productId) {
        return iProductService.getProductDetailById(productId);
    }

}
