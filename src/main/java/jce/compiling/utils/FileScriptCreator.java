package main.java.jce.compiling.utils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.List;
import java.util.Objects;

/**
 * FileScriptCreator is a Tool which is use to
 * Find proper FileScript in CompilerScript base on given information.
 *
 *  Note: This program is Written with Lombok.
 *  @see <a href="https://projectlombok.org/">Lombok Site</a>
 */
@RequiredArgsConstructor
@AllArgsConstructor
public class FileScriptCreator {
     @Getter private Pathify pathify;
     @Getter private String fileLanguage;
    @Getter private String parameters;
    private String compilerScriptPath;
    @Getter private List<String[]> customPairs;


    public FileScript get() {
        FileScript fileScript = findScript();
        checkNullity(fileScript);
        fileScript.setPathify(getPathify(), customPairs, parameters);
        return fileScript;
    }

    private InputStream getCompilerScriptPath() {
        if (compilerScriptPath != null) {
            try { return new FileInputStream(compilerScriptPath); }
            catch (FileNotFoundException e) { e.printStackTrace();}
        }

        return getDefaultCompilerScriptPath();
    }

    private InputStream getDefaultCompilerScriptPath() {
        return getClass().getClassLoader().getResourceAsStream("main/resources/CompilerScript.json");
    }

    private void checkNullity(FileScript fileScript) {
        if (fileScript == null) throw new IllegalArgumentException(
                String.format("%s: Cannot find %s in %s",
                        getClass().getSimpleName(),
                        getFileLanguage(),
                        getCompilerScriptPath()));

    }

    /**
     * @return All the FileScripts in CompilerScript
     */
    private FileScript[] getFileScripts() {
        return new Gson().fromJson(new JsonReader(new InputStreamReader(getCompilerScriptPath())),FileScript[].class);
    }

    /**
     * @return proper FileScript for given information
     */
    private FileScript findScript() {
        FileScript fileScript = null;
        for (FileScript script : Objects.requireNonNull(getFileScripts())) if (script.getLanguage().equals(getFileLanguage())) {
                fileScript = script;
                break;
            }
        return fileScript;
    }
}
