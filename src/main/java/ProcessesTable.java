import filters.ProcessCommandFilter;
import filters.ProcessFilter;
import filters.ProcessPathFilter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.JProcessesResponse;
import org.jutils.jprocesses.model.ProcessInfo;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;

import static filters.ProcessFilter.commandToPath;

/**
 * Created on 20/05/2017.
 *
 * @author Vittorio
 */
public class ProcessesTable extends TableView<ProcessInfo> {

    private final ObservableList<ProcessFilter> filters;
    private final ObservableList<ProcessInfo> processes;
    private final FilteredList<ProcessInfo> filteredProcesses;

    public ProcessesTable(ObservableList<ProcessInfo> processes) {
        setEditable(true);

        TableColumn<ProcessInfo, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        TableColumn<ProcessInfo, String> userColumn = new TableColumn<>("User");
        userColumn.setMinWidth(50);
        userColumn.setCellValueFactory(
                new PropertyValueFactory<>("user"));

        TableColumn<ProcessInfo, String> commandColumn = new TableColumn<>("Command");
        commandColumn.setMinWidth(100);
        commandColumn.setCellValueFactory(
                new PropertyValueFactory<>("command"));


        TableColumn<ProcessInfo, String> pathColumn = new TableColumn<>("Path");
        pathColumn.setMinWidth(100);
        pathColumn.setCellValueFactory(param -> {
            String command = param.getValue().getCommand();

            command = commandToPath(command);

            return new ReadOnlyStringWrapper(command);
        });


        nameColumn.prefWidthProperty().bind(widthProperty().multiply(0.35));
        userColumn.prefWidthProperty().bind(widthProperty().multiply(0.15));
        pathColumn.prefWidthProperty().bind(widthProperty().multiply(0.5));
        commandColumn.prefWidthProperty().bind(widthProperty());

        this.processes = processes;

        this.filters = FXCollections.observableArrayList();

        filters.addListener((ListChangeListener<? super ProcessFilter>) c -> handleFilterChange());

        filteredProcesses = this.processes.filtered(this::getPredicate);

        setRowFactory(
                tableView -> {
                    final TableRow<ProcessInfo> row = new TableRow<>();
                    final ContextMenu rowMenu = new ContextMenu();

                    MenuItem whiteListItemPath = new MenuItem("Whitelist path");
                    whiteListItemPath.setOnAction(event -> {
                        String path = (ProcessFilter.commandToPath(row.getItem().getCommand()));
                        filters.add(new ProcessPathFilter(path));
                    });

                    MenuItem whiteListItemCommand = new MenuItem("Whitelist command");
                    whiteListItemCommand.setOnAction(event -> filters.add(new ProcessCommandFilter(row.getItem().getCommand())));

                    MenuItem removeItem = new MenuItem("Delete");
                    removeItem.setOnAction(event -> processes.remove(row.getItem()));

                    MenuItem copyListItemPath = new MenuItem("Copy path");
                    copyListItemPath.setOnAction(event -> copyToClipboard(ProcessFilter.commandToPath(row.getItem().getCommand())));

                    MenuItem copyListItemCommand = new MenuItem("Copy Command");
                    copyListItemCommand.setOnAction(event -> copyToClipboard(row.getItem().getCommand()));

                    MenuItem killListItem = new MenuItem("Kill");
                    killListItem.setOnAction(event -> {
                        JProcessesResponse jProcessesResponse = JProcesses.killProcess(Integer.parseInt(row.getItem().getPid()));
                        if (jProcessesResponse.isSuccess()) {
                            processes.remove(row.getItem());
                        } else {
                            System.err.println(jProcessesResponse.getMessage());
                        }
                    });

                    MenuItem openListItemPath = new MenuItem("Open file location...");
                    openListItemPath.setOnAction(event -> {
                        File file = new File (ProcessFilter.commandToPath(row.getItem().getCommand()));
                        Desktop desktop = Desktop.getDesktop();
                        System.out.println(file);
                        try {
                            desktop.open(file.getParentFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    rowMenu.getItems().addAll(
                            whiteListItemPath, whiteListItemCommand,
                            new SeparatorMenuItem(), copyListItemPath, copyListItemCommand,
                            new SeparatorMenuItem(), openListItemPath,
                            new SeparatorMenuItem(), killListItem);

                    row.setOnKeyTyped(event -> {
                        System.out.println(event.getCharacter());
                    });

                    // only display context menu for non-null items:
                    row.contextMenuProperty().bind(
                            Bindings.when(Bindings.isNotNull(row.itemProperty()))
                                    .then(rowMenu)
                                    .otherwise((ContextMenu) null));
                    return row;
                });


        updateItems();

        //noinspection unchecked
        getColumns().addAll(nameColumn, userColumn, pathColumn, commandColumn);
    }

    private static void copyToClipboard(String string) {
        if (string.isEmpty())
            return;

        StringSelection selection = new StringSelection(string);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    public ObservableList<ProcessFilter> getFilters() {
        return filters;
    }

    private void handleFilterChange() {
        //filteredProcesses.setPredicate(this::getPredicate);
        //updateItems();
        setItems(processes.filtered(this::getPredicate));
    }

    private boolean getPredicate(ProcessInfo processInfo) {
        return ProcessFilter.checkAll(processInfo, filters);
    }

    private void updateItems() {
        setItems(filteredProcesses);
    }
}
