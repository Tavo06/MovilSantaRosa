package com.upsjb.movilsantarosa.core.navigation.component

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events (forward and back) by updating the navigation state.
 */
class Navigator(val state: NavigationState){
    fun navigate(route: NavKey){
        if (route in state.backStacks.keys){
            // This is a top level route, just switch to it
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack(){
        val currentStack = state.backStacks[state.topLevelRoute] ?:
        error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.last()

        // If we're at the base of the current route, go back to the start route stack.
        if (currentRoute == state.topLevelRoute){
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }

    fun reset(route: NavKey) {
        state.backStacks.forEach { (topLevel, stack) ->
            stack.clear()
            stack.add(topLevel)
        }

        if (route in state.backStacks.keys) {
            state.topLevelRoute = route
        } else {
            state.topLevelRoute = state.startRoute
            state.backStacks[state.startRoute]?.apply {
                clear()
                add(state.startRoute)
                add(route)
            }
        }
    }
}