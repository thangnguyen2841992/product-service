package com.order.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WaitingChatResponse {
    private int messageId;

    private String content;

    private int userId;
    private String userName;

    private Date dateCreated;

    private int staffAssignId;
    private String staffAssignName;


    private boolean deleted;
}
