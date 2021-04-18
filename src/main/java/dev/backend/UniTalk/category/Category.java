package dev.backend.UniTalk.category;

import dev.backend.UniTalk.group.Group;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@Getter @Setter
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    @Column(length = 128)
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Basic(optional = false)
    private Timestamp creation_timestamp;

    public Category(String name,Group group,Timestamp creation_timestamp)
    {
        this.name=name;
        this.group=group;
        this.creation_timestamp=creation_timestamp;
    }

    @Override
    public String toString() {
        return "category{" +
                "category_id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
