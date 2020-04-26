package me.jp.todo.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class TblTodo implements Serializable {

    private static final long serialVersionUID = -3716649632114675863L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private String content;

    private String ref;

    private String isFinish;

    private String isActive;

    @Column(updatable = false)
    private LocalDateTime regDate;

    private LocalDateTime lastUpdDate;

    @Builder
    public TblTodo(Long no, String content, String ref, String isFinish, String isActive, LocalDateTime regDate, LocalDateTime lastUpdDate) {
        this.no = no;
        this.content = content;
        this.ref = ref;
        this.isFinish = isFinish;
        this.isActive = isActive;
        this.regDate = regDate;
        this.lastUpdDate = lastUpdDate;
    }
}
