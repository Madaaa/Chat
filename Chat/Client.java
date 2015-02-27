import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class Client {
    DataInputStream dis;
    DataOutputStream dos;

    JFrame frame = new JFrame("Chat");
    JTextField textField = new JTextField(40);
    JTextArea messageArea = new JTextArea(15, 40);
    JTextArea listName = new JTextArea(15, 20);
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Options");
    JMenuItem menuItem1, menuItem2, menuItem3;
    String name;


    public Client() {
        menuBar.add(menu);
        menuItem1 = new JMenuItem("Online users");
        menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                listName.setText("");
                try {
                    dos.writeUTF("Name");
                } catch (Exception g) {
                }
            }
        });

        menuItem2 = new JMenuItem("Exit");
        menuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        menuItem3 = new JMenuItem("Change NickName");
        menuItem3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    dos.writeUTF("Change");
                } catch (Exception g) {
                }
            }
        });
        menu.add(menuItem1);
        menu.add(menuItem3);
        menu.add(menuItem2);

        frame.setJMenuBar(menuBar);
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.getContentPane().add(new JScrollPane(listName), "East");
        frame.pack();


        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF(textField.getText());
                } catch (Exception f) {
                }
                textField.setText("");
            }
        });
    }

    private String getServerAddress() {
        return JOptionPane.showInputDialog(frame, "Enter IP Address of the Server:", "Welcome to Chat", JOptionPane.INFORMATION_MESSAGE);
    }

    private String getName() {
        return JOptionPane.showInputDialog(frame, "Choose a name:", "Screen name selection", JOptionPane.PLAIN_MESSAGE);
    }

    private String changeName() {
        return JOptionPane.showInputDialog(frame, "Enter the new nickname:", "Chnage your nickname", JOptionPane.PLAIN_MESSAGE);
    }

    private void run() throws IOException {
        String serverAddress = "";

        serverAddress = getServerAddress();

        if (serverAddress.equals(""))
            System.out.println("Conectare esuata");
        else {
            try {
                Socket cs = new Socket(serverAddress, 1111);
                dis = new DataInputStream(cs.getInputStream());
                dos = new DataOutputStream(cs.getOutputStream());
            } catch (Exception e) {
                System.out.println("Wrong Server Adress");
            }
        }

        try {
            int ok = 0;
            while (true) {

                final String input = dis.readUTF();

                if (input.startsWith("TypeANickname")) {
                    name = getName();
                    dos.writeUTF(name);
                } else if (input.startsWith("NameAccepted")) {
                    textField.setEditable(true);
                    if (ok == 0) {
                        messageArea.append("Welcome, " + name + "!\n");
                        ok = 1;
                    }

                } else if (input.startsWith("Message")) {
                    messageArea.append(input.substring(8) + "\n");
                } else if (input.startsWith("List")) {
                    listName.append(input.substring(4) + "\n");
                } else if (input.startsWith("Change1")) {
                    dos.writeUTF("New" + changeName());
                } else if (input.startsWith("PM")) {
                    messageArea.append(input.substring(2) + '\n');
                } else if (input.startsWith("ER")) {
                    messageArea.append(input.substring(2) + '\n');
                } else if (input.startsWith("Er1")) {
                    messageArea.append(input.substring(3) + '\n');
                }


            }
        } catch (Exception e) {
            System.out.println("Actiune abandonata.");
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client();

        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);

        try {
            client.run();
        } catch (Exception e) {
            System.out.println("Actiune abandonata");
        }
    }
}
        
        
     
        
  
