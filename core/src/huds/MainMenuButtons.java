package huds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.jackthegiant.GameMain;
import helpers.GameInfo;
import helpers.GameManager;

import scenes.Gameplay;
import scenes.Highscore;
import scenes.Options;

public class MainMenuButtons {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;
    private ImageButton playBtn;
    private ImageButton highscoreBtn;
    private ImageButton optionsBtn;
    private ImageButton quitBtn;
    private ImageButton musicBtn;

    public MainMenuButtons(GameMain game){
        this.game = game;
        gameViewport = new FitViewport(GameInfo.Width, GameInfo.Height,
                new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        createAndPositionButtons();
        addListeners();

        stage.addActor(playBtn);
        stage.addActor(highscoreBtn);
        stage.addActor(optionsBtn);
        stage.addActor(quitBtn);
        stage.addActor(musicBtn);

        checkMusic();
    }

    void createAndPositionButtons() {

        playBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu/Start Game.png"))));

        highscoreBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu/Highscore.png"))));

        optionsBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu/Options.png"))));

        quitBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu/Quit.png"))));

        musicBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu/Music On.png"))));
        playBtn.setPosition(GameInfo.Width / 2 - 80,
                GameInfo.Height / 2 + 50, Align.center);
        highscoreBtn.setPosition(GameInfo.Width / 2 - 60,
                GameInfo.Height / 2 - 20, Align.center);
        optionsBtn.setPosition(GameInfo.Width / 2 - 40,
                GameInfo.Height / 2 - 90, Align.center);
        quitBtn.setPosition(GameInfo.Width / 2 - 20,
                GameInfo.Height / 2 - 160, Align.center);
        musicBtn.setPosition(GameInfo.Width - 13, 13, Align.bottomRight);
    }

    void addListeners(){
        playBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor){
                // any code that we type will be executed when we press the play button
                GameManager.getInstance().gameStartedFromMainMenu = true;

                RunnableAction run = new RunnableAction();
                run.setRunnable((new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new Gameplay(game));
                    }
                }));

                SequenceAction sa = new SequenceAction();
                sa.addAction(Actions.fadeOut(1f));
                sa.addAction(run);

                stage.addAction(sa);
            }
        });

        highscoreBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor){
                game.setScreen(new Highscore(game));
            }
        });

        optionsBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor){
                game.setScreen(new Options(game));
            }
        });

        quitBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor){
            }
        });

        musicBtn.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor){
                if(GameManager.getInstance().gameData.isMusicOn()){
                    GameManager.getInstance().gameData.setMusicOn(false);
                    GameManager.getInstance().stopMusic();
                } else {
                    GameManager.getInstance().gameData.setMusicOn(true);
                    GameManager.getInstance().stopMusic();
                }
                GameManager.getInstance().saveData();
            }
        });
    }

    void checkMusic(){
        if(GameManager.getInstance().gameData.isMusicOn()){
            GameManager.getInstance().playMusic();
        }
    }

    public Stage getStage(){
        return this.stage;
    }

}


