package edu.java.scrapper.controller.dto;

import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

@Validated
public record LinkRequest(@URL String url) {
}
