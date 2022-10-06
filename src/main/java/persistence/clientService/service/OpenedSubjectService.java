package persistence.clientService.service;

import Server.Protocol;

import java.io.IOException;
import java.util.Scanner;


public class OpenedSubjectService {

    final int CREATE = 1;
    final int READ_ALL = 2;
    final int READ_BY_PROF = 3;
    final int READ_BY_GRADE = 4;
    final int READ_BY_GRADE_PROF = 5;
    final int UPDATE_CLASSROOM = 6;
    final int UPDATE_CAPACITY = 7;
    final int DELETE_SUBJECT = 8;
    final int UPDATE_SYLLABUS = 9;
    final int SELECT_SYLLABUS = 10;
    final int EXIT = 11;

    private Object[] protocol;
    private String protocolData = "";

    Scanner s = new Scanner(System.in);

    public OpenedSubjectService(){
    }

    public String create() { // 개설교과목 테이블 생성
        int grade = 0;
        int semester = 0;
        System.out.print("input lectureCode: ");
        int lectureCode = s.nextInt();
        System.out.print("input maxCapacity : ");
        int maxCapacity = s.nextInt();
        System.out.print("input nowCapacity : ");
        int nowCapacity = s.nextInt();
        s.nextLine();
        System.out.print("input prof : ");
        String prof = s.nextLine();
        System.out.print("input classroom : ");
        String classroom = s.nextLine();
        System.out.print("input time : ");
        String time = s.nextLine();
        System.out.print("input syllabus : ");
        String syllabus = s.nextLine();

        String[] sendData = {Integer.toString(lectureCode), prof, Integer.toString(grade), Integer.toString(maxCapacity), Integer.toString(nowCapacity), classroom, time, syllabus};
        return makeSendData(sendData);

    }

    String makeSendData(String[] send){ // 구분자 생성
        String data = "";
        for (int i = 0; i < send.length-1; i++){
            data += send[i] + "#";
        }
        data += send[send.length-1];
        return data;
    }


    public void printMenu(){
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s |\n","","Create");
        System.out.printf("%50s| 2. %-23s |\n","","Read All");
        System.out.printf("%50s| 3. %-23s |\n","","Read By Prof");
        System.out.printf("%50s| 4. %-23s |\n","","Read By Grade");
        System.out.printf("%50s| 5. %-23s |\n","","Read By Grade And Prof");
        System.out.printf("%50s| 6. %-23s |\n","","Update Classroom");
        System.out.printf("%50s| 7. %-23s |\n","","Update Capacity");
        System.out.printf("%50s| 8. %-23s |\n","","DELETE Subject");
        System.out.printf("%50s| 9. %-23s |\n","","Input/Update Syllabus");
        System.out.printf("%50s| 10. %-23s |\n","","Find Syllabus");
        System.out.printf("%50s| 11. %-23s |\n","","Exit");
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50sInput num : ", "");
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

    public String makeMember(){
        return create();
    }

    public void selectMenu() throws IOException {
        int num = 0;
        printMenu();
        num = s.nextInt();

        switch(num){
            case CREATE : // 개설교과목 테이블 생성
                protocolData = makeMember();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_CRD_REQUEST, 0x01, protocolData});
                break;

            case READ_ALL : // 개설교과목 목록 전체 조회
                serviceProtocol(new Object[]{Protocol.PT_ALL_OPENED_SUBJECT_LIST_LOOKUP_REQUEST, 0x00});
                break;

            case READ_BY_PROF: // 개설교과목 교수별 조회
                System.out.printf("%50s교수 이름을 입력하세요 : ", "");
                s.nextLine();
                String prof = s.nextLine();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_REQUEST, 0x02, prof});
                break;

            case READ_BY_GRADE: // 개설교과목 학년별 조회
                System.out.printf("%50s검색할 학년을 입력하세요 : ", "");
                int grade = s.nextInt();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_REQUEST, 0x01, Integer.toString(grade)});
                break;

            case READ_BY_GRADE_PROF: // 개설교과목 학년 + 교수별 조회
                System.out.printf("%50s검색할 학년을 입력하세요 : ", "");
                int _grade = s.nextInt();
                s.nextLine();
                System.out.printf("%50s교수 이름을 입력하세요 : ", "");
                String _prof = s.nextLine();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_REQUEST, 0x03, Integer.toString(_grade), _prof});
                break;

            case UPDATE_CLASSROOM: // 강의실 변경
                System.out.printf("%50s과목코드를 입력하세요 : ", "");
                int lectureCode = s.nextInt();
                s.nextLine();
                System.out.printf("%50s변경할 강의실을 입력하세요 : ", "");
                String classRoom = s.nextLine();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_CRD_REQUEST, 0x02, Integer.toString(lectureCode), classRoom});
                break;

            case UPDATE_CAPACITY: // 최대 수강인원수 변경
                System.out.printf("%50s과목코드를 입력하세요 : ", "");
                int _lectureCode = s.nextInt();
                System.out.printf("%50s변경할 인원 수를 입력하세요 : ", "");
                int capacity = s.nextInt();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_CRD_REQUEST, 0x02, Integer.toString(_lectureCode),Integer.toString(capacity)});
                break;

            case DELETE_SUBJECT : // 개설교과목 삭제
                System.out.printf("%50s삭제할 과목 코드를 입력하세요 : ", "");
                int delete_lectureCode = s.nextInt();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_CRD_REQUEST, 0x03, Integer.toString(delete_lectureCode)});
                break;

            case UPDATE_SYLLABUS: // 강의계획서 입력 및 수정
                String data = updateSyllabus();
                serviceProtocol(new Object[]{Protocol.PT_SYLLABUS_INPUT_REVISE_REQUEST, 0x01, data});
                break;

            case SELECT_SYLLABUS: // 선택 강의계획서 조회
                System.out.printf("%50s과목 코드를 입력하세요 : ", "");
                int input = s.nextInt();
                serviceProtocol(new Object[]{Protocol.PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_REQUEST, 0x01, input});
                break;

            case EXIT :
                return;
            default :
                System.out.printf("%50sWRONG NUMBER!..\n","");
                selectMenu();
                break;
        }
    }

    public void serviceProtocol(Object[] protocol){
        // 0 : type, 1 : code, 2~ : some of data
        this.protocol = protocol;
    }

    public Object[] getProtocol(){
        return protocol;
    }

}
