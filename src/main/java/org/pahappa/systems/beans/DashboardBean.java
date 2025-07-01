package org.pahappa.systems.beans;

// Note the updated "jakarta.*" imports
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named; // Using CDI's @Named for the bean

import org.pahappa.systems.models.Patient;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Modern Jakarta EE / CDI annotations
@Named("dashboardBean") // Makes the bean available as #{dashboardBean} in XHTML
@ViewScoped           // Scope is now from jakarta.faces.view
public class DashboardBean implements Serializable {

    // Properties for Stat Cards and Knobs
    private int todaysAppointments = 50;
    private int completed = 10;
    private int canceled = 2;
    private int confirmed = 38;

    // Chart Models
    private LineChartModel lineModel;
    private DonutChartModel donutModel;

    // Data Table list
    private List<Patient> admittedPatients;

    @PostConstruct
    public void init() {
        createLineModel();
        createDonutModel();
        loadPatientData();
    }

    // The rest of the bean's logic is IDENTICAL to the previous example.
    // No changes are needed in the methods below.

    private void createLineModel() {
        lineModel = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = Arrays.asList(65, 140, 80, 45, 56, 40, 100, 145, 90, 105, 150, 60);
        dataSet.setData(values);
        dataSet.setFill(true);
        dataSet.setBorderColor("rgb(93, 135, 255)");
        dataSet.setBackgroundColor("rgba(93, 135, 255, 0.2)");
        dataSet.setTension(0.4);
        data.addChartDataSet(dataSet);

        List<String> labels = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        data.setLabels(labels);

        LineChartOptions options = new LineChartOptions();
        options.setMaintainAspectRatio(false);
        Legend legend = new Legend();
        legend.setDisplay(false);
        options.setLegend(legend);

        lineModel.setOptions(options);
        lineModel.setData(data);
    }

    private void createDonutModel() {
        donutModel = new DonutChartModel();
        ChartData data = new ChartData();

        List<Number> values = new ArrayList<>(Arrays.asList(30, 45, 25));
        List<String> bgColors = new ArrayList<>(Arrays.asList("rgb(32, 201, 151)", "rgb(255, 159, 64)", "rgb(93, 135, 255)"));

        org.primefaces.model.charts.donut.DonutChartDataSet dataSet = new org.primefaces.model.charts.donut.DonutChartDataSet();
        dataSet.setData(values);
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = Arrays.asList("Female", "Male", "Child");
        data.setLabels(labels);

        donutModel.setData(data);
    }

    private void loadPatientData() {
        admittedPatients = new ArrayList<>();
        admittedPatients.add(new Patient(1, "Jana Brincker", "Dr. Kenny Josh", "27/05/2016", "INFLUENZA", "bg-success"));
        admittedPatients.add(new Patient(2, "Mark Hay", "Dr. Mark", "26/05/2017", "CHOLERA", "bg-warning text-dark"));
        admittedPatients.add(new Patient(3, "Anthony Davia", "Dr. Cinnabar", "21/05/2016", "AMOEBASIS", "bg-info text-dark"));
    }

    // --- GETTERS ---
    public int getTodaysAppointments() { return todaysAppointments; }
    public int getCompleted() { return completed; }
    public int getCanceled() { return canceled; }
    public int getConfirmed() { return confirmed; }
    public LineChartModel getLineModel() { return lineModel; }
    public DonutChartModel getDonutModel() { return donutModel; }
    public List<Patient> getAdmittedPatients() { return admittedPatients; }
}