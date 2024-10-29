package com.example.demo.configuration;

import com.example.demo.client.api.PersonsClient;
import com.example.demo.client.webclient.PersonWebClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;


@Configuration
public class ClientConfig {

    @Value("${webclient.timeout}")
    int timeout;

    @Bean
    public WebClient webClient() {
        final HttpClient httpClient = HttpClient
                .create()
                .compress(true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(timeout, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(timeout, TimeUnit.MILLISECONDS));
                });

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public PersonsClient personsClient(
            WebClient webClient,
            @Value("${called.url.base}") String baseUrl
    ) {
        return new PersonWebClient(webClient, baseUrl);
    }
}
