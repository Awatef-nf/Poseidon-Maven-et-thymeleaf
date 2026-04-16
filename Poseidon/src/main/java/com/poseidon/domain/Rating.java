package com.poseidon.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "rating")
@Data
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Moodys rating is mandatory")
    private String moodysRating;

    @NotBlank(message = "S&P rating is mandatory")
    private String sandPRating;

    @NotBlank(message = "Fitch rating is mandatory")
    private String fitchRating;

    @NotNull(message = "Order number is mandatory")
    private Integer orderNumber;

    public Rating(String moodysRating, String sandPRating, String fitchRating, int i) {
    }

    public Rating() {

    }
}
