package org.paranora.mapstruct.starter.processor;

import lombok.Getter;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.function.Consumer;

@Getter
public abstract class AbsProcessor extends AbstractProcessor {

    protected Filer filer;
    protected Messager messager;
    protected Elements elementUtils;
    protected Types typesUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
        typesUtils = processingEnvironment.getTypeUtils();
    }

    protected boolean isPresentAnnotation(TypeElement annotation,String presentAnnotationName) {
        return presentAnnotationName.contentEquals(annotation.getQualifiedName());
    }

    protected void process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment, Class<?> annotationClass, Consumer<Element> consumer) {
        annotations.stream().filter(annotation -> isPresentAnnotation(annotation, annotationClass.getName())).forEach(typeElement -> {
            roundEnvironment.getElementsAnnotatedWith(typeElement).stream().forEach(element -> {
                consumer.accept(element);
            });
        });
    }

    protected String prefix(){return "";}

    protected void print(String message) {
        print("%s",message);
    }

    protected void print(String format,Object... message) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format(prefix()+format,message));
    }

}
