package com.example.guihuan.chatwifitest.jsip_ua.impl.sip_msg;

import android.javax.sip.InvalidArgumentException;
import android.javax.sip.SipProvider;
import android.javax.sip.address.Address;
import android.javax.sip.address.AddressFactory;
import android.javax.sip.address.SipURI;
import android.javax.sip.address.URI;
import android.javax.sip.header.CSeqHeader;
import android.javax.sip.header.CallIdHeader;
import android.javax.sip.header.ContactHeader;
import android.javax.sip.header.ContentTypeHeader;
import android.javax.sip.header.ExpiresHeader;
import android.javax.sip.header.FromHeader;
import android.javax.sip.header.Header;
import android.javax.sip.header.HeaderFactory;
import android.javax.sip.header.MaxForwardsHeader;
import android.javax.sip.header.RouteHeader;
import android.javax.sip.header.SupportedHeader;
import android.javax.sip.header.ToHeader;
import android.javax.sip.header.ViaHeader;
import android.javax.sip.message.MessageFactory;
import android.javax.sip.message.Request;

import com.example.guihuan.chatwifitest.Var;
import com.example.guihuan.chatwifitest.jsip_ua.impl.SipManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Bob on 2015/7/9.
 */
public class SipRequestMsgBuilder {

    public Request buildRegister(SipManager sipManager,String message) throws ParseException, InvalidArgumentException {
        AddressFactory addressFactory = sipManager.addressFactory;
        SipProvider sipProvider = sipManager.sipProvider;
        MessageFactory messageFactory = sipManager.messageFactory;
        HeaderFactory headerFactory = sipManager.headerFactory;

        Address fromAddress = addressFactory.createAddress("sip:BOB@10.28.245.174:5080");
        fromAddress.setDisplayName(sipManager.getSipProfile().getSipUserName());
        Address toAddress = addressFactory.createAddress("sip:Server@10.128.253.106:6666");
        toAddress.setDisplayName(sipManager.getSipProfile().getSipUserName());
        Address contactAddress = sipManager.createContactAddress();
        ArrayList<ViaHeader> viaHeaders = sipManager.createViaHeader();
        URI requestURI = addressFactory.createAddress(
                "sip:" + sipManager.getSipProfile().getRemoteEndpoint()).getURI();

        // Build the request
        final Request request = messageFactory.createRequest(requestURI,
                Request.REGISTER, sipProvider.getNewCallId(),
                headerFactory.createCSeqHeader(1l, Request.REGISTER),
                headerFactory.createFromHeader(fromAddress, "c3ff411e"),
                headerFactory.createToHeader(toAddress, null), viaHeaders,
                headerFactory.createMaxForwardsHeader(70));

        // Add the contact header
        request.addHeader(headerFactory.createContactHeader(contactAddress));
        ExpiresHeader eh = headerFactory.createExpiresHeader(300);
        request.addHeader(eh);

        ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader("text", "plain");
        request.setContent(message, contentTypeHeader);

        // Print the request
        System.out.println(request.toString());

        return request;

        // Send the request --- triggers an IOException
        // sipProvider.sendRequest(request);
        // ClientTransaction transaction = sipProvider.getNewClientTransaction(request);
        // Send the request statefully, through the client transaction.
        // transaction.sendRequest();
    }

    public Request buildMessage(SipManager sipManager, String to, String message,String method) throws ParseException, InvalidArgumentException {
        SipURI from = sipManager.addressFactory.createSipURI(Var.Username, sipManager.getSipProfile().getLocalEndpoint());
        Address fromNameAddress = sipManager.addressFactory.createAddress(from);
        // fromNameAddress.setDisplayName(sipUsername);
        FromHeader fromHeader = sipManager.headerFactory.createFromHeader(fromNameAddress,
                "Tzt0ZEP92");

        URI toAddress = sipManager.addressFactory.createURI(to);
        Address toNameAddress = sipManager.addressFactory.createAddress(toAddress);
        // toNameAddress.setDisplayName(username);
        ToHeader toHeader = sipManager.headerFactory.createToHeader(toNameAddress, null);

        URI requestURI = sipManager.addressFactory.createURI(to);
        // requestURI.setTransportParam("udp");

        ArrayList<ViaHeader> viaHeaders = sipManager.createViaHeader();

        CallIdHeader callIdHeader = sipManager.sipProvider.getNewCallId();

        CSeqHeader cSeqHeader = sipManager.headerFactory.createCSeqHeader(50l,
               method);

        MaxForwardsHeader maxForwards = sipManager.headerFactory
                .createMaxForwardsHeader(70);

        Request request = sipManager.messageFactory.createRequest(requestURI,
                method, callIdHeader, cSeqHeader, fromHeader,
                toHeader, viaHeaders, maxForwards);
        SupportedHeader supportedHeader = sipManager.headerFactory
                .createSupportedHeader("replaces, outbound");
        request.addHeader(supportedHeader);

        SipURI routeUri = sipManager.addressFactory.createSipURI(null, sipManager.getSipProfile().getRemoteIp());
        routeUri.setTransportParam(sipManager.getSipProfile().getTransport());
        routeUri.setLrParam();
        routeUri.setPort(sipManager.getSipProfile().getRemotePort());

        Address routeAddress = sipManager.addressFactory.createAddress(routeUri);
        RouteHeader route = sipManager.headerFactory.createRouteHeader(routeAddress);
        request.addHeader(route);

        SipURI contactURI = sipManager.addressFactory.createSipURI(Var.Username, Var.host);
        contactURI.setPort(Var.port);
        Address contactAddress = sipManager.addressFactory.createAddress(contactURI);
        contactAddress.setDisplayName(Var.Username);
        ContactHeader contactHeader = sipManager.headerFactory.createContactHeader(contactAddress);
        request.addHeader(contactHeader);

        ContentTypeHeader contentTypeHeader = sipManager.headerFactory
                .createContentTypeHeader("text", "plain");
        request.setContent(message, contentTypeHeader);
        System.out.println(request);
        return request;

    }

    public Request buildInvite(SipManager sipManager, String to, int port){

        try {
            SipURI from = sipManager.addressFactory.createSipURI(sipManager.getSipProfile().getSipUserName(), sipManager.getSipProfile().getLocalEndpoint());
            Address fromNameAddress = sipManager.addressFactory.createAddress(from);
            //fromNameAddress.setDisplayName(sipUsername);
            FromHeader fromHeader = sipManager.headerFactory.createFromHeader(fromNameAddress,
                    "Tzt0ZEP92");
            URI toAddress = sipManager.addressFactory.createURI(to);
            Address toNameAddress = sipManager.addressFactory.createAddress(toAddress);
            // toNameAddress.setDisplayName(username);
            ToHeader toHeader = sipManager.headerFactory.createToHeader(toNameAddress, null);

            URI requestURI = sipManager.addressFactory.createURI(to);
            // requestURI.setTransportParam("udp");

            ArrayList<ViaHeader> viaHeaders = sipManager.createViaHeader();

            CallIdHeader callIdHeader = sipManager.sipProvider.getNewCallId();

            CSeqHeader cSeqHeader = sipManager.headerFactory.createCSeqHeader(1l,
                    Request.INVITE);

            MaxForwardsHeader maxForwards = sipManager.headerFactory
                    .createMaxForwardsHeader(70);

            Request callRequest = sipManager.messageFactory.createRequest(requestURI,
                    Request.INVITE, callIdHeader, cSeqHeader, fromHeader,
                    toHeader, viaHeaders, maxForwards);
            SupportedHeader supportedHeader = sipManager.headerFactory
                    .createSupportedHeader("replaces, outbound");
            callRequest.addHeader(supportedHeader);
            addCustomHeaders(callRequest,sipManager);

            SipURI routeUri = sipManager.addressFactory.createSipURI(null, sipManager.getSipProfile().getRemoteIp());
            routeUri.setTransportParam(sipManager.getSipProfile().getTransport());
            routeUri.setLrParam();
            routeUri.setPort(sipManager.getSipProfile().getRemotePort());

            Address routeAddress = sipManager.addressFactory.createAddress(routeUri);
            RouteHeader route = sipManager.headerFactory.createRouteHeader(routeAddress);
            callRequest.addHeader(route);
            // Create ContentTypeHeader
            ContentTypeHeader contentTypeHeader = sipManager.headerFactory
                    .createContentTypeHeader("application", "sdp");

            // Create the contact name address.
            SipURI contactURI = sipManager.addressFactory.createSipURI(sipManager.getSipProfile().getSipUserName(), sipManager.getSipProfile().getLocalIp());
            contactURI.setPort(sipManager.sipProvider.getListeningPoint(sipManager.getSipProfile().getTransport())
                    .getPort());

            Address contactAddress = sipManager.addressFactory.createAddress(contactURI);

            // Add the contact address.
            //contactAddress.setDisplayName(fromName);

            ContactHeader contactHeader = sipManager.headerFactory.createContactHeader(contactAddress);
            callRequest.addHeader(contactHeader);

            // You can add extension headers of your own making
            // to the outgoing SIP request.
            // Add the extension header.
            Header extensionHeader = sipManager.headerFactory.createHeader("My-Header",
                    "my header value");
            callRequest.addHeader(extensionHeader);

            String sdpData= "v=0\r\n"
                    + "o=4855 13760799956958020 13760799956958020"
                    + " IN IP4 " + sipManager.getSipProfile().getLocalIp() +"\r\n" + "s=mysession session\r\n"
                    + "p=+46 8 52018010\r\n" + "c=IN IP4 " + sipManager.getSipProfile().getLocalIp()+"\r\n"
                    + "t=0 0\r\n" + "m=audio "+port+" RTP/AVP 0 4 18\r\n"
                    + "a=rtpmap:0 PCMU/8000\r\n" + "a=rtpmap:4 G723/8000\r\n"
                    + "a=rtpmap:18 G729A/8000\r\n" + "a=ptime:20\r\n";
            byte[] contents = sdpData.getBytes();

            callRequest.setContent(contents, contentTypeHeader);
            // You can add as many extension headers as you
            // want.

            extensionHeader = sipManager.headerFactory.createHeader("My-Other-Header",
                    "my new header value ");
            callRequest.addHeader(extensionHeader);

            Header callInfoHeader = sipManager.headerFactory.createHeader("Call-Info",
                    "<http://www.antd.nist.gov>");
            callRequest.addHeader(callInfoHeader);
            return callRequest;
            // Create the client transaction.
            //inviteTid = sipManager.sipProvider.getNewClientTransaction(callRequest);

            //System.out.println("inviteTid = " + inviteTid);

            // send the request out.

            //	inviteTid.sendRequest();

            //dialog = inviteTid.getDialog();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();

        }
        return null;
    }

    private void addCustomHeaders(Request callRequest, SipManager sipManager) {
        // Get a set of the entries
        Set set = sipManager.getCustomHeaders().entrySet();
        // Get an iterator
        Iterator i = set.iterator();
        // Display elements
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            try {
                Header customHeader = sipManager.headerFactory.createHeader(me.getKey().toString(),me.getValue().toString());
                callRequest.addHeader(customHeader);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
