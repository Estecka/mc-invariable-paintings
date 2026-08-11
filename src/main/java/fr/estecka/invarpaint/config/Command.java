package fr.estecka.invarpaint.config;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import fr.estecka.invarpaint.InvarpaintMod;
import fr.estecka.invarpaint.config.ConfigIO.Property;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;
import static fr.estecka.invarpaint.InvarpaintMod.CONFIG;
import static fr.estecka.invarpaint.InvarpaintMod.IO;

public class Command
{
	static public final Identifier ID = Identifier.fromNamespaceAndPath(InvarpaintMod.MODID, "command");

	static private final String ROOT_COMMAND = InvarpaintMod.MODID;
	static private final String PROP_ARG = "property";
	static private final String VALUE_ARG = "value";

	static public void	Register(){
		CommandRegistrationCallback.EVENT.register(ID, Command::RegisterWith);
	}

	static public void RegisterWith(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, CommandSelection env){
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
		root.requires(s -> s.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.ADMINS)));
		dispatcher.register(root);
	}


	static private CompletableFuture<Suggestions> PropertyName(final CommandContext<CommandSourceStack> context, final SuggestionsBuilder builder){
		for (String s : CONFIG.GetProperties().keySet())
			builder.suggest(s);
		return builder.buildFuture();
	}

	static private int Get(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		String name = getString(context, PROP_ARG);

		Property<?> property = CONFIG.GetProperties().get(name);
		if (property == null){
			context.getSource().sendFailure(Component.literal("No such property"));
			return 0;
		}

		String value;
		try {
			value = property.Encode();
		}
		catch (IllegalArgumentException e) {
			context.getSource().sendFailure(Component.literal(e.toString()));
			return -1;
		}

		context.getSource().sendSystemMessage(Component.literal(name+"="+value));
		return 0;
	}

	static private int Set(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		String name = getString(context, PROP_ARG);
		String value = getString(context, VALUE_ARG);

		Property<?> property = CONFIG.GetProperties().get(name);
		if (property == null){
			context.getSource().sendFailure(Component.literal("No such property"));
			return 0;
		}

		try {
			property.Decode(value);
			IO.Write(CONFIG);
		}
		catch (IllegalArgumentException|IOException e) {
			context.getSource().sendFailure(Component.literal(e.toString()));
			return -1;
		}

		Get(context);
		return 1;
	}
}
