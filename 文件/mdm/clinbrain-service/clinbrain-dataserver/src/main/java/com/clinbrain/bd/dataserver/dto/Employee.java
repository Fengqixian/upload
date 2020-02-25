package com.clinbrain.bd.dataserver.dto;

import java.util.Date;

public class Employee {
    private int empId;
    private String empName;
    private Date empDate;
    public int getEmpId() {
        return empId;
    }
    public void setEmpId(int empId) {
        this.empId = empId;
    }
    public String getEmpName() {
        return empName;
    }
    public void setEmpName(String empName) {
        this.empName = empName;
    }
    public Date getEmpDate() {
        return empDate;
    }
    public void setEmpDate(Date empDate) {
        this.empDate = empDate;
    }
    @Override
    public String toString() {
        return "Employee [empId=" + empId + ", empName=" + empName + ", empDate=" + empDate + "]";
    }

}
