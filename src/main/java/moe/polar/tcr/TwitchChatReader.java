package moe.polar.tcr;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class TwitchChatReader implements ClientModInitializer {
    @NotNull
    public static final Logger LOGGER = LoggerFactory.getLogger("twitchchatreader");

    @NotNull
    public static final Executor TCR_EXECUTOR = Executors.newSingleThreadExecutor();

    @Nullable
    public static BetterTwirk TWIRK = null;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            final LiteralCommandNode<FabricClientCommandSource> twitchNode = ClientCommandManager
                    .literal("twitch")
                    .build();

            final LiteralCommandNode<FabricClientCommandSource> connectNode = ClientCommandManager
                    .literal("connect")
                    .then(argument("channel", word()).executes(TwitchCommand::connect))
                    .build();

            final LiteralCommandNode<FabricClientCommandSource> disconnectNode = ClientCommandManager
                    .literal("disconnect")
                    .executes(TwitchCommand::disconnect)
                    .build();

            dispatcher.getRoot().addChild(twitchNode);
            twitchNode.addChild(connectNode);
            twitchNode.addChild(disconnectNode);
        });
    }
}