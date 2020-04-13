package pl.memexurer.limbobimbo.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import pl.memexurer.limbobimbo.LimboBimboPlugin;
import pl.memexurer.limbobimbo.utils.ChatUtil;

public class ScoreboardTask implements Runnable {
    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = board.registerNewObjective("limbobimbo", "dummy");

            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(ChatUtil.fixColor(LimboBimboPlugin.getPluginInstance().getConfiguration().SCOREBOARD_TITLE));

            int i = LimboBimboPlugin.getPluginInstance().getConfiguration().SCOREBOARD_CONTENT.size();

            for (String str : LimboBimboPlugin.getPluginInstance().getConfiguration().SCOREBOARD_CONTENT) {
                Score score = objective.getScore(ChatUtil.fixColor(str));
                score.setScore(i);
                i--;
            }

            p.setScoreboard(board);
        }
    }
}
