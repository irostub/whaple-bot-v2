package com.irostub.market.thirdparty.imgur;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImgurService {
    private final WebClient webClient;
    @Value("${app.imgur.url}")
    private String url;
    @Value("${app.imgur.access-token}")
    private String accessToken;

    public Mono<ImgurImagePostResponse> sendImagePostRequest(byte[] image){
        log.info("[ImgurService.sendImagePostRequest] has image={}", image != null);
        return webClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(imageToBase64(image)))
                .retrieve()
                .bodyToMono(ImgurImagePostResponse.class);
    }

    public List<ImgurImagePostResponse> sendImagePostRequests(List<byte[]> images){
        Flux<ImgurImagePostResponse> response = Flux.fromIterable(images)
                .flatMap(this::sendImagePostRequest);
        return response.toStream().collect(Collectors.toList());
    }

    private String imageToBase64(byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }

    public void deleteImages(List<String> deleteHashs) {
        Flux<HttpStatus> httpStatusFlux = Flux.fromIterable(deleteHashs)
                .flatMap(this::deleteImage);
        long count = httpStatusFlux.toStream().filter(s -> !s.is2xxSuccessful()).count();
        log.error("delete image fail count={}", count);
    }
    public Mono<HttpStatus> deleteImage(String deleteHash){
        return webClient.delete().uri(url + "/" + deleteHash)
                .header("Authorization", accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity()
                .map(ResponseEntity::getStatusCode);
    }
}
