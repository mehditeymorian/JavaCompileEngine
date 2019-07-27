package main.java.jce.compiling;

import lombok.Builder;
import main.java.jce.compiling.utils.FileScript;
import main.java.jce.compiling.utils.FileScriptCreator;
import main.java.jce.compiling.utils.Pathify;
import main.java.jce.processing.BasicProcessor;
import main.java.jce.processing.OnEachProcessListener;
import main.java.jce.processing.ProcessorState;
import main.java.jce.processing.TimerProcessor;

import java.io.File;
import java.util.List;

/**<p>
 * Compiler is a Concise Program with a builder that
 * takes multiple options and compile the given file.</p>
 *  Note: This program is Written with Lombok.
 *  @see <a href="https://projectlombok.org/">Lombok Site</a>
 */
@SuppressWarnings("unused")
@Builder
public class Compiler {
    private String fileAddress;
    private String fileLanguage;
    private String compilerScriptPath;
    /**
     * pairs refer to keys and values that used in CompilerScript. if CompilerScript is written manually
     * in some situations, it may needs custom pairs
     */
    private List<String[]> customPairs;
    private String parameters;
    private Integer exceedTimeInMillis;
    private boolean withExceedTime;
    private OnEachProcessListener onEachProcessListener;

    /**
     * if no exceedTime set, {@link #getExceedTimeInMillis()}
     * return this value as default value.
     */
    public static final int EXCEED_TIME_DEFAULT = 5000;

    // languages in default CompilerScript
    public static final String C = "cppC";
    public static final String CPP = "cppC";
    public static final String JAVA = "java";
    public static final String JAVA11 = "java11";
    public static final String JAVASCRIPT = "nodeJs";
    public static final String KOTLIN = "kotlin";
    public static final String NODEJS = "nodeJs";
    public static final String PHP = "php";
    public static final String PYTHON3 = "python3";

    /**
     * @return a CompileResult is Data Class and consist of compile operation result and information.
     */
    public CompileResult compile() throws InterruptedException {
        checkRequiredOptions();
        FileScript fileScript = getFileScript(Pathify.of(fileAddress));
        CompileResult compileResult = getProcess( fileScript.getCommands() );
        cleanup(fileScript.getCleanupFiles());
        return compileResult;
    }

    /**
     * check the required options
     */
    private void checkRequiredOptions(){
        if (fileAddress == null) throw new IllegalArgumentException("Compiler: FileAddress cannot be Null!");
        if (fileLanguage == null) throw new IllegalArgumentException("Compiler: CompileType cannot be Null!");
    }

    /**
     * Use Proper Processor Based on {@link #withExceedTime}
     * @return Compile result and information
     */
    private CompileResult getProcess(String[] commands) throws InterruptedException {
        CompileResult compileResult = withExceedTime ? timerProcess(commands) : basicProcess(commands);

        compileResult.setWithExceedTime(withExceedTime);
        compileResult.setCommands(commands);

        return compileResult;
    }

    private CompileResult basicProcess(String[] commands) throws InterruptedException {
        BasicProcessor basicProcessor = new BasicProcessor(commands);
        basicProcessor.setOnEachProcessListener(onEachProcessListener);
        basicProcessor.start();
        basicProcessor.join();

        CompileResult compileResult = getFromProcessor(basicProcessor);
        compileResult.setCompileState(ProcessorState.FINISHED);
        return compileResult;
    }

    private CompileResult timerProcess(String[] commands) throws InterruptedException {
        TimerProcessor timerProcessor = new TimerProcessor(commands,getExceedTimeInMillis());
        timerProcessor.getProcessor().setOnEachProcessListener(onEachProcessListener);
        timerProcessor.start();

        CompileResult compileResult = getFromProcessor(timerProcessor.getProcessor());
        compileResult.setCompileState(timerProcessor.getProcessorState());
        return compileResult;
    }

    /**
     * @return return extracted information from Processors
     */
    private CompileResult getFromProcessor(BasicProcessor processor){
        return CompileResult.builder()
                .compileResultList(processor.getProcessList())
                .duration(processor.getDuration())
                .build();
    }

    /**
     * @param paths paths of files that need to be deleted after compiling
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void cleanup(String[] paths){
        if (paths == null) return;

        for (String path : paths) {
            File file = new File(path);
            if (file.exists()) file.delete();
        }
    }

    /**
     * @return a FileScript that contains information about commands and cleanup
     */
    private FileScript getFileScript(Pathify pathify) {
        return new FileScriptCreator(pathify, fileLanguage, parameters, compilerScriptPath, customPairs).get();
    }

    private Integer getExceedTimeInMillis() {
        return exceedTimeInMillis == null ? EXCEED_TIME_DEFAULT : exceedTimeInMillis;
    }

}
