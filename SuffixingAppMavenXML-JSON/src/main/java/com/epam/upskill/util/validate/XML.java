package com.epam.upskill.util.validate;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class XML {
    public static void validate(final Path PATH_TO_XML, final Path PATH_TO_XSD) throws SAXException, IOException {
        Source xmlSource = new StreamSource(new File(PATH_TO_XML.toString()));
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(PATH_TO_XSD.toString()));
        Validator validator = schema.newValidator();
        validator.validate(xmlSource);
    }

    public static void validate(final DOMSource DOM_SOURCE, final Path PATH_TO_XSD) throws SAXException, IOException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(PATH_TO_XSD.toString()));
        Validator validator = schema.newValidator();
        validator.validate(DOM_SOURCE);
    }
}
