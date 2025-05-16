package com.example.lab1.bot;

import com.example.lab1.model.Joke;
import com.example.lab1.service.JokeService;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DiscordBot extends ListenerAdapter {
    private final JokeService jokeService;
    private final long[] allowedUserIds = {555767665986109450L}; // ID админов

    public DiscordBot(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw().trim();
        long userId = event.getAuthor().getIdLong();

        try {
            if (message.equalsIgnoreCase("!joke")) {
                handleRandomJoke(event, userId);
            } else if (message.startsWith("!joke ")) {
                handleJokeById(event, userId);
            } else if (message.equalsIgnoreCase("!topjokes")) {
                handleTopJokes(event);
            } else if (message.startsWith("!jokeadd") && isAdmin(userId, event)) {
                handleAddJoke(event);
            } else if (message.startsWith("!jokedelete") && isAdmin(userId, event)) {
                handleDeleteJoke(event);
            } else if (message.startsWith("!jokeupdate") && isAdmin(userId, event)) {
                handleUpdateJoke(event);
            }
        } catch (Exception e) {
            event.getChannel().sendMessage("Ошибка: " + e.getMessage()).queue();
        }
    }

    private boolean isAdmin(long userId, MessageReceivedEvent event) {
        for (long adminId : allowedUserIds) {
            if (adminId == userId) {
                return true;
            }
        }
        event.getChannel().sendMessage("У вас нет прав на эту команду").queue();
        return false;
    }


    private void handleRandomJoke(MessageReceivedEvent event, long userId) {
        String joke = jokeService.getRandomJokeText();
        event.getChannel().sendMessage(joke).queue();

        // Записываем факт вызова шутки
        jokeService.getRandomJokeId().ifPresent(id ->
                jokeService.recordJokeCall(id, userId)
        );
    }

    private void handleJokeById(MessageReceivedEvent event, long userId) {
        String[] parts = event.getMessage().getContentRaw().split(" ");
        if (parts.length != 2) {
            event.getChannel().sendMessage("Используйте: !joke [ID]").queue();
            return;
        }

        try {
            Long id = Long.parseLong(parts[1]);
            Optional<Joke> joke = jokeService.getById(id);

            if (joke.isPresent()) {
                event.getChannel().sendMessage(joke.get().getText()).queue();
                jokeService.recordJokeCall(id, userId);
            } else {
                event.getChannel().sendMessage("Шутка с ID " + id + " не найдена").queue();
            }
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Некорректный ID").queue();
        }
    }

    private void handleTopJokes(MessageReceivedEvent event) {
        List<Joke> topJokes = jokeService.getTopJokes(5);

        if (topJokes.isEmpty()) {
            event.getChannel().sendMessage("Пока нет статистики по шуткам").queue();
            return;
        }

        StringBuilder sb = new StringBuilder("**Топ-5 популярных шуток:**\n");
        for (int i = 0; i < topJokes.size(); i++) {
            sb.append(i + 1).append(". ").append(topJokes.get(i).getText()).append("\n");
        }
        event.getChannel().sendMessage(sb.toString()).queue();
    }

    private void handleAddJoke(MessageReceivedEvent event) {
        String jokeText = event.getMessage().getContentRaw().substring(9).trim();
        if (jokeText.isEmpty()) {
            event.getChannel().sendMessage("Текст шутки не может быть пустым").queue();
            return;
        }

        Joke newJoke = new Joke();
        newJoke.setText(jokeText);
        jokeService.create(newJoke);

        event.getChannel().sendMessage("Шутка добавлена! ID: " + newJoke.getId()).queue();
    }

    private void handleDeleteJoke(MessageReceivedEvent event) {
        String[] parts = event.getMessage().getContentRaw().split(" ");
        if (parts.length != 2) {
            event.getChannel().sendMessage("Используйте: !jokedelete [ID]").queue();
            return;
        }

        try {
            Long id = Long.parseLong(parts[1]);
            jokeService.delete(id);
            event.getChannel().sendMessage("Шутка удалена").queue();
        } catch (Exception e) {
            event.getChannel().sendMessage("Ошибка: " + e.getMessage()).queue();
        }
    }

    private void handleUpdateJoke(MessageReceivedEvent event) {
        String[] parts = event.getMessage().getContentRaw().split(" ", 3);
        if (parts.length != 3) {
            event.getChannel().sendMessage("Используйте: !jokeupdate [ID] [Новый текст]").queue();
            return;
        }

        try {
            Long id = Long.parseLong(parts[1]);
            String newText = parts[2].trim();

            Joke joke = new Joke();
            joke.setText(newText);

            jokeService.update(id, joke);
            event.getChannel().sendMessage("Шутка обновлена").queue();
        } catch (Exception e) {
            event.getChannel().sendMessage("Ошибка: " + e.getMessage()).queue();
        }
    }
}
