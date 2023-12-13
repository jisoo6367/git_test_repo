package com.spring5.mypro00.common.fileupload;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

//@Controller
public class FileUploadAjaxControllerV2 {
	private String uploadFileRepoDir = "C:/myupload";
	
	//1. 파일 업로드 요청 JSP 페이지 호출
	//@GetMapping(value= {"/fileUploadAjax"})
	public String callFileUploadAjaxPage() {
		System.out.println("'Form을 통한 업로드 테스트' JSP페이지 호출==============");
		return "sample/fileUploadAjax";
	}
	
	
	
	//2. 파일 업로드 처리
	//@PostMapping(value="/fileUploadAjaxAction")
	public @ResponseBody String fileUploadAction(@ModelAttribute("ename") String ename,
												  MultipartFile[] uploadFiles) {
		
		String fileName = null;
		String myUuid = null;
		
		for(MultipartFile uploadFile : uploadFiles) {
			System.out.println("=====================================");
			System.out.println("Uploaded File Name: " + uploadFile.getOriginalFilename());
			System.out.println("Uploaded File Size: " + uploadFile.getSize());
			
			fileName = uploadFile.getOriginalFilename();
			//파일이름.확장자, 경로명|파일이름.확장자 (아래는 파일 이름만 남기는 처리임)
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
			
			//UUID를 이용한 고유한 파일 이름 적용
			myUuid = UUID.randomUUID().toString();
			
			fileName = myUuid + "_" + fileName;
			
			File saveUploadfile = new File(uploadFileRepoDir, fileName);
			
			try {
				uploadFile.transferTo(saveUploadfile);
			} catch (IllegalStateException | IOException e) {
				System.out.println("error: " + e.getMessage());
			}
			
		}
		
		return "yourSuccess";
	}
	
}
