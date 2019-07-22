package main.java.jce.compiling.utils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * FileScript is a Data class contains information about a file for compiling.
 * At first {@link #commands} and {@link #cleanupFiles} are raw and are not formatted
 * after setting {@link Pathify} of file to class keys replaced with values and information.
 * Refer to {@link #setPathify(Pathify, List, String)}
 *
 *  List of primary keys in compileScript that need be replaced with values
 * Running Class Parent ----> RCP
 * Full path of file -------> FPF
 * Parent path of file -----> PPF
 * File name ---------------> FN
 * File extension ----------> FE
 * Parameters --------------> PA
 *
 *   Note: This program is Written with Lombok.
 *   @see <a href="https://projectlombok.org/">Lombok Site</a>
 */
@Data
@Setter(value = AccessLevel.PRIVATE)
public class FileScript {
    private String language;
    private boolean needCleanup;
    private String[] commands;
    private String[] cleanupFiles;

    /**
     * @param customPairs keys and values that used in custom CompilerScript.json
     * @param parameters inline parameters of program
     */
    protected void setPathify(Pathify pathify, List<String[]> customPairs, String parameters) throws IllegalArgumentException {
        if (commands == null) throw new IllegalArgumentException(String.format("%s: Commands[] cannot be null!",this.getClass().getSimpleName()));
        if (needCleanup && cleanupFiles == null) throw new IllegalArgumentException(String.format("%s: cleanupFiles[] cannot be null while needCleanup is set to true!",this.getClass().getSimpleName()));

        List<String[]> info = getConvertingInfo(pathify, customPairs, parameters);

        setCommands(getFormatted(getCommands(),info));
        if (isNeedCleanup()) setCleanupFiles(getFormatted(getCleanupFiles(),info));
    }

    /**
     * @param inputs can be commands or cleanupFiles that need to replace keys with values
     * @param info keys and values
     * @return formatted data
     */
    private String[] getFormatted(String[] inputs, List<String[]> info){
        return Arrays.stream(inputs).map(input -> getFormatted(input, info)).toArray(String[]::new);
    }

    /**
     * Refer to {@link #getFormatted(String[], List)} Doc
     */
    private String getFormatted(String input, List<String[]> info){
        String formatted = input;
        for (String[] pair : info) {
            String regex = String.format("\\{%s\\}",pair[0]); // pair[0] == code
            formatted = replaceKeys(formatted,pair,regex);
        }
        return formatted;
    }

    /**
     * Use {@link Matcher} and {@link Pattern} to find keys in compileScript for replacement
     * @param input a single input that need to be formatted
     * @param pair a two element 1D array consist of key and value
     * @param regex conventional regex of key in CompilerScript
     * @return formatted input
     */
    private String replaceKeys(String input, String[] pair, String regex) {
        String formatted = input;
        String format = String.format("{%s}",pair[0]); // code = pair[0];
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(formatted);
        while (matcher.find()) formatted = formatted.replace(format,pair[1]); // value = pair[1];

        return formatted;
    }

    /**
     * @return keys and values for formatting CompilerScript
     */
    private List<String[]> getConvertingInfo(Pathify pathify, List<String[]> customPairs, String parameters) {
        List<String[]> defaultPairs = new ArrayList<>();
        defaultPairs.add(new String[]{"FN",pathify.getFileName()});
        defaultPairs.add(new String[]{"FE",pathify.getExtension()});
        defaultPairs.add(new String[]{"PA", getParameters(parameters)});
        defaultPairs.add(new String[]{"FPF",pathify.getFullPath()});
        defaultPairs.add(new String[]{"PPF",pathify.getParentPath()});
        defaultPairs.add(new String[]{"RCP",new File("").getAbsolutePath().concat("\\")});

        if (customPairs != null) {
            defaultPairs.addAll(customPairs);
            defaultPairs = defaultPairs.parallelStream().distinct().collect(Collectors.toList());
        }

        return defaultPairs;
    }

    private String getParameters(String raw) {
        return raw == null ? "" : " ".concat(raw);
    }

}
