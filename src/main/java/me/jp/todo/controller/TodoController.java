package me.jp.todo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jp.todo.service.TodoService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    /**
     * to-do 목
     * @param todoType
     * @param todoItem
     * @param model
     * @return
     */
    @GetMapping("")
    public String getAllTodo(@RequestParam(value = "todo_type", required = false) String todoType,
                             @RequestParam(value ="todo_item", required = false) String todoItem,
                             Model model) {
        model.addAttribute("todoType", todoType);
        model.addAttribute("todoItem", todoItem);
        model.addAttribute("todoList", todoService.getTodoList(todoType, todoItem));
        return "list";
    }


    /**
     * to-do 추가
     * @param param
     * @return
     */
    @PostMapping("")
    public @ResponseBody ResponseEntity addTodo(@RequestBody Map<String, String> param) {
        int result = todoService.addTodo(param.get("content"), param.get("ref"));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * to-do 삭제
     * @param no
     * @return
     */
    @DeleteMapping("/{no}")
    public @ResponseBody ResponseEntity deleteTodo(@PathVariable(value = "no") Long no) {
        int result = todoService.deleteTodo(no);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * to-do 완료 / 미완료
     * @param param
     * @param no
     * @return
     */
    @PutMapping("/status/{no}")
    public @ResponseBody ResponseEntity updStatusTodo(@RequestBody Map<String, String> param, @PathVariable(value = "no") Long no) {
        int result = todoService.updStatusTodo(param.get("isFinish"), no);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * to-do 수정
     * @param param
     * @param no
     * @return
     */
    @PutMapping("/{no}")
    public @ResponseBody ResponseEntity updTodo(@RequestBody Map<String, String> param, @PathVariable(value = "no") Long no) {
        int result = 0;

        boolean isUpdate = true;

        // 참조 No 형식에 맞지 않으면 수정 안됨
        if (!Strings.isEmpty(param.get("ref"))) {
            Pattern p = Pattern.compile("^[0-9,]+$");
            Matcher m = p.matcher(param.get("ref"));

            if (!m.find()) {
                isUpdate = false;
            }
        }

        if (isUpdate) {
            result = todoService.updTodo(param.get("content"), param.get("ref"), no);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}