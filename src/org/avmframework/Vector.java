package org.avmframework;

import java.util.LinkedList;
import java.util.List;

public class Vector implements Element {

    protected List<Element> elements = new LinkedList<>();

    public Vector() {
    }

    public void addElement(Element element) {
        elements.add(element);
    }
}
