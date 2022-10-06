package main;

import Server.Protocol;

import java.io.IOException;
import java.util.Scanner;

public class AdminController {
    private Object[] protocol;
    Scanner s = new Scanner(System.in);
    private String protocolData="";

    public void printAdminMenu(){ // 관리자 기능 출력
        System.out.printf("%50s%30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s \n","","교수 학생 계정 생성");
        System.out.printf("%50s| 2. %-23s \n","","교과목 생성/수정/삭제");
        System.out.printf("%50s| 3. %-23s \n","","개설 교과목 생성/수정/삭제");
        System.out.printf("%50s| 4. %-23s \n","","강의 계획서 입력 기간 설정");
        System.out.printf("%50s| 5. %-23s \n","","학년별 수강 신청 기간 설정");
        System.out.printf("%50s| 6. %-23s \n","","교수/학생 정보 조회");
        System.out.printf("%50s| 7. %-23s \n","","개설 교과목 정보 조회");
        System.out.printf("%50s| 8. %-23s \n","","Quit");
        System.out.printf("%50s%30s\n","","+============================+");
        System.out.printf("%50sInput num : ","");
    }

    public void printSubjectMenu(){ // 관리자 교과목 기능 출력
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s |\n","","Create");
        System.out.printf("%50s| 2. %-23s |\n","","Update");
        System.out.printf("%50s| 3. %-23s |\n","","Delete");
        System.out.printf("%50s| 4. %-23s |\n","","Exit");
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50sInput num : ", "");
    }

    public void printOpenedSubjectMenu() {// 관리자 개설 교과목 기능 출력
        System.out.printf("%50s%-30s\n", "", "+============================+");
        System.out.printf("%50s| 1. %-23s |\n", "", "Create");
        System.out.printf("%50s| 2. %-23s |\n", "", "Update classroom");
        System.out.printf("%50s| 3. %-23s |\n", "", "Update capacity");
        System.out.printf("%50s| 4. %-23s |\n", "", "DELETE");
        System.out.printf("%50s| 5. %-23s |\n", "", "Exit");
        System.out.printf("%50s%-30s\n", "", "+============================+");
        System.out.printf("%50sInput num : ", "");
    }

    public void printSyllabusTermMenu(){ // 관리자 강의계획서기간 기능 출력
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s |\n","","Set Term");
        System.out.printf("%50s| 2. %-23s |\n","","Exit");
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50sInput num : ", "");
    }

    public void printSignUpTermMenu(){ // 관리자 수강신청기간 기능 출력
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s |\n","","Set Term");
        System.out.printf("%50s| 2. %-23s |\n","","Exit");
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50sInput num : ", "");
    }

    public void printReadPersonInfoMenu(){ // 학생 교수 조회 기능 출력
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s |\n","","Read All Professor");
        System.out.printf("%50s| 2. %-23s |\n","","Read All Student");
        System.out.printf("%50s| 3. %-23s |\n","","Exit");
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50sInput num : ","");
    }

    public void printReadOpenedSubjectMenu(){ //개설 교과목조회 기능 출력
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50s| 1. %-23s |\n","","Read All");
        System.out.printf("%50s| 2. %-23s |\n","","Read By Prof");
        System.out.printf("%50s| 3. %-23s |\n","","Read By Grade");
        System.out.printf("%50s| 4. %-23s |\n","","Read By Grade And Prof");
        System.out.printf("%50s| 5. %-23s |\n","","Exit");
        System.out.printf("%50s%-30s\n","","+============================+");
        System.out.printf("%50sInput num : ", "");
    }


    //관리자 기능 번호
    final int ADMIN_CREATE_ACCOUNT = 1;
    final int ADMIN_MANAGE_SUBJECT = 2;
    final int ADMIN_MANAGE_OPENED_SUBJECT = 3;
    final int ADMIN_SET_LECTURE_PLAN_DATE = 4;
    final int ADMIN_SET_SIGN_UP_TERM_BY_GRADE = 5;
    final int ADMIN_READ_PERSON_INFO = 6;
    final int ADMIN_READ_OPENED_SUBJECT_INFO = 7;
    final int ADMIN_EXIT = 8;


    //관리자 교과목 기능 번호
    final int ADMIN_SUBJECT_CREATE = 1;
    final int ADMIN_SUBJECT_UPDATE = 2;
    final int ADMIN_SUBJECT_DELETE = 3;
    final int ADMIN_SUBJECT_EXIT = 4;

    //관리자 개설교과목 기능 번호
    final int ADMIN_Opened_SUBJECT_CREATE = 1;
    final int ADMIN_Opened_SUBJECT_UPDATE_CLASSROOM = 2;
    final int ADMIN_Opened_SUBJECT_UPDATE_CAPACITY = 3;
    final int ADMIN_Opened_SUBJECT_DELETE = 4;
    final int ADMIN_Opened_SUBJECT_CUD_EXIT = 5;

    //관리자 강의계획서 기간 기능 번호
    final int ADMIN_SYLLABUS_SET_TERM = 1;
    final int ADMIN_SYLLABUS_EXIT = 2;

    //관리자 수강 신청 기간 기능 번호
    final int ADMIN_SIGNUP_TERM_SET = 1;
    final int ADMIN_SIGNUP_TERM_EXIT = 2;

    //관리자 교수 학생 조회 기능 번호
    final int ADMIN_READ_ALL_PROFESSOR = 1;
    final int ADMIN_READ_ALL_STUDENT = 2;
    final int ADMIN_READ_EXIT = 3;


    // 관리자 개설교과목 조회 기능 번호
    final int ADMIN_OPENED_SUBJECT_READ_ALL = 1;
    final int ADMIN_OPENED_SUBJECT_READ_BY_PROF = 2;
    final int ADMIN_OPENED_SUBJECT_READ_BY_GRADE = 3;
    final int ADMIN_OPENED_SUBJECT_READ_BY_GRADE_PROF = 4;
    final int ADMIN_OPENED_SUBJECT_READ_EXIT= 5;

    // 관리자 기능 선택
    public int[] selectAdminService(int num) throws IOException, Exception {
        int[] ans = new int[2]; // ans[0] : num , ans[1] : 프로토콜 유무 (값이 0이면 프로토콜 없음, 1이면 프로토콜 존재)
        ans[1] = 0;
        switch(num){
            case ADMIN_CREATE_ACCOUNT :
                protocolData = makeMember();
                // 교수, 학생 정보 생성 요청, 0x00, 정보 생성에 관한 데이터
                serviceProtocol(new Object[]{Protocol.PT_ACCOUNT_CREATE_REQUEST, 0x00, protocolData});
                if (getProtocol() != null){
                    ans[0] = 0;
                    ans[1] = 1;
                }
                break;
            case ADMIN_MANAGE_SUBJECT:
                printSubjectMenu();//추가적인 선택이 필요함
                selectSubjectCUD();
                if (getProtocol() != null){
                    ans[0] = ADMIN_MANAGE_SUBJECT;
                    ans[1] = 1;
                }

                break;
            case ADMIN_MANAGE_OPENED_SUBJECT:
                printOpenedSubjectMenu();
                selectOpenedSubjectCUD();
                if (getProtocol() != null){
                    ans[0] = ADMIN_MANAGE_OPENED_SUBJECT;
                    ans[1] = 1;
                }
                break;
            case ADMIN_SET_LECTURE_PLAN_DATE:
                printSyllabusTermMenu();
                selectSyllabusTerm();
                if (getProtocol() != null){
                    ans[0] = ADMIN_SET_LECTURE_PLAN_DATE;
                    ans[1] = 1;
                }
                break;

            case ADMIN_SET_SIGN_UP_TERM_BY_GRADE:
                printSignUpTermMenu();
                selectSignUpTerm();
                if (getProtocol() != null){
                    ans[0] = ADMIN_SET_SIGN_UP_TERM_BY_GRADE;
                    ans[1] = 1;
                }
                break;

            case ADMIN_READ_PERSON_INFO:
                printReadPersonInfoMenu();
                selectReadPerson();
                if (getProtocol() != null){
                    ans[0] = ADMIN_READ_PERSON_INFO;
                    ans[1] = 1;
                }
                break;

            case ADMIN_READ_OPENED_SUBJECT_INFO:
                printReadOpenedSubjectMenu();
                selectOpenedSubjectRead();
                if (getProtocol() != null){
                    ans[0] = ADMIN_READ_OPENED_SUBJECT_INFO;
                    ans[1] = 1;
                }
                break;

            case ADMIN_EXIT:
                return ans;
            default:
                break;
        }
        return ans;
    }

    //관리자 교과목 기능 선택
    public void selectSubjectCUD(){
        int num = s.nextInt();
        s.nextLine();

        switch (num) {
            case ADMIN_SUBJECT_CREATE:
                protocolData = createSubject();
                serviceProtocol(new Object[]{Protocol.PT_SUBJECT_REQUEST, 0x01, protocolData});
                return;
            case ADMIN_SUBJECT_UPDATE:
                protocolData = updateSubject();
                serviceProtocol(new Object[]{Protocol.PT_SUBJECT_REQUEST, 0x02, protocolData});
                return;
            case ADMIN_SUBJECT_DELETE:
                protocolData = deleteSubject();
                serviceProtocol(new Object[]{Protocol.PT_SUBJECT_REQUEST, 0x03, protocolData});
                return;
            case ADMIN_SUBJECT_EXIT:
                serviceProtocol(null);
                return;
            default:
                System.out.printf("%50sWRONG NUMBER!..\n", "");
                printSubjectMenu();
                break;
        }

    }

    // 관리자 개설교과목 기능 선택
    public void selectOpenedSubjectCUD() throws IOException {
        int num = s.nextInt();
        s.nextLine();


        switch (num) {
            case ADMIN_Opened_SUBJECT_CREATE:
                protocolData = createOpenedSubject();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_CRD_REQUEST, 0x01, protocolData});
                return;
            case ADMIN_Opened_SUBJECT_UPDATE_CLASSROOM:
                System.out.printf("%50s과목코드를 입력하세요 : ", "");
                int lectureCode = s.nextInt();
                s.nextLine();
                System.out.printf("%50s변경할 강의실을 입력하세요 : ", "");
                String classRoom = s.nextLine();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_CRD_REQUEST, 0x02, Integer.toString(lectureCode), classRoom});
                return;
            case ADMIN_Opened_SUBJECT_UPDATE_CAPACITY:
                System.out.printf("%50s과목코드를 입력하세요 : ", "");
                int _lectureCode = s.nextInt();
                System.out.printf("%50s변경할 인원 수를 입력하세요 : ", "");
                int capacity = s.nextInt();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_CRD_REQUEST, 0x02, Integer.toString(_lectureCode), Integer.toString(capacity)});
                return;
            case ADMIN_Opened_SUBJECT_DELETE:
                System.out.printf("%50s삭제할 과목 코드를 입력하세요 : ", "");
                int delete_lectureCode = s.nextInt();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_CRD_REQUEST, 0x03, Integer.toString(delete_lectureCode)});
                return;

            case ADMIN_Opened_SUBJECT_CUD_EXIT:
                serviceProtocol(null);
                return;

            default:
                System.out.printf("%50sWRONG NUMBER!..\n", "");
                printOpenedSubjectMenu();
                break;
        }

    }

    // 관리자 강의계획서 기간 기능 선택
    public void selectSyllabusTerm() {

        int num = s.nextInt();
        s.nextLine();
        switch (num) {
            case ADMIN_SYLLABUS_SET_TERM:
                protocolData = setSyllabusTerm();
                serviceProtocol(new Object[]{Protocol.PT_SYLLABUS_INPUT_TERM_SETTING_REQUEST, 0x00, protocolData});
                return;
            case ADMIN_SYLLABUS_EXIT:
                serviceProtocol(null);
                return;
            default:
                System.out.printf("%50sWRONG NUMBER!..\n", "");
                printSyllabusTermMenu();
                break;
        }

    }

    // 관리자 수강신청기간 기능 선택
    public void selectSignUpTerm(){

        int num = s.nextInt();
        s.nextLine();
        switch (num) {
            case ADMIN_SIGNUP_TERM_SET:
                protocolData = setSignUpTerm();
                serviceProtocol(new Object[]{Protocol.PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_REQUEST, 0x00, protocolData});
                return;
            case ADMIN_SIGNUP_TERM_EXIT:
                return;
            default:
                System.out.printf("%50sWRONG NUMBER!..\n", "");
                printSignUpTermMenu();
                break;
        }

    }

    // 학생,교수 조회 기능 선택
    public void selectReadPerson(){

        int num = s.nextInt();
        s.nextLine();
        switch (num) {
            case ADMIN_READ_ALL_PROFESSOR:
                // 정보 조회 요청, 0x01(교수)
                serviceProtocol(new Object[]{Protocol.PT_INFORMATION_LOOKUP_REQUEST, 0x01});
                return;
            case ADMIN_READ_ALL_STUDENT:
                // 정보 조회 요청, 0x02(학생)
                serviceProtocol(new Object[]{Protocol.PT_INFORMATION_LOOKUP_REQUEST, 0x02});
                return;

            case ADMIN_READ_EXIT:
                serviceProtocol(null);
                return;
            default:
                System.out.printf("%50sWRONG NUMBER!..\n", "");
                printSignUpTermMenu();
                break;
        }

    }

    // 개설교과목 조회 기능 선택
    public void selectOpenedSubjectRead(){

        int num = s.nextInt();
        s.nextLine();
        switch (num) {
            case ADMIN_OPENED_SUBJECT_READ_ALL:
                serviceProtocol(new Object[]{Protocol.PT_ALL_OPENED_SUBJECT_LIST_LOOKUP_REQUEST, 0x00});
                return;
            case ADMIN_OPENED_SUBJECT_READ_BY_PROF:
                System.out.printf("%50s교수 이름을 입력하세요 : ", "");
                String prof = s.nextLine();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_REQUEST, 0x02, prof});
                return;
            case ADMIN_OPENED_SUBJECT_READ_BY_GRADE:
                System.out.printf("%50s검색할 학년을 입력하세요 : ", "");
                int grade = s.nextInt();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_REQUEST, 0x01, Integer.toString(grade)});
                return;
            case ADMIN_OPENED_SUBJECT_READ_BY_GRADE_PROF:
                System.out.printf("%50s검색할 학년을 입력하세요 : ", "");
                int _grade = s.nextInt();
                s.nextLine();
                System.out.printf("%50s교수 이름을 입력하세요 : ", "");
                String _prof = s.nextLine();
                serviceProtocol(new Object[]{Protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_REQUEST, 0x03, Integer.toString(_grade), _prof});
                return;
            case ADMIN_OPENED_SUBJECT_READ_EXIT:
                serviceProtocol(null);
                return;

            default:
                System.out.printf("%50sWRONG NUMBER!..\n", "");
                printSignUpTermMenu();
                break;
        }

    }

    //관리자 수강신청 기간 기능 선택
    public String setSignUpTerm(){
        System.out.printf("%50s수강 신청 날짜 설정할 학년을 입력 (1) or (2) ... : ","");
        int grade = s.nextInt();
        if (grade < 1 || grade > 4){// 1~4 학년 이외의 값을 입력하면
            System.out.printf("%50s올바른 값을 입력하십시오!\n", "");
            return setSignUpTerm();
        }

        s.nextLine();
        System.out.printf("%50s수강 신청 시작 시간 입력 (yyyy-mm-dd) : ","");
        String strStartDate = s.nextLine();


        System.out.printf("%50s수강 신청 끝나는 시간 입력 (yyyy-mm-dd) : ","");
        String strEndDate = s.nextLine();

        String[] send = {Integer.toString(grade),strStartDate,strEndDate};
        return makeSendData(send);

    }


    //멤버 만드는 기능
    public String makeMember(){
        return createAccount(); // original create method
        //createSpec();
    }

    //개설 교과목 데이터 입력
    public String createOpenedSubject() {
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
        String syllabus = null;

        String[] sendData = {Integer.toString(lectureCode), prof, Integer.toString(grade), Integer.toString(maxCapacity), Integer.toString(nowCapacity), classroom, time, syllabus};
        return makeSendData(sendData);

    }

    //계정 생성 입력
    protected String createAccount(){ // delete parameter, input from keyboard
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

    //교과목 업데이트 입력
    public String updateSubject(){
        System.out.printf("%50sinput lectureCode : ","");
        int lectureCode = s.nextInt();
        s.nextLine();
        System.out.printf("%50sinput lecture : ","");
        String lecture = s.nextLine();

        String[] send = {Integer.toString(lectureCode), lecture};
        return makeSendData(send);
    }

    //교과목 삭제 입력
    public String deleteSubject(){
        System.out.printf("%50sinput lectureCode : ","");
        int lectureCode = s.nextInt();
        String[] send = {Integer.toString(lectureCode)};
        return makeSendData(send);
    }

    //교과목 생성 입력
    public String createSubject(){

        System.out.printf("%50sinput lectureCode : ","");
        int lectureCode = s.nextInt();
        s.nextLine();
        System.out.printf("%50sinput lecture : ","");
        String lecture = s.nextLine();
        System.out.printf("%50sinput grade : ","");
        int grade = s.nextInt();
        System.out.printf("%50sinput semester : ","");
        int semester = s.nextInt();

        String[] send = {Integer.toString(lectureCode), lecture, Integer.toString(grade), Integer.toString(semester)};
        return makeSendData(send);
    }

    // 구분자 넣어 주기
    String makeSendData(String[] send){ // 구분자 생성
        String data = "";
        for (int i = 0; i < send.length-1; i++){
            data += send[i] + "#";
        }
        data += send[send.length-1];
        return data;
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

    // 강의계획서 기간 입력
    public String setSyllabusTerm(){
        System.out.printf("%50s강의 계획서 입력 날짜 설정할 학년을 입력 (1) or (2) ... : ","");
        int grade = s.nextInt();
        if (grade < 1 || grade > 4){// 1~4 학년 이외의 값을 입력하면
            System.out.printf("%50s올바른 값을 입력하십시오!\n", "");
            return setSyllabusTerm();
        }

        s.nextLine();
        System.out.printf("%50s강의 계획서 입력 시작 시간 입력 (yyyy-mm-dd) : ","");
        String strStartDate = s.nextLine();


        System.out.printf("%50s강의 계획서 입력 끝나는 시간 입력 (yyyy-mm-dd) : ","");
        String strEndDate = s.nextLine();

        String[] send = {Integer.toString(grade),strStartDate,strEndDate};
        return makeSendData(send);

    }

}
