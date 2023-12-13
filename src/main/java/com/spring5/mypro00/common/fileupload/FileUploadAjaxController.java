package com.spring5.mypro00.common.fileupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnailator;

@Controller
public class FileUploadAjaxController {
   
   private String uploadFileRepoDir = "C:/myupload";
   
   //form을 통한 다중 파일 업로드  //uploadFiles
   
   //1. 파일 업로드 요청 JSP 페이지 호출
   @GetMapping(value= {"/fileUploadAjax"})
   public String callFileUploadFormPage() {
      System.out.println("'Ajax을 통한 업로드 테스트' JSP 페이지 호출=====");
      
      return "sample/fileUploadAjax";
   }
   
   //이미지파일에 대한 썸네일 이미지 저장
   //Step1: 업로드 파일에 대한 이미지 파일여부 검사 메소드
   private boolean isImageFile(File yourFile) {
      String yourfileContentType = null;
      try {
         yourfileContentType = Files.probeContentType(yourFile.toPath());
         System.out.println("fileContentType: " + yourfileContentType);
         
         return yourfileContentType.startsWith("image");
      } catch (IOException e) {
         System.out.println("오류: " + e.getMessage());
      }
      
      return false;
   }
   
   
   //2. 파일 업로드 처리
   @PostMapping(value= {"/fileUploadAjaxAction"})
   @ResponseBody
   public String fileUploadActionForm(MultipartFile[] yourUploadFiles) {
      System.out.println("yourUploadFiles: " + yourUploadFiles.toString());
      String fileName = null;
      
      //UUID를 이용한 고유한 파일이름 적용
      String myUuid = null;
      
//      for(MultipartFile uploadFile : uploadFiles) { <-- form태그를 통해 값이 전달 된 경우,
//                                            input태그의 name값과 매개변수이름이 같아야한다.
      for(MultipartFile uploadFile : yourUploadFiles) {
         System.out.println("===================");
         System.out.println("Upload File Name: " + uploadFile.getOriginalFilename());
         System.out.println("Upload File Size: " + uploadFile.getSize());
         
//         File saveuploadFile = new File(uploadFileRepoDir, uploadFile.getOriginalFilename());
         
         fileName = uploadFile.getOriginalFilename();
         //파일이름.확장자, 경로명\파일이름.확장자
         fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
         
         myUuid = UUID.randomUUID().toString() ;
         
         fileName = myUuid + "_" + fileName;
         
         File saveuploadFile = new File(uploadFileRepoDir, fileName);
         
         try {
            uploadFile.transferTo(saveuploadFile);
            
            if(isImageFile(saveuploadFile)) {
               FileOutputStream myFos = 
            		   new FileOutputStream(uploadFileRepoDir +  "/s_" + fileName);
               
               Thumbnailator.createThumbnail(uploadFile.getInputStream(), myFos, 20, 20);
               
               myFos.flush();
               myFos.close();
            }
         } catch (IllegalStateException | IOException e) {
            System.out.println("error: " + e.getMessage());
         }
      }
      
      return "yourSuccess";
   }
   
   
   
}