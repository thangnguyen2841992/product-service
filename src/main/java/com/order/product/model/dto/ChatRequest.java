package com.order.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRequest {
    private int formUserId;
    private int staffId;

    private String content;

    private int messageId;

    private Date dateCreated;

    private String isStaff;
    private int chatRoomId;
}
