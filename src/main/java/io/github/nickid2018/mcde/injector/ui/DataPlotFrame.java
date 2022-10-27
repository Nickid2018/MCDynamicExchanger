package io.github.nickid2018.mcde.injector.ui;

import io.github.nickid2018.mcde.data.DataCollector;
import io.github.nickid2018.mcde.data.DataEntry;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.ToDoubleBiFunction;

public class DataPlotFrame {

    private final String[] dataNames;

    // Arguments -----------
    private int widthPerData = 20;
    private double maxX = Double.NaN, minX = Double.NaN;
    private double maxY = Double.NaN, minY = Double.NaN;

    private final Set<String> enabledData = new HashSet<>();

    public DataPlotFrame(String[] dataNames) {
        this.dataNames = dataNames;
        enabledData.addAll(Arrays.asList(dataNames));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setDoubleBuffered(true);

        Canvas canvas = new DataCanvas();
        panel.add(canvas, BorderLayout.CENTER);

        Timer timer = new Timer(100, e -> canvas.repaint());

        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPopupMenu popupMenu = new JPopupMenu();
        for (String name : dataNames) {
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(name);
            item.setSelected(true);
            item.addActionListener(e -> {
                if (item.isSelected())
                    enabledData.add(name);
                else
                    enabledData.remove(name);
            });
            popupMenu.add(item);
        }

        popupMenu.add(new JSeparator());

        JMenuItem setWidthPerData = new JMenuItem("Set width per data");
        setWidthPerData.addActionListener(e -> widthPerData = (int) getNumberOrNaN());
        popupMenu.add(setWidthPerData);

        JMenuItem setMaxX = new JMenuItem("Set max X");
        setMaxX.addActionListener(e -> maxX = getNumberOrNaN());
        popupMenu.add(setMaxX);

        JMenuItem setMinX = new JMenuItem("Set min X");
        setMinX.addActionListener(e -> minX = getNumberOrNaN());
        popupMenu.add(setMinX);

        JMenuItem setMaxY = new JMenuItem("Set max Y");
        setMaxY.addActionListener(e -> maxY = getNumberOrNaN());
        popupMenu.add(setMaxY);

        JMenuItem setMinY = new JMenuItem("Set min Y");
        setMinY.addActionListener(e -> minY = getNumberOrNaN());
        popupMenu.add(setMinY);

        JMenuItem setRefreshRate = new JMenuItem("Set refresh rate");
        setRefreshRate.addActionListener(e -> {
            int rate = (int) getNumberOrNaN();
            if (rate > 0)
                timer.setDelay(rate);
        });
        popupMenu.add(setRefreshRate);

        canvas.addMouseListener(new PopupListener(popupMenu));

        frame.setVisible(true);
        timer.start();
    }

    private static double getNumberOrNaN() {
        String num = JOptionPane.showInputDialog("Please input a number or NaN");
        if (num == null)
            return Double.NaN;
        try {
            return Double.parseDouble(num);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Thread add = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DataCollector.put("test1", Math.sin(System.currentTimeMillis() / 1000.0)
                        + 2 * Math.sin(System.currentTimeMillis() / 500.0)
                        + 4 * Math.sin(System.currentTimeMillis() / 250.0)
                        + 8 * Math.sin(System.currentTimeMillis() / 125.0));
            }
        });
        add.setDaemon(true);
        add.start();
        new DataPlotFrame(new String[]{"test1"});
    }

    private class DataCanvas extends Canvas {

        private static final int PADDING = 30;
        private static final Color[] COLORS = new Color[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));

            Map<String, List<DataEntry<?>>> dataMap = new HashMap<>();
            for (String name : dataNames)
                dataMap.put(name, DataCollector.getData(name));

            Map<String, List<DataEntry<?>>> filteredDataMap = new HashMap<>();
            dataMap.entrySet().stream()
                    .filter(e -> enabledData.contains(e.getKey()))
                    .filter(en -> en.getValue().get(0).getValue() instanceof Number)
                    .forEach(en -> filteredDataMap.put(en.getKey(), en.getValue()));

            if (filteredDataMap.isEmpty()) {
                g2.setColor(Color.BLACK);
                g2.drawString("No data to plot", getWidth() / 2, getHeight() / 2);
                return;
            }

            int width = getWidth() - PADDING * 2;
            int height = getHeight() - PADDING * 2;

            List<Number> xValues = new ArrayList<>();
            double minX, maxX;
            if (filteredDataMap.size() == 1) {
                maxX = filteredDataMap.values().iterator().next().size();
                for (int i = 0; i < maxX; i++)
                    xValues.add(i);
            } else {
                filteredDataMap.values().forEach(list -> list.forEach(en -> xValues.add(en.getTime())));
            }

            int maxHoldValues = getWidth() / widthPerData;
            int expectedSize = maxHoldValues * filteredDataMap.size();

            List<Number> parsedXValues = xValues.stream()
                    .sorted(Comparator.comparingDouble(Number::doubleValue))
                    .skip(xValues.size() > expectedSize ? xValues.size() - expectedSize : 0)
                    .toList();

            minX = parsedXValues.get(0).doubleValue();
            maxX = parsedXValues.get(parsedXValues.size() - 1).doubleValue();

            if (!Double.isNaN(DataPlotFrame.this.minX))
                minX = DataPlotFrame.this.minX;
            if (!Double.isNaN(DataPlotFrame.this.maxX))
                maxX = DataPlotFrame.this.maxX;

            if (minX == maxX) {
                minX -= 1;
                maxX += 1;
            }
            double xScale = (maxX - minX) / width;

            List<Number> yValues = new ArrayList<>();
            if (filteredDataMap.size() == 1) {
                filteredDataMap.values().iterator().next().stream()
                        .skip(xValues.size() > maxHoldValues ? xValues.size() - maxHoldValues : 0)
                        .map(en -> (Number) en.getValue())
                        .forEach(yValues::add);
            } else {
                double finalMaxX = maxX;
                double finalMinX = minX;
                filteredDataMap.values().forEach(list -> list.stream()
                        .filter(en -> en.getTime() <= finalMaxX && en.getTime() >= finalMinX)
                        .forEach(en -> yValues.add((Number) en.getValue())));
            }
            double minY = keepPrecision(yValues.stream().mapToDouble(Number::doubleValue).min().orElse(0));
            double maxY = keepPrecision(yValues.stream().mapToDouble(Number::doubleValue).max().orElse(0));

            if (!Double.isNaN(DataPlotFrame.this.minY))
                minY = DataPlotFrame.this.minY;
            if (!Double.isNaN(DataPlotFrame.this.maxY))
                maxY = DataPlotFrame.this.maxY;
            if (minY == maxY) {
                minY -= 1;
                maxY += 1;
            }
            double yScale = (maxY - minY) / height;

            drawAxis(g2, minX, maxX, minY, maxY, xScale, yScale);

            if (filteredDataMap.size() == 1) {
                int startIndex = xValues.size() > maxHoldValues ? xValues.size() - maxHoldValues : 0;
                List<DataEntry<?>> list = filteredDataMap.values().iterator().next().stream()
                        .skip(startIndex).toList();
                drawDataLines(g2, list, (i, d) -> i + startIndex, Color.RED, minX, maxX, minY, maxY, xScale, yScale);
            } else {
                int colorIndex = 0;
                for (Map.Entry<String, List<DataEntry<?>>> en : filteredDataMap.entrySet())
                    drawDataLines(g2, en.getValue(), (i, d) -> d.getTime(), COLORS[colorIndex++ % COLORS.length],
                            minX, maxX, minY, maxY, xScale, yScale);
            }
        }

        private double keepPrecision(double value) {
            if (value == 0)
                return 0;
            int sign = value < 0 ? -1 : 1;
            value = Math.abs(value);
            int precision = 0;
            while (value > 10) {
                value /= 10;
                precision--;
            }
            while (value < 10) {
                value *= 10;
                precision++;
            }
            return Math.ceil(value) / Math.pow(10, precision) * sign;
        }

        private void drawAxis(Graphics2D g2, double minX, double maxX, double minY, double maxY, double xScale, double yScale) {
            g2.setColor(Color.BLACK);
            g2.drawLine(PADDING, PADDING, PADDING, getHeight() - PADDING);
            g2.drawLine(PADDING, getHeight() - PADDING, getWidth() - PADDING, getHeight() - PADDING);

            int xyRatio = (getWidth() - 2 * PADDING) / (getHeight() - 2 * PADDING);

            double yStep = (maxY - minY) / 10;
            double xStep = (maxX - minX) / (10 * xyRatio);

            for (double x = minX; x <= maxX; x += xStep) {
                int xCoord = getPointX(x, minX, xScale);
                g2.drawString(String.format("%.2f", x), xCoord, getHeight() - PADDING + 15);
            }

            for (double y = minY; y <= maxY; y += yStep) {
                int yCoord = getPointY(y, minY, yScale);
                g2.drawString(String.format("%.2f", y), 0, yCoord);
            }
        }

        private void drawDataLines(Graphics2D g2, List<DataEntry<?>> dataList,
                                   ToDoubleBiFunction<Integer, DataEntry<?>> xFunc, Color color,
                                   double minX, double maxX, double minY, double maxY, double xScale, double yScale) {
            g2.setColor(color);

            List<double[]> points = new ArrayList<>();
            int index = 0;
            for (DataEntry<?> entry : dataList) {
                double x = xFunc.applyAsDouble(index, entry);
                if (x < minX || x > maxX)
                    continue;
                double y = ((Number) entry.getValue()).doubleValue();
                points.add(new double[]{x, y});
                index++;
            }

            for (int i = 0; i < points.size() - 1; i++) {
                double[] p1 = points.get(i);
                double[] p2 = points.get(i + 1);
                int x1 = getPointX(p1[0], minX, xScale);
                int y1 = getPointY(p1[1], minY, yScale);
                int x2 = getPointX(p2[0], minX, xScale);
                int y2 = getPointY(p2[1], minY, yScale);
                g2.drawLine(x1, y1, x2, y2);
            }

            g2.setColor(Color.BLACK);

            for (double[] p : points) {
                int x = getPointX(p[0], minX, xScale) + 5;
                int y = getPointY(p[1], minY, yScale) + 5;
                g2.drawString(String.format("%.2f", p[1]), x, y);
            }
        }

        private int getPointX(Number x, double minX, double xScale) {
            return (int) ((x.doubleValue() - minX) / xScale) + PADDING;
        }

        private int getPointY(Number y, double minY, double yScale) {
            return getHeight() - (int) ((y.doubleValue() - minY) / yScale) - PADDING;
        }
    }
}
