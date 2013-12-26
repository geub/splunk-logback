package com.github.geub.splunk.logback.appender;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.util.ExecutorServiceUtil;

public class SplunkTcpSocketAppenderTest {

	static int TCP_PORT = 9998;

	private Callable<String> task = new Callable<String>() {
		@Override
		public String call() throws Exception {
			ServerSocket socket = new ServerSocket(SplunkTcpSocketAppenderTest.TCP_PORT);
			Socket connSocket = socket.accept();
			try {
				return IOUtils.toString(connSocket.getInputStream());
			} finally {
				connSocket.close();
			}
		}
	};

	@Test
	public void testSuccesfullySendMessage() throws JoranException, InterruptedException, ExecutionException {
		Future<String> future = ExecutorServiceUtil.newExecutorService().submit(this.task);
		LoggerContext context = new LoggerContext();
		new ContextInitializer(context).configureByResource(getClass().getResource("/SplunkTcpSocketAppenderTest-logback.xml"));
		String uuid = UUID.randomUUID().toString();
		context.getLogger(getClass()).info(uuid);
		context.stop();
		Assert.assertEquals("Valor do log incorreto", uuid, future.get());
	}

	// @Test
	public void testConnectionRefusedStartingTcpAppender() {
		SplunkTcpSocketAppender appender = new SplunkTcpSocketAppender();
		appender.setHost("localhost");
		appender.setPort(SplunkTcpSocketAppenderTest.TCP_PORT);
		LoggerContext context = new LoggerContext();
		appender.setContext(context);
		appender.start();
		Assert.assertFalse("O appender não poderia ter iniciado", appender.isStarted());
		Status expectedStatus = context.getStatusManager().getCopyOfStatusList().get(0);
		Assert.assertEquals("Deveria apresentar mensagem de erro", "Could not create socket channel", expectedStatus.getMessage());
	}

	// @Test
	public void testSocketReconnectionWhenFailedToStart() throws InterruptedException, JoranException {
		SplunkTcpSocketAppender appender = new SplunkTcpSocketAppender();
		appender.setHost("localhost");
		appender.setPort(SplunkTcpSocketAppenderTest.TCP_PORT);
		int reconnectionDelay = 2000;
		appender.setReconnectionDelay(reconnectionDelay);
		LoggerContext context = new LoggerContext();
		appender.setContext(context);
		appender.start();
		Assert.assertFalse("O appender não poderia ter iniciado", appender.isStarted());
		new ContextInitializer(context).configureByResource(getClass().getResource("/SplunkTcpSocketAppenderTest-logback.xml"));
		Future<String> future = ExecutorServiceUtil.newExecutorService().submit(this.task);
		Thread.sleep(reconnectionDelay * 2);
		// appender.doAppend(null);
		Assert.assertTrue("O appender deveria ter iniciado", appender.isStarted());

	}

}
