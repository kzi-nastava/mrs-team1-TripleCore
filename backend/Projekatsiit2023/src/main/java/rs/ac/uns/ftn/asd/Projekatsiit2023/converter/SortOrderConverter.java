package rs.ac.uns.ftn.asd.Projekatsiit2023.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.SortOrder;

@Component
public class SortOrderConverter implements Converter<String, SortOrder> {

    @Override
    public SortOrder convert(String source) {
        if (source.isBlank()) {
            return SortOrder.DESC;
        }

        return SortOrder.valueOf(source.toUpperCase());
    }
}
