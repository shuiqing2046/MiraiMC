package me.dreamvoid.miraimc.sponge.event;

import me.dreamvoid.miraimc.api.bot.group.MiraiNormalMember;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

import java.util.Objects;

/**
 * 被动收到消息 - 群临时会话消息
 */
public class MiraiGroupTempMessageEvent extends AbstractEvent {
    private final Cause cause;

    public MiraiGroupTempMessageEvent(GroupTempMessageEvent event, Cause cause) {
        this.event = event;
        this.cause = cause;
    }
    private final GroupTempMessageEvent event;

    /**
     * 返回接收到这条信息的机器人ID
     * @return 机器人ID
     */
    public long getBotID(){
        return event.getBot().getId();
    }

    /**
     * 返回接收到这条信息的群号
     * @return 群号
     */
    public long getGroupID(){
        return event.getGroup().getId();
    }

    /**
     * 返回接收到这条信息的群名称
     * @return 群名称
     */
    public String getGroupName(){
        return event.getGroup().getName();
    }

    /**
     * 返回发送这条信息的发送者ID
     * @return 发送者ID
     */
    public long getSenderID(){
        return event.getSender().getId();
    }

    /**
     * 返回发送这条信息的发送者群名片
     * @return 发送者群名片
     */
    public String getSenderNameCard(){
        return event.getSender().getNameCard();
    }

    /**
     * 返回发送这条信息的发送者昵称
     * @return 发送者昵称
     */
    public String getSenderNick(){
        return event.getSender().getNick();
    }

    /**
     * 返回接收到的消息内容转换到字符串的结果<br>
     * 此方法使用 contentToString()<br>
     * QQ 对话框中以纯文本方式会显示的消息内容，这适用于MC与QQ的消息互通等不方便展示原始内容的场景。<br>
     * 无法用纯文字表示的消息会丢失信息，如任何图片都是 [图片]
     * @return 转换字符串后的消息内容
     */
    public String getMessage(){
        return event.getMessage().contentToString();
    }

    /**
     * 返回接收到的消息内容转换到字符串的结果<br>
     * 此方法使用 contentToString()<br>
     * QQ 对话框中以纯文本方式会显示的消息内容，这适用于MC与QQ的消息互通等不方便展示原始内容的场景。<br>
     * 无法用纯文字表示的消息会丢失信息，如任何图片都是 [图片]
     * @return 转换字符串后的消息内容
     * @see #getMessage() 
     * @deprecated 
     */
    @Deprecated
    public String getMessageContent(){
        return event.getMessage().contentToString();
    }

    /**
     * 返回接收到的消息内容<br>
     * 此方法使用 toString()<br>
     * Java 对象的 toString()，会尽可能包含多的信息用于调试作用，行为可能不确定<br>
     * 如需处理常规消息内容，请使用 {@link #getMessageContent()}
     * @return 原始消息内容
     */
    public String getMessageToString(){
        return event.getMessage().toString();
    }

    /**
     * 返回接收到的消息内容转换到Mirai Code的结果<br>
     * 此方法使用 serializeToMiraiCode()<br>
     * 转换为对应的 Mirai 码，消息的一种序列化方式
     * @return 带Mirai Code的消息内容
     */
    public String getMessageToMiraiCode(){
        return event.getMessage().serializeToMiraiCode();
    }

    /**
     * 返回接收到这条信息的时间
     * @return 发送时间
     */
    public int getTime(){
        return event.getTime();
    }

    /**
     * 获取原始事件内容<br>
     * [!] 不推荐使用
     * @return 原始事件内容
     */
    public String eventToString() {
        return event.toString();
    }

    @Override
    public @NotNull Cause getCause() {
        return cause;
    }

    /**
     * 回复这条消息（支持 Mirai Code）
     * @param message 消息内容
     */
    public void reply(String message) {
        event.getSender().sendMessage(new MessageChainBuilder()
                .append(new QuoteReply(event.getMessage()))
                .append(MiraiCode.deserializeMiraiCode(message))
                .build()
        );
    }

    /**
     * 向发送来源发送消息（支持 Mirai Code）
     * @param message 消息内容
     */
    public void sendMessage(String message) {
        event.getSender().sendMessage(MiraiCode.deserializeMiraiCode(message));
    }

    /**
     * 返回被回复的消息的发送者
     * @return QQ号
     */
    public long getQuoteReplySenderID() {
        QuoteReply quoteReply = event.getMessage().get(QuoteReply.Key);
        return !Objects.isNull(quoteReply) ? quoteReply.getSource().getFromId() : 0;
    }

    /**
     * 返回被回复的消息内容转换到字符串的结果，如果不存在回复消息，返回null<br>
     * 此方法使用 contentToString()<br>
     * QQ 对话框中以纯文本方式会显示的消息内容，这适用于MC与QQ的消息互通等不方便展示原始内容的场景。<br>
     * 无法用纯文字表示的消息会丢失信息，如任何图片都是 [图片]
     * @return 被回复的转换字符串后的消息内容
     */
    @Nullable
    public String getQuoteReplyMessage() {
        QuoteReply quoteReply = event.getMessage().get(QuoteReply.Key);
        return !Objects.isNull(quoteReply) ? quoteReply.getSource().getOriginalMessage().contentToString() : null;
    }

    /**
     * 返回被回复的消息内容转换到字符串的结果，如果不存在回复消息，返回null<br>
     * 此方法使用 toString()<br>
     * Java 对象的 toString()，会尽可能包含多的信息用于调试作用，行为可能不确定<br>
     * 如需处理常规消息内容，请使用 {@link #getQuoteReplyMessage()}
     * @return 原始消息内容
     */
    @Nullable
    public String getQuoteReplyMessageToString() {
        QuoteReply quoteReply = event.getMessage().get(QuoteReply.Key);
        return !Objects.isNull(quoteReply) ? quoteReply.getSource().getOriginalMessage().toString() : null;
    }

    /**
     * 返回被回复的消息内容转换到字符串的结果，如果不存在回复消息，返回null<br>
     * 此方法使用 contentToString()<br>
     * QQ 对话框中以纯文本方式会显示的消息内容，这适用于MC与QQ的消息互通等不方便展示原始内容的场景。<br>
     * 无法用纯文字表示的消息会丢失信息，如任何图片都是 [图片]
     * @return 被回复的转换字符串后的消息内容
     * @see #getQuoteReplyMessage()
     */
    @Nullable
    public String getQuoteReplyMessageContent() {
        QuoteReply quoteReply = event.getMessage().get(QuoteReply.Key);
        return !Objects.isNull(quoteReply) ? quoteReply.getSource().getOriginalMessage().toString() : null;
    }

    /**
     * 返回被回复的消息内容转换到字符串的结果，如果不存在回复消息，返回null<br>
     * 此方法使用 serializeToMiraiCode()<br>
     * 转换为对应的 Mirai 码，消息的一种序列化方式
     * @return 带Mirai Code的消息内容
     */
    @Nullable
    public String getQuoteReplyMessageToMiraiCode() {
        QuoteReply quoteReply = event.getMessage().get(QuoteReply.Key);
        return !Objects.isNull(quoteReply) ? quoteReply.getSource().getOriginalMessage().serializeToMiraiCode() : null;
    }

    /**
     * 从临时会话获取群员实例
     * @return MiraiNormalMember 实例
     */
    public MiraiNormalMember getMember(){
        return new MiraiNormalMember(event.getGroup(), event.getSender().getId());
    }
}
