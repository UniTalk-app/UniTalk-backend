package dev.backend.UniTalk.chat.message;

import java.sql.Timestamp;
import lombok.*;

//Ligther version of class Message, will be used for transfer through web
@Getter @Setter
public class MessageDto
{
    private String content;
    private String sender;
    private Timestamp timestamp;
//TODO... avatar of sender

    public MessageDto( String content,String sender,Timestamp timestamp)
    {
        this.content=content;
        this.sender=sender;
        this.timestamp=timestamp;
    }
}
