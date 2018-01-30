package kmdm.beethoven.rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Converter extends Remote, Serializable{

	public static final String NAME = "converter";

	public String convertJsonToSequenceTest(String json) throws RemoteException;

    public String convertJsonToSequence(String json) throws RemoteException;
}
