package controller;

import ProdCons.Consumer;
import ProdCons.Producer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable {

    public Button OneSample;
    public Button Start;
    public Button Stop;
    public TextArea textArea;
    public LineChart<String,Number> lineChartWeight;
    public CategoryAxis xAxisLineChartWeight;
    public NumberAxis yAxisLineChartWeight;

    final int WINDOW_SIZE = 10;

    BlockingQueue q = new LinkedBlockingQueue();
    Producer p = new Producer(q);
    Consumer c1 = new Consumer(q);


    private ScheduledExecutorService scheduledExecutorService;
    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }




    public void OneSampleButtonClicked(ActionEvent actionEvent) {
        System.out.println("Initialize chart");
        textArea.appendText(OneSample.getText()+ System.lineSeparator());
        //FXML defining the axes and creating the line chart with two axis created above

    }

    public void StartButtonClicked(ActionEvent actionEvent) {
        System.out.println("Start");

        textArea.appendText(Start.getText()+ System.lineSeparator());
        addDataToChart();
    }

    public void StopButtonClicked(ActionEvent actionEvent) {
        System.out.println("Stop");

        textArea.appendText(Stop.getText()+ System.lineSeparator());


        //?????????????????????????? ??????????????????????
        scheduledExecutorService.shutdown();
    }
    //???????????????????????? ?????????????????? ?????????????????????? ?? ?????????????? ??????????????

    public void addDataToChart(){
        //defining a series to display data
        XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();
        series.setName("weight(t)");

        // this is used to display time in HH:mm:ss format
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        // setup a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        // put dummy data onto graph per second
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // get a random integer between 0-10
            try {
                Long weight = c1.getOutputQueue().take();


            // Update the chart
            Platform.runLater(() -> {
                // get current time
                Date now = new Date();
                // put random number with current time
                series.getData().add(
                        new XYChart.Data<>(simpleDateFormat.format(now), weight));

                if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
            });
            } catch (Throwable e) {
                e.printStackTrace();
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,"Caught exception in ScheduledExecutorService.",e);
            }}, 0, 1000, TimeUnit.MILLISECONDS);
        lineChartWeight.getData().add(series);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("?????????????????? ???????????? producer/consumer ");
        new Thread(p).start();
        new Thread(c1).start();

    }
}
