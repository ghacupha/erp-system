package io.github.erp.erp.reports;

import java.time.LocalDateTime;

public interface SettlementBillerReportView {
    long id();
    String description();
    LocalDateTime timeOfRequisition();
    String requisitionNumber();
    String iso4217CurrencyCode();
    double paymentAmount();
    String paymentStatus();
    String biller();
    String currentOwner();
    String nativeOwner();
    String nativeDepartment();
}
