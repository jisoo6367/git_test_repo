package com.spring5.mypro00.common.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User; // User 임포트 아파치 아님

import com.spring5.mypro00.domain.MyMemberVO;

public class MyMemberUser extends User{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	public MyMemberUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
//
//		super(username, password, authorities);
//	}
		private MyMemberVO mymember;
		
		public MyMemberUser (MyMemberVO mymember) {
			super(mymember.getUserId(),
				  mymember.getUserPw(),
				  mymember.getAuthorityList() //List<MyAuthorityVO>
							.stream()  //Stream<MyAuthorityVO>로 변환)
							.map(myauth -> new SimpleGrantedAuthority(myauth.getAuthority())) //Stream<GranteAuthorityVO>
							.collect(Collectors.toList())
			);
			this.mymember = mymember;
		}
	 

}
