package com.repitch.trashsensor.thingspeak.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "channel")
public class Channel implements Serializable {

    @DatabaseField(id = true)
    @SerializedName("id")
    @Expose
    private int id;

    @DatabaseField
    @SerializedName("name")
    @Expose
    private String name;

    @DatabaseField
    @SerializedName("description")
    @Expose
    private String description;

    @DatabaseField
    @SerializedName("latitude")
    @Expose
    private String latitude;

    @DatabaseField
    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("field1")
    @Expose
    private String field1;

    @SerializedName("field2")
    @Expose
    private String field2;

    @SerializedName("field3")
    @Expose
    private String field3;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @DatabaseField
    @SerializedName("last_entry_id")
    @Expose
    private int lastEntryId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getLastEntryId() {
        return lastEntryId;
    }

    public void setLastEntryId(int lastEntryId) {
        this.lastEntryId = lastEntryId;
    }

}
