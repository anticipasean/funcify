package funcify.template;

import funcify.typedef.JavaImport;
import funcify.typedef.JavaPackage;
import funcify.typedef.javatype.SimpleJavaType;

/**
 * @author smccarron
 * @created 2021-05-22
 */
public interface DefinitionTemplate<D> {

    JavaImport javaImport(JavaPackage javaPackage,
                          SimpleJavaType javaType);

    default D javaPackage(String packageName,
                          D definition) {
        return definition;
    }

    D name(String name);

    //    T javaType(String name, SimpleJavaTypeVariable... typeVariables);


}
