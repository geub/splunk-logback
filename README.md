splunk-logback
==============

Logback appender to send log events to splunk

Requirements
------------

* logback-classic version 1.0.13

Configuration
-------------

**TCP**

    <configuration>
        <appender name="SPLUNK_TCP_APPENDER" class="com.github.geub.splunk.logback.appender.SplunkTcpSocketAppender">
            <port>9997</port>
    	    <host>localhost</host>
    	    <layout class="com.github.geub.kv.converter.logback.KeyValuePatternLayout">
                <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            </layout>
        </appender>
        
        <root level="DEBUG">
            <appender-ref ref="SPLUNK_TCP_APPENDER" />
        </root>
    </configuration>
    
**UDP**

    <configuration>
        <appender name="SPLUNK_TCP_APPENDER" class="com.github.geub.splunk.logback.appender.SplunkUdpSocketAppender">
            <port>9997</port>
    	    <host>localhost</host>
    	    <layout class="com.github.geub.kv.converter.logback.KeyValuePatternLayout">
                <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            </layout>
        </appender>
        
        <root level="DEBUG">
            <appender-ref ref="SPLUNK_TCP_APPENDER" />
        </root>
    </configuration>
