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
@Table(name = "Track")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TrackId", nullable = false)
    private int trackId;

    @Column(name = "Name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "AlbumId")
    private Album album;

    @ManyToOne
    @JoinColumn(name = "MediaTypeId", nullable = false)
    private MediaType mediaType;

    @ManyToOne
    @JoinColumn(name = "GenreId")
    private Genre genre;

    @Column(name = "Composer")
    private String composer;

    @Column(name = "Milliseconds", nullable = false)
    private int milliseconds;

    @Column(name = "Bytes")
    private int bytes;

    @Column(name = "UnitPrice", nullable = false)
    private BigDecimal unitPrice;
}
