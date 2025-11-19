package org.fmarsico.springcloud.msvc.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcGatewayApplication.class, args);
	}

}
