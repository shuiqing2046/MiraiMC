package me.dreamvoid.miraimc.bukkit.event;

import net.mamoe.mirai.event.events.GroupAllowMemberInviteEvent;

/**
 * 群设置 - 允许群员邀请好友加群状态改变
 * @deprecated
 * @see me.dreamvoid.miraimc.bukkit.event.group.setting.MiraiGroupAllowMemberInviteEvent
 */
@Deprecated
public class MiraiGroupAllowMemberInviteEvent extends me.dreamvoid.miraimc.bukkit.event.group.setting.MiraiGroupAllowMemberInviteEvent {
    public MiraiGroupAllowMemberInviteEvent(GroupAllowMemberInviteEvent event) {
        super(event);
    }
}
