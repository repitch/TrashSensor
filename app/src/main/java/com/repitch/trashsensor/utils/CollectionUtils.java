package com.repitch.trashsensor.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.Collection;
import java.util.List;

/**
 * Created by repitch on 14.01.17.
 */
public class CollectionUtils {

    private CollectionUtils() {
        // empty
    }

    public static boolean isEmpty(@Nullable Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean hasSize(@Nullable List<?> list, int fieldsCount) {
        return list != null && list.size() == fieldsCount;
    }

    public static void switchVisibility(@NonNull View content, @NonNull View placeholder, @Nullable Collection<?> dataset) {
        boolean isEmpty = isEmpty(dataset);
        content.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        placeholder.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }
}
