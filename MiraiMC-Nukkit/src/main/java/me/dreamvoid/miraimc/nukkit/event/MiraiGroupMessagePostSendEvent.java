package me.dreamvoid.miraimc.nukkit.event;

import net.mamoe.mirai.event.events.GroupMessagePostSendEvent;

/**
 * 主动发送消息后 - 群消息
 * @deprecated
 * @see me.dreamvoid.miraimc.nukkit.event.message.postsend.MiraiGroupMessagePostSendEvent
 */
@Deprecated
public class MiraiGroupMessagePostSendEvent extends me.dreamvoid.miraimc.nukkit.event.message.postsend.MiraiGroupMessagePostSendEvent {
    public MiraiGroupMessagePostSendEvent(GroupMessagePostSendEvent event) {
        super(event);
    }
}
