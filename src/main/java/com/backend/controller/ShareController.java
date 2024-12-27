package com.backend.controller;

import com.backend.document.drive.ShareLink;
import com.backend.dto.request.drive.RemoveDepartmentRequestDto;
import com.backend.dto.request.drive.ShareLinkRequest;
import com.backend.dto.request.drive.ShareRequestDto;
import com.backend.dto.request.drive.SharedUser;
import com.backend.dto.response.drive.FolderDto;
import com.backend.service.FolderService;
import com.backend.service.ShareService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api")
public class ShareController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ShareService shareService;
    private final FolderService folderService;

    @PostMapping("/share/drive/{type}/{id}")
    public ResponseEntity<?> shareDepartments(@RequestBody ShareRequestDto request, @PathVariable String type, @PathVariable String id, HttpServletRequest httpServletRequest) {
        log.info("Request received with type: {}, id: {}, body: {}", type, id, request);

        String uid = (String) httpServletRequest.getAttribute("uid");
        boolean permission =  ReadWritePermission(id, uid);
        if(!permission) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("공유 권한이 없습니다.");

        }

        boolean result = shareService.shareUser(request, type, id);
        if(result) {
            return  ResponseEntity.ok().body("Request received");
        }else{
            return  ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Request failed");

        }
    }

    @PostMapping("/share/link")
    public ResponseEntity<?> createShareLink(@RequestBody ShareLinkRequest sharedRequest, HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        log.info("Request received with id: {}, body: {}", sharedRequest.getId(),uid);

        boolean permission =  ReadWritePermission(sharedRequest.getId(), uid);
        if(!permission) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("공유 권한이 없습니다.");

        }
        if(sharedRequest.getOwnerId().equals(uid)){
            ShareLink shared = shareService.generateToken(sharedRequest.getId(),uid);

            if(shared != null) {
                String linkToken = shared.getToken();
                return ResponseEntity.ok(Map.of("shareToken", linkToken));
            }else{
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("공유 중 에러가 발생했습니다.");
            }

        }else{
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("공유 권한이 없습니다.");
        }

    }

    @PostMapping("/share/token/validate")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        boolean isValid = shareService.validateToken(token);

        if (isValid) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }



    @PostMapping("/drive/share/department")
    public ResponseEntity<?> shareDepartment(@RequestParam String id ,@RequestParam String type,@RequestBody ShareRequestDto departments,HttpServletRequest servletRequest ) {
        log.info("여기지금,"+type+"id"+id+"request"+departments);
        String uid = (String) servletRequest.getAttribute("uid");
        boolean permission =  ReadWritePermission(id, uid);
        if(!permission) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("공유 권한이 없습니다.");

        }

        boolean result = shareService.sharedDepartment(departments,type,id);

        if (result) {
            return ResponseEntity.ok().body(departments);
        }else{
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(departments);
        }

    }

    @PostMapping("/drive/share/remove-department")
    public ResponseEntity<?> removeDepartment(@RequestBody RemoveDepartmentRequestDto request, HttpServletRequest servletRequest) {
        log.info("여기여기여기!!!!"+request);
        String remover = (String) servletRequest.getAttribute("uid");
        boolean permission =  ReadWritePermission(request.getId(), remover);
        if(!permission) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("공유 권한이 없습니다.");

        }
        if(remover.equals(request.getOwnerId())) {

            shareService.deletedDepartment(request);

            return ResponseEntity.ok().body("삭제성공");

        }else{



        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Collections.emptyList());
    }

    @GetMapping("/invite/validate/{invitationId}")
    public ResponseEntity<?> validateInvitation(@PathVariable String invitationId , HttpServletRequest servletRequest) {

        String uid = (String) servletRequest.getAttribute("uid");
        Map<String, Object> response = shareService.invitationInvaild(invitationId,uid);
        log.info(" 공유결과!!!!"+response);
        return ResponseEntity.ok().body(response);
    }


    private boolean ReadWritePermission(String id, String uid) {

        FolderDto folder = folderService.getParentFolder(id);
        if(!folder.getOwnerId().equals(uid)){
            List<SharedUser> list = folder.getSharedUsers()
                    .stream()
                    .filter(sharedUser -> sharedUser.getId().equals(uid))
                    .collect(Collectors.toList());

            // 첫 번째 일치하는 권한 반환
            if (list.isEmpty()) {
                return false;
            }else{
                String permission = list.get(0).getPermission();
                if ("읽기".equals(permission) || "수정".equals(permission)) {
                    return false;
                }
            }

        }
        return true;
    }
}
