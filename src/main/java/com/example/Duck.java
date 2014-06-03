package com.example;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Duck {

    private String name = "Donald";
    private List<String> nicknames;
    private List<String> nephews;
    private List<String> children; // should give null

    public Duck() {
        nicknames = Arrays.asList("Don");
        nephews = Arrays.asList("Huey", "Dewey", "Louie");

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public void setNicknames(List<String> nicknames) {
        this.nicknames = nicknames;
    }

    public List<String> getNephews() {
        return nephews;
    }

    public void setNephews(List<String> nephews) {
        this.nephews = nephews;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

}
