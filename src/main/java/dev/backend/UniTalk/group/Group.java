package dev.backend.UniTalk.group;

import dev.backend.UniTalk.thread.Thread;
import dev.backend.UniTalk.category.Category;
import lombok.*;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@Getter @Setter
public class Group {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long group_id;

    @Basic(optional = false)
    @Column(length = 128)
    private String group_name;

    @Basic(optional = false)
    private Long creator_id;

    @Basic(optional = false)
    private Timestamp creation_timestamp;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Thread> threads = new HashSet<>();
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Category> categories = new HashSet<>();

    public Group(String group_name, Long creator_id, Timestamp creation_timestamp) {
        this.group_name = group_name;
        this.creator_id = creator_id;
        this.creation_timestamp = creation_timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return group_id.equals(group.group_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group_id);
    }

    @Override
    public String toString() {
        return "Group{" +
                "group_id=" + group_id +
                ", group_name='" + group_name + '\'' +
                ", creator_id=" + creator_id +
                ", creation_timestamp=" + creation_timestamp +
                '}';
    }
}
