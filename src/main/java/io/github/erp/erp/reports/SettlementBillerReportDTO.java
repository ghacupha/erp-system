package io.github.erp.erp.reports;

import io.github.erp.domain.enumeration.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@XmlRootElement
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SettlementBillerReportDTO implements Serializable {

    private long id;
    private String biller;
    private String description;
    private String requisitionNumber;
    private String iso4217CurrencyCode;
    private BigDecimal paymentAmount;
    private PaymentStatus paymentStatus;
    private String currentOwner;
    private String nativeOwner;
    private String nativeDepartment;
    private ZonedDateTime timeOfRequisition;
}
