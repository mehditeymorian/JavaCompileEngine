package main.java.jce.processing;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * An enumSet that show state of Process in each step.
 * Note: This program is Written with Lombok. @see <a href="https://projectlombok.org/">Lombok Site</a>
 */
@RequiredArgsConstructor
public enum ProcessorState {
    NOT_FINISHED(-1),
    TIME_EXCEEDED(0),
    TASK_FINISHED_EARLY(1),
    FINISHED(2);

    @NonNull
    @Getter
    private int state;

     public boolean taskNotFinishedEarly(){
        return this.getState() != TASK_FINISHED_EARLY.getState();
    }

     public boolean timeNotExceeded(){
        return this.getState() != TIME_EXCEEDED.getState();
    }

}
