package security.backend.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "InvoiceLine")
public class InvoiceLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InvoiceLineId", nullable = false)
    private int invoiceLineId;

    @ManyToOne
    @JoinColumn(name = "InvoiceId", nullable = false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "TrackId", nullable = false)
    private Track track;

    @Column(name = "UnitPrice", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "Quantity", nullable = false)
    private int quantity;
}
