package dev.backend.unitalk.user;

import dev.backend.unitalk.role.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        }
)

@NoArgsConstructor
@Getter @Setter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    @Size(min = 1, max=128, message = "password: must be between 1 and 128 chars")
    private String password;

    @Basic(optional = false)
    @Size(min = 1, max = 32, message = "username: must be between 1 and 32 chars")
    private String username;

    @Basic(optional = false)
    @Size(min = 1, max = 32, message = "firstname: must be between 1 and 32 chars")
    private String firstName;

    @Basic(optional = false)
    @Size(min = 1, max = 32, message = "lastname: must be between 1 and 32 chars")
    private String lastName;

    @Basic(optional = false)
    @Email(message = "use correct email")
    @Size(min = 1, max = 64, message = "email: must be between 1 and 64 chars")
    private String email;

    public User(String username,
                String firstName,
                String lastName,
                String email,
                String password) {
        this.password = password;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Overrides

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var user = (User) o;
        return id.equals(user.id) || email.equals(user.email) || username.equals(user.username);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}
