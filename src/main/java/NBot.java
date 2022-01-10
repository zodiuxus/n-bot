import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.EnumSet;
import java.util.HashMap;

public class NBot {
    public static JDA jda;

    public static void main(String[] args) throws LoginException {
        jda = JDABuilder.createDefault("",
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_TYPING).disableCache(EnumSet.of(
                        CacheFlag.CLIENT_STATUS,
                        CacheFlag.ACTIVITY,
                        CacheFlag.ROLE_TAGS,
                        CacheFlag.VOICE_STATE,
                        CacheFlag.EMOTE
                )).setActivity(Activity.playing("a horrible Dragon's drinking game."))
                .build();

        try {
            Gson gson = new Gson();
            HashMap<User, Integer> copyMap = new HashMap<>();
            JsonReader reader = new JsonReader(new FileReader("src/jsons/wordMap.json"));
            copyMap = gson.fromJson(reader, copyMap.getClass());
            Listener.setWordMap(copyMap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        jda.addEventListener(new Listener());
    }
}
