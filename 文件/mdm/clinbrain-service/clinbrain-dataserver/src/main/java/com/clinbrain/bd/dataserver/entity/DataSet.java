package com.clinbrain.bd.dataserver.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DataSet {
    String name;
    DataTable dataTable;


    @Data
    public static class DataTable{
        String tableId;
        int rowSize;
        List<DataRow> dataRow;
    }

    @Data
    public static class DataRow{
        Map row;
        int rowId;
    }
}
