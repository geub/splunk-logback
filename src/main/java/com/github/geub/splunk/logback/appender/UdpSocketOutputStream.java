package com.github.geub.splunk.logback.appender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

class UdpSocketOutputStream extends OutputStream {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private DatagramChannel datagramChannel;
    private SocketAddress socketAddress;
    protected static final int UDP_MAX_LENGTH_IN_BYTES = 65500;

    public UdpSocketOutputStream(String host, int port) throws IOException {
        this.datagramChannel = DatagramChannel.open();
        this.socketAddress = new InetSocketAddress(host, port);
    }

    @Override
    public void write(int b) throws IOException {
        // Does nothing, since write(byte[] b, int off, int len) was overwritten.
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        int bytesLengthToWrite = truncateBytesLengthToMaxSizeAllowedForUdp(len);
        ByteBuffer wrap = ByteBuffer.wrap(b, off, bytesLengthToWrite);
        this.datagramChannel.send(wrap, this.socketAddress);
    }

    private int truncateBytesLengthToMaxSizeAllowedForUdp(int len) {
        if(len > UDP_MAX_LENGTH_IN_BYTES) {
            logger.warn("Line truncated");
            return UDP_MAX_LENGTH_IN_BYTES;
        }
        return len;
    }

    @Override
    public void flush() throws IOException {
        // Does nothing, UDP sends.
    }

    @Override
    public void close() throws IOException {
        this.datagramChannel.close();
    }
}
