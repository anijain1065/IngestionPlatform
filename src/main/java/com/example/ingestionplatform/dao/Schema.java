package com.example.ingestionplatform.dao;

import java.util.List;

public class Schema {
    public Schema(){}

    public Schema(int schemaId, List<SchemaField> schemaFieldList) {
        this.schemaId = schemaId;
        this.schemaFieldList = schemaFieldList;
    }

    private int schemaId;
    List<SchemaField> schemaFieldList;

    public List<SchemaField> getSchemaFieldList() {
        return schemaFieldList;
    }

    public void setSchemaFieldList(List<SchemaField> schemaFieldList) {
        this.schemaFieldList = schemaFieldList;
    }

    public int getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(int schemaId) {
        this.schemaId = schemaId;
    }

    @Override
    public String toString() {
        return "Schema{" +
                "schemaId=" + schemaId +
                ", schemaFieldList=" + schemaFieldList +
                '}';
    }

}