package dev.lightdream.controlpanel.dto;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Log {

    public int maxSize = 50;
    public List<String> logs = new ArrayList<>();

    public Log(List<String> logs) {
        this.logs = logs;
    }

    public Log(String... logs) {
        this.logs = new ArrayList<>(List.of(logs));
    }

    public void addLog(Log log) {
        this.logs.addAll(log.logs);
        while (this.logs.size() > maxSize) {
            this.logs.remove(0);
        }
    }
}
