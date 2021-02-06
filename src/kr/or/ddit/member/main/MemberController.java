package kr.or.ddit.member.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.member.vo.MemberVO;

/*
회원을 관리하는 프로그램 작성하기
(DB시스템의 MYMEMBER테이블 이용)

- 처리조건
1. 아래 메뉴의 기능을 모두 구현한다.(CRUD 구현하기)
2. '자료 추가'에서는 입력한 회원 ID가 중복되는지 여부를 검사해서 중복되면 다시 입력 받도록 한다.
3. '자료 삭제'는 회원 ID를 입력 받아 삭제한다.
4. '자료 수정'은 회원 ID를 제외한 전체 자료를 수정한다.
5. '자료 수정2' 메뉴를 선택하면
    1. 회원이름 수정     2.회원전화번호 수정      3. 회원주소 수정    4. 취소  메뉴를 출력하고
    각  부 메뉴에 해당하는 데이터를 수정한다.

메뉴예시)
	-- 작업 선택 --
  1. 자료 추가					---> insert (C)
  2. 자료 삭제					---> delete (D)
  3. 자료 수정					---> update (U)
  4. 전체 자료 출력				---> select (R)
  5. 자료 수정2
  0. 작업 끝.
 ----------------- 
 작업 번호 >> 

*/


public class MemberController {
	private Scanner scan = new Scanner(System.in);
	private IMemberService service;   // Service객체가 저장될 변수 선언
	
	// 생성자
	public MemberController() {
		//service = new MemberServiceImpl();
		service = MemberServiceImpl.getInstance();
	}
	
	public static void main(String[] args) {
		new MemberController().memberStart();
	}

	public void memberStart() {
		while(true) {
			int choice = displayMenu();
			switch(choice) {
				case 1 : insertMember();			// 추가
					break;
				case 2 : deleteMember();			// 삭제
					break;
				case 3 : updateMember();			// 수정
					break;
				case 4 : displayMember();			// 전체 출력
					break;
				case 5 : updateMember2();			// 수정2
					break;
				case 0 :
					System.out.println();
					System.out.println("프로그램을 종료합니다.");
					return;
				default :
					System.out.println("잘못 선택했습니다. 다시 입력하세요.");
					System.out.println();
			}
		}
	}
	
	private int displayMenu() {
		System.out.println();
		System.out.println("  -- 작업 선택 --");
		System.out.println(" 1. 자료 추가");
		System.out.println(" 2. 자료 삭제");
		System.out.println(" 3. 자료 수정");
		System.out.println(" 4. 전체 자료 출력");
		System.out.println(" 5. 자료 수정2");
		System.out.println(" 0. 작업 끝.");
		System.out.println("----------------");
		System.out.print(" 작업 선택 >> ");
		
		return scan.nextInt();
	}
	
	// 회원 정보 수정2
	private void updateMember2() {
		System.out.println();
		System.out.println("수정할 회원 정보를 입력하세요.");
		System.out.print("회원ID >> ");
		String memId = scan.next();
		
		int count = service.getMemberCount(memId);
		if(count==0) {  // 없는 회원이면...
			System.out.println(memId + "는(은) 없는 회원ID 입니다.");
			System.out.println("수정 작업을 종료합니다.");
			return;
		}
		
		System.out.println();
		System.out.println("수정할 항목을 선택하세요.");
		System.out.println("1. 회원이름 수정        2. 전화번호 수정       3. 회원주소 수정     4. 취소");
		System.out.println("--------------------------------------------------------");
		System.out.print("수정 항목 선택 >> ");
		int num = scan.nextInt();
		
		String updateField = null;		// 선택한 컬럼명이 저장될 변수
		String updateStr = null;		// 선택한 컬럼의 제목이 저장될 변수
		
		switch(num) {
			case 1 : updateField = "mem_name";
					 updateStr = "회원이름";
				break;
			case 2 : updateField = "mem_tel";
					updateStr = "전화번호";
				break;
			case 3 : updateField = "mem_addr";
					updateStr = "회원주소";
				break;
			default : return;	
		}
		
		// 수정할 데이터 입력 받기
		System.out.println();
		scan.nextLine();   // 입력버퍼 비우기
		System.out.print("새로운 " + updateStr + " >> ");
		String updateData = scan.nextLine();
		
		// 수정할 정보를 Map에 추가한다.
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("memId", memId);
		paramMap.put("field", updateField);
		paramMap.put("data", updateData);
		
		int cnt = service.updateMember2(paramMap);
		
		if(cnt>0) {
			System.out.println("수정 작업 성공!!!!");
		}else {
			System.out.println("수정 작업 실패~~~~");
		}
		
		
	}
	
	
	// 전체 회원 정보를 출력하는 메서드
	private void displayMember() {
		
		List<MemberVO> memList = service.getAllMemberList();
		
		System.out.println();
		System.out.println("--------------------------------------");
		System.out.println(" ID    이름       전화번호         주소");
		System.out.println("--------------------------------------");
		
		if(memList==null || memList.size()==0) {
			System.out.println(" 회원 정보가 하나도 없습니다.");
		}else {
			// List에 저장된 데이터 개수만큼 반복해서 자료를 출력한다.
			for(MemberVO memVo : memList) {
				String memId = memVo.getMem_id();
				String memName = memVo.getMem_name();
				String memTel = memVo.getMem_tel();
				String memAddr = memVo.getMem_addr();
				
				System.out.println(memId + "\t" + memName + 
						"\t" + memTel + "\t" + memAddr);
			}
		}
			
		System.out.println("--------------------------------------");
		
		
	}
	
	
	// 회원 정보를 수정하는 메서드
	private void updateMember() {
		System.out.println();
		System.out.println("수정할 회원 정보를 입력하세요.");
		System.out.print("수정할 회원 ID >> ");
		String memId = scan.next();
		
		int count = service.getMemberCount(memId);
		
		if(count==0) {
			System.out.println(memId + "는 없는 회원 ID 입니다.");
			System.out.println("수정 작업 종료");
			return;
		}
		
		System.out.print("새로운 회원 이름 : ");
		String memName = scan.next();
		
		System.out.print("새로운 전화번호 : ");
		String memTel = scan.next();
		
		scan.nextLine();
		System.out.print("새로운 회원 주소 : ");
		String memAddr = scan.nextLine();
		
		// 입력된 수정할 데이터를 MemeberVO객체를 생성해서 저장한다.
		MemberVO memVo = new MemberVO();
		
		memVo.setMem_id(memId);
		memVo.setMem_name(memName);
		memVo.setMem_tel(memTel);
		memVo.setMem_addr(memAddr);
		
		// Service객체의 데이터를 수정하는 메서드 호출하기
		int cnt = service.updateMember(memVo); 
		
		if(cnt>0) {
			System.out.println("update 작업 성공~~~");
		}else {
			System.out.println("수정 작업 실패!!!");
		}
			
		
	}
	
	
	// 회원 정보를 삭제하는 메서드
	private void deleteMember() {
		
		System.out.println();
		System.out.println("삭제할 회원 정보를 입력하세요.");
		System.out.print("삭제할 회원 ID >> ");
		String memId = scan.next();
		
		int count = service.getMemberCount(memId);
		if(count==0) {
			System.out.println(memId + "는 없는 회원 ID 입니다.");
			System.out.println("삭제 작업 종료");
			return;
		}
		
		int cnt = service.deleteMember(memId);  // Service의 회원 삭제 메서드 호출하기
			
		if(cnt>0) {
			System.out.println("삭제 작업 성공!!!");
		}else {
			System.out.println("삭제 작업 실패~~~");
		}
			
		
		
	}
	
	
	// 회원 정보를 추가하는 메서드
	private void insertMember() {
		System.out.println();
		System.out.println("추가할 회원 정보를 입력하세요.");
		int count = 0;
		
		String memId = null;
		do {
			System.out.print("회원 ID : ");
			memId = scan.next();
			
			count = service.getMemberCount(memId);
			
			if(count>0) {
				System.out.println(memId + "은(는) 이미 등록된 ID입니다.");
				System.out.println("다른 회원 ID를 입력하세요.");
				System.out.println();
			}
			
		}while(count>0);
		
		System.out.print("회원 이름 : ");
		String memName = scan.next();
		
		System.out.print("전화번호 : ");
		String memTel = scan.next();
		
		scan.nextLine();  // 입력 버퍼 비우기
		System.out.print("회원 주소 : ");
		String memAddr = scan.nextLine();
		
		// 입력한 회원 정보를 저장할 MemeberVO객체 생성
		MemberVO memVo = new MemberVO();
		
		// 입력한 데이터를 MemberVO객체에 저장한다.
		memVo.setMem_id(memId);
		memVo.setMem_name(memName);
		memVo.setMem_tel(memTel);
		memVo.setMem_addr(memAddr);
		
		// Service객체에서 회원 정보를 추가하는 메서드 호출하기
		int cnt = service.insertMember(memVo);
			
		if(cnt>0) {
			System.out.println(memId + "회원 정보 추가 성공!!");
		}else {
			System.out.println("추가 작업 실패~~~~");
		}

	}
	
}
