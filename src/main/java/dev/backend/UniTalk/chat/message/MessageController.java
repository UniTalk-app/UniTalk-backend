package dev.backend.UniTalk.chat.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
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
        MessageDto msgToSend= messageControllerService.save(roomId,messageDto);

        //send message to room(broker??)
        template.convertAndSend("/topic/room/"+roomId, msgToSend);
    }

    @GetMapping("/api/room/{roomId}/msg")
    public CollectionModel<EntityModel<MessageDto>> all(@PathVariable Long roomId)
    {
        List<EntityModel<MessageDto>> list=messageControllerService.all(roomId);

        return CollectionModel.of(list);
    }
}
