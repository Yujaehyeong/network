package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class NsLookup {

	private String url;
	private ArrayList<String> inetAddressList;

	public NsLookup() {
		inetAddressList = new ArrayList<String>();
	}

	public void setHostAddress(String url) {
		InetAddress[] inetAddresses = null;
		try {
			inetAddresses = InetAddress.getAllByName(url);
			this.url = url;

			for (InetAddress i : inetAddresses) {
				inetAddressList.add(i.getHostAddress());
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public boolean isExit(String url) {
		boolean result = true;
		if (!url.equals("exit")) {
			setHostAddress(url);
			result = false;
		}
		return result;
	}

	public void printHostAddress() {
		if (inetAddressList.size() != 0) {
			for (String hostName : inetAddressList) {
				System.out.println(this.url + " : " + hostName);
			}
		}
		inetAddressList.clear();
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		NsLookup nslookup = new NsLookup();
		while (true) {
			System.out.print("> ");
			String url = sc.next();
			if (nslookup.isExit(url))
				break;
			nslookup.printHostAddress();
		}
	}
}
