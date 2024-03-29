/*
 * MainFrame.java
 *
 * Created on 24. Juni 2007, 10:17
 */
package p2p_vcs_test;

import documentstorage.ClientController;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.Properties;
import org.apache.log4j.Logger;
import p2p_access.P2PAccess;

/**
 *
 * @author  podolak
 */
public class MainFrame extends javax.swing.JFrame {
    static Logger LOGGER = Logger.getLogger(MainFrame.class);
    
    private static final String PROPERTIES_FILE_NAME = "client.properties";
    private static final String[] NAMEN = {"Alice", "Bob", "Charlie", "Dennis", "Ernie", "Fred", "Gerald", "Hitch"};
    private PrintStream printStream;
    private int namecounter = 0;
    private Properties properties;
    private P2PAccess p2pAccess;

    /** Creates new form MainFrame */
    public MainFrame() throws UnknownHostException, IOException {
        initComponents();

        printStream = new PrintStream(new OutputStream() {
            public void write(int b) throws IOException {
                ausgabe.append(Character.toString((char)b));
                if (ausgabeScrollen.isSelected()) {
                    ausgabe.setCaretPosition(ausgabe.getText().length());
                }
            }
        });

        try {
            properties = new Properties();
            properties.load(new FileInputStream(new File(PROPERTIES_FILE_NAME)));
            p2pAccess = new P2PAccess(properties);
//            new ClientController(p2pAccess, "Duude [Boot Node]", printStream);
            new ClientController(p2pAccess, "P2P VCS", printStream);
        } catch (IOException ex) {
            System.err.println("error while reading properties file:\n" + ex.getMessage());
        }

//        System.setOut(printStream);
//        System.setErr(printStream);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ausgabeScrollen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 253, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(beenden, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(clientHinzufuegen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clientHinzufuegen)
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
      String name = "noName" + namecounter;
      
      if (namecounter < NAMEN.length) {
          name = NAMEN[namecounter++];
      }
      
      try {
          int bindport = Integer.parseInt(properties.getProperty("bindport")) + 1;
          properties.setProperty("bindport", Integer.toString(bindport));
          p2pAccess = new P2PAccess(properties);
//          new ClientController(p2pAccess, name, printStream);
          new ClientController(p2pAccess, "P2P VCS", printStream);
      } catch (UnknownHostException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }
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
                try {
                    new MainFrame().setVisible(true);
                } catch (UnknownHostException ex) {
                    LOGGER.error("unknown host", ex);
                } catch (IOException ex) {
                    LOGGER.error("io error", ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea ausgabe;
    private javax.swing.JCheckBox ausgabeScrollen;
    private javax.swing.JButton beenden;
    private javax.swing.JButton clientHinzufuegen;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
}