package kz.reself.limma.filestorage.controller;

import kz.reself.limma.filestorage.service.FileService;
import kz.reself.limma.filestorage.v1.dto.FileInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private/v1/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping(value = "/{id}", produces = {"application/json", "application/problem+json"})
    public ResponseEntity<FileInfo> getFileInfo(Long id) {
        return ResponseEntity.ok(fileService.getFileInfo(id));
    }
}
