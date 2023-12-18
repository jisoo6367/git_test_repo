//package com.spring5.mypro00.common.fileupload;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.nio.file.Files;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.spring5.mypro00.common.fileupload.domain.AttachFileDTO;
//
//import net.coobird.thumbnailator.Thumbnailator;
//
//@Controller
//public class FileUploadAjaxController2 {
//   
//	private String uploadFileRepoDir = "c:/myupload"; //저장할 경로 설정
//	
//	@GetMapping(value = "/fileUploadAjax") // mypro00/fileUploadAjax 라고 url에 치면
//	public String callFileUploadAjaxPage() {
//		return "sample/fileUploadAjax"; // webapp > web-INF > views > sample > fileUploadAjax 해당 경로의 JSP를 호출
//	}
//	
//	private boolean isImageFile(File yourFile) {
//		
//		String yourFileContentType = null;
//		
//		try {
//			yourFileContentType = Files.probeContentType(yourFile.toPath());
//			// 확장자가 없는 파일이면 null을 반환 / 있으면 "image","text"..
//			return yourFileContentType.startsWith("image"); // 확장자가 image로 시작하는 파일이 있으면 true를 반환
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return false; 
//	}
//
//	private String getDateFmtPathName() {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd") ;
//		String dateFmtStr = sdf.format(new Date()); // 오늘 날짜로
//		return dateFmtStr;
//	}
//	
//	public List<AttachFileDTO> fileUploadAction(MultipartFile[] yourUploadFiles){
//		List<AttachFileDTO> attachFileList = new ArrayList<AttachFileDTO>(); //DTO타입의 배열 담을 변수 선언
//		AttachFileDTO attachFile = null ; //이 변수는 무슨 목적이람? 저장되는 파일하나?
//		String fileName = null ; //파일이름 저장할 변수 선언
//		String myUuid = null; //uuid로 고유하게 만든 파일 이름 저장할 변수 선언 //왜 이렇게 변수두개써? uuid쓴다음 바로위변수에 담으면 되는거아닌가?
//		String dateFmtStr = getDateFmtPathName(); //오늘날짜로 문자열로 뽑는 위 메서드를 실행한 변수
//		
//		File fileUploadPath = new File (uploadFileRepoDir, dateFmtStr);//uploadFileRepoDir: 경로 "c:/myupload"를 담은 변수
//		// => C:/myupload\2023/12/14
//		if(!fileUploadPath.exists()) { // 파일 경로가 없으면
//			fileUploadPath.mkdirs();   // 만들어라
//		}
//		for(MultipartFile uploadFile : yourUploadFiles) { 
//			attachFile = new AttachFileDTO(); //DTO객체 생성
//			attachFile.setUploadPath(dateFmtStr); 
//			attachFile.setRepoPath(uploadFileRepoDir); //아예 첨부터의 경로 c:/myupload
//			fileName = uploadFile.getOriginalFilename(); // getOriginalFilename: 확장자를 포함한 파일의 이름을 반환
//			fileName = fileName.substring(fileName.lastIndexOf("\\")+ 1); // "\"바로 다음 위치부터 반환
//			attachFile.setFileName(fileName); 
//			myUuid = UUID.randomUUID().toString();
//			attachFile.setUuid(myUuid); 
//			fileName = myUuid + "_" + fileName;
//			File saveUploadFile = new File (fileUploadPath, fileName);// => C:/myupload\2023/12/14/uuid_파일이름.txt
//			
//			try {
//				uploadFile.transferTo(saveUploadFile); //업로드파일이 왜이렇게많아 뭐하느거야?
//				if(isImageFile(saveUploadFile)) {
//					attachFile.setFileType("I");
//					File thumbnailFile = new File(fileUploadPath, "s_" + fileName);
//					FileOutputStream myFos = new FileOutputStream(thumbnailFile); //왜 OS는 매개변수에 넣어서 뉴객체만들고
//					InputStream myIs = uploadFile.getInputStream(); 			//왜 IS는 uploadFile.겟IS로 만들지?
//					Thumbnailator.createThumbnail(myIs, myFos, 20, 20);
//					myIs.close();
//					myFos.flush();
//					myFos.close();
//				}else {
//					attachFile.setFileType("F");
//				}
//			} catch (IllegalStateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
//			
//			attachFileList.add(attachFile); // 배열통에 DTO객체담기
//			
//		}//for-end
//		return null;
//	}//fileUploadAction메서드-end
//	
//	@PostMapping("/deleteFile")
//	public ResponseEntity<String> deleteFile (String fileName, String fileType){
//		try {
//			fileName = URLDecoder.decode(fileName, "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		File delFile = new File (fileName);
//		boolean delResult = delFile.delete(); //삭제했으면 1반환 -> true
//		if(!delResult) { 
//			return new ResponseEntity<String>("F", HttpStatus.OK); //HttpStatus 이거는 무슨상황일 때 넣어야하는거지?
//		}
//		if(fileType.equals("I")) {
//			delFile = new File(fileName.replaceFirst("s_", "")); // ?
//			delResult = delFile.delete();
//		}
//		return delResult ? new ResponseEntity<String>("S", HttpStatus.OK) //delResult가 true면
//						: new ResponseEntity<String>("F", HttpStatus.OK); //delResult가 false면
//	}
//	
//	
//	
//	
//	
//	
//	
//}