package com.backend.dto.request.page;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatReqDto {
    private String model;
    private List<Message> messages;

    public ChatReqDto(String model, String prompt) {
        this.model = model;
        this.messages =  new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }
}
