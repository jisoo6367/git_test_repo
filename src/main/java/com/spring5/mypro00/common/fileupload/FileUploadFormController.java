package com.spring5.mypro00.common.fileupload;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadFormController {
	private String uploadFileRepoDir = "C:/myupload";
	
	//1. 파일 업로드 요청 JSP 페이지 호출
	@GetMapping(value= {"/fileUploadForm"})
	public String callFileUploadFormPage() {
		System.out.println("'Form을 통한 업로드 테스트' JSP페이지 호출==============");
		return "sample/fileUploadForm";
	}
	
	
	
	//2. 파일 업로드 처리
	@PostMapping(value="/fileUploadFormAction")
	public String fileUploadAction(@ModelAttribute("ename") String ename,
									MultipartFile[] uploadFiles) {
		
		String originalFileName = null;
		for(MultipartFile uploadFile : uploadFiles) {
			System.out.println("=====================================");
			System.out.println("Uploaded File Name: " + uploadFile.getOriginalFilename());
			System.out.println("Uploaded File Size: " + uploadFile.getSize());
			
//			File saveUploadFile = new File(uploadFileRepoDir, uploadFile.getOriginalFilename());
			originalFileName = uploadFile.getOriginalFilename();
			//파일이름.확장자, 경로명|파일이름.확장자 (아래는 파일 이름만 남기는 처리임)
			originalFileName = originalFileName.substring(originalFileName.lastIndexOf("\\") + 1);
			
			File saveUploadFile = new File(uploadFileRepoDir, uploadFile.getOriginalFilename());
			
			try {
				uploadFile.transferTo(saveUploadFile);
			} catch (IllegalStateException | IOException e) {
				System.out.println("error: " + e.getMessage());
			}
			
		}
		
		return "sample/fileUploadFormResult";
	}
	
}
