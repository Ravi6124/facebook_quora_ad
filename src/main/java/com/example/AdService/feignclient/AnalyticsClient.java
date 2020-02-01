package com.example.AdService.feignclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "repo",url = "10.177.68.68:8102/repo")
@CrossOrigin(allowedHeaders = "*",origins = "*")
public interface AnalyticsClient {

    @GetMapping("/findActionListByUserId/{id}")
    public List<String> findActionListByUserId(@PathVariable("id") String userId);
}
