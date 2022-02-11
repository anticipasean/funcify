package funcify.tool;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-07-25
 */
public interface StringOps {

    static Function<String, String> firstLetterCapitalizer() {
        return str -> Optional.ofNullable(str)
                              .filter(s -> !s.isEmpty() && Character.isLowerCase(s.charAt(0)))
                              .map(s -> {
                                  if (s.length() == 1) {
                                      return String.valueOf(Character.toUpperCase(s.charAt(0)));
                                  } else {
                                      return Character.toUpperCase(s.charAt(0)) + s.substring(1);
                                  }
                              })
                              .orElse(str);
    }

}
