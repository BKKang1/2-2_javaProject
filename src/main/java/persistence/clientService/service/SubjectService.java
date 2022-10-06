package persistence.clientService.service;

import Server.Protocol;
import persistence.MyBatisConnectionFactory;
import persistence.dao.SubjectDAO;
import persistence.dto.SubjectDTO;

import java.util.List;
import java.util.Scanner;

public class SubjectService {
    final int CREATE = 1;
    final int UPDATE = 2;
    final int DELETE = 3;
    final int EXIT = 4;

    Scanner s = new Scanner(System.in);

    SubjectDAO subDAO;

    private int protocolType = -3;
    private String protocolData = "";
    private Object[] protocol;

    public SubjectService(){
        subDAO = new SubjectDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    }

    public String create(){ // 교과목 생성
        System.out.printf("%50sinput lectureCode : ","");
        int lectureCode = s.nextInt();
        s.nextLine();
        System.out.printf("%50sinput lecture : ","");
        String lecture = s.nextLine();
        System.out.printf("%50sinput grade : ","");
        int grade = s.nextInt();
        System.out.printf("%50sinput semester : ","");
        int semester = s.nextInt();

        String[] send = {Integer.toString(lectureCode), lecture, Integer.toString(grade), Integer.toString(semester)};
        return makeSendData(send);
    }

    public String update(){ // 교과목 수정
        System.out.printf("%50sinput lectureCode : ","");
        int lectureCode = s.nextInt();
        s.nextLine();
        System.out.printf("%50sinput lecture : ","");
        String lecture = s.nextLine();

        String[] send = {Integer.toString(lectureCode), lecture};
        return makeSendData(send);
    }

    public String delete(){ // 교과목 삭제
        System.out.printf("%50sinput lectureCode : ","");
        int lectureCode = s.nextInt();
        String[] send = {Integer.toString(lectureCode)};
        return makeSendData(send);
    }

    public void printMenu(){
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s |\n","","Create");
        System.out.printf("%50s| 2. %-23s |\n","","Update");
        System.out.printf("%50s| 3. %-23s |\n","","Delete");
        System.out.printf("%50s| 4. %-23s |\n","","Exit");
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50sInput num : ", "");
    }

    public void selectMenu(){
        int num = 0;
        printMenu();
        num = s.nextInt();
        switch(num){
            case CREATE : // 교과목 생성
                protocolData = create();
                serviceProtocol(new Object[]{Protocol.PT_SUBJECT_REQUEST, 0x01, protocolData});
                break;
            case UPDATE : // 교과목 수정
                protocolData = update();
                serviceProtocol(new Object[]{Protocol.PT_SUBJECT_REQUEST, 0x02, protocolData});
                break;
            case DELETE : // 교과목 삭제
                protocolData = delete();
                serviceProtocol(new Object[]{Protocol.PT_SUBJECT_REQUEST, 0x03, protocolData});
                break;
            case EXIT : // 종료
                serviceProtocol(null);
                return;
            default :
                System.out.printf("%50sWRONG NUMBER!..\n","");
                selectMenu();
                break;
        }
    }

    public void serviceProtocol(Object[] protocol){
        // index -> 0 : type, 1 : code, 2 ~ : data
        this.protocol = protocol;
    }

    public Object[] getProtocol(){ return protocol; }

    String makeSendData(String[] send){
        String data = "";
        for (int i = 0; i < send.length-1; i++){
            data += send[i] + "#";
        }
        data += send[send.length-1];
        return data;
    }
}