package persistence.clientService.service;

import Server.Protocol;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class SignUpService  {
    final int CREATE = 1;
    final int READ = 2;
    final int PAGING = 3;
    final int DELETE = 4;
    final int EXIT = 5;
    private Object[] protocol;
    Scanner s = new Scanner(System.in);

    private String protocolData="";
    public SignUpService(){

    }


    public String create() throws Exception{//수강신청
        System.out.printf("%50sinput lectureCode : ","");
        int lectureCode = s.nextInt();

        return Integer.toString(lectureCode);
    }

    public String read(){ //수강신청 조회
        System.out.printf("%50sinput id : ","");
        int id = s.nextInt();
        return  Integer.toString(id);
    }


    public String delete(){ //수강신청 삭제
        System.out.printf("%50sinput Lecture Code : ","");
        int lectureCode = s.nextInt();
        return Integer.toString(lectureCode);
    }


    public void printMenu(){ // print menu
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s |\n","","Create");
        System.out.printf("%50s| 2. %-23s |\n","","Read");
        System.out.printf("%50s| 3. %-23s |\n","","Read(Paging)");
        System.out.printf("%50s| 4. %-23s |\n","","Delete");
        System.out.printf("%50s| 5. %-23s |\n","","Exit");
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50sInput num : ", "");
    }
    public void serviceProtocol(Object[] protocol){
        // 0 : type, 1 : code, 2~ : some of data
        this.protocol = protocol;
    }

    public String paging(){
        s.nextLine();
        System.out.printf("%50s교수명 또는 과목코드 또는 두가지 모두를 입력하세요 : ","");
        String input = s.nextLine();
        if (input.split(" ").length > 1){ // 두가지 입력했을 경우
            try{
                Integer.parseInt(input.split(" ")[1]);
            }
            catch (Exception e){
                System.out.printf("%50s양식이 맞지 않습니다!","");
                return paging();
            }
            return input.split(" ")[0] + "#" + input.split(" ")[1];
        }
        return input;
    }

    public void selectMenu() throws Exception{ //수강신청메뉴
        int num = 0;
        printMenu();
        num = s.nextInt();

        switch(num){
            case CREATE ://수강신청 요청
                protocolData=create();
                serviceProtocol(new Object[]{Protocol.PT_SIGN_UP_OR_CANCEL_REQUEST, 0x01, protocolData});
                break;
            case READ :
                //로그인 되어있으니 서버에서 알아서 ID를 가져옴
                serviceProtocol(new Object[]{Protocol.PT_MY_SCHEDULE_LOOKUP_REQUEST, 0x00});
                break;
            case PAGING://수강신청 페이징 조회 요청
                String prof = paging();
                serviceProtocol(new Object[]{Protocol.PT_SIGN_UP_STUDENT_LIST_REQUEST, 0x00, prof});
                break;
            case DELETE ://수강신청 삭제 요청
                protocolData=delete();
                serviceProtocol(new Object[]{Protocol.PT_SIGN_UP_OR_CANCEL_REQUEST, 0x02, protocolData});;
                break;
            case EXIT :
                serviceProtocol(null);
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