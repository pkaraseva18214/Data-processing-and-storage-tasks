package ru.nsu.xmltodb.d3;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class XMLtoDB {
    private final PreparedStatement putPerson;
    private final PreparedStatement putSibling;
    private final PreparedStatement putSpouse;
    private final PreparedStatement putParent;
    private final File file;

    public XMLtoDB(PreparedStatement putPerson,
                   PreparedStatement putSibling,
                   PreparedStatement putSpouse,
                   PreparedStatement putParent,
                   File file) {
        this.putPerson = putPerson;
        this.putSibling = putSibling;
        this.putParent = putParent;
        this.putSpouse = putSpouse;
        this.file = file;
    }

    private void addParameters(PreparedStatement s, String... parameters) throws SQLException {
        for (int i = 1; i <= parameters.length; i++) {
            s.setString(i, parameters[i - 1]);
        }
        s.addBatch();
    }

    public void writePeople() {
        try {
            XMLStreamReader xmlReader =
                    XMLInputFactory.newInstance()
                            .createXMLStreamReader(file.getName(), new BufferedReader(new FileReader(file)));
            Map<String, String> spouse = new HashMap<>();
            Map<String, String> mother = new HashMap<>();
            Map<String, String> father = new HashMap<>();

            Person person = null;
            while (xmlReader.hasNext()) {
                xmlReader.next();
                if (xmlReader.isStartElement()) {
                    switch (xmlReader.getLocalName()) {
                        case "person":
                            person = new Person();
                            person.setId(xmlReader.getAttributeValue(0));
                            person.setFirstname(xmlReader.getAttributeValue(1));
                            person.setLastName(xmlReader.getAttributeValue(2));
                            break;
                        case "gender":
                            xmlReader.next();
                            assert person != null;
                            person.setGender(xmlReader.getText().trim());
                            break;
                        case "mother":
                            assert person != null;
                            person.setMother(xmlReader.getAttributeValue(0));
                            break;
                        case "father":
                            assert person != null;
                            person.setFather(xmlReader.getAttributeValue(0));
                            break;
                        case "spouse":
                            assert person != null;
                            person.setSpouseId(xmlReader.getAttributeValue(0));
                            break;
                        case "sister":
                            assert person != null;
                            person.getSisters().add(xmlReader.getAttributeValue(0));
                            break;
                        case "brother":
                            assert person != null;
                            person.getBrothers().add(xmlReader.getAttributeValue(0));
                            break;
                        case "son":
                            assert person != null;
                            person.getSons().add(xmlReader.getAttributeValue(0));
                            break;
                        case "daughter":
                            assert person != null;
                            person.getDaughters().add(xmlReader.getAttributeValue(0));
                            break;
                    }
                } else if (xmlReader.isEndElement() && xmlReader.getLocalName().equals("person")) {
                    assert person != null;
                    addParameters(
                            putPerson,
                            person.getId(),
                            person.getGender(),
                            person.getFirstname(),
                            person.getLastName());

                    if (person.getMother() != null) {
                        if (!mother.containsKey(person.getId())) {
                            addParameters(putParent, person.getId(), person.getMother());
                            mother.put(person.getId(), person.getMother());
                        }
                    }
                    if (person.getFather() != null) {
                        if (!father.containsKey(person.getId())) {
                            addParameters(putParent, person.getId(), person.getFather());
                            father.put(person.getId(), person.getFather());
                        }
                    }
                    if (person.getSpouseId() != null) {
                        if (!(spouse.containsKey(person.getId())) || spouse.containsKey(person.getSpouseId())){
                            if (person.getGender().trim().contains("F")) {
                                addParameters(putSpouse, person.getId(), person.getSpouseId());
                            } else {
                                addParameters(putSpouse, person.getSpouseId(), person.getId());
                            }
                            spouse.put(person.getSpouseId(), person.getId());
                        }
                    }
                    for (String sister : person.getSisters()) {
                        addParameters(putSibling, sister, person.getId());
                    }
                    for (String brother : person.getBrothers()) {
                        addParameters(putSibling, brother, person.getId());
                    }
                    for (String son : person.getSons()) {
                        if ((person.getGender().equals("F") && !mother.containsKey(son))
                        || (person.getGender().equals("M") && !father.containsKey(son))){
                            addParameters(putParent, son, person.getId());
                            mother.put(son, person.getId());
                        }
                    }
                    for (String daughter : person.getDaughters()) {
                        if ((person.getGender().equals("F") && !mother.containsKey(daughter))
                                || (person.getGender().equals("M") && !father.containsKey(daughter))) {
                            addParameters(putParent, daughter, person.getId());
                        }
                    }
                }
            }
            putPerson.executeBatch();
            putSibling.executeBatch();
            putSpouse.executeBatch();
            putParent.executeBatch();
        } catch (SQLException | XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
