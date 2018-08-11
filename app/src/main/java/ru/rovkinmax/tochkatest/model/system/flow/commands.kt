package ru.rovkinmax.tochkatest.model.system.flow

import ru.terrakok.cicerone.commands.Command

class PresetScreen(val screenKey: String, val transitionData: Any?) : Command
class ToggleScreen(val newScreenKey: String, val oldScreenKey: String) : Command