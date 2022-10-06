package persistence.service;

import persistence.dto.*;
import persistence.dao.MemberDAO;

import java.util.Scanner;
import java.util.ArrayList;

public class MemberMainService extends MemberDAO { // Control DAO (main.Main Service)
    Scanner s = new Scanner(System.in);

    protected void create(){ // delete parameter, input from keyboard
        // Create and Save in DB
        System.out.printf("%49s input id : ", "");
        int id = s.nextInt();
        System.out.printf("%49s input password : ","");
        s.nextLine();
        String password = s.nextLine();
        System.out.printf("%49s input name : ","");
        String name = s.nextLine();
        System.out.printf("%49s input job : ","");
        String job = s.nextLine();
        System.out.printf("%49s input major : ","");
        String major = s.nextLine();
        System.out.printf("%49s input phone : ","");
        String phone = s.nextLine();
        System.out.printf("%49s input grade : ","");
        int grade = s.nextInt();



        MemberDTO member = new MemberDTO(id, job, password, major, phone, name, grade);
        // 프로토콜로 보낼 데이터
        String[] send = {Integer.toString(id), job, password, major, phone, name, Integer.toString(grade)};
        String sendData = makeSendData(send);
        insertMember(member); // insert
    }

    private String makeSendData(String[] send){ // 구분자 생성
        String data = "";
        for (int i = 0; i < send.length; i++){
            data += send + "#";
        }
        return data;
    }

    public void create(int id, String job, String password, String major, String phone, String name, int grade){
        MemberDTO member = new MemberDTO(id, job, password, major, phone, name, grade);
        insertMember(member); // insert
    }


    protected ArrayList<MemberDTO> read (Object obj, String type){ // Find Members using any object
        return selectAccordObjects(obj, type); // select
    }

    protected ArrayList<MemberDTO> read (){ // Find All Members without conditions
        return selectAllObjects(); // select
    }

    protected boolean update(MemberDTO member){ // Update Member
        return updateMember(member); // update
    }


}
