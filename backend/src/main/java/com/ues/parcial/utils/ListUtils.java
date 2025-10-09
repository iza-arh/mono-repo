package com.ues.parcial.utils;

import java.util.Collections;
import java.util.List;

// This utility class provides methods to handle lists safely and efficiently.
public class ListUtils {

    // This method returns an empty list if the input is null
    public static <T> List<T> emptyIfNull(List<T> list) { 
        return list == null ? Collections.emptyList() : list;
    }
}
