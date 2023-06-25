package com.irostub.market.web;

import com.irostub.domain.entity.market.ImgurImage;
import com.irostub.domain.entity.market.ResaleBoard;
import com.irostub.domain.entity.market.WebAppUser;
import com.irostub.domain.repository.ResaleBoardRepository;
import com.irostub.market.thirdparty.imgur.ImgurImagePostResponse;
import com.irostub.market.thirdparty.imgur.ImgurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResaleService {
    private final ImgurService imgurService;
    private final ResaleBoardRepository resaleBoardRepository;
    private final WebAppUserService webAppUserService;

    @Transactional
    public ResaleDto register(ResaleDto resaleDto, List<MultipartFile> images) {
        WebAppUser webAppUser = webAppUserService.getOrCreateUser(resaleDto.getWebAppUserDto());
        List<byte[]> imageList = fileToBytesList(images);
        List<ImgurImage> imageEntities = registerImageToImgur(imageList);
        ResaleBoard resaleBoard = new ResaleBoard(resaleDto.getTitle(), resaleDto.getContent(), imageEntities, webAppUser);
        ResaleBoard save = resaleBoardRepository.save(resaleBoard);
        return ResaleDto.createFromEntity(save);
    }

    private List<ImgurImage> registerImageToImgur(List<byte[]> imageList) {
        return imgurService.sendImagePostRequests(imageList).stream()
                .map(r -> {
                    ImgurImagePostResponse.Data data = r.getData();
                    return new ImgurImage(
                            data.getLink(),
                            data.getId(),
                            data.getDeletehash());
                })
                .collect(Collectors.toList());
    }

    private static List<byte[]> fileToBytesList(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }
        List<byte[]> imageList = new ArrayList<>();
        for (MultipartFile image : images) {
            try {
                byte[] bytesImage = image.getBytes();
                imageList.add(bytesImage);
            } catch (IOException e) {
                log.error("image is empty={}, name={}", image.isEmpty(), image.getName());
            }
        }
        return imageList;
    }

    @Transactional
    public boolean deleteResalePost(Long postId, Long webAppUserId) {
        Optional<ResaleBoard> byId = resaleBoardRepository.findById(postId);
        if (!byId.isPresent()) {
            return false;
        }
        ResaleBoard resaleBoard = byId.get();
        if (resaleBoard.getWebAppUser().getId().equals(webAppUserId)) {
            resaleBoardRepository.delete(resaleBoard);
            return true;
        }
        return false;
    }

    @Transactional
    public ResaleDto putPost(Long postId, ResaleDto resaleDto, List<MultipartFile> newImages) {
        ResaleBoard resaleBoard = resaleBoardRepository.findById(postId).orElseThrow(NotFoundException::new);
        if (!resaleBoard.getWebAppUser().getId().equals(resaleDto.getWebAppUserDto().getId())) {
            throw new NotAuthorizedException();
        }
        processRemoveImage(resaleDto, resaleBoard);
        List<ImgurImage> imgurImages = processRegisterImage(newImages);
        resaleBoard.updatePost(resaleDto.getTitle(), resaleDto.getContent(), imgurImages);
        ResaleBoard savedResaleBoard = resaleBoardRepository.saveAndFlush(resaleBoard);
        return ResaleDto.createFromEntity(savedResaleBoard);
    }

    private List<ImgurImage> processRegisterImage(List<MultipartFile> multipartFiles) {
        List<byte[]> bytes = fileToBytesList(multipartFiles);
        return registerImageToImgur(bytes);
    }

    private void processRemoveImage(ResaleDto resaleDto, ResaleBoard resaleBoard) {
        List<String> deleteHashs = new ArrayList<>();
        resaleBoard.getImages()
                .forEach(i -> {
                    if (!resaleDto.getImages().contains(i.getUrl())) {
                        deleteHashs.add(i.getDeleteHash());
                        resaleBoard.getImages().remove(i);
                    }
                });
        imgurService.deleteImages(deleteHashs);
    }

    public ResaleDto getPost(Long postId) {
        ResaleBoard resaleBoard = resaleBoardRepository.findAllRelationById(postId).orElseThrow(NotFoundException::new);
        return ResaleDto.createFromEntity(resaleBoard);
    }

    public Page<ResaleDto> search(Integer page, Integer limit, String name, String keyword) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        return resaleBoardRepository.findBySearch(pageRequest, name, keyword)
                .map(ResaleDto::createFromEntity);
    }
}
