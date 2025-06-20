# Code generated by ChatGPT (OpenAI), retrieved 13-06-2025

# Compile if any .java file is newer than its .class counterpart
needs_compile=false
for java_file in $(find src/main/java -name "*.java"); do
    class_file="bin/$(echo "$java_file" | sed 's|src/main/java/||; s|.java$|.class|')"
    if [ ! -f "$class_file" ] || [ "$java_file" -nt "$class_file" ]; then
        needs_compile=true
        break
    fi
done

# Compile if needed
if [ "$needs_compile" = true ]; then
    mkdir -p bin
    javac -d bin $(find src/main/java -name "*.java")
fi

# Run the program
java -cp bin nl.avans.Main "$@"
