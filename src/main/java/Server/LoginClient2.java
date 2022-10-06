package Server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class LoginClient2 {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		if(args.length < 2) System.out.println("사용법 : " + "주소 포트번호");

		Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();

		Protocol protocol = new Protocol();

		String data = "";
		String result;
		BufferedReader userIn;
		boolean start = true;

		while(true){
			protocol = new Protocol();			// 새 etc.Protocol 객체 생성 (기본 생성자)
			byte[] buf = protocol.getPacket();	// 기본 생성자로 생성할 때에는 바이트 배열의 길이가 1000바이트로 지정됨
			is.read(buf, 0, 6);			// 클라이언트로부터 정보 수신 (헤더부분만)
			int packetType = buf[0];			// 수신 데이터에서 패킷 타입 얻음
			int code = buf[1];					// 수신 데이터에서 코드 얻음
			int length = protocol.getPacketLength(buf, 0); // 패킷의 가변길이 얻어옴
			buf = new byte[length];
			is.read(buf);
			protocol = new Protocol(packetType, code);
			protocol.setPacket(packetType,code,buf);	// 패킷 설정
			if (start){
				protocol = new Protocol(Protocol.PT_START, 0);
			}

			if(packetType == Protocol.PT_EXIT){
				System.out.println("type : " + packetType + " code : " + code);
				System.out.println("클라이언트 종료");
				break;
			}

			switch(packetType) { // 타입 확인
				case Protocol.PT_START:
					System.out.println("connected");
					protocol = new Protocol(Protocol.PT_INFORMATION_LOOKUP_REQUEST, 0);
					os.write(protocol.getPacket());
					break;
				case Protocol.PT_INFORMATION_LOOKUP_RESPONSE: // Type 1: 단어, 정수 전송 가능 여부 관련
					switch (code) {
						case 0: // Type : 1 Code : 0, From Server
							//System.out.println("Received PT_INFORMATION_LOOKUP_RESPONSE");
							protocol = new Protocol(Protocol.PT_INFORMATION_LOOKUP_DATA_SEND, 2); // 단어,정수 전송 요청 응답
							//protocol.setResult("1");
							//System.out.println("Send PT_INFORMATION_LOOKUP_DATA_SEND (Code : " + 2 + ")");
							os.write(protocol.getPacket());
							break;
					}
					break;
				case Protocol.PT_INFORMATION_LOOKUP_RESULT:
					//System.out.println("Received PT_INFORMATION_LOOKUP_RESULT");
					String[] resultSet = protocol.getResult().split("#");
					for (int i = 0; i < resultSet.length; i++)
						System.out.println(resultSet[i]);
					Scanner scanner = new Scanner(System.in);
					//protocol = new Protocol(Protocol.PT_EXIT, 0);
					//os.write(protocol.getPacket());
					if (scanner.nextInt() == 3){
						protocol = new Protocol(Protocol.PT_INFORMATION_LOOKUP_REQUEST, 0);
						os.write(protocol.getPacket());
						/*
						socket = new Socket(args[0], Integer.parseInt(args[1]));
						os = socket.getOutputStream();
						is = socket.getInputStream();
						 */
					}
					break;
			}
		}
		//os.close();
		//is.close();
		//socket.close();
	}
}


