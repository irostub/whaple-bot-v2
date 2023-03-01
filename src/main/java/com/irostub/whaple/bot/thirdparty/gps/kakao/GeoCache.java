package com.irostub.whaple.bot.thirdparty.gps.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeoCache {
    private final GeoService geoService;

    @Cacheable(key = "#address", value = "geo")
    public GeoResponse getGeoResponse(String address) {
        log.info("is not cached");
        Mono<GeoResponse> geo = geoService.getGeo(address)
                .subscribeOn(Schedulers.boundedElastic());
        Mono<GeoResponse> geoKeyword = geoService.getGeoKeyword(address)
                .subscribeOn(Schedulers.boundedElastic());
        Tuple2<GeoResponse, GeoResponse> block = Mono.zip(geo, geoKeyword).block();

        GeoResponse t1 = block.getT1();
        GeoResponse t2 = block.getT2();

        if(t1.getDocuments() == null || t1.getDocuments().isEmpty()){
            return t2;
        }
        return t1;
    }
}
