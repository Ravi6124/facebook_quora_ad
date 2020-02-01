package com.example.AdService.services.impl;

import com.example.AdService.document.Ad;
import com.example.AdService.document.Category;
import com.example.AdService.dto.AdDTO;
import com.example.AdService.dto.CategoryDTO;
import com.example.AdService.dto.onclickapi.OnClickRequest;

import com.example.AdService.document.UserCache;
import com.example.AdService.feignclient.AnalyticsClient;
import com.example.AdService.repository.AdRepository;
import com.example.AdService.repository.CategoryRepository;
import com.example.AdService.services.AdService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.util.List;

@Service
public class AdServiceImpl implements AdService {


    @Autowired
    AdRepository adRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    KafkaTemplate<String,OnClickRequest> kafkaTemplate;

    @Autowired
    AnalyticsClient analyticsClient;

    @Value("${jwt.secret}")
    private  String SECRET_KEY;


    private static final String IMAGE_PATTERN =
            "([^\\s]+(\\.(?i)(jpg|png|gif|bmp|jpeg))$)";

    private Matcher matcher;
    private Pattern pattern;

    @Override
    public void onClick(OnClickRequest onClickRequest) {

        onClickRequest.setUserId(getUserId(onClickRequest.getUserId()));
        sendOnclick(onClickRequest);


    }

    @Override
    public String addAd(AdDTO adDTO) {

        String imageUrl = adDTO.getImageUrl();
        matcher = pattern.matcher(imageUrl);
        if(!matcher.matches() || adDTO.getImageUrl().equals(adDTO.getTargetUrl()))
            return "false";

        Ad ad = new Ad();
        BeanUtils.copyProperties(adDTO,ad);
        return adRepository.save(ad).toString();

    }

    @Override
    public String addCategory(Category category) {


        return categoryRepository.save(category).toString();

        //return true;

    }

    @Override
    public List<CategoryDTO> getCategories() {

        ModelMapper modelMapper = new ModelMapper();

        List<Category> categories = categoryRepository.findAll();

        List<CategoryDTO> categoryDTOS = Arrays.asList(modelMapper.map(categories, CategoryDTO[].class));

        return categoryDTOS;
    }

    @Override
    public List<String> getTags() {
        ModelMapper modelMapper = new ModelMapper();

        List<Category> categories = categoryRepository.findAll();

        List<CategoryDTO> categoryDTOS = Arrays.asList(modelMapper.map(categories, CategoryDTO[].class));


        List<String> tags = new ArrayList<>();
        for(int i=0;i<categories.size();i++){
            tags.addAll(categoryDTOS.get(i).getTags());
        }
        //return categoryDTOS;

        return  tags;
    }

    public void sendOnclick(OnClickRequest onClickRequest){

        kafkaTemplate.send("clicks",onClickRequest);

    }


    @Override
    public List<Ad> findByTag(String tag) {
        return adRepository.findByTag(tag);
        //return adRepository.findAll(tags);
    }

    public  String getUserId(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        return claims.get("userId").toString();
    }


    @Override
    public List<Ad> findByAdId(String adId) {
        return adRepository.findByAdId(adId);
    }

    @Override
    @Cacheable(value = "userCache")
    public UserCache getAds(String userId) {

        //Todo : use a feign client to call the finction to get user specific tags
        List<Ad> finalAds = new ArrayList<>();
        List<String> tags = new ArrayList<>();
//        tags.addAll(analyticsClient.findActionListByUserId(userId));

        //TODO: ADD RADOM TAGS WHEN WE DON'T GET ANY INTERESTS
//        tags.add("Clothing");
//        tags.add("Cricket");
//
        List<String> tagList = getTags();
        Random random = new Random();
        tags.add(tagList.get(random.nextInt(tagList.size())));
        tags.add(tagList.get(random.nextInt(tagList.size())));


        for(int i=0;i<tags.size();i++){
            finalAds.addAll(adRepository.findByTag(tags.get(i)));
        }

        System.out.println(finalAds);

        UserCache userCache=new UserCache();
        userCache.setAds(finalAds);
        userCache.setUserId(userId);

        return userCache;

    }


}
