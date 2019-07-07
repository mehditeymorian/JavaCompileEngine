package jce.processing;


import lombok.Builder;
import lombok.Getter;

@Builder
class ProcessInfo {
    @Getter private Process process;
    @Getter private String log;
    @Getter private String[] commands;
}
