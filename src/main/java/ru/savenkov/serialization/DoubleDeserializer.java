package ru.savenkov.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;

public class DoubleDeserializer extends JsonDeserializer<Double> {
    private static final Logger log = LoggerFactory.getLogger(DoubleDeserializer.class);
    @Override
    public Double deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        log.info("In method");
        String floatString = parser.getText();
        if (floatString.contains(",")) {
            floatString = floatString.replace(",", ".");
        }
        log.info("exit");
        return Double.valueOf(floatString);
    }
}
