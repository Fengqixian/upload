package com.clinbrain.bd.dataserver;

import com.clinbrain.bd.mdm.common.security.annotation.EnablePigFeignClients;
import com.clinbrain.bd.mdm.common.security.annotation.EnablePigResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnablePigResourceServer
//@EnablePigFeignClients
@EnableFeignClients
@SpringCloudApplication
public class DataServerApplication
{
    public static void main( String[] args ) {
        SpringApplication.run(DataServerApplication.class,args);
    }
}
