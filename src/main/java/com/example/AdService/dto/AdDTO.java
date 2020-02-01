package com.example.AdService.dto;

import com.example.AdService.document.Category;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;


@Data
public class AdDTO {

    public String adId;
    @NotNull
    public String categoryName;
    @NotNull
    public String tag;
    @NotNull
    public String imageUrl;
    @NotNull
    public String targetUrl;
    @NotNull
    public String description;
    @NotNull
    public String advertiserId;
    @NotNull
    public String location;
}
