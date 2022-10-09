package com.bobocode.marsfeignclient.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@FeignClient(name = "mars-feign-client", url = "${nasa.api.url}")
public interface MarsFeignClient {

    @GetMapping
    Photos getPhotos(
            @RequestParam("api_key") String apiKey,
            @RequestParam Integer sol,
            @RequestParam(required = false) String camera);

    record Photos(List<Photo> photos) {
    }

    record Photo(@JsonProperty("img_src") String ImgSrc) {
    }
}
