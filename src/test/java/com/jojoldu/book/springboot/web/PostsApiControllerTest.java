package com.jojoldu.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @LocalServerPort
    private int port;

    // 근데 왜 여기선 autowired로 스프링빈 등록하는것임?

    @Autowired
    private TestRestTemplate restTemplate;

    // 이 테코 다시 써보기

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_등록된다() throws Exception{
        //given
        String title = "title";
        String content = "content";
        // 그래서 여기서 dto를 가져오는데
        // 그것을 이제 builder로 가져오는것임
        // 근데 여기서 builder는 requestDto에 존재함

        // 여기서 만들어진 값은 근데 post아님? 아 근데 왜 여기서는 toEntity 사용안함?
        // ㅁㄹ 근데 그냥 이렇게 만들엇삼. dto.builder가 이해항ㄴ
        // requestDto로 builder를 불렀기 때문에 dto로 save가 되고 그게 저장이 된것임 아 집가고 싶어라.. 찌찌발
        // 아 이렇게 만든건 살짝 그리고 이건 롬복에 있는것ㅇㅁ
        // 집에서 할까 그냥
        // 집에 일찍가서 좀 뭐야 일기 좀 써야지
        // 할말이 너무 많아 유진아
        // 어쨋든 이렇게 만들어서 테스트를 하느넋임
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        // 뭐 대충 이렇게 쓰면 port가 불리나봐~ url 만들어줌
        String url = "http://localhost:" + port + "/api/v1/posts";

        // when
        // 엥 아니 근데 여기서 requestEntity가 갑자기 어디서 나타난거임 걍 갑자기 쓴다고?
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());


        // 전체적으로 임의로 넣어본다음에 테스트하고 있는거네 이거 그래서 ㅈ금
        // dto 만들어본다음에  url로 만들었음
        // 그걸 이제 불렀ㅇㄹ때 잘 뭐..반환을 하늕 보ㅡㄴ거아닌가

        // 일단 주소적으로 잘 둘어갔다가 나오는지 확인을 하시구 그다음에 이제
        // 뭐 .. 반환한 값이 제대로 뭐 요청을 한대로 수행하는지확인하기 위해  requestdto 막 썼음


        // then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);


        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_수정된다() throws Exception{
        // given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        // 일단 이렇게 값을 빌더로 저장한다고 하셈 그리고 posts로 리턴받아

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        // 그리고 값을 수정하셈
        // 수정도 빌더로 진행해주었쭘

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
//        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());


        // then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
}
