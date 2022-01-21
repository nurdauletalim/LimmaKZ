package kz.reself.limma.filestorage.configuration;

import org.modelmapper.Converter;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Configuration
public class ModelMapperConfig {

    private static final Converter<Date, LocalDateTime> DATE_TO_LOCAL_DATE_TIME_CONVERTER = mappingContext -> {
        Date source = mappingContext.getSource();
        return source.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    };

    @Bean
    public ModelMapperImpl modelMapper() {
        ModelMapperImpl mapper = new ModelMapperImpl();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PROTECTED);

        mapper.addConverter(DATE_TO_LOCAL_DATE_TIME_CONVERTER);
        return mapper;
    }
}

