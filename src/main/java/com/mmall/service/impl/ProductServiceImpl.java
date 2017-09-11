package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Proc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

/**
 * Description:
 * User: wen-sr
 * Date: 2017-09-11  11:45
 */
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductMapper productMapper;


    /**
     * 新增或更新产品信息
     * @param product
     * @return
     */
    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if(product == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "产品参数不正确");
        }

        if(StringUtils.isNotBlank(product.getSubImages())){
            String[] subImageArray = product.getSubImages().split(",");
            if(subImageArray.length > 0) {
                product.setMainImage(subImageArray[0]);
            }
        }

        if(product.getId() != null ){
            int resultCount = productMapper.updateByPrimaryKeySelective(product);
            if(resultCount > 0) {
                return ServerResponse.createBySuccessMsg("更新产品信息成功");
            }else {
                return ServerResponse.createByErrorMessage("更新产品信息失败");
            }
        }else{
            int resultCount = productMapper.insert(product);
            if(resultCount > 0) {
                return ServerResponse.createBySuccessMsg("新增产品信息成功");
            }else {
                return ServerResponse.createByErrorMessage("新增产品信息失败");
            }
        }
    }


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

    @Override
    public ServerResponse getProductDetailById(Integer productId) {
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "传入的参数错误");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null) {
            return ServerResponse.createByErrorMessage("该商品已下架或删除");
        }



        return null;
    }


    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setId(product.getId());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setName(product.getName());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setStatus(product.getStatus());


        //Category category =
        //productDetailVo.setParentCategoryId();



        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }
}
