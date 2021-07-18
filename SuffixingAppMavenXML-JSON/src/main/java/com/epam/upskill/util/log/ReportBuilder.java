package com.epam.upskill.util.log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import com.epam.upskill.util.validate.XML;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class ReportBuilder {
    private static final Path REPORT_PATH = Paths.get("src/main/resources/reports/file_controller_report.xml");
    private static final Path XML_REPORT_SCHEMA = Paths.get("src/main/resources/schemas/fc_report.xsd");
    private final Document REPORT_DOC;
    private final Element ROOT;
    private final Element CONFIG;
    private final Element FILES;

    public ReportBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        REPORT_DOC = builder.newDocument();
        ROOT = REPORT_DOC.createElement("report");
        REPORT_DOC.appendChild(ROOT);
        CONFIG = REPORT_DOC.createElement("config");
        ROOT.appendChild(CONFIG);
        FILES = REPORT_DOC.createElement("files");
        ROOT.appendChild(FILES);
    }

    public void addConfigInfo(final Path CONFIG_PATH) {
        CONFIG.setAttribute("src", CONFIG_PATH.toString());
    }

    public void addFileInfo(final Path CURRENT_NAME, final Path TARGET_NAME) {
        final Element FILE = REPORT_DOC.createElement("file");
        FILES.appendChild(FILE);
        final Element OLD_NAME = REPORT_DOC.createElement("oldName");
        OLD_NAME.appendChild(REPORT_DOC.createTextNode(CURRENT_NAME.toString()));
        FILE.appendChild(OLD_NAME);
        final Element NEW_NAME = REPORT_DOC.createElement("newName");
        NEW_NAME.appendChild(REPORT_DOC.createTextNode(TARGET_NAME.toString()));
        FILE.appendChild(NEW_NAME);
    }

    public void addExecutionTimeInfo(final long EXECUTION_TIME_VALUE) {
        final Element TIME = REPORT_DOC.createElement("executionTime");
        TIME.setAttribute("unit", "ms");
        TIME.appendChild(REPORT_DOC.createTextNode(String.valueOf(EXECUTION_TIME_VALUE)));
        ROOT.appendChild(TIME);
    }

    public void createReportFile() throws TransformerException, SAXException, IOException {
        final Element TIMESTAMP = REPORT_DOC.createElement("timestamp");
        TIMESTAMP.appendChild(REPORT_DOC.createTextNode(new Date().toString()));
        ROOT.appendChild(TIMESTAMP);
        DOMSource domSource = new DOMSource(REPORT_DOC);
        XML.validate(domSource, XML_REPORT_SCHEMA);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(domSource, new StreamResult(new File(REPORT_PATH.toString())));
    }
}
