package com.cloudhopper.smpp.util;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.impl.SmppSessionChannelListener;
import com.cloudhopper.smpp.pdu.EnquireLink;
import com.cloudhopper.smpp.pdu.EnquireLinkResp;
import com.cloudhopper.smpp.type.SmppChannelException;

public class EnquireLinkSender implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(EnquireLinkSender.class);
	
	private SmppSession session;
	private SequenceNumber sequence;
	
	private boolean started;
	private Thread thread;
	
	public EnquireLinkSender(SmppSession session) {
		if (session == null) {
			throw new NullPointerException("The SmppSession cannot be null.");
		}
		
		this.session = session;
		this.thread = new Thread(this);
		
		this.sequence = new SequenceNumber();
	}
	
	public void start() {
		if (!started) {
			started = true;
			thread.start();
		}
		
	}
	
	public void stop() {
		started = false;
	}

	@Override
	public void run() {
		while (started) {
			try {
				Thread.sleep(session.getConfiguration().getEnquireLinkTimer());
				
				if (session.isBound()) {
					EnquireLink enquireLink = new EnquireLink();
					enquireLink.setSequenceNumber(sequence.next());
					
					EnquireLinkResp response = session.enquireLink(enquireLink, session.getConfiguration().getRequestExpiryTimeout());
					response.getCommandStatus();
				} else if (session.isClosed()) {
					stop();
				}
			} catch (SmppChannelException e) {
				stop();
				session.close();
				
				if (session instanceof SmppSessionChannelListener) {
					((SmppSessionChannelListener) session).fireChannelClosed();
				}
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
	}

}