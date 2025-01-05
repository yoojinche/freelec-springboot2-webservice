package com.jojoldu.book.springboot.domain.posts;


import com.jojoldu.book.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder
    public Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }


    // ..? 엥 여기에서 작업 안하는거 아니었나? 껍데기..?
    // 여기에 있는 값을 바꿔야되긴하는데 setter가 없으니까 이렇게 값을 변경하는건 여기서 하도록한다..
    // 그럼 다른것들은 왜 service에 있는거에 뭐지  아..
    // 서비스에서 repo에 있는거 써가지고 save했었ㄴ는데..?
    // 그거는 그냥 값을 넣기만 해서 그런가?
    // 값을 읽고 싶으너소 ㄷ이제 서비스에서 있는거를 가져온건데..
    // 그래서 이런 형식으로 업뎃해달라고 하는것임

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}
