package org.kpi.senioroman4uk.tickets_accounting.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vladyslav on 01.12.2015.
 *
 */

@Component
public class StringToDateConverter implements Converter<Date, String> {
    @Override
    public String convert(Date date) {
        if (date == null)
            return null;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-M-dd");
        return dateFormatter.format(date);
    }
}
