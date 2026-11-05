import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Collections;

public class StringToLearn extends JFrame {

    // ---------- Light pastel theme, Times New Roman only ----------
    static final String FF = "Times New Roman";
    static final Color BG    = new Color(0xFFF8FB);   // very light pink background
    static final Color PINK  = new Color(0xFFD6E7);   // borders
    static final Color SEL   = new Color(0xFFE6F1);   // selected topic
    static final Color DEEP  = new Color(0xE2679A);   // titles, button
    static final Color LAV   = new Color(0xF3ECFF);   // header
    static final Color CREAM = new Color(0xFFFDF7);   // lesson and output paper
    static final Color TEXT  = new Color(0x6B5470);   // soft plum text

    static Font font(int style, int size) {
        return new Font(FF, style, size);
    }

    static TitledBorder titled(String title) {
        TitledBorder b = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PINK, 2, true), " " + title + " ");
        b.setTitleFont(font(Font.BOLD, 16));
        b.setTitleColor(DEEP);
        return b;
    }

    // Rounded pink button
    static class RoundButton extends JButton {
        RoundButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setForeground(Color.WHITE);
            setFont(font(Font.BOLD, 17));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(120, 36));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? DEEP.brighter() : DEEP);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // One lesson in the "mini website"
    static class Topic {
        final String id, syntax, trick, labelA, labelB;
        final String[] points;      // bullet points
        final boolean needS2;

        Topic(String id, String[] points, String syntax, String trick,
              boolean needS2, String labelA, String labelB) {
            this.id = id;
            this.points = points;
            this.syntax = syntax;
            this.trick = trick;
            this.needS2 = needS2;
            this.labelA = labelA;   // null = hide Input A
            this.labelB = labelB;   // null = hide Input B
        }

        @Override
        public String toString() {
            return id;
        }
    }

    private final Topic[] topics = {
        new Topic("length()",
            new String[]{"Counts every character, spaces included",
                         "Returns an int",
                         "An empty string \"\" has length 0"},
            "str.length()",
            "Array = length (no brackets). String = length() (brackets, it is a method).",
            false, null, null),
        new Topic("charAt()",
            new String[]{"Gives the char at an index",
                         "Index starts at 0",
                         "Last index = length() - 1",
                         "A bad index throws StringIndexOutOfBoundsException"},
            "str.charAt(index)",
            "Index = seat number starting from 0. Last seat = length - 1.",
            false, "Index:", null),
        new Topic("toUpperCase()",
            new String[]{"Returns a NEW string in capital letters",
                         "The original is unchanged (strings are immutable)",
                         "Digits and symbols stay the same"},
            "str.toUpperCase()",
            "UP = Upper. Think ALL CAPS.",
            false, null, null),
        new Topic("toLowerCase()",
            new String[]{"Returns a NEW string in small letters",
                         "The original is unchanged (strings are immutable)",
                         "Digits and symbols stay the same"},
            "str.toLowerCase()",
            "LOW = Lower. Think whisper.",
            false, null, null),
        new Topic("equals()",
            new String[]{"equals() compares the CONTENT",
                         "== compares memory addresses",
                         "equalsIgnoreCase() ignores upper and lower case",
                         "compareTo() returns 0 when both are equal"},
            "s1.equals(s2)   s1.equalsIgnoreCase(s2)   s1.compareTo(s2)",
            "equals = same words. == = same house address.",
            true, null, null),
        new Topic("startsWith()",
            new String[]{"Returns true or false (boolean)",
                         "Checks the beginning of the string",
                         "Optional second argument: start checking from an index"},
            "str.startsWith(prefix)   str.startsWith(prefix, index)",
            "Starts = Front door. Returns a boolean.",
            false, "Prefix:", "From index (optional):"),
        new Topic("endsWith()",
            new String[]{"Returns true or false (boolean)",
                         "Checks the end of the string",
                         "There is no index version",
                         "Handy for file types like .java or .png"},
            "str.endsWith(suffix)",
            "Ends = Back door.",
            false, "Suffix:", null),
        new Topic("substring()",
            new String[]{"Returns a part of the string",
                         "Start index is included, end index is excluded",
                         "Result length = end - start",
                         "With only a start index, it runs to the end"},
            "str.substring(start)   str.substring(start, end)",
            "Start is IN, End is OUT.",
            false, "Start index:", "End index (optional):"),
        new Topic("trim()",
            new String[]{"Removes spaces from both ends only",
                         "Spaces in the middle stay",
                         "Returns a new string",
                         "Java 11+ also has strip() for Unicode spaces"},
            "str.trim()",
            "trim = haircut: sides only, nothing from the middle.",
            false, null, null),
        new Topic("intern()",
            new String[]{"Strings can live in the String Pool",
                         "intern() returns the pooled copy (adds it if missing)",
                         "Same text gives the same pooled object",
                         "To compare content, use equals(), not =="},
            "str.intern()",
            "Pool = shared locker. intern() gives you the locker's key, not a new item.",
            false, null, null),
        new Topic("indexOf()",
            new String[]{"indexOf() gives the FIRST position",
                         "lastIndexOf() gives the LAST position",
                         "Returns -1 when not found",
                         "The search is case-sensitive"},
            "str.indexOf(text)   str.lastIndexOf(text)",
            "-1 = Not found. First = Forward search, Last = Back search.",
            false, "Find:", null),
        new Topic("hashCode()",
            new String[]{"Returns an int built from the content",
                         "Equal strings always give equal hash codes",
                         "Different strings can share a hash (collision)",
                         "Used by HashMap and HashSet"},
            "str.hashCode()",
            "Hash = fingerprint. Same text, same fingerprint.",
            true, null, null)
    };

    private final JList<Topic> menu = new JList<>(topics);
    private final JEditorPane info = new JEditorPane("text/html", "");
    private final JTextField tf1 = new JTextField();
    private final JTextField tf2 = new JTextField();
    private final JTextField tfA = new JTextField();
    private final JTextField tfB = new JTextField();
    private final JLabel lbA = new JLabel();
    private final JLabel lbB = new JLabel();
    private final JPanel rowS2 = row(new JLabel("String 2:"), tf2);
    private final JPanel rowA = row(lbA, tfA);
    private final JPanel rowB = row(lbB, tfB);
    private final JTextArea output = new JTextArea(6, 30);

    public StringToLearn() {
        super("Learn Java Strings");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel title = new JLabel("Learn Java Strings", JLabel.CENTER);
        title.setFont(font(Font.BOLD, 26));
        title.setForeground(DEEP);
        JLabel sub = new JLabel("pick a topic, read, then try it", JLabel.CENTER);
        sub.setFont(font(Font.ITALIC, 15));
        sub.setForeground(TEXT);
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(LAV);
        header.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        header.add(title);
        header.add(sub);
        add(header, BorderLayout.NORTH);

        // Left menu
        menu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menu.setBackground(Color.WHITE);
        menu.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean selected, boolean focus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, selected, false);
                l.setText("\u2022 " + value);
                l.setFont(font(selected ? Font.BOLD : Font.PLAIN, 16));
                l.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
                l.setBackground(selected ? SEL : Color.WHITE);
                l.setForeground(selected ? DEEP : TEXT);
                return l;
            }
        });
        JScrollPane menuScroll = new JScrollPane(menu);
        menuScroll.setPreferredSize(new Dimension(180, 0));
        menuScroll.setBorder(titled("Topics"));
        menuScroll.setBackground(BG);
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(BG);
        left.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        left.add(menuScroll, BorderLayout.CENTER);
        add(left, BorderLayout.WEST);

        // Lesson card
        info.setEditable(false);
        info.setBackground(CREAM);
        JScrollPane infoScroll = new JScrollPane(info);
        infoScroll.setPreferredSize(new Dimension(520, 250));
        infoScroll.setBorder(titled("Lesson"));

        // Try-it card
        for (JTextField f : new JTextField[]{tf1, tf2, tfA, tfB}) {
            f.setFont(font(Font.PLAIN, 16));
            f.setForeground(TEXT);
            f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PINK, 1, true),
                BorderFactory.createEmptyBorder(3, 8, 3, 8)));
        }
        JPanel tryIt = new JPanel();
        tryIt.setLayout(new BoxLayout(tryIt, BoxLayout.Y_AXIS));
        tryIt.setBackground(BG);
        tryIt.setBorder(titled("Try it"));
        tryIt.add(row(new JLabel("String 1:"), tf1));
        tryIt.add(rowS2);
        tryIt.add(rowA);
        tryIt.add(rowB);

        JButton run = new RoundButton("Run it!");
        run.addActionListener(e -> runSelected());
        JPanel runRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        runRow.setBackground(BG);
        runRow.add(run);
        tryIt.add(runRow);

        // Output card
        output.setEditable(false);
        output.setFont(font(Font.PLAIN, 17));
        output.setBackground(CREAM);
        output.setForeground(TEXT);
        output.setMargin(new Insets(8, 10, 8, 10));
        JScrollPane outScroll = new JScrollPane(output);
        outScroll.setBorder(titled("Output"));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(BG);
        bottom.add(tryIt, BorderLayout.NORTH);
        bottom.add(outScroll, BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setBackground(BG);
        right.add(infoScroll, BorderLayout.NORTH);
        right.add(bottom, BorderLayout.CENTER);
        right.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
        add(right, BorderLayout.CENTER);

        menu.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showTopic(menu.getSelectedValue());
            }
        });
        menu.setSelectedIndex(0);

        setSize(880, 760);
        setLocationRelativeTo(null);
    }

    private static JPanel row(JLabel label, JTextField field) {
        label.setFont(font(Font.BOLD, 16));
        label.setForeground(TEXT);
        label.setPreferredSize(new Dimension(170, 30));
        JPanel p = new JPanel(new BorderLayout(6, 0));
        p.setBackground(BG);
        p.add(label, BorderLayout.WEST);
        p.add(field, BorderLayout.CENTER);
        p.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return p;
    }

    // Show the lesson (with bullets) and only the inputs this topic needs
    private void showTopic(Topic t) {
        if (t == null) return;

        StringBuilder bullets = new StringBuilder("<ul>");
        for (String p : t.points) {
            bullets.append("<li>").append(p).append("</li>");
        }
        bullets.append("</ul>");

        info.setText("<html><body bgcolor='#FFFDF7' style='font-family:\"Times New Roman\",serif;"
            + "font-size:15pt;color:#6B5470;margin:10px'>"
            + "<h2 style='color:#E2679A'>" + t.id + "</h2>"
            + "<p><b>Key points</b></p>" + bullets
            + "<p><b>Syntax:</b> <code>" + t.syntax + "</code></p>"
            + "<p><b>Trick to remember:</b> <font color='#E2679A'><i>" + t.trick + "</i></font></p>"
            + "</body></html>");
        info.setCaretPosition(0);

        rowS2.setVisible(t.needS2);
        rowA.setVisible(t.labelA != null);
        rowB.setVisible(t.labelB != null);
        if (t.labelA != null) lbA.setText(t.labelA);
        if (t.labelB != null) lbB.setText(t.labelB);

        output.setText("");
        revalidate();
        repaint();
    }

    private void runSelected() {
        Topic t = menu.getSelectedValue();
        String s1 = tf1.getText();
        String s2 = tf2.getText();
        String a = tfA.getText();
        String b = tfB.getText().trim();
        String result;

        try {
            result = run(t, s1, s2, a, b);
        } catch (NumberFormatException ex) {
            result = "Oops! Please enter a valid whole number.";
        } catch (StringIndexOutOfBoundsException ex) {
            result = "Oops! Invalid index. Valid range is 0 to " + Math.max(0, s1.length() - 1)
                   + " (substring allows up to " + s1.length() + ").";
        }
        output.setText("\u2022 " + result.replace("\n", "\n\u2022 "));
    }

    // switch case: one case per topic
    private String run(Topic t, String s1, String s2, String a, String b) {
        switch (t.id) {
            case "length()":
                return "\"" + s1 + "\".length() = " + s1.length();

            case "charAt()": {
                int i = Integer.parseInt(a.trim());
                return "charAt(" + i + ") = '" + s1.charAt(i) + "'\n"
                     + "Valid index: 0 to " + (s1.length() - 1);
            }

            case "toUpperCase()":
                return "Before: " + s1 + "\nAfter: " + s1.toUpperCase();

            case "toLowerCase()":
                return "Before: " + s1 + "\nAfter: " + s1.toLowerCase();

            case "equals()":
                return "equals: " + s1.equals(s2) + "\n"
                     + "equalsIgnoreCase: " + s1.equalsIgnoreCase(s2) + "\n"
                     + "compareTo: " + s1.compareTo(s2) + "  (0 means equal)";

            case "startsWith()": {
                String out = "startsWith(\"" + a + "\") = " + s1.startsWith(a);
                if (!b.isEmpty()) {
                    int from = Integer.parseInt(b);
                    out += "\nstartsWith(\"" + a + "\", " + from + ") = " + s1.startsWith(a, from);
                }
                return out;
            }

            case "endsWith()":
                return "endsWith(\"" + a + "\") = " + s1.endsWith(a);

            case "substring()": {
                int start = Integer.parseInt(a.trim());
                if (b.isEmpty()) {
                    return "substring(" + start + ") = " + s1.substring(start);
                }
                int end = Integer.parseInt(b);
                return "substring(" + start + ", " + end + ") = " + s1.substring(start, end);
            }

            case "trim()": {
                String padded = "   " + s1 + "   ";
                return "Before trim: [" + padded + "]\nAfter trim: [" + padded.trim() + "]";
            }

            case "intern()": {
                String x = new String(s1);
                return "new String(str1) == str1: " + (x == s1) + "\n"
                     + "x.intern() == str1.intern(): " + (x.intern() == s1.intern()) + "\n"
                     + "x.equals(str1): " + x.equals(s1);
            }

            case "indexOf()":
                return "First index: " + s1.indexOf(a) + "\nLast index: " + s1.lastIndexOf(a)
                     + "\n(-1 means not found)";

            case "hashCode()":
                return "hashCode of String 1: " + s1.hashCode() + "\n"
                     + "hashCode of String 2: " + s2.hashCode() + "\n"
                     + "Same hash? " + (s1.hashCode() == s2.hashCode());

            default:
                return "Unknown topic";
        }
    }

    // Make every Swing component default to Times New Roman
    private static void useTimesNewRomanEverywhere() {
        FontUIResource f = new FontUIResource(font(Font.PLAIN, 16));
        for (Object key : Collections.list(UIManager.getDefaults().keys())) {
            if (UIManager.get(key) instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            useTimesNewRomanEverywhere();
            new StringToLearn().setVisible(true);
        });
    }
}