package org.paranora.mapstruct.java.metadata.entity;

import com.squareup.javapoet.TypeName;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class BaseMeta extends Meta {

    @Builder.Default
    protected List<Modifier> accessLevels = new ArrayList<>();

    @Builder.Default
    protected List<TypeName> genericTypes = new ArrayList<>();

    @Builder.Default
    protected List<AnnotationMeta> annotations = new ArrayList<>();

    @Builder.Default
    protected List<Class<?>> annotationClazs = new ArrayList<>();

}
