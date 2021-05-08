package dev.backend.UniTalk.thread;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.backend.UniTalk.group.Group;
import dev.backend.UniTalk.category.Category;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "threads")
@NoArgsConstructor
@Getter @Setter
public class Thread {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long threadId;

    @Size(min = 1, max = 128, message = "Thread name: must be between 1 and 128 chars")
    @Basic(optional = false)
    private String title;

    @Basic(optional = false)
    private Long creatorId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Column(nullable = true)
    private Long catId;

    private Long lastReplyAuthorId;

    @Basic(optional = false)
    private Timestamp creationTimestamp;

    private Timestamp lastReplyTimestamp;

    public Thread(String title,
                  Long creatorId,
                  Long catId,
                  Group group,
                  Long lastReplyAuthorId,
                  Timestamp creationTimestamp,
                  Timestamp lastReplyTimestamp) {
        this.title = title;
        this.creatorId = creatorId;
        this.catId = catId;
        this.group = group;
        this.lastReplyAuthorId = lastReplyAuthorId;
        this.creationTimestamp = creationTimestamp;
        this.lastReplyTimestamp = lastReplyTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Thread)) return false;
        Thread thread = (Thread) o;
        return threadId.equals(thread.threadId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(threadId);
    }
}
