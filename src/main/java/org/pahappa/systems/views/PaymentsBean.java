package org.pahappa.systems.views;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.pahappa.systems.models.Invoice;
import org.pahappa.systems.services.billing.BillingAndReportingService;

import java.io.Serializable;
import java.util.List;

@Named("paymentsBean")
@ViewScoped
public class PaymentsBean implements Serializable {

    @Inject
    private BillingAndReportingService billingService;

    private List<Invoice> invoices;

    @PostConstruct
    public void init() {
        this.invoices = billingService.findAllInvoices();
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void viewInvoice(Invoice invoice) {
        // Implement logic to view invoice details (e.g., open a dialog or redirect)
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "View Invoice", "Invoice ID: " + invoice.getId()));
    }

    public void deleteInvoice(Invoice invoice) {
        billingService.deleteInvoice(invoice);
        this.invoices = billingService.findAllInvoices(); // Refresh list
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Delete Invoice", "Invoice deleted successfully."));
    }
}