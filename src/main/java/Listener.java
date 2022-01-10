import com.google.gson.Gson;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Listener extends ListenerAdapter {
    public static final String prefix = "n!";
    public String keyword = null;

    private static HashMap<User, Integer> wordMap;

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        LOGGER.info("{} is ready to count some words.", event.getJDA().getSelfUser());
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);

        User user = event.getAuthor();

        String[] args = event.getMessage().getContentRaw().split(" ");
        String cmd = event.getMessage().getContentRaw();
        Gson gson = new Gson();
        wordMap = new HashMap<>();

        if (user.isBot()) return;

        if (cmd.equalsIgnoreCase(keyword)) {
            if (!wordMap.containsKey(user)) wordMap.put(user, 1);
            else wordMap.put(user, 1+wordMap.get(user));
        }
        if(args[0].equalsIgnoreCase(prefix + "count")) {
            event.getChannel().sendMessage("Getting your data now...\n").queue();
            if (keyword == null) event.getChannel().sendMessage("Keyword has not been set yet! Use n!change [word] to change the keyword.").queue();
            else
                if (wordMap.get(user) == null) event.getChannel().sendMessage(user.getAsMention() + " hasn't said the keyword yet!").queue();
            else event.getChannel().sendMessage(user.getAsMention() + " has said the word " +
                     + wordMap.get(user) + " times.").queue();
        }
        if (args[0].equalsIgnoreCase(prefix + "change")) {
            keyword = args[1];
            event.getChannel().sendMessage("The keyword has been changed to " + keyword).queue();
        }

        if(args[0].equalsIgnoreCase(prefix + "save")) {
            try {
                FileWriter writer = new FileWriter("src/jsons/wordMap.json");
                String jsonString = gson.toJson(getWordMap());

                writer.write(jsonString);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void setWordMap(HashMap<User, Integer> copyMap) {
        wordMap = copyMap;
    }

    public HashMap<User, Integer> getWordMap() { return wordMap;}
}
