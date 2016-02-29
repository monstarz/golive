package com.monstarz.golive;

import com.monstarz.golive.engine.PachiEngine;

import lrstudios.games.ego.lib.ui.NewGameActivity;

public class NewMatchActivity extends NewGameActivity {

    @Override
    protected Class<?> getBotClass() {
        return PachiEngine.class;
    }
}
