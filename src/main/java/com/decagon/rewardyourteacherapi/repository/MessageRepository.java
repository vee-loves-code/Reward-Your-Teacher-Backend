package com.decagon.rewardyourteacherapi.repository;

import com.decagon.rewardyourteacherapi.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ifeoluwa on 19/09/2022
 * @project
 */

public interface MessageRepository extends JpaRepository<Message,Long> {

}
