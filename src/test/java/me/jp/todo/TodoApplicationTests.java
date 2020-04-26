package me.jp.todo;

import me.jp.todo.domain.TblTodo;
import me.jp.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class TodoApplicationTests {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void saveTodo() {
//        todoRepository.save(TblTodo
//                .builder()
//                .content("toDoTest")
//                .regDate(LocalDateTime.now())
//                .build());
    }

}
