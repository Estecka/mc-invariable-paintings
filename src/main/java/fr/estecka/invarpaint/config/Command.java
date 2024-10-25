package fr.estecka.invarpaint.config;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import fr.estecka.invarpaint.InvarpaintMod;
import fr.estecka.invarpaint.config.ConfigIO.Property;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;
import static fr.estecka.invarpaint.InvarpaintMod.CONFIG;
import static fr.estecka.invarpaint.InvarpaintMod.IO;

public class Command
{
	static public final Identifier ID = Identifier.of(InvarpaintMod.MODID, "command");

	static private final String ROOT_COMMAND = InvarpaintMod.MODID;
	static private final String PROP_ARG = "property";
	static private final String VALUE_ARG = "value";

	static public void	Register(){
		CommandRegistrationCallback.EVENT.register(ID, Command::RegisterWith);
	}

	static public void	RegisterWith(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, RegistrationEnvironment env){
		final var root = literal(ROOT_COMMAND);
		final var config = literal("config");

		config.then(argument(PROP_ARG, string())
			.suggests(Command::PropertyName)
			.executes(Command::Get)
			.then(argument(VALUE_ARG, greedyString())
				.executes(Command::Set)
			)
		);

		root.then(config);
		root.requires(s -> s.hasPermissionLevel(3));
		dispatcher.register(root);
	}


	static private CompletableFuture<Suggestions> PropertyName(final CommandContext<ServerCommandSource> context, final SuggestionsBuilder builder){
		for (String s : CONFIG.GetProperties().keySet())
			builder.suggest(s);
		return builder.buildFuture();
	}

	static private int Get(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		String name = getString(context, PROP_ARG);

		Property<?> property = CONFIG.GetProperties().get(name);
		if (property == null){
			context.getSource().sendError(Text.literal("No such property"));
			return 0;
		}

		String value;
		try {
			value = property.Encode();
		}
		catch (IllegalArgumentException e) {
			context.getSource().sendError(Text.literal(e.toString()));
			return -1;
		}

		context.getSource().sendMessage(Text.literal(name+"="+value));
		return 0;
	}

	static private int Set(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		String name = getString(context, PROP_ARG);
		String value = getString(context, VALUE_ARG);

		Property<?> property = CONFIG.GetProperties().get(name);
		if (property == null){
			context.getSource().sendError(Text.literal("No such property"));
			return 0;
		}

		try {
			property.Decode(value);
			IO.Write(CONFIG);
		}
		catch (IllegalArgumentException|IOException e) {
			context.getSource().sendError(Text.literal(e.toString()));
			return -1;
		}

		Get(context);
		return 1;
	}
}
