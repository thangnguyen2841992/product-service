package com.order.product.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageId;

    private String imageLink;

    private Date dateCreated;

    private String imageDescription;

    @ManyToOne
    private Product product;

    public Image(String imageLink, Date dateCreated, String imageDescription, Product product) {
        this.imageLink = imageLink;
        this.dateCreated = dateCreated;
        this.imageDescription = imageDescription;
        this.product = product;
    }
}
