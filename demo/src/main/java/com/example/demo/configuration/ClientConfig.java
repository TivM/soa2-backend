package com.example.demo.configuration;

import com.example.demo.client.api.PersonsClient;
import com.example.demo.client.webclient.PersonWebClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Configuration
@LoadBalancerClient(name = "demo", configuration = BalancerConfig.class)
public class ClientConfig {

    @Value("${webclient.timeout}")
    int timeout;

    @Bean
    @LoadBalanced
    public WebClient configureWebclient() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(
                        ResourceUtils.getFile("classpath:keystore/trust.jks")),
                "password".toCharArray()
        );

        List<Certificate> certificateList = Collections.list(trustStore.aliases())
                .stream()
                .filter(t -> {
                    try {
                        return trustStore.isCertificateEntry(t);
                    } catch (KeyStoreException ex) {
                        throw new RuntimeException("Error getting truststore", ex);
                    }
                })
                .map(trustore -> {
                    try {
                        return trustStore.getCertificate(trustore);
                    } catch (KeyStoreException ex) {
                        throw new RuntimeException("Error getting truststore", ex);
                    }
                })
                .collect(Collectors.toList());
        X509Certificate[] certificates = certificateList.toArray(new X509Certificate[certificateList.size()]);


//        SslContext sslContext = SslContextBuilder
//                .forClient()
//                .trustManager(InsecureTrustManagerFactory.INSTANCE)
//                .build();

        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(certificates)
                .build();

        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
        return WebClient.builder().clientConnector(connector).build();
    }

    @Bean
    public PersonsClient personsClient(
            WebClient webClient,
            @Value("${called.url.base}") String baseUrl
    ) {
        return new PersonWebClient(webClient, baseUrl);
    }



}
