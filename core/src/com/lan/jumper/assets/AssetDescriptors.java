package com.lan.jumper.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors {
    private AssetDescriptors () {}

    public static final AssetDescriptor<TextureAtlas> GAMEPLAY_ATLAS = new AssetDescriptor<TextureAtlas>(AssetPaths.GAMEPLAY, TextureAtlas.class);
    public static final AssetDescriptor<Skin> SKIN = new AssetDescriptor<Skin>(AssetPaths.SKIN, Skin.class);
    public static final AssetDescriptor<ParticleEffect> PARTICLES = new AssetDescriptor<ParticleEffect>(AssetPaths.PARTICLE, ParticleEffect.class);
}
