import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.plaf.DimensionUIResource;

public class SnakePanel extends JPanel implements Action {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_HEIGHT*SCREEN_WIDTH)/UNIT_SIZE;
    static final int delay = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 5;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean isRunning = false;
    Timer timer;
    Random random;

    JButton retryButton;
    JButton exitButton;

    SnakePanel (){

        random = new Random();
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setPreferredSize(new DimensionUIResource(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame (){
        newApple();
        isRunning = true;
        timer = new Timer(delay, this);
        
        timer.start(); 
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if(isRunning) {
			/*
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			*/
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			for(int i = 0; i< bodyParts;i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					//g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}			
			}

             g.setColor(Color.RED);
             g.setFont( new Font("Ink Free",Font.BOLD, 40));
             FontMetrics fontMetrics = getFontMetrics(g.getFont());
             g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - fontMetrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        } else 
            gameOver(g);  
    }

    public void newApple (){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move(){
        for (int i = bodyParts; i > 0 ; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
            case 'U':
                y[0] = y[0]-UNIT_SIZE;
                break;
            
                case 'D':
                y[0] = y[0]+UNIT_SIZE;
                break;
            
                case 'L':
                x[0] = x[0]-UNIT_SIZE;
                break;
            
                case 'R':
                x[0] = x[0]+UNIT_SIZE;
                break;
            
        }
    }

    public void checkApple(){
        if (x[0] == appleX && y[0] == appleY){
            applesEaten++;
            bodyParts++;
            newApple();
        }
    }

    public void checkCollisions(){
        		//checks if head collides with body
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				isRunning = false;
			}
		}
		//check if head touches left border
		if(x[0] < 0) {
			isRunning = false;
		}
		//check if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			isRunning = false;
		}
		//check if head touches top border
		if(y[0] < 0) {
			isRunning = false;
		}
		//check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			isRunning = false;
		}
		
		if(!isRunning) {
			timer.stop();
		}
    }


    public void gameOver(Graphics g){
        //Score
		 g.setColor(Color.red);
		 g.setFont( new Font("Ink Free",Font.BOLD, 40));
		 FontMetrics metrics1 = getFontMetrics(g.getFont());
		 g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		 //Game Over text
		 g.setColor(Color.red);
		 g.setFont( new Font("Ink Free",Font.BOLD, 75));
		 FontMetrics metrics2 = getFontMetrics(g.getFont());
		 g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

        retryButton = new JButton("Retry");
        retryButton.setFocusable(true);
        exitButton = new JButton("Exit");
        exitButton.setFocusable(true);
        retryButton.setBounds(200, 400, 75, 50);
        exitButton.setBounds(285, 400, 75, 50);
        this.add(retryButton);
        retryButton.addActionListener(this);
        this.add(exitButton);
        exitButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning){
            move();
            checkApple();
            checkCollisions();
        }
        
        if (e.getSource() == retryButton){
            new SnakeFrame();
        }

        if (e.getSource() == exitButton){
            System.exit(0);
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
        } 
    }

	@Override
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}   
}
