package com.netty.rpc.serializer.serializer.old.entity;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * @author ex-pengfx
 */
@Data
public class Dept implements Serializable {
    @NonNull
    private String deptCode;
    @NonNull
    private String deptName;

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Dept(String deptCode, String deptName) {
        this.deptCode = deptCode;
        this.deptName = deptName;
    }
}
