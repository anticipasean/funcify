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

    static Stream<String> firstNAlphabetLettersAsStrings(int n) {
        if (n <= 0) {
            return Stream.empty();
        }
        return streamRangeOfCharactersAsStringsFrom('A',
                                                    (char) (((int) 'A') + Math.min(26,
                                                                                   n)));
    }

    static Stream<String> streamRangeOfCharactersAsStringsFrom(char start,
                                                               char end) {
        return streamRangeOfCharactersFrom(start,
                                           end).map(String::valueOf);
    }

    static Stream<Character> streamRangeOfCharactersFrom(char start,
                                                         char end) {
        if (start == end) {
            return Stream.of(start);
        } else {
            return IntStream.range(start,
                                   end)
                            .mapToObj(i -> {
                                return (char) i;
                            });
        }
    }

    static Optional<Character> uppercaseLetterByIndex(final int index) {
        return uppercaseLetterByIndexMapper().apply(index);
    }

    static IntFunction<Optional<Character>> uppercaseLetterByIndexMapper() {
        return CharacterOpsMapHolder.INSTANCE.uppercaseLetterByIndexMapper();
    }

    static enum CharacterOpsMapHolder {
        INSTANCE(streamRangeOfCharactersFrom('A',
                                             'Z').toArray(Character[]::new));

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
