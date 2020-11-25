package com.hourfun.cashexchange.service;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CaptchaService {
	@Autowired
    ServletContext servletContext;
	
	@Autowired
	TelegramSender sender;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	public String captchaImageSave(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        
        try {
            // 파일명에 부적합 문자가 있는지 확인한다.
            if(fileName.contains(".."))
                throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);
            
            String realPath = servletContext.getRealPath("/captcha");

            File savedFile = new File(realPath + "/" + fileName);
            
            file.transferTo(savedFile);
            
            
            sender.sendPhoto("capthca image", savedFile);
            
            return fileName;
        }catch(Exception e) {
            throw new FileUploadException("["+fileName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
        }
    }
	
	public void captchaMessageSave(String message) {
		
		redisTemplate.opsForValue().set("captcha", message);
		
	}
	
}
