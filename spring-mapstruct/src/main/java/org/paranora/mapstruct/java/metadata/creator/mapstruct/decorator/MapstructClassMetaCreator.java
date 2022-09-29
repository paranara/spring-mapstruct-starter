package org.paranora.mapstruct.java.metadata.creator.mapstruct.decorator;

import com.squareup.javapoet.TypeName;
import org.mapstruct.DecoratedWith;
import org.paranora.mapstruct.java.metadata.creator.mapstruct.AbsMapstructClassMetaCreator;
import org.paranora.mapstruct.java.metadata.entity.ClassMeta;
import org.paranora.mapstruct.java.metadata.entity.InterfaceMeta;

import javax.lang.model.element.Modifier;
import java.util.Arrays;

public class MapstructClassMetaCreator extends AbsMapstructClassMetaCreator {

    @Override
    public ClassMeta create(InterfaceMeta source, ClassMeta parent, Class<?> clasz) {
        ClassMeta meta = new ClassMeta();
        meta.setName(createClassName(source, parent, clasz));
        meta.setPackageName(source.getPackageName());
        meta.setAccessLevels(Arrays.asList(Modifier.PUBLIC, Modifier.ABSTRACT));
        meta.setSuperInterfaces(Arrays.asList(source));
        return meta;
    }

    protected String createClassName(InterfaceMeta source, ClassMeta parent, Class<?> clasz) {
        return source.getAnnotations().stream().filter(a->a.getTypeName().equals(TypeName.get(DecoratedWith.class))).findFirst().get().getName();
    }
}