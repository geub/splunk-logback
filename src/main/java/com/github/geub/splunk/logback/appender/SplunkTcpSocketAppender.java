package com.github.geub.splunk.logback.appender;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.net.AbstractSocketAppender;

public class SplunkTcpSocketAppender extends SplunkSocketAppenderBase {

	private Socket socket;
	private int reconnectionDelay = AbstractSocketAppender.DEFAULT_RECONNECTION_DELAY;
	private long tcpConnectionClosedTimeCounter;

	@Override
	protected OutputStream createSocketOutputStream(String host, int port) throws IOException {
		this.socket = new Socket(host, port);
		return this.socket.getOutputStream();
	}

	@Override
	public void doAppend(ILoggingEvent eventObject) {
		// workOnSocketReconnection();
		super.doAppend(eventObject);
	}

	private void workOnSocketReconnection() {
		if (isSocketJustClosed()) {
			startTcpConnectionClosedTimeCounter();
			addInfo("Iniciar a contagem para reinicio");
		}
		if (shouldTryToReconnect()) {
			addInfo("deve reconectar");
			restartTcpConnectionClosedTimeCounter();
			start();
		} else {
			addInfo("Não reconectado pois ainda não deu o tempo");
		}
	}

	private long startTcpConnectionClosedTimeCounter() {
		return this.tcpConnectionClosedTimeCounter = System.currentTimeMillis();
	}

	private boolean isSocketJustClosed() {
		return this.tcpConnectionClosedTimeCounter == 0 && (this.socket == null || this.socket.isClosed());
	}

	private void restartTcpConnectionClosedTimeCounter() {
		this.tcpConnectionClosedTimeCounter = 0;
	}

	private boolean shouldTryToReconnect() {
		return System.currentTimeMillis() >= this.tcpConnectionClosedTimeCounter + this.reconnectionDelay;
	}

	@Override
	public void stop() {
		super.stop();
		try {
			this.socket.close();
		} catch (IOException e) {
			addError("Unable to close TCP socket", e);
		}
	}

	public void setReconnectionDelay(int reconnectionDelay) {
		this.reconnectionDelay = reconnectionDelay;
	}

}
