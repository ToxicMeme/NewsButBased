package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyTelegramBot extends TelegramLongPollingBot {
    private final Map<String, String> responses = new HashMap<>();
    private final Map<Long, String> userStates = new HashMap<>();

    ArrayList<String> quotesTinkov = new ArrayList<>();
    ArrayList<String> modifiedContent = new ArrayList<>();

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    Random rand = new Random();
    int count = 0;

    public MyTelegramBot() {
        responses.put("свидетели", "знакомые чуваки");
        responses.put("предположительно", "вроде как");
        responses.put("новые исследования", "пост на Tumblr'e");
        responses.put("восстановить", "отомстить за");
        responses.put("космос", "кооосмос");
        responses.put("смартфон", "тамагочи");
        responses.put("электрический", "атомный");
        responses.put("депутат", "эльфийский лорд");
        responses.put("лидеры партий", "духи реки");
        responses.put("отказался давать комментарии", "виновен, и все это знают");
        responses.put("глобальное потепление", "потепление на планете Фантазия");
        responses.put("экономический кризис", "временные трудности с деньгами");
        responses.put("пандемия", "вирусный квест");
        responses.put("инновации", "магические штуки");
        responses.put("интернет", "глобальная паутина");
        responses.put("президент", "главный волшебник");
        responses.put("выборы", "шоу талантов для политиков");
        responses.put("информация", "слухи из параллельной вселенной");
        responses.put("безопасность", "защита от злобных троллей");
        responses.put("технологии", "умные игрушки для взрослых");
        responses.put("климатические изменения", "приключения погоды");
        responses.put("фонд", "копилка для мечтателей с большими амбициями");
        responses.put("мировая экономика", "глобальная игра в монополию");
        responses.put("социальные сети", "площадки для обмена мемами и сплетнями");
        responses.put("критика", "негативные отзывы от тех, кто не понимает шуток");
        responses.put("развитие", "бесконечные попытки стать лучше без результата");

        quotesTinkov.add("Это было не просто смело, это было пиздец как смело");
        quotesTinkov.add("Ну это пиздец какой-то просто! Ну сколько можно");
        quotesTinkov.add("Я ошибся! Я могу один раз ошибиться?");
        quotesTinkov.add("Какое то величие, какая-то хуйня блять, мне вообще они не интересны");
        quotesTinkov.add("Вот мне лично это не интересно, за других сказать не могу");
        quotesTinkov.add("Круто! Да это ж круто!");
        quotesTinkov.add("Все мы виноваты в этом пиздеце");
        quotesTinkov.add("Я как бы не понимал весь масштаб этого пиздеца");
        quotesTinkov.add("Нууу мне взяло где-то 48 часов как бы осознать вообще что происходит");
        quotesTinkov.add("Ну что это за пиздец такой? Ну как это может быть в 21 веке?");
        quotesTinkov.add("Да, мне было страшно, но я это сделал");
        quotesTinkov.add("Это была, скорее всего, бюрократическая просто спешка");
        quotesTinkov.add("То есть, понимаешь, ты должен страдать, и тогда..");
        quotesTinkov.add("Миш, мне похуй, я так чувствую");
        quotesTinkov.add("Я не хочу с этим говном жить просто, я хочу это забыть");
        quotesTinkov.add("Великая компания");
        quotesTinkov.add("Супергуд");
        quotesTinkov.add("Сомнительно, но okaэээy");
        quotesTinkov.add("It’s okey");
        quotesTinkov.add("Это их выбор, и я не могу их осуждать");
        quotesTinkov.add("Харе, ну вот харе!");
        quotesTinkov.add("Блять, я заплакал");
        quotesTinkov.add("Ни-ху-я, вот просто ни-ху-я");
        quotesTinkov.add("Если честно, я обрадовался. Сказал: вот же суки, вот получите");
        quotesTinkov.add("Прав я или не прав? Не прав");
    }

    public void onUpdateReceived(Update update) {
        String messageText = update.getMessage().getText().toLowerCase();
        long chatId = update.getMessage().getChatId();
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (messageText) {
                case "/start":
                    sendSimpleResponse(chatId, "Выбирай раздел в меню");
                    break;
                case "/science":
                    manageNewsResponse(chatId, "https://ria.ru/science/", "span.cell-list__item-title");
                    break;
                case "/sport":
                    manageNewsResponse(chatId, "https://rsport.ria.ru/", "span.cell-list__item-title");
                    break;
                default:
                    if (userStates.containsKey(chatId)) {
                        switch (userStates.get(chatId)) {
                            case "next_news":
                                count++;
                                if (count < modifiedContent.size()) {
                                    sendNews(chatId, modifiedContent.get(count));
                                } else {
                                    sendSimpleResponse(chatId, "На этом новости этого раздела пока все");
                                    userStates.remove(chatId);
                                    count = 0;
                                }
                        }
                    }
            }
        }

    }

    private void manageNewsResponse(long chatId, String URL, String cssQuery) {
        if (!modifiedContent.isEmpty()) {
            modifiedContent.clear();
        }

        createNews(URL, cssQuery);
        sendNews(chatId, modifiedContent.get(count));
        if (!userStates.isEmpty()) {
            userStates.remove(chatId);
        }

        userStates.put(chatId, "next_news");
    }

    private void createNews(String URL, String cssQuery) {
        try {
            Document doc = Jsoup.connect(URL).get();
            Elements spans = doc.select(cssQuery);
            for (Element span : spans) {
                StringBuilder contentBuilder = new StringBuilder(span.text());
                for (Map.Entry<String, String> entry : responses.entrySet()) {
                    int index = contentBuilder.indexOf(entry.getKey());
                    while (index != -1) {
                        contentBuilder.replace(index, index + entry.getKey().length(), entry.getValue());
                        index = contentBuilder.indexOf(entry.getKey(), index + entry.getValue().length());
                    }
                }
                modifiedContent.add(contentBuilder.toString());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendNews(long chatId, String content) {
        SendMessage message = new SendMessage();
        String quote = quotesTinkov.get(rand.nextInt(quotesTinkov.size()));
        String formattedQuote = "_" + quote + "_";
        String text = content + "\n\n- " + formattedQuote;
        message.setChatId(chatId);
        message.setText(text);
        message.enableMarkdown(true);
        message.setReplyMarkup(replyKeyboardMarkup);
        initKeyboard("Следующая новость");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private void sendSimpleResponse(long chatId, String content) {
        SendMessage message = new SendMessage();
        ReplyKeyboardRemove remove = new ReplyKeyboardRemove(true);
        message.setChatId(chatId);
        message.setText(content);
        message.setReplyMarkup(remove);

        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private void initKeyboard(String button) {
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRows.add(keyboardRow);
        keyboardRow.add(new KeyboardButton(button));
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }

    public String getBotUsername() {
        return "News, but экшуали based";
    }

    public String getBotToken() {
        StringBuilder api = new StringBuilder();

        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(".idea/API"))) {
            while((line = reader.readLine()) != null) {
                api.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return api.toString();
    }
}
