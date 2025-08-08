package com.order.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRoomResponse {

    private int chatRoomId;

    private int formUserId;
    private String formUsername;

    private int staffId;
    private String staffName;

    private Date dateCreated;

    private boolean isClosed;
}
