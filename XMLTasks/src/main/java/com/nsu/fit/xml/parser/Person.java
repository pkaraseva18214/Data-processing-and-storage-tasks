package com.nsu.fit.xml.parser;

import java.util.LinkedList;
import java.util.List;

public class Person {
    public String id;
    public String gender;
    public String firstName;
    public String surname;
    public int childrenNumber;
    public int siblingsNumber;
    public Person spouse;
    public Person father;
    public Person mother;
    public List<Person> brothers;
    public List<Person> sisters;
    public List<Person> daughters;
    public List<Person> sons;

    public List<Person> children;
    public List<Person> parents;
    public List<Person> siblings;

    public Person(){
        brothers = new LinkedList<>();
        sisters = new LinkedList<>();
        daughters = new LinkedList<>();
        sons = new LinkedList<>();
        children = new LinkedList<>();
        parents = new LinkedList<>();
        siblings = new LinkedList<>();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setChildrenNumber(int childrenNumber) {
        this.childrenNumber = childrenNumber;
    }

    public void setSiblingsNumber(int siblingsNumber) {
        this.siblingsNumber = siblingsNumber;
    }

    public void setParent(Person parent) {
        if (parent.gender.equals("male")){
            this.father = parent;
        } else {
            this.mother = parent;
        }
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    public void setSibling(Person sibling) {
        if (sibling.gender.equals("male")){
            brothers.add(sibling);
        } else {
            sisters.add(sibling);
        }
    }

    public void setChild(Person child) {
        if (child.gender.equals("male")){
            sons.add(child);
        } else {
            daughters.add(child);
        }
    }

    public String getId() {
        return id;
    }

    public String getGender() {
        return gender;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public int getChildrenNumber() {
        return childrenNumber;
    }

    public int getSiblingsNumber() {
        return siblingsNumber;
    }

    public Person getSpouse() {
        return spouse;
    }

    public List<Person> getDaughters(){
        return daughters;
    }

    public List<Person> getSons() {
        return sons;
    }

    public List<Person> getSisters() {
        return sisters;
    }

    public List<Person> getBrothers(){
        return brothers;
    }

    public Person getFather(){
        return father;
    }

    public Person getMother() {
        return mother;
    }

    public void setChildren(Person child) {
        children.add(child);
    }

    public List<Person> getChildren() {
        return children;
    }

    public void setParents(Person parent) {
        parents.add(parent);
    }

    public void setSiblings(Person sibling) {
        siblings.add(sibling);
    }

    public List<Person> getSiblings() {
        return siblings;
    }

    public List<Person> getParents() {
        return parents;
    }

    public void structure(){
        if (!parents.isEmpty()){
            for (Person p : parents){
                setParent(p);
            }
        }
        if (!children.isEmpty()){
            for (Person p : children){
                setChild(p);
            }
        }
        if (!siblings.isEmpty()){
            for (Person p : siblings){
                setSibling(p);
            }
        }
    }
}
