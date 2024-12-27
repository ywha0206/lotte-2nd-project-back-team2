package com.backend.controller;

import com.backend.document.drive.Folder;
import com.backend.document.page.Page;
import com.backend.dto.request.page.ChatReqDto;
import com.backend.dto.request.page.GetTitleChildDto;
import com.backend.dto.request.page.PageDto;
import com.backend.dto.response.page.ChatRespDto;
import com.backend.dto.response.user.GetUsersAllDto;
import com.backend.entity.folder.Permission;
import com.backend.repository.page.PageRepository;
import com.backend.service.FolderService;
import com.backend.service.SftpService;
import com.backend.service.mongoDB.PageService;
import com.backend.util.PermissionType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
@Log4j2
@RequiredArgsConstructor
public class PageController {


    private final PageService pageService;
    private final FolderService folderService;
    private final SftpService sftpService;
    private final RestTemplate template;
    private final PageRepository pageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/page")
    public ResponseEntity<?> postNewPage(HttpServletRequest request){
        String uid = (String)request.getAttribute("uid");
        ResponseEntity<?> resp = pageService.postNewPage(uid);
        return resp;
    }

    @PostMapping("/page/templete")
    public ResponseEntity<?> postNewTemplete(HttpServletRequest request){
        String uid = (String)request.getAttribute("uid");
        ResponseEntity<?> resp = pageService.postNewTemplete(uid);
        return resp;
    }


    @PostMapping("/page/save")
    public ResponseEntity<?> save(HttpServletRequest request, @RequestBody PageDto pageDto) {
        String uid = (String)request.getAttribute("uid");
        log.info("세이브되나?"+pageDto);

        Page page = pageService.save(pageDto);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/page/view/{id}")
    public ResponseEntity<?> view (@PathVariable String id
    ){

        log.info("요청이 들어오나?? /view"+id);
        Map<String,Object> map = new HashMap<>();
        PageDto pageDto = pageService.findById(id);

        map.put("pageDto",pageDto);
        log.info("나온값!!1"+pageDto);

        return ResponseEntity.ok(pageDto);
    }

    @GetMapping("/page/list")
    public ResponseEntity<?> list(HttpServletRequest request){
        String uid = (String)request.getAttribute("uid");

        List<Page> pages = pageService.pageList(uid);

        return ResponseEntity.ok().body(pages);

    }

    @GetMapping("/page/templete")
    public ResponseEntity<?> templeteList(HttpServletRequest request){
        String uid = (String)request.getAttribute("uid");

        List<Page> pages = pageService.templeteList(uid);

        return ResponseEntity.ok().body(pages);

    }

    @GetMapping("/page/content/{pageId}")
    public ResponseEntity<?> getPageContent(
            HttpServletRequest req,
            @PathVariable String pageId
    ){
        System.out.println(pageId);
        Long userId = (Long) req.getAttribute("id");
        ResponseEntity<?> response = pageService.getPageContent(pageId, userId);
        return response;
    }

    @PutMapping("/page/content/{pageId}")
    public ResponseEntity<?> putPageContent(
            HttpServletRequest req,
            @PathVariable String pageId,
            @RequestBody Object content
    ) throws JsonProcessingException {
        System.out.println("이거되고있냐????");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode contentNode = objectMapper.valueToTree(content); // content를 JsonNode로 변환

        // "content" 필드를 제거
        JsonNode contentData = contentNode.get("content");

        // contentData를 다시 JSON 문자열로 변환
        String contentString = objectMapper.writeValueAsString(contentData);

        // 확인
        System.out.println(contentString);
        System.out.println(pageId);
        Long userId = (Long)req.getAttribute("id");
        PageDto pageDto = PageDto.builder().content(contentString).id(pageId).build();
        ResponseEntity<?> response = pageService.putPageContent(pageDto,userId);
        return response;
    }

    @GetMapping("/page/users/{pageId}")
    public ResponseEntity<?> getPageUsers(
            @PathVariable String pageId
    ){
        ResponseEntity<?> resp = pageService.getPageUsers(pageId);
        return resp;
    }

    @GetMapping("/page/title/{pageId}")
    public ResponseEntity<?> getPageTitle (
            @PathVariable String pageId
    ){
        ResponseEntity<?> resp = pageService.getPageTitle(pageId);
        return resp;
    }

    @PutMapping("/page/title")
    public ResponseEntity<?> putPageTitle (
            @RequestParam String pageId,
            @RequestParam String title
    ){
        ResponseEntity<?> resp = pageService.putPageTitle(pageId,title);
        return resp;
    }

    @PutMapping("/page/users")
    public ResponseEntity<?> putPageUsers(
            @RequestParam String pageId,
            @RequestBody List<GetUsersAllDto> users
    ){
        ResponseEntity<?> resp = pageService.putPageUsers(pageId,users);
        return resp;
    }

    @DeleteMapping("/page/{pageId}")
    public ResponseEntity<?> deletePage(@PathVariable String pageId){
        ResponseEntity<?> resp = pageService.deletePage(pageId);
        return resp;
    }

    @GetMapping("/page/role/{pageId}")
    public ResponseEntity<?> getPageRole(
            @PathVariable String pageId
    ){
        ResponseEntity<?> resp = pageService.getPageRole(pageId);
        return resp;
    }

    @PatchMapping("/page/role/{pageId}")
    public ResponseEntity<?> patchPageRole(
            @PathVariable String pageId,
            @RequestParam String readonly
    ){
        ResponseEntity<?> resp = pageService.patchRole(pageId,readonly);
        return resp;
    }

    @PostMapping("/page/image/{pageId}")
    public ResponseEntity<?> postPageImage(
            @PathVariable String pageId,
            @RequestParam("file") MultipartFile file
    ){
        String remoteDir = sftpService.createPageFolder(pageId,"uploads/pages");
        String savedFilename= folderService.generateSavedName(file.getOriginalFilename());
        File tempFile = null;
        try {
            tempFile = File.createTempFile("upload_", "_" + file.getOriginalFilename());
            file.transferTo(tempFile); // MultipartFile 데이터를 임시 파일로 저장

            // SFTP 업로드
            String result = sftpService.uploadFile(tempFile.getAbsolutePath(),remoteDir,savedFilename);
            return ResponseEntity.ok(result);
        } catch ( IOException e) {
            log.error("임시 파일 생성 또는 전송 중 오류 발생: {}", e.getMessage());
        } finally {
            if (tempFile != null && tempFile.exists()) {
                if (tempFile.delete()) {
                    log.info("임시 파일 삭제 성공: {}", tempFile.getAbsolutePath());
                } else {
                    log.warn("임시 파일 삭제 실패: {}", tempFile.getAbsolutePath());
                }
            }
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/page/ai")
    public ResponseEntity<?> getPageAi(
            @RequestParam String text
    ){
        ChatReqDto request = new ChatReqDto("gpt-3.5-turbo", text);
        ChatRespDto chatGPTResponse =  template.postForObject("https://api.openai.com/v1/chat/completions", request, ChatRespDto.class);

        return ResponseEntity.ok(chatGPTResponse);
    }

    @GetMapping("/page/children")
    public ResponseEntity<?> getPageChildren(
            HttpServletRequest req
    ){
        String uid = (String) req.getAttribute("uid");
        Page page = Page.builder()
                .ownerUid(uid)
                .leader(uid)
                .createAt(LocalDateTime.now())
                .content(null)
                .title("하위페이지")
                .build();

        pageRepository.save(page);

        return ResponseEntity.ok(page.getId());
    }

    @PostMapping("/page/children")
    public ResponseEntity<?> postPageChildren(
            HttpServletRequest req,
            @RequestBody GetTitleChildDto title
    ){
        System.out.println(title.getSelectedTextContent());
        String uid = (String) req.getAttribute("uid");
        Page page = Page.builder()
                .ownerUid(uid)
                .leader(uid)
                .createAt(LocalDateTime.now())
                .content(null)
                .title(title.getSelectedTextContent())
                .type(title.getId())
                .build();

        pageRepository.save(page);

        return ResponseEntity.ok(page.getId());
    }

    @GetMapping("/page/child")
    public ResponseEntity<?> getPageChild(
            HttpServletRequest req
    ){
        String uid = (String) req.getAttribute("uid");
        List<Page> page = pageRepository.findAllByOwnerUidAndTypeIsNotAndTypeIsNot(uid,"0","1");

        return ResponseEntity.ok(page);
    }
}
