package funcify.spec;

import com.fasterxml.jackson.databind.JsonNode;
import funcify.st.adapter.model.JsonNodeModelAdapter;
import funcify.tool.container.SyncMap;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import org.stringtemplate.v4.ModelAdaptor;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Getter
public class DefaultStringTemplateSpec implements StringTemplateSpec {


    @Default
    private final List<String> typePackagePathSegments = Collections.emptyList();


    private final String typeName;


    private final String fileTypeExtension;


    private final Path stringTemplateGroupFilePath;


    private final Path destinationParentDirectoryPath;


    private final String templateFunctionName;

    private final SyncMap<Class<?>, ModelAdaptor<?>> modelAdapters = SyncMap.of(JsonNode.class, new JsonNodeModelAdapter());

    @Default
    private final SyncMap<String, Object> templateFunctionParameterInput = SyncMap.empty();
}
