package com.epam.upskill.util.сonfig;

import com.epam.upskill.start.FileController;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class ConfigReader {
    private static final String JSON_EXTENSION_PATTERN = ".*\\.json$";
    private static final String XML_EXTENSION_PATTERN = ".*\\.xml$";
    private static final Logger LOGGER = LogManager.getLogger(FileController.class);

    public static Configuration read(final Path CONFIG_PATH) throws IllegalArgumentException, IOException,
            JAXBException, JsonParseException {
        Configuration configuration;
        if (CONFIG_PATH.toString().matches(JSON_EXTENSION_PATTERN)) {
            Gson gson = new Gson();
            try (FileReader fileReader = new FileReader(CONFIG_PATH.toString())) {
                JsonReader jsonReader = new JsonReader(fileReader);
                configuration = gson.fromJson(jsonReader, Configuration.class);
                LOGGER.debug("Read configuration from JSON: {}", configuration);
            }
        } else if (CONFIG_PATH.toString().matches(XML_EXTENSION_PATTERN)) {
            try (FileReader fileReader = new FileReader(CONFIG_PATH.toString())) {
                JAXBContext context = JAXBContext.newInstance(Configuration.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                configuration = (Configuration) unmarshaller.unmarshal(fileReader);
                LOGGER.debug("Read configuration from XML: {}", configuration);
            }
        } else {
            throw new IllegalArgumentException("Invalid name of configuration file!");
        }
        return configuration;
    }
}
