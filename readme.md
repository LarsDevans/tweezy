### Getting started
To run the program, a single command is used to build and run the program with the proper arguments. Make sure to **use Git Bash** to execute the commands in this script, as it relies on Git Bash-specific features.\
```./run.sh [filepath] [displaytype]```

`./run.sh` refers to a file containing all logic required to rebuild the project upon code changes and execute the project. It was added to simplify the launch process of the application.

`[filepath]` is the argument used to locate the `.fsm` file that contains the State Diagram syntax. All `\` characters in the filepath should be `\\`. When a space character is present in the filepath, make sure to encase the filepath in double quotes (`"[filepath]"`).

`[displaytype]` refers to the method you want your State Diagram displayed. The options are `console` and `plantuml`.
- `console` - displays the State Diagram in the console
- `plantuml` - creates an image of the State Diagram in the root folder of the project using PlantUML.

**Example**
An example of the command using the `example_lamp.fsm` file and display method `console` looks like the following:\
```./run.sh C:\\Users\\sjulg\\Documents\\GitHub\\tweezy\\src\\main\\resources\\example_lamp.fsm console```