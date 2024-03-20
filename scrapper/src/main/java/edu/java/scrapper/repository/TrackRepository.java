package edu.java.scrapper.repository;

import edu.java.scrapper.domain.TrackRecord;
import java.util.List;

@SuppressWarnings("IllegalIdentifierName")
public interface TrackRepository {
    void add(TrackRecord record);

    void delete(TrackRecord record);

    List<TrackRecord> getAll();
}
