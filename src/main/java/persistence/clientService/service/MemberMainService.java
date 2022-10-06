package persistence.clientService.service;

import java.util.Scanner;

public class MemberMainService { // Control DAO (main.Main Service)
    Scanner s = new Scanner(System.in);
    public MemberMainService(){}


    protected String create(){ // delete parameter, input from keyboard
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

        String[] send = {Integer.toString(id), job, password, major, phone, name, Integer.toString(grade)};
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


}
