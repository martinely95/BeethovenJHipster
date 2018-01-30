package kmdm.beethoven.rmi.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import kmdm.beethoven.rmi.Converter;

public class ConverterClient {
	public static void main(String args[]) {

		String host = "localhost";

		try {
			System.out.println("locating RMI Registry");
			Registry registry = LocateRegistry.getRegistry(host);

			System.out.println("looking up object: " + Converter.NAME);
			Converter checker = (Converter) registry.lookup(Converter.NAME);
//			String a = "{'black':[[false,false,false,false,false,false,false,false,false,false,false,false,false,false,false],[false,false,false,false,false,false,false,false,false,false,false,false,false,false,false],[false,false,false,false,false,true,true,true,false,false,false,false,true,false,false],[false,false,false,false,false,true,false,true,true,true,true,true,true,false,false],[false,false,false,false,false,false,false,false,false,false,false,false,false,false,false],[false,false,false,false,false,false,false,false,false,false,false,false,false,false,false]]}";
//            System.out.println("Path:" + checker.convertJsonToSequence(a));
			System.out.println("***********************************************");
			System.out.println("* invoking the remote method with a parameter *");
			System.out.println("***********************************************");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
