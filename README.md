<img src="docs/images/logo/logo-with-title.png" alt="take-a-hint" height="200px">

[![Build Status](https://travis-ci.org/tahabasri/take-a-hint.svg?branch=master)](https://travis-ci.org/github/tahabasri/take-a-hint)
[![codecov](https://codecov.io/gh/tahabasri/take-a-hint/branch/master/graph/badge.svg)](https://codecov.io/gh/tahabasri/take-a-hint)
[![Follow @TheTahaBasri](https://img.shields.io/twitter/follow/TheTahaBasri.svg?style=social)](https://twitter.com/intent/follow?screen_name=TheTahaBasri) 


# take-a-hint
Give final users hints when getting error messages without the need of documentation.

## Introduction
Wouldn't be beneficial to give users hints about what went wrong with a 
failing command right away, without the need to open any documentation ?

take-a-hint is a tiny framework to change the look-and-feel of Java error messages. 
Giving the possibility to add hints on top of error message, final user can take-a-hint as quick as possible.

## How it works
Given the following example:
```java
public class Spaceship {
    public static void main(String[] args) {
        Spaceship main = new Spaceship();
        main.goToMars();
    }

    private void goToMars() {
        throw new IllegalStateException("Oxygen leak detected !");
    }
}
```
Typically, executing this will result in the stacktrace we are all familiar with:

<img src="docs/images/demo.hint.before.png" width="600">

Adding Hint is as simple as setting a Hint object using your primary class:
```java
public class Spaceship {
    public static void main(String[] args) {
        Spaceship main = new Spaceship();
        new HintCommand(main).init();
        main.goToMars();
    }
    // ...
}
```
Executing the same program again, we get a new look for our error message.

<img src="docs/images/demo.hint.after.png" width="600">

Going further more with Hint options, your final error message can be improved to something like this:

<img src="docs/images/demo.hint.after.extra.png" width="750">

### Use with Picocli

[Picocli](https://picocli.info/) is a one-file framework for creating Java command line applications with almost zero code.

In order to add take-a-hint features to your Picocli application, you need to use Hint custom handlers.
```java
@CommandLine.Command
@Hint // this is optional
public class PicocliWithHint implements Runnable {

    @CommandLine.Parameters
    int apiRating;

    public static void main(String[] args) {
        PicocliWithHint picocliWithHint = new PicocliWithHint();
        CommandLine cmd = new CommandLine(picocliWithHint);
        HintCommand hintCmd = new HintCommand(picocliWithHint);
        // add custom handler for parameter exceptions
        cmd.setParameterExceptionHandler(new PicocliParameterExceptionHandler(hintCmd));
        // add custom handler for all other exceptions
        cmd.setExecutionExceptionHandler(new PicocliExecutionExceptionHandler(hintCmd));
        System.exit(cmd.execute(args));
    }

    @Override
    public void run() {
        if (apiRating < 3) {
            throw new RuntimeException("Not cool !");
        }
    }
}
```

After wiring Hint with Picocli, exceptions will be shown with Hint style.

- When running CLI without required parameter `apiRating`, we get:

<img src="docs/images/demo.picocli.noparam.png" width="600">

- When running CLI with incompatible parameter for `apiRating`, we get:

<img src="docs/images/demo.picocli.wrongparam.png" width="900">

### How to use your own Emoji characters in your Java command-line application

take-a-hint comes with pre-defined messages with a set of Emojis, you can change yours via the configuration:

- via annotations, you can set your default prefix for error messages :
```java
import io.hint.annotation.Hint;

@Hint(errorPrefix = "\u26D4 error :")
class Spaceship{
}
```

Your final message will be 
```
⛔ error :   Spaceship is no longer a ship!
```

- via programmatic API, you can achieve the same result via the following method:
```java
import io.hint.HintCommand;

class Spaceship{
    public static void main(String[] args){
      new HintCommand(new Main()).errorPrefix("\u26D4 error :").init();
    }
}
```

To use a new Emoji, you can follow the steps bellow:
- Visit [Full Emoji List, v13.0](https://unicode.org/emoji/charts/full-emoji-list.html) and find emoji to use.
- Copy the code in `Code` column and make a search in 
[fileformat.info](http://www.fileformat.info/info/unicode/char/search.htm) using the copied code as query string.
- Click on the returned result (end of page) and copy the value for column `C/C++/Java source code`.
- Put the value in your prefix string and recompile your program, it should show the new Emoji.