package it.vergeit.galaxian.data;

import java.util.List;

/**
 * Allows multiple registrations.
 */
public interface MultipleWatchDataListener {

    List<DataType> getDataTypes();

    void onDataUpdate(DataType type, Object value);
}
