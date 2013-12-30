package com.github.geub.splunk.logback.appender;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

class UdpSocketOutputStream extends OutputStream {

    private DatagramChannel datagramChannel;
    private SocketAddress socketAddress;

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
        this.datagramChannel.send(ByteBuffer.wrap(b, off, len), this.socketAddress);
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
