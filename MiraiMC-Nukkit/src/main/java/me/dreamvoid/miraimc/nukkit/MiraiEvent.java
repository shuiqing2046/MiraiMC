package me.dreamvoid.miraimc.nukkit;

import me.dreamvoid.miraimc.IMiraiEvent;
import me.dreamvoid.miraimc.internal.Utils;
import me.dreamvoid.miraimc.nukkit.event.bot.*;
import me.dreamvoid.miraimc.nukkit.event.friend.*;
import me.dreamvoid.miraimc.nukkit.event.group.*;
import me.dreamvoid.miraimc.nukkit.event.group.member.*;
import me.dreamvoid.miraimc.nukkit.event.group.setting.*;
import me.dreamvoid.miraimc.nukkit.event.message.MiraiBeforeImageUploadEvent;
import me.dreamvoid.miraimc.nukkit.event.message.MiraiImageUploadEvent;
import me.dreamvoid.miraimc.nukkit.event.message.MiraiNudgeEvent;
import me.dreamvoid.miraimc.nukkit.event.message.passive.*;
import me.dreamvoid.miraimc.nukkit.event.message.postsend.MiraiFriendMessagePostSendEvent;
import me.dreamvoid.miraimc.nukkit.event.message.postsend.MiraiGroupMessagePostSendEvent;
import me.dreamvoid.miraimc.nukkit.event.message.postsend.MiraiGroupTempMessagePostSendEvent;
import me.dreamvoid.miraimc.nukkit.event.message.postsend.MiraiStrangerMessagePostSendEvent;
import me.dreamvoid.miraimc.nukkit.event.message.presend.MiraiFriendMessagePreSendEvent;
import me.dreamvoid.miraimc.nukkit.event.message.presend.MiraiGroupMessagePreSendEvent;
import me.dreamvoid.miraimc.nukkit.event.message.presend.MiraiGroupTempMessagePreSendEvent;
import me.dreamvoid.miraimc.nukkit.event.message.presend.MiraiStrangerMessagePreSendEvent;
import me.dreamvoid.miraimc.nukkit.event.message.recall.MiraiFriendMessageRecallEvent;
import me.dreamvoid.miraimc.nukkit.event.message.recall.MiraiGroupMessageRecallEvent;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.*;

import static me.dreamvoid.miraimc.nukkit.event.bot.MiraiBotOfflineEvent.Type.*;

public class MiraiEvent implements IMiraiEvent {
    private final NukkitPlugin plugin;

    public MiraiEvent(NukkitPlugin plugin){
        Thread.currentThread().setContextClassLoader(Utils.getClassLoader());
        this.plugin = plugin;
    }

    private Listener<BotOnlineEvent> BotOnlineListener;
    private Listener<BotOfflineEvent.Active> BotOfflineActiveListener;
    private Listener<BotOfflineEvent.Force> BotOfflineForceListener;
    private Listener<BotOfflineEvent.Dropped> BotOfflineDroppedListener;
    private Listener<BotOfflineEvent.RequireReconnect> BotOfflineRequireReconnectListener;
    private Listener<BotReloginEvent> BotReloginEventListener;
    private Listener<BotAvatarChangedEvent> BotAvatarChangedEventListener;
    private Listener<BotNickChangedEvent> BotNickChangedEventListener;

    private Listener<GroupMessageEvent> GroupMessageListener;
    private Listener<FriendMessageEvent> FriendMessageListener;
    private Listener<GroupTempMessageEvent> GroupTempMessageEventListener;
    private Listener<StrangerMessageEvent> StrangerMessageEventListener;
    private Listener<OtherClientMessageEvent> OtherClientMessageEventListener;

    private Listener<GroupMessagePreSendEvent> GroupMessagePreSendEventListener;
    private Listener<FriendMessagePreSendEvent> FriendMessagePreSendEventListener;
    private Listener<GroupTempMessagePreSendEvent> GroupTempMessagePreSendEventListener;
    private Listener<StrangerMessagePreSendEvent> StrangerMessagePreSendEventListener;

    private Listener<GroupMessagePostSendEvent> GroupMessagePostSendEventListener;
    private Listener<FriendMessagePostSendEvent> FriendMessagePostSendEventListener;
    private Listener<GroupTempMessagePostSendEvent> GroupTempMessagePostSendEventListener;
    private Listener<StrangerMessagePostSendEvent> StrangerMessagePostSendEventListener;

    private Listener<MessageRecallEvent.FriendRecall> FriendMessageRecallEventListener;
    private Listener<MessageRecallEvent.GroupRecall> GroupMessageRecallEventListener;

    private Listener<BeforeImageUploadEvent> BeforeImageUploadEventListener;
    private Listener<ImageUploadEvent.Succeed> ImageUploadSucceedEventListener;
    private Listener<ImageUploadEvent.Failed> ImageUploadFailedEventListener;
    private Listener<NudgeEvent> NudgeEventListener;

    private Listener<BotLeaveEvent.Active> BotLeaveActiveEventListener;
    private Listener<BotLeaveEvent.Kick> BotLeaveKickEventListener;
    private Listener<BotGroupPermissionChangeEvent> BotGroupPermissionChangeEventListener;
    private Listener<BotMuteEvent> BotMuteEventListener;
    private Listener<BotUnmuteEvent> BotUnmuteEventListener;
    private Listener<BotJoinGroupEvent> BotJoinGroupEventListener;

    private Listener<GroupNameChangeEvent> GroupNameChangeEventListener;
    @Deprecated
    private Listener<GroupEntranceAnnouncementChangeEvent> GroupEntranceAnnouncementChangeEventListener;
    private Listener<GroupMuteAllEvent> GroupMuteAllEventListener;
    private Listener<GroupAllowAnonymousChatEvent> GroupAllowAnonymousChatEventListener;
    private Listener<GroupAllowMemberInviteEvent> GroupAllowMemberInviteEventListener;

    private Listener<MemberJoinEvent.Invite> MemberJoinInviteEventListener;
    private Listener<MemberJoinEvent.Active> MemberJoinActiveEventListener;
    private Listener<MemberLeaveEvent.Kick> MemberLeaveKickEventListener;
    private Listener<MemberLeaveEvent.Quit> MemberLeaveQuitEventListener;
    private Listener<MemberJoinRequestEvent> MemberJoinRequestEventListener;
    private Listener<BotInvitedJoinGroupRequestEvent> BotInvitedJoinGroupRequestEventListener;

    private Listener<MemberCardChangeEvent> MemberCardChangeEventListener;
    private Listener<MemberSpecialTitleChangeEvent> MemberSpecialTitleChangeEventListener;

    private Listener<MemberPermissionChangeEvent> MemberPermissionChangeEventListener;

    private Listener<MemberMuteEvent> MemberMuteEventListener;
    private Listener<MemberUnmuteEvent> MemberUnmuteEventListener;

    private Listener<SignEvent> SignEventListener;

    private Listener<FriendRemarkChangeEvent> FriendRemarkChangeEventListener;
    private Listener<FriendAddEvent> FriendAddEventListener;
    private Listener<FriendDeleteEvent> FriendDeleteEventListener;
    private Listener<NewFriendRequestEvent> NewFriendRequestEventListener;
    private Listener<FriendAvatarChangedEvent> FriendAvatarChangedEventListener;
    private Listener<FriendNickChangedEvent> FriendNickChangedEventListener;
    private Listener<FriendInputStatusChangedEvent> FriendInputStatusChangedEventListener;

    @Override
    public void startListenEvent(){
        // Bot
        BotOnlineListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotOnlineEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotOnlineEvent(event)));
        BotOfflineActiveListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotOfflineEvent.Active.class,event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotOfflineEvent(event, Active)));
        BotOfflineForceListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotOfflineEvent.Force.class,event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotOfflineEvent(event, Force)));
        BotOfflineDroppedListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotOfflineEvent.Dropped.class,event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotOfflineEvent(event, Dropped)));
        BotOfflineRequireReconnectListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotOfflineEvent.RequireReconnect.class,event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotOfflineEvent(event, RequireReconnect)));
        BotReloginEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotReloginEvent.class,event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotReloginEvent(event)));
        BotAvatarChangedEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotAvatarChangedEvent.class,event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotAvatarChangedEvent(event)));
        BotNickChangedEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotNickChangedEvent.class,event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotNickChangedEvent(event)));

        // 消息
        // - 被动
        GroupMessageListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiGroupMessageEvent(event)));
        FriendMessageListener = GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessageEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiFriendMessageEvent(event)));
        GroupTempMessageEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupTempMessageEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiGroupTempMessageEvent(event)));
        StrangerMessageEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(StrangerMessageEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiStrangerMessageEvent(event)));
        OtherClientMessageEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(OtherClientMessageEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiOtherClientMessageEvent(event)));
        // - 主动前
        GroupMessagePreSendEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessagePreSendEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiGroupMessagePreSendEvent(event)));
        FriendMessagePreSendEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessagePreSendEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiFriendMessagePreSendEvent(event)));
        GroupTempMessagePreSendEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupTempMessagePreSendEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiGroupTempMessagePreSendEvent(event)));
        StrangerMessagePreSendEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(StrangerMessagePreSendEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiStrangerMessagePreSendEvent(event)));
        // - 主动后
        GroupMessagePostSendEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessagePostSendEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiGroupMessagePostSendEvent(event)));
        FriendMessagePostSendEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessagePostSendEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiFriendMessagePostSendEvent(event)));
        GroupTempMessagePostSendEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupTempMessagePostSendEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiGroupTempMessagePostSendEvent(event)));
        StrangerMessagePostSendEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(StrangerMessagePostSendEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiStrangerMessagePostSendEvent(event)));
        // - 撤回
        FriendMessageRecallEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(MessageRecallEvent.FriendRecall.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiFriendMessageRecallEvent(event)));
        GroupMessageRecallEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(MessageRecallEvent.GroupRecall.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiGroupMessageRecallEvent(event)));
        // - 图片上传
        // -- 图片上传前
        BeforeImageUploadEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(BeforeImageUploadEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiBeforeImageUploadEvent(event)));
        // -- 图片上传完成
        ImageUploadSucceedEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(ImageUploadEvent.Succeed.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiImageUploadEvent.Succeed(event)));
        ImageUploadFailedEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(ImageUploadEvent.Failed.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiImageUploadEvent.Failed(event)));
        // - 戳一戳
        NudgeEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(NudgeEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiNudgeEvent(event)));

        // 群
        BotLeaveActiveEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotLeaveEvent.Active.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotLeaveEvent.Active(event)));
        BotLeaveKickEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotLeaveEvent.Kick.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotLeaveEvent.Kick(event)));
        BotGroupPermissionChangeEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotGroupPermissionChangeEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotGroupPermissionChangeEvent(event)));
        BotMuteEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotMuteEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotMuteEvent(event)));
        BotUnmuteEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotUnmuteEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotUnmuteEvent(event)));
        BotJoinGroupEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotJoinGroupEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotJoinGroupEvent(event)));
        // - 群设置
        GroupNameChangeEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupNameChangeEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiGroupNameChangeEvent(event)));
        GroupEntranceAnnouncementChangeEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupEntranceAnnouncementChangeEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiGroupEntranceAnnouncementChangeEvent(event)));
        GroupMuteAllEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMuteAllEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiGroupMuteAllEvent(event)));
        GroupAllowAnonymousChatEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupAllowAnonymousChatEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiGroupAllowAnonymousChatEvent(event)));
        GroupAllowMemberInviteEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupAllowMemberInviteEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiGroupAllowMemberInviteEvent(event)));
        // - 群成员
        // -- 成员列表变更
        MemberJoinInviteEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinEvent.Invite.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiMemberJoinEvent.Invite(event)));
        MemberJoinActiveEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinEvent.Active.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiMemberJoinEvent.Active(event)));
        MemberLeaveKickEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(MemberLeaveEvent.Kick.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiMemberLeaveEvent.Kick(event)));
        MemberLeaveQuitEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(MemberLeaveEvent.Quit.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiMemberLeaveEvent.Quit(event)));
        MemberJoinRequestEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinRequestEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiMemberJoinRequestEvent(event)));
        BotInvitedJoinGroupRequestEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotInvitedJoinGroupRequestEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiBotInvitedJoinGroupRequestEvent(event)));
        // -- 名片和头衔
        MemberCardChangeEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(MemberCardChangeEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiMemberCardChangeEvent(event)));
        MemberSpecialTitleChangeEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(MemberSpecialTitleChangeEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiMemberSpecialTitleChangeEvent(event)));
        // -- 成员权限
        MemberPermissionChangeEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(MemberPermissionChangeEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiMemberPermissionChangeEvent(event)));
        // -- 动作
        MemberMuteEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(MemberMuteEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiMemberMuteEvent(event)));
        MemberUnmuteEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(MemberUnmuteEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiMemberUnmuteEvent(event)));
        // - 打卡事件
        SignEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(SignEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiSignEvent(event)));

        // 好友
        FriendRemarkChangeEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(FriendRemarkChangeEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiFriendRemarkChangeEvent(event)));
        FriendAddEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(FriendAddEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiFriendAddEvent(event)));
        FriendDeleteEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(FriendDeleteEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiFriendDeleteEvent(event)));
        NewFriendRequestEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(NewFriendRequestEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiNewFriendRequestEvent(event)));
        FriendAvatarChangedEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(FriendAvatarChangedEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiFriendAvatarChangedEvent(event)));
        FriendNickChangedEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(FriendNickChangedEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiFriendNickChangedEvent(event)));
        FriendInputStatusChangedEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(FriendInputStatusChangedEvent.class, event -> plugin.getServer().getPluginManager().callEvent(new MiraiFriendInputStatusChangedEvent(event)));
    }

    @Override
    public void stopListenEvent(){
        BotOnlineListener.complete();
        BotOfflineActiveListener.complete();
        BotOfflineForceListener.complete();
        BotOfflineDroppedListener.complete();
        BotOfflineRequireReconnectListener.complete();
        BotReloginEventListener.complete();
        BotAvatarChangedEventListener.complete();
        BotNickChangedEventListener.complete();

        GroupMessageListener.complete();
        FriendMessageListener.complete();
        GroupTempMessageEventListener.complete();
        StrangerMessageEventListener.complete();
        OtherClientMessageEventListener.complete();

        GroupMessagePreSendEventListener.complete();
        FriendMessagePreSendEventListener.complete();
        GroupTempMessagePreSendEventListener.complete();
        StrangerMessagePreSendEventListener.complete();

        GroupMessagePostSendEventListener.complete();
        FriendMessagePostSendEventListener.complete();
        GroupTempMessagePostSendEventListener.complete();
        StrangerMessagePostSendEventListener.complete();

        FriendMessageRecallEventListener.complete();
        GroupMessageRecallEventListener.complete();

        BeforeImageUploadEventListener.complete();
        ImageUploadSucceedEventListener.complete();
        ImageUploadFailedEventListener.complete();

        NudgeEventListener.complete();

        BotLeaveActiveEventListener.complete();
        BotLeaveKickEventListener.complete();
        BotGroupPermissionChangeEventListener.complete();
        BotMuteEventListener.complete();
        BotUnmuteEventListener.complete();
        BotJoinGroupEventListener.complete();

        GroupNameChangeEventListener.complete();
        GroupEntranceAnnouncementChangeEventListener.complete();
        GroupMuteAllEventListener.complete();
        GroupAllowAnonymousChatEventListener.complete();
        GroupAllowMemberInviteEventListener.complete();

        MemberJoinInviteEventListener.complete();
        MemberJoinActiveEventListener.complete();
        MemberLeaveKickEventListener.complete();
        MemberLeaveQuitEventListener.complete();
        MemberJoinRequestEventListener.complete();
        BotInvitedJoinGroupRequestEventListener.complete();

        MemberCardChangeEventListener.complete();
        MemberSpecialTitleChangeEventListener.complete();

        MemberPermissionChangeEventListener.complete();

        MemberMuteEventListener.complete();
        MemberUnmuteEventListener.complete();

        SignEventListener.complete();

        FriendRemarkChangeEventListener.complete();
        FriendAddEventListener.complete();
        FriendDeleteEventListener.complete();
        NewFriendRequestEventListener.complete();
        FriendAvatarChangedEventListener.complete();
        FriendNickChangedEventListener.complete();
        FriendInputStatusChangedEventListener.complete();
    }
}
