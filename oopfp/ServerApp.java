package oopfp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp extends javax.swing.JFrame{
	/**
     * Creates new ServerApp
     */
    //create the necessary object
    static ServerSocket serverSocket;
    static Socket socket;
    static DataInputStream in;
    static DataOutputStream out;
    static boolean chats = false;

    //Constructor class
    public ServerApp() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // a helper method to create the GUI
    private void initComponents() {

        //create the GUI objects
        jScrollPane1 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        chatInput = new javax.swing.JTextField();
        chatSend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server App");

        chatArea.setColumns(20);
        chatArea.setRows(5);
        jScrollPane1.setViewportView(chatArea);

        //first prefix chat input
        chatInput.setText("Hello dear patron of Freddy Fazbear's Pizzeria. How may i Help you today?");

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chatInput)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chatSend, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
            String chatOut = "";
            chatOut = chatInput.getText().trim(); //getting input
            if (!chatOut.equalsIgnoreCase("")){//For validating input(must not empty)
                //if the client ask for liveChat only then the server can start chatting to the client
                if(chats){
                    //writing input to the dataStream and the chatBox
                    out.writeUTF(chatOut);
                    chatArea.setText(chatArea.getText().trim() + "\nServer : " + chatOut);
                    chatInput.setText("");
                }
            }
        } 
        catch (Exception e) {
            //For error handling
            System.out.println("Error when trying to send live chats to the customers");
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
            java.util.logging.Logger.getLogger(ServerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the app */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerApp().setVisible(true);
            }
        });
        
        boolean connecting = true;
        String chatIn = "";
        FreddyCSProtocol scp = new FreddyCSProtocol();//the chat-answer protocol system
        try {
            //create serverSocket port 1201 and waiting to connect to other socket
            serverSocket = new ServerSocket(1201);
            socket = serverSocket.accept();
            //create data input and output stream from the connected socket
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            chatArea.setText("This is the server log\nServer will answer the customer automatically according to the Freddy CS Protocol\nServer can only chat to the customer when the customer asks to do a live chat");

            // serverSocket listen to the connected socket with condition client's chat value not "exit"
            while(!chatIn.equalsIgnoreCase("exit")){
                //read the client's chats and show it to the chatBox
                chatIn = in.readUTF();
                chatArea.setText(chatArea.getText().trim() + "\nClient : " + chatIn);
                try {
                    //process the client's chats to a processInput protocol method and generate the proper answer/question
                    String chatOuts = "";
                    chatOuts = scp.processInput(chatIn);

                    //Logic for transition when clients want to chat live with the server
                    if(!scp.chatLiveServer){
                        out.writeUTF(chatOuts);
                    }
                    else{
                        if (connecting) {
                            out.writeUTF(chatOuts);
                            chats = true;
                            connecting = false;
                        }
                    }
                } 
                catch (Exception e) {
                    //for handling error
                    System.out.println("Error when trying to send prefix-message to the customer");
                }
            }
            //client has disconnected, closing the data stream and socket
            out.writeUTF("client close");
            chatArea.setText(chatArea.getText().trim() + "\n---Client Disconnected---");
            chats = false;
            connecting = true;
            in.close();
            out.close();
            socket.close();
        } 
        catch (Exception e) {
            //for handling error
            System.out.println("Error when trying to create the server and connects to the customer");
        }
    }

    // Variables declaration
    private static javax.swing.JTextArea chatArea;
    private javax.swing.JTextField chatInput;
    private javax.swing.JButton chatSend;
    private javax.swing.JScrollPane jScrollPane1;
}
