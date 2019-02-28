package com.batataproductions.game.view.hud;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.batataproductions.game.DeathmatchMania;
import com.batataproductions.game.controller.GameController;
import com.batataproductions.game.model.GameModel;
import com.batataproductions.game.model.entities.PlayerModel;
import com.batataproductions.game.view.controller.Controller;

import static com.badlogic.gdx.Application.ApplicationType.Android;

public class Hud {

    private static Hud instance;
    public Stage stage;
    private Viewport viewport;

    /**
     * The sprite representing blood Top Left.
     */
    Sprite spriteTL;

    /**
     * The sprite representing blood Top Right.
     */
    Sprite spriteTR;

    /**
     * The sprite representing blood Bottom Left.
     */
    Sprite spriteBL;

    /**
     * The sprite representing blood Bottom Right.
     */
    Sprite spriteBR;

    /**
     * The sprite representing blood Bottom Center.
     */
    Sprite spriteBC;

    /**
     * The sprite representing blood Top Center.
     */
    Sprite spriteTC;



    private Controller controller1;

    private Controller controller2;


    /**
     * The animation used when the zombie is still.
     */
    private Animation<TextureRegion> bloodAnimation;

    /** The time between the animation frames for the knife walk animation. */
    private static final float FRAME_TIME_BLOOD = 1/15f;


    private Image rifle;
    private Image pistol;
    private Image knife;
    private Image red;
    private Image bb;


    private Image roundBox;
    private Image zombiesRoundBox;
    private Image enemiesLeftBox;

    private Label roundLabel;
    private Label zombiesRoundLabel;
    private Label enemiesLeftLabel;

    private Label roundNRLabel;
    private Label zombiesRoundNRLabel;
    private Label enemiesLeftNRLabel;

    private Label bulletsLabel;
    private Label healthLabel;

    private float time = 1.02f;
    private boolean doTime = false;

    public Hud (SpriteBatch sb, int width, int height, DeathmatchMania game){
        viewport = new FitViewport(width, height, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        initHudAssets(width, height);

        createBloodSprites(game);

        addActorsToStage();

        Gdx.input.setInputProcessor(stage);

        if (Gdx.app.getType() == Android){ //debug
            controller1 = new Controller();
            controller1.create();
            stage.addActor(controller1.getTouchpad());
            controller2 = new Controller();
            controller2.create();
            controller2.setControllerPosition(width - 270,70);//Gdx.graphics.getWidth() - 70 - controller2.getTouchpad().getWidth(),70);
            stage.addActor(controller2.getTouchpad());
        }
    }

    /**
     * Creates the animation used when the zombie is attacking.
     *
     * @param game the game this view belongs to. Needed to access the
     *             asset manager to get textures.
     * @return the animation used when the zombie is attacking
     */
    private Animation<TextureRegion> createBloodAnimation(DeathmatchMania game) {
        TextureAtlas textureAtlas = game.getAssetManager().get("bloodAtlas.atlas");

        return new Animation(FRAME_TIME_BLOOD, textureAtlas.findRegions("blood"));
    }

    private void createBloodSprites(DeathmatchMania game){
        bloodAnimation = createBloodAnimation(game);

        spriteBL =  new Sprite(bloodAnimation.getKeyFrame(Float.MAX_VALUE, false));
        spriteBR =  new Sprite(bloodAnimation.getKeyFrame(Float.MAX_VALUE, false));
        spriteTL =  new Sprite(bloodAnimation.getKeyFrame(Float.MAX_VALUE, false));
        spriteTR =  new Sprite(bloodAnimation.getKeyFrame(Float.MAX_VALUE, false));
        spriteTC =  new Sprite(bloodAnimation.getKeyFrame(Float.MAX_VALUE, false));
        spriteBC =  new Sprite(bloodAnimation.getKeyFrame(Float.MAX_VALUE, false));

    }

    private void addActorsToStage(){
        addGunsToStage();
        stage.addActor(bb);
        stage.addActor(bulletsLabel);
        stage.addActor(roundBox);
        stage.addActor(zombiesRoundBox);
        stage.addActor(enemiesLeftBox);
        stage.addActor(roundLabel);
        stage.addActor(zombiesRoundLabel);
        stage.addActor(enemiesLeftLabel);
        stage.addActor(roundNRLabel);
        stage.addActor(zombiesRoundNRLabel);
        stage.addActor(enemiesLeftNRLabel);
        stage.addActor(healthLabel);
    }

    private void addGunsToStage(){
        if (Gdx.app.getType() == Android)
            stage.addActor(rifle);
        else{
            stage.addActor(red);
            stage.addActor(rifle);
            stage.addActor(pistol);
            stage.addActor(knife);
        }
    }

    private void initRifle (int width, int height){
        Texture tmp = new Texture(Gdx.files.internal("Ak47_W[HUD].png"));
        TextureRegion image1 = new TextureRegion(tmp);
        rifle = new Image(image1);
        rifle.setScale(0.15f);
        if (Gdx.app.getType() == Android){
            rifle.setX(width/2 - (tmp.getWidth() * 0.15f)/2);
            rifle.setY(height * 30/720);
        }
        else{
            rifle.setX(width - tmp.getWidth() * 0.15f - 20);
            rifle.setY(height * 170/720);
        }
    }

    private void initPistol (int width, int height){
        Texture tmp = new Texture(Gdx.files.internal("Deagle_hud_go_W2.png"));
        TextureRegion image2 = new TextureRegion(tmp);
        pistol = new Image(image2);
        pistol.setScale(0.15f);
        if (Gdx.app.getType() == Android){
            pistol.setX(width/2 - (tmp.getWidth() * 0.15f)/2);
            pistol.setY(height * 30/720);
        }
        else{
            pistol.setX(rifle.getX() + (rifle.getWidth()*0.15f)/2 - pistol.getWidth()*0.15f/2);
            pistol.setY(rifle.getY() - pistol.getHeight()*0.15f - height *10/720 );
        }
    }

    private void initKnife (int width, int height){
        Texture tmp = new Texture(Gdx.files.internal("Knife_W[HUD].png"));
        TextureRegion image3 = new TextureRegion(tmp);
        knife = new Image(image3);
        knife.setScale(0.2f);
        if (Gdx.app.getType() == Android){
            knife.setX(width/2 - (tmp.getWidth() * 0.2f)/2);
            knife.setY(height * 30/720);
        }
        else{
            knife.setX(rifle.getX() + (rifle.getWidth()*0.15f)/2 - knife.getWidth()*0.2f/2);
            knife.setY(pistol.getY() - knife.getHeight()*0.15f- height *10/720 );
        }
    }

    private void initRed (int width){
        Texture tmp = new Texture(Gdx.files.internal("red2.png"));
        TextureRegion image1 = new TextureRegion(tmp);
        red = new Image(image1);
        red.setScale(0.15f);
        red.setX(width - red.getWidth()*0.15f);
        red.setY(rifle.getY());
    }

    private void initBB(){
        Texture temp;
        if (Gdx.app.getType() == Android){
            temp = new Texture(Gdx.files.internal("black bar rounded android.png"));
            TextureRegion image4 = new TextureRegion(temp);
            bb = new Image(image4);
            bb.setScale(0.15f);
            bb.setX(rifle.getX());
        }
        else{
            temp = new Texture(Gdx.files.internal("black bar[HUD].png"));
            TextureRegion image4 = new TextureRegion(temp);
            bb = new Image(image4);
            bb.setScale(0.2f);
            bb.setX(rifle.getX());
        }
    }

    private void initroundBox(int width, int height){
        Texture tmp = new Texture(Gdx.files.internal("box.png"));
        TextureRegion image = new TextureRegion(tmp);
        roundBox = new Image(image);
        roundBox.setScale(0.15f);
        roundBox.setX(width/2 - roundBox.getWidth()*0.15f*1.5f);
        roundBox.setY(height - roundBox.getHeight()*0.15f);
    }

    private void initroundLabel(int height){
        roundLabel = new Label("ROUND", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        roundLabel.setX(roundBox.getX() + (roundBox.getWidth() * 0.15f)/2 - roundLabel.getWidth()/2);
        roundLabel.setY(height - (roundBox.getHeight() * 0.15f)/3);
    }

    private void initroundNRLabel(int height){
        roundNRLabel = new Label("1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        roundNRLabel.setX(roundBox.getX() + (roundBox.getWidth()*0.15f)/2);
        roundNRLabel.setY(height - 2*(roundBox.getHeight() * 0.15f)/3);
    }

    private void initZombiesRoundBox(){
        Texture tmp = new Texture(Gdx.files.internal("box.png"));
        TextureRegion image = new TextureRegion(tmp);
        zombiesRoundBox = new Image(image);
        zombiesRoundBox.setScale(0.15f);
        zombiesRoundBox.setX(roundBox.getX() + roundBox.getWidth() * 0.15f);
        zombiesRoundBox.setY(roundBox.getY());
    }

    private void initZombiesRoundLabel(int height){
        zombiesRoundLabel = new Label("ZOMBIES ALIVE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        zombiesRoundLabel.setX(zombiesRoundBox.getX() + (zombiesRoundBox.getWidth() * 0.15f)/2 - zombiesRoundLabel.getWidth()/2);
        zombiesRoundLabel.setY(height - (zombiesRoundBox.getHeight() * 0.15f)/3);
    }

    private void initZombiesRoundNRLabel(int height){
        zombiesRoundNRLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        zombiesRoundNRLabel.setX(zombiesRoundLabel.getX() + (zombiesRoundLabel.getWidth() / 2));
        zombiesRoundNRLabel.setY(height - 2*(zombiesRoundBox.getHeight() * 0.15f)/3);
    }

    private void initEnemiesLeftBox(){
        Texture tmp = new Texture(Gdx.files.internal("box.png"));
        TextureRegion image = new TextureRegion(tmp);
        enemiesLeftBox = new Image(image);
        enemiesLeftBox.setScale(0.15f);
        enemiesLeftBox.setX(zombiesRoundBox.getX() + zombiesRoundBox.getWidth() * 0.15f);
        enemiesLeftBox.setY(roundBox.getY());
    }

    private void initEnemiesLeftLabel(int height){
        enemiesLeftLabel = new Label("ZOMBIES KILLED", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        enemiesLeftLabel.setX(enemiesLeftBox.getX() + (enemiesLeftBox.getWidth() * 0.15f)/2 - enemiesLeftLabel.getWidth()/2);
        enemiesLeftLabel.setY(height - (enemiesLeftBox.getHeight() * 0.15f)/3);
    }

    private void initEnemiesLeftNRLabel(int height) {
        enemiesLeftNRLabel = new Label("4", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        enemiesLeftNRLabel.setX(enemiesLeftLabel.getX() + (enemiesLeftLabel.getWidth() / 2));
        enemiesLeftNRLabel.setY(height - 2 * (enemiesLeftBox.getHeight() * 0.15f) / 3);
    }

    private void initBulletsLabel(int height){
        bulletsLabel = new Label("30/30", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        bulletsLabel.setX(rifle.getX() + rifle.getWidth()*0.225f/2);
        bulletsLabel.setY(height * 10/720);
    }

    private void initHealthLabel(int height){
        healthLabel = new Label("+ 100", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthLabel.setX(rifle.getX() + rifle.getWidth()*0.04f/2);
        healthLabel.setY(height * 10/720);
    }

    private void initHudAssets(int width, int height){
        initRifle(width, height);
        initPistol(width, height);
        initKnife(width, height);
        initRed(width);
        initBB();
        initBulletsLabel(height);
        initHealthLabel(height);
        initroundBox(width, height);
        initZombiesRoundBox();
        initEnemiesLeftBox();
        initroundLabel(height);
        initZombiesRoundLabel(height);
        initEnemiesLeftLabel(height);
        initroundNRLabel(height);
        initZombiesRoundNRLabel(height);
        initEnemiesLeftNRLabel(height);
    }

    public void update(String weapon){
        if (Gdx.app.getType() == Android){
            updateAndroid(weapon);
        }
        else{
            updateDesktop(weapon);
        }
    }

    private void updateAndroid(String weapon){
        rifle.remove();
        pistol.remove();
        knife.remove();
        if (weapon.equals("RIFLE")){
            stage.addActor(rifle);
        }
        if (weapon.equals("PISTOL")){
            stage.addActor(pistol);
        }
        if (weapon.equals("KNIFE")){
            stage.addActor(knife);
        }
    }

    public static Hud getInstance() {
        if (instance == null)
            return null;
        return instance;
    }


    private void updateDesktop(String weapon){
        if (weapon.equals("RIFLE")){
            red.setY(rifle.getY());
        }
        if (weapon.equals("PISTOL")){
            red.setY(pistol.getY());
        }
        if (weapon.equals("KNIFE")){
            red.setY(knife.getY());
        }
    }

    private void setBulletsLabel(String newBulletsLabel){
        bulletsLabel.setText(newBulletsLabel);
    }

    private void setRoundsNR(String newNRKills){
        roundNRLabel.setText(newNRKills);
    }

    private void setEnemiesLeftNR(String newNREnemiesLeft){
        enemiesLeftNRLabel.setText(newNREnemiesLeft);
    }

    //isto vai ser diferente acho!
    private void setZombiesRoundNRLabel(String newTime){
        zombiesRoundNRLabel.setText(newTime);
    }

    private void setHealthLabel(String newHealth){
        healthLabel.setText(newHealth);
    }

    private void setZombiesRoundLabel(String text){
            /* Float f = time;
            int min = f.intValue();

            f -= f.intValue();
            f *= 100;
            int seg = f.intValue();

            if (seg > 60) {
                setTime(min*1.00f + .60f);
                setZombiesRoundLabel(this.time);
                return;
            }

            if (seg >= 0 && seg < 10)
                this.zombiesRoundNRLabel.setText(min + ":0" + seg);
            else
                this.zombiesRoundNRLabel.setText(min + ":" + seg);

            if (seg == 0 && min == 0){
                this.doTime = false;
            }
            */
        zombiesRoundNRLabel.setText(text);
    }

    public void setTime(float time){
        this.time = time;
        this.doTime = true;
    }

    /**
     * Update the hud in order to make it show the updated information regarding the game state.
     */
    public void update(float delta, int WIDTH, int HEIGHT)
    {

        PlayerModel player = GameModel.getInstance().getSelfPlayer();

        //stateTime += delta * multp;


        spriteBL.setCenter(-70 + 70,-70 + 80);
        spriteTL.setCenter(-70 + 70, HEIGHT + 70 - 70);
        spriteTR.setCenter(WIDTH + 70 - 70, HEIGHT + 70 - 70);
        spriteBR.setCenter(WIDTH + 70 - 70, -70 + 70);

        spriteTC.setCenter(WIDTH/2, HEIGHT + 190 - 60);
        spriteBC.setCenter(WIDTH/2, -170 + 60);



        if(player.getHealth() <= 100 && player.getHealth() >= 0){
            spriteBL.setRegion(bloodAnimation.getKeyFrame(player.getHealth()/100 * bloodAnimation.getAnimationDuration(), false));
            spriteBR.setRegion(bloodAnimation.getKeyFrame(player.getHealth()/100 * bloodAnimation.getAnimationDuration(), false));
            spriteTL.setRegion(bloodAnimation.getKeyFrame(player.getHealth()/100 * bloodAnimation.getAnimationDuration(), false));
            spriteTR.setRegion(bloodAnimation.getKeyFrame(player.getHealth()/100 * bloodAnimation.getAnimationDuration(), false));
            if(player.getHealth() < 20){
                spriteTC.setRegion(bloodAnimation.getKeyFrame(player.getHealth()/100 * bloodAnimation.getAnimationDuration(), false));
                spriteBC.setRegion(bloodAnimation.getKeyFrame(player.getHealth()/100 * bloodAnimation.getAnimationDuration(), false));
            }
            else{
                spriteTC.setRegion(bloodAnimation.getKeyFrame(Float.MAX_VALUE, false));
                spriteBC.setRegion(bloodAnimation.getKeyFrame(Float.MAX_VALUE, false));
            }
        }





        if(player.getCurrentWeapon() == PlayerModel.Weapons.RIFLE)
            this.setBulletsLabel(  String.format("%d/%d", player.getRifleCurrentAmmo(), PlayerModel.MAX_RIFLE_AMMO ) );
        else if(player.getCurrentWeapon() == PlayerModel.Weapons.PISTOL)
            this.setBulletsLabel(  String.format("%d/%d", player.getPistolCurrentAmmo(), PlayerModel.MAX_PISTOL_AMMO ) );
        else // Knife
            this.setBulletsLabel("");

        Float f = player.getHealth();

        setHealthLabel("+ " + f.intValue());

        setRoundsNR("" + GameController.getInstance().getCurrentRound());
        setZombiesRoundLabel("" + GameModel.getInstance().getZombies().size());
        setEnemiesLeftNR("" + GameController.getInstance().getNrZombiedKilled());

    }

    public short handleController1(){
        short playerMovement = 0;
        if (Gdx.app.getType() == Android){
            if (controller1.isTouched()){
                double angle = Math.atan2(controller1.getKnobPercentX(), controller1.getKnobPercentY())*180/Math.PI;

                if (angle > -22.5 && angle < 22.5)
                    playerMovement |= GameController.PLAYER_MOVE_UP;
                else if (angle > -180 && angle < -157.5 || angle >= 157.5 && angle <= 180)
                    playerMovement |= GameController.PLAYER_MOVE_DOWN;
                else if (angle > -112.5 && angle < -67.5)
                    playerMovement |= GameController.PLAYER_MOVE_LEFT;
                else if (angle > 67.5 && angle < 112.5)
                    playerMovement |= GameController.PLAYER_MOVE_RIGHT;

                else if (angle >= 22.5 && angle <= 67.5){
                    playerMovement |= GameController.PLAYER_MOVE_UP;
                    playerMovement |= GameController.PLAYER_MOVE_RIGHT;
                }
                else if (angle >= 112.5 && angle <= 157.5){
                    playerMovement |= GameController.PLAYER_MOVE_DOWN;
                    playerMovement |= GameController.PLAYER_MOVE_RIGHT;
                }
                else if (angle >= -67.5 && angle <= -22.5){
                    playerMovement |= GameController.PLAYER_MOVE_UP;
                    playerMovement |= GameController.PLAYER_MOVE_LEFT;
                }
                else if (angle >= -157.5 && angle <= -112.5){
                    playerMovement |= GameController.PLAYER_MOVE_DOWN;
                    playerMovement |= GameController.PLAYER_MOVE_LEFT;
                }

            }
        }

        return  playerMovement;
    }

    public void handleController2(){
        if (Gdx.app.getType() == Android) {
            if (controller2.isTouched()) {
                double angle = Math.atan2(controller2.getKnobPercentX(), controller2.getKnobPercentY());
                angle *= -1;
                if (angle < 0)
                    angle += 2 * Math.PI;
                GameController.getInstance().getSelfPlayerBody().setAngle((float) (angle + Math.PI / 2));
            }
        }
    }

    /**
     * Draws the hud on the screen.
     */
    public void draw()
    {
        stage.draw();
    }

    /**
     * Draws the hud's sprite on the screen.
     */
    public void drawSprite(SpriteBatch batch)
    {
        spriteTL.draw(batch);
        spriteTR.draw(batch);
        spriteBL.draw(batch);
        spriteBR.draw(batch);
        spriteBC.draw(batch);
        spriteTC.draw(batch);
    }

}
