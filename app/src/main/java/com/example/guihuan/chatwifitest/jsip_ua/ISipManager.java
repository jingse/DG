package com.example.guihuan.chatwifitest.jsip_ua;

public interface ISipManager {

	public void SendMessage(String to, String message, String method) throws NotInitializedException;
	public void SendDTMF(String digit) throws NotInitializedException;
	public void Register(String to, String message);
	public void Call(String to, int localRtpPort) throws NotInitializedException;
	public void Hangup() throws NotInitializedException;;
}
