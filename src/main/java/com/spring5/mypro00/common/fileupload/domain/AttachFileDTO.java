package com.spring5.mypro00.common.fileupload.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttachFileDTO {

	private String fileName ; // 원본 파일 이름
	private String uploadPath ; // 업로드 된 파일의 경로 (yyyy/MM/dd)
	private String uuid ; // 파일이름에 추가된 UUID.tostring() 값
	private String fileType ; // "I":이미지 파일, "F":그 외
	private String repoPath = "C:/myupload"; //
	
	
	
}
