package funcify.tool;

import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * @author smccarron
 * @created 2021-06-06
 */
public interface CharacterOps {

    static Stream<String> firstNAlphabetLettersAsStrings(int n) {
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
        }
        if ((start - end) > 0) {
            return Stream.empty();
        } else {
            return Stream.iterate(start,
                                  (Character c) -> {
                                      return (char) (((int) c) + 1);
                                  })
                         .limit(end - start);
        }
    }

    static Optional<Character> uppercaseAlphabetLetterByIndex(final int index) {
        return CharacterOpsMapHolder.INSTANCE.uppercaseAlphabetLetterByIndex()
                                             .apply(index);
    }

    static enum CharacterOpsMapHolder {
        INSTANCE(streamRangeOfCharactersFrom('A',
                                             'Z').toArray(Character[]::new));

        private final Character[] alphabetArray;

        CharacterOpsMapHolder(final Character[] alphabetArray) {
            this.alphabetArray = alphabetArray;
        }

        public IntFunction<Optional<Character>> uppercaseAlphabetLetterByIndex() {
            return (int i) -> {
                return i >= 0 && i < alphabetArray.length ? Optional.of(alphabetArray[i]) : Optional.empty();
            };
        }
    }

}
