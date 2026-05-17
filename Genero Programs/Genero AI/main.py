"""
Main entry point for the Genero AI Assistant.
Interactive command-line interface to chat with and train the AI about Genero coding.
100% FREE - Works completely offline with no API costs!
"""

from genero_ai import GeneroAIAssistant


def print_welcome():
    """Print welcome message and instructions."""
    print("\n" + "="*70)
    print("🧠 GENERO AI ASSISTANT - LOCAL LEARNING VERSION")
    print("="*70)
    print("\n🎉 100% FREE - No API key needed!")
    print("📚 Learns from YOUR code examples")
    print("🚀 Works completely offline\n")
    print("Available commands:")
    print("  help       - Show all commands")
    print("  train      - Teach the AI a code example")
    print("  ask        - Ask the AI a question")
    print("  generate   - Generate code from description")
    print("  debug      - Debug Genero code")
    print("  explain    - Explain code")
    print("  list       - Show all learned examples")
    print("  history    - Show conversation history")
    print("  reset      - Reset conversation")
    print("  exit       - Exit the assistant")
    print("="*70 + "\n")


def print_help():
    """Print detailed help information."""
    print("\n" + "="*70)
    print("AVAILABLE COMMANDS")
    print("="*70)
    
    help_text = """
train
  └─ Teach the AI by providing a code example
  └─ Categories: variables, functions, loops, database, arrays, forms, general
  
ask <question>
  └─ Ask the AI any question about Genero
  └─ Example: "How do I create a variable?"
  
generate
  └─ Ask the AI to generate code from description
  └─ It learns from your trained examples!
  
debug
  └─ Debug your Genero code
  └─ Pastes code, optionally provides error message
  
explain
  └─ Get the AI to explain what code does
  └─ Paste your code and get detailed explanation
  
list
  └─ See all code examples you've trained the AI with
  
history
  └─ View your conversation history with the AI
  
reset
  └─ Clear conversation history
  
exit
  └─ Exit the program

TRAINING THE AI:
================
The more examples you train the AI with, the better it gets!

1. Type 'train'
2. Choose a category (variables, functions, loops, etc.)
3. Describe what the code does
4. Paste the Genero code
5. AI learns and saves it!

The AI will then use your examples to answer questions and generate code.
    """
    print(help_text)
    print("="*70)


def get_multiline_input(prompt: str) -> str:
    """
    Get multiline input from user.
    User enters lines until they type 'DONE' on a new line.
    """
    print(prompt)
    print("(Type 'DONE' on a new line when finished)\n")
    lines = []
    while True:
        line = input()
        if line.strip().upper() == "DONE":
            break
        lines.append(line)
    return "\n".join(lines)


def train_ai(assistant: GeneroAIAssistant):
    """Train the AI with a new code example."""
    print("\n🎓 TRAIN THE AI")
    print("-" * 70)
    
    # Show available categories
    categories = ["variables", "functions", "loops", "database", "arrays", "forms", "general"]
    print("\nAvailable categories:")
    for i, cat in enumerate(categories, 1):
        print(f"  {i}. {cat}")
    
    # Get category choice
    while True:
        try:
            choice = input("\nSelect category (1-7): ").strip()
            category = categories[int(choice) - 1]
            break
        except (ValueError, IndexError):
            print("Invalid choice. Please enter 1-7.")
    
    # Get description
    description = input("\nDescribe what this code does: ").strip()
    
    if not description:
        print("❌ Description cannot be empty!")
        return
    
    # Get code
    code = get_multiline_input("\nPaste the Genero code:")
    
    if not code.strip():
        print("❌ Code cannot be empty!")
        return
    
    # Train
    assistant.train(category, description, code)


def main():
    """Main function to run the interactive AI assistant."""
    print("🚀 Initializing Genero AI Assistant...")
    assistant = GeneroAIAssistant()
    print("✅ Ready! (All learning is saved to genero_knowledge.json)\n")
    
    print_welcome()
    
    # Main interaction loop
    while True:
        try:
            user_input = input("📝 You: ").strip()
            
            if not user_input:
                continue
            
            # Handle commands
            if user_input.lower() == "exit":
                print("\n👋 Thanks for learning Genero with me! Goodbye!\n")
                break
            
            elif user_input.lower() == "help":
                print_help()
            
            elif user_input.lower() == "reset":
                assistant.reset_conversation()
            
            elif user_input.lower() == "train":
                train_ai(assistant)
            
            elif user_input.lower() == "list":
                print("\n")
                print(assistant.list_learned_examples())
                print()
            
            elif user_input.lower() == "history":
                history = assistant.get_conversation_history()
                if not history:
                    print("\n📋 No conversation history yet.\n")
                else:
                    print("\n📋 Conversation History:")
                    print("-" * 70)
                    for i, msg in enumerate(history, 1):
                        role = "You" if msg["role"] == "user" else "AI"
                        content = msg["content"][:100] + "..." if len(msg["content"]) > 100 else msg["content"]
                        print(f"{i}. [{role}]: {content}")
                    print("-" * 70 + "\n")
            
            elif user_input.lower().startswith("generate"):
                description = get_multiline_input(
                    "\n📝 Describe what code you want to generate:"
                )
                if description.strip():
                    print("\n🤖 Assistant:\n")
                    response = assistant.generate_code(description)
                    print(response)
                    print()
            
            elif user_input.lower().startswith("debug"):
                code = get_multiline_input("\n📝 Paste the Genero code to debug:")
                error = input("\n❓ Any error message? (press Enter to skip): ").strip()
                if code.strip():
                    print("\n🤖 Assistant:\n")
                    response = assistant.debug_code(code, error if error else None)
                    print(response)
                    print()
            
            elif user_input.lower().startswith("explain"):
                code = get_multiline_input("\n📝 Paste the Genero code to explain:")
                if code.strip():
                    print("\n🤖 Assistant:\n")
                    response = assistant.explain_code(code)
                    print(response)
                    print()
            
            elif user_input.lower().startswith("ask"):
                question = user_input[3:].strip()
                if not question:
                    question = input("❓ What's your question about Genero? ").strip()
                
                if question:
                    print("\n🤖 Assistant:\n")
                    response = assistant.ask(question)
                    print(response)
                    print()
            
            else:
                # Treat as a regular question
                print("\n🤖 Assistant:\n")
                response = assistant.ask(user_input)
                print(response)
                print()
                    
        except KeyboardInterrupt:
            print("\n\n⚠️  Operation cancelled.")
            continue
        except Exception as e:
            print(f"\n❌ Error: {str(e)}\n")
            continue


if __name__ == "__main__":
    main()
