package dev.backend.UniTalk.group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.backend.UniTalk.thread.Thread;
import dev.backend.UniTalk.category.Category;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
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
