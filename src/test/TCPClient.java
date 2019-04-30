package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClient {
	private static final String SERVER_IP = "192.168.1.20";
	private static final int SERVER_PORT = 5000;
	
	public static void main(String[] args) {
		Socket socket = null;
		try {
			// 1. 소켓 생성
			socket = new Socket();
			
			// 1-1. 소켓 버퍼사이즈 확인
			int receiveBufferSize = socket.getReceiveBufferSize();
			int sendBufferSize =  socket.getSendBufferSize();
			System.out.println(receiveBufferSize + ":"+ sendBufferSize);
			
			// 1-2. 소켓버퍼사이즈 변경 - 버퍼사이즈가 변경된다고해도 차이가 없다.
			socket.setReceiveBufferSize(1024*10);
			socket.setSendBufferSize(1024*10);
			
			receiveBufferSize = socket.getReceiveBufferSize();
			sendBufferSize =  socket.getSendBufferSize();
			System.out.println(receiveBufferSize + ":"+ sendBufferSize);
			
			// 1-3. SO_NODELAY(Nagle Algorithm off)
			socket.setTcpNoDelay(true);
			// 1-4. SO_TIMEOUT
			socket.setSoTimeout(1000);
			
			// 2. 서버연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[client connected]");

			// 3. IOStream 받아오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			// 4. 쓰기
			String data = "Hello world\n";
			os.write(data.getBytes("utf-8"));
			
			// 5. 읽기
			byte buffer[] = new byte[256];
			int readByteCount = is.read(buffer); // blocking
			if (readByteCount == -1) {
				System.out.println("[client close by Server]");
			}
			
			data = new String(buffer, 0, readByteCount, "utf-8");
			System.out.println("[client] received: " + data);

		} catch (SocketTimeoutException e) {
			System.out.println("[client] time out");
		}catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}
