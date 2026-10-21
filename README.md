# Bug-fix Challenge

Someone has written a small data science toolkit in Java. It looks fine. It is not fine.
There are **bugs** hiding in it, and a set of tests that fail because of them.
Your team's job: **find the bugs, fix them, and commit as you go.**

The team with the highest score at the end wins.

## Before you start

You need three things installed (the organisers will help if something goes wrong):

1. **Git**: check with `git --version`
2. **JDK 17 or newer**: check with `java -version`
3. **VS Code** with the *Extension Pack for Java* (VS Code will offer it when you open this folder)

You do **not** need to install Gradle. The project brings its own: use `./gradlew` (Mac/Linux, or Git Bash on Windows) or `gradlew.bat` (Windows Command Prompt; in PowerShell use `.\gradlew.bat`). The first run downloads Gradle and takes a minute or two, so run `./gradlew test` once before the event.

## Get the code

```
git clone https://github.com/OllieSClark/bugfix-challenge.git
cd bugfix-challenge
code .
```

## Your first commit

1. Open `TEAM.md` and fill in your **team name** and **member names**.
2. Commit it:
   ```
   git add TEAM.md
   git commit -m "Add team details"
   ```

## The loop

Repeat until you run out of time or bugs:

1. **Run the tests**: `./gradlew test`
   Read the output. Each failure tells you what it expected and what it got.
2. **Pick one failing test.** Find the code it is testing in `src/main/java/challenge/`.
3. **Fix the bug** (one bug at a time).
4. **Run the tests again.** Did it go green? Did anything else turn red?
5. **Commit.** One fix, one commit, with a message that says what you fixed:
   ```
   git add -A
   git commit -m "Fix sum skipping the last element"
   ```

Check your score at any time:

```
./gradlew score
```

## Scoring

| What | Points |
|---|---|
| Each Level 1 test fixed | 1 |
| Each Level 2 test fixed | 2 |
| Each Level 3 test fixed | 3 |
| Good git habits: each commit with a clear, different message (max 5) | 1 |

- Tests that already pass at the start don't score, but **breaking one costs you**.
- Only commits made **after** the starting point count, and one giant commit at the end counts as one.
- If two teams tie, the organisers run extra tests you haven't seen. Making the visible tests pass by special-casing them will not save you.

## Rules

- **Don't edit** anything in `src/test/`, `src/main/java/scoring/`, `build.gradle` or `settings.gradle`. The score card will tell you off.
- **Use whatever help you like**, including AI assistants (GitHub Copilot in VS Code, ChatGPT, anything), and each other. A few tips:
  - Ask it to **explain** a failing test or a piece of code before asking it to fix anything.
  - Check what it changed with `git diff` before you commit. Do you understand the change?
  - AI is confidently wrong sometimes. A test that passes is a good sign. A test that fails is information.
  - Some bugs only show up *after* you fix another one. Some tests pass for the wrong reasons.
- Work as a team. Everyone should be able to explain at least one fix.

## Handy commands

| Command | What it does |
|---|---|
| `./gradlew test` | Run all tests |
| `./gradlew score` | Run tests and show your score card |
| `git status` | What have I changed? |
| `git diff` | Show exactly what changed |
| `git log --oneline` | Your commit history |

## Where things are

```
src/main/java/challenge/   <- the buggy code (fix this)
src/test/java/challenge/   <- the tests (read these, don't edit)
TEAM.md                    <- your team details
```

Good luck, and read the error messages. They are trying to help you.
