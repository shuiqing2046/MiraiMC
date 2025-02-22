package me.dreamvoid.miraimc;

import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.internal.*;
import me.dreamvoid.miraimc.internal.database.DatabaseManager;
import me.dreamvoid.miraimc.internal.database.MySQL;
import me.dreamvoid.miraimc.internal.database.SQLite;
import me.dreamvoid.miraimc.internal.webapi.Info;
import net.mamoe.mirai.utils.BotConfiguration;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class MiraiMCPlugin {
    public static MiraiMCPlugin INSTANCE;
    private static Platform platform;
    private Logger logger;

    public MiraiMCPlugin(Platform plugin){
        INSTANCE = this;
        platform = plugin;
    }

    public static Platform getPlatform() {
        return platform;
    }

    /**
     * 此方法应在插件实例化时调用，用于设置必要的运行环境，此时配置尚未初始化。
     * @param logger Logger 实例。由于各平台初始化 Logger 的时机不一，因此需要一个 Logger 来辅助。
     */
    public void startUp(Logger logger) {
        logger.info("Preparing MiraiMC start-up.");

        System.setProperty("mirai.no-desktop", "MiraiMC");
        System.setProperty("mirai.slider.captcha.supported", "MiraiMC");

        Utils.setLogger(logger);
        Utils.setClassLoader(this.getClass().getClassLoader());

        logger.info("Start-up tasks finished.");
    }

    /**
     * 此方法应在插件各项准备工作均已完成时调用。此时插件已经准备就绪，可以开始初始化配置文件，加载 mirai 核心。
     */
    public void preLoad() throws IOException, ParserConfigurationException, SAXException {
        logger = platform.getPluginLogger();
        Utils.setLogger(logger);
        Utils.setClassLoader(platform.getPluginClassLoader());

        logger.info("Preparing MiraiMC pre-load.");

        // 加载配置
        logger.info("Loading config.");
        platform.getPluginConfig().loadConfig();
        if(platform.getPluginVersion().contains("dev-") && MiraiMCConfig.General.MiraiCoreVersion.equalsIgnoreCase("stable")) {
            MiraiMCConfig.General.MiraiCoreVersion = "latest"; // Fix dev version
        }

        logger.info("Mirai working dir: " + MiraiMCConfig.General.MiraiWorkingDir);

        // 加载 mirai 核心
        logger.info("Selected mirai core version: " + MiraiMCConfig.General.MiraiCoreVersion);
        if (MiraiMCConfig.General.MiraiCoreVersion.equalsIgnoreCase("latest")) {
            MiraiLoader.loadMiraiCore();
        } else if (MiraiMCConfig.General.MiraiCoreVersion.equalsIgnoreCase("stable")) {
            MiraiLoader.loadMiraiCore(MiraiLoader.getStableVersion(getPlatform().getPluginVersion()));
        } else {
            MiraiLoader.loadMiraiCore(MiraiMCConfig.General.MiraiCoreVersion);
        }

        // 加载 EncryptService
        if(MiraiMCConfig.Bot.RegisterEncryptService){
            logger.info("Registering Mirai Encrypt Service.");
            try{
                MiraiEncryptServiceFactory.install();
            } catch (NoClassDefFoundError error){
                logger.severe("Failed to register encrypt service, please use mirai 2.15.0-dev-105 or later.");
            } catch (Throwable cause){
                logger.severe("Failed to register encrypt service: " + cause);
            }
        }

        // 加载来自 cssxsh 的 fix-protocol-version
        if(MiraiMCConfig.Bot.UpdateProtocolVersion){
            logger.info("Updating mirai protocol version. (Author: cssxsh)");

            logger.info("协议版本检查更新...");
            try {
                FixProtocolVersion.update();
                for (BotConfiguration.MiraiProtocol protocol : BotConfiguration.MiraiProtocol.values()) {
                    File file = new File(new File(MiraiMCConfig.PluginDir, "protocol"), protocol.name().toLowerCase() + ".json");
                    if (file.exists()) {
                        logger.info(protocol + " load from " + file.toPath().toUri());
                        FixProtocolVersion.load(protocol);
                    }
                }
            } catch (Throwable cause){
                logger.severe("协议版本升级失败: " + cause);
            }

            logger.info("当前各登录协议版本日期: ");
            FixProtocolVersion.info().values().forEach(s -> logger.info(s));
        }

        logger.info("Pre-load tasks finished.");
    }

    /**
     * 此方法应在 mirai 核心加载完毕、服务端准备就绪后调用。<br>
     * 此方法调用完成后，可以继续执行HTTP API监听等未完成的任务。
     */
    public void postLoad() {
        logger.info("Preparing MiraiMC post-load.");

        // 数据库
        try {
            switch (MiraiMCConfig.Database.Type.toLowerCase()){
                case "sqlite":
                default: {
                    logger.info("Initializing SQLite database.");
                    DatabaseManager.setDatabase(new SQLite());
                    break;
                }
                case "mysql": {
                    logger.info("Initializing MySQL database.");
                    DatabaseManager.setDatabase(new MySQL());
                    break;
                }
            }
            DatabaseManager.getDatabase().initialize();
        } catch (ClassNotFoundException e) {
            logger.warning("Failed to initialize database, reason: " + e);
        }

        // 接入 mirai 事件
        logger.info("Starting Mirai-Event listener.");
        platform.getMiraiEvent().startListenEvent();

        // 自动登录机器人
        logger.info("Starting Auto-Login bot.");
        platform.getAutoLogin().loadFile();
        platform.getAutoLogin().doStartUpAutoLogin();

        // 安全警告
        if(!(MiraiMCConfig.General.DisableSafeWarningMessage)){
            logger.warning("确保您正在使用开源的 MiraiMC 插件，未知来源的插件可能会盗取您的账号！");
            logger.warning("请始终从 GitHub 或作者指定的其他途径下载插件: https://github.com/DreamVoid/MiraiMC");
        }

        // 公告板
        platform.runTaskLaterAsync(() -> {
            try {
                List<String> announcement = Info.init().announcement;
                if(announcement != null && announcement.size() != 0){
                    logger.info("========== [ MiraiMC 公告版 ] ==========");
                    announcement.forEach(logger::info);
                    logger.info("=======================================");
                }
            } catch (IOException ignored) {}
        }, 40);

        // 检查更新
        if(MiraiMCConfig.General.CheckUpdate && !platform.getPluginVersion().contains("dev")){
            platform.runTaskAsync(() -> {
                logger.info("正在检查更新...");
                try {
                    PluginUpdate fetch = new PluginUpdate();
                    String version = !platform.getPluginVersion().contains("-") ? fetch.getLatestRelease() : fetch.getLatestPreRelease();
                    if (fetch.isBlocked(platform.getPluginVersion())) {
                        logger.severe("当前版本已停用，继续使用将不会得到作者的任何支持！");
                        logger.severe("请立刻更新到最新版本: " + version);
                        logger.severe("从 GitHub 下载更新: https://github.com/DreamVoid/MiraiMC/releases");
                    } else if (!platform.getPluginVersion().equals(version)) {
                        logger.info("已找到新的插件更新，最新版本: " + version);
                        logger.info("从 GitHub 下载更新: https://github.com/DreamVoid/MiraiMC/releases");
                    } else {
                        logger.info("你使用的是最新版本的 MiraiMC！");
                    }
                } catch (IOException e) {
                    logger.warning("An error occurred while fetching the latest version, reason: " + e);
                }
            });
        }

        logger.info("Some initialization tasks will continue to run afterwards.");
        logger.info("Post-load tasks finished. Welcome to use MiraiMC!");
    }

    public void unload() {
        logger.info("Preparing MiraiMC unload.");

        // 取消所有的待验证机器人和已登录机器人进程
        logger.info("Closing all bots");
        MiraiLoginSolver.cancelAll();
        for (long bots : MiraiBot.getOnlineBots()){
            MiraiBot.getBot(bots).close();
        }

        // 停止事件监听
        logger.info("Stopping bot event listener.");
        platform.getMiraiEvent().stopListenEvent();

        // 停止数据库
        logger.info("Closing database.");
        DatabaseManager.getDatabase().close();

        logger.info("Unload tasks finished. Thanks for use MiraiMC!");
    }
}
