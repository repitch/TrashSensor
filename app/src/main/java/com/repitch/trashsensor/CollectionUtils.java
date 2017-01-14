package com.repitch.trashsensor;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by repitch on 14.01.17.
 */
public class CollectionUtils {
    public static boolean hasSize(@Nullable List<?> list, int fieldsCount) {
        return list != null && list.size() == fieldsCount;
    }
}
