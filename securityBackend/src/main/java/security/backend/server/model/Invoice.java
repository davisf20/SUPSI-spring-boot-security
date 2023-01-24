package security.backend.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InvoiceId", nullable = false)
    private int invoiceId;

    @ManyToOne
    @JoinColumn(name = "CustomerId", nullable = false)
    private Customer customer;

    @Column(name = "InvoiceDate", nullable = false)
    private LocalDateTime invoiceDate;

    @Column(name = "BillingAddress")
    private String billingAddress;

    @Column(name = "BillingCity")
    private String billingCity;

    @Column(name = "BillingState")
    private String billingState;

    @Column(name = "BillingCountry")
    private String billingCountry;

    @Column(name = "BillingPostalCode")
    private String billingPostalCode;

    @Column(name = "Total", nullable = false)
    private BigDecimal total;
}
