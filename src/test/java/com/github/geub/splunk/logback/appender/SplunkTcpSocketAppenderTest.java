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

}
