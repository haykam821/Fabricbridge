# Fabricbridge

[Matterbridge](https://github.com/42wim/matterbridge) support for the Minecraft client. It allows sending and recieving messages in a gateway, completely within the Minecraft client.

The server never receives the messages you send or receive, meaning it allows:

* Bypassing the server's filter
* Sharing information without the server's owners being able to access it.

Fabricbridge requires the [Fabric modloader](https://fabricmc.net/use/) and [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api).

## Installation

1. Install [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) if it is not installed.
2. Download Fabricbridge from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/fabricbridge/files) or [GitHub](https://github.com/haykam821/Fabricbridge/releases).
3. Place the downloaded file in your `mods` folder.

## Configuration

The configuration can be edited with the `config/matterbridge.json` file or using the [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu) mod. Descriptions for each configuration option are available in-game.

Fabricbridge requires a [Matterbridge](https://github.com/42wim/matterbridge) server to be running and acts as a relay. The Matterbridge API must be configured as documented [here](https://github.com/42wim/matterbridge/wiki/Api#configure-matterbridgetoml). The API details must be configured in Fabricbridge's configuration file as well.

## Usage

After configuring Fabricbridge, you can recieve and send messages.

To recieve messages, simply wait in-game for a message to be sent through the bridge. To send messages, use the `/fb <message>` command (with the message to send).
