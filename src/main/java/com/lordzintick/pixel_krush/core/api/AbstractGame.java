package com.lordzintick.pixel_krush.core.api;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.lordzintick.pixel_krush.core.util.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;
import com.lordzintick.pixel_krush.core.util.*;
import com.lordzintick.pixel_krush.core.util.Logger;
import com.lordzintick.pixel_krush.core.util.registry.*;
import com.lordzintick.pixel_krush.core.util.input.GamepadInput;
import com.lordzintick.pixel_krush.core.util.input.Input;
import com.lordzintick.pixel_krush.core.util.input.Keybind;
import com.lordzintick.pixel_krush.core.ui.api.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

//TODO: Not that much stuff now
public abstract class AbstractGame extends ApplicationAdapter {
    public static final String GLOBAL_NAMESPACE = "global";
    public static final String ENGINE_NAME = "PixelKrushEngine";
    private final Logger LOGGER = new Logger(ENGINE_NAME);

    private final Registry<Registry<?>> registryRegistry = new Registry<>();
    private final Registry<Function<AbstractGame, DeferredRegister<?>>> registrarRegistry = new Registry<>();

    private Registry<IModifiableValue<?>> metadata;
    private Registry<SpriteBatch> batchRegistry;
    private Registry<BitmapFont> fontRegistry;
    private Registry<IGameDataSerializer> gameDataRegistry;
    private Registry<TiledAtlas> atlasRegistry;
    private Registry<NinePatch> patchRegistry;
    private Registry<Texture> cachedTextures;
    private Registry<BaseScreen> screenRegistry;
    private Registry<Sound> audioRegistry;
    private Registry<Keybind> keybindRegistry;

    private SpriteBatch uiBatch;
    private SpriteBatch gameBatch;

    private AssetManager assetManager;
    private boolean loadedAssets = false;
    private FileHandle fontFile;
    private GameDataHandler gameDataHandler;
    private FileHandle gameDataFile;

    private Input input;
    private GamepadInput gamepadInput;
    public int gamepadCursorX, gamepadCursorY;

    private Random random;
    private Json json;

    private BaseScreen screen;
    private OrthographicCamera camera;
    private float toastTicks = 0;
    public Toast displayingToast = null;

    @Override
    public final void create() {
        LOGGER.log("Starting!");
        gamepadCursorX = Gdx.graphics.getWidth() / 2;
        gamepadCursorY = Gdx.graphics.getHeight() / 2;

        json = new Json();
        camera = new OrthographicCamera(2, 2);
        camera.setToOrtho(false, 2, 2);

        LOGGER.log("Initializing render batches...");
        batchRegistry = registryRegistry.register(getGlobalId("batches"), new Registry<>());
        gameBatch = batchRegistry.register(getGlobalId("game"), new SpriteBatch());
        uiBatch = batchRegistry.register(getGlobalId("ui"), new SpriteBatch());
        batchRegistry.register(getGlobalId("background"), new SpriteBatch());

        LOGGER.log("Building fonts...");
        fontFile = Gdx.files.local("Monocraft.ttf");
        fontRegistry = registryRegistry.register(getGlobalId("fonts"), new Registry<>());
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.classpath("Monocraft.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.color = Color.WHITE;
        param.size = 22;
        fontRegistry.register(getGlobalId("normal"), fontGenerator.generateFont(param));
        param.borderColor = Color.BLACK;
        param.borderWidth = 2;
        fontRegistry.register(getGlobalId("outlined"), fontGenerator.generateFont(param));
        param.size = 66;
        param.borderColor = Color.DARK_GRAY;
        fontRegistry.register(getGlobalId("mega"), fontGenerator.generateFont(param));
        fontGenerator.dispose();

        LOGGER.log("Loading assets...");
        assetManager = new AssetManager(new LocalFileHandleResolver());
        assetManager.setLoader(Texture.class, new TextureLoader(new LocalFileHandleResolver()));
        assetManager.setLoader(com.badlogic.gdx.audio.Sound.class, new SoundLoader(new LocalFileHandleResolver()));
        assetManager.setLoader(Music.class, new MusicLoader(new LocalFileHandleResolver()));
        loadAllAssets(Gdx.files.local("textures"));
        loadAllAssets(Gdx.files.local("audio"));

        LOGGER.log("Initializing input capturing...");
        input = new Input(this);
        gamepadInput = new GamepadInput(this);
        Controllers.addListener(gamepadInput);
        Gdx.input.setInputProcessor(input);
        random = new Random(System.currentTimeMillis());

        LOGGER.log("Initializing registries...");
        screenRegistry = registryRegistry.register(getGlobalId("screens"), new Registry<>());
        keybindRegistry = registryRegistry.register(getGlobalId("keybinds"), new Registry<>());
        audioRegistry = registryRegistry.register(getGlobalId("audio"), new Registry<>());
        metadata = registryRegistry.register(getGlobalId("metadata"), new Registry<>());
        gameDataRegistry = registryRegistry.register(getGlobalId("game_data"), new Registry<>());
        gameDataHandler = new GameDataHandler();

        LOGGER.log("Finished pre-initialization!");
        LOGGER.log("Starting initialization phase...");
        initialize();
    }

    @Override
    public void render() {
        BitmapFont megaFont = getFont("mega");

        if (assetManager.update()) {
            if (!loadedAssets) {
                LOGGER.log("Finishing initialization...");
                // Initialize asset-related instances
                loadedAssets = true;
                LOGGER.log("Loading asset registries...");
                atlasRegistry = registryRegistry.register(getGlobalId("atlases"), new Registry<>());
                patchRegistry = registryRegistry.register(getGlobalId("nine_patches"), new Registry<>());
                cachedTextures = registryRegistry.register(getGlobalId("cached_textures"), new Registry<>());
                loadAssets();
                activateRegistrars();

                // Initialize data file
                LOGGER.log("Acquiring game data file...");
                gameDataFile = Gdx.files.local("gameData.json");
                if (!gameDataFile.exists()) {
                    LOGGER.log("No game data file found. Creating new game data file...");
                    gameDataFile.parent().mkdirs();
                    try {
                        if (gameDataFile.file().createNewFile()) {
                            LOGGER.log("Successfully created data file at path " + gameDataFile.file().getAbsolutePath());
                        }
                    } catch (IOException e) {
                        LOGGER.log("IOException: " + e.getMessage());
                    }
                    writeGameData();
                    LOGGER.log("Finished creating game data file!");
                }
                readGameData();
                postInit();
                changeScreen(getStartScreen());
                LOGGER.log("Completed initialization!");
            }
            BitmapFont outlinedFont = getFont("outlined");

            // Clear the screen to the background color and update screen objects + the camera
            ScreenUtils.clear(screen == null ? Color.BLACK : screen.getBackgroundColor());
            if (Controllers.getCurrent() != null && Controllers.getCurrent().isConnected()) {
                Controller controller = Controllers.getCurrent();
                gamepadCursorX = MathUtils.clamp(gamepadCursorX + (int) (controller.getAxis(2) * 50), 0, Gdx.graphics.getWidth());
                gamepadCursorY = MathUtils.clamp(gamepadCursorY + (int) (controller.getAxis(3) * 50), 0, Gdx.graphics.getHeight());

                Gdx.input.setCursorPosition(gamepadCursorX, gamepadCursorY);
            }
            preUpdate();
            if (screen != null && !screen.isPaused()) {
                screen.update(Gdx.graphics.getDeltaTime());
            }
            update();
            camera.update();

            // Update the gameBatch transform matrix
            gameBatch.setTransformMatrix(camera.combined);

            // Render the current screen
            gameBatch.begin();
            screen.renderGame(Gdx.graphics.getDeltaTime());
            gameRender();
            gameBatch.end();

            uiBatch.begin();
            screen.renderUI(Gdx.graphics.getDeltaTime());
            uiRender();
            outlinedFont.draw(uiBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 40, Gdx.graphics.getHeight() - outlinedFont.getLineHeight() * 2);
            outlinedFont.setColor(Color.GOLD);

            if (screen.isPaused()) {
                megaFont.draw(uiBatch, "PAUSED", (float) Gdx.graphics.getWidth() / 2 - UIUtil.getFontStringWidth("PAUSED", megaFont) / 2, (float) Gdx.graphics.getHeight() - megaFont.getLineHeight());
            }

            if (toastTicks > 0) {
                toastTicks -= Gdx.graphics.getDeltaTime();
                if (displayingToast != null) {
                    displayingToast.render(uiBatch, Gdx.graphics.getDeltaTime());
                }
            }

            uiBatch.end();
        } else {
            ScreenUtils.clear(Color.BLACK);

            float progress = assetManager.getProgress();
            String progressText = (int) (progress * 100) + "%";
            String engineNameText = ENGINE_NAME.substring(ENGINE_NAME.length() - Math.round(MathUtils.map(0, 1, 0, ENGINE_NAME.length(), progress)));
            uiBatch.begin();
            megaFont.draw(uiBatch, progressText, (float) Gdx.graphics.getWidth() / 2 - UIUtil.getFontStringWidth(progressText, megaFont) / 2, (float) Gdx.graphics.getHeight() / 2 - megaFont.getLineHeight() / 2);
            megaFont.draw(uiBatch, engineNameText, (float) Gdx.graphics.getWidth() / 2 - UIUtil.getFontStringWidth(ENGINE_NAME, megaFont) / 2, (float) Gdx.graphics.getHeight() / 2 - megaFont.getLineHeight() * 2);
            uiBatch.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (width == 0 || height == 0) return;

        camera.setToOrtho(false, 2, 2);
    }

    @Override
    public void dispose() {
        LOGGER.log("Shutting down...");
        LOGGER.log("Freeing batches and fonts...");
        batchRegistry.forEachEntry((id, batch) -> batch.dispose());
        fontRegistry.forEachEntry((id, font) -> font.dispose());
        LOGGER.log("Releasing cached assets...");
        screen.dispose();

        assetManager.dispose();
        writeGameData();
        LOGGER.log("Deinitialization Complete! Goodbye!");
    }

    private void readGameData() {
        LOGGER.log("Loading game data...");
        gameDataHandler.updateSerializers(gameDataRegistry);
        gameDataHandler.read(json, json.getReader().parse(gameDataFile));
    }
    private void writeGameData() {
        LOGGER.log("Saving game data...");
        gameDataHandler.updateSerializers(gameDataRegistry);
        json.toJson(gameDataHandler, gameDataFile);
    }

    public Random getRandom() {return random;}
    public Input getInput() {return input;}
    public OrthographicCamera getCamera() {return camera;}

    public <T> T getAssetOrThrow(String path) {
        if (!assetManager.isLoaded(path))
            throw new AssetException("File " + path + " was not found in the asset manager");

        return assetManager.get(path);
    }
    public Identifier getAssetId(String prefix, String path) {
        return getId(prefix + "_" + getFileName(path).replace(".png", ""));
    }
    public String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }
    protected void createAtlas(String path, int tileWidth, int tileHeight) {
        if (!loadedAssets)
            throw new IllegalStateException("Cannot create atlases before the assets have loaded");
        Identifier id = getAssetId("atlas", path);
        LOGGER.log("Caching texture atlas with id \"" + id + "\"");
        atlasRegistry.register(id, new TiledAtlas(this, path, tileWidth, tileHeight));
    }
    protected void createNinePatch(String path, int left, int right, int top, int bottom) {
        if (!loadedAssets)
            throw new IllegalStateException("Cannot create nine patches before the assets have loaded");
        Identifier id = getAssetId("nine_patch", path);
        LOGGER.log("Caching nine-patch with id \"" + id + "\"");
        patchRegistry.register(id, new NinePatch((Texture) getAssetOrThrow(path), left, right, top, bottom));
    }
    protected void cacheTexture(String path) {
        if (!loadedAssets)
            throw new IllegalStateException("Cannot cache textures before the assets have loaded");
        Identifier id = getAssetId("texture", path);
        LOGGER.log("Caching texture with id \"" + id + "\"");
        cachedTextures.register(id, getAssetOrThrow(path));
    }

    public final TiledAtlas getCachedAtlas(String name) {
        if (!loadedAssets)
            throw new IllegalStateException("Cannot query atlases before the assets have loaded");
        Identifier id = getId("atlas_" + name);
        try {
            return atlasRegistry.getOrError(id);
        } catch (RegistryError e) {
            throw new IdentifierNotFoundException("Atlas with id \"" + id + "\" was either not found or not cached at initialization");
        }
    }
    public final NinePatch getCachedNinePatch(String name) {
        if (!loadedAssets)
            throw new IllegalStateException("Cannot query nine patches before the assets have loaded");
        Identifier id = getId("nine_patch_" + name);
        try {
            return patchRegistry.getOrError(id);
        } catch (RegistryError e) {
            throw new IdentifierNotFoundException("Nine-patch with id \"" + id + "\" was either not found or not cached at initialization");
        }
    }
    public final Texture getCachedTexture(String name) {
        if (!loadedAssets)
            throw new IllegalStateException("Cannot query textures before the assets have loaded");
        Identifier id = getId("texture_" + name);
        try {
            return cachedTextures.getOrError(id);
        } catch (RegistryError e) {
            throw new IdentifierNotFoundException("Texture with id \"" + id + "\" was either not found or not cached at initialization");
        }
    }

    protected <T extends IGameDataSerializer> T registerDataSerializer(String name, T dataSerializer) {
        return gameDataRegistry.register(getId(name), dataSerializer);
    }
    public <T extends IGameDataSerializer> T getDataSerializerOrThrow(String name) {
        return gameDataRegistry.getOrThrow(getId(name));
    }
    protected void createRegistry(String name) {
        if (loadedAssets)
            throw new IllegalStateException("Cannot create new registries after assets have been loaded");
        registryRegistry.register(getId(name), new Registry<>());
    }
    protected void connectRegistrar(Identifier id, Function<AbstractGame, DeferredRegister<?>> registrar) {
        registrarRegistry.register(id, registrar);
    }
    private void activateRegistrars() {
        LOGGER.log("Activating registrars...");
        registrarRegistry.forEachEntry((id, registrar) -> {
            LOGGER.log("Activating registrar with id \"" + id + "\"");
            DeferredRegister<?> deferredRegister = registrar.apply(this);
            validateRegistry(deferredRegister.getRegistryId());
            deferredRegister.register(this);
        });
    }

    protected <T> void putMetadata(String name, T value) {
        ModifiableValueImpl<T> metaVal = metadata.register(getId(name), new ModifiableValueImpl<>(value));
    }
    public <T> void setMetadata(String name, T value) {
        ModifiableValueImpl<T> metaVal = metadata.getOrThrow(getId(name));
        metaVal.set(value);
        metadata.override(getId(name), metaVal);
    }
    public <T> T getMetadata(String name) {
        return (T) metadata.getOrThrow(getId(name)).get();
    }

    protected <T extends BaseScreen> void addScreen(String name, T screen) {
        screenRegistry.register(getId(name), screen);
    }

    public <T> void registerDeferred(DeferredRegister<T> registrar) {
        Registry<T> registry;
        try {
            registry = registryRegistry.getOrError(registrar.getRegistryId());
        } catch (RegistryError e) {
            throw new IllegalRegistrationException("Registry " + registrar.getRegistryId() + " was not found");
        }
        registry.registerAll(registrar);
    }
    public <T> ImmutableRegistry<T> queryRegistryOrThrow(Identifier id) {return ImmutableRegistry.of(registryRegistry.getOrThrow(id));}
    public <T> ImmutableRegistry<T> queryRegistryOrError(Identifier id) throws RegistryError {return ImmutableRegistry.of(registryRegistry.getOrError(id));}
    protected void validateRegistry(Identifier id) {
        Registry<?> registry = registryRegistry.getOrNull(id);
        if (registry == null)
            throw new IdentifierNotFoundException(id.toString());
    }

    public BitmapFont getFont(String name) {return fontRegistry.getOrThrow(getGlobalId(name));}
    public SpriteBatch getBatch(String name) {return batchRegistry.getOrThrow(getGlobalId(name));}
    public <T extends Sound> T getAudioSample(String name) {return audioRegistry.getOrThrow(getId(name));}
    public Keybind getKeybind(String name) {return keybindRegistry.getOrThrow(getId(name));}

    public BaseScreen getScreen() {return screen;}
    public void togglePaused() {
        if (screen.isPaused()) {
            screen.resume();
        } else {
            screen.pause();
        }
    }
    public void resetScreen(String name, BaseScreen override) {
        screenRegistry.override(getId(name), override);
    }
    public void changeScreen(String name) {
        BaseScreen newScreen;
        try {
            newScreen = screenRegistry.getOrError(getId(name));
        } catch (RegistryError e) {
            LOGGER.warn("Could not find screen with name \"" + name + "\"");
            return;
        }
        if (screen == null) {
            this.screen = newScreen;
            screen.startMusic();
            return;
        }

        boolean musicChange = !Objects.equals(this.screen.getPlayingBackgroundMusic(), newScreen.getPlayingBackgroundMusic());
        if (musicChange) {
            this.screen.pauseMusic();
        }
        this.screen = newScreen;
        if (musicChange) {
            this.screen.startMusic();
        }
    }

    public void showToast(Toast toast, float time) {
        this.toastTicks = time;
        this.displayingToast = toast;
    }

    private void loadAllAssets(FileHandle folder) {
        String folderName = folder.nameWithoutExtension();
        for (FileHandle child : folder.list()) {
            if (child.isDirectory()) {
                loadAllAssets(child);
            } else {
                if (child.name().endsWith(".png")) {
                    assetManager.load(child.path(), Texture.class);
                } else {
                    if (folderName.equals("sfx") || folderName.equals("sounds")) {
                        assetManager.load(child.path(), com.badlogic.gdx.audio.Sound.class);
                    } else if (folderName.equals("music")) {
                        assetManager.load(child.path(), Music.class);
                    }
                }
            }
        }
    }

    public static Identifier getGlobalId(String path) {return Identifier.of(GLOBAL_NAMESPACE, path);}
    public Identifier getId(String path) {return Identifier.of(getNamespace(), path);}
    public abstract String getNamespace();
    public abstract String getStartScreen();
    protected abstract void initialize();
    protected void loadAssets() {}
    protected void postInit() {}
    protected void preUpdate() {}
    protected void update() {}
    protected void gameRender() {}
    protected void uiRender() {}

    private static final class GameDataHandler implements Json.Serializable {
        private final ArrayList<IGameDataSerializer> serializers = new ArrayList<>();

        private GameDataHandler() {}

        public void updateSerializers(Registry<IGameDataSerializer> serializerRegistry) {
            serializers.clear();
            serializerRegistry.forEachEntry((id, serializer) -> serializers.add(serializer));
        }

        @Override
        public void write(Json json) {
            serializers.forEach(
                    serializer -> {
                        json.writeObjectStart("\"" + serializer.getName() + "\"");
                        serializer.write().forEach(
                            (key, value) -> json.writeValue("\"" + key + "\"", value)
                        );
                        json.writeObjectEnd();
                    }
            );
        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            serializers.forEach(
                serializer -> serializer.read(new JsonAccessor(serializer.getName(), json, jsonData))
            );
        }
    }
}
