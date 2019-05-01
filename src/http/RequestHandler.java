package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread {

	private static final String DOCUMENT_ROOT = "./webapp"; // document_root("/"랑 매핑됨)는 사이트의 최상위 경로이다.
	private Socket socket;
	public RequestHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {

			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			consoleLog("connected from " + inetSocketAddress.getAddress().getHostAddress() + ":"
					+ inetSocketAddress.getPort());

			// get IOStream
			OutputStream os = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));

			String request = null;
			while (true) {

				String line = br.readLine();

				// 브라우저가 연결을 끊으면 ..
				if (line == null) {
					break;
				}

				// Request Header만 읽음
				if ("".equals(line)) {
					break;
				}

				// Header의 첫번째 라인만 처리
				if (request == null) {
					request = line;
				}

				// consoleLog("received: " + line);
			}

			consoleLog("received: " + request);
			String[] tokens = request.split(" ");
			if ("GET".contentEquals(tokens[0])) { // GET
				consoleLog("request" + tokens[1]);
				responseStaticResource(os, tokens[1], tokens[2]);

			} else { // POST, PUT, DELETE, HEAD, CONNECT // 와 같은 Method는 무시
				consoleLog("Bad Request" + tokens[1]);

				response400Error(os, tokens[2]);
				/*
				 * 응답예시 HTTP/1.1 400 Bad Request\r\n -리눅스에서는 rn 윈도우에서는 n 
				 * Content-Type:text/html;
				 * charset=utf-8\r\n
				 * \r\n
				 *  HTML 에러 문서(./webapp/error/400.html)
				 */


			}

			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
			// os.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
			// os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
			// os.write( "\r\n".getBytes() ); //header 부분과 body 부분을 나눔
			// os.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된것입니다.</h1>".getBytes( "UTF-8" ) );

		} catch (Exception ex) {
			consoleLog("error:" + ex);
		} finally {
			// clean-up
			try {
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}

			} catch (IOException ex) {
				consoleLog("error:" + ex);
			}
		}
	}



	public void consoleLog(String message) {
		System.out.println("[RequestHandler#" + getId() + "] " + message);
	}

	public void responseStaticResource(OutputStream os, String url, String protocol) throws IOException {
		if ("/".equals(url)) {
			url = "/index.html"; // 상용적인 WAS는 이 내용을 설정파일에 설정 후 읽어와서 처리함
		}
		// 예측하지 못한 경우에만 예외처리를 해주는 것이좋다. 최대한 안쓰면서 조건문 등의 코드로 처리하는게 좋음
		File file = new File(DOCUMENT_ROOT + url);
		if (file.exists() == false) {
			response404Error(os, protocol);
			/*
			 * 응답예시 HTTP/1.1 404 File Not Found\r\n -리눅스에서는 rn 윈도우에서는 n
			 * Content-Type:text/html; charset=utf-8\r\n \r\n HTML 에러
			 * 문서(./webapp/error/404.html)
			 */
			return;
		}

		// nio
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());

		// 응답
		String message = "200 OK";
		outputStreamWrite(os, protocol, message, contentType, body );
	}

	private void response404Error(OutputStream os, String protocol) throws IOException {
		File file = new File(DOCUMENT_ROOT+"/error/404.html");
		
		byte[] body = null;
		String contentType = null;
		if(file.exists()) {
			body = Files.readAllBytes(file.toPath());
			contentType = Files.probeContentType(file.toPath());
		}
		// 응답
		String message = "404 File Not Found";
		outputStreamWrite(os, protocol, message, contentType, body );
	}
	
	private void response400Error(OutputStream os, String protocol) throws IOException {

		File file = new File(DOCUMENT_ROOT+"/error/400.html");
		byte[] body = null;
		String contentType = null;
		if(file.exists()) {
			body = Files.readAllBytes(file.toPath());
			contentType = Files.probeContentType(file.toPath());
		}
		// 응답
		String message = "400 Bad Request";

		outputStreamWrite(os, protocol, message, contentType, body );
	}

	private void outputStreamWrite(OutputStream os, String protocol, String message, String contentType, byte [] body) throws UnsupportedEncodingException, IOException {

		os.write((protocol +" "+ message+"\r\n").getBytes("UTF-8"));
		os.write(("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes("UTF-8"));
		os.write("\r\n".getBytes()); // header 부분과 body 부분을 나눔
		os.write(body);
	}
}
