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
	<button class="appendInputBtn">파일입력창 추가</button>
	<input class="inputFile" type="file" name="uploadFiles" multiple="multiple"/><br>
	
</div>
	<button id="btnFileUpload" type="button">File Upload With Ajax</button>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
	$("#btnFileUpload").on("click", function(){
		//FormData() Ajax 파일 전송 시에 사용되는 Web API 클래스의 생성자
		var formData = new FormData();
		
		//uploadFiles 이름의 file 유형 input 요소를 변수에 저장
		var fileInputs = $("input[name='uploadFiles']");
		
		//fileInputs 에 저장된 파일들을 yourfiles 변수에 저장
		//file 타입의 input 요소에는 files 속성에 파일이 저장됩니다.
		//input이 하나만 있더라도 배열을 만들어서 저장시킴.***
		var yourFiles = fileInputs[0].files;
		
		console.log(yourFiles);
		if(yourFiles == null || yourFiles.length == 0){
			alert("파일을 선택해쥬세유");
			return ;
		}
		
		//FormData() 객체에 input의 파일을 모두 저장함
		for(var i = 0; i < yourFiles.length; i++){
			formData.append("yourUploadFiles", yourFiles[i]);
			
		}
		
		//formData() 객체(formData)를 서버로 전송
		//url 키에 명시된 주소의 컨트롤러에게 formData 객체를 POST 방식으로 전송.
		$.ajax({
			type: "post",
			url: "${contextPath}/fileUploadAjaxAction",
			data: formData,
			contentType: false,//contentType에 MIME 타입을 지정하지 않음.
			processData: false,//contentType에 설정된 형식으로 data를 처리하지 않음.
			dataType: "text",
			sucess: function(uploadResult, status){
				console.log("업로드 결과: " + uploadResult);
			}
		});
		
		
		
	});
	
	$(".appendInputBtn").on("click", function(){
		var str = '<input class="inputFile" type="file" name="uploadFiles" multiple="multiple"/><br>';
		$(".uploadDiv").append(str);
	});
</script>


</body>
</html>