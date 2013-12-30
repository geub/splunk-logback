package com.github.geub.splunk.logback.appender;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SplunkTcpSocketAppender extends SplunkSocketAppenderBase {

    private Socket socket;

    @Override
    protected OutputStream createSocketOutputStream(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        return this.socket.getOutputStream();
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

}
