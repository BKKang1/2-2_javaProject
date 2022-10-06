package persistence.view;

import main.ConvertTimeLine;
import persistence.dto.SignUpDTO;
import persistence.dao.SignUpDAO;

import java.util.ArrayList;
import java.util.List;

public class SignUpView {
    public SignUpView(){

    }

    public static void viewSingle(String signUp){
        String[] splitData = signUp.split(",");
        String id = splitData[1].split("=")[1];
        String lectureCode = splitData[2].split("=")[1];
        String lecture = splitData[3].split("=")[1];
        String name = splitData[4].split("=")[1];
        String time = splitData[5].split("=")[1].split("\\)")[0];
        ConvertTimeLine convertTimeLine = new ConvertTimeLine(time);
        System.out.print("[학번] " + id);
        System.out.print("[강의코드] " + lectureCode);
        System.out.print(" [강의명] " + lecture);
        System.out.print(" [이름] " + name);
        System.out.println(" [시간] " + convertTimeLine.getTimeLine());
    }

    public static void viewAll(String[] signUps){
        for(String signUp : signUps){
            viewSingle(signUp);
        }
    }

    // 모든정보, 시작인덱스, 한페이지당 출력할 갯수(길이)
    public static String[] getPages(String[] allPage, int startPage, int length){
        String[] pages = new String[length];
        int pageIndex = 0; // pages 전용 index
        int startPoint = startPage*length - length; // 1페이지 = 1*3 - 3 => 0 부터 , 2페이지 = 2*3 -3 => 3부터 ...
        if (startPoint + length > allPage.length){ // 해당 페이지가 마지막 페이지면서 출력할 갯수 미만의 출력물을 가지고 있는 경우
            pages = new String[startPoint + length - allPage.length];
            for (int i = startPoint; i < allPage.length; i++)
                pages[pageIndex++] = allPage[i];
        }
        else{
            for (int i = startPoint; i < startPoint+length; i++){ // x페이지부터 x+length 페이지까지
                pages[pageIndex++] = allPage[i];
            }
        }
        return pages;
    }
}
