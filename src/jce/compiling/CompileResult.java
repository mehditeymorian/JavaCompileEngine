package jce.compiling;

import jce.processing.ProcessorState;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter(AccessLevel.PACKAGE)
@Builder
public class CompileResult {
    private String[] commands;
    private Map<Integer, String> compileResultList;
    private Map<Integer, String> log;
    private ProcessorState compileState;
    private long duration;
    private boolean withExceedTime;
}
