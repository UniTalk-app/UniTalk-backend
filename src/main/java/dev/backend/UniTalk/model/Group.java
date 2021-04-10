package dev.backend.UniTalk.model;

import lombok.*;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "group")
@NoArgsConstructor
@Getter @Setter
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer group_id;

    @Basic(optional = false)
    @Column(length = 128)
    private String name;

    @Basic(optional = false)
    private Integer creator_id;

    @Basic(optional = false)
    private Timestamp creation_timestamp;


    @Override
    public String toString() {
        return "group{" +
                "group_id=" + group_id +
                ", name='" + title + '\'' +
                '}';
    }
}
