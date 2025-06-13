package com.example.ingestionplatform.dao;

import java.io.Serializable;

public class SchemaField implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String fieldName;
    private String dataType;
    private boolean nullable;
    private String isPrimaryKey;
    private String defaultValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(String isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }


    @Override
    public String toString() {
        return "SchemaField{" +
                "id=" + id +
                ", fieldName='" + fieldName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", nullable=" + nullable +
                ", isPrimaryKey='" + isPrimaryKey + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }

}