package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPEchoServer {
	// Data 유실이 있기 때문에 스트리밍 서비스등에 쓰인다.
	// 데이터패킷을 교환하는 형식으로 스레드를 사용하지 않아도 된다. 분산처리를 지원한다.
	public static final int PORT = 7000;
	public static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		DatagramSocket socket = null;
		try {
			//1. socket 생성
			socket = new DatagramSocket(PORT);

			while (true) {
				//2. 데이터 수신
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receivePacket);

				byte [] data = receivePacket.getData();
				int length = receivePacket.getLength(); 
				String message = new String(data, 0 , length, "UTF-8"); 

				System.out.println("[server] received: "+ message);

				//3. 데이터 전송
				byte [] sendData = message.getBytes("utf-8");
				DatagramPacket sendPacket = 
						new DatagramPacket(sendData, sendData.length, 
								receivePacket.getAddress(), 
								receivePacket.getPort());
				
				socket.send(sendPacket);

			}
		} catch (SocketException e) {
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(socket!=null && socket.isClosed() == false) {
				socket.close();
			}
		}

	}
}
