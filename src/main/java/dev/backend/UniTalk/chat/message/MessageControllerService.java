package dev.backend.UniTalk.chat.message;

import dev.backend.UniTalk.exception.ResourceNotFoundException;
import dev.backend.UniTalk.thread.Thread;
import dev.backend.UniTalk.thread.ThreadRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MessageControllerService
{
    private final ThreadRepository threadRepository;
    private final MessageRepository messageRepository;

    public MessageControllerService(ThreadRepository threadRepository,MessageRepository messageRepository)
    {
        this.threadRepository = threadRepository;
        this.messageRepository = messageRepository;
    }

    public MessageDto save(Long threadId,MessageDto messageDto)
    {
        Thread thread=threadRepository.findById(threadId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found thread with id = " + threadId));

        Message message = new Message(messageDto.getContent(),messageDto.getSender(),messageDto.getTimestamp(),thread);
        messageRepository.save(message);

        return messageDto;
    }

    public List<EntityModel<MessageDto>> all(Long threadId)
    {
        Thread thread=threadRepository.findById(threadId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found thread with id = " + threadId));

        return messageRepository.findByThread(thread).stream()
                .map(message -> EntityModel.of(new MessageDto(message.getContent(),message.getSender(),message.getTimestamp())))
                .collect(Collectors.toList());
    }
}