import javax.swing.*;
import java.awt.*;

public class ReceiptPanel extends JPanel {

    private MainFrame frame;
    private Receipt receipt;
    private double change;

    public ReceiptPanel(MainFrame frame, Receipt receipt, double change) {
        this.frame = frame;
        this.receipt = receipt;
        this.change = change;

        setLayout(new BorderLayout());

        // Receipt text area
        JTextArea receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        // Display receipt text + change
        receiptArea.setText(receipt.getFormattedReceipt()
                + String.format("\nChange: ₱%.2f\n", change));

        // Buttons
        JButton printBtn = new JButton("Print Receipt");
        JButton newTransBtn = new JButton("New Transaction");

        // Print Button → confirm saved receipt
        printBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Receipt saved: " + receipt.getFileName());
        });

        // New Transaction -> return to catalog
        newTransBtn.addActionListener(e -> {
            frame.showPage("catalog");
        });

        // Bottom Panel
        JPanel bottom = new JPanel();
        bottom.add(printBtn);
        bottom.add(newTransBtn);

        add(new JScrollPane(receiptArea), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }
}
