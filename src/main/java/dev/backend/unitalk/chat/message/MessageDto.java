package dev.backend.unitalk.chat.message;

import java.sql.Timestamp;
import lombok.*;

//Ligther version of class Message, will be used for transfer through web
@Getter @Setter
public class MessageDto
{
    private String content;
    private String senderUsername;
    private Timestamp sendingTimestamp;
//TODO... avatar of sender

    public MessageDto( String content,String sender,Timestamp sendingTimestamp)
    {
        this.content=content;
        this.senderUsername =sender;
        this.sendingTimestamp = sendingTimestamp;
    }
}
