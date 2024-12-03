package org.example.configuration;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;

public class ConsulConfig {

    private static final String CONSUL_HOST = "127.0.0.1";
    private static final int CONSUL_PORT = 8500;

    public static void registerService() {

        ConsulClient consulClient = new ConsulClient(CONSUL_HOST, CONSUL_PORT);

        NewService newService = new NewService();
        newService.setId("called");
        newService.setName("called-service");
        newService.setPort(8881);
        newService.setAddress("127.0.0.1");

//        NewService.Check healthCheck = new NewService.Check();
//        healthCheck.setHttp("http://127.0.0.1:8881/");
//        healthCheck.setInterval("10s");
//
//        newService.setCheck(healthCheck);

        consulClient.agentServiceRegister(newService);
    }
}
