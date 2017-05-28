package filters;

import org.jutils.jprocesses.model.ProcessInfo;

import java.util.Objects;

import static filters.ProcessFilter.commandToPath;

/**
 * Created on 26/05/2017.
 *
 * @author Vittorio
 */
public class ProcessCommandFilter implements ProcessFilter {
    private final String command;

    public ProcessCommandFilter(String command) {
        this.command = command;
    }

    @Override
    public boolean check(ProcessInfo process) {
        Objects.requireNonNull(process);

        return command.toLowerCase().equals(process.getCommand().toLowerCase());
    }
}
