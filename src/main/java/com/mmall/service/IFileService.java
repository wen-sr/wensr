package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Description:
 * User: wen-sr
 * Date: 2017-09-11  22:28
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
