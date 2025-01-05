package com.jojoldu.book.springboot.web.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/*
* 클래스 이름 어케 쓰지 싶은게 이게 뭐냐면
* 엔티티이름 하고 싶은 쿼리 response인지 request인지
* postSaveRequestDto
* postUpdateRequestDto
* PostsResposeDto
*
* */

// 솔직히 언제 언제 이런게 필요한지 잘 모르겟음
@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private String title;
    private String content;

    @Builder
    public PostsUpdateRequestDto(String title, String content){
        this.title = title;
        this.content = content;
    }

}
