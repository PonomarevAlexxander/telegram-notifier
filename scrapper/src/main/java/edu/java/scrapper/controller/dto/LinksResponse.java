package edu.java.scrapper.controller.dto;

import edu.java.scrapper.dto.LinkDTO;
import java.util.List;

public record LinksResponse(
    List<LinkDTO> links,
    Integer size
) {
}
