package security.backend.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AlbumId", nullable = false)
    private int albumId;

    @Column(name = "Title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "ArtistId", nullable = false)
    private Artist artistId;
}
