package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class KeyboardTest {
	public static void main(String[] args) {

		BufferedReader br = null;
		try {
			// 기반 스트림 (표준입력 , 키보드, System.in)

			// 보조스트림
			// byte|byte|byte -> char
			InputStreamReader isr = new InputStreamReader(System.in, "utf-8");

			// 보조스트림2 - 버퍼에 담아뒀다가 엔터치면 버퍼에 저장되어있던 문자열반환해줌
			// char1|char2|char3|\n -> "char1char2char3"
			br = new BufferedReader(isr);

			// read
			String line = null;
			while ((line = br.readLine()) != null) {
				if ("exit".equals(line))
					break;
				System.out.println(">>" + line);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) // InputStreamReader에서 예외발생시 br을 열리지 않았기때문에 null임
								// 그 상태에서 close하면 NullPointerException 발생
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
