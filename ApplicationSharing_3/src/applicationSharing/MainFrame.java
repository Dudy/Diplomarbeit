/*
 * MainFrame.java
 *
 * Created on 24. Juni 2007, 10:17
 */
package applicationSharing;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import rice.p2p.commonapi.NodeHandle;
import rice.pastry.PastryNode;

/**
 *
 * @author  podolak
 */
public class MainFrame extends javax.swing.JFrame {
    private static final String[] NAMEN = {"Alice", "Bob", "Charlie", "Dennis", "Ernie", "Fred", "Gerald", "Hitch"};
    private Integer bindport = 12345;
    private int bootport = 12345;
    private InetAddress bootaddr = null;
    private InetSocketAddress bootaddress = null;
    private PrintStream printStream;
    private ApplicationSharingNodeFactory factory;
    private NodeHandle bootHandle;
    private int namecounter = 0;
    private ApplicationSharingClientController firstClient;

    /** Creates new form MainFrame */
    public MainFrame() {
        initComponents();
        port.setText(bindport.toString());

        printStream = new PrintStream(new OutputStream() {
            public void write(int b) throws IOException {
                ausgabe.append(Character.toString((char)b));
                if (ausgabeScrollen.isSelected()) {
                    ausgabe.setCaretPosition(ausgabe.getText().length());
                }
            }
        });

        try {
            bootaddr = InetAddress.getByName("192.168.2.35");
            bootaddress = new InetSocketAddress(bootaddr, bootport);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(0);
        }

        System.setOut(printStream);
        System.setErr(printStream);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        ausgabe = new javax.swing.JTextArea();
        clientHinzufuegen = new javax.swing.JButton();
        beenden = new javax.swing.JButton();
        ausgabeScrollen = new javax.swing.JCheckBox();
        portLabel = new javax.swing.JLabel();
        port = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        scrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Ausgaben"));

        ausgabe.setColumns(20);
        ausgabe.setRows(5);
        scrollPane.setViewportView(ausgabe);

        clientHinzufuegen.setText("Client hinzufügen");
        clientHinzufuegen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientHinzufuegenActionPerformed(evt);
            }
        });

        beenden.setText("Anwendung beenden");
        beenden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beendenActionPerformed(evt);
            }
        });

        ausgabeScrollen.setText("scrolle Ausgabenfenster");
        ausgabeScrollen.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ausgabeScrollen.setMargin(new java.awt.Insets(0, 0, 0, 0));

        portLabel.setText("Port");

        port.setText("12345");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ausgabeScrollen)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(portLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(port, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 246, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(beenden, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(clientHinzufuegen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clientHinzufuegen)
                    .addComponent(portLabel)
                    .addComponent(port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(beenden)
                    .addComponent(ausgabeScrollen))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-582)/2, (screenSize.height-254)/2, 582, 254);
    }// </editor-fold>//GEN-END:initComponents

  private void clientHinzufuegenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientHinzufuegenActionPerformed
      if (port.isEnabled()) {
          try {
              bindport = Integer.parseInt(port.getText());
              port.setEnabled(false);
              factory = new ApplicationSharingNodeFactory(bootaddr, bindport);
          } catch (NumberFormatException ex) {
              ex.printStackTrace();
          } catch (IOException ex) {
              ex.printStackTrace();
          }
      }

      bootHandle = factory.getNodeHandle(bootaddress);

      String name = "noName" + namecounter;
      if (namecounter < NAMEN.length) {
          name = NAMEN[namecounter++];
      }

      PastryNode newNode = factory.newNode((rice.pastry.NodeHandle)bootHandle);
      System.out.println("Finished creating new node " + newNode);
      new ApplicationSharingClientController(printStream, newNode.getLocalHandle(), name).start();
  }//GEN-LAST:event_clientHinzufuegenActionPerformed

  private void beendenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beendenActionPerformed
      System.exit(0);
  }//GEN-LAST:event_beendenActionPerformed
  
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea ausgabe;
    private javax.swing.JCheckBox ausgabeScrollen;
    private javax.swing.JButton beenden;
    private javax.swing.JButton clientHinzufuegen;
    private javax.swing.JTextField port;
    private javax.swing.JLabel portLabel;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
}