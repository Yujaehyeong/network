package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class EchoClient {
	private static final String SERVER_IP = "192.168.1.20";
	private static final int SERVER_PORT = 7000;

	public static void main(String[] args) {
		Scanner scanner = null;
		Socket socket = null;
		try {
			// 1. Scanner 생성 (표준입력 연결)
			scanner = new Scanner(System.in);
			
			// 2. 소켓 생성
			socket = new Socket();
			
			
			// 3. 서버연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			log("connected");
			
			
			// 4. IOStream todtjd(받아오기)
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			// true를 넣어줌으로써 Auto flush 해줌
			PrintWriter pr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

			while (true) {
				// 5. 키보드 입력받기
				System.out.print(">> ");
				String line = scanner.nextLine();
				if ("quit".contentEquals(line)) {
					break;
				}

				// 6. 데이터 쓰기
				
				pr.println(line);
				System.out.println(new Date().toString());

				// 7. 데이터 읽기
				String data = br.readLine();
				if (data == null) {
					log("closed by server");
					break;
				}

				// 8. 콘솔 출력
				System.out.println("<< " + data);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (scanner != null) {
					scanner.close();
				}
				if (socket != null && !socket.isClosed()) {
					socket.close();
					log("Client Closed");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	public static void log(String log) {
		System.out.println("[client] " + log);
	}
}
