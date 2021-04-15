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
    private Integer creator_id;

    private Integer category_id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    private Integer last_reply_author_id;

    @Basic(optional = false)
    private Timestamp creation_timestamp;

    private Timestamp last_reply_timestamp;

    public Thread(String title,
                  Integer creator_id,
                  Integer category_id,
                  Group group,
                  Integer last_reply_author_id,
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
        return getThread_id().equals(thread.getThread_id()) && getTitle().equals(thread.getTitle())
                && getCreator_id().equals(thread.getCreator_id()) && Objects.equals(getCategory_id(),
                thread.getCategory_id()) && getGroup().equals(thread.getGroup())
                && Objects.equals(getLast_reply_author_id(), thread.getLast_reply_author_id())
                && getCreation_timestamp().equals(thread.getCreation_timestamp())
                && Objects.equals(getLast_reply_timestamp(), thread.getLast_reply_timestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getThread_id(), getTitle(), getCreator_id(), getCategory_id(), getGroup(),
                getLast_reply_author_id(), getCreation_timestamp(), getLast_reply_timestamp());
    }
}
