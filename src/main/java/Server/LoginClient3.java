package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LoginClient3 {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		if(args.length < 2) System.out.println("사용법 : " + "주소 포트번호");

		Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();

		Protocol protocol = new Protocol();
		byte[] buf = protocol.getPacket();

		String data = "";
		String result;
		BufferedReader userIn;

		while(true){
			is.read(buf);
			int packetType = buf[0];
			int code = buf[1];
			protocol.setPacket(packetType,code,buf); // 첫 패킷 설정 (타입, 코드, 읽어들인 내용)
			System.out.println(packetType);

			if(packetType == Protocol.PT_EXIT){
				System.out.println("type : " + packetType + " code : " + code);
				System.out.println("클라이언트 종료");
				break;
			}

			switch(packetType){ // 타입 확인 -> 타입에 대한 코드 확인
				case Protocol.PT_START:
					System.out.println("connected");
					protocol = new Protocol(Protocol.PT_ACCOUNT_CREATE_REQUEST, 0);
					os.write(protocol.getPacket());
					break;
				case Protocol.PT_ACCOUNT_CREATE_RESPONSE: // Type 1: 단어, 정수 전송 가능 여부 관련
					switch(code) {
						case 0: // Type : 1 Code : 0, From Server
							System.out.println("Account Create");
							protocol = new Protocol(Protocol.PT_ACCOUNT_CREATE_DATA_SEND, 0); // 단어,정수 전송 요청 응답
							protocol.setResult("client3");
							os.write(protocol.getPacket());
							break;
					}
			}
		}
		//os.close();
		//is.close();
		//socket.close();
	}
}


