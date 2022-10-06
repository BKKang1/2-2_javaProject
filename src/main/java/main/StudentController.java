package main;

import Server.Protocol;
import persistence.clientService.service.*;

import java.util.*;

public class StudentController {
    final int UPDATE_NAME = 1;
    final int UPDATE_PASSWORD = 2;
    final int CREATE_SIGN_UP = 3;
    final int DELETE_SIGN_UP = 4;
    final int READ_ALL_OPENED_SUBJECT = 5;
    final int READ_SELECT_LECTURE_SYLLABUS = 6;
    final int READ_MY_SCHEDULE = 7;
    final int EXIT = 8;
    private Object[] protocol = null;

    Scanner s = new Scanner(System.in);

    MemberMainService memberMainService;
    MemberSubService memberSubService;
    SubjectService subjectService;
    OpenedSubjectService openedSubjectService;
    SignUpService signUpService;
    SignUpTermService signUpTermService;
    SyllabusTermService syllabusTermService;
    private boolean conOff = false;

    private String protocolData="";

    public StudentController(){ // constructor
        memberMainService = new MemberMainService();
        memberSubService = new MemberSubService();
        subjectService = new SubjectService();
        openedSubjectService = new OpenedSubjectService();
        signUpService = new SignUpService();
        signUpTermService = new SignUpTermService();
        syllabusTermService = new SyllabusTermService();
    }

    public void printStudentMenu(){
        System.out.printf("%50s%30s\n","","+============================+");
        System.out.printf("%50s| 1. %-20s \n","","개인정보 수정");
        System.out.printf("%50s| 2. %-20s \n","","비밀번호 수정");
        System.out.printf("%50s| 3. %-20s \n","","수강 신청");
        System.out.printf("%50s| 4. %-20s \n","","수강 취소");
        System.out.printf("%50s| 5. 개설교과목 목록(전학년) 조회\n","");
        System.out.printf("%50s| 6. %-15s \n","","선택 교과목 강의계획서 조회");
        System.out.printf("%50s| 7. %-18s \n","","본인 시간표 조회");
        System.out.printf("%50s| 8. %-23s \n","","EXIT");
        System.out.printf("%50s%30s\n","","+============================+");
        System.out.printf("%50sInput num : ","");
    }

    public void select() throws Exception {
        int num = 0;
        printStudentMenu();
        num = s.nextInt();
        switch(num){
            case UPDATE_NAME : // 학생 이름 변경
                System.out.printf("%50s변경할 이름 : ", "");
                s.nextLine();
                String studentName = s.nextLine(); // 변경할 이름 입력
                serviceProtocol(new Object[]{Protocol.PT_STUDENT_INFORMATION_REVISE_REQUEST, 0x01, studentName});
                break;
            case UPDATE_PASSWORD: // 학생 비밀번호 변경
                System.out.printf("%50s변경할 비밀번호 : ", "");
                s.nextLine();
                String studentPassword = s.nextLine();
                serviceProtocol(new Object[]{Protocol.PT_STUDENT_INFORMATION_REVISE_REQUEST, 0x02, studentPassword});
                break;
            case CREATE_SIGN_UP: // 학생 수강 신청
                protocolData=signUpService.create();
                serviceProtocol(new Object[]{Protocol.PT_SIGN_UP_OR_CANCEL_REQUEST, 0x01, protocolData});
                break;
            case DELETE_SIGN_UP: // 학생 수강 취소
                protocolData=signUpService.delete();
                serviceProtocol(new Object[]{Protocol.PT_SIGN_UP_OR_CANCEL_REQUEST, 0x02, protocolData});;
                break;
            case READ_ALL_OPENED_SUBJECT: // 개설 교과목(전학년) 조회
                serviceProtocol(new Object[]{Protocol.PT_ALL_OPENED_SUBJECT_LIST_LOOKUP_REQUEST, 0x00});
                break;
            case READ_SELECT_LECTURE_SYLLABUS: // 선택 개설 교과목 강의 계획서 조회
                System.out.printf("%50s과목 코드를 입력하세요 : ", "");
                int input = s.nextInt();
                serviceProtocol(new Object[]{Protocol.PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_REQUEST, 0x01, input});
                break;
            case READ_MY_SCHEDULE: // 본인 시간표 조회
                serviceProtocol(new Object[]{Protocol.PT_MY_SCHEDULE_LOOKUP_REQUEST, 0x00});
                break;
            case EXIT: // 종료
                conOff = true;
                return;
            default:
                System.out.println("올바른 숫자를 입력하세요.");
                break;
        }
    }

    public boolean isConOff(){
        return conOff;
    } // 연결 종료라면 true return

    public void serviceProtocol(Object[] protocol){
        // 0 : type, 1 : code, 2~ : some of data
        this.protocol = protocol;
    }

    public Object[] getProtocol(){
        return protocol;
    }

    public void setProtocol(Object[] protocol){
        this.protocol = protocol;
    }
}
