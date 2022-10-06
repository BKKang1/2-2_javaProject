package main;

import Server.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void printWELCOME() {
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s%-11s%8s%11s\n","","|","WELCOME!","|");
        System.out.printf("%50s%-30s\n","","+============================+");
    }

    public static void main(String args[]) throws IOException, Exception {
        final int QUIT = 8;

        printWELCOME();

        Scanner s = new Scanner(System.in);

        if(args.length < 2) System.out.println("사용법 : " + "주소 포트번호");

        Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();

        int num = 0;
        int[] ans = new int[2];
        ans[0] = num; // ans[0] : selectService에서 시작하는 값, ans[1] : 보내야할 프로토콜이 존재하는 경우(0:없음, 1:있음)

        // 첫 로그인 시
        MakingProtocol makingProtocol = new MakingProtocol(is, os, null);
        makingProtocol.response();

        if (makingProtocol.getLoginStatus() == MakingProtocol.ADMIN){ // Admin Login
            while (num != QUIT) {
                AdminController con = new AdminController();
                if (ans[0] != 0){ // 기존에 프로토콜 호출로 인해 재출력된 경우
                    ans = con.selectAdminService(ans[0]);
                }
                else{ // EXIT 또는 첫 시작인 경우
                    con.printAdminMenu();
                    num = s.nextInt();
                    ans = con.selectAdminService(num);
                }
                if (ans[1] != 0) { // 보내야할 프로토콜이 있는 경우
                    makingProtocol = new MakingProtocol(is, os, con.getProtocol());
                    // 여기서부터 통신 시작임
                    makingProtocol.send(); // REQUEST
                    makingProtocol.response();//RESPONSE
                }
            }
        }
        else if (makingProtocol.getLoginStatus() == MakingProtocol.PROFESSOR){ // Professor Login
            while (num != 7) {
                ProfController con = new ProfController();
                if (ans[0] != 0){ // 기존에 프로토콜 호출로 인해 재출력된 경우
                    ans = con.selectService(ans[0]);
                }

                else{ // EXIT 또는 첫 시작인 경우
                    con.printServiceMenu();
                    num = s.nextInt();
                    ans = con.selectService(num);
                }
                if (ans[1] != 0) { // 보내야할 프로토콜이 있는 경우
                    makingProtocol = new MakingProtocol(is, os, con.getProtocol());
                    // 여기서부터 통신 시작임
                    makingProtocol.send(); // REQUEST
                    makingProtocol.response();//RESPONSE
                }
            }
        }
        else if (makingProtocol.getLoginStatus() == MakingProtocol.STUDENT){ // Student Login
            while (num != QUIT) {
                StudentController con = new StudentController();
                con.select();
                if (con.getProtocol() != null) { // 보내야할 프로토콜이 있는 경우
                    makingProtocol = new MakingProtocol(is, os, con.getProtocol());
                    // 여기서부터 통신 시작임
                    makingProtocol.send(); // REQUEST
                    makingProtocol.response();//RESPONSE
                    con.setProtocol(null);
                }
                if (con.isConOff())
                    break;
            }
        }
        else if (makingProtocol.getLoginStatus() == MakingProtocol.UNDEFINED)
            System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
        makingProtocol = new MakingProtocol(is, os, new Object[]{Protocol.PT_EXIT, 0});
        makingProtocol.send();
    }


}
