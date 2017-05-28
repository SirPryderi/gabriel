import filters.ProcessFilter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.ProcessInfo;

import java.util.List;

public class Gabriel extends Application {

    ObservableList<ProcessInfo> processes = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        stage.setTitle("Gabriel");
        stage.setWidth(450);
        stage.setHeight(600);

        ProcessesTable table = new ProcessesTable(processes);

        table.getFilters().addAll(Serializer.deserialize());

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        //vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(table);

        VBox.setVgrow(table, Priority.ALWAYS);

        Scene scene = new Scene(vbox);

        stage.setScene(scene);
        stage.show();


        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                addProcesses();
                return null;
            }
        };

        new Thread(task).start();


        table.getFilters().addListener((ListChangeListener<? super ProcessFilter>) c -> {
            Serializer.serialize(table.getFilters());
        });


    }

    private synchronized void addProcesses() {
        System.out.println("Adding processes...");

//        JProcesses jProcesses = JProcesses.get().fastMode().listProcesses();

        //jProcesses.fastMode();

        List<ProcessInfo> processList = JProcesses.getProcessList();
//        List<ProcessInfo> processList = JProcesses.get().fastMode().listProcesses();

        Platform.runLater(() -> processes.addAll(processList));

    }


} 