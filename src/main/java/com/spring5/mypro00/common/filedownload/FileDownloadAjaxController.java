package com.spring5.mypro00.common.filedownload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FileDownloadAjaxController {
	
	@GetMapping(value="/displayThumbnail")
	public ResponseEntity<byte[]> sendThumbnail (String fileName){
		//일반 컨트롤러지만 반환타입이 ResponseEntity라서 jsp호출 안함
		
		File thumbnailFile = new File(fileName);
		
		ResponseEntity<byte[]> result = null;
		
		HttpHeaders httpHeaders = new HttpHeaders();
		try {
			httpHeaders.add("Content-Type", Files.probeContentType(thumbnailFile.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(thumbnailFile),
					httpHeaders, HttpStatus.OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
