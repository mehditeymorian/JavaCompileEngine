package main.java.jce.compiling;

import main.java.jce.processing.ProcessorState;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/** A Data Class for returning result of {@link Compiler} Compiling.
 *  Note: This program is Written with Lombok.
 *  @see <a href="https://projectlombok.org/">Lombok Site</a>
 */
@SuppressWarnings("WeakerAccess")
@Getter
@Setter(AccessLevel.PACKAGE)
@Builder
public class CompileResult {
    private String[] commands;
    private Map<Integer, String> compileResultList;
    private ProcessorState compileState;
    private long duration;
    private boolean withExceedTime;
}
