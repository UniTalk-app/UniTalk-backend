package dev.backend.UniTalk.chat.message;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

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
