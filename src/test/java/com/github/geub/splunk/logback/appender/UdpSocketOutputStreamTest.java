package com.github.geub.splunk.logback.appender;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.ExecutorServiceUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class UdpSocketOutputStreamTest {

    private static final int UDP_PORT = 10997;

    private Callable<String> task = new Callable<String>() {
        @Override
        public String call() throws Exception {
            DatagramSocket sock = new DatagramSocket(UdpSocketOutputStreamTest.UDP_PORT);
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            sock.receive(incoming);
            byte[] data = incoming.getData();
            sock.close();
            return new String(data, 0, incoming.getLength());
        }
    };

    @Test
    public void testSuccesfullyMessage() throws JoranException, InterruptedException, ExecutionException, UnsupportedEncodingException {
        Future<String> future = ExecutorServiceUtil.newExecutorService().submit(this.task);
        LoggerContext context = new LoggerContext();
        new ContextInitializer(context).configureByResource(getClass().getResource("/SplunkUdpSocketAppenderTest-logback.xml"));
        String logLineMultiplied = createLogLineMultiplied("geub", UdpSocketOutputStream.UDP_MAX_LENGTH_IN_BYTES / 4);
        context.getLogger(getClass()).info(logLineMultiplied);
        Assert.assertEquals("Wrong value", logLineMultiplied, future.get());
        Assert.assertEquals(UdpSocketOutputStream.UDP_MAX_LENGTH_IN_BYTES, getBytesLength(logLineMultiplied));
    }

    @Test
    public void testSuccesfullyTruncateMessage() throws JoranException, InterruptedException, ExecutionException, UnsupportedEncodingException {
        Future<String> future = ExecutorServiceUtil.newExecutorService().submit(this.task);
        LoggerContext context = new LoggerContext();
        new ContextInitializer(context).configureByResource(getClass().getResource("/SplunkUdpSocketAppenderTest-logback.xml"));
        String logLineMultiplied = createLogLineMultiplied("geub", UdpSocketOutputStream.UDP_MAX_LENGTH_IN_BYTES);
        context.getLogger(getClass()).info(logLineMultiplied);
        Assert.assertEquals("Wrong value", createLogLineMultiplied("geub", UdpSocketOutputStream.UDP_MAX_LENGTH_IN_BYTES / 4), future.get());
        Assert.assertEquals(UdpSocketOutputStream.UDP_MAX_LENGTH_IN_BYTES, getBytesLength(future.get()));
    }

    public String createLogLineMultiplied(String logLine, int multiplier){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < multiplier; i++) {
            sb.append(logLine);
        }
        return sb.toString();
    }

    public int getBytesLength(String logLIne) throws UnsupportedEncodingException {
        byte[] b = logLIne.getBytes("UTF-8");
        return b.length;
    }
}
