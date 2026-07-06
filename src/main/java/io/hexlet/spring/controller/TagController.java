package io.hexlet.spring.controller;

import io.hexlet.spring.dto.TagCreateDTO;
import io.hexlet.spring.dto.TagDTO;
import io.hexlet.spring.dto.TagUpdateDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.TagMapper;
import io.hexlet.spring.model.Tag;
import io.hexlet.spring.repository.TagRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagDTO>> index() {
        List<Tag> tags = tagRepository.findAll();
        List<TagDTO> tagDTOs = tags.stream()
                .map(tagMapper::toDTO)
                .toList();
        return ResponseEntity.ok(tagDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> show(@PathVariable Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag with id " + id + " not found"));
        return ResponseEntity.ok(tagMapper.toDTO(tag));
    }

    @PostMapping
    public ResponseEntity<TagDTO> create(@Valid @RequestBody TagCreateDTO tagCreateDTO) {
        Tag tag = tagMapper.toEntity(tagCreateDTO);
        Tag savedTag = tagRepository.save(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(tagMapper.toDTO(savedTag));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TagUpdateDTO tagUpdateDTO) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag with id " + id + " not found"));

        tagMapper.updateEntity(tagUpdateDTO, existingTag);
        Tag savedTag = tagRepository.save(existingTag);
        return ResponseEntity.ok(tagMapper.toDTO(savedTag));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag with id " + id + " not found");
        }
        tagRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
