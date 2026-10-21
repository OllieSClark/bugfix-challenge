package scoring;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Prints the team's score card. Run with:  ./gradlew score
 *
 * Score = tests fixed (weighted by level) + up to 5 points for good git habits.
 * Tests that already passed at the start do not score, but breaking one costs you.
 */
public final class Score {

    private static final String START_TAG = "start";
    private static final int MAX_COMMIT_POINTS = 5;
    private static final String[] PROTECTED = {"src/test", "src/main/java/scoring", "build.gradle", "settings.gradle"};

    /** Level class, weight per fixed test, number of tests passing at the start tag. */
    private record Level(String testClass, int weight, int baselinePassing) {}

    private static final Level[] LEVELS = {
        new Level("Level1Test", 1, 4),
        new Level("Level2Test", 2, 4),
        new Level("Level3Test", 3, 7),
    };

    private static final Set<String> LAZY_MESSAGES =
            Set.of("fix", "fixed", "update", "updated", "changes", "change", "commit", "test",
                   "tests", "stuff", "asdf", "wip", "done", "work", "edit", "ok", "final");

    public static void main(String[] args) throws Exception {
        Path root = Path.of("").toAbsolutePath();
        List<String> out = new ArrayList<>();

        boolean haveTag = !git("rev-parse", "-q", "--verify", "refs/tags/" + START_TAG).isEmpty();
        List<String> tampered = haveTag ? tamperedFiles() : List.of();
        boolean tamperedTests = !tampered.isEmpty();

        // ---- tests ----
        int testPoints = 0;
        List<String> stillFailing = new ArrayList<>();
        List<String> levelLines = new ArrayList<>();
        for (int i = 0; i < LEVELS.length; i++) {
            Level level = LEVELS[i];
            Path xml = root.resolve("build/test-results/test/TEST-challenge." + level.testClass() + ".xml");
            int[] r = readSuite(xml, stillFailing);
            int total = r[0];
            int passing = r[0] - r[1] - r[2] - r[3];
            int fixed = Math.max(0, passing - level.baselinePassing());
            int canFix = Math.max(0, total - level.baselinePassing());
            int points = tamperedTests ? 0 : fixed * level.weight();
            testPoints += points;
            levelLines.add(String.format(Locale.ROOT, "  Level %d (x%d points each)   %2d / %-2d tests fixed   +%d",
                    i + 1, level.weight(), fixed, canFix, points));
        }

        // ---- git habits ----
        List<String> goodCommits = new ArrayList<>();
        int allCommits = 0;
        if (haveTag) {
            Set<String> seen = new HashSet<>();
            for (String msg : git("log", "--no-merges", "--format=%s", START_TAG + "..HEAD")) {
                allCommits++;
                String key = msg.trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9 ]", "");
                boolean substantial = key.length() >= 8 && !LAZY_MESSAGES.contains(key);
                if (substantial && seen.add(key)) {
                    goodCommits.add(msg.trim());
                }
            }
        }
        int commitPoints = Math.min(MAX_COMMIT_POINTS, goodCommits.size());

        // ---- team details ----
        String team = "(no team name in TEAM.md)";
        String members = "";
        Path teamFile = root.resolve("TEAM.md");
        if (Files.exists(teamFile)) {
            for (String line : Files.readAllLines(teamFile, StandardCharsets.UTF_8)) {
                if (line.startsWith("Team name:") && line.substring(10).trim().length() > 0) {
                    team = line.substring(10).trim();
                } else if (line.startsWith("Members:")) {
                    members = line.substring(8).trim();
                }
            }
        }

        int total = testPoints + commitPoints;
        String bar = "=".repeat(60);
        out.add("");
        out.add(bar);
        out.add("  BUG-FIX CHALLENGE  -  SCORE CARD");
        out.add("  Team: " + team);
        if (!members.isEmpty()) {
            out.add("  Members: " + members);
        }
        out.add(bar);
        out.addAll(levelLines);
        out.add(String.format(Locale.ROOT, "  Git habits                    %d good commits (max %d)  +%d",
                goodCommits.size(), MAX_COMMIT_POINTS, commitPoints));
        out.add("  " + "-".repeat(56));
        out.add("  TOTAL: " + total);
        out.add(bar);

        if (!haveTag) {
            out.add("  ! Cannot find the 'start' tag. Did you clone the challenge repo?");
        }
        if (tamperedTests) {
            out.add("  ! You changed files you are not allowed to change, so test points are 0:");
            for (String f : tampered) {
                out.add("      " + f);
            }
            out.add("    (Undo with: git checkout start -- <file>)");
        }
        if (!stillFailing.isEmpty()) {
            out.add("");
            out.add("  Still failing:");
            for (String name : stillFailing) {
                out.add("    x " + name);
            }
        }
        if (!goodCommits.isEmpty()) {
            out.add("");
            out.add("  Commits that count:");
            for (String msg : goodCommits) {
                out.add("    * " + msg);
            }
        }
        if (allCommits > goodCommits.size()) {
            out.add("  (" + (allCommits - goodCommits.size())
                    + " commit(s) did not count: message too short, too vague or repeated)");
        }
        if (!git("status", "--porcelain").isEmpty()) {
            out.add("");
            out.add("  Heads up: you have uncommitted changes. Commit them!");
        }
        out.add("");

        for (String line : out) {
            System.out.println(line);
        }
        Files.createDirectories(root.resolve("build"));
        Files.write(root.resolve("build/score.txt"), out, StandardCharsets.UTF_8);
    }

    /** Returns {tests, failures, errors, skipped}; adds the names of failing tests to failingOut. */
    private static int[] readSuite(Path xml, List<String> failingOut) {
        if (!Files.exists(xml)) {
            return new int[] {0, 0, 0, 0};
        }
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml.toFile());
            Element suite = doc.getDocumentElement();
            int[] r = {
                Integer.parseInt(suite.getAttribute("tests")),
                Integer.parseInt(suite.getAttribute("failures")),
                Integer.parseInt(suite.getAttribute("errors")),
                Integer.parseInt(suite.getAttribute("skipped")),
            };
            NodeList cases = suite.getElementsByTagName("testcase");
            for (int i = 0; i < cases.getLength(); i++) {
                Element tc = (Element) cases.item(i);
                boolean failed = tc.getElementsByTagName("failure").getLength() > 0
                        || tc.getElementsByTagName("error").getLength() > 0;
                if (failed) {
                    failingOut.add(tc.getAttribute("name"));
                }
            }
            return r;
        } catch (Exception e) {
            System.out.println("  (could not read " + xml.getFileName() + ": " + e.getMessage() + ")");
            return new int[] {0, 0, 0, 0};
        }
    }

    /** Files in protected paths that differ from the start tag (committed or not). */
    private static List<String> tamperedFiles() throws Exception {
        List<String> args = new ArrayList<>(List.of("diff", "--name-only", START_TAG, "--"));
        args.addAll(List.of(PROTECTED));
        return git(args.toArray(new String[0]));
    }

    private static List<String> git(String... args) throws Exception {
        List<String> cmd = new ArrayList<>();
        cmd.add("git");
        cmd.addAll(List.of(args));
        Process p = new ProcessBuilder(cmd).redirectErrorStream(false).start();
        List<String> lines = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (!line.isBlank()) {
                    lines.add(line);
                }
            }
        }
        p.getErrorStream().transferTo(java.io.OutputStream.nullOutputStream());
        p.waitFor();
        return lines;
    }
}
