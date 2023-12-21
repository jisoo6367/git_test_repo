package com.spring5.mypro00.domain;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyMemberVO {

	private String userId;
	private String userPw;
	private String userName;
	private Timestamp mregDate;
	private Timestamp mmodDate; //암호 등 계정정보 수정 날짜시간
	private String mdropFlag; // '0': 유지(false) , '1':탈퇴요청(true)
	private boolean enabled; // '0': 비활성화(false), '1': 활성화(true)
	
	private List<MyAuthorityVO> authorityList ;
	
	private boolean accountNonExpired;
	
	
}




