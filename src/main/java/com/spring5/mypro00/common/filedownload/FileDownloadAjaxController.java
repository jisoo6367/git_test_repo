package com.spring5.mypro00.common.filedownload;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;


import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileDownloadAjaxController {
   @GetMapping(value= "/displayThumbnail")
   public ResponseEntity<byte[]> sendThumbnail(String fileName){
      
      File thumbnailFile = new File(fileName);
      
      ResponseEntity<byte[]> result = null;
      
      HttpHeaders httpHeaders = new HttpHeaders();
      try {
         httpHeaders.add("content-Type", Files.probeContentType(thumbnailFile.toPath()));
         result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(thumbnailFile),
                                    httpHeaders, HttpStatus.OK);
      } catch (IOException e) {
         e.printStackTrace();
      }
      
      return result;
   }
   
    @GetMapping(value = "/fileDownloadAjax", produces = "application/octet-stream") //octet-stream: 파일종류 상관없이
      @ResponseBody
      public ResponseEntity<Resource> fileDownloadAjaxAction (String fileName
                                                //,@RequestHeader("User-Agent") String userAgent
                                                ){
         System.out.println("fileName: "+ fileName);
         //C:/myupload/2023\12\15/f08e73aa-9812-4cd9-8381-1bafc6978ed1_업로드 테스트 PPT파일.pptx
         //System.out.println("userAgent: "+ userAgent);
         //Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36
         
         Resource myResource = new FileSystemResource(fileName);
         
         if(!myResource.exists()) {
            return new ResponseEntity<Resource>(HttpStatus.OK);
         }
            
         String downloadName = myResource.getFilename();
         System.out.println("그냥downloadName: "+ downloadName);
         //f08e73aa-9812-4cd9-8381-1bafc6978ed1_업로드 테스트 PPT파일.pptx
         
         //UUID가 제거된 파일이름
         downloadName = downloadName.substring(downloadName.indexOf("_") + 1);
         System.out.println("UUID제거된downloadName: "+ downloadName);
         
         HttpHeaders httpHeaders = new HttpHeaders();
         
         String _downloadName = null;
         
         try {
            _downloadName = URLEncoder.encode(downloadName, "UTF-8"); // IE, OLD-Edge
            System.out.println("Encoding된downloadName: "+ _downloadName);
            
			//downloadName = new String(downloadName.getBytes("utf-8")); 
			//System.out.println("downloadName4: " + downloadName);
            
            _downloadName = new String(downloadName.getBytes("utf-8"), "ISO-8859-1"); //(IE, OLD-Edge)아닌거
            System.out.println("byte로 바뀐downloadName: "+ _downloadName);
            
         } catch (UnsupportedEncodingException e) {   
            e.getMessage();
         }
         
         httpHeaders.add("Content-Disposition", "attachment; fileName=" + _downloadName);
            
         return new ResponseEntity<Resource>(myResource, httpHeaders, HttpStatus.OK);
      }

}