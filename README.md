# admin-discord-bot
![](https://github.com/GBenoitG/secret-santa-diskord-bot/actions/workflows/allTest.yml/badge.svg)

Another discord bot wrote in kotlin, to manage Secret Santa participation via discord.

## Installation
### Build

Get sources
```shell script
git clone git@github.com:GBenoitG/secret-santa-diskord-bot.git  && cd secret-santa-diskord-bot/
```

Build sources
```shell script
docker build -t ssdb .
```

### Initialization
```shell script
docker run --rm -v $(pwd):/home/gradle/src ssdb run --args="init"
```
Edit the freshly created `properties.json` file ([seen here](#properties)) with your Discord Api Key, then your discord 
administrator, and what ever you need to edit.

### Run
```shell script
docker run --restart unless-stopped -d -v $(pwd):/home/gradle/src ssdb run
```

## Documentation

### Properties
There is an automatically generated `properties.json` during the [initialization](#initialization) : 
```json
{
    "token": "[YOUR_DISCORD_BOT_TOKEN]",
    "playing_at": "[THE_BOT_ACTIVITY]",
    "allowed_roles_list": [
        "[ANY_ROLE_NAME]",
        "Administrator"
    ],
    "allowed_users_id_list": [
        "[ANY_MEMBER_ID]"
    ],
    "language": "[LANGUAGE_TAG]"
}
```

* `token` put your own discord bot token [get from developer console](https://discord.com/developers/applications).
* `playing_at` write any sentence you want to display as activity of your bot on discord.

![](https://i.imgur.com/1e8MgJs.png)
* `allowed_roles_list` contains a list of names of roles that can use the bot.
* `allowed_users_id_list` same as before, but instead it's all ID from members who can use the bot.
* `language` select your locale language tag, like: _en-US_, _fr-FR_ (these are currently supported).

# License
Based on [GNU GPL v3.0](LICENSE).
