package com.clinbrain.bd;
import com.clinbrain.bd.dataserver.entity.DataSet;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GpTest {

    static String url = "jdbc:postgresql://192.168.0.113:5432/cdr";
    static String user = "gpadmin";
    static String pswd = "gpadmin";
    /*static String url = "jdbc:postgresql://127.0.0.1:3307/cdr";
    static String user = "root";
    static String pswd = "root";*/
    /*static String sql = "select * from (select hospitalno as no,hospitalname as name," +
            "serialno,feeno,invoiceno,returnfeeno from  ab_op_feelist  limit 10000 offset 3) s";*/
    static String sql = "select * from ab_op_feelist  limit 10";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]) throws SQLException {
        //创建连接
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(url, user, pswd);
            statement = connection.prepareStatement(sql);
            //statement.setInt(1,11);
            resultSet = statement.executeQuery();
            DataSet dataSet = tranToDataSet(resultSet);
            System.out.println(dataSet);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(resultSet !=null) resultSet.close();
            if(statement !=null) statement.close();
            if(connection !=null) connection.close();
        }
    }

    public static DataSet tranToDataSet(ResultSet resultSet) throws SQLException {
        DataSet dataSet = new DataSet();
        dataSet.setName("dataSet");
        ResultSetMetaData  metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();
        int rowId=0;
        DataSet.DataTable table = new DataSet.DataTable();
        List<DataSet.DataRow> rows = new ArrayList<DataSet.DataRow>();
        while (resultSet.next()){
            DataSet.DataRow row = new DataSet.DataRow();
            rowId++;
            Map map = new HashMap();
            for (int i =1 ;i<=count;i++){
                map.put(metaData.getColumnName(i),resultSet.getObject(i));
                row.setRow(map);
            }
            row.setRowId(rowId);
            rows.add(row);
            table.setTableId("");
            table.setDataRow(rows);
            table.setRowSize(rows.size());
            dataSet.setDataTable(table);
        }
        return dataSet;
    }
}
