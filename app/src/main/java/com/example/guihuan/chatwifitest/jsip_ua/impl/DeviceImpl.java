package com.example.guihuan.chatwifitest.jsip_ua.impl;

import android.content.Context;
import android.os.Handler;

import com.example.guihuan.chatwifitest.jsip_ua.IDevice;
import com.example.guihuan.chatwifitest.jsip_ua.NotInitializedException;
import com.example.guihuan.chatwifitest.jsip_ua.SipProfile;
import com.example.guihuan.chatwifitest.jsip_ua.SipUAConnectionListener;
import com.example.guihuan.chatwifitest.jsip_ua.SipUADeviceListener;
import com.example.guihuan.chatwifitest.jsip_ua.impl.SipEvent.SipEventType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// ISSUE#17: commented those, as we need to decouple the UI details
//import org.mobicents.restcomm.android.sdk.ui.IncomingCall;
//import org.mobicents.restcomm.android.sdk.ui.NotifyMessage;

public class DeviceImpl implements IDevice,Serializable {
	private static DeviceImpl device;




	private Handler chatHandler;

	Context context;
	SipManager 	 sipManager;

	public SipProfile getSipProfile() {
		return sipProfile;
	}

	public void setSipProfile(SipProfile sipProfile) {
		this.sipProfile = sipProfile;
	}

	SipProfile 	 sipProfile;
	SoundManager soundManager;

	boolean isInitialized;
	public SipUADeviceListener sipuaDeviceListener = null;
	public SipUAConnectionListener sipuaConnectionListener = null;

	public List<String> getReCallMsgList() {
		return reCallMsgList;
	}

	public void setReCallMsgList(List<String> reCallMsgList) {
		this.reCallMsgList = reCallMsgList;
	}

	private List<String> reCallMsgList;//回传给list的消息信息


	private DeviceImpl(){
		
	}
	public static DeviceImpl getInstance(){
		if(device == null){
			device = new DeviceImpl();
		}
		return device;
	}

	public Handler getChatHandler() {
		return chatHandler;
	}

	public void setChatHandler(Handler chatHandler) {
		this.chatHandler = chatHandler;
		sipManager.setmUpdateHandler(this.chatHandler);
	}



    public void Initialize(Context context, SipProfile sipProfile, HashMap<String,String> customHeaders){
        this.Initialize(context,sipProfile);
        sipManager.setCustomHeaders(customHeaders);
    }
	public void Initialize(Context context, SipProfile sipProfile){
		this.context = context;
		this.sipProfile = sipProfile;
		sipManager = new SipManager(sipProfile);
//		soundManager = new SoundManager(context,sipProfile.getLocalIp());
		sipManager.addSipEventListener(this);
		reCallMsgList = new ArrayList<String>();
		reCallMsgList.clear();
	}
	
	@Override
	public void onSipMessage(final SipEvent sipEventObject) {
		System.out.println("Sip Event fired");
		if (sipEventObject.type == SipEventType.MESSAGE) {
			if (this.sipuaDeviceListener != null) {
				this.sipuaDeviceListener.onSipUAMessageArrived(new SipEvent(this, SipEventType.MESSAGE, sipEventObject.content, sipEventObject.from));
			}
		} else if (sipEventObject.type == SipEventType.BYE) {
			this.soundManager.releaseAudioResources();
			if (this.sipuaConnectionListener != null) {
				// notify our listener that we are connected
				this.sipuaConnectionListener.onSipUADisconnected(null);
			}
		} else if (sipEventObject.type == SipEventType.REMOTE_CANCEL) {
			//this.soundManager.releaseAudioResources();
			if (this.sipuaConnectionListener != null) {
				// notify our listener that we are connected
				this.sipuaConnectionListener.onSipUACancelled(null);
			}
		} else if (sipEventObject.type == SipEventType.DECLINED) {
			//this.soundManager.releaseAudioResources();
			if (this.sipuaConnectionListener != null) {
				// notify our listener that we are connected
				this.sipuaConnectionListener.onSipUADeclined(null);
			}
		}else if (sipEventObject.type == SipEventType.BUSY_HERE) {
			this.soundManager.releaseAudioResources();
		} else if (sipEventObject.type == SipEventType.SERVICE_UNAVAILABLE) {
			this.soundManager.releaseAudioResources();

		} else if (sipEventObject.type == SipEventType.CALL_CONNECTED) {
			this.soundManager.setupAudio(sipEventObject.remoteRtpPort, this.sipProfile.getRemoteIp());
			if (this.sipuaConnectionListener != null) {
				// notify our listener that we are connected
				this.sipuaConnectionListener.onSipUAConnected(null);
			}
		} else if (sipEventObject.type == SipEventType.REMOTE_RINGING) {
			if (this.sipuaConnectionListener != null) {
				// notify our listener that we are connecting
				this.sipuaConnectionListener.onSipUAConnecting(null);
			}
		} else if (sipEventObject.type == SipEventType.LOCAL_RINGING) {
			if (this.sipuaDeviceListener != null) {
				this.sipuaDeviceListener.onSipUAConnectionArrived(null);
			}
		}
	}

	@Override
	public void Call(String to) {
		try {
			this.sipManager.Call(to,this.soundManager.setupAudioStream(sipProfile.getLocalIp()));
		} catch (NotInitializedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void Accept() {
		sipManager.AcceptCall(soundManager.setupAudioStream(sipProfile.getLocalIp()));
	}

	@Override
	public void Reject() {
		sipManager.RejectCall();
	}

	@Override
	public void Cancel() {
		try {
			sipManager.Cancel();
		} catch (NotInitializedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void Hangup() {
		if (this.sipManager.direction == this.sipManager.direction.OUTGOING ||
				this.sipManager.direction == this.sipManager.direction.INCOMING) {
			try {
				this.sipManager.Hangup();
			} catch (NotInitializedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void SendMessage(String to, String message, String method) {
		try {

			this.sipManager.SendMessage(to, message, method);
		} catch (NotInitializedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void SendDTMF(String digit) {
		try {
			this.sipManager.SendDTMF(digit);
		} catch (NotInitializedException e) {
			e.printStackTrace();
		}

	}
	@Override
	public void Register(String to, String message) {

		this.sipManager.Register(to,message);
	}

	@Override
	public SipManager GetSipManager() {
		// TODO Auto-generated method stub
		return sipManager;
	}

	@Override
	public void Mute(boolean muted)
	{
		soundManager.muteAudio(muted);
	}

	@Override
	public SoundManager getSoundManager() {
		// TODO Auto-generated method stub
		return soundManager;
	}
	public static byte[] serialize(Object o) {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    try {   
	        ObjectOutput out = new ObjectOutputStream(bos);
	        out.writeObject(o);                                       //This is where the Exception occurs
	        out.close();     
	        // Get the bytes of the serialized object    
	        byte[] buf = bos.toByteArray();   
	        return buf;    
	    } catch(IOException ioe) {
	        //Log.e("serializeObject", "error", ioe);           //"ioe" says java.io.NotSerializableException exception
	        return null; 
	    }  

	}


	public static Object deserialize(byte[] b) {
	        try {    
	            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
	            Object object = in.readObject();
	            in.close();  
	            return object;  
	        } catch(ClassNotFoundException cnfe) {
	            //Log.e("deserializeObject", "class not found error", cnfe);   
	            return null;  
	        } catch(IOException ioe) {
	            //Log.e("deserializeObject", "io error", ioe);    
	            return null; 
	        } 
	    } 
	
	

}
