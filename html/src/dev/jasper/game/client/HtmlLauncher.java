package dev.jasper.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import dev.jasper.game.BobIsMelting;

public class HtmlLauncher extends GwtApplication {

    @Override
    public void onModuleLoad () {
        super.onModuleLoad();

        com.google.gwt.user.client.Window.addResizeHandler(ev -> {
            GameScreenSize screenSize = calculateGameScreenSize();

            Gdx.graphics.setWindowedMode(screenSize.width, screenSize.height);
        });
    }

    @Override
    public GwtApplicationConfiguration getConfig () {
        GameScreenSize screenSize = calculateGameScreenSize();

        return new GwtApplicationConfiguration(screenSize.width, screenSize.height);
    }

    @Override
    public ApplicationListener createApplicationListener () {
        return new BobIsMelting();
    }

    private GameScreenSize calculateGameScreenSize() {
        float aspectRatio = (float) BobIsMelting.V_WIDTH / BobIsMelting.V_HEIGHT;
        Element container = DOM.getElementById("game-container");
        int containerWidth = container.getOffsetWidth();
        int widthBasedHeight = (int) (containerWidth / aspectRatio);
        return new GameScreenSize(containerWidth, widthBasedHeight);
    }
}

class GameScreenSize {
    public final int width;
    public final int height;

    public GameScreenSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}