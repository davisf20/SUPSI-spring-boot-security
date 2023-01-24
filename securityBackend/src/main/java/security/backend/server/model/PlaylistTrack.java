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
@Table(name = "PlaylistTrack")
public class PlaylistTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PlaylistTrackId", nullable = false)
    private int playlistTrackId;

    @ManyToOne
    @JoinColumn(name = "PlaylistId", nullable = false)
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "TrackId")
    private Track track;
}
