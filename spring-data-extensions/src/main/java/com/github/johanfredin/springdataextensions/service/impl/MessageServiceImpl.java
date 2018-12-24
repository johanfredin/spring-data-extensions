package com.github.johanfredin.springdataextensions.service.impl;

import com.github.johanfredin.springdataextensions.repository.MessageRepository;
import com.github.johanfredin.springdataextensions.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("messageService")
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public MessageRepository getRepository() {
        return this.messageRepository;
    }

    @Override
    public void setRepository(MessageRepository repository) {
        this.messageRepository = repository;
    }

}
