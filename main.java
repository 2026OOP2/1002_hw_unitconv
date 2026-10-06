import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class main
{
    private static final String[] TYPES = {"長度", "重量", "溫度"};
    private static final String[][] UNITS = {
        {"公尺", "公分", "英吋", "英尺"},
        {"公斤", "公克", "磅", "盎司"},
        {"攝氏", "華氏", "克氏"}
    };

    private static final JFrame frame = new JFrame("單位換算器");
    private static final JComboBox<String> typeBox = new JComboBox<>(TYPES);
    private static final JTextField inputField = new JTextField();
    private static final JTextField resultField = new JTextField();
    private static final JComboBox<String> sourceBox = new JComboBox<>();
    private static final JComboBox<String> targetBox = new JComboBox<>();

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(main::createWindow);
    }

    private static void createWindow()
    {
        frame.setSize(480, 280);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(8, 8));

        resultField.setEditable(false);

        JPanel centerPanel = new JPanel(new GridLayout(2, 3, 8, 12));
        centerPanel.add(new JLabel("來源"));
        centerPanel.add(inputField);
        centerPanel.add(sourceBox);
        centerPanel.add(new JLabel("目標"));
        centerPanel.add(resultField);
        centerPanel.add(targetBox);

        JButton convertButton = new JButton("換算");
        typeBox.addActionListener(e -> updateUnits());
        convertButton.addActionListener(e -> convert());

        frame.add(typeBox, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(convertButton, BorderLayout.SOUTH);
        updateUnits();
        frame.setVisible(true);
    }

    private static void updateUnits()
    {
        int typeIndex = typeBox.getSelectedIndex();
        sourceBox.setModel(new DefaultComboBoxModel<>(UNITS[typeIndex]));
        targetBox.setModel(new DefaultComboBoxModel<>(UNITS[typeIndex]));
        targetBox.setSelectedIndex(Math.min(1, UNITS[typeIndex].length - 1));
        resultField.setText("");
    }

    private static void convert()
    {
        try {
            double value = Double.parseDouble(inputField.getText().trim());
            int typeIndex = typeBox.getSelectedIndex();
            int sourceIndex = sourceBox.getSelectedIndex();
            int targetIndex = targetBox.getSelectedIndex();
            double baseValue = toBase(value, typeIndex, sourceIndex);
            double result = fromBase(baseValue, typeIndex, targetIndex);
            resultField.setText(String.format("%.6g", result));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "請輸入有效的數值。", "輸入錯誤", JOptionPane.ERROR_MESSAGE);
            inputField.requestFocusInWindow();
        }
    }

    private static double toBase(double value, int typeIndex, int unitIndex)
    {
        if (typeIndex == 0) {
            double[] metersPerUnit = {1.0, 0.01, 0.0254, 0.3048};
            return value * metersPerUnit[unitIndex];
        }
        if (typeIndex == 1) {
            double[] kilogramsPerUnit = {1.0, 0.001, 0.45359237, 0.028349523125};
            return value * kilogramsPerUnit[unitIndex];
        }
        if (unitIndex == 0) return value + 273.15;
        if (unitIndex == 1) return (value - 32) * 5 / 9 + 273.15;
        return value;
    }

    private static double fromBase(double value, int typeIndex, int unitIndex)
    {
        if (typeIndex == 0) {
            double[] metersPerUnit = {1.0, 0.01, 0.0254, 0.3048};
            return value / metersPerUnit[unitIndex];
        }
        if (typeIndex == 1) {
            double[] kilogramsPerUnit = {1.0, 0.001, 0.45359237, 0.028349523125};
            return value / kilogramsPerUnit[unitIndex];
        }
        if (unitIndex == 0) return value - 273.15;
        if (unitIndex == 1) return (value - 273.15) * 9 / 5 + 32;
        return value;
    }
}