package com.clinbrain.bd.mdm.strategy;

import com.clinbrain.bd.mdm.common.security.annotation.EnablePigFeignClients;
import com.clinbrain.bd.mdm.common.security.annotation.EnablePigResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnablePigResourceServer
@EnablePigFeignClients
@SpringBootApplication
public class ClinbrainStrategyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinbrainStrategyApplication.class, args);
	}

}
