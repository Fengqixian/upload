package com.clinbrain.bd.proxy.store;

import com.clinbrain.bd.proxy.client.Client;
import com.clinbrain.bd.proxy.config.DatabaseConnectionInfo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author WANGYI
 * @className com.clinbrain.netty.store.RegisterStore
 * @createdDate 2019/8/21 11:07
 * @description TODO
 * @e-mail WANGYI@clinbrain.com
 * @group bigdata develop group (Simple-proxy)
 */
public class RegisterStore {
    private static final ConcurrentHashMap<String,LinkedBlockingQueue> clientMessageStore = new ConcurrentHashMap();
    private static final ConcurrentHashMap<String,Client> clientStore = new ConcurrentHashMap();
    private static final ConcurrentHashMap<String,DatabaseConnectionInfo> databaseConnectionInfoStore = new ConcurrentHashMap();
    public static void register(String contextId){
        clientMessageStore.put(contextId,new LinkedBlockingQueue());
    }
    public static void register(String contextId, Client client){
        clientStore.put(contextId,client);
    }
    public static void register(String contextId, DatabaseConnectionInfo databaseConnectionInfo){
        databaseConnectionInfoStore.put(contextId,databaseConnectionInfo);
    }
    public static void unregister(String contextId){
        clientStore.remove(contextId).stopClient();
        clientMessageStore.remove(contextId);
    }
    public static LinkedBlockingQueue getClientMessageStore(String contextId){
        return clientMessageStore.get(contextId);
    }
    public static Client getClient(String contextId){
        return clientStore.get(contextId);
    }
    public static DatabaseConnectionInfo getDatabaseConnectionInfo(String contextId){
        return databaseConnectionInfoStore.get(contextId);
    }
}
