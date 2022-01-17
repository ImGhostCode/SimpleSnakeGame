import javax.swing.JFrame;

public class SnakeFrame extends JFrame {
    SnakePanel panel = new SnakePanel();
    SnakeFrame (){

    
        this.add(panel);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);

    }

    

}
