package com.example.AdService.dto.onclickapi;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OnClickRequest {

    @NotNull
    String adId;
    @NotNull
    String tag;
    @NotNull
    String advertiserId;
    @NotNull
    String categoryName;
    @NotNull
    String userId;
    String description;
    @NotNull
    String source;
    @NotNull
    String targetUrl;
}
