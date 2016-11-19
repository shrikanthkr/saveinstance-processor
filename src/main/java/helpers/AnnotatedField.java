package helpers;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import generator.Generator;
import generator.SaveLoadBaseGenerator;

/**
 * Created by shrikanth on 11/13/16.
 */

public class AnnotatedField {
    Element enclosingClass;
    Element element;
    SaveLoadBaseGenerator generator;
    String fieldType;

    public AnnotatedField(Element element) {
        this.element = element;
        enclosingClass = element.getEnclosingElement();
        fieldType = element.asType().toString();
        try {
            generator = Generator.getGenerator(fieldType);
        }catch (NullPointerException e){

        }
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Element getEnclosingClass() {
        return enclosingClass;
    }

    public void setEnclosingClass(TypeElement enclosingClass) {
        this.enclosingClass = enclosingClass;
    }

    public SaveLoadBaseGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(SaveLoadBaseGenerator generator) {
        this.generator = generator;
    }
}
