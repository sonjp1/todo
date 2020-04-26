package me.jp.todo.repository;

import me.jp.todo.domain.TblTodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<TblTodo, Long> {
    List<TblTodo> findAllByIsActive(String isActive);
    List<TblTodo> findAllByIsFinishAndIsActive(String isFinish, String isActive);
    List<TblTodo> findAllByContentIgnoreCaseLikeAndIsActive(String content, String isActive);
    List<TblTodo> findAllByLastUpdDateGreaterThanEqualAndIsActive(LocalDateTime lastUpdDate, String isActive);
    List<TblTodo> findAllByRegDateGreaterThanEqualAndIsActive(LocalDateTime regDate, String isActive);

    Optional<TblTodo> findByNoAndIsActive(Long no, String isActive);

    @Modifying
    @Transactional
    @Query("update TblTodo set isFinish=:isFinish, lastUpdDate=current_timestamp where no=:no")
    int updateIsFinishByNo(@Param("isFinish") String isFinish, @Param("no") Long no);

    @Modifying
    @Transactional
    @Query("update TblTodo set content=:content, ref=:ref, lastUpdDate=current_timestamp where no=:no")
    int updateTodoByNo(@Param("content") String content, @Param("ref") String ref, @Param("no") Long no);

    @Modifying
    @Transactional
    int deleteByNo(Long no);
}