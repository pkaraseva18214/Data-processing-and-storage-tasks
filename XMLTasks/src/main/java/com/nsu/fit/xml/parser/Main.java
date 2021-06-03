package com.nsu.fit.xml.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static List<Person> people = new LinkedList<>();
    public static HashMap<String, Person> idAndPerson = new HashMap<>();
    public static HashMap<String, Person> nameAndPerson = new HashMap<>();
    public static int peopleNumber;

    public static void main(String[] args) {
        String fileName = "C:\\Users\\Polina\\Documents\\XMLParser\\src\\main\\java\\com\\nsu\\fit\\xml\\parser\\people.xml";
        people = parseXML(fileName);
        System.out.println(people.size());
    }

    private static List<Person> parseXML(String fileName) {
        Person person = null;
        Person relative;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
            //<people>
                    switch (startElement.getName().getLocalPart()) {
                        case "people": {
                            Attribute attr = startElement.getAttributeByName(new QName("count"));
                            peopleNumber = Integer.parseInt(attr.getValue());
                            //<person>
                            break;
                        }
                        case "person":
                            person = new Person();
                            if (startElement.getAttributeByName(new QName("id")) != null) {
                                Attribute attr = startElement.getAttributeByName(new QName("id"));
                                if (idAndPerson.containsKey(attr.getValue())) {
                                    person = getWholePerson(idAndPerson.get(attr.getValue()), person);
                                } else {
                                    person.setId(attr.getValue());
                                    idAndPerson.put(person.id, person);
                                }
                            } else if (startElement.getAttributeByName(new QName("name")) != null) {
                                Attribute attr = startElement.getAttributeByName(new QName("name"));
                                String[] fullname = attr.getValue().split(" ");
                                if (nameAndPerson.containsKey(fullname[0] + " " + fullname[1])) {
                                    person = getWholePerson(nameAndPerson.get(fullname[0] + " " + fullname[1]), person);
                                } else {
                                    person.setFirstName(fullname[0]);
                                    person.setSurname(fullname[1]);
                                    nameAndPerson.put(fullname[0] + " " + fullname[1], person);
                                }
                            }
                            break;
                        //<children-number>
                        case "children-number": {
                            Attribute attr = startElement.getAttributeByName(new QName("value"));
                            person.setChildrenNumber(Integer.parseInt(attr.getValue()));
                            //<siblings-number>
                            break;
                        }
                        case "siblings-number": {
                            Attribute attr = startElement.getAttributeByName(new QName("value"));
                            person.setSiblingsNumber(Integer.parseInt(attr.getValue()));
                            //<firstname>
                            break;
                        }
                        case "firstname":
                            if (startElement.getAttributeByName(new QName("value")) != null) {
                                Attribute attr = startElement.getAttributeByName(new QName("value"));
                                person.setFirstName(attr.getValue());
                            } else {
                                xmlEvent = xmlEventReader.nextEvent();
                                person.setFirstName(xmlEvent.asCharacters().getData());
                            }
                            //<gender>
                            break;
                        case "gender":
                            if (startElement.getAttributeByName(new QName("value")) != null) {
                                Attribute attr = startElement.getAttributeByName(new QName("value"));
                                person.setGender(attr.getValue());
                            } else {
                                xmlEvent = xmlEventReader.nextEvent();
                                if (xmlEvent.asCharacters().getData().equals("M")) {
                                    person.setGender("male");
                                } else {
                                    person.setGender("female");
                                }
                            }
                            //<surname>
                            break;
                        case "surname": {
                            Attribute attr = startElement.getAttributeByName(new QName("value"));
                            person.setSurname(attr.getValue());
                            //<first>
                            break;
                        }
                        case "first":
                            xmlEvent = xmlEventReader.nextEvent();
                            person.setFirstName(xmlEvent.asCharacters().getData());
                            //<family> <family-name>
                            break;
                        case "family":
                        case "family-name":
                            xmlEvent = xmlEventReader.nextEvent();
                            person.setSurname(xmlEvent.asCharacters().getData());
                            //<id>
                            break;
                        case "id": {
                            Attribute attr = startElement.getAttributeByName(new QName("value"));
                            if (!idAndPerson.containsKey(attr.getValue())) {
                                person.setId(attr.getValue());
                                idAndPerson.put(attr.getValue(), person);
                            } else {
                                person = getWholePerson(idAndPerson.get(attr.getValue()), person);
                            }
                            // <mother>
                            break;
                        }
                        case "mother": {
                            xmlEvent = xmlEventReader.nextEvent();
                            String[] fullname = xmlEvent.asCharacters().getData().split(" ");
                            if (nameAndPerson.containsKey(fullname[0] + " " + fullname[1])) {
                                relative = nameAndPerson.get(fullname[0] + " " + fullname[1]);
                            } else {
                                relative = new Person();
                                relative.setFirstName(fullname[0]);
                                relative.setSurname(fullname[1]);
                                nameAndPerson.put(fullname[0] + " " + fullname[1], relative);
                            }
                            relative.setGender("female");
                            relative.setChildren(person);
                            person.setParents(relative);
                            //<father>
                            break;
                        }
                        case "father": {
                            xmlEvent = xmlEventReader.nextEvent();
                            String[] fullname = xmlEvent.asCharacters().getData().split(" ");
                            if (nameAndPerson.containsKey(fullname[0] + " " + fullname[1])) {
                                relative = nameAndPerson.get(fullname[0] + " " + fullname[1]);
                            } else {
                                relative = new Person();
                                relative.setFirstName(fullname[0]);
                                relative.setSurname(fullname[1]);
                                nameAndPerson.put(fullname[0] + " " + fullname[1], relative);
                            }
                            relative.setGender("male");
                            relative.setChildren(person);
                            person.setParents(relative);
                            //<husband>
                            break;
                        }
                        case "husband": {
                            Attribute attr = startElement.getAttributeByName(new QName("value"));
                            if (idAndPerson.containsKey(attr.getValue())) {
                                relative = idAndPerson.get(attr.getValue());
                            } else {
                                relative = new Person();
                                relative.setId(attr.getValue());
                                idAndPerson.put(attr.getValue(), relative);
                            }
                            relative.setGender("male");
                            relative.setSpouse(person);
                            person.setGender("female");
                            person.setSpouse(relative);
                            //<wife>
                            break;
                        }
                        case "wife": {
                            Attribute attr = startElement.getAttributeByName(new QName("value"));
                            if (idAndPerson.containsKey(attr.getValue())) {
                                relative = idAndPerson.get(attr.getValue());
                            } else {
                                relative = new Person();
                                relative.setId(attr.getValue());
                                idAndPerson.put(attr.getValue(), relative);
                            }
                            relative.setGender("female");
                            relative.setSpouse(person);
                            person.setGender("male");
                            person.setSpouse(relative);
                            //<daughter>
                            break;
                        }
                        case "daughter": {
                            Attribute attr = startElement.getAttributeByName(new QName("id"));
                            if (idAndPerson.containsKey(attr.getValue())) {
                                relative = idAndPerson.get(attr.getValue());
                            } else {
                                relative = new Person();
                                relative.setId(attr.getValue());
                                idAndPerson.put(attr.getValue(), relative);
                            }
                            relative.setGender("female");
                            relative.setParents(person);
                            person.setChildren(relative);
                            //<son>
                            break;
                        }
                        case "son": {
                            Attribute attr = startElement.getAttributeByName(new QName("id"));
                            if (idAndPerson.containsKey(attr.getValue())) {
                                relative = idAndPerson.get(attr.getValue());
                            } else {
                                relative = new Person();
                                relative.setId(attr.getValue());
                                idAndPerson.put(attr.getValue(), relative);
                            }
                            relative.setGender("male");
                            relative.setParents(person);
                            person.setChildren(relative);
                            //<sister>
                            break;
                        }
                        case "sister": {
                            xmlEvent = xmlEventReader.nextEvent();
                            String[] fullname = xmlEvent.asCharacters().getData().split(" ");
                            if (nameAndPerson.containsKey(fullname[0] + " " + fullname[1])) {
                                relative = nameAndPerson.get(fullname[0] + " " + fullname[1]);
                            } else {
                                relative = new Person();
                                relative.setFirstName(fullname[0]);
                                relative.setSurname(fullname[1]);
                                nameAndPerson.put(fullname[0] + " " + fullname[1], relative);
                            }
                            relative.setGender("female");
                            relative.setSiblings(person);
                            person.setSiblings(relative);
                            //<brother>
                            break;
                        }
                        case "brother": {
                            xmlEvent = xmlEventReader.nextEvent();
                            String[] fullname = xmlEvent.asCharacters().getData().split(" ");
                            if (nameAndPerson.containsKey(fullname[0] + " " + fullname[1])) {
                                relative = nameAndPerson.get(fullname[0] + " " + fullname[1]);
                            } else {
                                relative = new Person();
                                relative.setFirstName(fullname[0]);
                                relative.setSurname(fullname[1]);
                                nameAndPerson.put(fullname[0] + " " + fullname[1], relative);
                            }
                            relative.setGender("male");
                            relative.setSiblings(person);
                            person.setSiblings(relative);
                            //<spouse>
                            break;
                        }
                        case "spouce":
                            if (startElement.getAttributeByName(new QName("value")) != null) {
                                Attribute attr = startElement.getAttributeByName(new QName("value"));
                                if (!attr.getValue().equals("NONE")) {
                                    String[] fullname = attr.getValue().split(" ");
                                    if (nameAndPerson.containsKey(fullname[0] + " " + fullname[1])) {
                                        relative = nameAndPerson.get(fullname[0] + " " + fullname[1]);
                                    } else {
                                        relative = new Person();
                                        relative.setFirstName(fullname[0]);
                                        relative.setSurname(fullname[1]);
                                        nameAndPerson.put(fullname[0] + " " + fullname[1], relative);
                                    }
                                    relative.setSpouse(person);
                                    person.setSpouse(relative);
                                }
                            }
                            break;
                        case "child": {
                            xmlEvent = xmlEventReader.nextEvent();
                            String[] fullname = xmlEvent.asCharacters().getData().split(" ");
                            if (nameAndPerson.containsKey(fullname[0] + " " + fullname[1])) {
                                relative = nameAndPerson.get(fullname[0] + " " + fullname[1]);
                            } else {
                                relative = new Person();
                                relative.setFirstName(fullname[0]);
                                relative.setSurname(fullname[1]);
                                nameAndPerson.put(fullname[0] + " " + fullname[1], relative);
                            }
                            relative.setParents(person);
                            person.setChildren(relative);
                            //<siblings>
                            break;
                        }
                        case "siblings": {
                            Attribute attr = startElement.getAttributeByName(new QName("val"));
                            if (attr != null) {
                                String[] siblingsIdList = attr.getValue().split(" ");
                                for (String s : siblingsIdList) {
                                    if (idAndPerson.containsKey(s)) {
                                        relative = idAndPerson.get(s);
                                    } else {
                                        relative = new Person();
                                        relative.setId(s);
                                        idAndPerson.put(s, relative);
                                    }
                                    relative.setSiblings(person);
                                    person.setSiblings(relative);
                                }
                            }
                            //<parent>
                            break;
                        }
                        case "parent":
                            if (startElement.getAttributeByName(new QName("value")) != null) {
                                Attribute attr = startElement.getAttributeByName(new QName("value"));
                                if (!attr.getValue().equals("UNKNOWN")) {
                                    if (idAndPerson.containsKey(attr.getValue())) {
                                        relative = idAndPerson.get(attr.getValue());
                                    } else {
                                        relative = new Person();
                                        relative.setId(attr.getValue());
                                        idAndPerson.put(attr.getValue(), relative);
                                    }
                                    relative.setChildren(person);
                                    person.setParents(relative);
                                }
                            }
                            break;
                    }
                }

                if (xmlEvent.isEndElement()) {
                    EndElement endElement = xmlEvent.asEndElement();
                    if (endElement.getName().getLocalPart().equals("person")) {
                        if (person.firstName != null && person.surname != null){
                            if (!nameAndPerson.containsKey(person.firstName + " " + person.surname)){
                                nameAndPerson.put(person.firstName + " " + person.surname, person);
                            }
                        }
                    }
                }
            }

        } catch (FileNotFoundException |
                XMLStreamException e) {
            e.printStackTrace();
        }
        return new LinkedList(idAndPerson.values());
    }

    private static Person getWholePerson(Person fromMap, Person actual){
        if (actual.getId() != null){
            fromMap.setId(fromMap.getId());
        }
        if (actual.getFirstName() != null){
            fromMap.setFirstName(fromMap.getFirstName());
        }
        if (actual.getSurname() != null){
            fromMap.setSurname(fromMap.getSurname());
        }
        if (actual.getGender() != null){
            fromMap.setGender(fromMap.getGender());
        }
        if (actual.getSpouse() != null){
            fromMap.setSpouse(fromMap.getSpouse());
        }
        if (!actual.getChildren().isEmpty()){
            if (fromMap.children.isEmpty()){
                fromMap.children = actual.children;
            }
        }
        if (!actual.getParents().isEmpty()){
            if (fromMap.parents.isEmpty()){
                fromMap.parents = actual.parents;
            }
        }
        if (!actual.getSiblings().isEmpty()){
            if (fromMap.siblings.isEmpty()){
                fromMap.siblings = actual.siblings;
            }
        }
        return fromMap;
    }

}
