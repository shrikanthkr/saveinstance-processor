package helpers;

import android.os.Bundle;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by shrikanth on 11/13/16.
 */

public class OwnerClass {
    Element classElement;
    Types typeUtils;
    Elements elementUtils;
    List<AnnotatedField> fields;
    Filer filer;
    String originalClassName;
    private boolean hasSuper = false;
    Element superTypeElement;
    private static final String SUFFIX = "$$Proxy";
    private static final String OUTSTATE = "outstate";
    private static final String ANDROID_PREFIX = "android";


    public OwnerClass(Filer filer, Element classElement, Types typeUtils, Elements elementUtils) {
        this.filer = filer;
        this.classElement = classElement;
        this.fields = new ArrayList<>();
        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
        this.originalClassName =  classElement.getSimpleName().toString();
        for (TypeMirror supertype : typeUtils.directSupertypes(classElement.asType())) {
            DeclaredType declared = (DeclaredType)supertype; //you should of course check this is possible first
            superTypeElement = declared.asElement();
            String superPackageName = elementUtils.getPackageOf(superTypeElement).getQualifiedName().toString();
            hasSuper = !superPackageName.startsWith(ANDROID_PREFIX);
            break;
        }
    }

    public List<AnnotatedField> getFields() {
        return fields;
    }

    public void setFields(List<AnnotatedField> fields) {
        this.fields = fields;
    }

    public void addField(AnnotatedField field){
        fields.add(field);
    }

    public void generateCode(){
        String className = originalClassName + SUFFIX;
        TypeSpec.Builder builder = TypeSpec.classBuilder(className);
        buildConstructor(builder);
        buildSaveMethod(builder);
        buildLoadMethod(builder);
        if(hasSuper){
            TypeName superClassTypeName = ClassName.get(elementUtils.getPackageOf(superTypeElement).getQualifiedName().toString(),
                    superTypeElement.getSimpleName().toString() + SUFFIX);
            builder.superclass(superClassTypeName);
        }
        TypeSpec typeSpec = builder.build();
        PackageElement packageElement = elementUtils.getPackageOf(classElement);
        try {
            JavaFile.builder(packageElement.getQualifiedName().toString(), typeSpec).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildConstructor(TypeSpec.Builder builder){
        String originalClassName =  classElement.getSimpleName().toString();
        TypeName typeName = TypeName.get(classElement.asType());
        FieldSpec.Builder spec;
        String fieldName = StringUtils.uncapitalize(originalClassName);
        spec = FieldSpec.builder(typeName, fieldName, Modifier.PUBLIC);
        builder.addField(spec.build());
        builder.addModifiers(Modifier.PUBLIC);


        MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
        if(hasSuper){
            constructor.addStatement("super("+fieldName+")");
        }
        constructor.addModifiers(Modifier.PUBLIC);
        constructor.addParameter(typeName, fieldName);
        constructor.addStatement("this.$N = $N", fieldName, fieldName);

        builder.addMethod(constructor.build());
    }

    private void buildSaveMethod(TypeSpec.Builder builder){
        MethodSpec.Builder method = MethodSpec.methodBuilder("save")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Bundle.class, OUTSTATE);
        method.addJavadoc("Total number of fields $L" , fields.size());
        if(hasSuper){
            method.addStatement("super.save($N)", OUTSTATE);
        }
        Iterator<AnnotatedField> iterator = fields.iterator();
        while (iterator.hasNext()){
            AnnotatedField field = iterator.next();
            generateFieldSaveStatements(field, method);
        }
        builder.addMethod(method.build());
    }

    private void buildLoadMethod(TypeSpec.Builder builder){
        MethodSpec.Builder method = MethodSpec.methodBuilder("load")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Bundle.class, OUTSTATE);
        method.addJavadoc("Total number of fields $L" , fields.size());
        if(hasSuper){
            method.addStatement("super.load($N)", OUTSTATE);
        }
        Iterator<AnnotatedField> iterator = fields.iterator();
        while (iterator.hasNext()){
            AnnotatedField field = iterator.next();
            generateFieldLoadStatements(field, method);
        }
        builder.addMethod(method.build());
    }

    private void generateFieldSaveStatements(AnnotatedField field, MethodSpec.Builder method ){
        String className = StringUtils.uncapitalize(originalClassName);
        String fieldName = field.getElement().getSimpleName().toString();
        method.addStatement(field.getGenerator().generateWrite(OUTSTATE, fieldName, className + "." + fieldName));


    }

    private void generateFieldLoadStatements(AnnotatedField field, MethodSpec.Builder method ){
        String className = StringUtils.uncapitalize(originalClassName);
        String fieldName = field.getElement().getSimpleName().toString();
        method.addStatement(field.getGenerator().generateRead(OUTSTATE, fieldName, className + "." + fieldName) );
    }
}
