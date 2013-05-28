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

import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppClient;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppClient;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;

public class EnquireLinkClientMain {

	public static void main(String[] args) throws Exception {
		/* I've created this to check if the automatic enquire link is Ok
		 * The only way to close the sessions is by finishing the application
		 */
		SmppSessionConfiguration configuration = new SmppSessionConfiguration();
		configuration.setWindowSize(1);
		configuration.setName("Tester.Session.0");
		configuration.setType(SmppBindType.TRANSCEIVER);
		configuration.setHost("localhost");
		configuration.setPort(2776);
		configuration.setConnectTimeout(10000);
		configuration.setSystemId("4104");
		configuration.setPassword("89696155");
		configuration.getLoggingOptions().setLogBytes(true);
		configuration.setRequestExpiryTimeout(30000);
		configuration.setWindowMonitorInterval(15000);
		configuration.setCountersEnabled(true);
		configuration.setAutomaticEnquireLink(true);
        
		
		SmppClient smppClient = new DefaultSmppClient();
		SmppSession smppSession = smppClient.bind(configuration, new DefaultSmppSessionHandler());
		
		Runtime.getRuntime().addShutdownHook(new EnquireLinkClientShutdownHook(smppSession));
	}
	
	static class EnquireLinkClientShutdownHook extends Thread {
		
		private SmppSession smppSession;
		
		public EnquireLinkClientShutdownHook(SmppSession smppSession) {
			this.smppSession = smppSession;
		}
		
		@Override
		public void run() {
			smppSession.unbind(10000);
		}
	}
}
