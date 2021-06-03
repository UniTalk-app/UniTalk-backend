package dev.backend.unitalk.group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.backend.unitalk.category.Category;
import dev.backend.unitalk.thread.Thread;
import dev.backend.unitalk.user.User;
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
@Table(name = "groups")
@NoArgsConstructor
@Getter @Setter
public class Group {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Size(min = 1, max = 128, message = "Group name: must be between 1 and 128 chars")
    @Basic(optional = false)
    private String groupName;

    @Basic(optional = false)
    private Long creatorId;

    @Basic(optional = false)
    private Timestamp creationTimestamp;

    @JsonIgnore
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Thread> threads = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Category> categories = new HashSet<>();

    public Group(String groupName, Long creatorId, Timestamp creationTimestamp) {
        this.groupName = groupName;
        this.creatorId = creatorId;
        this.creationTimestamp = creationTimestamp;
    }

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        var group = (Group) o;
        return groupId.equals(group.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId);
    }

    @Override
    public String toString() {
        return "Group{" +
                "group_id=" + groupId +
                ", group_name='" + groupName + '\'' +
                ", creator_id=" + creatorId +
                ", creation_timestamp=" + creationTimestamp +
                '}';
    }
}
