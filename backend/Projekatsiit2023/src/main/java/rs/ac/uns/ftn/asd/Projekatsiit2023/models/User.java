package rs.ac.uns.ftn.asd.Projekatsiit2023.models;

import jakarta.persistence.*;
import rs.ac.uns.ftn.asd.Projekatsiit2023.enums.UserRole;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, unique = true)
    protected String email;

    @Column(nullable = false)
    protected String password;

    @Column(nullable = false)
    protected String firstName;

    @Column(nullable = false)
    protected String lastName;

    @Column(nullable = false)
    protected String address;

    @Column(nullable = false)
    protected String phone;

    @Enumerated(EnumType.STRING)
    protected UserRole role;

    @Column(nullable = false)
    protected boolean accountActivated;

    @Column(nullable = false)
    protected boolean accountBlocked;

    @Column(nullable = false)
    protected LocalDateTime createdAt;

    protected String profileImage;
}