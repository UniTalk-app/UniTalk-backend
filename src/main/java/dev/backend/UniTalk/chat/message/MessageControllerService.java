package dev.backend.UniTalk.chat.message;

import dev.backend.UniTalk.exception.ResourceNotFoundException;
import dev.backend.UniTalk.thread.Thread;
import dev.backend.UniTalk.thread.ThreadRepository;
import dev.backend.UniTalk.user.User;
import dev.backend.UniTalk.user.UserRepository;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.Optional;
import java.sql.Timestamp;

@Service
public class MessageControllerService
{
    private final UserRepository userRepository;
    private final ThreadRepository threadRepository;
    private final MessageRepository messageRepository;

    public MessageControllerService(ThreadRepository threadRepository,MessageRepository messageRepository,UserRepository userRepository)
    {
        this.threadRepository = threadRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public MessageDto save(Long threadId,MessageDto messageDto)
    {
        Optional<User> userOptional =userRepository.findByUsername(messageDto.getSenderUsername());
        if( !userOptional.isPresent())
        {
            throw new ResourceNotFoundException("Not found user with username = " + messageDto.getSenderUsername());
        }

        Date date = new Date();
        Timestamp tstamp=new Timestamp(date.getTime());

        //update thread informations about chat
        Thread thread=threadRepository.findById(threadId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found thread with id = " + threadId));

        thread.setLastReplyAuthorId(userOptional.get().getId());
        thread.setLastReplyTimestamp(tstamp);
        threadRepository.save(thread);

        //convert and save message
        Message message = new Message(messageDto.getContent(),
                                      userOptional.get().getId(),
                                      tstamp,
                                      thread);
        messageRepository.save(message);

        return convertToMessageDto(message);
    }

    public List<EntityModel<MessageDto>> all(Long threadId)
    {
        Thread thread=threadRepository.findById(threadId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found thread with id = " + threadId));

        return messageRepository.findByThread(thread).stream()
                .map(message -> EntityModel.of(
                        convertToMessageDto(message)))
                .collect(Collectors.toList());
    }

    private MessageDto convertToMessageDto(Message message)
    {
        Optional<User> user =userRepository.findById(message.getSenderId());
        if(user.isPresent())
            return new MessageDto(message.getContent(),userRepository.findById(message.getSenderId()).get().getUsername(),message.getSendingTimestamp());
        else
            return new MessageDto(message.getContent(),"unknownuser",message.getSendingTimestamp());
    }

}