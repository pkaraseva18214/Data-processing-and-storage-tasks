package com.nsu.fit.xml.lab3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.transform.stream.StreamSource;

public class Main {

    public static void main(String[] args) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
            File schemaFile = new File("src\\main\\java\\com\\nsu\\fit\\xml\\lab2\\peopleScheme.xsd");
            docBuilderFactory.setSchema(schemaFactory.newSchema(schemaFile));
            docBuilderFactory.setNamespaceAware(true);
            Document doc = docBuilderFactory.newDocumentBuilder().parse("src\\main\\resources\\people1.xml");

            StreamSource styleSheet = new StreamSource(new File("src\\main\\resources\\transform.xsl"));
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer(styleSheet);
            transformer.transform(new DOMSource(doc),
                    new StreamResult(new FileOutputStream("src\\main\\resources\\output.html")));
        } catch (SAXException | ParserConfigurationException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
