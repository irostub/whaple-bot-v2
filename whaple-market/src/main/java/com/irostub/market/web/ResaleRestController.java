package com.irostub.market.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/resale")
public class ResaleRestController {
    private final ResaleService resaleService;

    @GetMapping("/posts")
    public ResponseEntity<PagingResponse<List<ResaleDto>>> getResalePosts(@RequestParam(defaultValue = "0") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer limit,
                                                      @RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String keyword){
        Page<ResaleDto> search = resaleService.search(page, limit, name, keyword);
        return ResponseEntity.ok(PagingResponse.ok(search));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Response<ResaleDto>> getResalePost(@PathVariable Long postId){
        ResaleDto resaleDto = resaleService.getPost(postId);
        return ResponseEntity.ok(Response.ok(resaleDto));
    }

    @PostMapping(value = "/posts", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Response<ResaleDto>> registerPost(@RequestPart ResaleDto resaleDto,
                                                            @RequestPart(required = false) List<MultipartFile> images){
        ResaleDto register = resaleService.register(resaleDto, images);
        return ResponseEntity.ok(Response.ok(register));
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<Response<ResaleDto>> putPost(@PathVariable Long postId,
                                     @RequestPart ResaleDto resaleDto,
                                     @RequestPart List<MultipartFile> newImages){
        ResaleDto put = resaleService.putPost(postId, resaleDto, newImages);
        return ResponseEntity.ok(Response.ok(put));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Response<Boolean>> deletePost(@PathVariable Long postId,
                                                        @RequestBody WebAppUserDto webAppUserDto) {
        boolean result = resaleService.deleteResalePost(postId, webAppUserDto.getId());
        return ResponseEntity.ok(Response.ok(result));
    }
}
