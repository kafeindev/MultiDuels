/*
 * MIT License
 *
 * Copyright (c) 2023 Kafein
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.kafein.multiduels.bukkit.command;

import com.google.common.collect.ImmutableList;
import dev.kafein.multiduels.common.command.Command;
import dev.kafein.multiduels.common.command.CommandSender;
import dev.kafein.multiduels.common.command.completion.RegisteredTabCompletion;
import dev.kafein.multiduels.common.command.completion.TabCompletion;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

final class BukkitCommandAdapter extends BukkitCommand {
    private final Command command;

    BukkitCommandAdapter(Command command) {
        super(command.getName(), command.getDescription(), command.getUsage(), command.getAliases());
        this.command = command;
    }

    @Override
    public boolean execute(@NotNull org.bukkit.command.CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!getAliases().contains(commandLabel)) {
            return false;
        }

        CommandSender commandSender = new BukkitCommandSender(sender);
        if (this.command.getPermission() != null && !commandSender.hasPermission(this.command.getPermission())) {
            //TODO: Send message
            return true;
        }

        if (args.length == 0) {
            this.command.execute(commandSender, args);
        } else {
            Command subCommand = this.command.findSubCommand(args);
            if (subCommand.getPermission() != null && !commandSender.hasPermission(subCommand.getPermission())) {
                //TODO: Send message
                return true;
            }

            String[] subCommandArgs = Arrays.copyOfRange(args, this.command.findSubCommandIndex(args), args.length);
            subCommand.execute(commandSender, subCommandArgs);
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull org.bukkit.command.CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        CommandSender commandSender = new BukkitCommandSender(sender);
        if (this.command.getPermission() != null && !commandSender.hasPermission(this.command.getPermission())) {
            return ImmutableList.of();
        }

        if (args.length == 0) {
            return complete(this.command, commandSender);
        } else {
            Command subCommand = this.command.findSubCommand(args);
            if (subCommand.getPermission() != null && !commandSender.hasPermission(subCommand.getPermission())) {
                return ImmutableList.of();
            }

            return complete(subCommand, commandSender);
        }
    }

    private List<String> complete(@NotNull Command command, @NotNull CommandSender commandSender) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();

        for (Command subCommand : command.getSubCommands()) {
            builder.addAll(subCommand.getAliases());
        }
        for (RegisteredTabCompletion registeredTabCompletion : command.getTabCompletions()) {
            TabCompletion tabCompletion = registeredTabCompletion.getTabCompletion();
            builder.addAll(tabCompletion.apply(commandSender));
        }

        return builder.build();
    }
}
