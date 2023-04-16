package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mpa {
    private int id;
    private String name;

    public Mpa() {
    }

    public Mpa(int id) {
        this.id = id;
    }

}