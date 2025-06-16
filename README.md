# Tweezy FSM Interpreter

## Getting Started

This project is a Java-based interpreter for state diagrams written in a custom `.fsm` syntax. It can display the parsed state machine in the console or generate a PlantUML diagram.

### Prerequisites

- **Java 21** or higher (ensure `java -version` outputs at least version 21)
- **Maven** (for building the project)
- **Git Bash** (recommended for running the provided scripts on Windows)

### Setup

1. **Clone the repository**
   ```sh
   git clone git@github.com:LarsDevans/tweezy.git
   cd tweezy
   ```

2. **Build the project**
   Use Maven to compile the project:
   ```sh
   mvn clean package
   ```

3. **(Optional) Make the run script executable**
   On Linux/macOS:
   ```sh
   chmod +x run.sh
   ```

### Usage

To run the program, use the provided `run.sh` script:

```sh
./run.sh [filepath] [displaytype]
```

- `[filepath]`: Path to the `.fsm` file containing the state diagram.
  - On Windows, use double backslashes (`\\`) in the path.
  - If the path contains spaces, wrap it in double quotes (`"[filepath]"`).

- `[displaytype]`: Output format.
  - `console` — displays the state diagram in the terminal.
  - `puml` — generates a PlantUML diagram image in the project root.

#### Example

```sh
./run.sh "src\\main\\resources\\example_lamp.fsm" console
```

This command will parse the `example_lamp.fsm` file and display the state diagram in the console.

### Notes

- The `run.sh` script will rebuild and run the project automatically.
- All `.fsm` example files are located in `src/main/resources/`.
