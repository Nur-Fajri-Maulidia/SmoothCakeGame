package com.semutlevel.game.desktop;

import com.badlogic.gdx.Game;
import com.semutlevel.game.SemutMenu;
import com.semutlevel.game.SemutMenu;
import com.semutlevel.game.SemutMenu;

public class SemutGame   extends Game {
    @Override
    public void create() {
        SemutMenu cm = new SemutMenu(this);
        setScreen(cm);
    }
}
