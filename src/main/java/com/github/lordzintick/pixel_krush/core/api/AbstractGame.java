package com.github.lordzintick.pixel_krush.core.api;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.github.lordzintick.pixel_krush.core.util.*;
import com.github.lordzintick.pixel_krush.core.util.Logger;
import com.github.lordzintick.pixel_krush.core.util.audio.Sound;
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
import com.github.lordzintick.pixel_krush.core.util.input.GamepadInput;
import com.github.lordzintick.pixel_krush.core.util.input.Input;
import com.github.lordzintick.pixel_krush.core.util.input.Keybind;
import com.github.lordzintick.pixel_krush.core.ui.api.Toast;
import com.github.lordzintick.pixel_krush.core.util.registry.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

/**
 * A massive class that is the base of all games.<br>
 * An inheritor of this class is always needed for games to keep track of everything.
 */
public abstract class AbstractGame extends ApplicationAdapter {
    /**
     * A string with the value <code>"global"</code> used for global registries and IDs not specific to the game.
     */
    public static final String GLOBAL_NAMESPACE = "global";
    /**
     * A string with the value <code>"PixelKrushEngine"</code> used to define the name of the engine for various purposes.
     */
    public static final String ENGINE_NAME = "PixelKrushEngine";
    /**
     * A {@link Logger} used for lifecycle and status logging.
     */
    private final Logger LOGGER = new Logger(ENGINE_NAME);

    /**
     * A top-level {@link Registry} of {@link Registry Registries} used for, well, registering {@link Registry Registries}.
     */
    private final Registry<Registry<?>> registryRegistry = new Registry<>();
    /**
     * A {@link Registry} of {@link Function}s to keep track of {@link DeferredRegister}s for correct registration.
     */
    private final Registry<Function<AbstractGame, DeferredRegister<?>>> registrarRegistry = new Registry<>();

    /**
     * A {@link Registry} used to hold all the game-specific metadata.
     */
    private Registry<IModifiableValue<?>> metadata;
    /**
     * A {@link Registry} used for holding rendering batches.
     */
    private Registry<SpriteBatch> batchRegistry;
    /**
     * A {@link Registry} used for holding fonts.
     */
    private Registry<BitmapFont> fontRegistry;
    /**
     * A {@link Registry} of {@link IGameDataSerializer}s for keeping track of all the game data serializers for data I/O.
     */
    private Registry<IGameDataSerializer> gameDataRegistry;
    /**
     * A {@link Registry} of cached {@link TiledAtlas}es used to keep track of which atlases are cached.
     */
    private Registry<TiledAtlas> atlasRegistry;
    /**
     * A {@link Registry} of cached {@link NinePatch}es used to keep track of which nine-patches are cached.
     */
    private Registry<NinePatch> patchRegistry;
    /**
     * A {@link Registry} of cached {@link Texture}s used to keep track of which textures are cached.
     */
    private Registry<Texture> cachedTextures;
    /**
     * A {@link Registry} of {@link BaseScreen}s used for holding different screens of the game.
     */
    private Registry<BaseScreen> screenRegistry;
    /**
     * A {@link Registry} of {@link Sound}s used for keeping track of registered audio samples.
     */
    private Registry<Sound> audioRegistry;
    /**
     * A {@link Registry} of {@link Keybind}s used for input detection.
     */
    private Registry<Keybind> keybindRegistry;

    /**
     * A reference to the batch used to draw UI elements.
     */
    private SpriteBatch uiBatch;
    /**
     * A reference to the batch used to draw game elements.
     */
    private SpriteBatch gameBatch;

    /**
     * The {@link AssetManager} used for loading and querying filesystem assets.
     */
    private AssetManager assetManager;
    /**
     * A <code>boolean</code> used to keep track of whether the assets have already loaded yet.
     */
    private boolean loadedAssets = false;
    /**
     * A {@link GameDataHandler} used for data I/O.
     */
    private GameDataHandler gameDataHandler;
    /**
     * A {@link FileHandle} linking to the <code>gameData.json</code> file used for data saving.
     */
    private FileHandle gameDataFile;

    /**
     * The {@link Input} handler of the application.
     */
    private Input input;
    /**
     * Two integers representing the position of the cursor when using gamepad control.
     */
    public int gamepadCursorX, gamepadCursorY;

    /**
     * A {@link Random} instance used for random number generation.
     */
    private Random random;
    /**
     * A {@link Json} instance used for JSON I/O operations.
     */
    private Json json;

    /**
     * The current {@link BaseScreen Screen} of the game.
     */
    private BaseScreen screen;
    /**
     * The camera for the game viewport.
     */
    private OrthographicCamera camera;
    /**
     * The remaining time in seconds until the current toast in question disappears.
     */
    private float toastTicks = 0;
    /**
     * The {@link Toast} that the game is currently displaying.
     */
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
        GamepadInput gamepadInput = new GamepadInput(this);
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
        screenRegistry.forEachEntry((id, screenl) -> screenl.dispose());

        assetManager.dispose();
        writeGameData();
        LOGGER.log("Deinitialization Complete! Goodbye!");
    }

    /**
     * Reads game data from disk and loads it.
     */
    private void readGameData() {
        LOGGER.log("Loading game data...");
        gameDataHandler.updateSerializers(gameDataRegistry);
        gameDataHandler.read(json, json.getReader().parse(gameDataFile));
    }

    /**
     * Saves game data and writes it to disk.
     */
    private void writeGameData() {
        LOGGER.log("Saving game data...");
        gameDataHandler.updateSerializers(gameDataRegistry);
        json.toJson(gameDataHandler, gameDataFile);
    }

    /**
     * Gets the {@link Random} instance used by the game for random number generation.
     * @return The {@link Random} instance used by the game.
     */
    public Random getRandom() {return random;}

    /**
     * Gets an asset at a specified path, or throws an exception if none is found.
     * @param path The path to get the asset at.
     * @param <T> The type of asset to load.
     * @return The asset found at the specified path.
     */
    public <T> T getAssetOrThrow(String path) {
        if (!assetManager.isLoaded(path))
            throw new AssetException("File " + path + " was not found in the asset manager");

        return assetManager.get(path);
    }

    /**
     * A utility method to turn a specified prefix and path into a formatted identifier.
     * @param prefix A prefix to use, to identify different types of asset IDs.
     * @param path The path of the asset, to transform into an asset ID.
     * @return A formatted ID created from the specified prefix and path.
     */
    public Identifier getAssetId(String prefix, String path) {
        return getId(prefix + "_" + getFileName(path).replace(".png", "").replace(".wav", "").replace(".mp3", "").replace(".ogg", ""));
    }

    /**
     * Gets the name of the file in the specified path.
     * @param path The path to get the filename for.
     * @return The name of the file specified in the path.
     */
    public String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    /**
     * Creates and caches an atlas with the specified texture path and tile dimensions.
     * @param path The path of the texture to load the atlas with.
     * @param tileWidth The width of a tile in the atlas.
     * @param tileHeight The height of a tile in the atlas.
     */
    protected void createAtlas(String path, int tileWidth, int tileHeight) {
        if (!loadedAssets)
            throw new IllegalStateException("Cannot create atlases before the assets have loaded");
        Identifier id = getAssetId("atlas", path);
        LOGGER.log("Caching texture atlas with id \"" + id + "\"");
        atlasRegistry.register(id, new TiledAtlas(this, path, tileWidth, tileHeight));
    }

    /**
     * Creates and caches a nine-patch with the specified texture path and border dimensions.
     * @param path The path of the texture to load the nine-patch with.
     * @param left The pixels from the left edge of the texture to use as the left border.
     * @param right The pixels from the right edge of the texture to use as the right border.
     * @param top The pixels from the top edge of the texture to use as the top border.
     * @param bottom The pixels from the bottom edge of the texture to use as the bottom border.
     */
    protected void createNinePatch(String path, int left, int right, int top, int bottom) {
        if (!loadedAssets)
            throw new IllegalStateException("Cannot create nine patches before the assets have loaded");
        Identifier id = getAssetId("nine_patch", path);
        LOGGER.log("Caching nine-patch with id \"" + id + "\"");
        patchRegistry.register(id, new NinePatch((Texture) getAssetOrThrow(path), left, right, top, bottom));
    }

    /**
     * Creates and caches a texture with no further modifications.
     * @param path The path of the texture to cache.
     */
    protected void cacheTexture(String path) {
        if (!loadedAssets)
            throw new IllegalStateException("Cannot cache textures before the assets have loaded");
        Identifier id = getAssetId("texture", path);
        LOGGER.log("Caching texture with id \"" + id + "\"");
        cachedTextures.register(id, getAssetOrThrow(path));
    }

    /**
     * Gets an atlas that was previously cached with the specified name, or throws an exception if none was found.
     * @param name The name of the atlas to get.<br>
     *             Corresponds to the name of the texture used to create the atlas, for example the texture "textures/atlas.png" would have a name of "atlas"
     * @return The {@link TiledAtlas} cached with the specified name.
     */
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
    /**
     * Gets a nine-patch that was previously cached with the specified name, or throws an exception if none was found.
     * @param name The name of the nine-patch to get.<br>
     *             Corresponds to the name of the texture used to create the nine-patch, for example the texture "textures/patch.png" would have a name of "patch"
     * @return The {@link NinePatch} cached with the specified name.
     */
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
    /**
     * Gets a texture that was previously cached with the specified name, or throws an exception if none was found.
     * @param name The name of the texture to get.<br>
     *             Corresponds to the name of the texture cached, for example the texture "textures/tex.png" would have a name of "tex"
     * @return The {@link Texture} cached with the specified name.
     */
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

    /**
     * Registers an {@link IGameDataSerializer} with the specified name, allowing it to read/write to the game data file.
     * @param name The name of the data serializer.
     * @param dataSerializer The data serializer to register.
     * @param <T> The type of data serializer being registered.
     * @return The data serializer that was registered.
     */
    protected <T extends IGameDataSerializer> T registerDataSerializer(String name, T dataSerializer) {
        return gameDataRegistry.register(getId(name), dataSerializer);
    }

    /**
     * Gets an {@link IGameDataSerializer} with the specified name, or throws an exception if none was found.
     * @param name The name of the data serializer to get.
     * @param <T> The type of serializer.
     * @return The game data serializer with the specified name.
     */
    public <T extends IGameDataSerializer> T getDataSerializerOrThrow(String name) {
        return gameDataRegistry.getOrThrow(getId(name));
    }


    /**
     * Creates a new, custom registry with the specified name.<br>
     * See {@link DeferredRegister} and {@link AbstractGame#connectRegistrar(Identifier, Function)} for more info on how to register to it.
     * @param name The name of the registry to create.
     */
    protected void createRegistry(String name) {
        if (loadedAssets)
            throw new IllegalStateException("Cannot create new registries after assets have been loaded");
        registryRegistry.register(getId(name), new Registry<>());
    }

    /**
     * Connects a {@link DeferredRegister} to its specified {@link Registry} (see {@link DeferredRegister} for more info) for future registration.
     * @param id The identifier of the registrar.
     * @param registrar A function that will be used to get the {@link DeferredRegister} when registrars are activated.
     */
    protected void connectRegistrar(Identifier id, Function<AbstractGame, DeferredRegister<?>> registrar) {
        registrarRegistry.register(id, registrar);
    }


    /**
     * Activates the registrars, one by one, registering their objects into their target registry.<br>
     * See {@link DeferredRegister} for more information.
     */
    private void activateRegistrars() {
        LOGGER.log("Activating registrars...");
        registrarRegistry.forEachEntry((id, registrar) -> {
            LOGGER.log("Activating registrar with id \"" + id + "\"");
            DeferredRegister<?> deferredRegister = registrar.apply(this);
            validateRegistry(deferredRegister.getRegistryId());
            deferredRegister.register(this);
        });
    }

    /**
     * Adds metadata to the game.<br>
     * This can be used to store various general information.
     * @param name The name of the metadata to add.
     * @param value The initial value of the metadata to add.
     * @param <T> The type of metadata to add.
     */
    protected <T> void putMetadata(String name, T value) {
        metadata.register(getId(name), new ModifiableValueImpl<>(value));
    }

    /**
     * Sets a metadata with the specified name to the new value.
     * @param name The name of metadata to modify.
     * @param value The new value to set the metadata to.
     * @param <T> The type of metadata to set.
     */
    public <T> void setMetadata(String name, T value) {
        ModifiableValueImpl<T> metaVal = metadata.getOrThrow(getId(name));
        metaVal.set(value);
        metadata.override(getId(name), metaVal);
    }

    /**
     * Gets the value of the metadata with the specified name.
     * @param name The name of the metadata to query.
     * @param <T> The type of metadata value to get.
     * @return The value of the metadata with the specified name.
     */
    public <T> T getMetadata(String name) {
        return (T) metadata.getOrThrow(getId(name)).get();
    }

    /**
     * Adds/registers a screen into the screen registry so it can be switched to later.
     * @param name The name of the screen to add.
     * @param screen The screen to add.
     * @param <T> The type of screen to add.
     */
    protected <T extends BaseScreen> void addScreen(String name, T screen) {
        screenRegistry.register(getId(name), screen);
    }

    /**
     * Registers a deferred register into its target registry.<br>
     * This should not be used directly, see {@link DeferredRegister}.
     * @param registrar The {@link DeferredRegister} to register its objects into its target registry.
     * @param <T> The type of the registry to register into.
     */
    public <T> void registerDeferred(DeferredRegister<T> registrar) {
        Registry<T> registry;
        try {
            registry = registryRegistry.getOrError(registrar.getRegistryId());
        } catch (RegistryError e) {
            throw new IllegalRegistrationException("Registry " + registrar.getRegistryId() + " was not found");
        }
        registry.registerAll(registrar);
    }

    /**
     * Gets a registry from the <code>registryRegistry</code> with the specified ID, or throws an exception if none was found.
     * @param id The ID of the registry to query.
     * @param <T> The type of registry to query.
     * @return The registry with the specified ID.
     */
    public <T> ImmutableRegistry<T> queryRegistryOrThrow(Identifier id) {return ImmutableRegistry.of(registryRegistry.getOrThrow(id));}
    public <T> ImmutableRegistry<T> queryRegistryOrError(Identifier id) throws RegistryError {return ImmutableRegistry.of(registryRegistry.getOrError(id));}

    /**
     * Checks whether a registry with a specified ID exists, and if not, then throws an exception.
     * @param id The ID to check the existence of the registry attached.
     */
    protected void validateRegistry(Identifier id) {
        Registry<?> registry = registryRegistry.getOrNull(id);
        if (registry == null)
            throw new IdentifierNotFoundException(id.toString());
    }

    /**
     * Gets a font with the specified name, or throws an exception if none was found.
     * @param name The name of the font to get.
     * @return A {@link BitmapFont} with the specified name.
     */
    public BitmapFont getFont(String name) {return fontRegistry.getOrThrow(getGlobalId(name));}
    /**
     * Gets a batch with the specified name, or throws an exception if none was found.
     * @param name The name of the batch to get.
     * @return A {@link SpriteBatch} with the specified name.
     */
    public SpriteBatch getBatch(String name) {return batchRegistry.getOrThrow(getGlobalId(name));}

    /**
     * Gets a registered audio sample with the specified name, or throws an exception if none was found.
     * @param name The name of the audio sample to get.
     * @param <T> The type of {@link Sound} to get.
     * @return The audio sample with the specified name.
     */
    public <T extends Sound> T getAudioSample(String name) {return audioRegistry.getOrThrow(getId(name));}
    /**
     * Gets a keybind with the specified name, or throws an exception if none was found.
     * @param name The name of the keybind to get.
     * @return A {@link Keybind} with the specified name.
     */
    public Keybind getKeybind(String name) {return keybindRegistry.getOrThrow(getId(name));}

    /**
     * Gets the current screen the game is on.
     * @return The current screen.
     */
    public BaseScreen getScreen() {return screen;}

    /**
     * Toggles the "paused" state of the current screen.
     */
    public void togglePaused() {
        if (screen.isPaused()) {
            screen.resume();
        } else {
            screen.pause();
        }
    }

    /**
     * Resets a screen by setting it to the provided <code>override</code>.
     * @param name The name of the screen to reset.
     * @param override The new screen to reset with.
     */
    public void resetScreen(String name, BaseScreen override) {
        screenRegistry.override(getId(name), override);
    }

    /**
     * Changes the current screen to the screen with the provided name.
     * @param name The name of the screen to change to.
     */
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

    /**
     * Shows a {@link Toast} for a certain amount of time.
     * @param toast The {@link Toast} to show.
     * @param time The amount of time, in seconds, to show the toast for.
     */
    public void showToast(Toast toast, float time) {
        this.toastTicks = time;
        this.displayingToast = toast;
    }

    /**
     * Loads all assets from a specified folder into the asset manager.
     * @param folder The {@link FileHandle} of the folder to load from.
     */
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

    /**
     * Gets an ID with the "{@link AbstractGame#GLOBAL_NAMESPACE global}" namespace and the specified path.
     * @param path The path of the new ID.
     * @return A new ID with the "{@link AbstractGame#GLOBAL_NAMESPACE global}" namespace and the specified path.
     */
    public static Identifier getGlobalId(String path) {return Identifier.of(GLOBAL_NAMESPACE, path);}
    /**
     * Gets an ID with {@link AbstractGame#getNamespace() the game's namespace} and the specified path.
     * @param path The path of the new ID.
     * @return A new ID with {@link AbstractGame#getNamespace() the game's namespace} and the specified path.
     */
    public Identifier getId(String path) {return Identifier.of(getNamespace(), path);}

    /**
     * Used to define the namespace used by all game-specific assets.
     * @return The namespace of this game.
     */
    public abstract String getNamespace();

    /**
     * Used to define the name of the screen to open when the game is started.
     * @return The name of the screen to start the game with.
     */
    public abstract String getStartScreen();

    /**
     * Called when the game is initializing, after it has loaded registries but before it has loaded assets.<br>
     * This is where you create registries ({@link AbstractGame#createRegistry(String)}), connect registrars ({@link AbstractGame#connectRegistrar(Identifier, Function)}), register game data serializers ({@link AbstractGame#registerDataSerializer(String, IGameDataSerializer)}), and define metadata ({@link AbstractGame#putMetadata(String, Object)}).
     */
    protected abstract void initialize();

    /**
     * Called when the assets load, after asset-related registries are created.<br>
     * This is where you cache textures ({@link AbstractGame#cacheTexture(String)}, {@link AbstractGame#createNinePatch(String, int, int, int, int)}, {@link AbstractGame#createAtlas(String, int, int)}).
     */
    protected void loadAssets() {}

    /** 
     * Called after the assets are loaded, right before the initial screen is loaded.<br>
     * This is where you register screens ({@link AbstractGame#addScreen(String, BaseScreen)}).
     */
    protected void postInit() {}

    /**
     * Called every frame, before the current screen is updated.
     */
    protected void preUpdate() {}

    /**
     * Called every frame, after the current screen is updated.
     */
    protected void update() {}

    /**
     * Called every frame within the <code>game</code> batch render context.
     */
    protected void gameRender() {}
    /**
     * Called every frame within the <code>ui</code> batch render context.
     */
    protected void uiRender() {}

    /**
     * A wrapper class around {@link Json.Serializable} used to read and write JSON using {@link IGameDataSerializer}s.
     */
    private static final class GameDataHandler implements Json.Serializable {
        /**
         * An {@link ArrayList} storing all of the {@link IGameDataSerializer}s stored in this {@link GameDataHandler}.
         */
        private final ArrayList<IGameDataSerializer> serializers = new ArrayList<>();

        /**
         * Constructs a new {@link GameDataHandler}.
         */
        private GameDataHandler() {}

        /**
         * Clears the serializer list and updates it with the values from the provided registry.
         * @param serializerRegistry The {@link IGameDataSerializer} {@link Registry} holding all the game data serializers.
         */
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
