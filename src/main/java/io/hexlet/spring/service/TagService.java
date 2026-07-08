package io.hexlet.spring.service;

import io.hexlet.spring.dto.TagCreateDTO;
import io.hexlet.spring.dto.TagDTO;
import io.hexlet.spring.dto.TagUpdateDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.TagMapper;
import io.hexlet.spring.model.Tag;
import io.hexlet.spring.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;

    public List<TagDTO> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(tagMapper::toDTO)
                .toList();
    }

    public TagDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag with id " + id + " not found"));
        return tagMapper.toDTO(tag);
    }

    public TagDTO createTag(TagCreateDTO tagCreateDTO) {
        Tag tag = tagMapper.toEntity(tagCreateDTO);
        Tag savedTag = tagRepository.save(tag);
        return tagMapper.toDTO(savedTag);
    }

    public TagDTO updateTag(Long id, TagUpdateDTO tagUpdateDTO) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag with id " + id + " not found"));

        tagMapper.updateEntity(tagUpdateDTO, existingTag);
        Tag savedTag = tagRepository.save(existingTag);
        return tagMapper.toDTO(savedTag);
    }

    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag with id " + id + " not found");
        }
        tagRepository.deleteById(id);
    }
}
