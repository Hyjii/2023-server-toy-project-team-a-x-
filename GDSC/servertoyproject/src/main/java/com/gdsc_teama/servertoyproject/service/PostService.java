package com.gdsc_teama.servertoyproject.service;

import com.gdsc_teama.servertoyproject.dto.post.PostResponseDto;
import com.gdsc_teama.servertoyproject.dto.post.PostSaveRequestDto;
import com.gdsc_teama.servertoyproject.dto.post.PostUpdateRequestDto;
import com.gdsc_teama.servertoyproject.entity.Post;
import com.gdsc_teama.servertoyproject.persistence.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    // Save Post
    @Transactional
    public Long save(PostSaveRequestDto requestDto) {
        return postRepository.save(requestDto.toEntity()).getId();
    }

    // Update Post
    @Transactional
    public Long update(Long id, PostUpdateRequestDto requestDto) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            throw new IllegalArgumentException("해당 게시글이 없습니다. id = " + id);
        }

        Post post = optionalPost.get();
        post.update(requestDto.getPostTitle(), requestDto.getPostContent());
        return id;
    }

    // Post Read
    @Transactional(readOnly = true)
    public PostResponseDto findById(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            throw new IllegalArgumentException("해당 게시글이 없습니다. id = " + id);
        }

        return new PostResponseDto(optionalPost.get());
    }

    // List Post Read
    @Transactional(readOnly = true) // readOnly = true를 주면 트랜잭션 범위는 유지하되, 조회 기능만 남겨두어 조회 속도가 개선.
    public List<PostResponseDto> findAllDesc() {
        return postRepository.findAllByOrderByIdDesc().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }


    // Post Delete
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        postRepository.delete(post);
    }
}
