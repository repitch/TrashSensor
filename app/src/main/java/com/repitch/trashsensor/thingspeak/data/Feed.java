package com.repitch.trashsensor.thingspeak.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feed {

    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("entry_id")
    @Expose
    private Integer entryId;
    @SerializedName("field1")
    @Expose
    private String field1;
    @SerializedName("field2")
    @Expose
    private Object field2;
    @SerializedName("field3")
    @Expose
    private Object field3;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getEntryId() {
        return entryId;
    }

    public void setEntryId(Integer entryId) {
        this.entryId = entryId;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public Object getField2() {
        return field2;
    }

    public void setField2(Object field2) {
        this.field2 = field2;
    }

    public Object getField3() {
        return field3;
    }

    public void setField3(Object field3) {
        this.field3 = field3;
    }

}
