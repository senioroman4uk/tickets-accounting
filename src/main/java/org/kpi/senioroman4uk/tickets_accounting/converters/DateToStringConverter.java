package org.kpi.senioroman4uk.tickets_accounting.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vladyslav on 30.11.2015.
 *
 */

@Component
public class DateToStringConverter implements Converter<String, Date> {

    @Override
    public Date convert(String s) {
        if(s == null )
            return null;
        s = s.trim();
        if (s.isEmpty())
            return null;

        Date date;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-M-dd");
        try {
            date = dateFormatter.parse(s);
        } catch (Exception ex) {
            date = null;
        }
        return date;
    }
}
