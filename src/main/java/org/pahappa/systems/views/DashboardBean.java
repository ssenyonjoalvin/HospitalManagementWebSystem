package org.pahappa.systems.views;

// Note the updated "jakarta.*" imports
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named; // Using CDI's @Named for the bean

import org.pahappa.systems.models.Appointment;
import org.pahappa.systems.models.Invoice;
import org.pahappa.systems.models.Patient;
import org.pahappa.systems.services.appointment.AppointmentsService;
import org.pahappa.systems.services.billing.BillingAndReportingService;
import org.pahappa.systems.services.patient.PatientService;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.pie.PieChartDataSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

// Modern Jakarta EE / CDI annotations
@Named("dashboardBean") // Makes the bean available as #{dashboardBean} in XHTML
@ViewScoped // Scope is now from jakarta.faces.view
public class DashboardBean implements Serializable {

    @Inject
    private PatientService patientService;
    @Inject
    private AppointmentsService appointmentsService;
    @Inject
    private BillingAndReportingService billingService;

    private long totalPatients;
    private long totalAppointments;
    private long pendingAppointments;
    private long totalInvoices;
    private long paidInvoices;
    private long totalMedicines;
    private long totalServices;

    private List<Appointment> recentAppointments;
    private List<Invoice> recentInvoices;

    // Chart data
    private Map<String, Long> appointmentsByStatus;
    private Map<String, Long> invoicesByStatus;

    private LineChartModel lineModel;
    private DonutChartModel donutModel;

    private PieChartModel appointmentsPieModel;
    private PieChartModel invoicesPieModel;

    @PostConstruct
    public void init() {
        totalPatients = patientService.countAll();
        totalAppointments = appointmentsService.countAll();
        pendingAppointments = appointmentsService.getAllAppointments().stream()
                .filter(appt -> appt.getStatus().toString().equals("PENDING"))
                .count();
        List<Invoice> allInvoices = billingService.findAllInvoices();
        totalInvoices = allInvoices.size();
        paidInvoices = allInvoices.stream()
                .filter(inv -> inv.getStatus().toString().equals("PAID"))
                .count();
        totalMedicines = billingService.getAllMedicines().size();
        totalServices = billingService.getAllServices().size();

        recentAppointments = appointmentsService.getAllAppointments().stream()
                .sorted(Comparator.comparing(Appointment::getAppointmentDate).reversed())
                .limit(5)
                .collect(Collectors.toList());
        recentInvoices = allInvoices.stream()
                .sorted(Comparator.comparing(Invoice::getCreationDate).reversed())
                .limit(5)
                .collect(Collectors.toList());

        appointmentsByStatus = appointmentsService.getAllAppointments().stream()
                .collect(Collectors.groupingBy(
                        appt -> appt.getStatus().toString(),
                        Collectors.counting()));
        invoicesByStatus = allInvoices.stream()
                .collect(Collectors.groupingBy(
                        inv -> inv.getStatus().toString(),
                        Collectors.counting()));

        createLineModel();
        createDonutModel();
        createAppointmentsPieModel();
        createInvoicesPieModel();
    }

    private void createLineModel() {
        lineModel = new LineChartModel();
        ChartData data = new ChartData();

        // Dynamic: Appointments per month for the current year
        List<Appointment> allAppointments = appointmentsService.getAllAppointments();
        Map<Integer, Long> monthCounts = allAppointments.stream()
                .filter(appt -> appt.getAppointmentDate() != null)
                .filter(appt -> appt.getAppointmentDate().getYear() == java.time.LocalDate.now().getYear())
                .collect(Collectors.groupingBy(
                        appt -> appt.getAppointmentDate().getMonthValue(),
                        Collectors.counting()));

        List<Object> values = new ArrayList<>();
        List<String> labels = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
                "Dec");
        for (int i = 1; i <= 12; i++) {
            values.add(monthCounts.getOrDefault(i, 0L));
        }

        LineChartDataSet dataSet = new LineChartDataSet();
        dataSet.setData(values);
        dataSet.setFill(true);
        dataSet.setBorderColor("rgb(93, 135, 255)");
        dataSet.setBackgroundColor("rgba(93, 135, 255, 0.2)");
        dataSet.setTension(0.4);
        data.addChartDataSet(dataSet);
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

        // Dynamic: Gender distribution of patients
        List<Patient> allPatients = patientService.getAllPatients();
        long female = allPatients.stream()
                .filter(p -> p.getGender() != null && p.getGender().toString().equalsIgnoreCase("FEMALE")).count();
        long male = allPatients.stream()
                .filter(p -> p.getGender() != null && p.getGender().toString().equalsIgnoreCase("MALE")).count();
        long other = allPatients.size() - female - male;

        List<Number> values = Arrays.asList(female, male, other);
        List<String> bgColors = Arrays.asList("rgb(32, 201, 151)", "rgb(255, 159, 64)", "rgb(93, 135, 255)");

        DonutChartDataSet dataSet = new DonutChartDataSet();
        dataSet.setData(values);
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = Arrays.asList("Female", "Male", "Other");
        data.setLabels(labels);

        donutModel.setData(data);
    }

    private void createAppointmentsPieModel() {
        appointmentsPieModel = new PieChartModel();
        ChartData data = new ChartData();
        Map<String, Long> statusCounts = appointmentsService.getAllAppointments().stream()
                .collect(Collectors.groupingBy(
                        appt -> appt.getStatus().toString(),
                        Collectors.counting()));
        PieChartDataSet dataSet = new PieChartDataSet();
        dataSet.setData(new ArrayList<>(statusCounts.values()));
        dataSet.setBackgroundColor(
                Arrays.asList("rgb(93, 135, 255)", "rgb(255, 159, 64)", "rgb(32, 201, 151)", "rgb(255, 99, 132)"));
        data.addChartDataSet(dataSet);
        data.setLabels(new ArrayList<>(statusCounts.keySet()));
        appointmentsPieModel.setData(data);
    }

    private void createInvoicesPieModel() {
        invoicesPieModel = new PieChartModel();
        ChartData data = new ChartData();
        List<Invoice> allInvoices = billingService.findAllInvoices();
        Map<String, Long> statusCounts = allInvoices.stream()
                .collect(Collectors.groupingBy(
                        inv -> inv.getStatus().toString(),
                        Collectors.counting()));
        PieChartDataSet dataSet = new PieChartDataSet();
        dataSet.setData(new ArrayList<>(statusCounts.values()));
        dataSet.setBackgroundColor(
                Arrays.asList("rgb(93, 135, 255)", "rgb(255, 159, 64)", "rgb(32, 201, 151)", "rgb(255, 99, 132)"));
        data.addChartDataSet(dataSet);
        data.setLabels(new ArrayList<>(statusCounts.keySet()));
        invoicesPieModel.setData(data);
    }

    // Getters for all fields
    public long getTotalPatients() {
        return totalPatients;
    }

    public long getTotalAppointments() {
        return totalAppointments;
    }

    public long getPendingAppointments() {
        return pendingAppointments;
    }

    public long getTotalInvoices() {
        return totalInvoices;
    }

    public long getPaidInvoices() {
        return paidInvoices;
    }

    public long getTotalMedicines() {
        return totalMedicines;
    }

    public long getTotalServices() {
        return totalServices;
    }

    public List<Appointment> getRecentAppointments() {
        return recentAppointments;
    }

    public List<Invoice> getRecentInvoices() {
        return recentInvoices;
    }

    public Map<String, Long> getAppointmentsByStatus() {
        return appointmentsByStatus;
    }

    public Map<String, Long> getInvoicesByStatus() {
        return invoicesByStatus;
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public DonutChartModel getDonutModel() {
        return donutModel;
    }

    public PieChartModel getAppointmentsPieModel() {
        return appointmentsPieModel;
    }

    public PieChartModel getInvoicesPieModel() {
        return invoicesPieModel;
    }
}