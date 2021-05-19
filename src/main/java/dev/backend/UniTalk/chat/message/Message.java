package dev.backend.UniTalk.chat.message;

import dev.backend.UniTalk.thread.Thread;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "messages")
@NoArgsConstructor
@Getter @Setter
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long message_id;

    @Size(min = 1, max = 512, message = "Message content: must be between 1 and 512 chars")
    @Basic(optional = false)
    private String content;

    @Basic(optional = false)
    private Long senderId;

    @Basic(optional = false)
    private Timestamp sendingTimestamp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "thread_id", nullable = false)
    private Thread thread;

    public Message(String content, Long senderId, Timestamp sendingTime, Thread thread)
    {
        this.content=content;
        this.senderId = senderId;
        this.sendingTimestamp = sendingTime;
        this.thread=thread;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return message_id.equals(message.message_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message_id);
    }
}
