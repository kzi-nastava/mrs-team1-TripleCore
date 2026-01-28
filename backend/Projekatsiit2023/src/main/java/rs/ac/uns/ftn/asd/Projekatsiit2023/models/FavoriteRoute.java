package rs.ac.uns.ftn.asd.Projekatsiit2023.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "favorite_routes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "route_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FavoriteRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "route_id", nullable = false)
    private Long routeId;

    public FavoriteRoute(Long userId, Long routeId) {
        this.userId = userId;
        this.routeId = routeId;
    }

}
