package cn.elytra.mod.gtmqol.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class QualityStringUtils {

    private static <T> Stream<T> iterableToStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /// Non-RegEx implementation
    private static String replaceFirst(String str, String pattern, String replacement) {
        int i = str.indexOf(pattern);
        if (i < 0) return str;
        return str.substring(0, i) + replacement + str.substring(i + pattern.length());
    }

    /// Replace the pattern in the given str.
    ///
    /// If there's more than one pattern in the given str, each pattern will be filled with every replacement.
    /// For example, given str `Hello {name} and {name}` with replacements of `[Alice, Bob]`,
    /// the result should be:
    ///
    /// - `Hello Alice and Alice`
    /// - `Hello Alice and Bob`
    /// - `Hello Bob and Alice`
    /// - `Hello Bob and Bob`
    ///
    /// This method is originally designed for generating a sequence of items, where there's something iterable, like voltages `{volt}_to_{volt}_transformer`.
    public static Iterable<String> replace(String str, String pattern, Iterable<String> replacements) {
        if (!str.contains(pattern)) return List.of(str);

        return iterableToStream(replacements)
            .map(replace -> replaceFirst(str, pattern, replace))
            .flatMap(replaced -> iterableToStream(replace(replaced, pattern, replacements)))
            ::iterator;
    }

    /// Expand the comma-separated values in brackets.
    ///
    /// For example, given `Hello {Alice,Bob} from {Charlie,David}`, will result in:
    ///
    /// - *Hello Alice from Charlie*
    /// - *Hello Alice from David*
    /// - *Hello Bob from Charlie*
    /// - *Hello Bob from David*`
    ///
    /// This implementation doesn't support nested brackets.
    public static Collection<String> expand(String str) {
        // bake a list of each part of the given str.
        // for example, given `{foo,bar}baz{1,2,3}`,
        // the result will be [[foo, bar], [baz], [1, 2, 3]],
        // so we can merge them later while not generating the results in one go.
        List<List<String>> baked = new ArrayList<>();
        int i = 0;
        while (i < str.length()) {
            if (str.charAt(i) == '{') {
                int end = str.indexOf('}', i);
                baked.add(List.of(str.substring(i + 1, end).split(",")));
                i = end + 1;
            } else {
                int nextB = str.indexOf('{', i);
                String seg = nextB < 0 ? str.substring(i) : str.substring(i, nextB);
                baked.add(List.of(seg));
                i += seg.length();
            }
        }

        return baked.stream()
            .reduce((l1, l2) -> {
                List<String> combined = new ArrayList<>();
                for (String s1 : l1) {
                    for (String s2 : l2) {
                        combined.add(s1 + s2);
                    }
                }
                return combined;
            }).orElse(Collections.emptyList());
    }

    public static Stream<String> expandToStream(String str) {
        return expand(str).stream();
    }
}
