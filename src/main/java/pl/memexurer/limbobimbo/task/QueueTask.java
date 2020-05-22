package pl.memexurer.limbobimbo.task;

import pl.memexurer.limbobimbo.config.impl.PluginConfiguration;
import pl.memexurer.limbobimbo.data.queue.LimboQueueData;
import pl.memexurer.limbobimbo.data.queue.LimboQueuePlayer;
import pl.memexurer.limbobimbo.utils.ChatUtil;

import java.util.Iterator;
import java.util.List;

public class QueueTask implements Runnable {
    private LimboQueueData queueData;
    private PluginConfiguration configuration;

    public QueueTask(PluginConfiguration configuration, LimboQueueData data) {
        this.queueData = data;
        this.configuration = configuration;
    }

    @Override
    public void run() {
        int position = queueData.getQueue().size();
        if (position == 0) return;

        Iterator<LimboQueuePlayer> iterator = queueData.getQueue().iterator();

        position = 1;
        while (iterator.hasNext()) {
            LimboQueuePlayer player = iterator.next();

            if (player.getPlayer() == null) {
                iterator.remove();
                continue;
            }

            if (player.getConnectTime() < configuration.QUEUE_CONNECTION_TIME + 5) {
                if (player.getConnectTime() > configuration.QUEUE_CONNECTION_TIME) {
                    sendMessage(player, configuration.QUEUE_MESSAGE_CONNECTION_FAILED);
                    queueData.getQueue().add(player);
                } else if (player.getConnectTime() < configuration.QUEUE_CONNECTION_TIME)
                    sendMessage(player, configuration.QUEUE_MESSAGE_CONNECTING, "{TIME}", configuration.QUEUE_CONNECTION_TIME - player.getConnectTime() + "");
            } else if (position == 1) sendMessage(player, configuration.QUEUE_MESSAGE_SENDING);
            else sendMessage(player, configuration.QUEUE_MESSAGE_AWAITING, "{POSITION}", position + "");
            position++;
        }

        LimboQueuePlayer connection = queueData.getQueue().peek();
        if (connection == null) return;

        if (connection.getConnectTime() > configuration.QUEUE_CONNECTION_TIME + 5)
            connection.connect();
    }

    private void sendMessage(LimboQueuePlayer player, List<String> string) {
        ChatUtil.sendActionBar(player.getPlayer(), String.join("\n", string));
    }

    private void sendMessage(LimboQueuePlayer player, List<String> string, String from, String to) {
        ChatUtil.sendActionBar(player.getPlayer(), String.join("\n", string).replace(from, to));
    }
}
