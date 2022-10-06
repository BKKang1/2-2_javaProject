package main;

import Server.Protocol;
import persistence.clientService.service.*;
import persistence.dto.MemberDTO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class ProfController {
    final int INFORMATION_REVISE = 1; // 개인정보 및 비밀번호 수정
    final int OPENED_SUBJECT_LOOKUP = 2; // 개설 교과목 조회
    final int SYLLABUS_INPUT_REVISE = 3; // 강의계획서 입력, 수정
    final int SUBJECT_SYLLABUS_LOOKUP = 4; // 개설 교과목 강의 계획서 조회
    final int SIGN_UP_STUDENT_LIST = 5; // 교과목 수강신청 학생 목록 조회
    final int SUBJECT_SCHEDULE_LOOKUP = 6; // 교과목 시간표 조회
    final int QUIT = 7;
    private Object[] protocol;

    final int CHANGE_PHONE = 1;
    final int CHANGE_PWD = 2;
    final int LOOK_FOR_ALL = 1;
    final int LOOK_FOR_GRADE = 2;
    final int LOOK_FOR_PROF = 3;
    final int LOOK_FOR_GRADE_PROF = 4;

    Scanner s = new Scanner(System.in);

    MemberMainService memberMainService;
    MemberSubService memberSubService;
    SubjectService subjectService;
    OpenedSubjectService openedSubjectService;
    SignUpService signUpService;
    SignUpTermService signUpTermService;
    SyllabusTermService syllabusTermService;
    InputStream is;
    OutputStream os;

    public ProfController(){
        memberMainService = new MemberMainService();
        memberSubService = new MemberSubService();
        subjectService = new SubjectService();
        openedSubjectService = new OpenedSubjectService();
        signUpService = new SignUpService();
        signUpTermService = new SignUpTermService();
        syllabusTermService = new SyllabusTermService();
    }

    public void printServiceMenu(){ // print service menu
        System.out.printf("%50s%30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s \n","","개인정보 및 비밀번호 수정");
        System.out.printf("%50s| 2. %-23s \n","","개설 교과목 목록 조회");
        System.out.printf("%50s| 3. %-23s \n","","강의 계획서 입력/수정");
        System.out.printf("%50s| 4. %-23s \n","","개설 교과목 강의 계획서 조회");
        System.out.printf("%50s| 5. %-23s \n","","교과목 수강 신청 학생 목록 조회");
        System.out.printf("%50s| 6. %-23s \n","","교과목 시간표 조회");
        System.out.printf("%50s| 7. %-23s \n","","Quit");
        System.out.printf("%50s%30s\n","","+============================+");
        System.out.printf("%50sInput num : ","");
    }
    public int[] selectService(int num) throws IOException, Exception { // select service by input number
        int[] ans = new int[2]; // 0 : num 정보, 1 : 프로토콜 정보(없으면 값이 0, 있으면 1)
        switch(num){
            case INFORMATION_REVISE : // 개인정보 및 비밀번호 변경
                System.out.printf("%50s1. 전화번호 변경 \n", "");
                System.out.printf("%50s2. 비밀번호 변경 \n", "");
                System.out.printf("%50s입력 : ", "");
                int selectNum = s.nextInt();
                infoUpdate(selectNum);
                ans[1] = 1;
                break;

            case OPENED_SUBJECT_LOOKUP : // 개설 교과목 목록 조회
                System.out.printf("%50s1. 전체 조회\n", "");
                System.out.printf("%50s2. 학년별 검색\n", "");
                System.out.printf("%50s3. 교수별 검색\n", "");
                System.out.printf("%50s4. 학년, 교수별 검색\n", "");
                System.out.printf("%50s5. EXIT\n", "");
                System.out.printf("%50s입력 : ", "");
                int select = s.nextInt();
                if (select == 5)
                    return ans;
                lookUpOpenedSubject(select);
                ans[0] = OPENED_SUBJECT_LOOKUP;
                ans[1] = 1;
                break;

            case SYLLABUS_INPUT_REVISE : // 강의계획서 입력 및 수정
                String data = updateSyllabus();
                serviceProtocol(new Object[]{Protocol.PT_SYLLABUS_INPUT_REVISE_REQUEST, 0x01, data});
                ans[1] = 1;
                break;

            case SUBJECT_SYLLABUS_LOOKUP : // 선택 강의계획서 조회
                System.out.printf("%50s과목 코드를 입력하세요 : ", "");
                int input = s.nextInt();
                serviceProtocol(new Object[]{Protocol.PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_REQUEST, 0x01, input});
                ans[1] = 1;
                break;

            case SIGN_UP_STUDENT_LIST : // 수강신청 학생 목록 조회
                String prof = paging();
                serviceProtocol(new Object[]{Protocol.PT_SIGN_UP_STUDENT_LIST_REQUEST, 0x00, prof});
                ans[1] = 1;
                break;

            case SUBJECT_SCHEDULE_LOOKUP: // 교수 자신 시간표 조회
                serviceProtocol(new Object[]{Protocol.PT_SUBJECT_SCHEDULE_LOOKUP_REQUEST, 0x00});
                ans[1] = 1;
                break;

            case QUIT :
                System.out.printf("%50sQUIT!  ", "");
                break;
            //return "QUIT";
            default :
                System.out.printf("%50sWRONG NUMBER!..\n","");
                break;
        }
        return ans;
    }
    public void infoUpdate(int num){ // 개인정보 및 비밀번호 수정 메소드
        switch(num){
            case CHANGE_PHONE: // 핸드폰 번호 변경
                s.nextLine();
                System.out.printf("%50s변경할 휴대폰 번호 : ", "");
                String profPhone = s.nextLine(); // 변경할 휴대폰 번호 입력
                // 개인 정보 수정 요청(교수), 0x01, 교수번호, 휴대폰번호
                serviceProtocol(new Object[]{Protocol.PT_PROF_INFORMATION_REVISE_REQUEST, 0x01,profPhone});
                break;

            case CHANGE_PWD: // 비밀번호 변경
                s.nextLine();
                System.out.printf("%50s변경할 비밀번호 : ", "");
                String profPwd = s.nextLine(); // 변경할 휴대폰 번호 입력
                // 개인 정보 수정 요청(교수), 0x01, 교수번호, 휴대폰번호
                serviceProtocol(new Object[]{Protocol.PT_PROF_INFORMATION_REVISE_REQUEST, 0x02,profPwd});
                break;
        }
    }
    public void lookUpOpenedSubject(int num){ // 개설 교과목 목록 조회
        switch (num){
            case LOOK_FOR_ALL: // 전체조회
                serviceProtocol(new Object[]{Protocol.PT_ALL_OPENED_SUBJECT_LIST_LOOKUP_REQUEST, 0x00});
                break;

            case LOOK_FOR_GRADE: //학년별 조회
                System.out.printf("%50s검색할 학년을 입력하세요 : ", "");
                int grade = s.nextInt();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_REQUEST, 0x01, Integer.toString(grade)});
                break;

            case LOOK_FOR_PROF: //교수별 조회
                System.out.printf("%50s교수 이름을 입력하세요 : ", "");
                s.nextLine();
                String prof = s.nextLine();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_REQUEST, 0x02, prof});
                break;

            case LOOK_FOR_GRADE_PROF: //학년 + 교수별 조회
                System.out.printf("%50s검색할 학년을 입력하세요 : ", "");
                int _grade = s.nextInt();
                s.nextLine();
                System.out.printf("%50s교수 이름을 입력하세요 : ", "");
                String _prof = s.nextLine();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_REQUEST, 0x03, Integer.toString(_grade), _prof});
                break;
        }
    }

    public Object[] getProtocol(){
        return protocol;
    }

    public void setProtocol(Object[] protocol){
        this.protocol = protocol;
    }

    public void serviceProtocol(Object[] protocol){
        // 0 : type, 1 : code, 2~ : some of data
        this.protocol = protocol;
    }
    public String updateSyllabus(){ // 강의계획서 입력 및 수정
        System.out.printf("%50s과목코드를 입력하세요 : ", "");
        int lectureCode = s.nextInt();
        s.nextLine();
        System.out.printf("%50s강의 계획서 내용을 입력하세요 : ", "");
        String classRoom = s.nextLine();
        String[] send = new String[]{Integer.toString(lectureCode), classRoom};
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

    public String paging(){ // 수강신청 학생 목록 조회 (페이징)
        System.out.printf("%50s교수명 또는 과목코드 또는 두가지 모두를 입력하세요 : ","");
        String input = s.nextLine();
        if (input.split(" ").length > 1){ // 두가지 입력했을 경우
            try{
                Integer.parseInt(input.split(" ")[1]);
            }
            catch (Exception e){
                System.out.printf("%50s양식이 맞지 않습니다!\n","");
                return paging();
            }
            return input.split(" ")[0] + "#" + input.split(" ")[1];
        }
        return input;
    }
}
