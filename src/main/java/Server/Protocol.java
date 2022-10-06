package Server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Protocol {
	//프로토콜 타입에 관한 변수
	public static final int PT_UNDEFINED = -1;			// 프로토콜이 지정되어 있지 않은 경우
	public static final int PT_START = -2;
	public static final int PT_EXIT = 0;				// 프로그램 종료
	public static final int PT_ID_REQUEST = 0x01; // ID 요청
	public static final int PT_ID_SEND = 0x02; // ID 전송
	public static final int PT_PWD_REQUEST = 0x03; // PWD 요청
	public static final int PT_PWD_SEND = 0x04; // PWD 전송
	public static final int PT_LOGIN_RESULT = 0x05; // 로그인 결과
	public static final int PT_ACCOUNT_CREATE_REQUEST = 0x10; // 교수, 학생 계정 생성 요청
	public static final int PT_ACCOUNT_CREATE_RESPONSE = 0x11; // 교수, 학생 계정 생성 요청 응답
	public static final int PT_ACCOUNT_CREATE_DATA_SEND = 0x12; // 교수, 학생 계정 생성 데이터 전송
	public static final int PT_ACCOUNT_CREATE_RESULT = 0x13; // 교수, 학생 계정 생성 데이터 전송 결과
	public static final int PT_SUBJECT_REQUEST = 0x14; // 교과목 생성, 수정, 삭제 요청
	public static final int PT_SUBJECT_RESPONSE = 0x15; // 교과목 생성, 수정, 삭제 요청 응답
	public static final int PT_SUBJECT_DATA_SEND = 0x16; // 교과목 생성, 수정, 삭제 데이터 전송
	public static final int PT_SUBJECT_RESULT = 0x17; // 과고목 생성, 수정, 삭제 데이터 전송 결과
	public static final int PT_SYLLABUS_INPUT_TERM_SETTING_REQUEST = 0x18; // 강의 계획서 입력 기간 설정 요청
	public static final int PT_SYLLABUS_INPUT_TERM_SETTING_RESPONSE = 0x19; // 강의 계획서 입력 기간 설정 요청 응답
	public static final int PT_SYLLABUS_INPUT_TERM_SETTING_DATA_SEND = 0x1A; // 강의 계획서 입력 기간 설정 데이터 전송
	public static final int PT_SYLLABUS_INPUT_TERM_SETTING_RESULT = 0x1B; // 강의 계획서 입력 기간 설정 데이터 전송 결과
	public static final int PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_REQUEST = 0x1C; // 학년별 수강 신청 기간 설정 요청
	public static final int PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_RESPONSE = 0X1D; // 학년별 수강 신청 기간 설정 요청 응답
	public static final int PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_DATA_SEND = 0x1E; // 학년별 수강 신청 기간 설정 데이터 전송
	public static final int PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_RESULT = 0x1F; // 학년별 수강 신청 기간 설정 결과
	public static final int PT_INFORMATION_LOOKUP_REQUEST = 0x20; // 교수, 학생 정보 조회 요청
	public static final int PT_INFORMATION_LOOKUP_RESPONSE = 0x21; // 교수, 학생 정보 조회 응답
	public static final int PT_INFORMATION_LOOKUP_DATA_SEND = 0x22; // 교수, 학생 정보 조회 데이터 전송
	public static final int PT_INFORMATION_LOOKUP_RESULT = 0x23; // 교수, 학생 정보 조회 결과
	public static final int PT_OPENED_SUBJECT_INFORMATION_LOOKUP_REQUEST = 0x24; // 개설 교과목 정보 조회 요청
	public static final int PT_OPENED_SUBJECT_INFORMATION_LOOKUP_RESPONSE = 0x25; // 개설 교과목 정보 조회 응답
	public static final int PT_OPENED_SUBJECT_INFORMATION_LOOKUP_DATA_SEND = 0x26; // 개설 교과목 정보 조회 데이터 전송
	public static final int PT_OPENED_SUBJECT_INFORMATION_LOOKUP_RESULT = 0x27; // 개설 교과목 정보 조회 결과
	public static final int PT_PROF_INFORMATION_REVISE_REQUEST = 0x28; // 개인 정보 및 비밀번호 수정 요청
	public static final int PT_PROF_INFORMATION_REVISE_RESPONSE = 0x29; // 개인 정보 및 비밀번호 수정 요청 응답
	public static final int PT_PROF_INFORMATION_REVISE_DATA_SEND = 0x2A; // 개인 정보 및 비밀번호 수정 데이터 전송
	public static final int PT_PROF_INFORMATION_REVISE_RESULT = 0x2B; // 개인 정보 및 비밀번호 수정 결과
	public static final int PT_SYLLABUS_INPUT_REVISE_REQUEST = 0x2C; //강의 계획서 입력/수정 요청
	public static final int PT_SYLLABUS_INPUT_REVISE_RESPONSE = 0x2D; //강의 계획서 입력/수정 요청 응답
	public static final int PT_SYLLABUS_INPUT_REVISE_DATA_SEND = 0x2E; //강의 계획서 입력/수정 요청 데이터 전송
	public static final int PT_SYLLABUS_INPUT_REVISE_RESULT = 0x2F; //강의 계획서 입력/수정 결과
	public static final int PT_SUBJECT_LIST_LOOKUP_REQUEST = 0x30; // 교과목 목록 조회 요청
	public static final int PT_SUBJECT_LIST_LOOKUP_RESPONSE = 0x31; // 교과목 목록 조회 요청 응답
	public static final int PT_SUBJECT_LIST_LOOKUP_DATA_SEND = 0x32; // 교과목 목록 조회 요청 데이터 전송
	public static final int PT_SUBJECT_LIST_LOOKUP_RESULT = 0x33; // 교과목 목록 조회 결과
	public static final int PT_SUBJECT_SYLLABUS_LOOKUP_REQUEST = 0x34; // 교과목 강의 계획서 조회 요청
	public static final int PT_SUBJECT_SYLLABUS_LOOKUP_RESPONSE = 0x35; // 교과목 강의 계획서 조회 요청 응답
	public static final int PT_SUBJECT_SYLLABUS_LOOKUP_DATA_SEND = 0x36; // 교과목 강의 계획서 조회 데이터 전송
	public static final int PT_SUBJECT_SYLLABUS_LOOKUP_RESULT = 0x37; // 교과목 강의 계획서 조회 결과
	public static final int PT_SUBJECT_SIGN_UP_STUDENT_LIST_REQUEST = 0x38; // 교과목 수강 신청 학생 목록 요청
	public static final int PT_SUBJECT_SIGN_UP_STUDENT_LIST_RESPONSE = 0x39; // 교과목 수강 신청 학생 목록 요청 응답
	public static final int PT_SUBJECT_SIGN_UP_STUDENT_LIST_DATA_SEND = 0x3A; // 교과목 수강 신청 학생 목록 요청 데이터 전송
	public static final int PT_SUBJECT_SIGN_UP_STUDENT_LIST_RESULT = 0x3B; // 교과목 수강 신청 학생 목록 요청 결과
	public static final int PT_SUBJECT_SCHEDULE_LOOKUP_REQUEST = 0x3C; // 교과목 시간표 조회 요청
	public static final int PT_SUBJECT_SCHEDULE_LOOKUP_RESPONSE = 0x3D; // 교과목 시간표 조회 요청 응답
	public static final int PT_SUBJECT_SCHEDULE_LOOKUP_DATA_SEND = 0x3E; // 교과목 시간표 조회 요청 데이터 전송
	public static final int PT_SUBJECT_SCHEDULE_LOOKUP_RESULT = 0x3F; // 교과목 시간표 조회 요청 결과
	public static final int PT_STUDENT_INFORMATION_REVISE_REQUEST = 0x40; // 개인 정보 및 비밀번호 수정 요청(학생)
	public static final int PT_STUDENT_INFORMATION_REVISE_RESPONSE = 0x41; // 개인 정보 및 비밀번호 수정 요청 응답(학생)
	public static final int PT_STUDENT_INFORMATION_REVISE_DATA_SEND = 0x42; // 개인 정보 및 비밀번호 수정 요청 데이터 전송(학생)
	public static final int PT_STUDENT_INFORMATION_REVISE_RESULT = 0x43; // 개인 정보 및 비밀번호 수정 요청 결과(학생)
	public static final int PT_SIGN_UP_OR_CANCEL_REQUEST = 0x44; // 수강 신청/취소 요청
	public static final int PT_SIGN_UP_OR_CANCEL_RESPONSE = 0x45; // 수강 신청/취소 요청 응답
	public static final int PT_SIGN_UP_OR_CANCEL_DATA_SEND = 0x46; // 수강 신청/취소 요청 데이터 전송
	public static final int PT_SIGN_UP_OR_CANCEL_RESULT = 0x47; // 수강 신청/취소 요청 결과
	public static final int PT_ALL_OPENED_SUBJECT_LIST_LOOKUP_REQUEST = 0x48; // 개설 교과목 목록 조회 요청(전과목)
	public static final int PT_ALL_OPENED_SUBJECT_LIST_LOOKUP_DATA_SEND = 0x49; // 개설 교과목 목록 데이터 전송
	public static final int PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_REQUEST = 0x4A; // 선택 교과목 강의 계획서 조회 요청
	public static final int PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_RESPONSE = 0x4B; // 선택 교과목 강의 계획서 조회 요청 응답
	public static final int PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_DATA_SEND = 0x4C; // 선택 교과목 강의 계획서 조회 데이터 전송
	public static final int PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_RESULT = 0x4D; // 선택 교과목 강의 계획서 조회 결과
	public static final int PT_MY_SCHEDULE_LOOKUP_REQUEST = 0x4E; // 본인 시간표 조회 요청
	public static final int PT_MY_SCHEDULE_LOOKUP_DATA_SEND = 0x4F; // 본인 시간표 조회 데이터 전송
	public static final int PT_OPENED_SUBJECT_CRD_REQUEST = 0x50; // 개설 교과목 생성, 수정, 삭제 요청
	public static final int PT_OPENED_SUBJECT_CRD_RESPONSE = 0x51; // 개설 교과목 생성, 수정, 삭제 요청 응답
	public static final int PT_OPENED_SUBJECT_CRD_DATA_SEND = 0x52; // 개설 교과목 생성, 수정, 삭제 데이터 전송
	public static final int PT_OPENED_SUBJECT_CRD_RESULT = 0x53; // 개설 교과목 생성, 수정, 삭제 결과
	public static final int PT_SIGN_UP_STUDENT_LIST_REQUEST = 0x54; // 수강 신청 학생 목록 조회
	public static final int PT_SIGN_UP_STUDENT_LIST_RESPONSE = 0x55; // 수강 신청 학생 목록 조회 응답
	public static final int PT_SIGN_UP_STUDENT_LIST_DATA_SEND = 0x56; // 수강 신청 학생 목록 조회 데이터 전송
	public static final int PT_SIGN_UP_STUDENT_LIST_RESULT = 0x57; // 수강 신청 학생 목록 조회 결과 전송
	public static final int LEN_PROTOCOL_TYPE=1;		// 프로토콜 타입 길이
	public static final int LEN_MAX = 1000;				//최대 데이터 길이
	public static final int LEN_CODE = 1;			    // 코드 정보가 담겨있는 길이
	public static final int LEN_PACKETLENGTH = 4;	    // 가변 정보가 담겨있는 길이
	protected int protocolType; // 프로토콜 타입
	protected int code; // 코드
	private byte[] packet;	// 프로토콜과 데이터의 저장공간이 되는 바이트 배열

	/***
	 기본 패킷 구성
	 [protocolType][code][packetLength][Contents]
	 ***/

	public Protocol(){					// 생성자
		this(PT_UNDEFINED, 0);
	}

	public Protocol(int protocolType, int code){	// 생성자
		this.protocolType = protocolType;
		this.code = code;
		packet = getPacket(protocolType, code);
	}

	// 프로토콜 타입에 따라 바이트 배열 packet의 length가 다름
	public byte[] getPacket(int protocolType, int code){
		if(packet==null){
			switch(protocolType){
				// 바디가 존재하지 않는 패킷
				case PT_SIGN_UP_STUDENT_LIST_RESPONSE:
				case PT_SIGN_UP_STUDENT_LIST_REQUEST:
				case PT_OPENED_SUBJECT_CRD_RESPONSE:
				case PT_OPENED_SUBJECT_CRD_REQUEST:
				case PT_MY_SCHEDULE_LOOKUP_REQUEST:
				case PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_RESPONSE:
				case PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_REQUEST:
				case PT_ALL_OPENED_SUBJECT_LIST_LOOKUP_REQUEST:
				case PT_SIGN_UP_OR_CANCEL_RESULT:
				case PT_SIGN_UP_OR_CANCEL_RESPONSE:
				case PT_SIGN_UP_OR_CANCEL_REQUEST:
				case PT_STUDENT_INFORMATION_REVISE_RESULT:
				case PT_STUDENT_INFORMATION_REVISE_RESPONSE:
				case PT_STUDENT_INFORMATION_REVISE_REQUEST:
				case PT_SUBJECT_SCHEDULE_LOOKUP_RESPONSE:
				case PT_SUBJECT_SCHEDULE_LOOKUP_REQUEST:
				case PT_SUBJECT_SIGN_UP_STUDENT_LIST_RESPONSE:
				case PT_SUBJECT_SIGN_UP_STUDENT_LIST_REQUEST:
				case PT_SUBJECT_SYLLABUS_LOOKUP_RESPONSE:
				case PT_SUBJECT_SYLLABUS_LOOKUP_REQUEST:
				case PT_SUBJECT_LIST_LOOKUP_RESPONSE:
				case PT_SUBJECT_LIST_LOOKUP_REQUEST:
				case PT_SYLLABUS_INPUT_REVISE_RESULT:
				case PT_SYLLABUS_INPUT_REVISE_RESPONSE:
				case PT_SYLLABUS_INPUT_REVISE_REQUEST:
				case PT_PROF_INFORMATION_REVISE_RESULT:
				case PT_PROF_INFORMATION_REVISE_RESPONSE:
				case PT_PROF_INFORMATION_REVISE_REQUEST:
				case PT_OPENED_SUBJECT_INFORMATION_LOOKUP_RESPONSE:
				case PT_OPENED_SUBJECT_INFORMATION_LOOKUP_REQUEST:
				case PT_INFORMATION_LOOKUP_RESPONSE:
				case PT_INFORMATION_LOOKUP_REQUEST:
				case PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_RESULT:
				case PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_RESPONSE:
				case PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_REQUEST:
				case PT_SYLLABUS_INPUT_TERM_SETTING_RESULT:
				case PT_SYLLABUS_INPUT_TERM_SETTING_RESPONSE:
				case PT_SYLLABUS_INPUT_TERM_SETTING_REQUEST:
				case PT_SUBJECT_RESULT:
				case PT_SUBJECT_RESPONSE:
				case PT_SUBJECT_REQUEST:
				case PT_ACCOUNT_CREATE_RESULT:
				case PT_ACCOUNT_CREATE_REQUEST:
				case PT_ACCOUNT_CREATE_RESPONSE:
				case PT_ID_REQUEST:
				case PT_PWD_REQUEST:
				case PT_LOGIN_RESULT:
				case PT_START:
					packet = new byte[LEN_PROTOCOL_TYPE + LEN_CODE];
					break;

				// 바디가 존재하는 패킷
				case PT_SIGN_UP_STUDENT_LIST_RESULT:
				case PT_SIGN_UP_STUDENT_LIST_DATA_SEND:
				case PT_OPENED_SUBJECT_CRD_RESULT:
				case PT_OPENED_SUBJECT_CRD_DATA_SEND:
				case PT_MY_SCHEDULE_LOOKUP_DATA_SEND:
				case PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_RESULT:
				case PT_SELECT_SUBJECT_SYLLABUS_LOOKUP_DATA_SEND:
				case PT_ALL_OPENED_SUBJECT_LIST_LOOKUP_DATA_SEND:
				case PT_SIGN_UP_OR_CANCEL_DATA_SEND:
				case PT_STUDENT_INFORMATION_REVISE_DATA_SEND:
				case PT_SUBJECT_SCHEDULE_LOOKUP_RESULT:
				case PT_SUBJECT_SCHEDULE_LOOKUP_DATA_SEND:
				case PT_SUBJECT_SIGN_UP_STUDENT_LIST_RESULT:
				case PT_SUBJECT_SIGN_UP_STUDENT_LIST_DATA_SEND:
				case PT_SUBJECT_SYLLABUS_LOOKUP_RESULT:
				case PT_SUBJECT_SYLLABUS_LOOKUP_DATA_SEND:
				case PT_SUBJECT_LIST_LOOKUP_RESULT:
				case PT_SUBJECT_LIST_LOOKUP_DATA_SEND:
				case PT_SYLLABUS_INPUT_REVISE_DATA_SEND:
				case PT_PROF_INFORMATION_REVISE_DATA_SEND:
				case PT_OPENED_SUBJECT_INFORMATION_LOOKUP_RESULT:
				case PT_OPENED_SUBJECT_INFORMATION_LOOKUP_DATA_SEND:
				case PT_INFORMATION_LOOKUP_RESULT:
				case PT_INFORMATION_LOOKUP_DATA_SEND:
				case PT_SIGN_UP_CLASS_TERM_SETTING_BY_GRADE_DATA_SEND:
				case PT_SYLLABUS_INPUT_TERM_SETTING_DATA_SEND:
				case PT_SUBJECT_DATA_SEND:
				case PT_ACCOUNT_CREATE_DATA_SEND:
				case PT_ID_SEND:
				case PT_PWD_SEND:
					packet = new byte[LEN_PROTOCOL_TYPE + LEN_CODE + LEN_PACKETLENGTH];
					break;

				case PT_UNDEFINED:
					packet = new byte[LEN_MAX];
					break;
				case PT_EXIT:
					switch(code){
						case 0:
							packet = new byte[LEN_PROTOCOL_TYPE + LEN_CODE];
							break;
					}
					break;
			} // end switch
		} // end if
		packet[0] = (byte)protocolType;	// packet 바이트 배열의 첫 번째 바이트에 프로토콜 타입 설정
		packet[1] = (byte)code; // packet 바이트 배열의 두 번째 바이트에 코드 설정
		return packet;
	}

	public void setResult(String result){ // result
		int resultLength = 0; // result String 원소 전체 길이의 합
		int header = LEN_PROTOCOL_TYPE + LEN_CODE + LEN_PACKETLENGTH; // 헤더 정보
		resultLength += result.getBytes().length;
		byte[] cLength = intToByte(resultLength, ByteOrder.BIG_ENDIAN);
		System.arraycopy(cLength, 0, packet, LEN_PROTOCOL_TYPE + LEN_CODE, cLength.length); // 가변길이를 헤더에 추가
		byte[] varLength = new byte[packet.length + resultLength]; // 고정길이 + 가변길이(result 데이터)
		for (int i = 0; i < packet.length; i++){
			varLength[i] = packet[i]; // 기존 packet 정보 저장
		}
		packet = varLength; // 새롭게 길이가 배정된 배열을 packet에 저장

		System.arraycopy(result.getBytes(), 0, packet, header, result.getBytes().length);
		// header += result.getBytes().length;
	}

	public String getResult(){
		if (packet.length < LEN_PROTOCOL_TYPE + LEN_CODE + LEN_PACKETLENGTH)
			return "";
		return new String(packet, LEN_PROTOCOL_TYPE + LEN_CODE + LEN_PACKETLENGTH, packet.length - LEN_PROTOCOL_TYPE - LEN_CODE - LEN_PACKETLENGTH); // contents 출력
	}

	public int getType(){
		return protocolType;
	}

	public int getCode(){
		return code;
	}

	// Default 생성자로 생성한 후 etc.Protocol 클래스의 packet 데이터를 바꾸기 위한 메서드
	public void setPacket(int pt, int code, byte[] buf){
		packet = null;
		if (buf.length == 0){ // UNDEFINED 이거나 BODY 가 없는 경우 헤더부분만 생성
			packet = getPacket(pt, code);
			return;
		}
		else if (buf.length == 1000){
			packet = getPacket(pt, code);
			return;
		}
		packet = getPacket(pt, code); // 패킷 헤더 설정
		protocolType = pt;
		this.code = code;
		byte[] varLength = new byte[packet.length + buf.length]; // 고정길이 + 가변길이(가변 데이터)
		packet = varLength; // 변형된 길이를 패킷에 적용
		packet[0] = (byte)pt; // 패킷의 0번과 1번에 위치할 type 과 코드 재적용
		packet[1] = (byte)this.code;
		System.arraycopy(buf, 0, packet, LEN_PROTOCOL_TYPE+LEN_CODE+LEN_PACKETLENGTH, buf.length); // 패킷의 길이만큼 buf에서 읽어들임
	}

	public byte[] getPacket(){
		return packet;
	}

	public int getPacketLength(byte[] buf, int code){ // 외부에서 사용하는 용도(코드는 아무거나 사용해도 무관)
		byte[] lengthInfo = new byte[4];
		lengthInfo[0] = buf[2];
		lengthInfo[1] = buf[3];
		lengthInfo[2] = buf[4];
		lengthInfo[3] = buf[5];
		return byteToInt(lengthInfo, ByteOrder.BIG_ENDIAN);
	}

	public int getPacketLength(byte[] buf){ // 길이 정보 바이트 배열 위치에서 길이를 출력 (형식 : 빅엔디안)
		byte[] lengthInfo = new byte[4];
		lengthInfo[0] = buf[LEN_PROTOCOL_TYPE + LEN_CODE + 0];
		lengthInfo[1] = buf[LEN_PROTOCOL_TYPE + LEN_CODE + 1];
		lengthInfo[2] = buf[LEN_PROTOCOL_TYPE + LEN_CODE + 2];
		lengthInfo[3] = buf[LEN_PROTOCOL_TYPE + LEN_CODE + 3];
		return byteToInt(lengthInfo, ByteOrder.BIG_ENDIAN);
	}

	public byte[] intToByte(int integer, ByteOrder order) {
		ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE/8);
		buff.order(order);
		buff.putInt(integer);
		return buff.array();
	}

	public int byteToInt(byte[] bytes, ByteOrder order) {
		ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE/8);
		buff.order(order);
		buff.put(bytes);
		buff.flip();
		return buff.getInt();
	}

}
