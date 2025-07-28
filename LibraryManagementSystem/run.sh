#!/bin/bash

echo "Library Management System"
echo "========================"
echo ""

# Compile all Java files
echo "Compiling Java files..."
javac *.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo ""
    echo "Starting Library Management System..."
    echo "Default login credentials:"
    echo "  Username: admin, Password: admin123"
    echo "  Username: john, Password: password123"
    echo "  Username: sarah, Password: password456"
    echo ""
    java LibraryGUI
else
    echo "Compilation failed!"
    exit 1
fi 