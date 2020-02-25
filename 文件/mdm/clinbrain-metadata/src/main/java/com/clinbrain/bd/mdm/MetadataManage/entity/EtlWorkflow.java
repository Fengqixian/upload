package com.clinbrain.bd.mdm.MetadataManage.entity;

public class EtlWorkflow {
    public String source="";
    public String target="";
    public String excutor = "DataXCompoment";
    public String parama = "{\n" +
            "\t\"job\": {\n" +
            "\t\t\"setting\": {\n" +
            "\t\t\t\"speed\": {\n" +
            "\t\t\t\t\"channel\": 3\n" +
            "\t\t\t},\n" +
            "\t\t\t\"errorLimit\": {\n" +
            "\t\t\t\t\"record\": 0,\n" +
            "\t\t\t\t\"percentage\": 0.02\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"content\": [{\n" +
            "\t\t\t\t\"reader\": {\n" +
            "\t\t\t\t\t\"name\": \"mysqlreader\",\n" +
            "\t\t\t\t\t\"parameter\": {\n" +
            "\t\t\t\t\t\t\"username\": \"root\",\n" +
            "\t\t\t\t\t\t\"password\": \"root\",\n" +
            "\t\t\t\t\t\t\"column\": [\n" +
            "\t\t\t\t\t\t\t\"id\",\n" +
            "\t\t\t\t\t\t\t\"name\"\n" +
            "\t\t\t\t\t\t],\n" +
            "\t\t\t\t\t\t\"splitPk\": \"db_id\",\n" +
            "\t\t\t\t\t\t\"connection\": [{\n" +
            "\t\t\t\t\t\t\t\t\"querySql\": [\n" +
            "                                    \"select SerialNo,TotalYBPaidMoney,TotalSelfPaidMoney,TotalOweMoney,TotalSpecialMoney from BUS_AB_IP_FeeAccount\"\n" +
            "                                ],\n" +
            "\t\t\t\t\t\t\t\t\"jdbcUrl\": [\n" +
            "\t\t\t\t\t\t\t\t\t\"jdbc:mysql://127.0.0.1:3306/database\"\n" +
            "\t\t\t\t\t\t\t\t]\n" +
            "\t\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t\t]\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"writer\": {\n" +
            "\t\t\t\t\t\"name\": \"mysqlwriter\",\n" +
            "\t\t\t\t\t\"parameter\": {\n" +
            "\t\t\t\t\t\t\"writeMode\": \"insert\",\n" +
            "\t\t\t\t\t\t\"username\": \"root\",\n" +
            "\t\t\t\t\t\t\"password\": \"root\",\n" +
            "\t\t\t\t\t\t\"column\": [\n" +
            "\t\t\t\t\t\t\t\"SerialNo\",\n" +
            "\t\t\t\t\t\t\t\"TotalYBPaidMoney\",\n" +
            "\t\t\t\t\t\t\t\"TotalSelfPaidMoney\",\n" +
            "\t\t\t\t\t\t\t\"TotalOweMoney\",\n" +
            "\t\t\t\t\t\t\t\"TotalSpecialMoney\"\n" +
            "\t\t\t\t\t\t],\n" +
            "\t\t\t\t\t\t\"session\": [\n" +
            "\t\t\t\t\t\t\t\"set session sql_mode='ANSI'\"\n" +
            "\t\t\t\t\t\t],\n" +
            "\t\t\t\t\t\t\"preSql\": [\n" +
            "\t\t\t\t\t\t\t\"delete from test\"\n" +
            "\t\t\t\t\t\t],\n" +
            "\t\t\t\t\t\t\"connection\": [{\n" +
            "\t\t\t\t\t\t\t\t\"jdbcUrl\": \"jdbc:mysql://127.0.0.1:3306/datax?useUnicode=true&characterEncoding=gbk\",\n" +
            "\t\t\t\t\t\t\t\t\"table\": [\n" +
            "\t\t\t\t\t\t\t\t\t\"AB_IP_FeeAccount\"\n" +
            "\t\t\t\t\t\t\t\t]\n" +
            "\t\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t\t]\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t}\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t}\n" +
            "}";
}
