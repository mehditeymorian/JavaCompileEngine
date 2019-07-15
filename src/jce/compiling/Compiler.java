package jce.compiling;

import jce.processing.BasicProcessor;
import jce.processing.OnEachProcessListener;
import jce.processing.ProcessorState;
import jce.processing.TimerProcessor;
import lombok.Builder;

/**<p>
 * Compiler is a Concise Program with a builder that
 * takes multiple options and compile the given file.</p>
 *  Note: This program is Written with Lombok.
 *  @see <a href="https://projectlombok.org/">Lombok Site</a>
 */
@Builder
public class Compiler {
    private String fileAddress;
    private CompileType compileType;
    private String parameters;
    private Integer exceedTimeInMillis;
    private boolean withExceedTime;
    private OnEachProcessListener onEachProcessListener;

    /**
     * if not exceedTime set, {@link #getExceedTimeInMillis()}
     * return this as default value.
     */
    public static final int EXCEED_TIME_DEFAULT = 5000;


    /**
     * @return a CompileResult is Data Class and consist of compile operation result and information.
     */
    public CompileResult compile() throws InterruptedException {
        Pathify pathify = Pathify.of(fileAddress);
        CompileResult compileResult = getProcess( getCommands(pathify) );
        Cleanup.clean(pathify,compileType);
        return compileResult;
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
     * @return proper command to compile the file base on CompileType and fileAddress
     */
    private String[] getCommands(Pathify pathify){
        return compileType.commands.apply(pathify,parameters);
    }

    private Integer getExceedTimeInMillis() {
        return exceedTimeInMillis == null ? EXCEED_TIME_DEFAULT : exceedTimeInMillis;
    }
}
