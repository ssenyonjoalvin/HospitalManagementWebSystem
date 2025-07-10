package org.pahappa.systems.views;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.pahappa.systems.models.Invoice;
import org.pahappa.systems.services.billing.BillingAndReportingService;
import java.io.Serializable;

@Named("invoiceDetailsBean")
@ViewScoped
public class InvoiceDetailsBean implements Serializable {
    private Long invoiceId;
    private Invoice invoice;

    @Inject
    private BillingAndReportingService billingService;

    public void loadInvoice() {
        if (invoiceId != null) {
            invoice = billingService.findInvoiceById(invoiceId);
        }
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Invoice getInvoice() {
        return invoice;
    }
} 