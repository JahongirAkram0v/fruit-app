package com.example.fruit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "_order")
public class Order {

    @Id
    @Generated
    private String id;
    private String productName;
    private String description;
    private double price;
    //
    private String imageName;
    private String imageType;
    private byte[] imageData;
}
