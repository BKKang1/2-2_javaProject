package main;

import Server.Protocol;
import persistence.view.OpenedSubjectView;
import persistence.view.SignUpView;
import persistence.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class MakingProtocol {
    private String msg = "";
    private int type;
    private int code;
    private InputStream is;
    private OutputStream os;
    private int loginStatus;
    public static final int ADMIN = 1;
    public static final int PROFESSOR = 2;
    public static final int STUDENT = 3;
    public static final int UNDEFINED = 4;
    public MakingProtocol(InputStream is, OutputStream os, Object[] objects) throws IOException {
        this.is = is;
        this.os = os;
        if (objects != null)
            make(objects);
    }

    public int getLoginStatus(){
        return loginStatus;
    }

    public void make(Object[] objects){
        type = (int)objects[0]; // 0번에는 무조건 type이 옴
        code = (int)objects[1]; // 1번에는 무조건 code가 옴 (이후 length가 2 이상인 경우에는 보내야할 데이터가 추후에 있는 것)
        if (objects.length > 2){ // 데이터 존재 시 msg 저장
            for (int i = 2 ; i < objects.length-1; i++){
                msg += objects[i].toString() + "#";
            }
            msg += objects[objects.length-1].toString();
        }
    }

    public void send(){ //프로토콜 전송
        try {
            Protocol protocol = new Protocol(type, code);
            os.write(protocol.getPacket());

        } catch (IOException ioe) {
            System.out.println(" ERROR sending: " + ioe.getMessage());
        }
    }

    public void response() throws IOException { // 각 서버에서 프로토콜이 왔을 경우 case에 맞게 받는 메소드
        Protocol protocol;
        int packetType;
        int code;
        String[] result;
        Scanner s = new Scanner(System.in);
        while (true) {
            protocol = new Protocol();            // 새 etc.Protocol 객체 생성 (기본 생성자)
            byte[] buf = protocol.getPacket();    // 기본 생성자로 생성할 때에는 바이트 배열의 길이가 1000바이트로 지정됨
            is.read(buf, 0, 6);            // 클라이언트로부터 정보 수신 (헤더부분만)
            packetType = buf[0];            // 수신 데이터에서 패킷 타입 얻음
            code = buf[1];                    // 수신 데이터에서 코드 얻음
            int length = protocol.getPacketLength(buf, 0); // 패킷의 가변길이 얻어옴
            buf = new byte[length];
            is.read(buf);
            protocol = new Protocol(packetType, code);
            protocol.setPacket(packetType, code, buf);    // 패킷 설정

            if (packetType == Protocol.PT_EXIT) {
                System.out.println("type : " + packetType + " code : " + code);
                System.out.println("클라이언트 종료");
                break;
            }

            switch(packetType) {
                case Protocol.PT_START:
                    protocol = new Protocol(Protocol.PT_INFORMATION_LOOKUP_REQUEST, 0);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_ID_REQUEST: // 서버로부터 ID 요청을 받음
                    protocol =new Protocol(Protocol.PT_ID_SEND, 0);
                    System.out.print("Insert your ID : ");
                    String idData = s.nextLine();
                    protocol.setResult(idData);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_PWD_REQUEST: // 서버로부터 PW 요청을 받음
                    protocol =new Protocol(Protocol.PT_PWD_SEND, 0);
                    System.out.print("Insert your Password : ");
                    String pwData = s.nextLine();
                    protocol.setResult(pwData);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_LOGIN_RESULT: // 로그인 결과 받음
                    if (code == 0x01 || code == 0x02 || code == 0x03) {
                        System.out.println("LOGIN_Success");
                        loginStatus = code;
                    } else if (code == 0x04) {
                        System.out.println("LOGIN_Failed");
                        loginStatus = UNDEFINED;
                        protocol = new Protocol(Protocol.PT_EXIT, 0);
                        os.write(protocol.getPacket());
                    }
                    return;

                case Protocol.PT_ACCOUNT_CREATE_RESPONSE: // 계정 생성 응답 받음
                    protocol = new Protocol(Protocol.PT_ACCOUNT_CREATE_DATA_SEND, 0);
                    protocol.setResult(msg);
                    os.write(protocol.getPacket());
                    break ;

                case Protocol.PT_ACCOUNT_CREATE_RESULT: //계정 생성 결과 받음
                    if (code == 0x01) {
                        System.out.println("계정 생성 성공!");
                    } else if (code == 0x02) {
                        System.out.println("계정 생성 실패!");
                    }
                    return;

                case Protocol.PT_SUBJECT_RESPONSE: //교과목 생성, 수정, 삭제 요청 응답
                    if (code == 0x00) {//승인시
                        protocol = new Protocol(Protocol.PT_SUBJECT_DATA_SEND,this.code);
                        protocol.setResult(msg);
                        os.write(protocol.getPacket());
                    }
                    break;

                case Protocol.PT_SUBJECT_RESULT: // 교과목 생성, 수정, 삭제 데이터 전송 결과
                    if (code == 1)
                        System.out.println("성공");
                    else
                        System.out.println("실패");

                    return;

                case Protocol.PT_SYLLABUS_INPUT_TERM_SETTING_RESPONSE: //강의 계획서 입력 기간 설정 요청 응답
                    protocol =new Protocol(Protocol.PT_SYLLABUS_INPUT_TERM_SETTING_DATA_SEND, this.code);
                    protocol.setResult(msg);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_SYLLABUS_INPUT_TERM_SETTING_RESULT:// 강의 계획서 입력 기간 설정 데이터 전송 결과
                    if (code == 0x01) {
                        System.out.println("강의계획서 기간 설정 성공!");
                    } else if (code == 0x02) {
                        System.out.println("강의계획서 기간 설정 실패!");
                    }
                    return;

                case Protocol.PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_RESPONSE:// 학년별 수강 신청 기간 설정 요청 응답

                    protocol = new Protocol(Protocol.PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_DATA_SEND, 0);
                    protocol.setResult(msg);
                    os.write(protocol.getPacket());

                    break;
                case Protocol.PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_RESULT:// 학년별 수강 신청 기간 설정 결과
                    if (code == 0x01) {
                        System.out.println("수강 신청 기간 설정 성공!");
                    } else if (code == 0x02) {
                        System.out.println("수강 신청 기간 설정 실패!");
                    }
                    return;


                case Protocol.PT_INFORMATION_LOOKUP_RESPONSE:// 교수, 학생 정보 조회 요청
                    protocol = new Protocol(Protocol.PT_INFORMATION_LOOKUP_DATA_SEND, this.code);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_INFORMATION_LOOKUP_RESULT:// 교수, 학생 정보 조회 결과
                    result = protocol.getResult().split("#");
                    try{
                        View.viewAll(result);
                    }
                    catch(Exception e){
                        for (String res : result)
                            System.out.println(res);
                    }
                    return;

                case Protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_RESPONSE:// 개설 교과목 정보 조회 응답
                    protocol = new Protocol(Protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_DATA_SEND, this.code);
                    protocol.setResult(msg);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_OPENED_SUBJECT_INFORMATION_LOOKUP_RESULT:// 개설 교과목 정보 조회 결과
                    if (code == 0x01) {
                        result = protocol.getResult().split("#");
                        try{
                            OpenedSubjectView.viewAll(result);
                        }
                        catch(Exception e){
                            for (String res : result)
                                System.out.println(res);
                        }
                    } else if (code == 0x02) {
                        System.out.println("개설 교과목 조회 실패!");
                    }
                    return;


                case Protocol.PT_PROF_INFORMATION_REVISE_RESPONSE: // 개인 정보 및 비밀번호 수정 요청 응답

                    protocol = new Protocol(Protocol.PT_PROF_INFORMATION_REVISE_DATA_SEND, this.code);
                    protocol.setResult(msg);
                    os.write(protocol.getPacket());
                    break;



                case Protocol.PT_PROF_INFORMATION_REVISE_RESULT:// 개인 정보 및 비밀번호 수정 결과
                    if (code == 0x01) {
                        System.out.println("(교수) 정보 수정 성공!");
                    } else if (code == 0x02) {
                        System.out.println("(교수) 정보 수정 실패!");
                    }
                    return;


                case Protocol.PT_SYLLABUS_INPUT_REVISE_RESPONSE: //강의 계획서 입력/수정 요청 응답
                    protocol = new Protocol(Protocol.PT_SYLLABUS_INPUT_REVISE_DATA_SEND, this.code);
                    protocol.setResult(msg);
                    os.write(protocol.getPacket());
                    break;


                case Protocol.PT_SYLLABUS_INPUT_REVISE_RESULT: //강의 계획서 입력/수정 결과
                    if (code == 0x01) {
                        System.out.println("강의계획서 입력/수정 성공!");
                    } else if (code == 0x02) {
                        System.out.println("강의계획서 입력/수정 실패!");
                    }
                    return;

                case Protocol.PT_SUBJECT_LIST_LOOKUP_RESPONSE: // 교과목 목록 조회 요청 응답

                    protocol = new Protocol(Protocol.PT_SUBJECT_LIST_LOOKUP_DATA_SEND, this.code);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_SUBJECT_LIST_LOOKUP_RESULT: // 교과목 목록 조회 결과

                    if (code == 0x01) {
                        System.out.println("교과목 조회 성공!");

                        result = protocol.getResult().split("#");
                        for (String res : result)
                            System.out.println(res);
                        return;

                    } else if (code == 0x02) {
                        System.out.println("교과목 조회 실패!");
                    }
                    return;

                case Protocol.PT_SUBJECT_SYLLABUS_LOOKUP_RESPONSE: // 교과목 강의 계획서 조회 요청 응답
                    protocol = new Protocol(Protocol.PT_SUBJECT_SYLLABUS_LOOKUP_DATA_SEND, 0);
                    protocol.setResult(msg);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_SUBJECT_SYLLABUS_LOOKUP_RESULT: // 교과목 강의 계획서 조회 결과

                    if (code == 0x01) {
                        System.out.println("강의 계획서 조회 성공!");

                        result = protocol.getResult().split("#");
                        for (String res : result)
                            System.out.println(res);

                    } else if (code == 0x02) {
                        System.out.println("강의 계획서 조회 실패!");
                    }
                    return;

                case Protocol.PT_SUBJECT_SIGN_UP_STUDENT_LIST_RESPONSE:  // 교과목 수강 신청 학생 목록 요청 응답

                    protocol = new Protocol(Protocol.PT_SUBJECT_SIGN_UP_STUDENT_LIST_DATA_SEND, 0);
                    protocol.setResult(msg);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_SUBJECT_SIGN_UP_STUDENT_LIST_RESULT:  // 교과목 수강 신청 학생 목록 요청 결과
                    if (code == 0x01) {
                        System.out.println("수강 신청 목록 조회 성공!");
                        result = protocol.getResult().split("#");
                        for (String res : result)
                            System.out.println(res);
                        return;
                    } else if (code == 0x02) {
                        System.out.println("수강 신청 목록 조회 실패!");
                    }
                    return;

                case Protocol.PT_SUBJECT_SCHEDULE_LOOKUP_RESPONSE: // 교과목 시간표 조회 요청 응답

                    protocol = new Protocol(Protocol.PT_SUBJECT_SCHEDULE_LOOKUP_DATA_SEND, 0);
                    protocol.setResult(msg);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_SUBJECT_SCHEDULE_LOOKUP_RESULT: // 교과목 시간표 조회 요청 결과

                    if (code == 0x01) {
                        System.out.println("시간표 조회 성공!");
                        result = protocol.getResult().split("#");
                        for(String res:result){
                            System.out.println(res);
                        }

                    } else if (code == 0x02) {
                        System.out.println("시간표 조회 실패!");
                    }

                    return;

                case Protocol.PT_STUDENT_INFORMATION_REVISE_RESPONSE: // 개인 정보 및 비밀번호 수정 요청 응답(학생)
                    protocol = new Protocol(Protocol.PT_STUDENT_INFORMATION_REVISE_DATA_SEND, this.code);
                    protocol.setResult(msg);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_STUDENT_INFORMATION_REVISE_RESULT: // 개인 정보 및 비밀번호 수정 요청 결과(학생)

                    if (code == 0x01) {
                        System.out.println("(학생) 정보 수정 성공!");
                    } else if (code == 0x02) {
                        System.out.println("(학생) 정보 수정 실패!");
                    }
                    return;

                case Protocol.PT_SIGN_UP_OR_CANCEL_RESPONSE: // 수강 신청/취소 요청 응답
                    protocol = new Protocol(Protocol.PT_SIGN_UP_OR_CANCEL_DATA_SEND, this.code);
                    protocol.setResult(msg);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_SIGN_UP_OR_CANCEL_RESULT: // 수강 신청/취소 요청 결과

                    if (code == 0x01) {
                        System.out.println("수강 신청/취소 성공!");
                    } else if (code == 0x02) {
                        System.out.println("수강 신청/취소 실패!");
                    }
                    return;

                case Protocol.PT_ALL_OPENED_SUBJECT_LIST_LOOKUP_DATA_SEND: // 개설 교과목 목록 데이터 전송
                    result = protocol.getResult().split("#");
                    try{
                        OpenedSubjectView.viewAll(result);
                    }
                    catch (Exception e){
                    }
                    return;


                case Protocol.PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_RESPONSE: // 선택 교과목 강의 계획서 조회 요청 응답
                    protocol = new Protocol(Protocol.PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_DATA_SEND, this.code);
                    protocol.setResult(msg);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_RESULT: // 선택 교과목 강의 계획서 조회 결과
                    if (code == 0x01) {
                        String res = protocol.getResult();
                        System.out.println("교과목 강의계획서 조회 성공!");
                        if (res.equals(""))
                            System.out.println("내용 없음");
                        else
                            System.out.println(res);
                    } else if (code == 0x02) {
                        System.out.println("교과목 강의계획서 조회 실패!");
                    }
                    return;

                case Protocol.PT_MY_SCHEDULE_LOOKUP_DATA_SEND: // 본인 시간표 조회 데이터 전송
                    result = protocol.getResult().split("#");
                    System.out.println("내 시간표 정보");
                    for (String res : result){
                        String[] needTime = res.split(" ");
                        String time = needTime[needTime.length-1];
                        ConvertTimeLine convertTimeLine = new ConvertTimeLine(time);
                        System.out.print(res); // Main result
                        System.out.println(" (" + convertTimeLine.getTimeLine() + ")"); // add Time convert to korean
                    }
                    return ;

                case Protocol.PT_OPENED_SUBJECT_CRD_RESPONSE:  // 개설 교과목 생성, 수정, 삭제 요청 응답
                    protocol = new Protocol(Protocol.PT_OPENED_SUBJECT_CRD_DATA_SEND, this.code);
                    protocol.setResult(msg);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_OPENED_SUBJECT_CRD_RESULT: // 개설 교과목 생성, 수정, 삭제 결과
                    if (code == 0x01)
                        System.out.println("성공!");
                    else if (code == 0x02)
                        System.out.println("실패!");
                    return;

                case Protocol.PT_SIGN_UP_STUDENT_LIST_RESPONSE: // 수강 신청 학생 목록 조회 응답
                    protocol = new Protocol(Protocol.PT_SIGN_UP_STUDENT_LIST_DATA_SEND, this.code);
                    protocol.setResult(msg);
                    os.write(protocol.getPacket());
                    break;

                case Protocol.PT_SIGN_UP_STUDENT_LIST_RESULT: // 수강 신청 학생 목록 조회 결과 전송
                    if (code == 0x01){
                        int input;
                        int pageInDataLength = 2;
                        result = protocol.getResult().split("#");
                        int maxCount = (int)Math.ceil((double)result.length/pageInDataLength); // calculate max page index
                        while(true){
                            System.out.printf("%50s검색을 원하시는 페이지를 입력하세요(최대 페이지 : " + maxCount + ", EXIT : -1) : ","");
                            input = s.nextInt();
                            if (input == -1) // EXIT
                                break;
                            else if (input <= 0 || input > maxCount) // 잘못된 값 입력
                                continue;
                            else
                                SignUpView.viewAll(SignUpView.getPages(result, input, pageInDataLength));
                        }
                    }
                    else if (code == 0x02){
                        System.out.println("데이터가 존재하지 않습니다.");
                    }

                    return;
            }
        }
    }
}
