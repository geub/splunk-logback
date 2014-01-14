splunk-logback
==============

Logback appender to send log events to splunk

Requirements
------------

* logback-classic-1.0.13
* splunk-1.2.0

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

Release
-------

* 1.0.0
    - Initial project.
* 1.0.1
    - Included log limit.
* 1.0.2-SNAPSHOT
    - Included travis.