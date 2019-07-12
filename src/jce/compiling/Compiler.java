package jce.compiling;

import jce.processing.BasicProcessor;
import jce.processing.OnEachProcessListener;
import jce.processing.ProcessorState;
import jce.processing.TimerProcessor;
import lombok.Builder;

@Builder
public class Compiler {
    private String fileAddress;
    private CompileType compileType;
    private String parameters;
    private Integer exceedTimeInMillis; // default value of time Exceed
    private boolean withExceedTime;
    private OnEachProcessListener onEachProcessListener;

    public static final int EXCEED_TIME_DEFAULT = 5000;


    public CompileResult compile() throws InterruptedException {
        String[] commands = compileType.commands.apply(Pathify.create(fileAddress),parameters);

        CompileResult compileResult = withExceedTime ? timerProcess(commands) : normalProcess(commands);

        compileResult.setWithExceedTime(withExceedTime);
        compileResult.setCommands(commands);
        return compileResult;
    }

    private CompileResult normalProcess(String[] commands) throws InterruptedException {
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

    private CompileResult getFromProcessor(BasicProcessor processor){
        return CompileResult.builder()
                .compileResultList(processor.getProcessList())
                .log(processor.getLog())
                .duration(processor.getDuration())
                .build();
    }

    private Integer getExceedTimeInMillis() {
        return exceedTimeInMillis == null ? EXCEED_TIME_DEFAULT : exceedTimeInMillis;
    }
}
