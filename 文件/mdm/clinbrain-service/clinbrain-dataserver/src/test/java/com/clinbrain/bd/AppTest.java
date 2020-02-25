package com.clinbrain.bd;

import static org.junit.Assert.assertTrue;

import com.clinbrain.bd.dataserver.DataServerApplication;
import com.clinbrain.bd.dataserver.dto.DataServer;
import com.clinbrain.bd.dataserver.service.DataServerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    @Autowired
    private DataServerService dataServerService;
    @Test
    public void insertDataServer(){
        DataServer dataServer = new DataServer();
        dataServer.setServerName("getTest");
        dataServer.setServerDesc("获取test");
        dataServer.setQuerySql("select * from data_server");
        dataServer.setReturnType("JSON");
        dataServerService.edit(dataServer);

    }
}
