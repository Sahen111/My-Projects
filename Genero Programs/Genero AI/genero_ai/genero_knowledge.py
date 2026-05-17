"""
Genero Language Knowledge Base
Contains comprehensive information about the Genero 4GL language.
"""

GENERO_KNOWLEDGE = """
# Genero 4GL Language Knowledge Base

## Overview
Genero is a modern 4GL (fourth-generation language) developed by Four Js (formerly Fourj Software).
It's used for rapid development of business applications with a focus on user interface and database interaction.

## Core Language Features

### 1. Variables and Data Types
- SMALLINT: Small integer (-32768 to 32767)
- INTEGER: Standard integer
- BIGINT: Large integer
- DECIMAL(p,s): Precise decimal number
- FLOAT, DOUBLE: Floating point numbers
- CHAR(n): Fixed-length string
- VARCHAR(n): Variable-length string
- TEXT: Long text
- BOOLEAN: TRUE or FALSE
- DATE: Date value
- DATETIME: Date and time

### 2. Variable Declaration
```
DEFINE variable_name data_type
DEFINE v_count INTEGER
DEFINE v_name VARCHAR(50)
DEFINE v_salary DECIMAL(10,2)
```

### 3. Main Program Structure
```
MAIN
  -- Declarations here
  DEFINE v_id INTEGER
  DEFINE v_name VARCHAR(100)
  
  -- Program logic here
  LET v_id = 1
  LET v_name = "John Doe"
  
  -- Display results
  DISPLAY "ID: ", v_id
  DISPLAY "Name: ", v_name
END MAIN
```

### 4. Control Flow Statements
- IF..THEN..ELSE..END IF
- WHILE..END WHILE
- FOR..END FOR
- CASE..END CASE
- EXIT WHILE / EXIT FOR
- CONTINUE WHILE / CONTINUE FOR

### 5. Functions
```
FUNCTION add_numbers(a INTEGER, b INTEGER) RETURNS INTEGER
  RETURN a + b
END FUNCTION
```

### 6. Array Operations
```
DEFINE arr DYNAMIC ARRAY OF RECORD
  id INTEGER,
  name VARCHAR(50)
END RECORD

LET arr[1].id = 1
LET arr[1].name = "John"
```

### 7. Database Operations
```
SELECT column_list INTO var_list FROM table WHERE condition
INSERT INTO table (columns) VALUES (values)
UPDATE table SET column=value WHERE condition
DELETE FROM table WHERE condition
```

### 8. Forms and UI
- OPEN FORM form_name FROM "form_name"
- DISPLAY FORM form_name
- INPUT BY NAME var1, var2
- OUTPUT BY NAME var1, var2
- ON ACTION: Handles user actions in forms

### 9. String Functions
- LENGTH(string)
- SUBSTR(string, start, length)
- INSTR(string, substring)
- UPCASE(string)
- DOWNCASE(string)
- TRIM(string)
- CONCAT() or || operator

### 10. Numeric Functions
- ABS(number)
- ROUND(number, decimals)
- TRUNC(number)
- MOD(number1, number2)
- SQRT(number)

### 11. Date Functions
- TODAY()
- CURRENT TIMESTAMP
- DAY(date), MONTH(date), YEAR(date)
- DATE_FORMAT(date, format)

### 12. Error Handling
```
TRY
  -- Code that might generate error
CATCH
  -- Error handling code
  DISPLAY "Error occurred"
END TRY
```

### 13. Modules and Imports
- MODULE keyword to define a module
- IMPORT keyword to import modules
- FUNCTION keyword for public functions
- PRIVATE keyword for private functions

### 14. Comments
- Using -- for single-line comments
- Using /* */ for multi-line comments

### 15. Common Patterns
- Cursor operations: DECLARE, OPEN, FETCH, CLOSE
- Batch operations
- Transaction handling: BEGIN WORK, COMMIT WORK, ROLLBACK WORK

## Best Practices
1. Use meaningful variable names
2. Comment complex logic
3. Use TRY-CATCH for error handling
4. Validate user input
5. Use stored procedures for database operations
6. Keep functions small and focused
7. Use consistent indentation

## Compilation and Execution
- Genero programs are compiled using: fglcomp program.4gl
- Execution: fglrun program.42m (bytecode format)

## Resources
- Official Genero documentation
- Four Js website for current versions
- Community forums for support
"""


def get_genero_knowledge():
    """Return the Genero knowledge base."""
    return GENERO_KNOWLEDGE


def search_knowledge(query: str) -> str:
    """
    Search the knowledge base for relevant information.
    
    Args:
        query: Search query string
        
    Returns:
        Relevant knowledge base sections
    """
    query_lower = query.lower()
    
    # Simple keyword matching to return relevant sections
    keywords = {
        "variable": "### 1. Variables and Data Types\n### 2. Variable Declaration",
        "function": "### 5. Functions",
        "array": "### 6. Array Operations",
        "database": "### 7. Database Operations",
        "form": "### 8. Forms and UI",
        "string": "### 9. String Functions",
        "numeric": "### 10. Numeric Functions",
        "date": "### 11. Date Functions",
        "error": "### 12. Error Handling",
        "module": "### 13. Modules and Imports",
        "loop": "### 4. Control Flow Statements",
        "if": "### 4. Control Flow Statements",
    }
    
    relevant_sections = []
    for keyword, section in keywords.items():
        if keyword in query_lower:
            relevant_sections.append(section)
    
    if relevant_sections:
        return "\n".join(relevant_sections)
    else:
        return "No specific section found. Here's the full knowledge base:\n" + GENERO_KNOWLEDGE
