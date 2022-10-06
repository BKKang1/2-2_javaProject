package persistence.clientService.service;

import Server.Protocol;

import java.util.ArrayList;
import java.util.Scanner;

public class MemberSubService extends MemberMainService { // Sub Service
    final int CREATE = 1;
    final int READ_ALL_PROFESSOR = 2;
    final int READ_ALL_STUDENT = 3;
    final int UPDATE_PROFESSOR_PHONE = 4;
    final int UPDATE_STUDENT_NAME = 5;
    final int EXIT = 6;

    private int protocolType = -3;
    private String protocolData = "";
    private Object[] protocol;

    Scanner s = new Scanner(System.in);

    public MemberSubService(){
        super();
    }
    // make Member (Insert)
    public String makeMember(){
        return create(); // original create method
        //createSpec();
    }



    public void printMenu(){ // 메뉴 출력
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s |\n","","Create");
        System.out.printf("%50s| 2. %-23s |\n","","Read All Professor");
        System.out.printf("%50s| 3. %-23s |\n","","Read All Student");
        System.out.printf("%50s| 4. %-23s |\n","","Update Professor Phone");
        System.out.printf("%50s| 5. %-23s |\n","","Update Student Name");
        System.out.printf("%50s| 6. %-23s |\n","","Exit");
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50sInput num : ","");
    }


    public void selectMenu(){
        int num = 0;
        String menuResult;
        String[] data;
        printMenu();
        num = s.nextInt();
        switch(num){
            case CREATE :
                protocolData = makeMember();
                // 교수, 학생 정보 생성 요청, 0x00, 정보 생성에 관한 데이터
                serviceProtocol(new Object[]{Protocol.PT_ACCOUNT_CREATE_REQUEST, 0x00, protocolData});
                break;
            case READ_ALL_PROFESSOR :
                // 정보 조회 요청, 0x01(교수)
                serviceProtocol(new Object[]{Protocol.PT_INFORMATION_LOOKUP_REQUEST, 0x01});
                break;
            case READ_ALL_STUDENT:
                // 정보 조회 요청, 0x02(학생)
                serviceProtocol(new Object[]{Protocol.PT_INFORMATION_LOOKUP_REQUEST, 0x02});
                break;
            case UPDATE_PROFESSOR_PHONE:
                System.out.printf("%50s교수번호를 입력하세요 : ", ""); // 로그인 된 상태면 바꿀 예정
                int profNum = s.nextInt(); // 교수번호 입력
                s.nextLine();
                System.out.printf("%50s변경할 휴대폰 번호 : ", "");
                String profPhone = s.nextLine(); // 변경할 휴대폰 번호 입력
                // 개인 정보 수정 요청(교수), 0x01, 교수번호, 휴대폰번호
                serviceProtocol(new Object[]{Protocol.PT_PROF_INFORMATION_REVISE_REQUEST, 0x01, Integer.toString(profNum), profPhone});
                break;
            case UPDATE_STUDENT_NAME:
                System.out.printf("%50s학번을 입력하세요 : ", ""); // 로그인 된 상태면 바꿀 예정
                int studentNum = s.nextInt(); // 학번 입력
                s.nextLine();
                System.out.printf("%50s변경할 이름 : ", "");
                String studentName = s.nextLine(); // 변경할 이름 입력
                // 개인 정보 수정 요청(학생), 0x01, 학번, 변경할 이름
                serviceProtocol(new Object[]{Protocol.PT_STUDENT_INFORMATION_REVISE_REQUEST, 0x01, Integer.toString(studentNum), studentName});
                break;
            case EXIT :
                serviceProtocol(null);
                break;
            default :
                System.out.printf("%50sWRONG NUMBER!..\n","");
                selectMenu();
                break;
        }
    }

    public void serviceProtocol(Object[] protocol){
        // 0 : type, 1 : code, 2~ : some of data
        this.protocol = protocol;
    }

    public Object[] getProtocol(){
        return protocol;
    }
}
