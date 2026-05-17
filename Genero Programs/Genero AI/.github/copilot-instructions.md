# Genero AI Assistant - Local Learning Version

## Project Overview
A completely FREE, offline AI assistant specialized in Genero 4GL programming language. Learns from YOUR code examples - no API keys, no costs, no internet needed!

## Project Structure
```
.
├── main.py                          # Interactive CLI entry point
├── requirements.txt                 # Empty (no dependencies!)
├── genero_knowledge.json            # Learned examples (auto-created)
├── README.md                        # Project documentation
├── genero_ai/
│   ├── __init__.py                 # Package initialization
│   ├── ai_assistant.py             # Local AI learning class
│   └── genero_knowledge.py         # Genero language knowledge base
└── .github/
    └── copilot-instructions.md     # This file
```

## Getting Started

### 1. Prerequisites
- Python 3.8 or higher
- That's it! No API keys needed!

### 2. Installation
```bash
# Just run it:
python main.py

# No pip install needed - no external dependencies!
```

### 3. Using the Application
```bash
python main.py
```

Then:
- Type questions: "How do I create a variable?"
- Type 'train': Teach it your Genero code
- Type 'generate': Ask for code suggestions
- Type 'help': See all commands

## How It Works

### Key Features

**Local Learning System**
- Train with your own Genero code examples
- AI learns patterns from your code
- Saves learned examples to `genero_knowledge.json`
- Works 100% offline, no internet needed

**Pattern Matching**
- Finds similar code examples when you ask
- Uses string similarity to match queries
- Better matches as you train more examples

**Code Analysis**
- Detects common coding issues
- Analyzes code structure
- Explains what code does

## Commands

| Command | Purpose |
|---------|---------|
| `train` | Train AI with code examples |
| `ask` | Ask about Genero |
| `generate` | Generate code from description |
| `debug` | Debug your code |
| `explain` | Explain code |
| `list` | Show learned examples |
| `history` | View conversation |
| `reset` | Clear conversation |
| `help` | Show all commands |
| `exit` | Exit program |

## Training the AI

```
1. Type: train
2. Select category (variables, functions, loops, database, arrays, forms, general)
3. Describe: "What does this code do?"
4. Paste: Your Genero code
5. AI learns and saves it!
```

Example:
```
Category: functions
Description: Function that calculates sum of numbers
Code:
  FUNCTION sum_numbers(n INTEGER) RETURNS INTEGER
    LET total = 0
    FOR i = 1 TO n
      LET total = total + i
    END FOR
    RETURN total
  END FUNCTION
```

## Development

### Key Modules

**genero_ai/ai_assistant.py**
- `GeneroAIAssistant`: Main AI class (100% local, no API calls)
- Methods: `ask()`, `train()`, `generate_code()`, `debug_code()`, `explain_code()`
- Uses local knowledge and learned examples

**genero_ai/genero_knowledge.py**
- Genero language knowledge base
- `get_genero_knowledge()`: Built-in Genero knowledge
- `search_knowledge()`: Keyword-based search

**main.py**
- Interactive CLI
- Training interface
- Command processing

### Using as a Library

```python
from genero_ai import GeneroAIAssistant

# Initialize (no API key!)
assistant = GeneroAIAssistant()

# Train it
assistant.train(
    category="functions",
    description="Add two numbers",
    code="FUNCTION add(a INT, b INT) RETURNS INT\nRETURN a + b\nEND FUNCTION"
)

# Ask questions
print(assistant.ask("How do I create a function?"))

# Generate code
print(assistant.generate_code("Create a function to multiply"))

# See what it learned
print(assistant.list_learned_examples())
```

## Features

✅ 100% FREE - No API costs!
✅ Works OFFLINE - No internet needed!
✅ Learns from YOUR code examples
✅ Persistent memory (saves to file)
✅ Conversation context remembered
✅ Code debugging and analysis
✅ Interactive CLI with multiple commands
✅ No external dependencies

## Built-in Knowledge

The assistant includes knowledge of:
- Variables and data types
- Functions and procedures
- Control flow (IF, FOR, WHILE)
- Database operations (SELECT, INSERT, UPDATE, DELETE)
- Arrays and records
- Forms and UI
- String and numeric functions
- Date operations
- Error handling (TRY/CATCH)
- Best practices

## Knowledge Storage

Learned examples are saved to `genero_knowledge.json`:
- Persists between sessions
- Simple JSON format (can be edited manually)
- Can be deleted to start fresh
- Can be shared with others

## Tips for Best Results

1. **Train with examples** - More examples = better suggestions
2. **Be descriptive** - Clear descriptions help matching
3. **Use all categories** - Diverse training improves learning
4. **Review learned examples** - Use 'list' to see knowledge
5. **Ask follow-ups** - AI remembers conversation context

## Troubleshooting

**"No examples found"**
- Train more examples in that category
- Type 'list' to see current knowledge

**"Bad suggestions"**
- Teach it better examples
- Delete `genero_knowledge.json` to start fresh

## Dependencies

**None!** This project has:
- ✅ No pip packages required
- ✅ No external API calls
- ✅ No internet needed
- ✅ Pure Python implementation

## Future Enhancements
- Syntax highlighting
- Genero compiler integration
- Code templates
- Multi-file projects
- Performance analysis
- Export learned examples

## License
Open source and FREE for all uses!

---

**Enjoy learning Genero totally free! No costs, no API keys, no internet! 🚀**
