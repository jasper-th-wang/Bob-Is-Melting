# Bob Is Melting

A 2D platformer game where you must prevent Bob the Snowman from melting away. Collect snowballs scattered across the map and bring them back to Bob to restore his health before time runs out.

## Game Features

- **Time pressure**: Bob's health decreases constantly (like a real snowman melting)
- **Snowball collection**: Gather snowballs to restore Bob's health
- **Enemy encounters**: Avoid Bears and Chickens that chase you around the map
- **Invincibility system**: Brief invincibility after being hit by enemies
- **Physics-based gameplay**: Built with Box2D physics engine for realistic movement
- **Diverse enemy AI**: Each enemy type (Bear, Chicken) has unique movement patterns and behaviors
- **Level design with Tiled**: Custom maps created using Tiled Map Editor with multiple layers
- **Sprite atlases**: Efficient sprite management using TextureAtlas for character animations

## Gameplay

**Objective**: Keep Bob alive by collecting snowballs and delivering them before he melts completely.

**Controls**: Use arrow keys (Up, Left, Right) to move around the map.

**Mechanics**:
- Collect snowballs by walking into them
- Carry snowballs back to Bob to restore his health
- Avoid enemy creatures (Bears, Chickens) that will knock you down
- After being hit, you become temporarily invincible

## Platforms

Supports **Desktop** and **Web (HTML5)** platforms.

## Tech Stack

- **Java 21** (OpenJDK)
- **libGDX 1.12.1** - Cross-platform game development framework
- **Box2D** - 2D physics engine
- **Tiled Map Editor** - Level design and tilemap creation
- **Gradle 8.5** - Build automation

## Architecture

- **Template Pattern**: Customizable enemy movement mechanics through `AbstractEnemy` base class
- **Factory Pattern**: Efficient object creation with `enemyFactory()` methods for Bears and Chickens
- **Singleton Pattern**: Centralized game entities (Bob, Kid) with controlled instantiation
- **Tiled Integration**: Maps created in Tiled Map Editor (`.tmx` files) with multiple layers:
  - Background layers (sky, clouds, trees)
  - Collision objects for ground and enemy boundaries  
  - Object layers for snowball spawn points

## Getting Started

### Prerequisites

- Java OpenJDK 21.0.2+
- Gradle 8.5+

### Running the Game

**Desktop version:**
```sh
./gradlew desktop:run
```

**Web version:**
```sh
./gradlew html:superdev
```
Then navigate to http://127.0.0.1:8080/

### Building

**Create web distribution:**
```sh
./gradlew html:dist
```

## Documentation

Additional project documentation can be found in the `/documentation/` folder:
- UML diagrams
- Project mindmaps
