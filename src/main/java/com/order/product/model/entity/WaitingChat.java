package com.order.product.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class WaitingChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageId;

    private String content;

    private int userId;

    private Date dateCreated;

    private int staffAssignId;


    private boolean deleted;
}
