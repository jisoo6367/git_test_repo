package com.spring5.mypro00.common.paging.domain;

import java.util.List;

import com.spring5.mypro00.domain.MyReplyVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class MyReplyPagingCreatorDTO {
	
	private List<MyReplyVO> myreplyList ;
	private long replyTotCnt ;
	private MyReplyPagingDTO myReplyPaging;
	
	public MyReplyPagingCreatorDTO(List<MyReplyVO> myreplyList, 
								   long replyTotCnt,
								   MyReplyPagingDTO myReplyPaging) {
		this.myreplyList = myreplyList ;
		this.replyTotCnt = replyTotCnt ;
		this.myReplyPaging = myReplyPaging;
	}

}
