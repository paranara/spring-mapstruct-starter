package org.paranora.mapstruct.java.metadata.entity;

import com.squareup.javapoet.TypeName;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MethodMeta extends BaseValueMeta {
    protected TypeName returnType;

    @Builder.Default
    protected List<ParameterMeta> parameters=new ArrayList<>();
}
