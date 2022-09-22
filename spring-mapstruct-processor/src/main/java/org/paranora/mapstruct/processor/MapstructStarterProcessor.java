package org.paranora.mapstruct.processor;

import com.squareup.javapoet.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.paranora.mapstruct.annotations.PMapper;
import org.paranora.mapstruct.annotations.PMapping;
import org.paranora.mapstruct.java.generator.poet.DefaultInterfaceGenerator;
import org.paranora.mapstruct.java.generator.poet.InterfaceJavapoetGenerator;
import org.paranora.mapstruct.java.metadata.converter.DefaultAnnotationMetaConverter;
import org.paranora.mapstruct.java.metadata.creator.InterfaceMetaCreator;
import org.paranora.mapstruct.java.metadata.creator.mapstruct.mapper.MapstructMapperInterfaceMetaCreatorFacader;
import org.paranora.mapstruct.java.metadata.entity.AnnotationMeta;
import org.paranora.mapstruct.java.metadata.entity.ClassMeta;
import org.paranora.mapstruct.java.metadata.entity.FieldMeta;
import org.paranora.mapstruct.java.metadata.entity.InterfaceMeta;
import org.paranora.mapstruct.java.metadata.extractor.DefaultElementClassMetaExtractor;
import org.paranora.mapstruct.java.metadata.extractor.ElementClassMetaExtractor;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@SupportedAnnotationTypes(MapstructStarterProcessor.PMapperAnnotationName)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MapstructStarterProcessor extends AbsProcessor {

    public static final String PMapperAnnotationName = "org.paranora.mapstruct.annotations.PMapper";

    protected InterfaceMetaCreator<ClassMeta, InterfaceMeta> interfaceMetaCreator = new MapstructMapperInterfaceMetaCreatorFacader();

    protected InterfaceJavapoetGenerator interfaceJavapoetGenerator = new DefaultInterfaceGenerator();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        print("entry.");

        if (!orderIsCreated) {
            try {
                generateOrderItemClass(filer);
                generateOrderClass(filer);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                orderIsCreated = true;
            }
        }

        processPresentAnnotation(annotations, roundEnvironment, PMapper.class, element -> {
            ElementClassMetaExtractor elementClassDefinitionExtractor = new DefaultElementClassMetaExtractor();
            ClassMeta clz = elementClassDefinitionExtractor.extract((TypeElement) element);

            InterfaceMeta interfaceMeta = interfaceMetaCreator.create(clz, null, null);
            TypeSpec typeSpec = interfaceJavapoetGenerator.create(interfaceMeta);

            print("InterfaceMeta -> TypeSpec : begin");

            print(typeSpec.toString());

            print("InterfaceMeta -> TypeSpec : end");

//                interfaceMeta.getAnnotations().forEach((at) -> {
//                    print("package : %s , class : %s , annotation  : %s  , class : %s"
//                            , clz.getPackageName()
//                            , clz.getName()
//                            , at.getName()
//                            , at.getTypeName());
//                    at.getFields().forEach((atfk, atf) -> {
//                        print("package : %s , class : %s , annotation key : %s , type : %s , value : %s ,value class : %s"
//                                , clz.getPackageName()
//                                , clz.getName()
//                                , atf.getName()
//                                , atf.getTypeName()
//                                , atf.getValue()
//                                , atf.getValue().getClass());
//                    });
//                    print("\r\n\r\n");
//                });
//
//                print("=============================================.");
//                interfaceMeta.getMethods()
//                        .stream()
//                        .forEach(m -> {
//                            print("package : %s , class : %s , method  : %s "
//                                    , clz.getPackageName()
//                                    , clz.getName()
//                                    , m.getName());
//                            m.getAnnotations().forEach((at) -> {
//                                print("package : %s , class : %s , method : %s , annotation  : %s ."
//                                        , clz.getPackageName()
//                                        , clz.getName()
//                                        , m.getName()
//                                        , at.getName());
//                                at.getFields().forEach((atfk, atf) -> {
//                                    print("package : %s , class : %s , method : %s  , annotation key : %s , type : %s , value : %s ,value class : %s"
//                                            , clz.getPackageName()
//                                            , clz.getName()
//                                            , m.getName()
//                                            , atf.getName()
//                                            , atf.getTypeName()
//                                            , atf.getValue()
//                                            , atf.getValue().getClass());
//                                });
//                                print("\r\n\r\n");
//                            });
//                        });

//                clz.getFields().forEach((fk, f) -> {
//                    print("package : %s , class %s , field : %s ", clz.getPackageName(), clz.getName(), f.getName());
//                    f.getAnnotations().forEach((at) -> {
//                        at.getFields().forEach((atfk, atf) -> {
//                            print("package : %s , class : %s , field : %s , annotation key : %s , type : %s , value : %s ,value class : %s"
//                                    , at.getPackageName()
//                                    , at.getName()
//                                    , f.getName()
//                                    , atf.getName()
//                                    , atf.getTypeName()
//                                    , atf.getValue()
//                                    , atf.getValue().getClass());
//                        });
//                    });
//                });
        });
        return false;
    }


    protected Boolean orderIsCreated = false;

    public void generateOrderClass(Filer filer) throws ClassNotFoundException {
        String packageName = "org.paranora.spring.test.component.entity";
        String className = "Order";

        FieldSpec itemName = FieldSpec.builder(String.class, "itemName")
                .addModifiers(Modifier.PROTECTED)
                .initializer("$S", "Paranora")
                .build();

        FieldSpec claz = FieldSpec.builder(TypeName.get(Class.class), "itemClass")
                .addModifiers(Modifier.PROTECTED)
                .initializer("$L", "org.paranora.spring.test.component.entity.OrderItem.class")
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addModifiers(javax.lang.model.element.Modifier.PUBLIC)
                .addField(itemName)
                .addField(claz)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateOrderItemClass(Filer filer) {
        String packageName = "org.paranora.spring.test.component.entity";
        String className = "OrderItem";

        FieldSpec itemName = FieldSpec.builder(String.class, "name")
                .addModifiers(Modifier.PROTECTED)
                .initializer("$S", "Paranora")
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addModifiers(javax.lang.model.element.Modifier.PUBLIC)
                .addField(itemName)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String prefix() {
        return "PM.Process entry. ";
    }

}
