package kz.iamthewatch.springbot.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import kz.iamthewatch.springbot.model.ChatMessageHistory;
import kz.iamthewatch.springbot.repository.ChatMessageHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static kz.iamthewatch.springbot.utils.ApplicationConstants.FIRST_PAGE;
import static kz.iamthewatch.springbot.utils.ApplicationConstants.MESSAGE_HISTORY_LIMIT;

@Service
@RequiredArgsConstructor
public class ChatMessageHistoryService {

    private final ChatMessageHistoryRepository chatMessageHistoryRepository;

    public void saveMessage(Long chatId, String userMessage, String assistantMessage) {
        ChatMessageHistory chatMessageHistory = new ChatMessageHistory();
        chatMessageHistory.setChatId(chatId);
        chatMessageHistory.setUserMessage(userMessage);
        chatMessageHistory.setAssistantMessage(assistantMessage);
        chatMessageHistory.setCreatedAt(LocalDateTime.now());

        chatMessageHistoryRepository.save(chatMessageHistory);
    }

    public List<ChatMessageHistory> getLastMessages(Long chatId) {
        List<ChatMessageHistory> messages = chatMessageHistoryRepository
                .findByChatIdOrderByCreatedAtDesc(chatId, PageRequest.of(FIRST_PAGE, MESSAGE_HISTORY_LIMIT));

        messages.sort(Comparator.comparing(ChatMessageHistory::getCreatedAt));
        return messages;
    }
}