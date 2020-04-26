package me.jp.todo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jp.todo.domain.TblTodo;
import me.jp.todo.repository.TodoRepository;
import org.omg.CORBA.Object;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public List<TblTodo> getTodoList(String todoType, String todoItem) {
        List<TblTodo> todoList = new ArrayList<>();

        if (!StringUtils.isEmpty(todoType) && !StringUtils.isEmpty(todoItem)) {
            switch (todoType) {
                case "no":
                    todoRepository.findByNoAndIsActive(Long.parseLong(todoItem), "Y").ifPresent(todoList::add);
                    break;
                case "fin":
                    todoList = todoRepository.findAllByIsFinishAndIsActive(todoItem, "Y");
                    break;
                case "content":
                    todoList = todoRepository.findAllByContentIgnoreCaseLikeAndIsActive("%" + todoItem + "%", "Y");
                    break;
                case "last":
                    todoItem = todoItem.replaceAll("-", "");
                    todoList = todoRepository.findAllByLastUpdDateGreaterThanEqualAndIsActive(dateParser(todoItem), "Y");
                    break;
                case "reg":
                    todoItem = todoItem.replaceAll("-", "");
                    todoList = todoRepository.findAllByRegDateGreaterThanEqualAndIsActive(dateParser(todoItem), "Y");
                    break;
            }
        }
        else {
            todoList = todoRepository.findAllByIsActive("Y");
        }

        return todoList;
    }

    public int addTodo(String content, String ref) {
        int result = 0;

        TblTodo tblTodo = todoRepository.save(TblTodo.builder()
                .content(content)
                .ref(ref)
                .isFinish("N")
                .isActive("Y")
                .regDate(LocalDateTime.now())
                .lastUpdDate(null)
                .build());

        log.info("No : {}", tblTodo.getNo());

        if (tblTodo.getNo() != null) {
            result++;
        }

        return result;
    }


    public int deleteTodo(Long no) {
        return todoRepository.deleteByNo(no);
    }


    public int updStatusTodo(String isFinish, Long no) {
        int result = 0;

        boolean isUpdate = true;

        List<String> refList = new ArrayList<>();

        Optional<TblTodo> refData = Optional.empty();
        Optional<TblTodo> todoData = todoRepository.findByNoAndIsActive(no, "Y");

        if ("Y".equals(isFinish)) {

            // 수정할 todo의 ref를 id로 사용하여 isFinish를 체크
            // 하나라도 "N"이면 안됨
            if (todoData.isPresent() && !StringUtils.isEmpty(todoData.get().getRef())) {
                refList = Arrays.asList(todoData.get().getRef().split(","));

                for (String s : refList) {
                    refData = todoRepository.findByNoAndIsActive(Long.parseLong(s), "Y");
                    if (refData.isPresent() && "N".equals(refData.get().getIsFinish())) {
                        isUpdate = false;
                        break;
                    }
                }
            }
        }

        if (isUpdate) {
            result = todoRepository.updateIsFinishByNo(isFinish, no);
        }

        return result;
    }


    public int updTodo(String content, String ref, Long no) {
        return todoRepository.updateTodoByNo(content, ref, no);
    }


    private LocalDateTime dateParser(String dateStr) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(dateStr.replaceAll("-", ""), dtf).atStartOfDay();
    }
}