package dev.backend.UniTalk.chat.message;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import org.springframework.http.ResponseEntity;
import javax.validation.Valid;

@Controller
public class MessageController
{
    private final SimpMessagingTemplate template;
    private final MessageControllerService messageControllerService;

    @Autowired
    public MessageController(SimpMessagingTemplate template, MessageControllerService messageControllerService)
    {
        this.template = template;
        this.messageControllerService=messageControllerService;
    }

    //receive message to /chat/room/1 then resend msg to /topic/room/1
    @MessageMapping("/room/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId,@Payload MessageDto messageDto)
    {
        //room == thread
        //append message to thread's list of messages
        messageControllerService.save(roomId,messageDto);

        //send message to room(broker??)
        template.convertAndSend("/topic/room/"+roomId, messageDto);
    }

    @GetMapping("/api/room/{roomId}/msg")
    public CollectionModel<EntityModel<MessageDto>> findChatMessages( @PathVariable Long roomId)
    {
        List<EntityModel<MessageDto>> messages = messageControllerService.all(roomId);

        return CollectionModel.of(messages);
    }
}
