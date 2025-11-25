import javax.swing.*;
import java.awt.*;

public class ReceiptPanel extends JPanel {

    public ReceiptPanel(StoreGUI frame, Receipt receipt, double change) {
        setLayout(new BorderLayout()); //Uses BorderLayout to organize components
        //Text area to show the generated receipt text
        JTextArea area = new JTextArea();
        area.setEditable(false); //User cannot edit/modify the receipt
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setText(receipt.getFormattedReceipt() + String.format("\nChange: ₱%.2f\n", change));
        //Button for saving the receipt to a txt file
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

        add(new JScrollPane(area), BorderLayout.CENTER); //scrollable receipt display    
        add(bottom, BorderLayout.SOUTH); //buttons at the bottom
    }
}

