package persistence.view;

import persistence.dto.MemberDTO;

import java.util.ArrayList;

public class View { // View Phase
    public View(){
    }

    public static void viewSingle(String member){ // Showing only one object With using String (in Client)
        String[] splitData = member.split(",");
        String id = splitData[0].split("=")[1];
        String job = splitData[1].split("=")[1];
        String major = splitData[3].split("=")[1];
        String phone = splitData[4].split("=")[1];
        String name = splitData[5].split("=")[1];
        String grade = splitData[6].split("=")[1].split("\\)")[0];
        System.out.print("[학번] " + id);
        System.out.print(" [분류] " + job);
        System.out.print(" [전공] " + major);
        System.out.print(" [휴대폰번호] " + phone);
        System.out.print(" [이름] " + name);
        if (Integer.parseInt(grade) != 0)
            System.out.print(" [학년] " + grade);
        System.out.println();
    }

    public static void viewAll(String[] members){ // Showing at least two object With using String (in Client)
        for (String member : members){
            viewSingle(member);
        }
    }

    public static void viewSingle(MemberDTO member){ // Showing only one object With using DTO (not using network)
        System.out.print("[학번] " + member.getId());
        System.out.print(" [분류] " + member.getJob());
        System.out.print(" [전공] " + member.getMajor());
        System.out.print(" [휴대폰번호] " + member.getPhone());
        System.out.print(" [이름] " + member.getName());
        if (member.getGrade() != 0)
            System.out.print(" [학년] " + member.getGrade());
        System.out.println();
    }

    public static void viewAll(ArrayList<MemberDTO> members){ // Showing at least two object With using DTO (not using network)
        for (MemberDTO member : members){
            viewSingle(member);
        }
    }

}
