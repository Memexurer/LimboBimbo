package pl.memexurer.limbobimbo.task;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import pl.memexurer.limbobimbo.config.impl.PluginConfiguration;
import pl.memexurer.limbobimbo.utils.ChatUtil;

public class ScoreboardTask implements Runnable {
    private PluginConfiguration configuration;
    private boolean hasPlaceholderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

    public ScoreboardTask(PluginConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = board.registerNewObjective("limbobimbo", "dummy");

            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(ChatUtil.fixColor(configuration.SCOREBOARD_TITLE));

            int i = configuration.SCOREBOARD_CONTENT.size();

            for (String str : configuration.SCOREBOARD_CONTENT) {
                String strong = ChatUtil.fixColor(str);
                if (hasPlaceholderAPI) strong = PlaceholderAPI.setPlaceholders(p, strong);
                Score score = objective.getScore(strong);
                score.setScore(i);
                i--;
            }

            p.setScoreboard(board);
        }
    }
}
