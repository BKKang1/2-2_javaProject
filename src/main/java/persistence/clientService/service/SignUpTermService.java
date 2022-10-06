package persistence.clientService.service;

import Server.Protocol;

import java.util.Scanner;

public class SignUpTermService {
    final int SET_TERM = 1;
    final int EXIT = 2;
    private String protocolData="";
    private Object[] protocol;
    Scanner s = new Scanner(System.in);

    public SignUpTermService(){

    }
    public String setSignUpTerm(){ //학년별 수강신청 기간 인서트 해주는 메소드
        System.out.printf("%50s수강 신청 날짜 설정할 학년을 입력 (1) or (2) ... : ","");
        int grade = s.nextInt();
        if (grade < 1 || grade > 4){// 1~4 학년 이외의 값을 입력하면
            System.out.printf("%50s올바른 값을 입력하십시오!\n", "");
            return setSignUpTerm();
        }

        s.nextLine();
        System.out.printf("%50s수강 신청 시작 시간 입력 (yyyy-mm-dd) : ","");
        String strStartDate = s.nextLine();


        System.out.printf("%50s수강 신청 끝나는 시간 입력 (yyyy-mm-dd) : ","");
        String strEndDate = s.nextLine();

        String[] send = {Integer.toString(grade),strStartDate,strEndDate};
        return makeSendData(send);

    }

    String makeSendData(String[] send){ // 구분자 생성
        String data = "";
        for (int i = 0; i < send.length-1; i++){
            data += send[i] + "#";
        }
        data += send[send.length-1];
        return data;
    }

    public void printMenu(){ // print menu
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s |\n","","Set Term");
        System.out.printf("%50s| 2. %-23s |\n","","Exit");
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50sInput num : ", "");
    }
    public void serviceProtocol(Object[] protocol){
        // 0 : type, 1 : code, 2~ : some of data
        this.protocol = protocol;
    }

    public void selectMenu(){//수강신청 기간 메뉴
        int num = 0;
        printMenu();
        num = s.nextInt();
        switch(num){
            case SET_TERM ://수강 신청 기간 설정 요청
                protocolData = setSignUpTerm();
                serviceProtocol(new Object[]{Protocol.PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_REQUEST, 0x00, protocolData});
                break;
            case EXIT :
                return;
            default :
                System.out.printf("%50sWRONG NUMBER!..\n","");
                selectMenu();
                break;
        }
    }
    public Object[] getProtocol(){
        return protocol;
    }
}
