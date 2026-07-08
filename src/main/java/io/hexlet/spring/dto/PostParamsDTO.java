package io.hexlet.spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PostParamsDTO {
    private String titleCont;      // Поиск по вхождению в заголовок
    private String contentCont;    // Поиск по вхождению в содержание
    private Long userId;           // Фильтр по автору
    private Boolean published;     // Фильтр по статусу публикации
    private LocalDate createdAtGt; // Созданы после даты
    private LocalDate createdAtLt; // Созданы до даты
}
