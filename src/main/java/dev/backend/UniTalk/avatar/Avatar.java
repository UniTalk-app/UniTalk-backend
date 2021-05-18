package dev.backend.UniTalk.avatar;


import dev.backend.UniTalk.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "avatars")
@NoArgsConstructor
@Getter
@Setter
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="image")
    private byte[] image;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public Avatar(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Avatar)) return false;
        Avatar avatar = (Avatar) o;
        return getId().equals(avatar.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
