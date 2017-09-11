package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * User: wen-sr
 * Date: 2017-09-11  11:44
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    IProductService iProductService;

    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    public ServerResponse list(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "5") int PageSize,
                               @RequestParam(value = "orderBy", defaultValue = "") String orderBy){

        return iProductService.list(categoryId, keyword, pageNum, PageSize, orderBy);
    }
}


