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
    private Long thread_id;

    @Size(min = 1, max = 128, message = "Group name: must be between 1 and 128 chars")
    @Basic(optional = false)
    private String title;

    @Basic(optional = false)
    private Long creator_id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Column(nullable = true)
    private Long cat_id;

    private Long last_reply_author_id;

    @Basic(optional = false)
    private Timestamp creation_timestamp;

    private Timestamp last_reply_timestamp;

    public Thread(String title,
                  Long creator_id,
                  Long category_id,
                  Group group,
                  Long last_reply_author_id,
                  Timestamp creation_timestamp,
                  Timestamp last_reply_timestamp) {
        this.title = title;
        this.creator_id = creator_id;
        this.cat_id = category_id;
        this.group = group;
        this.last_reply_author_id = last_reply_author_id;
        this.creation_timestamp = creation_timestamp;
        this.last_reply_timestamp = last_reply_timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Thread)) return false;
        Thread thread = (Thread) o;
        return thread_id.equals(thread.thread_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(thread_id);
    }
}
