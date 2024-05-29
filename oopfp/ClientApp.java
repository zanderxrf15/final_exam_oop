package oopfp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JFrame;

public class ClientApp extends JFrame {
	/**
     * Creates new ClientApp
     */
    //create the necessary object
    static Socket socket;
    static DataInputStream in;
    static DataOutputStream out;
    static String chatOut = "";

    //Constructor class
    public ClientApp() {
        initComponents();
    }

    
    // a helper method to create the GUI
    private void initComponents() {

        //create the GUI objects
        jScrollPane1 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        chatInput = new javax.swing.JTextField();
        chatSend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Client App");

        chatArea.setColumns(20);
        chatArea.setRows(5);
        jScrollPane1.setViewportView(chatArea);

        //first prefix chat input
        chatInput.setText("Hello? Is this Freddy Fazbear's Pizzeria?");

        //button send and link it to the corresponding method
        chatSend.setText("Send");
        chatSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chatSendActionPerformed(evt);
            }
        });

        //configuring the layout of the jpanel
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chatInput)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chatSend, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chatInput, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(chatSend))
                .addContainerGap())
        );

        pack();
    }

    //method for buttonPress
    private void chatSendActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            chatOut = chatInput.getText().trim();//getting input
            if (!chatOut.equalsIgnoreCase("")){//for validating input(must not empty)
                //writing input to the dataStream and the chatBox
                out.writeUTF(chatOut);
                chatArea.setText(chatArea.getText().trim() + "\nClient : " + chatOut);
                chatInput.setText("");
            }
        } 
        catch (Exception e) {
            //for error handling
            System.out.println("Error when trying to send chats to the server");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } 
        catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the app */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientApp().setVisible(true);
            }
        });
        
        String chatIn = "";
        try {
            //create socket and connects it to the port 1201
            socket = new Socket("127.0.0.1", 1201);
            //create data input and output stream from the connected socket
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // socket listen to the connected server until the socket closes/disconnects
            while(!(socket.isClosed())){
                //read the server's output chats
                chatIn = in.readUTF();
                //if clients chatted "exit", disconnects, close the data stream and socket
                if (chatOut.equalsIgnoreCase("exit")) {
                    in.close();
                    out.close();
                    socket.close();
                    chatArea.setText(chatArea.getText().trim() + "\n---You've been disconnected---");
                }
                else{
                    //show the server's output chats to the chatBox
                    chatArea.setText(chatArea.getText().trim() + "\nServer : " + chatIn);
                }
            }
        } 
        catch (Exception e) {
            //for handling error
            System.out.println("Error when trying to connects to the server");
        }
    }

    // Variables declaration
    private static javax.swing.JTextArea chatArea;
    private javax.swing.JTextField chatInput;
    private javax.swing.JButton chatSend;
    private javax.swing.JScrollPane jScrollPane1;
}
