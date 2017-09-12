package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Constant;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ICategoryService iCategoryService;

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

    /**
     * 分页排序查询商品列表
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public ServerResponse list(Integer categoryId, String keyword, int pageNum, int pageSize, String orderBy) {
        if(categoryId == null && StringUtils.isBlank(keyword)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "请求的类目id和关键字错误");
        }

        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuffer("%").append(keyword).append("%").toString();
        }

        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null) {
            categoryIdList = iCategoryService.getDeepCategory(categoryId).getData();
        }

        if(categoryIdList.size() == 0 && StringUtils.isBlank(keyword)){
            //如果查询的类目不存在，且关键字为空，则返回空分页对象
            PageHelper.offsetPage(pageNum, pageSize);
            return ServerResponse.createBySuccess(new PageInfo());
        }

        PageHelper.startPage(pageNum, pageSize);

        //排序
        if(StringUtils.isNotBlank(orderBy)){
            if(Constant.ProductOrderBy.PRICE_ORDER_BY.contains(StringUtils.lowerCase(orderBy))){
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }


        List<Product> productList = productMapper.selectByProductNameAndCategoryId(StringUtils.isBlank(keyword) ? null: keyword, categoryIdList.size() == 0 ? null : categoryIdList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product p : productList) {
            ProductListVo pv = this.assembleProductListVo(p);
            productListVoList.add(pv);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);

        return ServerResponse.createBySuccess(pageInfo);
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

        if(product.getStatus() != Constant.productStatus.ON_SELL.getCode()){
            return ServerResponse.createByErrorMessage("该商品已下架");
        }

        ProductDetailVo productDetailVo = this.assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }


    @Override
    public ServerResponse getManageProductDetailById(Integer productId) {
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "传入的参数错误");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null) {
            return ServerResponse.createByErrorMessage("该商品已下架或删除");
        }

        ProductDetailVo productDetailVo = this.assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }


    @Override
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        if(productId == null || status == null ){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数错误");
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int resultCount = productMapper.updateByPrimaryKeySelective(product);
        if(resultCount > 0) {
            return ServerResponse.createBySuccessMsg("修改商品状态成功");
        }
        return ServerResponse.createByErrorMessage("修改商品状态失败");
    }

    @Override
    public ServerResponse getManageProductList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product p : productList) {
            ProductListVo productListVo = this.assembleProductListVo(p);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse getManageProductSearch(Integer productId, String productName, int pageNum, int pageSize) {
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuffer("%").append(productName).append("%").toString();
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectByProductNameAndId(productId, productName);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product p : productList) {
            ProductListVo productListVo = this.assembleProductListVo(p);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);

        return ServerResponse.createBySuccess(pageInfo);

    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
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

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        productDetailVo.setParentCategoryId(category.getParentId());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }
}
