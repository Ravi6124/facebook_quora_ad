package com.example.AdService.controller;

import com.example.AdService.document.Ad;
import com.example.AdService.dto.RecieveTagDTO;
import com.example.AdService.services.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@CrossOrigin(origins =  "*", allowedHeaders = "*")
@RequestMapping("/ads")
@RestController
public class AdController {

    @Autowired
    AdService adService;

    @GetMapping("/getAds/{userId}")
    @Cacheable(value = "update")
    public ResponseEntity<List<Ad>> getAds(@PathVariable(value = "userId") String userId )
    {
        //TODO api call from analytical service
        List<String> tags=null;

        List<Ad> ads=new ArrayList<>();

        List<Ad> finalAds=new ArrayList<>();

        for(int i=0;i<tags.size();i++)
        {

            List<Ad> tagAds=adService.findByTags(tags.get(i)).get();
            ads.addAll(tagAds);
        }

        for(int i=0;i<5;i++)
        {
            Random rand = new Random();
            Ad ad= ads.get(rand.nextInt(ads.size()));
            finalAds.add(ad);
        }

        return new ResponseEntity<List<Ad>>(finalAds,HttpStatus.FOUND);
    }

    //@CachePut(value = "update" ,key = "")
    @KafkaListener(topics = "listenTags",groupId = "group_id")
    public void consume(RecieveTagDTO recieveTagDTO){

        //todo find the category System.out.println("Consumed message ");
        List<String> tags=recieveTagDTO.getTags();
        String userId=recieveTagDTO.getUserId();

        for(int i=0;i<tags.size();i++)
        {
            List<Ad> tagAds = adService.findByTags(tags.get(i)).get();

        }

    }
}
