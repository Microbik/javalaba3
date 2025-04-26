package com.example.lab1.bot;

import com.example.lab1.service.JokeService;
import com.example.lab1.model.Joke;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DiscordBot extends ListenerAdapter {

    private final JokeService jokeService;
    private final long[] allowedUserIds = {555767665986109450L};  // Пример разрешенных user ID

    public DiscordBot(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // Игнорировать сообщения от ботов
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw().trim();

        // Команда для получения случайной шутки
        if (message.equalsIgnoreCase("!joke")) {
            String joke = jokeService.getRandomJokeText();
            event.getChannel().sendMessage(joke).queue();
        }
        // Команда для получения шутки по ID
        else if (message.startsWith("!joke ")) {
            String[] parts = message.split(" ");
            if (parts.length == 2) {
                try {
                    Long id = Long.parseLong(parts[1]);
                    Optional<Joke> jokeOptional = jokeService.getById(id);
                    if (jokeOptional.isPresent()) {
                        event.getChannel().sendMessage(jokeOptional.get().getText()).queue();
                    } else {
                        event.getChannel().sendMessage("Шутка с таким ID не найдена.").queue();
                    }
                } catch (NumberFormatException e) {
                    event.getChannel().sendMessage("Некорректный формат ID.").queue();
                }
            }
        }
        // Команда для получения количества шуток в базе
        else if (message.equalsIgnoreCase("!jokecount")) {
            long count = jokeService.getAll().size();
            event.getChannel().sendMessage("Количество шуток в базе: " + count).queue();
        }
        // Команда для добавления шутки
        else if (message.startsWith("!jokeadd")) {
            if (isUserAllowed(event.getAuthor().getIdLong())) {
                String jokeText = message.substring(9).trim();  // Убираем команду и пробел
                if (!jokeText.isEmpty()) {
                    Joke newJoke = new Joke();
                    newJoke.setText(jokeText);
                    jokeService.create(newJoke);
                    event.getChannel().sendMessage("Шутка добавлена!").queue();
                } else {
                    event.getChannel().sendMessage("Текст шутки не может быть пустым.").queue();
                }
            } else {
                event.getChannel().sendMessage("У вас нет прав для добавления шуток.").queue();
            }
        }
        // Команда для удаления шутки по ID
        else if (message.startsWith("!jokedelete")) {
            if (isUserAllowed(event.getAuthor().getIdLong())) {
                String[] parts = message.split(" ");
                if (parts.length == 2) {
                    try {
                        Long id = Long.parseLong(parts[1]);
                        Optional<Joke> jokeOptional = jokeService.getById(id);
                        if (jokeOptional.isPresent()) {
                            jokeService.delete(id);
                            event.getChannel().sendMessage("Шутка удалена.").queue();
                        } else {
                            event.getChannel().sendMessage("Шутка с таким ID не найдена.").queue();
                        }
                    } catch (NumberFormatException e) {
                        event.getChannel().sendMessage("Некорректный формат ID.").queue();
                    }
                } else {
                    event.getChannel().sendMessage("Пожалуйста, укажите ID шутки для удаления.").queue();
                }
            } else {
                event.getChannel().sendMessage("У вас нет прав для удаления шуток.").queue();
            }
        }
        else if (message.startsWith("!jokeupdate")) {
            if (isUserAllowed(event.getAuthor().getIdLong())) {
                String[] parts = message.split(" ", 3); // Разделяем на 3 части: команда, ID, новый текст
                if (parts.length == 3) {
                    try {
                        Long id = Long.parseLong(parts[1]);
                        String newText = parts[2].trim();
                        if (!newText.isEmpty()) {
                            Optional<Joke> jokeOptional = jokeService.getById(id);
                            if (jokeOptional.isPresent()) {
                                Joke jokeToUpdate = jokeOptional.get();
                                jokeToUpdate.setText(newText);
                                jokeService.update(id, jokeToUpdate);
                                event.getChannel().sendMessage("Шутка обновлена!").queue();
                            } else {
                                event.getChannel().sendMessage("Шутка с таким ID не найдена.").queue();
                            }
                        } else {
                            event.getChannel().sendMessage("Новый текст шутки не может быть пустым.").queue();
                        }
                    } catch (NumberFormatException e) {
                        event.getChannel().sendMessage("Некорректный формат ID.").queue();
                    }
                } else {
                    event.getChannel().sendMessage("Пожалуйста, укажите ID и новый текст шутки для обновления.").queue();
                }
            } else {
                event.getChannel().sendMessage("У вас нет прав для обновления шуток.").queue();
            }
        }
    }

    // Проверка, является ли пользователь разрешенным для выполнения команд добавления/удаления шуток
    private boolean isUserAllowed(long userId) {
        for (long allowedId : allowedUserIds) {
            if (allowedId == userId) {
                return true;
            }
        }
        return false;
    }
}
