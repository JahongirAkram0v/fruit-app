package com.example.fruit.controller;

import com.example.fruit.botService.SendService;
import com.example.fruit.model.User;
import com.example.fruit.service.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MyBot extends TelegramWebhookBot {

    private final Dotenv dotenv = Dotenv.load();
    private final String botUsername = dotenv.get("TELEGRAM_BOT_USERNAME");
    private final String botWebhookPath = dotenv.get("TELEGRAM_BOT_WEBHOOK_PATH");

    private final UserService userService;
    private final SendService sendService;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            User user = userService.findById(chatId).orElseGet(
                    () -> User.builder()
                            .id(chatId)
                            .build()
            );

            if (message.hasText() && message.getText().equals("/start")) {
                Map<String, Object> response = Map.of(
                        "chat_id", chatId,
                        "text", "Assalomu alaykum, botga xush kelibsiz!"
                );
                sendService.send(response, "sendMessage");
            }

            if (user.getNumber() == null && !message.hasContact()) {
                Map<String, Object> response =  Map.of(
                        "chat_id", chatId,
                        "text", "Telefon raqamizni kiriting",
                        "reply_markup", Map.of(
                                "keyboard", List.of(
                                        List.of(
                                                Map.of(
                                                        "text", "Telefon raqamini yuborish",
                                                        "request_contact", true
                                                )
                                        )
                                ),
                                "one_time_keyboard", true,
                                "resize_keyboard", true
                        )
                );
                sendService.send(response, "sendMessage");
                return null;
            }

            if (message.hasContact()) {
                if (user.getNumber() == null) {
                    user.setNumber(message.getContact().getPhoneNumber());
                    userService.save(user);
                    Map<String, Object> response = Map.of(
                            "chat_id", chatId,
                            "text", "Raqamingiz qabul qilindi",
                            "reply_markup", Map.of(
                                    "remove_keyboard", true
                            )
                    );
                    sendService.send(response, "sendMessage");
                }
            }
        }

        return null;
    }

    @Override
    public String getBotPath() {
        return botWebhookPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
