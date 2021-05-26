package funcify.commandline;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.AllArgsConstructor;
import picocli.CommandLine.ITypeConverter;

/**
 * @author smccarron
 * @created 2021-05-20
 */
@AllArgsConstructor(staticName = "of")
public class PathConverter implements ITypeConverter<Path> {

    @Override
    public Path convert(final String value) throws Exception {
        return Optional.ofNullable(value)
                       .flatMap(s -> {
                           try {
                               return Optional.<Path>of(Paths.get(s));
                           } catch (final Throwable t) {
                               return Optional.<Path>empty();
                           }
                       })
                       .orElseGet(() -> Paths.get(System.getProperty("user.dir")));
    }
}
