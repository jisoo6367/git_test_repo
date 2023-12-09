package com.spring5.mypro00.common.paging.domain;

import java.util.List;

import com.spring5.mypro00.domain.MyReplyVO;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MyReplyPagingCreatorDTO {
	
	private List<MyReplyVO> myreplyList ;
	private long replyTotCnt ;
	
	public MyReplyPagingCreatorDTO(List<MyReplyVO> myreplyList, 
								   long replyTotCnt) {
		this.myreplyList = myreplyList ;
		this.replyTotCnt = replyTotCnt ;
	}

}
