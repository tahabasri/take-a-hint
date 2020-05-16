<img src="docs/images/logo/logo-with-title.png" alt="take-a-hint" height="200px">

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

<img src="docs/images/demo.hint.after.extra.png" width="700">