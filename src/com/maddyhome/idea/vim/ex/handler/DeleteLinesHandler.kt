/*
 * IdeaVim - Vim emulator for IDEs based on the IntelliJ platform
 * Copyright (C) 2003-2019 The IdeaVim authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.maddyhome.idea.vim.ex.handler

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.maddyhome.idea.vim.VimPlugin
import com.maddyhome.idea.vim.command.SelectionType
import com.maddyhome.idea.vim.ex.CommandHandler
import com.maddyhome.idea.vim.ex.ExCommand
import com.maddyhome.idea.vim.ex.commands
import com.maddyhome.idea.vim.ex.flags
import com.maddyhome.idea.vim.handler.CaretOrder

class DeleteLinesHandler : CommandHandler(commands("d[elete]"),
        flags(CommandHandler.RANGE_OPTIONAL, CommandHandler.ARGUMENT_OPTIONAL, CommandHandler.WRITABLE),
        true, CaretOrder.DECREASING_OFFSET
) {
    override fun execute(editor: Editor, caret: Caret, context: DataContext,
                         cmd: ExCommand): Boolean {
        val argument = cmd.argument
        val register = if (argument.isNotEmpty() && !argument[0].isDigit()) {
            cmd.argument = argument.substring(1)
            argument[0]
        } else {
            VimPlugin.getRegister().defaultRegister
        }

        if (!VimPlugin.getRegister().selectRegister(register)) return false

        val textRange = cmd.getTextRange(editor, caret, context, true)
        return VimPlugin.getChange().deleteRange(editor, caret, textRange, SelectionType.LINE_WISE, false)
    }
}
