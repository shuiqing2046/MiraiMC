package me.dreamvoid.miraimc.nukkit.event;

import net.mamoe.mirai.event.events.NewFriendRequestEvent;

/**
 * 一个账号请求添加机器人为好友
 * @deprecated
 * @see me.dreamvoid.miraimc.nukkit.event.friend.MiraiNewFriendRequestEvent
 */
@Deprecated
public class MiraiNewFriendRequestEvent extends me.dreamvoid.miraimc.nukkit.event.friend.MiraiNewFriendRequestEvent {
    public MiraiNewFriendRequestEvent(NewFriendRequestEvent event) {
        super(event);
    }
}
