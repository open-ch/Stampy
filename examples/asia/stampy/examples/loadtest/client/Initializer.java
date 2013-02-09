/*
 * Copyright (C) 2013 Burton Alexander
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
 */
package asia.stampy.examples.loadtest.client;

import asia.stampy.client.mina.ClientMinaMessageGateway;
import asia.stampy.client.mina.ConnectedMessageListener;
import asia.stampy.client.mina.RawClientMinaHandler;
import asia.stampy.common.heartbeat.HeartbeatContainer;

/**
 * This class programmatically initializes the Stampy classes required for this
 * example. It is expected that a DI framework such as <a
 * href="http://www.springsource.org/">Spring</a> or <a
 * href="http://code.google.com/p/google-guice/">Guice</a> will be used to
 * perform this task.
 */
public class Initializer {

	/**
	 * Initialize.
	 * 
	 * @return the client mina message gateway
	 */
	public static ClientMinaMessageGateway initialize() {
		HeartbeatContainer heartbeatContainer = new HeartbeatContainer();

		ClientMinaMessageGateway gateway = new ClientMinaMessageGateway();
		gateway.setPort(1234);
		gateway.setHost("localhost");

		// ClientMinaHandler handler = new ClientMinaHandler();
		RawClientMinaHandler handler = new RawClientMinaHandler();
		handler.setHeartbeatContainer(heartbeatContainer);
		handler.setMessageGateway(gateway);

		ConnectedMessageListener cml = new ConnectedMessageListener();
		cml.setHeartbeatContainer(heartbeatContainer);
		cml.setMessageGateway(gateway);
		handler.addMessageListener(cml);

		gateway.setHandler(handler);

		return gateway;

	}
}
