package com.cloudhopper.smpp.demo;

/*
 * #%L
 * ch-smpp
 * %%
 * Copyright (C) 2009 - 2013 Cloudhopper by Twitter
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.cloudhopper.smpp.SmppServer;
import com.cloudhopper.smpp.SmppServerConfiguration;
import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.demo.ServerMain.DefaultSmppServerHandler;
import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.BaseBindResp;
import com.cloudhopper.smpp.pdu.EnquireLink;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.type.SmppProcessingException;

public class EnquireLinkServerMain {

	public static void main(String[] args) throws Exception {
		/* I've created this to check if the automatic enquire link is Ok
		 * The only way to close the sessions is by finishing the application
		 */
		SmppServerConfiguration configuration = new SmppServerConfiguration();
        configuration.setPort(2776);
        configuration.setMaxConnectionSize(10);
        configuration.setNonBlockingSocketsEnabled(true);
        configuration.setDefaultRequestExpiryTimeout(30000);
        configuration.setDefaultWindowMonitorInterval(15000);
        configuration.setDefaultWindowSize(5);
        configuration.setDefaultWindowWaitTimeout(configuration.getDefaultRequestExpiryTimeout());
        configuration.setDefaultSessionCountersEnabled(true);
        configuration.setJmxEnabled(true);
        
        SmppServer smppServer = new DefaultSmppServer(configuration, new DefaultSmppServerHandler());
        smppServer.start();
        
        Runtime.getRuntime().addShutdownHook(new EnquireLinkServerShutdownHook(smppServer));
	}
	
	static class EnquireLinkServerHandler extends DefaultSmppServerHandler {
		
		@Override
		public void sessionCreated(Long sessionId, SmppServerSession session, BaseBindResp preparedBindResponse) throws SmppProcessingException {
			session.serverReady(new EnquireLinkSessionHandler());
		}
		
	}
	
	static class EnquireLinkSessionHandler extends DefaultSmppSessionHandler {
		
		@Override
		public PduResponse firePduRequestReceived(@SuppressWarnings("rawtypes") PduRequest pduRequest) {
			if (pduRequest instanceof EnquireLink) {
				return pduRequest.createResponse();
			} else {
				return super.firePduRequestReceived(pduRequest);
			}
		}
		
	}
	
	static class EnquireLinkServerShutdownHook extends Thread {
		
		private SmppServer smppServer;
		
		public EnquireLinkServerShutdownHook(SmppServer smppServer) {
			this.smppServer = smppServer;
		}
		
		@Override
		public void run() {
			smppServer.stop();
		}
		
		
	}
	
}
