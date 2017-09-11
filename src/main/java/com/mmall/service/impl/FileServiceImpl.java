package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import jdk.nashorn.internal.runtime.regexp.joni.constants.TargetInfo;
import org.apache.commons.net.ftp.FTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Description:
 * User: wen-sr
 * Date: 2017-09-11  22:30
 */
@Service
public class FileServiceImpl implements IFileService{

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        //获得文件原名称
        String fileName = file.getOriginalFilename();
        //获得文件拓展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        //使用UUID生成上传后的文件名
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;

        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);
        //判断tomcat服务器上是否有path对应的文件夹（webapp目录下），如果没有则创建该文件夹
        File filedir = new File(path);
        if(!filedir.exists()){
            filedir.setWritable(true);
            filedir.mkdirs();
        }
        //创建上传后的文件对象
        File targetFile = new File(path, uploadFileName);

        try {
            //文件上传
            file.transferTo(targetFile);
            //文件上传到FTP服务器上
            boolean isOK = FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            if(!isOK){
                return "上传服务器失败";
            }
            //删除掉tomcat服务器下的文件
            targetFile.delete();

        } catch (IOException e) {
            logger.error("文件上传服务器失败", e);
            return null;
        }

        //返回上传后的文件名
        return targetFile.getName();
    }
}
