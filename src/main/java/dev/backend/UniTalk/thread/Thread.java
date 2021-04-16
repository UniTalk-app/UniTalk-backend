package dev.backend.UniTalk.thread;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.backend.UniTalk.group.Group;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "threads")
@NoArgsConstructor
@Getter @Setter
public class Thread {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long thread_id;

    @Basic(optional = false)
    @Column(length = 128)
    private String title;

    @Basic(optional = false)
    private Long creator_id;

    private Long category_id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

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
        this.category_id = category_id;
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
