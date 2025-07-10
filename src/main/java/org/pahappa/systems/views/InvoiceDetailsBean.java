package org.pahappa.systems.views;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.pahappa.systems.models.Invoice;
import org.pahappa.systems.services.billing.BillingAndReportingService;
import java.io.Serializable;

@Named("invoiceDetailsBean")
@SessionScoped
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

    public String goToCustomPayment() {
        System.out.println("[DEBUG] goToCustomPayment called. invoiceId=" + invoiceId);
        return "/custom-payment.xhtml?faces-redirect=true&invoiceId=" + invoiceId;
    }
} 