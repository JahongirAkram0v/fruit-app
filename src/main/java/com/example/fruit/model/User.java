package com.example.fruit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "_user")
public class User {

    // TODO: qoshimcha ishlar olib borishim kerak
    @Id
    private Long id;
    private String number;

}
