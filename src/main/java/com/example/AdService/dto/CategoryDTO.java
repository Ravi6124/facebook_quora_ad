package com.example.AdService.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {

    @NonNull
    private String categoryId;
    @NonNull
    private String categoryName;
    @NonNull
    private List<String> tags;
}
