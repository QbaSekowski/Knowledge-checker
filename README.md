# Application Introduction

The **“Knowledge Checker”** application allows users to test their knowledge and general education.

The knowledge model is simple: each element consists of a question and a single correct answer for a given topic.

Example:

- Topic: Geography
- Question: What is the capital of Germany?
- Answer: Berlin

The application asks users questions and verifies whether their answers are correct.
It also tracks users' past answers, which can be useful for providing feedback
and selecting questions based on past performance.

Your task is to implement this application according to the specifications below.

# Application Lifecycle

Here are the main steps from application start to stop:

1. When the application starts, it loads the **Knowledge Bank** from a file.
2. The application iteratively performs the following steps:
    - Selects a question from the knowledge bank based on a strategy and presents it to the user.
    - The user inputs their answer.
    - The application checks if the answer is correct, provides feedback, and tracks the result for the question.
3. When the user decides to exit, the application proceeds to the next step.
4. The application prints the user's performance and saves the updated knowledge bank to the file.

# Data Model

The file format is JSON. It contains a top-level array of objects representing questions:

Example:
```json
{
  "id": 1,
  "category": "Geography",
  "question": "What is the capital of Germany?",
  "answer": "Berlin"
}
```

If the user has answered the question in the past, the application also saves additional information:

Example:
```json
{
  "id": 1,
  "category": "Geography",
  "question": "What is the capital of Germany?",
  "answer": "Berlin",
  "lastAsked": "2026-02-13T13:51:58",
  "history": [
    false,
    true
  ]
}
```

- `lastAsked`: The time when the question was last asked.
- `history`: An array of answers; `true` for correct, `false` for incorrect. Append new answers to the end of the array.

Time format:
- ISO 8601, `YYYY-MM-DDThh:mm:ss`
- For simplicity, the time zone is ignored.

# User Interface

The user interface is command-line based.

When the application starts, it displays a welcome message, the available categories,
and the total number of questions in the **Knowledge Bank**.

Example:
```text
Welcome to the Knowledge Bank Application!
This application will test your knowledge on the following topics: Geography, History, Culture, Science, Food & Cooking, Art, Nature
Total questions in the knowledge bank: 25
You will be asked questions from various categories.
If you want to exit, type '/exit' as your answer.

Question: What is the capital of Germany?
Your answer:
```

The user types their answer and presses Enter.
Depending on the answer, the application provides feedback.

Case 1) Correct answer:
```text
Correct! Well done.
```

Case 2) Incorrect answer:
```text
Incorrect. The correct answer is: Berlin
```

If the user types `/exit`, the application stops asking questions and prints the user's performance.

```text
Your performance:
Geography - Asked: 2, Correct: 2, Percentage: 100%
History - Asked: 4, Correct: 3, Percentage: 75%
Culture - Asked: 1, Correct: 0, Percentage: 0%
```

For each topic, `Asked` is the total number of questions asked from that topic,
and `Correct` is the total number of questions answered correctly.

`Percentage` is calculated as the number of correct answers divided by the total number of answers.
If the result is fractional, it must be rounded to the nearest whole number.

If no questions were asked from a topic, skip the topic.

# Question Selection

The application should select questions from the knowledge bank based on a strategy.

Possible strategies:
- **Fixed Strategy:** Always returns the same question. Useful for testing the application.
- **Sequential Strategy:** Presents questions in the order they appear in the collection.
- **Random Selection:** Selects questions randomly from the collection.
- **Adaptive Selection:** Selects questions based on the user's past performance.

## Weight Calculation for Adaptive Selection

To implement the adaptive selection strategy, calculate a weight for each question.
A higher weight increases the probability of the question being selected.

The weight is based on two factors: history and time.

**History Weight Calculation**

Prioritize questions the user struggled with in the past.
Only the 2 most recent answers are considered.

- The weight is 0 by default.
- For each **incorrect** answer in history, add 2 to the weight.
- For each **correct** answer in history, subtract 1 from the weight.

Examples:
- No history → 0
- `[true]` → -1
- `[false]` → 2
- `[false, true]` → 1
- `[false, false]` → 4
- `[false, true, true, true]` → -2 (only the 2 most recent answers are considered)

**Time Weight Calculation**

If the question has never been asked before, the time weight is 1. Otherwise, it is 0.

**Total Weight Calculation**

The total weight is the sum of the history weight and the time weight.

# Implementation

Instructions for implementing the application:

## Model Classes

The application should use the model class `KnowledgeElement`, which is already defined in the project.
You may add fields and methods as needed.

The class includes **Lombok** annotations.

If you are not familiar with Lombok, it is a Java library that helps reduce boilerplate code
by generating common methods like getters, setters, constructors, and more at compile time using annotations.

Here is a guide about it: https://auth0.com/blog/a-complete-guide-to-lombok/

## Persistence

Implement the `JsonPersistence` class to handle loading and saving the knowledge bank to a JSON file.

- Generate well-formatted output (add spaces and newlines).
- Format the `lastAsked` attribute in ISO 8601 format `YYYY-MM-DDThh:mm:ss`.

Use the `Jackson` library for JSON parsing, which is already included in the project Maven dependencies.
Do not map model classes directly to JSON using Jackson annotations.
Define **Data Transfer Object** classes instead, and load data into them.

When DTOs are loaded, they should be mapped to the corresponding model classes.
`ModelMapper`, which is already included as a dependency,
can be used to perform the mapping between DTOs and model objects.

## Strategy Classes

Implement the following strategy classes for question selection:
- `FixedStrategy`
- `SequentialStrategy`
- `RandomStrategy`
- `AdaptiveStrategy`

The first three strategies are straightforward to implement.

Read the details below for the `AdaptiveStrategy`.

## Adaptive Selection Strategy

Implement the weight calculation for the adaptive selection strategy
as described in **Weight Calculation for Adaptive Selection**.

After calculating weights, sort questions by weight in descending order,
take the **top 5 questions**, and select one randomly.

Note: If the number of questions is less than 5, use all questions for random selection.

## Spring Framework

Use the **Spring Framework** for dependency injection and application configuration.

Define beans for components such as:
- `JsonPersistence`
- Application main logic
- Strategy implementation classes
- User interface component

### Spring Configuration

See the `application.properties` file for configuration properties used in the application.

- `data.file`: Path to the JSON file to load.
- `fixedstrategy.questionIndex`: Index of the question to be returned by the `FixedStrategy`.
- `question.selection.strategy`: Question selection strategy to use at runtime.

Hint:
- For the strategy bean, use Java configuration to return the bean of the selected strategy implementation class.

Requirements:
- Use constructor injection for all dependencies and configurations.

**Maven dependencies**

Do not add additional dependencies to the project.
Use only those already defined.

# Testing

The template project includes several JUnit test classes for testing application components:

- `JsonPersistenceTest` for testing JSON file loading.
- Test classes for some strategies:
    - `FixedStrategyTest`
    - `SequentialStrategyTest`

Acceptance by **Autocode** platform:
- Your application must pass all provided test cases.
- Run the test locally before you submit to Autocode to make sure everything works as expected.

**Notes:**
- The provided test cases do not cover all functionality.
  (For example, there are no test cases for saving data to JSON, weight calculation, or the adaptive strategy.)
- You may write additional test classes for other components as needed.
  Writing unit tests for the **weight calculation** is highly recommended.

Restrictions:
- Do not change the provided test classes. They will be used to evaluate your implementation.
- Do not change the content of `test_data/questions.json`, as it is used in the tests.

# Engineering Excellence

When you implement the application, apply software development and design best practices, such as:
the Single Responsibility Principle, Loosely Coupled Design, and Clean Code principles.
