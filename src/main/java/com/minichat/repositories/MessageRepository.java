package com.minichat.repositories;

import com.minichat.models.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    /*@Query("select m from Message m order by id desc")
    List<Message> findeLastTwentyOrderByIdDesc();*/
    @Query(value = "select m from Message m")
    List<Message> findWithPageable(Pageable pageable);
}
