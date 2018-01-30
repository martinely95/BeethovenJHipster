package kmdm.beethoven.rmi.server;

import kmdm.beethoven.rmi.Converter;
import kmdm.beethoven.rmi.server.impl.ConverterImpl;

import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConverterServer {
	public static void main(String args[]) {
		try {
            String jvmName = ManagementFactory.getRuntimeMXBean().getName();
            System.out.println("==== JVM Server:" + jvmName);

			System.out.println("creating converter");
			ConverterImpl converter = new ConverterImpl();

			System.out.println("locating local RMI Registry");
			Registry registry = LocateRegistry.getRegistry();

			System.out.println("registering converter with RMI Registry");
			registry.rebind(Converter.NAME, converter);
			System.out.println("*********************************");
			System.out.println("* waiting for client requests...*");
			System.out.println("*** ******************************");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
