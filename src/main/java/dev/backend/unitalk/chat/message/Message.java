package dev.backend.unitalk.chat.message;

import dev.backend.unitalk.thread.Thread;
import lombok.*;

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
    private Long messageId;

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
        var message = (Message) o;
        return messageId.equals(message.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }
}
