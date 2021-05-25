package dev.backend.unitalk.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.backend.unitalk.group.Group;
import dev.backend.unitalk.thread.Thread;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@Getter @Setter
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Size(min = 1, max = 128, message = "Category name: must be between 1 and 128 chars")
    @Basic(optional = false)
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Thread> threads = new HashSet<>();

    @Basic(optional = false)
    private Timestamp creationTimestamp;

    public Category(String name,Group group,Timestamp creationTimestamp)
    {
        this.name=name;
        this.group=group;
        this.creationTimestamp=creationTimestamp;
    }

    @Override
    public String toString() {
        return "category{" +
                "category_id=" + categoryId +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        var category = (Category) o;
        return categoryId.equals(category.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId);
    }
}
