package client;

import java.util.regex.Pattern;

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

import swe681poker.Card;


public class LoginInterface {

	final int WIDTH = 480, HEIGHT = 360;
	final int CENTERX = WIDTH/2, CENTERY = HEIGHT/2;

    protected Shell shell;
    Display display;
    PokerClient client;
    private GameThread gamethread;
    String tablename;
	private Text bidtext;
	private Label handlabel;

    public LoginInterface() {
    	client = new PokerClientDummy();
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
		String[] gameliststr = client.getGameList();
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
		String[] tableliststr = client.getTableList();
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
    				joinTable(tablelist.getSelection()[0]);
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

	private void joinTable(String tablename) {
		this.tablename = tablename;
		showGame();
		showJoinGame();
	}
	
	void showGame(){
		clearContents();
		
    	Label title = new Label(shell, SWT.CENTER);
    	title.setText(tablename);
    	title.setBounds(10, 10, WIDTH - 20, 30);
		
		showPlayers();
		showPot();
		showCommunityCards();
		
		Rectangle rect = new Rectangle(CENTERX + 30, HEIGHT - 70, 70, 30);
		Button backbtn = createButton(shell, SWT.NONE, rect, "Main Menu");
		backbtn.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			gamethread.quit();
    			client.leaveTable();
    			showMainMenu();
    		}
    	});		
	}

	private void showPlayers() {
		String[] playerlist = client.getPlayerList();
		Rectangle playerrect = new Rectangle(10, 40, WIDTH/3, 20);
    	Label title = new Label(shell, SWT.LEFT);
    	title.setText("Players");
    	title.setBounds(playerrect);
    	
    	int dealer = client.getDealer();
    	int smallblind = (dealer + 1)%playerlist.length;
    	int bigblind = (dealer + 2)%playerlist.length;
    	
		for(int i=0; i<playerlist.length; i++){
			playerrect.y += 22; 
			Label player = new Label(shell, SWT.LEFT);
			String seat = Integer.toString(i) + ". ";
			if(dealer == i)
				seat += "D ";
			if(smallblind == i)
				seat += "S ";
			if(bigblind == i)
				seat += "B ";
			player.setText(seat + playerlist[i]);
			player.setBounds(playerrect);
		}

		playerrect.y = HEIGHT - 70;
		playerrect.width = 300;
    	Label legend = new Label(shell, SWT.LEFT);
    	legend.setText("D - dealer, S - small blind, B - big blind");
    	legend.setBounds(playerrect);
	}

	private void showPot() {
		String pot = client.getPot();
		Rectangle potrect = new Rectangle(CENTERX, 120, CENTERX - 10, 20);
    	Label potlabel = new Label(shell, SWT.LEFT);
    	potlabel.setText("Pot: $"+pot);
    	potlabel.setBounds(potrect);
	}

	private void showCommunityCards() {
		Card[] cards = client.getCommunityCards();
		String cardlist = "";
		for(Card card:cards){
			cardlist += card.suit.symbol + card.value.symbol + " ";
		}
		
		Rectangle cardrect = new Rectangle(CENTERX, 80, CENTERX - 10, 20);
    	Label title = new Label(shell, SWT.LEFT);
    	title.setText("Cards: " + cardlist);
    	title.setBounds(cardrect);		
		
	}

	private void showJoinGame() {
		final int chips = client.getChips();
		Rectangle rect = new Rectangle(10, HEIGHT - 110, 70, 30);
    	Label chipstext = new Label(shell, SWT.NONE);
    	chipstext.setText("Chips: $");
    	chipstext.setBounds(rect);
    	rect.x += 75;
    	final Text chipsbox = new Text(shell, SWT.BORDER);
    	chipsbox.setBounds(rect);
    	chipsbox.setText(Integer.toString(chips));
		Button joinbtn = createButton(shell, SWT.NONE, rect, "Join game");
		joinbtn.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			String chipstr = chipsbox.getText();
    			int joinchips = 0;
    			if(Pattern.matches("^[0-9]*$", chipstr)){
    				joinchips = Integer.parseInt(chipstr);
    				if(joinchips > chips)
    					joinchips = chips;
    				if(joinchips == 0)
    					return;
    			} else return;
    			if(client.joinGame(joinchips))
    				startGame();
    			else return;
    		}
    	});
	}

	private void startGame() {
		bidtext = new Text(shell, SWT.BORDER|SWT.RIGHT);
		Rectangle rect = new Rectangle(10, HEIGHT - 110, 70, 30);
    	Label chipstext = new Label(shell, SWT.NONE);
    	chipstext.setText("Bid: $");
    	chipstext.setBounds(rect);
    	rect.x += 75;
    	bidtext.setBounds(rect);
    	bidtext.setText("0");
		Button bidbtn = createButton(shell, SWT.NONE, rect, "Bid");
		bidbtn.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			String chipstr = bidtext.getText();
    			int bid = 0;
    			if(Pattern.matches("^[0-9]*$", chipstr)){
    				bid = Integer.parseInt(chipstr);
    				if(gamethread != null && gamethread.active)
    					gamethread.setBid(bid);
    			} else return;
    		}
    	});
		gamethread = new GameThread(this);
		Thread thread = new Thread(gamethread);
		thread.start();
	}

	public void setBid(int newbid) {
		final int bid = newbid;
		display.asyncExec(new Runnable(){
			@Override
			public void run(){
				bidtext.setText(""+bid);
			}
		});
		
	}

	public void setHand(Card[] cards) {
		final String card1 = cards[0].symbol;
		final String card2 = cards[1].symbol;
		display.asyncExec(new Runnable(){
			@Override
			public void run(){
				handlabel.setText("Hand: "+card1+" "+card2);
			}
		});
	}
}
