package dev.jasper.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import dev.jasper.game.sprites.dynamicSprites.Kid;

/**
 * The InputHandler class is responsible for handling user input to control the player character in the game.
 * It includes methods for handling player jump and run actions.
 *
 * @author Jasper Wang
 * @version 2024
 */
public final class InputHandler {
    private static final float INVINCIBLE_JUMP_VELOCITY = 2.2f;
    private static final float NORMAL_JUMP_VELOCITY = 3.2f;
    private static final float INVINCIBLE_RUN_VELOCITY = 0.04f;
    private static final float NORMAL_RUN_VELOCITY = 0.1f;
    private static final int ABSOLUTE_HORIZONTAL_VELOCITY = 2;
    private final Kid player;
    /**
     * Constructs an InputHandler instance.
     *
     * @param player The player character in the game.
     */
    public InputHandler(final Kid player) {
        this.player = player;
    }

    /**
     * Handles the user input for controlling the player character.
     */
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.getState() != Kid.State.JUMPING
                && player.getState() != Kid.State.FALLING) {

            handlePlayerJump();

        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                && player.getB2body().getLinearVelocity().x <= ABSOLUTE_HORIZONTAL_VELOCITY) {
            handlePlayerRun(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
                && player.getB2body().getLinearVelocity().x >= -ABSOLUTE_HORIZONTAL_VELOCITY) {
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
