package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Localhost {

	public static void main(String[] args) {
		try {
			
			InetAddress inetAddress = InetAddress.getLocalHost();
			String hostName = inetAddress.getHostName();
			String hostAddress = inetAddress.getHostAddress();

			byte[] addresses = inetAddress.getAddress();
			for (byte address : addresses) {
				System.out.print(address & 0x000000ff);				
				System.out.print(".");
			}

		} catch (UnknownHostException e) {

			e.printStackTrace();

		}

	}

}
