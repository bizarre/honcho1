# Honcho

###### [wiki](https://github.com/joeleoli/honcho/wiki) | [issues](https://github.com/joeleoli/honcho/issues)
> Honcho is a [Bukkit](https://www.spigotmc.org/wiki/about-spigot/) command framework that speeds up
  command creation via annotations.
  
###### What can I do?
We're not reinventing the wheel, but here are a couple things that Honcho can do:
 * Easily create commands and subcommands using annotations.
 * Convert user input directly into objects to use in commands.
 * Automatically instantiate nested subcommands.
 * Dynamically generate command usage to display to user.
 
## Getting Started

### Installation

To use Honcho in your project, add this repository to your pom.xml:

```xml
<repository>
    <id>qrakn-repo</id>
    <url>http://hub.qrakn.com/nexus/repository/snapshots/</url>
</repository>
```

and then add this to your maven project's dependencies:

```xml
<dependency>
    <groupId>com.qrakn</groupId>
    <artifactId>honcho</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

### Usage
###### Note: Honcho requires at least Java 8 to make use of parameter names.

**Kotlin Example** - A command that broadcasts a message in chat (/broadcast \<message\>)
```kotlin
@CommandMeta(label = ["broadcast"])
class BroadcastCommand {

  fun execute(sender: CommandSender, message: String) {
    Bukkit.broadcastMessage("${ChatColor.AQUA}$message");
  }

}
```

**Java Example** - A command that broadcasts a message in chat (/broadcast \<message\>)
```java
@CommandMeta(label = "broadcast")
public class BroadcastCommand {

  public void execute(CommandSender sender, String message) {
    Bukkit.broadcastMessage(ChatColor.AQUA + message);
  }

}
```

**Result** (Honcho automatically generates command usage and converts String[] arguments into a single String)
> ![img](https://i.gyazo.com/15f0fc1f1af2f49dda1571a1a80e31ce.gif)
