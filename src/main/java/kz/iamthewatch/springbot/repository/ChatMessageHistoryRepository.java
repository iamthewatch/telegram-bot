package kz.iamthewatch.springbot.repository;

import java.util.List;
import kz.iamthewatch.springbot.model.ChatMessageHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageHistoryRepository extends JpaRepository<ChatMessageHistory, Long> {

    List<ChatMessageHistory> findByChatIdOrderByCreatedAtDesc(Long chatId, Pageable pageable);
}