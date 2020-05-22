package pl.memexurer.limbobimbo.task;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class AutoMessageTask implements Runnable {
    private int index;
    private List<BaseComponent[]> messages;

    public AutoMessageTask(List<String> messages) {
        this.messages = messages.stream().map(ComponentSerializer::parse).collect(Collectors.toList());
    }

    @Override
    public void run() {
        if (index >= messages.size()) {
            index = 0;
        }

        for (Player p : Bukkit.getOnlinePlayers()) p.spigot().sendMessage(messages.get(index));

        index++;
    }
}
