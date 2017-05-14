package generator;

import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import helpers.AnnotatedField;

/**
 * Created by shrikanth on 11/19/16.
 */

public class Generator {

    public  static SaveLoadBaseGenerator getGenerator(Elements elementUtils, Types typeUtils, AnnotatedField field){
        Class generatorClass = getGeneratorClass(elementUtils, typeUtils, field);
        try {
            Constructor constructor = generatorClass.getConstructor(AnnotatedField.class);
            return (SaveLoadBaseGenerator)constructor.newInstance(field);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class getGeneratorClass(Elements elementUtils, Types typeUtils, AnnotatedField field){
        TypeMirror elementType = field.getElement().asType();
        TypeMirror expectedType;

        expectedType = elementUtils.getTypeElement(Integer.class.getName()).asType();
        if(typeUtils.isAssignable(elementType, expectedType)){
            return IntGenerator.class;
        }

        expectedType = elementUtils.getTypeElement(String.class.getName()).asType();
        if(typeUtils.isAssignable(elementType, expectedType)){
            return StringGenerator.class;
        }

        expectedType = elementUtils.getTypeElement(Byte.class.getName()).asType();
        if(typeUtils.isAssignable(elementType, expectedType)){
            return ByteGenerator.class;
        }

        expectedType = elementUtils.getTypeElement(Boolean.class.getName()).asType();
        if(typeUtils.isAssignable(elementType, expectedType)){
            return BooleanGenerator.class;
        }

        expectedType = elementUtils.getTypeElement(Character.class.getName()).asType();
        if(typeUtils.isAssignable(elementType, expectedType)){
            return CharGenerator.class;
        }

        expectedType = elementUtils.getTypeElement(Short.class.getName()).asType();
        if(typeUtils.isAssignable(elementType, expectedType)){
            return ShortGenerator.class;
        }

        expectedType = elementUtils.getTypeElement(Float.class.getName()).asType();
        if(typeUtils.isAssignable(elementType, expectedType)){
            return FloatGenerator.class;
        }

        expectedType = elementUtils.getTypeElement(Double.class.getName()).asType();
        if(typeUtils.isAssignable(elementType, expectedType)){
            return DoubleGenerator.class;
        }

        expectedType = elementUtils.getTypeElement(CharSequence.class.getName()).asType();
        if(typeUtils.isAssignable(elementType, expectedType)){
            return CharSequenceGenerator.class;
        }

        expectedType = elementUtils.getTypeElement(Serializable.class.getName()).asType();
        if(typeUtils.isAssignable(elementType, expectedType)){
            return SerializableGenerator.class;
        }

        expectedType = elementUtils.getTypeElement(Parcelable.class.getName()).asType();
        if(typeUtils.isAssignable(elementType, expectedType)){
            return ParcelableGenerator.class;
        }
        return null;
    }

}
