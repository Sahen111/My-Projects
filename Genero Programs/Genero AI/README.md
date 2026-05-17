# Genero AI Assistant - Local Learning Version

A **completely FREE**, offline AI assistant that learns from your Genero code examples. No API keys needed, no internet required, no costs!

## 🎉 Features

🆓 **100% FREE** - No OpenAI API costs!
🚀 **Works Offline** - No internet connection needed
🧠 **Learns from YOU** - Train it with your own code examples
💾 **Persistent Memory** - Saves learned examples to file
💬 **Conversation Context** - Remembers previous questions
🔍 **Pattern Matching** - Finds similar code examples
🐛 **Code Analysis** - Debugging and explanation tools

## Prerequisites

- Python 3.8 or higher
- That's it! No API keys, no subscriptions needed!

## Installation

1. **Download/clone this repository**

2. **No dependencies to install** (No pip packages needed!)
   ```bash
   # This project has no external dependencies!
   # Everything is built-in Python
   ```

3. **Run it**:
   ```bash
   python main.py
   ```

## Quick Start

```bash
python main.py
```

Then interact with the AI:
- **Type a question**: "How do I create a function in Genero?"
- **Type 'train'**: Teach the AI with code examples
- **Type 'generate'**: Ask for code suggestions
- **Type 'debug'**: Debug your code
- **Type 'help'**: See all commands

## How It Works

### Training the AI (The Secret Sauce!)

The more examples you teach it, the smarter it gets:

```
1. Type: train
2. Choose a category (variables, functions, loops, etc.)
3. Describe what the code does
4. Paste your Genero code
5. AI learns and saves it!
```

### Example Training Session

```
📝 You: train

[Select category: functions]

Describe: Function that calculates factorial

Paste code:
FUNCTION calculate_factorial(n INTEGER) RETURNS INTEGER
  IF n <= 1 THEN
    RETURN 1
  ELSE
    RETURN n * calculate_factorial(n - 1)
  END IF
END FUNCTION

✅ Knowledge saved!
```

## Available Commands

| Command | Description |
|---------|-------------|
| `train` | Teach AI a code example |
| `ask <question>` | Ask about Genero |
| `generate` | Generate code from description |
| `debug` | Debug your code |
| `explain` | Explain what code does |
| `list` | Show all learned examples |
| `history` | View conversation history |
| `reset` | Clear conversation |
| `help` | Show all commands |
| `exit` | Exit program |

## Training Categories

- **variables** - Variable declarations and assignments
- **functions** - Function definitions and calls  
- **loops** - FOR and WHILE loops
- **database** - SELECT, INSERT, UPDATE, DELETE
- **arrays** - Array operations and access
- **forms** - UI forms and display
- **general** - Anything else

## Using as a Python Library

```python
from genero_ai import GeneroAIAssistant

# Initialize (completely free, no API key!)
assistant = GeneroAIAssistant()

# Train it
assistant.train(
    category="functions",
    description="Function that adds two numbers",
    code="""
FUNCTION add(a INTEGER, b INTEGER) RETURNS INTEGER
  RETURN a + b
END FUNCTION
"""
)

# Ask questions
response = assistant.ask("How do I create a function?")
print(response)

# Generate code
code = assistant.generate_code("Create a function that multiplies numbers")
print(code)

# Explain code
explanation = assistant.explain_code("MAIN\nLET x = 5\nEND MAIN")
print(explanation)

# Debug code
debug = assistant.debug_code("MAIN\nSELECT * FROM users\nEND MAIN")
print(debug)

# List all learned examples
print(assistant.list_learned_examples())
```

## Knowledge Storage

All learned examples are saved to `genero_knowledge.json` - a simple JSON file that persists between sessions. You can:

- Delete it to start fresh
- Share it with others
- Edit it manually if needed
- Backup your learned examples

## Example Workflow

```
1. Start the assistant:
   python main.py

2. Train with your favorite Genero patterns:
   - Type 'train'
   - Add 10-20 examples across categories
   - AI learns your coding style!

3. Ask questions and let it suggest code:
   "Show me how to create a cursor"
   "How do I validate input?"
   "Generate code for database insert"

4. It gets smarter with more training!
```

## Built-in Genero Knowledge

The assistant includes built-in knowledge of:
- Variables and data types
- Functions and procedures
- Loops (FOR, WHILE)
- Database operations (SELECT, INSERT, UPDATE, DELETE)
- Arrays and dynamic arrays
- Forms and UI
- String and numeric functions
- Date functions
- Error handling with TRY/CATCH
- Best practices

## Tips for Best Results

1. **Train with diverse examples** - Variety helps!
2. **Be descriptive** - Clear descriptions = better suggestions
3. **Train regularly** - Add examples as you learn new patterns
4. **Review learned examples** - Type 'list' to see what it knows
5. **Ask follow-up questions** - It remembers conversation context

## Troubleshooting

**"No examples found"**
- Train the AI with examples in that category
- Use 'list' to see what it's learned

**"Weird suggestions"**
- Teach it better examples
- Delete genero_knowledge.json and start fresh

**"Forgotten examples"**
- They're saved in genero_knowledge.json
- If that's deleted, they're gone (but you can retrain!)

## Project Structure

```
.
├── main.py                      # Interactive CLI
├── requirements.txt             # Empty (no dependencies!)
├── genero_knowledge.json        # Learned examples (auto-created)
├── README.md                    # This file
└── genero_ai/
    ├── __init__.py
    ├── ai_assistant.py          # Main AI class
    └── genero_knowledge.py      # Base knowledge
```

## Customization

You can customize the assistant by editing:

- **genero_knowledge.py** - Add more built-in Genero knowledge
- **ai_assistant.py** - Modify how it learns and responds
- **main.py** - Change the interface or add commands

## Future Enhancements

- Syntax highlighting for code
- Integration with Genero compiler
- Performance analysis
- Code templates library
- Multi-file project support
- Export learned examples

## License

Free to use for learning, development, and sharing!

---

**Enjoy learning Genero with your free AI! 🚀 No API costs, no internet needed!**
