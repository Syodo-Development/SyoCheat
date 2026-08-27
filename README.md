# SyoCheat

SyoCheat is an anti-cheat plugin for **PowerNukkitX 3.0.0**. It monitors player movement and combat, records suspicious behavior, and can remove players from the server after they exceed the configured violation limits.

## Features

- Detects suspicious flying and movement speed
- Checks packet timing for timer-related cheats
- Detects unusually long combat reach
- Detects highly consistent aiming behavior
- Detects suspiciously consistent clicking
- Keeps a temporary violation history for every online player
- Automatically kicks players who exceed the configured limits
- Notifies staff when a player is kicked
- Provides a bypass permission for trusted players
- Includes a fully commented configuration file

## Requirements

- PowerNukkitX 3.0.0
- Java 21 or newer

No additional plugins or external services are required.

## Installation

1. Stop your PowerNukkitX server.
2. Download the latest `SyoCheat.jar` from the [GitHub releases page](https://github.com/Syodo-Development/SyoCheat/releases).
3. Place the JAR file in your server's `plugins` folder.
4. Start the server.
5. The plugin will create its configuration in `plugins/SyoCheat/config.yml`.
6. Stop the server before editing the configuration, then start it again to apply your changes.

## Commands

| Command | Description | Permission |
| --- | --- | --- |
| `/syocheat toggle` | Temporarily enables or disables all detection checks. | `syocheat.moderator` |

The toggle command only changes the current running session. To control whether SyoCheat is enabled after every restart, change `general.enabled` in `config.yml`.

## Permissions

| Permission | Description |
| --- | --- |
| `syocheat.moderator` | Allows the use of `/syocheat toggle`. |
| `syocheat.broadcast` | Receives a message when SyoCheat kicks a player. |
| `syocheat.bypass` | Prevents SyoCheat from kicking the player. Their accumulated violation history is cleared when a punishment threshold is reached. |

Permissions can be assigned with your preferred permissions plugin or through the permission system used by your server.

## Checks

SyoCheat currently includes the following detection methods:

| Check | What it monitors |
| --- | --- |
| Fly | Unexpected upward or level movement while the player is airborne. |
| Speed Auth | Movement reported through player authentication input packets. |
| Speed Timed | Distance travelled between periodically sampled locations. |
| Timer | Unusually fast packet timing. |
| Reach | The distance between an attacker and the target. |
| Aimlock | Unnaturally consistent attack angles across several targets or hits. |
| Autoclicker | Click rates that remain unusually high and consistent across multiple samples. |
| Hit Cooldown | Cancels hits that occur faster than the configured minimum interval. |

A single suspicious action does not necessarily cause an immediate kick. Checks add violation points to a temporary history. The player is kicked when either the total point limit or the repeated same-check limit is reached.

## Configuration

The generated `config.yml` explains every available option in English. The most important sections are:

- `general` controls the startup state and message prefix.
- `tasks` controls how often scheduled checks collect data.
- `violations` controls automatic kicking, punishment limits, and how long violations are remembered.
- `checks` contains the points and detection thresholds for each check.

Time options ending in `-ms` use milliseconds. Normally, 20 server ticks equal one second.

### Recommended setup

Start with the default values and observe normal players before making checks stricter. Network latency, low server performance, unusual movement mechanics, custom items, or other plugins can influence player movement and may cause false positives.

When adjusting the configuration:

1. Change only one or two values at a time.
2. Test with trusted players in different situations.
3. Keep a backup of your previous configuration.
4. Increase a detection threshold if legitimate players are being flagged.
5. Increase `violations.maximum-points` or `violations.maximum-same-check-count` if kicks happen too quickly.

Lower distance limits make movement and reach checks stricter. A higher Aimlock angle limit makes the Aimlock check stricter. Lower violation limits cause players to be kicked sooner.

## Troubleshooting

### Legitimate players are being kicked

- Temporarily disable detection with `/syocheat toggle`.
- Give trusted players `syocheat.bypass` while testing.
- Restore the default configuration or make the affected check less strict.
- Check whether the server is overloaded or experiencing network problems.
- Review movement-enhancing plugins and custom gameplay mechanics for conflicts.

### Configuration changes do not apply

Restart the server after editing `config.yml`. The `/syocheat toggle` command does not reload configuration values.

## Support and issues

If you find a bug, open an issue on the [SyoCheat GitHub repository](https://github.com/Syodo-Development/SyoCheat/issues). Include:

- Your PowerNukkitX version
- Your Java version
- The SyoCheat version
- Relevant console errors
- The affected configuration section
- Clear steps to reproduce the problem

## Important notice

No automated anti-cheat is perfect. Detection results should be treated as indicators, especially after changing thresholds or installing plugins that alter movement and combat. Test changes carefully before using strict settings on a public server.
