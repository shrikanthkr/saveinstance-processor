package helpers;

import javax.lang.model.element.Element;

/**
 * Created by shrikanth on 11/6/16.
 */
public class ProcessingException extends Exception {
    Element element;

    public ProcessingException(Element element, String msg, Object... args) {
        super(String.format(msg, args));
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}
