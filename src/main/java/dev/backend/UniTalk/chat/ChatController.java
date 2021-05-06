package dev.backend.UniTalk.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import dev.backend.UniTalk.chat.message.Message;
import dev.backend.UniTalk.chat.message.MessageDto;
import dev.backend.UniTalk.chat.message.MessageRepository;
import dev.backend.UniTalk.thread.Thread;
import dev.backend.UniTalk.thread.ThreadRepository;
import dev.backend.UniTalk.exception.ResourceNotFoundException;


@Controller
public class ChatController
{
    private final ThreadRepository threadRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate template;

    @Autowired
    public ChatController(SimpMessagingTemplate template,ThreadRepository threadRepository,MessageRepository messageRepository)
    {
        this.template = template;
        this.threadRepository=threadRepository;
        this.messageRepository=messageRepository;
    }

    //receive message to /chat/1 then resend msg to /topic/room/1
    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId,@Payload MessageDto messageDto)
    {
        //room == thread
        //append message to thread's list of messages
        Thread thread=threadRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found thread with id = " + roomId));

        Message message = new Message(messageDto.getContent(),messageDto.getSender(),messageDto.getTimestamp(),thread);
        messageRepository.save(message);

        //send message to room(broker??)
        template.convertAndSend("/topic/room/"+roomId, messageDto);
    }
}
