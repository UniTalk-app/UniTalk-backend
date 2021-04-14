package dev.backend.UniTalk.thread.model;

import lombok.*;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "thread")
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

    @Basic(optional = false)
    private Integer group_id;

    private Integer last_reply_author_id;

    @Basic(optional = false)
    private Timestamp creation_timestamp;

    private Timestamp last_reply_timestamp;

    public Thread(String title,
                  Integer creator_id,
                  Integer category_id,
                  Integer group_id,
                  Integer last_reply_author_id,
                  Timestamp creation_timestamp,
                  Timestamp last_reply_timestamp) {
        this.title = title;
        this.creator_id = creator_id;
        this.category_id = category_id;
        this.group_id = group_id;
        this.last_reply_author_id = last_reply_author_id;
        this.creation_timestamp = creation_timestamp;
        this.last_reply_timestamp = last_reply_timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Thread)) return false;
        Thread thread = (Thread) o;
        return getThread_id().equals(thread.getThread_id()) && getTitle().equals(thread.getTitle()) && getCreator_id().equals(thread.getCreator_id())
                && Objects.equals(getCategory_id(), thread.getCategory_id()) && getGroup_id().equals(thread.getGroup_id())
                && Objects.equals(getLast_reply_author_id(), thread.getLast_reply_author_id()) && getCreation_timestamp().equals(thread.getCreation_timestamp())
                && Objects.equals(getLast_reply_timestamp(), thread.getLast_reply_timestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getThread_id(), getTitle(), getCreator_id(), getCategory_id(),
                getGroup_id(), getLast_reply_author_id(), getCreation_timestamp(), getLast_reply_timestamp());
    }

    @Override
    public String toString() {
        return "thread{" +
                "thread_id=" + thread_id +
                ", title='" + title + '\'' +
                '}';
    }
}
