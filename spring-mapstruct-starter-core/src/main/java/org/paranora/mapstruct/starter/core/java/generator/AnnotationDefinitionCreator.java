package org.paranora.mapstruct.starter.core.java.generator;

import org.paranora.mapstruct.starter.core.java.generator.entity.AnnotationDefinition;

import java.lang.annotation.Annotation;

public interface AnnotationDefinitionCreator<T extends Annotation> {
    AnnotationDefinition create(T arg);
}