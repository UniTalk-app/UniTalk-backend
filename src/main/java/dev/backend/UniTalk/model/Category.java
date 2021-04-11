package dev.backend.UniTalk.model;

import lombok.*;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "category")
@NoArgsConstructor
@Getter @Setter
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer category_id;

    @Basic(optional = false)
    @Column(length = 128)
    private String name;

    @Basic(optional = false)
    private Integer group_id;

    @Basic(optional = false)
    private Timestamp creation_timestamp;

    public Category(Integer id,String name,Integer gid,Timestamp creation_timestamp)
    {
        this.category_id=id;
        this.name=name;
        this.creation_timestamp=creation_timestamp;
        this.group_id=gid;
    }

    @Override
    public String toString() {
        return "category{" +
                "category_id=" + category_id +
                ", name='" + name + '\'' +
                '}';
    }
}
