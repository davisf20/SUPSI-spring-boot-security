package security.backend.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Artist")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ArtistId", nullable = false)
    private int artistId;

    @Column(name = "Name")
    private String name;

    /*@OneToMany(mappedBy = "artistId")
    private List<Album> albums;*/
}
