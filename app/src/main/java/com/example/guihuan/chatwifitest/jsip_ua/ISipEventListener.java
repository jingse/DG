package com.example.guihuan.chatwifitest.jsip_ua;

import java.util.EventListener;

import com.example.guihuan.chatwifitest.jsip_ua.impl.SipEvent;

public interface ISipEventListener extends EventListener {

	public void onSipMessage(SipEvent sipEvent);
}
