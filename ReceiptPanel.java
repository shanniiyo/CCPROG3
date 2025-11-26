/**
 * ReceiptPanel.java
 * A GUI panel that displays the final transaction receipt to the user.
 * It shows a formatted summary of the purchase, including items, totals, and change.
 * It provides options to save the receipt to a file or start a new transaction.
 *
 * @author Alfonzo Regaspi
 * @version 1.0
 */
import javax.swing.*;
import java.awt.*;

public class ReceiptPanel extends JPanel {

    public ReceiptPanel(StoreGUI frame, Receipt receipt, double change) {
        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setText(receipt.getFormattedReceipt() + String.format("\nChange: ₱%.2f\n", change));

        JButton printBtn = new JButton("Save Receipt to File");
        printBtn.addActionListener(e -> {
            receipt.saveToFile();
            JOptionPane.showMessageDialog(this, "Receipt saved: " + receipt.getFileName());
        });

        JButton newTransBtn = new JButton("New Transaction");
        newTransBtn.addActionListener(e -> frame.showPage("Catalog"));

        JPanel bottom = new JPanel();
        bottom.add(printBtn);
        bottom.add(newTransBtn);

        add(new JScrollPane(area), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }
}
