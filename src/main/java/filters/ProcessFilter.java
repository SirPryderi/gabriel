package filters;

import org.jutils.jprocesses.model.ProcessInfo;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created on 26/05/2017.
 *
 * @author Vittorio
 */
public interface ProcessFilter extends Serializable{
    static String commandToPath(String command) {
        try {
            if (command.charAt(0) == '"') {
                int i = command.indexOf('"', 1);
                command = command.substring(1, i);
            } else {
                command = command.split("\\s")[0];
            }
        } catch (Exception e) {
            command = "";
        }
        return command;
    }

    static boolean checkAll(ProcessInfo process, Collection<ProcessFilter> filters) {
        for (ProcessFilter filter : filters) {
            if (filter.check(process))
                return false;
        }

        return true;
    }

    boolean check(ProcessInfo process);
}
