package rs.ac.uns.ftn.asd.Projekatsiit2023.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_blocks")
@Getter
@Setter
@NoArgsConstructor
public class UserBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private Long userId;


    @Column(columnDefinition = "TEXT")
    private String note;
}
