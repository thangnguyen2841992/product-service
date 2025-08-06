package com.order.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatResponse {

    private int formUserId;

    private String formUserName;

    private int toUserId;

    private String toUserName;

    private int chatRoomId;

    private String content;

    private Date dateCreated;

}
