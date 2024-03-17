package edu.java.scrapper.domain;

import lombok.Data;

/**
 * User - link relation record
 *
 * @author Alexander Ponomarev
 */
@Data
public class TrackRecord {
    private final Long chatId;
    private final Long linkId;
}
