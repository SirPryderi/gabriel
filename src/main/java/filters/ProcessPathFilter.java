package filters;

import org.jutils.jprocesses.model.ProcessInfo;

import java.util.Objects;

import static filters.ProcessFilter.commandToPath;

/**
 * Created on 26/05/2017.
 *
 * @author Vittorio
 */
public class ProcessPathFilter implements ProcessFilter {
    private final String path;

    public ProcessPathFilter(String path) {
        this.path = path;
    }

    @Override
    public boolean check(ProcessInfo process) {
        Objects.requireNonNull(process);

        String path = commandToPath(process.getCommand()).toLowerCase();

        return this.path.toLowerCase().equals(path);
    }
}
