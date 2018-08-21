package com.lan.jumper.desktop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AssetPacker {

    private static final boolean DRAW_DEBUG_LINES = false;

    private static final String INPUT_PATH = "desktop/raw-assets";
    private static final String OUTPUT_PATH = "./android/assets";

    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.debug = DRAW_DEBUG_LINES;
        settings.maxHeight = 2048;
        settings.maxWidth = 2048;

        TexturePacker.process(
                settings,
                INPUT_PATH + "/gameplay-16bit",
                OUTPUT_PATH + "/gameplay",
                "gameplay"
        );

        TexturePacker.process(
                settings,
                INPUT_PATH + "/skin",
                OUTPUT_PATH + "/skin",
                "/skin"
        );
    }
}
