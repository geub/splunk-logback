package com.github.geub.splunk.logback.appender;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.ExecutorServiceUtil;

public class SplunkUdpSocketAppenderTest {

	private static final int UDP_PORT = 10997;

	private Callable<String> task = new Callable<String>() {
		@Override
		public String call() throws Exception {
			DatagramSocket sock = null;
			sock = new DatagramSocket(SplunkUdpSocketAppenderTest.UDP_PORT);
			byte[] buffer = new byte[65536];
			DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
			while (true) {
				sock.receive(incoming);
				byte[] data = incoming.getData();
				return new String(data, 0, incoming.getLength());
			}
		}
	};

	@Test
	public void testSuccesfullySendMessage() throws JoranException, InterruptedException, ExecutionException {
		Future<String> future = ExecutorServiceUtil.newExecutorService().submit(this.task);
		LoggerContext context = new LoggerContext();
		new ContextInitializer(context).configureByResource(getClass().getResource("/SplunkUdpSocketAppenderTest-logback.xml"));
		String uuid = UUID.randomUUID().toString();
		context.getLogger(getClass()).info(uuid);
		Assert.assertEquals("Valor do log incorreto", uuid, future.get());
	}

}
