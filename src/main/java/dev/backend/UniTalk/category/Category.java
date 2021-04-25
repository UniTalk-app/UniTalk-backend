package dev.backend.UniTalk.category;

import dev.backend.UniTalk.group.Group;
import dev.backend.UniTalk.thread.Thread;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@Getter @Setter
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;

    @Basic(optional = false)
    @Column(length = 128)
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Thread> threads = new HashSet<>();

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
                "category_id=" + category_id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return category_id.equals(category.category_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category_id);
    }
}
