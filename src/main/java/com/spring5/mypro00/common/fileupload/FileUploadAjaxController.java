package com.spring5.mypro00.common.fileupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring5.mypro00.common.fileupload.domain.AttachFileDTO;

import net.coobird.thumbnailator.Thumbnailator;

@Controller
public class FileUploadAjaxController {
   
   private String uploadFileRepoDir = "C:/myupload";
   
   //form을 통한 다중 파일 업로드  //uploadFiles
   
   //1. 파일 업로드 요청 JSP 페이지 호출
   @GetMapping(value= {"/fileUploadAjax"})
   public String callFileUploadAjaxPage() {
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
   
   private String myDatePath() {
      
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
      
      Date date = new Date();
      
      String myDateStr = simpleDateFormat.format(date); 
      
      return myDateStr.replace("/", File.separator); //경로 구분자 참고함.
   }
   
   
   //2. 파일 업로드 처리
   @PostMapping(value= {"/fileUploadAjaxAction"},
		   		produces= "application/json; charset=utf-8")//produces:내가 보내는 파일타입
   @ResponseBody
   public List<AttachFileDTO> fileUploadAction(MultipartFile[] yourUploadFiles) {
      
	  List<AttachFileDTO> attachFileList = new ArrayList<AttachFileDTO>() ;
	  
	  AttachFileDTO attachFile = null;
	   
      String fileName = null;
      File myFileUploadPath = new File(uploadFileRepoDir, myDatePath());
      

      
      if(!myFileUploadPath.exists()) {
         myFileUploadPath.mkdirs();
      }
      
      //UUID를 이용한 고유한 파일이름 적용
      String myUuid = null;
      
//      for(MultipartFile uploadFile : uploadFiles) { <-- form태그를 통해 값이 전달 된 경우,
//                                            input태그의 name값과 매개변수이름이 같아야한다.
      for(MultipartFile uploadFile : yourUploadFiles) {
         System.out.println("===================");
         System.out.println("Upload File Name: " + uploadFile.getOriginalFilename());
         System.out.println("Upload File Size: " + uploadFile.getSize());

         attachFile = new AttachFileDTO();
         
         attachFile.setUploadPath(myDatePath());
         attachFile.setRepoPath(uploadFileRepoDir);
         
         fileName = uploadFile.getOriginalFilename();
         //파일이름.확장자, 경로명\파일이름.확장자
         fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
         
         attachFile.setFileName(fileName);
         
         myUuid = UUID.randomUUID().toString() ;
         
         attachFile.setUuid(myUuid);
         
         fileName = myUuid + "_" + fileName;
         
         File saveuploadFile = new File(myFileUploadPath, fileName);
         

         
         try {
            uploadFile.transferTo(saveuploadFile);
            
            File thumbnameFile = new File(myFileUploadPath, "s_" + fileName); 
            //이미지파일이면 썸네일 생성
            if(isImageFile(saveuploadFile)) {
            
            	attachFile.setFileType("I");
            	
               FileOutputStream myFos = new FileOutputStream(thumbnameFile);
               
               Thumbnailator.createThumbnail(uploadFile.getInputStream(), myFos, 20, 20);
               
               myFos.flush();
               myFos.close();
            } else {
            	attachFile.setFileType("F");
            }
         } catch (IllegalStateException | IOException e) {
            System.out.println("error: " + e.getMessage());
         }
         
         attachFileList.add(attachFile);
      }//for-end
      
      return attachFileList;
   }
   
   
   
}