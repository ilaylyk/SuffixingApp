package com.epam.upskill.start;

import com.epam.upskill.service.file.FileWorker;
import com.epam.upskill.util.сonfig.ConfigReader;
import com.epam.upskill.util.сonfig.Configuration;
import com.epam.upskill.util.log.ReportBuilder;
import com.epam.upskill.util.validate.XML;
import com.google.gson.JsonParseException;
import jakarta.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileController {

    private static final Path DIRECTORY = Paths.get("src/main/resources/files/");
    private static final Path CONFIG = Paths.get("src/main/resources/config.xml");
    private static final Path XML_CONFIG_SCHEMA = Paths.get("src/main/resources/schemas/fc_config.xsd");
    private static final Logger LOGGER = LogManager.getLogger(FileController.class);

    public static void main(String[] args) {
        final long START_TIME = System.currentTimeMillis();
        LOGGER.trace("*** The SuffixingApp is running ***");
        try {
            XML.validate(CONFIG, XML_CONFIG_SCHEMA);
            Configuration configuration = Objects.requireNonNull(ConfigReader.read(CONFIG), "Check config path");
            ReportBuilder reportBuilder = new ReportBuilder();
            reportBuilder.addConfigInfo(CONFIG);
            String suffix = configuration.getSuffix();
            String[] fileNames = configuration.getFileNames();
            LOGGER.debug("{} files found in total", fileNames.length);
            for (String fileName : fileNames) {
                final Path currentName = DIRECTORY.resolve(fileName);
                final Path targetName = DIRECTORY.resolve(suffix.concat(fileName));
                FileWorker.renameFile(currentName, targetName);
                reportBuilder.addFileInfo(currentName, targetName);
            }
            final long EXECUTION_TIME = System.currentTimeMillis() - START_TIME;
            LOGGER.trace("*** Shutting down the SuffixingApp (execution time is {} ms) ***\n", EXECUTION_TIME);
            reportBuilder.addExecutionTimeInfo(EXECUTION_TIME);
            reportBuilder.createReportFile();
        } catch (IllegalArgumentException | IOException | NullPointerException | JAXBException | JsonParseException |
                ParserConfigurationException | TransformerException | SAXException ex) {
            LOGGER.error("*** Shutting down the SuffixingApp with error (execution time is {} ms) ***\n",
                    System.currentTimeMillis() - START_TIME, ex);
            ex.printStackTrace();
        }
    }
}

