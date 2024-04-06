package dev.jasper.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import dev.jasper.game.sprites.dynamicSprites.Kid;

public final class InputHandler {
    private static final float INVINCIBLE_JUMP_VELOCITY = 2.2f;
    private static final float NORMAL_JUMP_VELOCITY = 3.2f;
    private static final float INVINCIBLE_RUN_VELOCITY = 0.04f;
    private static final float NORMAL_RUN_VELOCITY = 0.1f;
    private Kid player;
    public InputHandler(Kid player) {
        this.player = player;
    }

    /**
     * Handles the user input for controlling the player character.
     * @param dt - delta time
     */
    public void handleInput(final float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.getState() != Kid.State.JUMPING
                && player.getState() != Kid.State.FALLING) {

            handlePlayerJump();

        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                && player.getB2body().getLinearVelocity().x <= 2) {
            handlePlayerRun(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
                && player.getB2body().getLinearVelocity().x >= -2) {
            handlePlayerRun(false);
        }
    }

    private void handlePlayerJump() {
        final float jumpVelocity;
        if (player.getIsInvincibleToEnemy()) {
            jumpVelocity = INVINCIBLE_JUMP_VELOCITY;
        } else {
            jumpVelocity = NORMAL_JUMP_VELOCITY;
        }

        player.getB2body().applyLinearImpulse(new Vector2(0, jumpVelocity),
                player.getB2body().getWorldCenter(), true);
    }

    private void handlePlayerRun(final boolean isRunningRight) {
        float speed;
        if (player.getIsInvincibleToEnemy()) {
            speed = INVINCIBLE_RUN_VELOCITY;
        } else {
            speed = NORMAL_RUN_VELOCITY;
        }

        if (!isRunningRight) {
            speed = speed * -1;
        }

        player.getB2body().applyLinearImpulse(new Vector2(speed, 0),
                player.getB2body().getWorldCenter(), true);
    }
}
