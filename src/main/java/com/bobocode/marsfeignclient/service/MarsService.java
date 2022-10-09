package com.bobocode.marsfeignclient.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

@Service
public class MarsService {
    private final MarsFeignClient marsFeignClient;
    private final RestTemplate restTemplate;
    @Value("${nasa.api.key}")
    private String nasaKey;

    public MarsService(final MarsFeignClient marsFeignClient, final RestTemplate restTemplate) {this.marsFeignClient = marsFeignClient;
        this.restTemplate = restTemplate;
    }

    public byte[] getLargestPicture(final Integer sol, final String camera) {
        return marsFeignClient.getPhotos(nasaKey, sol, camera).photos()
                .parallelStream()
                .map(this::getImageUlrAndSize)
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .map(url -> restTemplate.getForObject(url, byte[].class))
                .orElseThrow(() -> new IllegalArgumentException("No pictures found"));

    }

    private Map.Entry<URI, Long> getImageUlrAndSize(MarsFeignClient.Photo photo) {
        var location = restTemplate.headForHeaders(photo.ImgSrc()).getLocation();
        var imgSize = restTemplate.headForHeaders(location).getContentLength();
        return Map.entry(location, imgSize);
    }
}
