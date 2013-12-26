package com.github.geub.splunk.logback.appender;

import java.io.IOException;
import java.io.OutputStream;

public class SplunkUdpSocketAppender extends SplunkSocketAppenderBase {

	@Override
	protected OutputStream createSocketOutputStream(String host, int port) throws IOException {
		return new UdpSocketOutputStream(host, port);
	}

}
