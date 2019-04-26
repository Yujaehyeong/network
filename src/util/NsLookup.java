package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NsLookup {
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		String url = "";
		try {
			while (true) {
				System.out.print("> ");
				url = sc.next();
				if(url.equals("exit")) break;
				InetAddress[] inetAddresses = InetAddress.getAllByName(url);
				for (InetAddress i : inetAddresses) {
					System.out.println(url + " : " + i.getHostAddress());
				}
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
