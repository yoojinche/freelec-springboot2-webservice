package com.jojoldu.book.springboot.service.posts;

import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsListResponseDto;
import com.jojoldu.book.springboot.web.dto.PostsResponseDto;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    // 그니까 그래도 여기서 역할은 하는게
    // 서비스에서 아 뭐야 dto를 이용해서 repo 메서드를 사용하네
    // 수정하기 위한 메소드임
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당게시글이 없습니다. id = "+ id));

        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    // 조회하기 위한 메소드임
    @Transactional
    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당게시글이 없습니다. id="+ id));

        return new PostsResponseDto(entity);
    }

    // repo 내부 메소드로 가져옴 그래서 posts로 가져오는데 이걸 dto에 담아서 리턴해준다
    // repo는 entity형식으로 주나봐
    // 그냥 PostsResponseDto entity로 바로 받으면 안되나?
    // 싶은데 살짝 뭐라고 해야됨? 새로 만들어야됨 없으니까
    // 읽어주고 싶기 때문에 이걸 이제 post로 받은거임 그리고 리턶하고



    // 조회하기 위한 클래스임
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    // 삭제하기 전에 트랜잭션적으로 관리하고 싶은 메소드임
    // 여기서도 dto 만들어야되는건가?

    @Transactional
    public void delete(Long id){
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        postsRepository.delete(posts);
        // jpa에 기존에 있는 메소드를 사용해서 삭제하도록한다
    }

}
