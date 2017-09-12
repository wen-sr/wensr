package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Constant;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

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

    @Autowired
    IFileService iFileService;

    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    public ServerResponse productSave(Product product, HttpSession session) {
        return iProductService.saveOrUpdateProduct(product);
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    public ServerResponse getProductDetailById(Integer productId) {
        return iProductService.getManageProductDetailById(productId);
    }

    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    public ServerResponse setSaleStatus(Integer productId, Integer status, HttpSession session){
        return iProductService.setSaleStatus(productId, status);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    public ServerResponse list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "2") Integer pageSize) {
        return iProductService.getManageProductList(pageNum, pageSize);
    }

    @RequestMapping(value = "search.do", method = RequestMethod.POST)
    public ServerResponse search(String productName, Integer productId,
                                 @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "2") int pageSize){
        return iProductService.getManageProductSearch(productId, productName, pageNum, pageSize);
    }

    @RequestMapping(value = "upload.do", method = RequestMethod.POST)
    public ServerResponse uploadFile(@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpSession session){
       //权限控制
        /* User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        if(user.getRole() != Constant.Role.ROLE_ADMIN){
            return ServerResponse.createByErrorMessage("用户没有权限");
        }
*/
        if(file == null || file.getSize() == 0) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "参数错误");
        }

//        获得tomcat服务器upload文件夹的路径
        String path = request.getSession().getServletContext().getRealPath("upload");
//        文件上传成功后返回上传到ftp服务器上面的文件名称
        String targetFileName = iFileService.upload(file, path);
        if(StringUtils.isBlank(targetFileName)){
            return ServerResponse.createByErrorMessage("上传失败");
        }
//        拼接文件上传到服务器后的路径
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);

        return ServerResponse.createBySuccess(fileMap);
    }
    @RequestMapping(value = "richtext_img_upload.do", method = RequestMethod.POST)
    public Map reichtextImgUpload(@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpSession session, HttpServletResponse response){
        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
        //{
        //    "success": true/false,
        //        "msg": "error message", # optional
        //    "file_path": "[real file path]"
        //}

       //权限控制
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        if(user.getRole() != Constant.Role.ROLE_ADMIN){
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
        if(file == null || file.getSize() == 0) {
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }

        //获得tomcat服务器upload文件夹的路径
        String path = request.getSession().getServletContext().getRealPath("upload");
        //文件上传成功后返回上传到ftp服务器上面的文件名称
        String targetFileName = iFileService.upload(file, path);
        if(StringUtils.isBlank(targetFileName)){
            resultMap.put("success",false);
            resultMap.put("msg","上传失败");
            return resultMap;
        }

        //拼接文件上传到服务器后的路径
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

        resultMap.put("success",true);
        resultMap.put("msg","上传成功");
        resultMap.put("file_path",url);
        response.addHeader("Access-Control-Allow-Headers","X-File-Name");
        return resultMap;
    }

}
