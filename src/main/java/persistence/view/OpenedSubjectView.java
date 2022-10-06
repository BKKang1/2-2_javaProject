package persistence.view;

import main.ConvertTimeLine;
import persistence.dao.OpenedSubjectDAO;
import persistence.dto.OpenedSubjectDTO;

import java.util.ArrayList;
import java.util.List;

public class OpenedSubjectView {
    public OpenedSubjectView() {

    }

    public static void viewSingle(String openedSubject){ // openedSubject : OpenedSubjectDTO(lectureCode=3, grade=1......)
        String[] splitData = openedSubject.split(",");
        String lectureCode = splitData[0].split("=")[1];
        String grade = splitData[1].split("=")[1];
        String maxCapacity = splitData[2].split("=")[1];
        String nowCapacity = splitData[3].split("=")[1];
        String prof = splitData[4].split("=")[1];
        String time = splitData[5].split("=")[1];
        String classroom = splitData[6].split("=")[1];
        String isSignupState = splitData[7].split("=")[1];
        String syllabus = splitData[9].split("=")[1].split("\\)")[0];
        if (syllabus.equals("null"))
            syllabus = "내용없음";
        ConvertTimeLine convertTimeLine = new ConvertTimeLine(time);
        System.out.print("[과목코드] " + lectureCode);
        System.out.print(" [학년] " + grade);
        System.out.print(" [최대수강인원수] " + maxCapacity);
        System.out.print(" [현재수강인원수] " + nowCapacity);
        System.out.print(" [담당 교수님] " + prof);
        System.out.print(" [시간] " + convertTimeLine.getTimeLine());
        System.out.print(" [강의실] " + classroom);
        System.out.print(" [수강신청가능기간] " + isSignupState);
        System.out.println(" [강의계획서] " + syllabus);
    }

    public static void viewAll(String[] openedSubjects){
        for(String openedSubject : openedSubjects){
            viewSingle(openedSubject);
        }
    }
}
