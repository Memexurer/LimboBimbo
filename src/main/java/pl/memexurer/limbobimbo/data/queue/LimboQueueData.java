package pl.memexurer.limbobimbo.data.queue;

import org.bukkit.entity.Player;

import java.util.PriorityQueue;

public class LimboQueueData {
    private final PriorityQueue<LimboQueuePlayer> queue = new PriorityQueue<>();

    public void addPlayer(Player player) {
        queue.add(new LimboQueuePlayer(player));
    }

    public LimboQueuePlayer removePlayer() {
        return queue.remove();
    }

    public PriorityQueue<LimboQueuePlayer> getQueue() {
        return queue;
    }
}
