package com.spring5.mypro00.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring5.mypro00.common.paging.domain.MyBoardPagingCreatorDTO;
import com.spring5.mypro00.common.paging.domain.MyBoardPagingDTO;
import com.spring5.mypro00.domain.MyBoardAttachFileVO;
import com.spring5.mypro00.domain.MyBoardVO;
import com.spring5.mypro00.service.MyBoardService;

import lombok.Setter;

@Controller
@RequestMapping("/myboard")
public class MyBoardController {
	
//	@Setter(onMethod_ = @Autowired)
	private MyBoardService myBoardService ; 
	
	//(단일 생성자를 이용한 주입: 생성자가 여러개 이고, 기본 생성자가 포함되어 있으면, 무조건 기본 생성자를 사용함)
	public MyBoardController(MyBoardService myBoarService) {
		this.myBoardService = myBoarService ;
		//System.out.println("MyBoardController의 모든 필드 초기화 생성자 입니다.");
	}
	
	
//  (Setter를 이용한 주입) 
//  public MyBoardController() {
//     System.out.println("MyBoardController의 기본 생성자 입니다.");
//  }
//  
//  @Autowired
//  public void setMyBoardService(MyBoardService myBoardService) {
//     this.myBoardService = myBoardService ;
//  }
	
//	//게시물 목록 조회
//	@GetMapping("/list")
//	public String showBoardList(MyBoardPegingDTO myboardPaging, Model model) {
//		List<MyBoardVO> myBoardList = myBoardService.getBoardList(myboardPaging);
//		System.out.println("myBoardList: " + myBoardList);
//		
//		model.addAttribute("myBoardList", myBoardList);
//		
//		return "myboard/list" ;
//	}
	
	
	//수정
	@GetMapping("/list")
	public String showBoardList(MyBoardPagingDTO myboardPaging, Model model) {
		MyBoardPagingCreatorDTO pagingCreator =  myBoardService.getBoardList(myboardPaging) ;
		System.out.println("컨트롤러에 전달된 myboardPagingCreator: \n" + pagingCreator);
		
		model.addAttribute("pagingCreator", pagingCreator) ;
		
		return "myboard/list" ;
	}
	
//	등록 페이지 호출
	@GetMapping("/register")
	public String showBoardRegisterPage() {
		System.out.println("등록페이지 호출........");
		
		return "myboard/register" ;
	}
	
//	게시물 등록 처리
	@PostMapping("/register")
	public String registerNewBoard(MyBoardVO myboard ,
			                       RedirectAttributes redirectAttr) {
		
		List<MyBoardAttachFileVO> myAttachFileList = myboard.getAttachFileList();
		
		
//		if(myboard.getAttachFileList() != null) {
		if(myAttachFileList != null) {
			
			myboard.getAttachFileList()
					.forEach(attachFile -> System.out.println(attachFile.toString()));
		} else {
			System.out.println("<<<<<<<<<<<<<<첨부파일 없음>>>>>>>>>>>>>>>");
		}System.out.println();
		
		
		
		
		long bno = myBoardService.registerBoard(myboard);
		
		//return "redirect:/myboard/list?bno=" + bno ;
		
		redirectAttr.addFlashAttribute("result", bno) ;
		System.out.println("result: " + redirectAttr.getFlashAttributes());
		
		return "redirect:/myboard/list";
		
	}
	
	//특정 게시물 조회 페이지 , 수정 후 조회 페이지
	@GetMapping("/detail")
	public String showBoardDetail(Long bno, Model model, String result,
			  					  @ModelAttribute("myboardPaging") MyBoardPagingDTO myboardPaging) {

		MyBoardVO myboard = null ;
		myboard = myBoardService.getBoard(bno, result);
//				
//		if (result == null) {//목록페이지에서 조회요청
//		myboard = myBoardService.getBoard(bno) ;	
//		
//		} else if (result != null) {//수정 후 조회 요청
//		myboard = myBoardService.getBoard2(bno) ;
//		}
//		
		model.addAttribute("myboard", myboard) ;
		model.addAttribute("result", result) ;
		
		return "myboard/detail" ; //jsp파일로
	}
	
	
//	특정 게시물 수정삭제 페이지 호출 GET /myboard/modify 
	@GetMapping("/modify")
	public String showBoardModify(Long bno, Model model, MyBoardPagingDTO myboardPaging) {
		MyBoardVO myboard = myBoardService.getBoard2(bno) ;
		
		model.addAttribute("myboard", myboard) ;
			
		return "myboard/modify" ;
	}
	
//	특정 게시물 수정
	@PostMapping("/modify")
	public String modifyBoard(MyBoardVO myboard,
		      RedirectAttributes redirectAttr,
		      MyBoardPagingDTO myboardPaging) {

		boolean modifyResult = myBoardService.modifyBoard(myboard) ;
		
		if(modifyResult) {
		redirectAttr.addAttribute("result", "successModify") ;
		
		} else {
		redirectAttr.addAttribute("result", "failModify") ;
		}
		
		redirectAttr.addAttribute("bno", myboard.getBno()) ;
		redirectAttr.addAttribute("pageNum", myboardPaging.getPageNum());
		redirectAttr.addAttribute("rowAmountPerPage", myboardPaging.getRowAmountPerPage()) ;
		redirectAttr.addAttribute("scope", myboardPaging.getScope()) ;
		redirectAttr.addAttribute("keyword", myboardPaging.getKeyword());
		
		return "redirect:/myboard/detail" ;
	}
	
	
	
//	특정 게시물 삭제 POST /myboard/remove
	@PostMapping("/remove")
	public String removeBoard(Long bno, RedirectAttributes redirectAttr,
							  MyBoardPagingDTO myboardPaging) {
		
		//boolean removeResult = myBoardService.removeBoard(bno);
		
		if(myBoardService.modifyBdelFlag(bno)) {
		//if(myBoardService.removeBoard(bno)) {
			redirectAttr.addFlashAttribute("result", "successRemove");
		
		} else {
			redirectAttr.addFlashAttribute("result", "failRemove");
		}
		
		redirectAttr.addAttribute("pageNum", myboardPaging.getPageNum()) ;
		redirectAttr.addAttribute("rowAmountPerPage", myboardPaging.getRowAmountPerPage()) ;
		redirectAttr.addAttribute("scope", myboardPaging.getScope()) ;
		redirectAttr.addAttribute("keyword", myboardPaging.getKeyword());
		
		return "redirect:/myboard/list";
	}
	
	
	//특정 게시물의 조회 페이지 호출 후,
	//특정 게시물의 첨부파일 정보를 JSON으로 전달 (특정 게시물의 수정페이지에서 사용) ###########################
	@GetMapping(value="/getFiles", produces = {"application/json; charset=utf-8"})
	public @ResponseBody ResponseEntity<List<MyBoardAttachFileVO>> showAttachFiles (Long bno) {
		return new ResponseEntity<List<MyBoardAttachFileVO>>(myBoardService.getAttachFileList(bno), HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
}
