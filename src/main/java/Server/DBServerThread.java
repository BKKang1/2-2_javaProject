package Server;

import lombok.SneakyThrows;
import persistence.dto.MemberDTO;

import java.net.*;
import java.io.*;

public class DBServerThread extends Thread {
    private DBServer server = null;
    private Socket socket = null;
    private int ID = -1;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    private Protocol protocol;
    protected MemberDTO user;
    private int IDData;

    public DBServerThread(DBServer _server, Socket _socket) {
        super();
        server = _server;
        socket = _socket;
        ID     = socket.getPort();
    }

    public int getID() {
        return ID;
    }

    @SneakyThrows
    public synchronized void run() {
        Protocol protocol;
        int packetType;
        int code;
        System.out.println("Server Thread " + ID + " running.");
        protocol = new Protocol(Protocol.PT_ID_REQUEST, 0);
        server.selectPT(ID, protocol);
        while (true) {
            try {
                protocol = new Protocol();            // 새 etc.Protocol 객체 생성 (기본 생성자)
                byte[] buf = protocol.getPacket();    // 기본 생성자로 생성할 때에는 바이트 배열의 길이가 1000바이트로 지정됨
                streamIn.read(buf, 0, 6);            // 클라이언트로부터 정보 수신 (헤더부분만)
                packetType = buf[0];            // 수신 데이터에서 패킷 타입 얻음
                code = buf[1];                    // 수신 데이터에서 코드 얻음
                int length = protocol.getPacketLength(buf, 0); // 패킷의 가변길이 얻어옴
                buf = new byte[length];
                streamIn.read(buf);
                protocol = new Protocol(packetType, code);
                protocol.setPacket(packetType, code, buf);    // 패킷 설정
                if (packetType == Protocol.PT_EXIT)
                    break;
                server.selectPT(ID, protocol);
            } catch (Exception e) {
                break;
            }
        }
    }

    public void send(String msg, int type, int code){
        try {
            Protocol protocol = new Protocol(type, code);
            protocol.setResult(msg);
            streamOut.write(protocol.getPacket());
            streamOut.flush();
        } catch(IOException ioe) {
            System.out.println(ID + " ERROR sending: " + ioe.getMessage());
            server.remove(ID);
            stop();
        }
    }

    public void send(int type, int code){
        try {
            Protocol protocol = new Protocol(type, code);
            streamOut.write(protocol.getPacket());
            streamOut.flush();
        } catch(IOException ioe) {
            System.out.println(ID + " ERROR sending: " + ioe.getMessage());
            server.remove(ID);
            stop();
        }
    }

    public void open() throws IOException {
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void close() throws IOException {
        if (socket != null)    socket.close();
        if (streamIn != null)  streamIn.close();
        if (streamOut != null) streamOut.close();
    }

    public void setIDData(int IDData){
        this.IDData = IDData;
    }

    public int getIDData(){
        return IDData;
    }
}