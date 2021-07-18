package com.epam.upskill.util.сonfig;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "configuration")
@XmlRootElement
public class Configuration {
    @XmlElement(name = "suffix")
    private String suffix;
    @XmlElementWrapper(name = "fileNames")
    @XmlElement(name = "fileName")
    private String[] fileNames;

    public String getSuffix() {
        return suffix;
    }

    public String[] getFileNames() {
        return fileNames;
    }
}
