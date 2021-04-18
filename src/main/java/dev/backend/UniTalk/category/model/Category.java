package dev.backend.UniTalk.model;

import lombok.*;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "category")
@NoArgsConstructor
@Getter @Setter
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    @Column(length = 128)
    private String name;

    @Basic(optional = false)
    private Long group_id;

    @Basic(optional = false)
    private Timestamp creation_timestamp;

    public Category(String name,Long gid,Timestamp creation_timestamp)
    {
        this.name=name;
        this.group_id=gid;
        this.creation_timestamp=creation_timestamp;
    }

    @Override
    public String toString() {
        return "category{" +
                "category_id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
