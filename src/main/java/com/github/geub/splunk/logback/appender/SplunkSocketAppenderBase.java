package com.github.geub.splunk.logback.appender;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;

abstract class SplunkSocketAppenderBase extends OutputStreamAppender<ILoggingEvent> {

	private String host;
	private int port;

	@Override
	public void start() {
		try {
			setOutputStream(createSocketOutputStream(this.host, this.port));
			configEncoderToUtf8IfNecessary();
			super.start();
		} catch (IOException e) {
			addError("Could not create socket channel", e);
		}
	}

	protected void configEncoderToUtf8IfNecessary() {
		Encoder<ILoggingEvent> layoutWrappingEncoder = getEncoder();
		if (layoutWrappingEncoder instanceof LayoutWrappingEncoder<?>) {
			((LayoutWrappingEncoder<?>) layoutWrappingEncoder).setCharset(Charset.forName("UTF-8"));
		}
	}

	protected abstract OutputStream createSocketOutputStream(String hostValue, int portValue) throws IOException;

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
