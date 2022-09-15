package org.paranora.mapstruct.java.metadata.converter;

import com.squareup.javapoet.TypeName;
import lombok.Synchronized;
import org.paranora.mapstruct.java.metadata.entity.AnnotationFieldMeta;
import org.paranora.mapstruct.java.metadata.entity.AnnotationMeta;
import org.paranora.mapstruct.java.metadata.entity.ClassMeta;

import java.lang.reflect.Method;
import java.util.*;

public abstract class AbsAnnotationMetaConverter extends AbsMetaConverter<ClassMeta, AnnotationMeta> implements AnnotationMetaConverter<ClassMeta> {

    protected Map<String, List<Method>> methodMap = new HashMap<>();

    protected AnnotationMeta convert(AnnotationMeta source, Class targetClass) {
        AnnotationMeta meta = AnnotationMeta.builder().build();
        meta.setPackageName(targetClass.getPackage().getName());
        meta.setName(targetClass.getSimpleName());
        getClassMethods(targetClass)
                .stream()
                .forEach(m -> {
                    Optional<AnnotationFieldMeta> opt = source.getFields()
                            .stream()
                            .filter(a -> {
                                boolean check = a.getName().equalsIgnoreCase(m.getName());
                                if (check) {
                                    if (!a.getTypeName().equals(TypeName.get(m.getReturnType()))) {
                                        check = false;
                                    }
                                }
                                return check;
                            })
                            .findFirst();

                    if (opt.isPresent()) {
                        meta.getFields().add(AnnotationFieldMeta.builder()
                                .name(opt.get().getName())
                                .typeName(opt.get().getTypeName())
                                .value(opt.get().getValue())
                                .annotations(new ArrayList<>(opt.get().getAnnotations()))
                                .build());
                    }
                });
        if (meta.getFields().size() < 1) {
            return null;
        }
        return meta;
    }

    @Synchronized
    protected List<Method> getClassMethods(Class targetClass) {
        String name = targetClass.getName();
        if (!methodMap.containsKey(name)) {
            methodMap.put(name, Arrays.asList(targetClass.getDeclaredMethods()));
        }
        return methodMap.get(name);
    }


}