package client;
  
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
  
public class ActionListenerTest implements ActionListener {
  
    JButton button;
    JFrame frame;
    JTextArea textArea;
  
    public ActionListenerTest() 
    {
        button = new JButton("Click Me");
        frame = new JFrame("ActionListener Test");
        textArea = new JTextArea(5, 40);
        button.addActionListener(this);
        textArea.setLineWrap(true);
        frame.setLayout(new BorderLayout());
        frame.add(textArea, BorderLayout.NORTH);
        frame.add(button, BorderLayout.SOUTH);
        frame.pack();
  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
  
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        textArea.setText(textArea.getText().concat("You have clicked the buttonn"));
        
    }
  
}