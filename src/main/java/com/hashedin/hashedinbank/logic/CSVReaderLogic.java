package com.hashedin.hashedinbank.logic;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.hashedin.hashedinbank.exception.DataTransformationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CSVReaderLogic {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CsvMapper csvMapper = new CsvMapper();

    public <T> Object convertLineTextToObject(Class<T> className, String currentLine, String headerLine) {
        try {
            List<Map<?, ?>> list;
            try (MappingIterator<Map<?, ?>> mappingIterator = csvMapper.readerFor(className)
                    .with(csvSchema())
                    .readValues(headerLine + currentLine)) {
                list = mappingIterator.readAll();
            }
            String recordJson = objectMapper.writeValueAsString(list.get(0));
            return objectMapper.readValue(recordJson, className);
        } catch (IOException e) {
            throw new DataTransformationException("Unable to convert record to java object");
        }
    }

    private CsvSchema csvSchema() {
        return CsvSchema.emptySchema().withHeader();
    }
}