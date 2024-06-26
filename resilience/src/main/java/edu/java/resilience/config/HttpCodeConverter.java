package edu.java.resilience.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class HttpCodeConverter implements Converter<Integer, HttpStatusCode> {
    @Override
    public HttpStatusCode convert(Integer source) {
        return HttpStatusCode.valueOf(source);
    }
}
