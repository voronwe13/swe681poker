package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.LinkedList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;

import server.ServerThreadSsl;

public class LoginInterface {

	final int WIDTH = 480, HEIGHT = 360;
	final int CENTERX = WIDTH/2, CENTERY = HEIGHT/2;

    protected Shell shell;
    private Display display;
    private PokerClient client;


    public LoginInterface() {
    	client = new PokerClient();
    }

    /*
     * public static IvParameterSpec ivspecClient() { return ivspec; }
     * 
     * public static SecretKey sessionKeyClient() { return sessionKey; }
     * 
     * public static String nonceStringClient() { return nonceString; }
     */
    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String[] args) {
    	try {
    		LoginInterface window = new LoginInterface();
    		window.open();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    /**
     * Open the window.
     */
    public void open() {
    	display = Display.getDefault();
    	createLoginContents();
    	shell.open();
    	shell.layout();
    	while (!shell.isDisposed()) {
    		if (!display.readAndDispatch()) {
    			display.sleep();
    		}
    	}
    	display.dispose();
    }

    /**
     * Create contents of the window.
     */
    protected void createLoginContents() {
    	shell = new Shell();
    	shell.setSize(WIDTH, HEIGHT);
    	shell.setText("SecurePoker");
    	showLogin(false);

    }
    
    protected void showLogin(boolean withloginerror) {
    	clearContents();
    	Label title = new Label(shell, SWT.CENTER);
    	title.setText("Welcome to SecurePoker");
    	title.setBounds(10, 10, WIDTH - 20, 40);
    	
    	if(withloginerror){
    		Label loginerror = new Label(shell, SWT.NONE);
    		loginerror.setText("invalid username or password");
    		loginerror.setBounds(CENTERX/2, CENTERY/2 - 20, 200, 20);
    		loginerror.setForeground(display.getSystemColor(SWT.COLOR_RED));
    	}
    	
    	Label usernametxt = new Label(shell, SWT.NONE);
    	usernametxt.setText("Username:");
    	usernametxt.setBounds(CENTERX/2, CENTERY/2, 70, 20);
    	final Text usernamebox = new Text(shell, SWT.BORDER);
    	usernamebox.setBounds(CENTERX/2 + 75, CENTERY/2, 100, 20);
    	
    	Label passwordtxt= new Label(shell, SWT.NONE);
    	passwordtxt.setText("Password:");
    	passwordtxt.setBounds(CENTERX/2, CENTERY/2+45, 70, 20);
    	final Text passwordbox = new Text(shell, SWT.PASSWORD | SWT.BORDER);
    	passwordbox.setBounds(CENTERX/2 + 75, CENTERY/2+45, 100, 20);

    	Button btnsignup = new Button(shell, SWT.NONE);
    	btnsignup.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			showCreateUser(null, null);
    		}
    	});
    	btnsignup.setBounds(140, 239, 95, 28);
    	btnsignup.setText("Sign Up");
    	
    	Button btnsignin = new Button(shell, SWT.NONE);
    	btnsignin.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			String response = client.sendCredentials(usernamebox.getText(), passwordbox.getText(), true);
    			if("success".equals(response))
    				showMainMenu();
    			else 
    				showLogin(true);
    		}
    	});
    	btnsignin.setBounds(240, 239, 95, 28);
    	btnsignin.setText("Log In");
		
	}

	protected void showCreateUser(String usernameerror, String passworderror) {
    	clearContents();
    	Label title = new Label(shell, SWT.CENTER);
    	title.setText("Welcome to SecurePoker");
    	title.setBounds(10, 10, WIDTH - 20, 40);
    	
    	int textwidth = 100;
    	
    	if(usernameerror != null){
    		Label nameerror = new Label(shell, SWT.NONE);
    		nameerror.setText(usernameerror);
    		nameerror.setBounds(CENTERX/2, CENTERY/2 - 20, 220, 20);
    		nameerror.setForeground(display.getSystemColor(SWT.COLOR_RED));
    	}
    	
    	Label usernametxt = new Label(shell, SWT.NONE);
    	usernametxt.setText("Username:");
    	usernametxt.setBounds(CENTERX/2, CENTERY/2, textwidth, 20);
    	final Text usernamebox = new Text(shell, SWT.BORDER);
    	usernamebox.setBounds(CENTERX/2 + 5 + textwidth, CENTERY/2, 100, 20);
    	
    	if(passworderror != null){
    		Label passerror = new Label(shell, SWT.NONE);
    		passerror.setText(passworderror);
    		passerror.setBounds(CENTERX/2, CENTERY/2 + 25, 220, 20);
    		passerror.setForeground(display.getSystemColor(SWT.COLOR_RED));
    	}
    	
    	Label passwordtxt = new Label(shell, SWT.NONE);
    	passwordtxt.setText("Password:");
    	passwordtxt.setBounds(CENTERX/2, CENTERY/2+50, textwidth, 20);
    	final Text passwordbox = new Text(shell, SWT.PASSWORD | SWT.BORDER);
    	passwordbox.setBounds(CENTERX/2 + 5 + textwidth, CENTERY/2+50, 100, 20);

    	Label passwordtxt2 = new Label(shell, SWT.NONE);
    	passwordtxt2.setText("Confirm password:");
    	passwordtxt2.setBounds(CENTERX/2, CENTERY/2+75, textwidth, 20);
    	final Text passwordbox2 = new Text(shell, SWT.PASSWORD | SWT.BORDER);
    	passwordbox2.setBounds(CENTERX/2 + 5 + textwidth, CENTERY/2+75, 100, 20);
    	
    	Button btnsignup = new Button(shell, SWT.NONE);
    	btnsignup.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			String pass1 = passwordbox.getText();
    			String pass2 = passwordbox2.getText();
    			if(!pass1.equals(pass2)){
    				showCreateUser(null, "passwords don't match.");
    				return;
    			}
    			
    			if(!checkSecurity(pass1)){
    				showCreateUser(null, "password must be at least 8 characters");
    				return;
    			}
    			String response = client.sendCredentials(usernamebox.getText(), passwordbox.getText(), true);
    			if("usernametaken".equals(response))
    				showCreateUser("username not available", null);
    			else if("success".equals(response))
    				showMainMenu();
    		}
    	});
    	btnsignup.setBounds(140, 239, 95, 28);
    	btnsignup.setText("Sign Up");
    	
    	Button cancelbtn = new Button(shell, SWT.NONE);
    	cancelbtn.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			showLogin(false);
    		}
    	});
    	cancelbtn.setBounds(240, 239, 95, 28);
    	cancelbtn.setText("Cancel");
	}

	protected boolean checkSecurity(String pass1) {
		
		return pass1.length() >= 8;
	}

	private void clearContents(){
    	for (Control child : shell.getChildren()) {
    		child.dispose();
    	}
    }
    
    Button createButton(Shell shell, int style, Rectangle rect, String text){
    	Button button = new Button(shell, style);
    	button.setBounds(rect);
    	button.setText(text);
    	return button;
    }
    
    private void showMainMenu(){
    	clearContents();
    	Label title = new Label(shell, SWT.CENTER);
    	title.setText("Welcome to SecurePoker");
    	title.setBounds(10, 10, WIDTH - 20, 40);
    	
    	int buttonwidth = 200, buttonheight = 30;
    	int halfbtnw = buttonwidth/2;
    	Rectangle buttonrect = new Rectangle(CENTERX - halfbtnw, 60, buttonwidth, buttonheight);
    	Button jointable = createButton(shell, SWT.NONE, buttonrect, "Join a Table");
    	jointable.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			showTableMenu();
    		}
    	});
    	buttonrect.y += buttonheight + 5;
    	Button seescores = createButton(shell, SWT.NONE, buttonrect, "See Leaderboard");
    	seescores.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			showLeaderBoard();
    		}
    	});
    	buttonrect.y += buttonheight + 5;
    	Button seegames = createButton(shell, SWT.NONE, buttonrect, "See Completed Games");
    	seegames.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			showOldGames();
    		}
    	});    	
    }

	protected void showOldGames() {
		String[] gameliststr = new String[0]; //getGameList();
		clearContents();
    	Label title = new Label(shell, SWT.CENTER);
    	title.setText("Previous games");
    	title.setBounds(10, 10, WIDTH - 20, 40);
    	
		final List gamelist = new List(shell, SWT.SINGLE | SWT.BORDER);
		int listwidth = 200;
		int halfwidth = listwidth/2;
		gamelist.setBounds(CENTERX-halfwidth, 60, listwidth, HEIGHT - 140);
		for(int i=0; i<gameliststr.length; i++){
			gamelist.add(gameliststr[i]);
		}
		
		Rectangle rect = new Rectangle(CENTERX-halfwidth, HEIGHT-75, listwidth/2 - 5, 30);
		Button selectbtn = createButton(shell, SWT.NONE, rect, "Select Game");
		selectbtn.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			int selected = gamelist.getSelectionIndex();
    			showGameResult(selected);
    		}
    	});
		rect.x += rect.width + 10;
		Button backbtn = createButton(shell, SWT.NONE, rect, "Main Menu");
		backbtn.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			showMainMenu();
    		}
    	});
	}

	protected void showGameResult(int selected) {
		// TODO Auto-generated method stub
		
	}

	protected void showLeaderBoard() {
		// TODO request leaderboard from server
		
	}

	protected void showTableMenu() {
		String[] tableliststr = new String[0]; //getGameList();
		clearContents();
    	Label title = new Label(shell, SWT.CENTER);
    	title.setText("Open Tables");
    	title.setBounds(10, 10, WIDTH - 20, 40);
    	
		final List tablelist = new List(shell, SWT.MULTI | SWT.BORDER);
		int listwidth = 200;
		int halfwidth = listwidth/2;
		tablelist.setBounds(CENTERX-halfwidth, 60, listwidth, HEIGHT - 140);
		for(int i=0; i<tableliststr.length; i++){
			tablelist.add(tableliststr[i]);
		}
		
		Rectangle rect = new Rectangle(CENTERX-halfwidth, HEIGHT-75, listwidth/2 - 5, 30);
		Button selectbtn = createButton(shell, SWT.NONE, rect, "Select Table");
		selectbtn.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			int selected = tablelist.getSelectionIndex(); 
    			if(client.selectTable(selected))
    				joinTable();
    		}
    	});
		rect.x += rect.width + 10;
		Button backbtn = createButton(shell, SWT.NONE, rect, "Main Menu");
		backbtn.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			showMainMenu();
    		}
    	});
	}

	private void joinTable() {
		clearContents();
		showPlayers();
		showPot();
		showCommunityCards();
		showJoinGame();
	}

	private void showPlayers() {
		String[] playerlist = client.getPlayerList();
		Rectangle playerrect = new Rectangle(10, 40, WIDTH/3, 20);
    	Label title = new Label(shell, SWT.CENTER);
    	title.setText("Players");
    	title.setBounds(10, 10, WIDTH - 20, 40);
    	
    	int dealer = client.getDealer();
    	
		for(int i=0; i<playerlist.length; i++){
			
		}
		
	}

	private void showPot() {
		// TODO Auto-generated method stub
		
	}

	private void showCommunityCards() {
		// TODO Auto-generated method stub
		
	}

	private void showJoinGame() {
		// TODO Auto-generated method stub
		
	}
}
