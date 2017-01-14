package com.repitch.trashsensor;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by repitch on 14.01.17.
 */
public class TrashReport implements Serializable {

    private static final String FIELD = "field";
    private static final String EQUALS = "=";
    private static final String AND = "&";

    private List<String> fields = new ArrayList<>();

    public static final int FIELD_VOLUME = 0;
    public static final int FIELD_TEMPERATURE = 1;

    public static final int FIELDS_COUNT = 2;

    public TrashReport() {
        for (int i = 0; i < FIELDS_COUNT; i++) {
            fields.add("");
        }
    }

    public TrashReport(@Nullable List<String> fields) {
        if (!CollectionUtils.hasSize(fields, FIELDS_COUNT)) {
            throw new IllegalArgumentException("Fields must not be null and must have size of " + FIELDS_COUNT);
        }
        fields.addAll(fields);
    }

    public String generateMessage() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.size() - 1; i++) {
            sb.append(FIELD)
                    .append(Integer.toString(i + 1))
                    .append(EQUALS)
                    .append(fields.get(i))
                    .append(AND);
        }
        sb.append(FIELD)
                .append(Integer.toString(fields.size()))
                .append(EQUALS)
                .append(fields.get(fields.size() - 1));
        return sb.toString();
    }

    public void setField(int fieldId, String fieldVal) {
        if (fieldId < 0 || fieldId >= fields.size()) {
            throw new IllegalArgumentException("There is no field with id " + fieldId + " maximum value: " + (fields.size() - 1));
        }
        fields.set(fieldId, fieldVal);
    }


}
