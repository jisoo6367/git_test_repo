<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>File Upload Ajax Page</title>
</head>
<body>
<h1>Ajax를 이용한 파일 업로드 페이지</h1>
<%-- 다중파일 업로드 방법2: Ajax 방식의 파일업로드 --%>
<div class="uploadDiv">
	<input type="text" name="ename" /><br><br>
	<button class="appendInputBtn">파일입력창 추가</button><br>
	<input class="inputFile" type="file" name="uploadFiles" multiple="multiple"/><br>
	
</div>
	<button id="btnFileUpload" type="button">File Upload With Ajax</button>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>

function checkUploadFile(fileSize){
	var allowedMaxSize = '1048576';
	
	if(fileSize > allowedMaxSize){
		alert("업로드 파일이 1MB를 초과했음");
		return false;
	}
	return true;
}

$("#btnFileUpload").on("click", function(){
	   //FormData() Ajax 파일 전송 시에 사용되는 Web API 클래스의 생성자
	   var formData = new FormData();
	   
	   //uploadFiles 이름의 file 유형 input 요소를 변수에 저장
	   var fileInputs = $("input[name='uploadFiles']");
	   
	   //fileInputs 변수에 대입된 input들에 저장된 파일들을 files 변수에 저장.
	   //file 타입의 input요소에는 files 속성에 파일이 저장됩니다.
	   //fileInputs[0]은 첫번째 input 요소를 의미(input요소가 하나만 있더라도 [0]을 명시해야 한다.)
	   //var yourFiles = fileInputs[0].files;
	   var yourFiles = {};
	   
	   for(var i = 0; i < fileInputs.length; i++){
		   yourFiles[i] = fileInputs[i].files;
	   }
	   console.log(yourFiles);
	   //[문제] input 요소가 여러개일 때, 파일 정보를 담는 방법 구현
	   
	   if(yourFiles == null || yourFiles.length == 0){
	      alert("파일을 선택하세요");
	      return;
	   }
	   
	   for(var i = 0; i < yourFiles.length; i++){
		   //파일크기검사
		   /* if(checkUploadFile(yourFiles[i].size)){
			   return ;
		   } */
		   
	       formData.append("yourUploadFiles", yourFiles[i])
	   }
	   
	   //FormData() 객체(formData)를 서버로 전송(By Ajax)
	   //url 키에 명시된 주소의 컨트롤러에게 formData 객체를 POST 방식으로 전송.
	   $.ajax({
	      type: "post",
	      url: "${contextPath}/fileUploadAjaxAction",
	      data: formData ,
	      contentType: false , //contentType에 MIME 타입을 지정하지 않음.
	      processData: false , //contentType에 설정된 형식으로 data를 처리하지 않음.
	      dataType: "text",
	      success: function(uploadResult, status){
	         console.log("업로드 결과: " + uploadResult);
	      }
	      
	   
	   })//ajax-end
	   
	   console.log(yourFiles);
	   
	})//function-end

	
	$(".appendInputBtn").on("click", function(){
		var str = '<input class="inputFile" type="file" name="uploadFiles" multiple="multiple"/><br>';
		$(".uploadDiv").append(str);
	});
</script>


</body>
</html>