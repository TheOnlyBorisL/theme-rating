package net.etfbl.themerating.controller;

import net.etfbl.themerating.model.entity.Content;
import net.etfbl.themerating.model.request.ContentRequest;
import net.etfbl.themerating.model.response.ContentResponse;
import net.etfbl.themerating.service.ContentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final ContentService _service;

    public ContentController(ContentService service){
        _service = service;
    }

    @PostMapping
    public ResponseEntity<Boolean> CreateContent(@RequestBody ContentRequest request){
        try {
            _service.CreateContent(request);
            return ResponseEntity.ok(true);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @GetMapping("/{clientID}")
    public ResponseEntity<List<ContentResponse>> GetContentForClient(@PathVariable long clientID){
        try{
            return ResponseEntity.ok(_service.getClientContent(clientID));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}