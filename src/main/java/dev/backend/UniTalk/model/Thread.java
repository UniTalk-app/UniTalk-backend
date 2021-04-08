package dev.backend.UniTalk.model;

import lombok.*;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "thread")
@NoArgsConstructor
@Getter @Setter
public class Thread {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer thread_id;

    @Basic(optional = false)
    @Column(length = 128)
    private String title;

    @Basic(optional = false)
    private Integer creator_id;

    private Integer category_id;

    @Basic(optional = false)
    private Integer group_id;

    @Basic(optional = false)
    private Integer last_reply_author_id;

    @Basic(optional = false)
    private Timestamp creation_timestamp;

    private Timestamp last_reply_timestamp;


    @Override
    public String toString() {
        return "thread{" +
                "thread_id=" + thread_id +
                ", title='" + title + '\'' +
                '}';
    }
}
