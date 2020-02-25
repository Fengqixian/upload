package com.clinbrain.bd;

import com.clinbrain.bd.dataserver.DataServerApplication;
import com.clinbrain.bd.dataserver.dto.DataServer;
import com.clinbrain.bd.dataserver.entity.DataSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataServerCtrlTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private URL base;

    @Before
    public void setUp() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        System.out.println(String.format("port is : [%d]", port));
        this.base = new URL(url);
    }

    @Test
    public void testAddDataServer() throws Exception {
        DataServer dataServer = new DataServer();
        dataServer.setServerName("getTest");
        dataServer.setServerDesc("获取test");
        dataServer.setQuerySql("select * from data_server");
        dataServer.setReturnType("JSON");
        ResponseEntity<String> response =
                restTemplate.postForEntity(this.base.toString()+"dataServer/edit",
                dataServer,String.class);
        System.out.println(String.format("测试结果为：%s", response.getBody()));
    }

    @Test
    public void testDataSet(){
        DataSet dataSet =new DataSet();
        /*ResponseEntity<DataSet> response =
                restTemplate.postForEntity(this.base.toString()+"dataServer/getDataSet",
                        dataSet,DataSet.class);*/
        System.out.println(this.base.toString()+"dataServer/getDataSet");
        DataSet response = restTemplate.getForObject(this.base.toString()+"dataServer/getDataSet",DataSet.class,"");
        System.out.println(String.format("测试结果为：%s", response));

    }

}
