package funcify.tool;

import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author smccarron
 * @created 2021-06-06
 */
public interface CharacterOps {

    static Stream<String> firstNUppercaseLettersAsStrings(int n) {
        if (n <= 0) {
            return Stream.empty();
        }
        return streamRangeOfCharactersAsStringsFrom('A', (char) (((int) 'A') + Math.min(26, n)));
    }

    static Stream<String> firstNUppercaseLettersWithNumericIndexExtension(int n) {
        if (n <= 0) {
            return Stream.empty();
        }
        final int aToZSize = 'Z' - 'A' + 1;
        return IntStream.range(0, n)
                        .mapToObj(i -> {
                            return uppercaseLetterByIndex(i % aToZSize).map(String::valueOf)
                                                                       .map(s -> Optional.of(s)
                                                                                         .filter(s1 -> i >= aToZSize)
                                                                                         .map(s1 -> new StringBuilder().append(s1)
                                                                                                                       .append(i
                                                                                                                               / aToZSize)
                                                                                                                       .toString())
                                                                                         .orElse(s))
                                                                       .orElse("");
                        });
    }

    static Stream<String> streamRangeOfCharactersAsStringsFrom(char start, char end) {
        return streamRangeOfCharactersFrom(start, end).map(String::valueOf);
    }

    static Stream<Character> streamRangeOfCharactersFrom(char start, char end) {
        if (start == end) {
            return Stream.of(start);
        } else {
            return IntStream.range(start, end)
                            .mapToObj(i -> {
                                return (char) i;
                            });
        }
    }

    static Optional<Character> uppercaseLetterByIndex(final int index) {
        return uppercaseLetterByIndexMapper().apply(index);
    }

    static String uppercaseLetterByIndexWithNumericExtension(final int index) {
        int validatedIndex;
        if (index < 0) {
            validatedIndex = -index;
        } else {
            validatedIndex = index;
        }
        final int aToZSize = 'Z' - 'A' + 1;
        return uppercaseLetterByIndex(validatedIndex % aToZSize).map(c -> Optional.of(validatedIndex)
                                                                                  .filter(i -> i >= aToZSize)
                                                                                  .map(i -> new StringBuilder().append(c)
                                                                                                               .append(i
                                                                                                                       / aToZSize)
                                                                                                               .toString())
                                                                                  .orElse(String.valueOf(c)))
                                                                .orElse(null);
    }

    static IntFunction<Optional<Character>> uppercaseLetterByIndexMapper() {
        return CharacterOpsMapHolder.INSTANCE.uppercaseLetterByIndexMapper();
    }

    enum CharacterOpsMapHolder {
        INSTANCE(streamRangeOfCharactersFrom('A', (char) ('Z' + 1)).toArray(Character[]::new));

        private final Character[] uppercaseLettersArr;

        CharacterOpsMapHolder(final Character[] uppercaseLettersArr) {
            this.uppercaseLettersArr = uppercaseLettersArr;
        }

        public IntFunction<Optional<Character>> uppercaseLetterByIndexMapper() {
            return (int i) -> {
                return i >= 0 && i < uppercaseLettersArr.length ? Optional.of(uppercaseLettersArr[i]) : Optional.empty();
            };
        }
    }

}
