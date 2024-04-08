package dev.jasper.game.sprites.dynamicSprites;


public abstract class AbstractPlayer extends DynamicB2BodySprite implements SnowballCarrier, InteractableWithEnemy{
    public AbstractPlayer(final short collisionCategory, final short maskBits) {
        super(collisionCategory, maskBits);
    }

    public abstract boolean getIsInvincibleToEnemy();

}
