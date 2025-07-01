package petspa.search_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import petspa.search_service.entity.UserSearchDocument;
import petspa.search_service.service.UserSearchService;

import java.util.Optional;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class UserSearchController {

    private final UserSearchService userSearchService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserSearchDocument> getUserContext(@PathVariable String userId) {
        Optional<UserSearchDocument> userOpt = userSearchService.getUserContext(userId);
        return userOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // (Optional) API để insert/update thủ công
    @PostMapping("/user")
    public ResponseEntity<UserSearchDocument> save(@RequestBody UserSearchDocument doc) {
        return ResponseEntity.ok(userSearchService.saveOrUpdate(doc));
    }
}
