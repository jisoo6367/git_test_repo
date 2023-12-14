<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>File Upload Form Page</title>
</head>
<body>
<h1>Ajax를 이용한 파일 업로드 페이지</h1>

<%-- 다중파일 업로드 방법1: form 방식의 파일업로드 --%>
<div class="uploadDiv">
   <input class="inputFile" type="file" name="uploadFiles" multiple="multiple" style="display:inblock"/>&nbsp;<br>
</div>
   <button type="button" id="addinputFile">파일입력창 추가</button><br>
   <button id="btnFileUpload" type="button">File Upload With Ajax</button>
   
 <div class="fileUploadResult">
   <ul>
      <%-- 업로드 후 처리결과가 표시될 영역 --%>
   </ul>
</div> 

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>

//[문제] input 요소가 여러개일 때, 파일 정보를 담는 방법 구현 : 완료
$("#addinputFile").on("click", function(){
   var str = '<input class="inputFile" type="file" name="uploadFiles" multiple="multiple" /><br>';
    $(".uploadDiv").append(str);
	$(this).attr("style", "display:in-block;");
})





//input 초기화를 위해 div 요소의 비어있는 input 요소를 복사해서 저장함.
var cloneFileInput = $(".uploadDiv").clone();
console.log("cloneFileInput: " + cloneFileInput.html());

function checkUploadFile(fileName, fileSize){
   
   var allowedMaxSize = 104857600 ;
   var regExpForbiddenFileExtension = /^(.*)(\.(exe|dll|sh|c|zip|alz|tar))?$/i ; //파일이름.확장자
   
   if(fileSize > allowedMaxSize){
      alert("업로드 파일의 제한된 크기(1MB)를 초과했습니다.");
      return false;
   }
   
   if(regExpForbiddenFileExtension.test(fileName)){
      alert("확장명이 없거나 [exe,dll,sh,c,zip,alz,tar] 형식 파일은 업로드 할 수 없습니다.");
      return false;
   }
   
   return true;
}

<%-- 업로드 결과 표시 함수 --%>
function showUploadResult (uploadResult){
	if(!uploadResult|| uploadResult.length==0){
		return;
	}
	var fileUploadResult = $(".fileUploadResult ul");
	var htmlStr = "";
	
	$(uploadResult).each(function(i, attachFile){
		var fullFileName = encodeURI(attachFile.repoPath + "/" +
									 attachFile.uploadPath + "/" +
									 attachFile.uuid + "_" +
									 attachFile.fileName );
		
		if(attachFile.fileType == "F") {
			htmlStr += "<li>"+ attachFile.fileName +"</li>";
		} else {
			var thumbnail = encodeURI(attachFile.repoPath + "/" +
									  attachFile.uploadPath + "/s_" +
									  attachFile.uuid + "_" +
									  attachFile.fileName );
			
			htmlStr 
				+= "<li>"
				+"		<img src='${contextPath}/displayThumbnail?fileName="+ thumbnail +"'>"
				+ attachFile.fileName +"</li>";
		}
	});<%--each-end--%>
	
	fileUploadResult.html(htmlStr);
//	fileUploadResult.append(htmlStr);
}

<%-- 업로드 처리 --%>
$("#btnFileUpload").on("click", function(){
   //FormData() Ajax 파일 전송 시에 사용되는 Web API 클래스의 생성자
   var formData = new FormData();
   
   //uploadFiles 이름의 file 유형 input 요소를 변수에 저장
   var fileInputs = $("input[name='uploadFiles']");
   console.log(fileInputs.length);
   
   //fileInputs 변수에 대입된 input들에 저장된 파일들을 files 변수에 저장.
   //file 타입의 input요소에는 files 속성에 파일이 저장됩니다.
   //fileInputs[0]은 첫번째 input 요소를 의미(input요소가 하나만 있더라도 [0]을 명시해야 한다.)
   var yourFiles = null;
   
   //FormData 객체에 input파일을 모두 저장함.
   for(var i = 0; i < fileInputs.length; i++){{
         yourFiles = fileInputs[i].files;
         
//for문을 중첩하지않으면 i파일이 덮어써지기 때문에 0번째파일 저장하고 바로 올리고~ 1번째파일 저장하고 바로 올리고~ 해야함
         for(var j = 0; j < yourFiles.length; j++ ){
            //파일크기 검사
            /*if(!checkUploadFile(yourFiles[j].name, yourFiles[j].size)){
               console.log("파일이름: " + yourFiles[j].name);
               return;
            }*/
            
            formData.append("yourUploadFiles", yourFiles[j])
         }
         
      }
   
   }
   
   if(yourFiles == null || yourFiles.length == 0){
      alert("파일을 선택하세요");
      return;
   }
   
   //FormData() 객체(formData)를 서버로 전송(By Ajax)
   //url 키에 명시된 주소의 컨트롤러에게 formData 객체를 POST 방식으로 전송.
   $.ajax({
      type:"post",
      url: "${contextPath}/fileUploadAjaxAction",
      data: formData ,
      contentType: false , //contentType에 MIME 타입을 지정하지 않음.
      processData: false , //contentType에 설정된 형식으로 data를 처리하지 않음.
      dataType: "json",
      success: function(uploadResult, status){
//         console.log("업로드 결과: " + uploadResult);
//         console.log(attachFileList);
			console.log(uploadResult);
         
         $("inputFile").val("");
         
         showUploadResult(uploadResult);
      }
      
   
   });
   
})
</script>

</body>

</html>
