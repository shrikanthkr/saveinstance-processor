package com.example.processor;

import android.os.Bundle;

import com.example.SaveInstance;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;


import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import generator.Generator;
import generator.SaveLoadBaseGenerator;
import helpers.AnnotatedField;
import helpers.OwnerClass;
import helpers.ProcessingException;

/**
 * Created by shrikanth on 11/13services/16.
 */

@SupportedAnnotationTypes("com.example.SaveInstance")
@AutoService(Processor.class)
public class SaveInstanceProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    Map<Element, OwnerClass> ownerClassMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        for(Element element : roundEnvironment.getElementsAnnotatedWith(SaveInstance.class)){
            validateElement(element);
            AnnotatedField annotatedField = new AnnotatedField(element);
            SaveLoadBaseGenerator generator = Generator.getGenerator(elementUtils, typeUtils, annotatedField);
            annotatedField.setGenerator(generator);
            Element ownerElement = annotatedField.getEnclosingClass();
            OwnerClass ownerClass = ownerClassMap.get(element.getEnclosingElement());
            if(ownerClass == null){
                ownerClass = new OwnerClass(filer, ownerElement, typeUtils, elementUtils);
                ownerClassMap.put(ownerElement, ownerClass);
            }
            ownerClass.addField(annotatedField);
        }
        Iterator<Element> iterator = ownerClassMap.keySet().iterator();
        while (iterator.hasNext()){
            Element ownerElement = iterator.next();
            OwnerClass ownerClass = ownerClassMap.get(ownerElement);
            ownerClass.generateCode();
        }
        ownerClassMap.clear();
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void validateElement(Element element){
        if(!( element.getKind() == ElementKind.FIELD)){
            new ProcessingException(element, "You cannot use SaveInstance annotations on elements other than fields");

        }
    }
    private void log(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format(msg));
    }

}
