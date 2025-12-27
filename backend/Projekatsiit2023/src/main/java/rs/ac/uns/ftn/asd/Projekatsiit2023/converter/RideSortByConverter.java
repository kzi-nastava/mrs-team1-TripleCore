package rs.ac.uns.ftn.asd.Projekatsiit2023.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.RideSortBy;

@Component
public class RideSortByConverter implements Converter<String, RideSortBy> {

    @Override
    public RideSortBy convert(String source) {
        if (source.isBlank()) {
            return RideSortBy.START_TIME;
        }

        return RideSortBy.valueOf(source.toUpperCase());
    }
}
