package persistence.service;

import persistence.view.View;
import persistence.dto.MemberDTO;

import java.util.ArrayList;
import java.util.Scanner;

public class MemberSubService extends MemberMainService { // Sub Service

    Scanner s = new Scanner(System.in);

    public MemberSubService(){
        super();
    }

    // Get Member (Select)
    public ArrayList<MemberDTO> getMembersByJob(String job){return read(job, "job");}
    public ArrayList<MemberDTO> getMembersByMajor(String major){return read(major, "major");}
    public ArrayList<MemberDTO> getMembersByName(String name){return read(name, "name");}
    public ArrayList<MemberDTO> getMembersByGrade(int grade){
        return read(grade, "grade");
    }
    public ArrayList<MemberDTO> getAllMembers(){ return read();}
    public MemberDTO getMemberById(int id){return read(id, "id").get(0);}

    // set Member (Update)
    public boolean setMember(MemberDTO member){
        return update(member);
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


}
