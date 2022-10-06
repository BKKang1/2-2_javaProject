package Server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import main.ConvertTimeLine;
import persistence.dto.*;
import persistence.service.*;

public class DBServer implements Runnable {

    private DBServerThread clients[] = new DBServerThread[50];
    private ServerSocket server = null;
    private Thread       thread = null;
    private int clientCount = 0;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    private OpenedSubjectService openedSubjectService = new OpenedSubjectService();
    private SubjectService subjectService = new SubjectService();
    private SignUpTermService signUpTermService = new SignUpTermService();
    private SignUpService signUpService = new SignUpService();
    private MemberMainService memberMainService = new MemberMainService();
    private MemberSubService memberSubService = new MemberSubService();
    private SyllabusTermService syllabusTermService = new SyllabusTermService();
    private Protocol protocol = new Protocol();



    private final String DELIMITER = " ";
    private final String DATA_DELIMITER = "#";

    private String data;

    public DBServer(){}

    public DBServer(int port) {
        try {
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            start();
        } catch(IOException ioe) {
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
        }
    }

    public static void main(String[] args) {
        DBServer server = null;
            server = new DBServer(5500);
    }


    public void run() {
        while (thread != null) {
            try {
                System.out.println("Waiting for a client ...");
                addThread(server.accept());
            } catch(IOException ioe) {
                System.out.println("Server accept error: " + ioe); stop();
            }

        }
    }

    public void start()  {
        if (thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++)
            if (clients[i].getID() == ID)
                return i;
        return -1;
    }

    public void addThread(Socket socket) {
        if (clientCount < clients.length) {
            System.out.println("Client accepted: " + socket);
            clients[clientCount] = new DBServerThread(this, socket);
            try {
                clients[clientCount].open();
                clients[clientCount].start();
                clientCount++;
            } catch(IOException ioe) {
                System.out.println("Error opening thread: " + ioe);
            }
        } else
            System.out.println("Client refused: maximum " + clients.length + " reached.");
    }

    public synchronized void handle(int ID, String input) { // here
        if (input.equals(Protocol.PT_EXIT)) { // PT_EXIT
            // clients[findClient(ID)].send("bye");
            remove(ID);
        } else
            for (int i = 0; i < clientCount; i++);
                // clients[i].send(ID + ": " + input);
    }

    public synchronized void remove(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            DBServerThread toTerminate = clients[pos];
            System.out.println("Removing client thread " + ID + " at " + pos);
            if (pos < clientCount-1)
                for (int i = pos+1; i < clientCount; i++)
                    clients[i-1] = clients[i];
            clientCount--;
            try {
                toTerminate.close();
            } catch(IOException ioe) {
                System.out.println("Error closing thread: " + ioe);
            }
            toTerminate.stop();
        }
    }

    public void selectPT(int id, Protocol protocol) throws Exception { // id : port number, str : input data (ex : header, body )
        int type = protocol.getType();
        int code = protocol.getCode();
        int length = 0;
        try{
            length = protocol.getPacketLength(protocol.getPacket());
        }
        catch(ArrayIndexOutOfBoundsException e){
            length = 0;
        }

        int myID = clients[findClient(id)].getID();
        String packet_data = protocol.getResult();

        //System.out.println("type : " + type + " code : " + code);
        // packet을 type, code, length, data 분할하는 부분

        switch (type){
            case Protocol.PT_ID_REQUEST:
                login_request(id);
                break;

            case Protocol.PT_ID_SEND:
                read_id_response(id, packet_data);
                break;

            case Protocol.PT_PWD_SEND:
                read_pwd_response(id, packet_data);
                break;

            case Protocol.PT_ACCOUNT_CREATE_REQUEST : // 교수 학생 계정 생성 요청
                account_create_response(id);
                break;

            case Protocol.PT_ACCOUNT_CREATE_DATA_SEND : // 교수, 학생 계정 생성 데이터 전송
                account_create_result(id, packet_data); // here
                break;

            case Protocol.PT_SUBJECT_REQUEST : // 교과목 생성,수정,삭제
                manage_subject_response(id);
                break;

            case Protocol.PT_SUBJECT_DATA_SEND : // 교과목 생성, 수정, 삭제 데이터 전송
                manage_subject_result(id, code, packet_data); // here
                break;

            case 9999 : // here 개설 교과목 생성,수정,삭제 개설교과목 프로토콜 없음
                manage_opened_subject_response(id);
                break;

            case 99999 : // here 개설 교과목 생성, 수정, 삭제 데이터 전송 here 개설교과목 프로토콜 없음
                break;

            case Protocol.PT_SYLLABUS_INPUT_TERM_SETTING_REQUEST : // 강의계획서 입력기간 설정 요청
                set_lecture_plan_response(id);
                break;

            case Protocol.PT_SYLLABUS_INPUT_TERM_SETTING_DATA_SEND: // 강의 계획서 입력 기간 데이터 전송
                set_lecture_plan_result(id, packet_data);
                break;

            case Protocol.PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_REQUEST : // 학년별 수강신청 기간 설정 요청
                set_applyDate_by_grade_response(id);
                break;

            case Protocol.PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_DATA_SEND : // 학년별 수강신청 기간 설정 데이터 전송
                set_applyDate_by_grade_result(id, packet_data);
                break;

            case Protocol.PT_INFORMATION_LOOKUP_REQUEST : // 교수 학생 정보 조회 요청
                read_person_response(id);
                break;

            case Protocol.PT_INFORMATION_LOOKUP_DATA_SEND : // 교수 학생 정보 데이터 전송
                read_person_result(id, code);
                break;

            case Protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_REQUEST : // 개설 교과목 정보 조회 요청
                read_inform_opened_subject_response(id);
                break;

            case Protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_DATA_SEND : // 개설 교과목 정보 조회 데이터 전송
                read_inform_opened_subject_result(id, code, packet_data);
                break;

            case Protocol.PT_PROF_INFORMATION_REVISE_REQUEST : // 개인정보 및 비밀번호 수정 요청(교수)
                update_prof_response(id);
                break;

            case Protocol.PT_PROF_INFORMATION_REVISE_DATA_SEND : // 개인정보 및 비밀번호 수정 데이터 전송(교수)
                update_prof_result(id, code, packet_data);
                break;

            case Protocol.PT_STUDENT_INFORMATION_REVISE_REQUEST : // 개인정보 및 비밀번호 수정 요청 (학생)
                update_student_response(id);
                break;

            case Protocol.PT_STUDENT_INFORMATION_REVISE_DATA_SEND : // 개인정보 및 비밀번호 수정 데이터 전송 (학생)
                update_student_result(id, code, packet_data);
                break;

            case Protocol.PT_SUBJECT_LIST_LOOKUP_REQUEST : // 교과목 목록 조회 요청
                read_opened_subject_response(id);
                break;

            case Protocol.PT_SUBJECT_LIST_LOOKUP_DATA_SEND : // 교과목 목록 조회 요청 데이터 전송
                read_opened_subject_result(id);
                break;

            case Protocol.PT_SYLLABUS_INPUT_REVISE_REQUEST : // 강의계획서 입력 수정 요청
                manage_lecture_plan_response(id);
                break;

            case Protocol.PT_SYLLABUS_INPUT_REVISE_DATA_SEND : // 강의계획서 입력 수정 데이터 전송
                manage_lecture_plan_result(id, code , packet_data); // here
                break;

            case Protocol.PT_SUBJECT_SYLLABUS_LOOKUP_REQUEST : // 교과목 강의 계획서 조회 요청
                read_lecture_plan_response(id);
                break;

            case Protocol.PT_SUBJECT_SYLLABUS_LOOKUP_DATA_SEND : // 교과목 강의 계획서 조회 데이터 전송
                read_lecture_plan_result(id,"name");
                break;

            case Protocol.PT_SUBJECT_SIGN_UP_STUDENT_LIST_REQUEST : // 교과목 수강 신청 학생 목록 조회 요청
                read_subject_sign_up_student_response(id);
                break;

            case Protocol.PT_SUBJECT_SIGN_UP_STUDENT_LIST_DATA_SEND : // 교과목 수강 신청 학생 목록 조회 데이터 전송
                read_subject_sign_up_student_result(id,"name");
                break;

            case Protocol.PT_SUBJECT_SCHEDULE_LOOKUP_REQUEST : // 교과목 시간표 조회 요청
                read_opened_subject_schedule_response(id);
                break;

            case Protocol.PT_SUBJECT_SCHEDULE_LOOKUP_DATA_SEND : // 교과목 시간표 조회 데이터 전송
                read_opened_subject_schedule_result(id,"name");
                break;

            case Protocol.PT_SIGN_UP_OR_CANCEL_REQUEST : // 수강 신청,취소 요청
                sign_up_or_cancel_response(id);
                break;

            case Protocol.PT_SIGN_UP_OR_CANCEL_DATA_SEND : // 수강 신청,취소 데이터 전송
                sign_up_or_cancel_result(id, code ,packet_data); // here
                break;

            case Protocol.PT_ALL_OPENED_SUBJECT_LIST_LOOKUP_REQUEST : // 개설교과목 조회요청(전과목)
                read_all_opened_subject_result(id);
                break;

            case Protocol.PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_REQUEST : // 선택 교과목 강의계획서 조회 요청
                read_select_subject_plan_response(id);
                break;

            case Protocol.PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_DATA_SEND : // 선택 교과목 강의계획서 조회 데이터 전송
                read_select_subject_plan_result(id, packet_data);
                break;

            case Protocol.PT_MY_SCHEDULE_LOOKUP_REQUEST : // 본인 시간표 조회 전송
                read_my_schedule_result(id); // here
                break;

            case Protocol.PT_OPENED_SUBJECT_CRD_REQUEST:
                read_opened_subject_crd_response(id);
                break;

            case Protocol.PT_OPENED_SUBJECT_CRD_DATA_SEND:
                manage_opened_subject_result(id, code, packet_data);
                break;

            case Protocol.PT_SIGN_UP_STUDENT_LIST_REQUEST:
                read_sign_up_student_list(id);
                break;

            case Protocol.PT_SIGN_UP_STUDENT_LIST_DATA_SEND:
                read_sign_up_student_list_data(id, packet_data);
                break;



            default:
                System.out.println("ERROR (Code : " + code + ", Type : " + type + ")");
                break;
        }

    }

    public void read_sign_up_student_list(int id){
        clients[findClient(id)].send(protocol.PT_SIGN_UP_STUDENT_LIST_RESPONSE, 0);
    }

    public void read_sign_up_student_list_data(int id, String data){
        List<SignUpDTO> dtos;
        try{
            String[] datas = data.split("#"); // 데이터 2개 이상일 시
            dtos = signUpService.paging(datas); // 이름, 과목코드 으로 검색
        }
        catch(Exception e){
            dtos = signUpService.paging(data); // 이름 또는 과목코드로 검색
        }

        String result = "";

        if (dtos.size() != 0){
            for (int i = 0 ; i < dtos.size()-1; i++)
                result += dtos.get(i).toString() + DATA_DELIMITER;
            result += dtos.get(dtos.size()-1).toString();
            clients[findClient(id)].send(result, protocol.PT_SIGN_UP_STUDENT_LIST_RESULT, 1);
        }

        else
            clients[findClient(id)].send(result, protocol.PT_SIGN_UP_STUDENT_LIST_RESULT, 2);
    }

    public void read_opened_subject_crd_response(int id){
        clients[findClient(id)].send(protocol.PT_OPENED_SUBJECT_CRD_RESPONSE, 0);
    }

    public synchronized void login_request(int id){
        clients[findClient(id)].send(protocol.PT_ID_REQUEST, 0);
    }

    public synchronized void read_id_response(int id, String data){
        clients[findClient(id)].setIDData(Integer.parseInt(data));
        clients[findClient(id)].send(protocol.PT_PWD_REQUEST, 0);
    }

    public synchronized void read_pwd_response(int id, String data){
        int id_data = clients[findClient(id)].getIDData();
        try{
            if (memberSubService.getMemberById(id_data).getPassword().equals(data)){ // 비밀번호 일치할경우
                clients[findClient(id)].user = memberSubService.getMemberById(id_data);
                System.out.println(clients[findClient(id)].user.toString());
                if (clients[findClient(id)].user.getJob().equals("관리자"))
                    clients[findClient(id)].send(protocol.PT_LOGIN_RESULT,1);
                else if (clients[findClient(id)].user.getJob().equals("교수"))
                    clients[findClient(id)].send(protocol.PT_LOGIN_RESULT,2);
                else if (clients[findClient(id)].user.getJob().equals("학생"))
                    clients[findClient(id)].send(protocol.PT_LOGIN_RESULT,3);
            }

            else // 비밀번호가 틀릴 경우
                clients[findClient(id)].send(protocol.PT_LOGIN_RESULT,4);
        }
        catch (IndexOutOfBoundsException e){ // 없는 ID인 경우
            clients[findClient(id)].send(protocol.PT_LOGIN_RESULT,4);
        }

    }

    // 교수 / 학생 계정 생성
    // 교수 / 학생 계정 생성 응답
    public void account_create_response(int id){
        clients[findClient(id)].send(protocol.PT_ACCOUNT_CREATE_RESPONSE, 0);
    }
    // 교수 / 학생 계정 생성 결과
    public synchronized void account_create_result(int id, String data){
        String[] personInfo = data.split(DATA_DELIMITER); // data를 구분자로 나눠서 계정 생성
        memberMainService.create(Integer.parseInt(personInfo[0]), personInfo[1], personInfo[2], personInfo[3], personInfo[4], personInfo[5], Integer.parseInt(personInfo[6]));
        clients[findClient(id)].send(protocol.PT_ACCOUNT_CREATE_RESULT, 1);
    }



    // 교과목 생성 / 수정 / 삭제
    // 교과목 생성 / 수정 / 삭제 응답
    public void manage_subject_response(int id){
        clients[findClient(id)].send(protocol.PT_SUBJECT_RESPONSE, 0);
    }
    // 교과목 생성 / 수정 / 삭제 결과
    public synchronized void manage_subject_result(int id, int code, String data){
        String[] subject_data = data.split(DATA_DELIMITER);
        for (String s : subject_data)
            System.out.println("data : " + s);

        switch(code){
            case 1:
                subjectService.create(Integer.parseInt(subject_data[0]),subject_data[1], Integer.parseInt(subject_data[2]), Integer.parseInt(subject_data[3]));
                clients[findClient(id)].send(protocol.PT_SUBJECT_RESULT, 1);
                break;
            case 2:
                subjectService.update(Integer.parseInt(subject_data[0]), subject_data[1]);
                clients[findClient(id)].send(protocol.PT_SUBJECT_RESULT, 1);
                break;
            case 3:
                subjectService.delete(Integer.parseInt(subject_data[0]));
                clients[findClient(id)].send(protocol.PT_SUBJECT_RESULT, 1);
                break;
            default:
                clients[findClient(id)].send(protocol.PT_SUBJECT_RESULT, 2);
                break;
        }
    }


    // 개설 교과목 생성 / 수정 / 삭제
    // 개설 교과목 생성 / 수정 / 삭제 응답
    public void manage_opened_subject_response(int id){
        clients[findClient(id)].send(protocol.PT_OPENED_SUBJECT_CRD_RESPONSE,0); // here
    }
    // 개설 교과목 생성 / 수정 / 삭제 결과
        public synchronized void manage_opened_subject_result(int id, int code, String data){
        String[] opened_subject_data = data.split(DATA_DELIMITER);
        int grade;
        try{
            grade = subjectService.getByLectureCode(Integer.parseInt(opened_subject_data[0])).getGrade();
        }
        catch (Exception e){
            clients[findClient(id)].send(protocol.PT_OPENED_SUBJECT_CRD_RESULT,2); // here
            return;
        }

            switch (code){
            case 1:
                if (openedSubjectService.create(Integer.parseInt(opened_subject_data[0]), opened_subject_data[1],
                        grade, Integer.parseInt(opened_subject_data[3]),
                        Integer.parseInt(opened_subject_data[4]), opened_subject_data[5], opened_subject_data[6], opened_subject_data[7]))
                    clients[findClient(id)].send(protocol.PT_OPENED_SUBJECT_CRD_RESULT,0x01); // here
                else{
                    clients[findClient(id)].send(protocol.PT_OPENED_SUBJECT_CRD_RESULT,0x02); // here
                }

                break;
            case 2:
                if(opened_subject_data[1].length()>3){ // 강의코드#강의실 (강의실의 경우 길이가 3보다 크기 때문에)
                    openedSubjectService.updateClassroom(Integer.parseInt(opened_subject_data[0]), opened_subject_data[1]); // 강의실 변경
                }else{
                    openedSubjectService.updateCapacity(Integer.parseInt(opened_subject_data[0]), Integer.parseInt(opened_subject_data[1])); // 최대 수용 인원 변경
                }
                clients[findClient(id)].send(protocol.PT_OPENED_SUBJECT_CRD_RESULT,1); // here
                break;
            case 3:
                openedSubjectService.deleteSubject(Integer.parseInt(data));
                clients[findClient(id)].send(protocol.PT_OPENED_SUBJECT_CRD_RESULT,1); // here
                break;
            default:
                clients[findClient(id)].send(protocol.PT_OPENED_SUBJECT_CRD_RESULT,2); // here
                break;
        }
    }


    // 강의 계획서 입력 기간 설정
    // 강의 계획서 입력 기간 설정 응답
    public void set_lecture_plan_response(int id){
        clients[findClient(id)].send(protocol.PT_SYLLABUS_INPUT_TERM_SETTING_RESPONSE, 0);
    }
    // 강의 계획서 입력 기간 설정 결과
    public synchronized void set_lecture_plan_result(int id, String data) throws Exception {
        String[] sign_up_temp_data = data.split(DATA_DELIMITER);
        syllabusTermService.setSyllabusTerm(Integer.parseInt(sign_up_temp_data[0]), sign_up_temp_data[1] ,sign_up_temp_data[2]);
        clients[findClient(id)].send(protocol.PT_SYLLABUS_INPUT_TERM_SETTING_RESULT, 1);
    }


    // 학년별 수강 신청 기간 설정
    // 학년별 수강 신청 기간 설정 응답
    public void set_applyDate_by_grade_response(int id){
        clients[findClient(id)].send(protocol.PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_RESPONSE, 0);
    }
    // 학년별 수강 신청 기간 설정 결과
    public synchronized void set_applyDate_by_grade_result(int id, String data) throws Exception {
        String[] sign_up_temp_data = data.split(DATA_DELIMITER);
        signUpTermService.setSignUpTerm(Integer.parseInt(sign_up_temp_data[0]), sign_up_temp_data[1] ,sign_up_temp_data[2]);
        clients[findClient(id)].send(protocol.PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_RESULT, 1);
    }


    // 교수 / 학생 정보 조회
    // 교수 / 학생 정보 조회 응답
    public void read_person_response(int id) {
        clients[findClient(id)].send(protocol.PT_INFORMATION_LOOKUP_RESPONSE, 0);
    }
    // 교수 / 학생 정보 조회 결과
    public void read_person_result(int id, int code){
        if(code == 1){ // 교수 정보 조회
            ArrayList<MemberDTO> members = memberSubService.getMembersByJob("교수");
            String sumOfList = "";
            for(MemberDTO dto : members){
                sumOfList += dto + "#";
            }
            clients[findClient(id)].send(sumOfList, protocol.PT_INFORMATION_LOOKUP_RESULT, 1);
        } else if(code == 2){ // 학생 정보 조회
            ArrayList<MemberDTO> members = memberSubService.getMembersByJob("학생");
            String sumOfList = "";
            for(MemberDTO dto : members){
                sumOfList += dto + "#";
            }
            clients[findClient(id)].send(sumOfList, protocol.PT_INFORMATION_LOOKUP_RESULT, 1);
        }else{
            clients[findClient(id)].send(protocol.PT_INFORMATION_LOOKUP_RESULT, 2);
        }
    }


    // 개설 교과목 정보 조회
    // 개설 교과목 정보 조회 응답
    public void read_inform_opened_subject_response(int id){
        clients[findClient(id)].send(protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_RESPONSE, 0);
    }
    // 개설 교과목 정보 조회 결과
    public void read_inform_opened_subject_result(int id, int code, String data) throws IOException {
        if(code == 1){ // 학년별
            List<OpenedSubjectDTO> openedSubjectDTOS = openedSubjectService.readByGrade(Integer.parseInt(data));
            String sumOfList = "";
            for(OpenedSubjectDTO dto : openedSubjectDTOS){
                sumOfList += dto + "#";
            }
            clients[findClient(id)].send(sumOfList, protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_RESULT, 1);
        }else if(code == 2){ // 교수별
            List<OpenedSubjectDTO> openedSubjectDTOS = openedSubjectService.readByProf(data);
            String sumOfList = "";
            for(OpenedSubjectDTO dto : openedSubjectDTOS){
                sumOfList += dto + "#";
            }
            clients[findClient(id)].send(sumOfList, protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_RESULT, 1);
        }else if(code == 3){
            String[] opened_subject_data = data.split(DATA_DELIMITER);
            List<OpenedSubjectDTO> openedSubjectDTOS = openedSubjectService.readByGradeProf(Integer.parseInt(opened_subject_data[0]), opened_subject_data[1]);
            String sumOfList = "";
            for(OpenedSubjectDTO dto : openedSubjectDTOS){
                sumOfList += dto + "#";
            }
            clients[findClient(id)].send(sumOfList, protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_RESULT, 1);
        }else {
            clients[findClient(id)].send(protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_RESULT, 2);
        }
    }


    // 교수님 관점 작업
    // 교수 개인 정보 및 비밀번호 수정 요청
    // 교수 개인 정보 및 비밀번호 수정 요청 응답
    public void update_prof_response(int id){
        clients[findClient(id)].send(protocol.PT_PROF_INFORMATION_REVISE_RESPONSE,0); // 교수 일때
    }
    // 교수 개인 정보 및 비밀번호 수정 요청 결과
    public synchronized void update_prof_result(int id, int code, String data){
        String[] member_data = data.split(DATA_DELIMITER);
        if (code == 1){ // 휴대폰 변경
            MemberDTO profMember = memberSubService.getMemberById(clients[findClient(id)].user.getId());
            System.out.println(clients[findClient(id)].user.toString());
            profMember.setPhone(member_data[0]);
            memberSubService.setMember(profMember);
            clients[findClient(id)].send(protocol.PT_PROF_INFORMATION_REVISE_RESULT,1);
        } // 비밀번호 변경
        else if (code == 2){
            MemberDTO profMember = memberSubService.getMemberById(clients[findClient(id)].user.getId());
            profMember.setPassword(member_data[0]);
            memberSubService.setMember(profMember);
            clients[findClient(id)].send(protocol.PT_PROF_INFORMATION_REVISE_RESULT, 1);
        }
        else{
            clients[findClient(id)].send(protocol.PT_PROF_INFORMATION_REVISE_RESULT,2);
        }
    }


    // 개설 교과목 목록 조회 요청
    // 개설 교과목 목록 조회 요청 응답
    public void read_opened_subject_response(int id){
        clients[findClient(id)].send(protocol.PT_SUBJECT_LIST_LOOKUP_RESPONSE,0);
    }
    // 개설 교과목 목록 조회 요청 결과
    public synchronized void read_opened_subject_result(int id) throws IOException {
        List<OpenedSubjectDTO> openedSubjectDTOS = openedSubjectService.readByProf("NULL"); // here
        String sumOfList = "";
        for(OpenedSubjectDTO dto : openedSubjectDTOS){
            sumOfList += dto + "#";
        }
        clients[findClient(id)].send(sumOfList ,protocol.PT_SUBJECT_LIST_LOOKUP_RESULT, 1);
    }


    // 강의 계획서 입력 / 수정 요청
    // 강의 계획서 입력 / 수정 요청 응답
    public  void manage_lecture_plan_response(int id){
        clients[findClient(id)].send(protocol.PT_SYLLABUS_INPUT_REVISE_RESPONSE, 0);
    }
    // 강의 계획서 입력 / 수정 요청 결과 (data : 강의 코드, 수정 데이터)
    public synchronized void manage_lecture_plan_result(int id,int code,String data) throws Exception {
        int lectureCode = 0;
        String syllabus = "";
        try{
            lectureCode = Integer.parseInt(data.split(DATA_DELIMITER)[0]);
            syllabus = data.split(DATA_DELIMITER)[1];
        }
        catch (Exception e){ // 잘못된 데이터가 온 경우
            clients[findClient(id)].send(protocol.PT_SYLLABUS_INPUT_REVISE_RESULT, 2);
            return;
        }

        if (openedSubjectService.readByLectureCode(lectureCode) == null){ // 해당 과목코드가 존재하지 않는 경우
            clients[findClient(id)].send(protocol.PT_SYLLABUS_INPUT_REVISE_RESULT, 2);
            return;
        }

        if (!openedSubjectService.readByLectureCode(lectureCode).getProf().equals(clients[findClient(id)].user.getName())){
            clients[findClient(id)].send(protocol.PT_SYLLABUS_INPUT_REVISE_RESULT, 2);
            return;
        }


        // 입력 수정 모두 동일하게 처리
        if (syllabusTermService.isSyllabusTerm(openedSubjectService.readByLectureCode(lectureCode).getGrade(), lectureCode)){ // 강의 계획서 입력 기간이라면
            openedSubjectService.updateSyllabus(lectureCode, syllabus);
            clients[findClient(id)].send(protocol.PT_SYLLABUS_INPUT_REVISE_RESULT, 1);
        }
        else
            clients[findClient(id)].send(protocol.PT_SYLLABUS_INPUT_REVISE_RESULT, 2);

    }

    // 개설 교과목 강의 계획서 조회
    // 개설 교과목 강의 계획서 조회 응답
    public void read_lecture_plan_response(int id){ // here
        clients[findClient(id)].send(protocol.PT_SUBJECT_SYLLABUS_LOOKUP_RESPONSE, 0);
    }
    // 개설 교과목 강의 계획서 조회 결과
    public void read_lecture_plan_result(int id, String name){ // here
        clients[findClient(id)].send(protocol.PT_SUBJECT_SYLLABUS_LOOKUP_RESULT, 1);
    }


    // 교과목 수강 신청 학생 목록 조회 응답
    public void read_subject_sign_up_student_response(int id){
        clients[findClient(id)].send(protocol.PT_SUBJECT_SIGN_UP_STUDENT_LIST_RESPONSE,0);
    }
    public void read_subject_sign_up_student_result(int id,String name){
        // here
        // sign_up.xml 에서 selectProfAspect 기능 메소드 넣어야 됨
    }
// 교과목 시간표 조회 응답

    public void read_opened_subject_schedule_response(int id){
        clients[findClient(id)].send(protocol.PT_SUBJECT_SCHEDULE_LOOKUP_RESPONSE,0);
    }
    // 교과목 시간표 조회 결과
    public void read_opened_subject_schedule_result(int id,String data) throws IOException {
        List<OpenedSubjectDTO> openedSubjectTimes = openedSubjectService.readTimeByProf(clients[findClient(id)].user.getName());
        String result = "";
        for (OpenedSubjectDTO dto : openedSubjectTimes){
            String time = dto.getTime();
            String subject = subjectService.getByLectureCode(dto.getLectureCode()).getLecture();
            ConvertTimeLine convertTimeLine = new ConvertTimeLine(time);
            result += "[과목명] " + subject + " [시간] " + convertTimeLine.getTimeLine() + DATA_DELIMITER;
        }
        clients[findClient(id)].send(result,protocol.PT_SUBJECT_SCHEDULE_LOOKUP_RESULT , 1);
    }


    // 학생 관점 작업
    // 학생 개인정보 및 비밀번호 수정 요청
    // 학생 개인정보 및 비밀번호 수정 요청 응답
    public void update_student_response(int id){
        clients[findClient(id)].send(protocol.PT_STUDENT_INFORMATION_REVISE_RESPONSE,0); // 학생일때
    }
    //학생 개인정보 및 비밀번호 수정 요청 결과
    public synchronized void update_student_result(int id, int code, String data){
        String[] member_data = data.split(DATA_DELIMITER);
        if (code == 1){ // 이름 변경
            MemberDTO studentMember = memberSubService.getMemberById(clients[findClient(id)].user.getId());
            studentMember.setName(member_data[0]);
            memberSubService.setMember(studentMember);
            clients[findClient(id)].send(protocol.PT_STUDENT_INFORMATION_REVISE_RESULT,1);
        } // 비밀번호 변경
        else if (code == 2){
            MemberDTO studentMember = memberSubService.getMemberById(clients[findClient(id)].user.getId());
            studentMember.setPassword(member_data[0]);
            memberSubService.setMember(studentMember);
            clients[findClient(id)].send(protocol.PT_STUDENT_INFORMATION_REVISE_RESULT, 1);
        }
        else{
            clients[findClient(id)].send(protocol.PT_STUDENT_INFORMATION_REVISE_RESULT,2);
        }
    }


    // 수강 신청 / 취소
    // 수강 신청 / 취소 응답
    public void sign_up_or_cancel_response(int id){
        clients[findClient(id)].send(protocol.PT_SIGN_UP_OR_CANCEL_RESPONSE, 0);
    }
    // 수강 신청 / 취소 응답
    public synchronized void sign_up_or_cancel_result(int id, int code, String data) throws Exception { // 학번을 thread에서 가지고 있다고 가정
        int myID = clients[findClient(id)].user.getId();
        if(code == 1){ // 수강 신청
            if (signUpService.create(myID, Integer.parseInt(data))) // 여기서 data 는 lectureCode를 의미
                clients[findClient(id)].send(protocol.PT_SIGN_UP_OR_CANCEL_RESULT, 1);
            else
                clients[findClient(id)].send(protocol.PT_SIGN_UP_OR_CANCEL_RESULT, 2);
        }else if(code == 2){
            if (signUpService.delete(myID, Integer.parseInt(data)))
                clients[findClient(id)].send(protocol.PT_SIGN_UP_OR_CANCEL_RESULT, 1);
            else
                clients[findClient(id)].send(protocol.PT_SIGN_UP_OR_CANCEL_RESULT, 2);
        }else{
            clients[findClient(id)].send(protocol.PT_SIGN_UP_OR_CANCEL_RESULT, 2);
        }
    }


    // 개설 교과목 (전학년) 조회
    // 개설 교과목 (전학년) 조회 결과
    public void read_all_opened_subject_result(int id) throws IOException{
        List<OpenedSubjectDTO> openedSubjectDTOS = openedSubjectService.readAll();
        String sumOfList = "";
        for(OpenedSubjectDTO dto : openedSubjectDTOS){
            sumOfList += dto + "#";
        }
        clients[findClient(id)].send(sumOfList, protocol.PT_ALL_OPENED_SUBJECT_LIST_LOOKUP_DATA_SEND, 1);
    }


    // 선택 교과목 강의 계획서 조회
    // 선택 교과목 강의 계획서 조회 응답
    public void read_select_subject_plan_response(int id){
        clients[findClient(id)].send(protocol.PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_RESPONSE, 0);
    }
    // 선택 교과목 강의 계획서 조회 결과 (데이터 : 강의 코드)
    public void read_select_subject_plan_result(int id, String data) throws IOException {
        int lectureCode = 0;

        try{
            String result = "";
            lectureCode = Integer.parseInt(data);
            if (openedSubjectService.readByLectureCode(lectureCode).getSyllabus() != null)
                result = openedSubjectService.readByLectureCode(lectureCode).getSyllabus();
            clients[findClient(id)].send(result, protocol.PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_RESULT, 0x01);
        }
        catch (Exception e){ // 잘못된 양식 입력 시 또는 과목코드가 존재하지 않는 경우
            clients[findClient(id)].send(protocol.PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_RESULT, 0x02);
            return;
        }



        // 강의 계획서 조회하는 method
    }


    // 본인 시간표 조회
    // 본인 시간표 조회 결과
    public void read_my_schedule_result(int id){
        List<SignUpDTO> userSignups = signUpService.getById(clients[findClient(id)].user.getId());
        String result = "";
        for (SignUpDTO userSignup : userSignups){
            result += "[과목명] " + userSignup.getLecture() + " [시간] " + userSignup.getTime() + DATA_DELIMITER;
        }
        clients[findClient(id)].send(result, protocol.PT_MY_SCHEDULE_LOOKUP_DATA_SEND, 1);
    }


}
