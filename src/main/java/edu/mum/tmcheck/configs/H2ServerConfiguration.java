package edu.mum.tmcheck.configs;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class H2ServerConfiguration {
    @Value("${h2.tcp.port:9092}")
    private String h2TcpPort;

    // Web port, default 8082
    @Value("${h2.web.port:8082}")
    private String h2WebPort;

    /**
     * TCP connection to connect with SQL clients to the embedded h2 database.
     * Connect to "jdbc:h2:tcp://localhost:9092/mem:testdb", username "sa", password empty.
     */
    @Bean
    @ConditionalOnExpression("${h2.tcp.enabled:true}")
    public Server h2TcpServer() throws SQLException {
        try{
            Server.shutdownTcpServer("tcp://localhost:9092", "", false, true);
        } catch (Exception e){}

        Server h2TcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", h2TcpPort);

        if (!h2TcpServer.isRunning(false))
            h2TcpServer.start();

        return h2TcpServer;
    }

    /**
     * Web console for the embedded h2 database.
     * Go to http://localhost:8082 and connect to the database "jdbc:h2:mem:testdb", username "sa", password empty.
     */
    @Bean
    @ConditionalOnExpression("${h2.web.enabled:false}")
    public Server h2WebServer() throws SQLException {
        Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", h2WebPort);

        if (!webServer.isRunning(false))
            webServer.start();

        return webServer;
    }
}