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

Then tell Git who you are. You only do this once per laptop, and without it your first `git commit` fails with *"Please tell me who you are"*:

```
git config --global user.name "Your Name"
git config --global user.email "you@example.com"
```

You don't need a GitHub account for this challenge, but it's worth making one at [github.com](https://github.com): it's where you'll keep your code for the rest of your degree. If you have one, use the same email above so your commits show up on your profile.

You do **not** need to install Gradle. The project brings its own: use `./gradlew` (Mac/Linux, or Git Bash on Windows) or `gradlew.bat` (Windows Command Prompt; in PowerShell use `.\gradlew.bat`). The first run downloads Gradle and takes a minute or two, so run `./gradlew test` as soon as you clone the repo.

## Get the code

```
git clone https://github.com/OllieSClark/bugfix-challenge.git
cd bugfix-challenge
code .
```

## Working as a team

Everyone clones the code and works on their own laptop, but **only one laptop gets scored**. Commits you make on your laptop stay on your laptop, so:

1. **Pick a submitter.** Their laptop is your team's entry, and only the submitter commits.
2. **Split up the bugs by experience.** Use the table in *Where to start* below so everyone has something they can fix, and make sure no two people work on the same method.
3. **Hand your fixes to the submitter.** When a test goes green on your laptop, run `git diff` to show the submitter exactly what you changed. They make the same change, run the tests, and commit it.
4. **Stay up to date.** `Scaler` uses `Stats`, so whoever works on `Scaler` should copy in each `Stats` fix as the submitter commits it.

When time is up, the organisers score the submitter's laptop. Everything must be committed by then.

## Where to start

Not every bug is equally hard. Pick a row that matches your experience:

| If you are... | Start with | File | Level |
|---|---|---|---|
| **New to coding** | `sum` | `Stats.java` | 1 |
| | `max` | `Stats.java` | 1 |
| | `birthdayCollision` | `Probability.java` | 3 |
| **Have done some coding** | `median` | `Stats.java` | 2 |
| | `minMax` | `Scaler.java` | 2 |
| | `atLeastOne` | `Probability.java` | 3 |
| | `trainTestSplit` | `DataSplit.java` | 3 |
| **Confident** | `variance` | `Stats.java` | 2 |
| | `choose` | `Probability.java` | 3 |

- **New to coding?** Each bug in your rows is a small change to one or two lines. Ask your AI assistant to explain the method line by line *before* you change anything, then find the line that doesn't match the comment above the method.
- `birthdayCollision` is Level 3, so it's worth 3 points, but it's no harder than the Level 1 bugs.
- Some methods use others: `mean` uses `sum`, `minMax` uses `min` and `max`, and `zScores` uses `mean` and `stdDev`. A test can stay red until another person's fix lands, so get the beginner fixes committed early.
- The confident person on each team: once you're done, go and help someone else.

## Your first commit

The submitter does this one:

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
5. **Commit** (submitter only). One fix, one commit, with a message that says what you fixed:
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
- If two teams tie, the team that passes more **hidden tests** wins (see below).

## Hidden tests (just like your coursework)

The tests you can see are not the only tests. The organisers have a second, hidden set that checks the same methods with different data and with the edge cases: empty arrays, single values, negative numbers, very large numbers, and bad input that should throw an exception.

This is how your coursework will be marked. You get some unit tests to check your work as you go, but the markers run more tests that you haven't seen. Code that only passes the visible tests, or special-cases them, loses marks.

To do well on the hidden tests:

- **Read the comment above each method.** That comment (its *Javadoc*) is the specification. The hidden tests check everything it promises, not just the parts the visible tests cover.
- **Ask "what input would break this?"** An empty array? One value? All negative numbers? A number too big for an `int`?
- **Fix the cause, not the symptom.** If your fix only works for the numbers in the visible test, it isn't a fix.

The hidden tests run on the submitter's **committed** code, so commit everything before time is up.

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
