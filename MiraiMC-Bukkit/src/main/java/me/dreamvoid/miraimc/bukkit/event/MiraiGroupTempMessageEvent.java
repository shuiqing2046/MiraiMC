package me.dreamvoid.miraimc.bukkit.event;

import net.mamoe.mirai.event.events.GroupTempMessageEvent;

/**
 * 被动收到消息 - 群临时会话消息
 * @deprecated
 * @see me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupTempMessageEvent
 */
@Deprecated
public class MiraiGroupTempMessageEvent extends me.dreamvoid.miraimc.bukkit.event.message.passive.MiraiGroupTempMessageEvent {
    public MiraiGroupTempMessageEvent(GroupTempMessageEvent event) {
        super(event);
    }
}
