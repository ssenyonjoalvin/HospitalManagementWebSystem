package org.pahappa.systems.views;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import org.pahappa.systems.models.Invoice;
import org.pahappa.systems.services.billing.BillingAndReportingService;

@Named("customPaymentBean")
@ViewScoped
public class CustomPaymentBean implements Serializable {
    private Long invoiceId;
    private String paymentMethod = "mobilemoney";
    private String operator = "Airtel";
    private String mobileNumber;
    private String cardNumber;
    private String cardExpiry;
    private String cardCvv;
    private String message;

    @Inject
    private BillingAndReportingService billingService;
    private Invoice invoice;

    @PostConstruct
    public void init() {
        if (invoiceId != null) {
            invoice = billingService.findInvoiceById(invoiceId);
        }
    }

    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getCardExpiry() { return cardExpiry; }
    public void setCardExpiry(String cardExpiry) { this.cardExpiry = cardExpiry; }
    public String getCardCvv() { return cardCvv; }
    public void setCardCvv(String cardCvv) { this.cardCvv = cardCvv; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTotalAmountDisplay() {
        if (invoice != null && invoice.getTotalAmount() != null) {
            return String.format("%,.0f", invoice.getTotalAmount());
        }
        return "0";
    }

    public void pay() {
        // Simulate payment processing
        if ("mobilemoney".equals(paymentMethod)) {
            if (mobileNumber != null && !mobileNumber.isEmpty()) {
                message = "Mobile Money payment successful for " + operator + ": +256" + mobileNumber;
            } else {
                message = "Please enter a valid mobile number.";
            }
        } else if ("card".equals(paymentMethod)) {
            if (cardNumber != null && !cardNumber.isEmpty() && cardExpiry != null && !cardExpiry.isEmpty() && cardCvv != null && !cardCvv.isEmpty()) {
                message = "Card payment successful (****" + cardNumber.substring(cardNumber.length() - 4) + ")";
            } else {
                message = "Please enter all card details.";
            }
        }
    }
} 