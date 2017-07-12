package com.example.guihuan.chatwifitest.jsip_ua.impl;

import android.content.Context;
import android.media.AudioManager;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.net.rtp.RtpStream;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SoundManager {
	Context appContext;
	AudioManager audio;
	AudioStream audioStream;
	AudioGroup audioGroup;
	public SoundManager(Context appContext, String ip){
		this.appContext = appContext;
		audio = (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE);
		try {
			audioStream = new AudioStream(InetAddress.getByName(ip));
			audioStream.setCodec(AudioCodec.PCMU);
			audioStream.setMode(RtpStream.MODE_NORMAL);
			audioGroup = new AudioGroup();
			audioGroup.setMode(AudioGroup.MODE_ECHO_SUPPRESSION);
		
				
				
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void releaseAudioResources() {
		
		audioStream.join(null);
		//audioStream.release();
		
		audioGroup.clear();
		audio.setMode(AudioManager.MODE_NORMAL);
		
			
	}
	public int setupAudioStream(String localIp) {
		audio.setMode(AudioManager.MODE_IN_COMMUNICATION);
		System.out.println("-------------------"+audioStream.getLocalPort());
		return audioStream.getLocalPort();
	}
	public void setupAudio(int remoteRtpPort, String remoteIp) {

		try {
			audioStream.associate(
					InetAddress.getByName(remoteIp),
					remoteRtpPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		audioStream.join(audioGroup);

	}
	public void muteAudio(boolean muted)
	{
		if (muted) {
			if (audioGroup.getMode() != audioGroup.MODE_MUTED) {
				audioGroup.setMode(audioGroup.MODE_MUTED);
			}
		}
		else {
			if (audioGroup.getMode() == audioGroup.MODE_MUTED) {
				audioGroup.setMode(audioGroup.MODE_NORMAL);
			}
		}
	}
}
