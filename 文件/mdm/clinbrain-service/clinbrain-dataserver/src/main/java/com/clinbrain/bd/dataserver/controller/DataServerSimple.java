package com.clinbrain.bd.dataserver.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

public class DataServerSimple {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DataServerSimple.class);

    public String doPost(String url, Map<String,String> map, String charset){
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = "";
        InputStream inputStream = null;
        ByteArrayOutputStream out=null;
        long start = System.currentTimeMillis();
        try{
            httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
            }
            if(list.size() > 0){
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            System.out.println("返回数据耗时："+(System.currentTimeMillis()-start));
            inputStream = response.getEntity().getContent();
            out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            result = new String(out.toByteArray(),"utf-8");
            System.out.println("总耗时："+(System.currentTimeMillis()-start));
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            try{
                if(out!=null) out.close();
                if(inputStream!=null) inputStream.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }

        }
        return result;
    }

    /*public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String url = "http://localhost:9999/clinbrain-dataserver/dataServer/s/getDataSet";
        Map<String,String> params = new HashMap<>();
        params.put("invoiceno","40307101");
        params.put("feeno","sf9344858");
        params.put("serialno","yj47490421");
        DataServerSimple client = new DataServerSimple();
        client.doPost(url,params,"UTF-8");
        //System.out.println(result);
    }*/
}
