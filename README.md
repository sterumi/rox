<h1>rox</h1>

<h1>Rationale of ROX</h1>
ROX is a full system that includes systems. It is usable for networks with a discord or a teamspeak server and a game server.
It will send information to the main server and later it is readable via web or a external client.

<h2>Features</h2>
 - Switchable between Redis and MySql<br>
 - Discord Bot<br>
 - Full event system<br>
 - Full command system<br>
 - A http server to get information in JSON and a dynamic http content viewer<br>
 - Minecraft System to get information from a registered minecraft server with a own plugin<br>
 - A plugin system to develop own plugins for rox<br>
 - A teamspeak bot to get information from your teamspeak server<br>
 - A main server for a external client service<br>
 - A news system -> with http<br>
 - Lua Script Engine<br><br>
 

Currently is the client service in development.<br>
All information in files or internet is sent via JSON.

<h2>Planning Currently</h2>

<h3>###Systems</h3>
 - Client in C++

<h3>Bots</h3>
 - Arma 3 Addon<br>
 - CS:GO<br>
 - Factorio<br>
 - Avorion<br>
 - ARK<br>
 - CS:S<br>
 - Minecraft as mod<br>
 <h2></h2>
 
 <h3>Website Links</h3>
 - (domain)/get?ts                                   (To get your teamspeak server information)<br>
 - (domain)/get?gsUUID=(uuid)                        (Replace (uuid) with a registered uuid. If the game server is running, it will send information to the rox server and is readable as json string)<br>
 - (domain)/get?ds                                   (To get information from the discord bot)
 - (domain)/news                                     (To get the news as default json).<br>
 - (domain)/news?style=true                          (To get the default table style for easy web implementation).<br>
 - (domain)/news?style=true?customStyle=<fileName>   (To get a self-written css file for the table. The css files a searched in: <root>/http/css/).
 <br><br>
 To register a server you must add a entry in your database (gameserver). To get a uuid type /uuid in the console. The password is in SHA-256. You can create a password with /sha (text). It will return a sha 256 password.

 <br><br>
 You can add own websites. The server will ask for files in the http/ folder. If a index.html file was found, it will directly opened if the domain ends with no file name. Php is not currently included as engine.
 
 <h3>Contact</h3>
 Skype: bleikind11 <br>
 Steam: bleikind <br>
 YouTube: bleikind <br>
 Discord: bleikind#1100 <br>
 E-Mail: bleikindOnline@gmail.com <br>
 Twitch: bleikind <br>
 
<h3>Libraries using</h3>
 - Cassandra<br>
 - Commons-Collection<br>
 - JavaMP3<br>
 - JDA<br>
 - Jedis<br>
 - JNA<br>
 - JSON<br>
 - JSON-Simple<br>
 - MySql-Connector<br>
 - NV-WebSocket-Client<br>
 - OKHttp<br>
 - Okio<br>
 - SLF4J<br>
 - TeamSpeak3-API<br>
 - Trove4J<br>
 - LuaJ<br>
 
