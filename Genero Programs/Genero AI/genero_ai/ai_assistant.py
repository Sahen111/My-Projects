"""
Genero AI Assistant - Local, Trainable Version
A completely free AI that learns from code examples you provide.
No API keys, no internet required - works 100% offline!
"""

import json
import os
from typing import Optional, Dict, List
from .genero_knowledge import get_genero_knowledge, search_knowledge


class GeneroAIAssistant:
    """Local AI Assistant specialized in Genero coding language."""
    
    def __init__(self, knowledge_file: str = "genero_knowledge.json"):
        """
        Initialize the Genero AI Assistant (completely local).
        
        Args:
            knowledge_file: File to store learned code examples
        """
        self.knowledge_file = knowledge_file
        self.genero_knowledge = get_genero_knowledge()
        
        # Load or initialize the learned examples database
        self.code_examples: Dict[str, List[Dict]] = self._load_knowledge()
        
        # Conversation history for context
        self.conversation_history = []
    
    def _load_knowledge(self) -> Dict:
        """Load learned code examples from file."""
        if os.path.exists(self.knowledge_file):
            try:
                with open(self.knowledge_file, 'r') as f:
                    return json.load(f)
            except:
                return self._create_default_knowledge()
        return self._create_default_knowledge()
    
    def _create_default_knowledge(self) -> Dict:
        """Create default knowledge structure."""
        return {
            "variables": [],
            "functions": [],
            "loops": [],
            "database": [],
            "arrays": [],
            "forms": [],
            "general": []
        }
    
    def _save_knowledge(self):
        """Save learned code examples to file."""
        with open(self.knowledge_file, 'w') as f:
            json.dump(self.code_examples, f, indent=2)
        print("\n✅ Knowledge saved!")
    
    def _string_similarity(self, s1: str, s2: str) -> float:
        """Calculate simple string similarity (0-1)."""
        s1, s2 = s1.lower(), s2.lower()
        if len(s1) == 0 or len(s2) == 0:
            return 0.0
        
        words1 = set(s1.split())
        words2 = set(s2.split())
        
        if len(words1) == 0 or len(words2) == 0:
            return 0.0
            
        intersection = words1.intersection(words2)
        union = words1.union(words2)
        
        return len(intersection) / len(union) if union else 0.0
    
    def _find_relevant_examples(self, query: str, limit: int = 3) -> List[Dict]:
        """Find code examples relevant to a query."""
        all_examples = []
        
        # Collect all examples
        for category, examples in self.code_examples.items():
            all_examples.extend(examples)
        
        # Score and sort by relevance
        scored = [
            (example, self._string_similarity(query, example.get("description", "")))
            for example in all_examples
        ]
        
        scored.sort(key=lambda x: x[1], reverse=True)
        
        # Return top matches
        return [example for example, score in scored[:limit] if score > 0.1]
    
    def train(self, category: str, description: str, code: str):
        """
        Train the AI by providing code examples.
        
        Args:
            category: Category (variables, functions, loops, database, arrays, forms, general)
            description: What this code does
            code: The Genero code example
        """
        if category not in self.code_examples:
            self.code_examples[category] = []
        
        example = {
            "description": description,
            "code": code
        }
        
        self.code_examples[category].append(example)
        self._save_knowledge()
        print(f"✅ Learned: {description}")
    
    def ask(self, question: str) -> str:
        """
        Ask the AI a question about Genero.
        It will search learned examples and base knowledge.
        
        Args:
            question: Question about Genero coding
            
        Returns:
            Assistant's response based on learned knowledge
        """
        self.conversation_history.append({
            "role": "user",
            "content": question
        })
        
        # Find relevant examples
        examples = self._find_relevant_examples(question)
        
        # Build response
        response = self._generate_response(question, examples)
        
        self.conversation_history.append({
            "role": "assistant",
            "content": response
        })
        
        return response
    
    def _generate_response(self, question: str, examples: List[Dict]) -> str:
        """Generate a response based on learned examples and knowledge."""
        response = ""
        
        # Check for specific keywords
        question_lower = question.lower()
        
        if "variable" in question_lower or "declare" in question_lower:
            response = self._answer_about_variables(question, examples)
        elif "function" in question_lower:
            response = self._answer_about_functions(question, examples)
        elif "loop" in question_lower or "for" in question_lower or "while" in question_lower:
            response = self._answer_about_loops(question, examples)
        elif "array" in question_lower:
            response = self._answer_about_arrays(question, examples)
        elif "database" in question_lower or "select" in question_lower or "insert" in question_lower:
            response = self._answer_about_database(question, examples)
        elif "form" in question_lower or "display" in question_lower:
            response = self._answer_about_forms(question, examples)
        else:
            response = self._generate_general_response(question, examples)
        
        # Add relevant examples if found
        if examples:
            response += "\n\n📚 Similar Examples from Your Training:"
            for i, ex in enumerate(examples, 1):
                response += f"\n\n{i}. {ex['description']}:"
                response += f"\n```genero\n{ex['code']}\n```"
        
        return response
    
    def _answer_about_variables(self, question: str, examples: List[Dict]) -> str:
        """Answer questions about variables."""
        return """In Genero, variables are declared using the DEFINE statement:

```genero
DEFINE variable_name data_type
DEFINE v_count INTEGER
DEFINE v_name VARCHAR(50)
DEFINE v_balance DECIMAL(10,2)
```

Common data types:
- INTEGER: Whole numbers
- VARCHAR(n): Text up to n characters
- DECIMAL(p,s): Decimal with p total digits, s decimal places
- DATE: Calendar date
- BOOLEAN: TRUE or FALSE
- CHAR(n): Fixed-length text"""
    
    def _answer_about_functions(self, question: str, examples: List[Dict]) -> str:
        """Answer questions about functions."""
        return """In Genero, functions are defined with FUNCTION keyword:

```genero
FUNCTION function_name(param1 TYPE, param2 TYPE) RETURNS RETURN_TYPE
  -- Function body
  RETURN result
END FUNCTION
```

Example:
```genero
FUNCTION add_numbers(a INTEGER, b INTEGER) RETURNS INTEGER
  RETURN a + b
END FUNCTION
```

To call a function:
```genero
LET result = add_numbers(5, 3)
DISPLAY result  -- Displays 8
```"""
    
    def _answer_about_loops(self, question: str, examples: List[Dict]) -> str:
        """Answer questions about loops."""
        return """Genero supports different loop types:

FOR loop (fixed iterations):
```genero
FOR i = 1 TO 10
  DISPLAY "Count: ", i
END FOR
```

WHILE loop (condition-based):
```genero
WHILE counter < 100
  LET counter = counter + 1
END WHILE
```

Exit or Continue:
```genero
FOR i = 1 TO 10
  IF i = 5 THEN
    EXIT FOR
  END IF
  DISPLAY i
END FOR
```"""
    
    def _answer_about_arrays(self, question: str, examples: List[Dict]) -> str:
        """Answer questions about arrays."""
        return """Genero uses DYNAMIC ARRAY for flexible arrays:

```genero
DEFINE arr DYNAMIC ARRAY OF RECORD
  id INTEGER,
  name VARCHAR(50),
  salary DECIMAL(10,2)
END RECORD
```

Accessing array elements:
```genero
LET arr[1].id = 1
LET arr[1].name = "John Doe"
LET arr[1].salary = 5000.00

DISPLAY arr[1].name  -- Shows "John Doe"
```

Array size:
```genero
LET size = arr.getLength()
```"""
    
    def _answer_about_database(self, question: str, examples: List[Dict]) -> str:
        """Answer questions about database operations."""
        return """Basic database operations in Genero:

SELECT (retrieve data):
```genero
SELECT id, name, salary INTO v_id, v_name, v_salary
FROM employees 
WHERE id = 100
```

INSERT (add data):
```genero
INSERT INTO employees (id, name, salary)
VALUES (123, 'Jane Smith', 6000)
```

UPDATE (modify data):
```genero
UPDATE employees 
SET salary = 7000 
WHERE id = 123
```

DELETE (remove data):
```genero
DELETE FROM employees 
WHERE id = 123
```"""
    
    def _answer_about_forms(self, question: str, examples: List[Dict]) -> str:
        """Answer questions about forms and UI."""
        return """Working with forms in Genero:

Open form:
```genero
OPEN FORM employee_form FROM "employee_form.frm"
```

Display form:
```genero
DISPLAY FORM employee_form
```

Input from user:
```genero
INPUT BY NAME v_name, v_salary
```

Handle button clicks:
```genero
ON ACTION save
  -- Handle save action
  DISPLAY "Data saved"
END ON
```"""
    
    def _generate_general_response(self, question: str, examples: List[Dict]) -> str:
        """Generate a general response."""
        return f"""❓ Question: {question}

I'm a local AI learning from your code examples. Here's what I know about Genero:

**Main Program Structure:**
```genero
MAIN
  -- Your code here
END MAIN
```

**To teach me:**
Use the 'train' command to add code examples, and I'll learn to answer your questions better!

**Quick Tips:**
- Use DEFINE to declare variables
- Use FUNCTION to create reusable code
- Use FOR/WHILE for loops
- Use TRY/CATCH for error handling
- Comments use -- for single line or /* */ for multi-line"""
    
    def generate_code(self, description: str) -> str:
        """
        Generate Genero code based on description.
        
        Args:
            description: What you want to code
            
        Returns:
            Generated code or suggestion
        """
        examples = self._find_relevant_examples(description)
        
        response = f"📝 Generating code for: {description}\n\n"
        
        if examples:
            response += "Based on your training data, here's a similar example:\n\n"
            response += f"```genero\n{examples[0]['code']}\n```\n\n"
            response += "✏️ You can modify the above code for your needs!"
        else:
            response += "No similar examples found in training data.\n\n"
            response += "Here's a basic template:\n\n"
            response += """```genero
MAIN
  DEFINE v_var1 INTEGER
  DEFINE v_var2 VARCHAR(50)
  
  -- Your code here
  
END MAIN
```

👉 Tip: Train me with examples so I can suggest better code!"""
        
        return response
    
    def debug_code(self, code: str, error: Optional[str] = None) -> str:
        """
        Debug Genero code (pattern matching).
        
        Args:
            code: The code to debug
            error: Error message if available
            
        Returns:
            Debugging suggestions
        """
        suggestions = []
        
        # Check for common issues
        issues = self._find_code_issues(code)
        
        response = "🔍 Code Analysis:\n"
        
        if error:
            response += f"\n❌ Error: {error}\n"
        
        if issues:
            response += "\n⚠️ Potential Issues Found:\n"
            for issue in issues:
                response += f"• {issue}\n"
        else:
            response += "\n✅ No obvious syntax issues detected.\n"
        
        response += "\n💡 Tips:\n"
        response += "• Make sure all variables are DEFINED before use\n"
        response += "• Check that all END statements match their start (IF/END IF, FOR/END FOR, etc.)\n"
        response += "• Ensure proper indentation for readability\n"
        response += "• Use meaningful variable names\n"
        
        return response
    
    def _find_code_issues(self, code: str) -> List[str]:
        """Find potential issues in code."""
        issues = []
        code_lower = code.lower()
        
        # Check for common patterns
        if "select" in code_lower and "into" not in code_lower:
            issues.append("SELECT without INTO clause - where will results go?")
        
        if code.count("IF") != code.count("END IF"):
            issues.append("Mismatched IF/END IF statements")
        
        if code.count("FOR") != code.count("END FOR"):
            issues.append("Mismatched FOR/END FOR statements")
        
        if code.count("WHILE") != code.count("END WHILE"):
            issues.append("Mismatched WHILE/END WHILE statements")
        
        return issues
    
    def explain_code(self, code: str) -> str:
        """
        Explain what code does (pattern analysis).
        
        Args:
            code: The code to explain
            
        Returns:
            Code explanation
        """
        response = "📖 Code Explanation:\n\n"
        
        # Analyze code structure
        lines = code.strip().split('\n')
        response += f"Lines of code: {len(lines)}\n\n"
        
        # Check for key statements
        code_lower = code.lower()
        
        response += "**Key Components Found:**\n"
        if "define" in code_lower:
            response += "• Variable declarations (DEFINE)\n"
        if "select" in code_lower:
            response += "• Database query (SELECT)\n"
        if "insert" in code_lower:
            response += "• Insert operation (INSERT)\n"
        if "for" in code_lower or "while" in code_lower:
            response += "• Loop structure\n"
        if "if" in code_lower:
            response += "• Conditional logic (IF)\n"
        if "function" in code_lower:
            response += "• Function definition\n"
        
        response += "\n**Code Flow:**\n"
        response += code + "\n"
        
        return response
    
    def list_learned_examples(self) -> str:
        """List all learned code examples."""
        response = "📚 Current Knowledge Base:\n\n"
        
        total = 0
        for category, examples in self.code_examples.items():
            count = len(examples)
            total += count
            response += f"**{category.upper()}**: {count} example(s)\n"
            for ex in examples:
                response += f"  • {ex['description']}\n"
        
        response += f"\nTotal: {total} learned examples"
        return response
    
    def reset_conversation(self):
        """Reset the conversation history."""
        self.conversation_history = []
        print("✅ Conversation history cleared.")
    
    def get_conversation_history(self):
        """Get the current conversation history."""
        return self.conversation_history
