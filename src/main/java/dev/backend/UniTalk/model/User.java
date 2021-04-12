package dev.backend.UniTalk.model;

import lombok.*;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter @Setter
public class User {
    @Id
    @Column(length = 64)
    private String email;

    @Basic(optional = false)
    @Column(length = 64)
    private String passwd_hash;

    @Basic(optional = false)
    @Column(length = 64)
    private String first_name;

    @Basic(optional = false)
    @Column(length = 64)
    private String last_name;

    @Basic(optional = false)
    private Boolean is_admin;

    @Basic(optional = false)
    private Boolean is_moderator;

    @Basic(optional = false)
    private Integer group_id;

    @Basic(optional = false)
    private Timestamp creation_timestamp;

    @Basic(optional = false)
    private Boolean email_confirmed;

    @Override
    public String toString() {
        return "user{" +
                "first_name=" + first_name +
                " last_name=" + last_name +
                ", email='" + email + '\'' +
                '}';
    }
}
