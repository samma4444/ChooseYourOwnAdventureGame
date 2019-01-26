import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Character.isDigit;

public class main extends JPanel {

    static JPanel panel; // main drawing panel
    static JFrame frame; // window frame which contains the panel
    static final int WINDOW_WIDTH = 1600; // width of display window
    static final int WINDOW_HEIGHT = 900;// height of display window

    // stage of game
    static int gameStage = 0;
    static final int MENU = 0;
    static final int INSTRUCTIONS = 1;
    static final int IN_PLAY = 2;
    static final int END_GAME = 3;

    static Image menu; // menu background
    static Image gameScreen; // in game background
    static Image instructions; // shows instructions
    static Image endScreen; // game over

    static boolean inMenuMode = true; // true when we are waiting for a user to make a specific choice
    static boolean waitingForChoice = false; // true when we are waiting for a user to make a specific choice
    static boolean endGame = false;
    static boolean inInstructionMode = false;

    static int nextChoice = 0;
    static int maxChoice = 0;

    static Character cole;

    static Page currentPage;

    static String textForCurrentPage = "";
    static List<String> options = new ArrayList<>();

    static Color c = new Color(150, 10, 10);

    public static void main(String[] args) {

        Toolkit tk = Toolkit.getDefaultToolkit();

        menu = tk.getImage("src\\images\\" + "Menu.png");
        instructions = tk.getImage("src\\images\\" + "Instruction.png");
        gameScreen = tk.getImage("src\\images\\" + "Background.png");
        endScreen = tk.getImage("src\\images\\" + "endGame.png");

        // Create Frame and Panel to display graphics in
        panel = new main();

        panel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        frame = new JFrame("12 Monkeys By Sam and Zach"); // set title of window
        frame.add(panel);


        // add a key input listener (defined below) to our canvas so we can
        // respond to key pressed
        frame.addKeyListener(new KeyInputHandler());

        // exits window if close button pressed
        frame.addWindowListener(new ExitListener());

        // request the focus so key events come to the frame
        frame.requestFocus();
        frame.pack();
        frame.setVisible(true);


        init();

    }

    // paints all screens
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // calls the paintComponent method of JPanel to
        g.setColor(Color.gray);
        if (gameStage == MENU) {
            g.drawImage(menu, 0, 0, this); // set background
            // display instructions
        } else if (gameStage == INSTRUCTIONS) {
            g.drawImage(instructions, 0, 0, this); // set background
            // are part of a picture
            // display the game
        } else if (gameStage == IN_PLAY) {
            System.out.println(cole.getFavour());
            g.drawImage(gameScreen, 0, 0, this);
            g.setFont(new Font("Sanserif", Font.BOLD, 25));
            g.setColor(c);
            int y = 750;
            int y1 = 120;

            List<String> strs = wrap(currentPage.getMsg());
            for (String s : strs) {
                g.drawString(s, 300, y1);
                y1 += 25;
            }
            g.setFont(new Font("Sanserif", Font.BOLD, 40));
            g.setColor(c);
            for (String s : options) {
                g.drawString(s, 300, y);
                y += 50;
            }
        } else if (gameStage == END_GAME) {
            g.drawImage(endScreen, 0, 0, this); // set background
        }
    } // paintComponent

    // shows the menu
    private static void showMenu() {
        // display this stage of the game
        gameStage = MENU;
        inMenuMode = true;
        endGame = false;
        panel.repaint();
    } // startGame

    // shows the instructions
    private static void showInstructions() {
        gameStage = INSTRUCTIONS;
        inMenuMode = false;
        inInstructionMode = true;
        panel.repaint();
    } // startGame

    // actually plays the game
    private static void playGame() {
        // display this stage of game
        gameStage = IN_PLAY;
        inMenuMode = false;
        waitingForChoice = true;
        inInstructionMode = false;

        textForCurrentPage = currentPage.getMsg();
        options = currentPage.getOptions();
        panel.repaint();

    } // playGame

    private static void endGame() {
        gameStage = END_GAME;
        inMenuMode = false;
        waitingForChoice = false;
        endGame = true;
        panel.repaint();
    }

    // takes in key commands from user
    private static class KeyInputHandler extends KeyAdapter {

        public void keyTyped(KeyEvent e) {

            // quit if the user presses "escape"
            if (e.getKeyChar() == 27) {
                System.exit(0);
            } else if (inMenuMode) {
                if (e.getKeyChar() == 'a') {
                    showInstructions();
                    gameStage = 1;
                } else if (e.getKeyChar() == 'b') {
                    playGame();
                    gameStage = 2;
                }
            } else if (inInstructionMode) {
                if (e.getKeyChar() == 'a') {
                    playGame();
                    gameStage = 2;
                }
            } else if (waitingForChoice) {
                int x = nextChoice = e.getKeyChar() - '0';
                maxChoice = currentPage.getFilteredOptions().size();
                if (maxChoice == 0 && e.getKeyChar() == KeyEvent.VK_ENTER) {
                    endGame();
                } else if (maxChoice != 0 && isDigit(e.getKeyChar()) && x > 0 && x <= maxChoice) {
                    nextChoice = e.getKeyChar() - '0';
                    currentPage = currentPage.nextPage(nextChoice - 1);
                    currentPage.makeOptions();
                    playGame();
                }
            } else if (endGame) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    init();
                    showMenu();
                }
            }

        } // keyTyped
    } // KeyInputHandler class

    private static class ExitListener extends WindowAdapter {
        public void windowClosing(WindowEvent event) {
            System.exit(0);
        } // windowClosing
    } // ExitListener

    private static void init() {
        cole = new Character("Cole");
        currentPage = null;
        inMenuMode = true;
        waitingForChoice = false;
        gameStage = 0;

        Page C6 = new Page(new ArrayList<Page>(), cole, "Continue", "You scan through the crowd at the security check, and see Dr. Peters crossing through. You force your way through and chase after him. Your leg is still injured, but close to healed due to the amount of time spent recovering. You are able to match up to his speed, and keep an equal distance to him as you pull out your gun. You are about to make a shot, but then are turned around by Dr. Railly’s scream. You see a policeman pull out a gun, and desperately fire your gun at Dr. Peters, but unfortunately your bullet streaks over his head due to your inexperience and distance. You are spun around by a bullet to the back and fall. Dr. Railly’s tear-filled face is the last thing you see before your vision fades out.", 0, 0);
        Page C7 = new Page(new ArrayList<Page>(), cole, "Continue", "You scan through the crowd at the security check, and see Dr. Peters crossing through. You force your way through and chase after him. Due to your injured leg, you are unable to match his speed and are gradually left behind. You pull out your gun, desperate to make a shot at him, but are turned around by Dr. Railly’s scream. You see a policeman pull out a gun and shoot at you, striking you in the chest. Dr. Railly’s tear-filled face is the last thing you see before your vision fades out.", 0, 0);

        Page C51 = new Page(new ArrayList<Page>() {{
            add(C6);
            add(C7);
        }}, cole, "Don’t transplant teeth", "Upon departure from the motel, you Dr. Railly makes a call with a nearby phone, to the number you told her about which was traced by the future. You feel your horror growing as you realize that the message she left was the exact same one that you heard being played to you in the future. She tells you to calm down and helps you apply a disguise in a movie theater, then procures tickets to a flight out of the state. On your way to the airport, you find out that the objective of the Army of the 12 Monkeys was just to free animals. Maybe it wasn’t so bad after all. Upon arriving at the airport, you call the number again and inform the future that they were wrong and the 12 Monkeys weren’t the ones who did it, but are soon approached by Jose giving you a gun, saying that his orders were to shoot Dr. Railly if you didn’t follow instructions. You find from Dr. Railly that the assistant of Dr. Goines, a man named Dr. Peters, had the briefcase with the virus and was ready to spread it, and that he was headed to a flight to San Francisco.", 0, 0);
        Page C5 = new Page(new ArrayList<Page>() {{
            add(C6);
            add(C7);
        }}, cole, "Transplant teeth", "Upon departure from the motel, you Dr. Railly makes a call with a nearby phone, to the number you told her about which was traced by the future. You feel your horror growing as you realize that the message she left was the exact same one that you heard being played to you in the future. She tells you to calm down and helps you apply a disguise in a movie theater, then procures tickets to a flight out of the state. On your way to the airport, you find out that the objective of the Army of the 12 Monkeys was just to free animals. Maybe it wasn’t so bad after all. Upon arriving at the airport, you call the number again and inform the future that they were wrong and the 12 Monkeys weren’t the ones who did it, but are soon approached by Jose giving you a gun, saying that his orders were to shoot Dr. Railly if you didn’t follow instructions. You find from Dr. Railly that the assistant of Dr. Goines, a man named Dr. Peters, had the briefcase with the virus and was ready to spread it, and that he was headed to a flight to San Francisco.", 0, 0);

        Page C4 = new Page(new ArrayList<Page>() {{
            add(C51);
            add(C5);
        }}, cole, "Continue", "When entering the motel, the man at the counter says some incomprehensible things. You ignore him and push past, entering a room. Dr. Railly talks to you about your mental divergence and claims that you actually were from the future. Shortly after, a man enters, accusing Railly of more incomprehensible things. You don’t pay attention to him until he hits Railly. In a fit of anger, you clock him over the head with the nearby telephone, beat him up and drag him into the bathtub, bringing a knife with you. Suddenly, you are stuck with an epiphany: you could dig out your teeth and leave them with him, forever severing the hold the scientists have on you, and enabling yourself to stay in the past with Dr. Railly, or you could accomplish your original objective and leave your teeth in, fully enjoying your pardon in the reclaimed future world.", 0, 0);
        Page C3 = new Page(new ArrayList<Page>() {{
            add(C4);
        }}, cole, "Don’t turn yourself in", "You move towards the nearby policeman with the intention to turn yourself in as an insane man seeking treatment, but Railly dissuades you. She takes you to a nearby motel to discuss things with you.", 0, 0);
        Page C2 = new Page(new ArrayList<Page>() {{
            add(C4);
        }}, cole, "Turn yourself in to the police", "Disregarding Dr. Railly’s protests, you attempt to turn yourself in to the policeman nearby as an insane man seeking treatment, which he takes note of. Dr. Railly eventually stops you and drags you into a nearby motel to discuss things with you.", 0, 0);
        Page C1 = new Page(new ArrayList<Page>() {{
            add(C2);
            add(C3);
        }}, cole, "Continue", "You awaken, once again surrounded by the scientists, in the reality you hoped to leave behind. You realize with mounting dread that there’s likely no chance for you to return to the past again, and struggle against your restraints as the scientists inject you with something and leave you be. The voice from the ceiling returns again, giving you advice on what you want and how to achieve it. You talk to the scientists, claiming to want to help with the restoration of the surface. They give you information and test you on it, which you pass with flying colors. You return to the past, acquire clothing in a similar way to before, and find Dr. Railly by the building that you found information about the Army of the Twelve Monkeys at. She seems glad to see you, and pulls you away from a policeman in the corner. You consider your options: to either turn yourself in to the police and live the life of an insane patient under Dr. Railly’s care, or in the off chance that you aren’t crazy, you could instead collect information and aim to resolve the crisis threatening the future.", 0, 0);

        Page B6 = new Page(new ArrayList<Page>() {{
            add(C1);
        }}, cole, "Continue", "Taking their car, you go to the location they gave you to find Goines. You enter a rather high class party, and he tries to turn you away until you mention monkeys. He talks to you for a while, but laughs off your attempts to extract information from you. You return to where you left a furious Dr. Railly, and slowly your memories of the future distort. You think back to your time spent in the asylum: perhaps you are also mentally divergent? You decide to let the police arrest you, and spend time with Dr. Railly as her patient, when the world shifts.", 0, 0);

        Page B51 = new Page(new ArrayList<Page>() {{
            add(B6);
        }}, cole, "Get the bullet taken out", "On the way to Goins’ location, Dr. Railly asks you about your leg. You tell her that you were shot, and she immediately stops the car by a gas station to procure supplies to treat it, and remove the bullet.", 0, 3, -1, 0);
        Page B5 = new Page(new ArrayList<Page>() {{
            add(B51);
            add(B6);
        }}, cole, "Continue", "A short distance away from the confrontation with the hobos, you see a statue of a pig head that you recognized from one of the photos scientists showed you. You approach and enter the building, and confront the people inside with the gun you acquired from the hobos. From them, you learn that Jeffrey Goines, the insane man back from the asylum that aided your escape, created his own independent little army that he called “Army of the Twelve Monkeys”.", 0, 0);

        Page B41 = new Page(new ArrayList<Page>() {{
            add(B5);
        }}, cole, "Kill Them", "You proceed to curbstomp the helpless man over and over, eventually Dr Railly realizes what you’ve been doing and stops you, then exclaims, “you’ve killed him!” You force her to flee with you at gunpoint.", 0, 0);
        Page B42 = new Page(new ArrayList<Page>() {{
            add(B5);
        }}, cole, "Spare Them", "Turning towards Dr Railly’s assailant, you threaten him off with the gun. You escape together with Dr. Railly.", 0, 0);
        Page B43 = new Page(new ArrayList<Page>() {{
            add(B5);
        }}, cole, "Do not fight back", "You slowly start to comprehend the impossibility of your situation and decide to submit. Before the man can do anything, his body goes limp. The voice from the ceiling appears again, “You ain’t getting off that easily, Cole”. As you stand up, you notice the man with the gun also appears to have died, still holding a passed out Dr Railly. You quickly walk over and pick up the gun, as well as wake up the doctor, and together you escape.", 0, 0);

        Page B4 = new Page(new ArrayList<Page>() {{
            add(B41);
            add(B42);
        }}, cole, "Fight Back", "With your hand, you smash the balls of the person with the gun, sending the man tumbling, then grabbing onto him you bull rush him into a wall. Then turning towards Dr Railly’s assailant, you knock him onto the ground. You pause and consider your options: you could let them live despite their actions, or you could finish them off now to remove witnesses and rid yourself of a potential future threat.", 0, 0);

        Page B3 = new Page(new ArrayList<Page>() {{
            add(B4);
            add(B43);
        }}, cole, "Continue", "After arriving in Philadelphia, you and Dr. Railly begin searching for clues. As the two of you search for clues, you encounter a series of graffiti messages with the symbol of the Army of the Twelve Monkeys, following these clues, you and Dr Railly are lead into a run-down concert hall, packed with tents. Suddenly two people sneak up behind the two of you. One grabs Dr. Railly and begins to rummage through her bag while the other threatens you down with a gun. You are paralyzed by indecision: can you fight off these brutish aggressors with your injured knee, or will you be overrun anyways?", 0, 0);
        Page Bullet = new Page(new ArrayList<Page>() {{
            add(B3);
        }}, cole, "Treat your leg", "As the light begins to fade, the two of you decide to stop at a motel. Dr. Railly offers to have a look at your injured leg, and the two of you settle down while she slowly extracts the bullet with some of the things from a nearby gas station. You immediately feel better. The next day, the sunlight wakes you up from the first restful night’s rest you’ve had in days. You resume the journey to Philadelphia.", -3, 0);

        Page B2 = new Page(new ArrayList<Page>() {{
            add(B3);
        }}, cole, "Force Her", "You yell at Dr. Railly to “get in the car”, and she obliges due to her terror and belief of your possession of a weapon. You tell her to start driving, and she does as well. She asks you where you’re going, and you tell her Philadelphia, and the reason for it. She plays a song on the radio, and you immerse yourself in it, reminiscing to your distant childhood when you heard this last, and stick your head out the window, feeling the wind rush past your face.", 0, 0);
        Page B21 = new Page(new ArrayList<Page>() {{
            add(Bullet);
            add(B3);
        }}, cole, "Persuade Her", "You approach Dr. Railly as she’s leaving her lecture, and beg her for help. “Dr. Railly, do you still remember me? I’m James. James Cole.” She looks at you for a little while as you approach, and her eyes shine with a glint of recognition, and then she seems to look hard at your face. “I’m in a bad situation right now. I hurt my leg, have no money, and need to get to Philadelphia real bad.” She pauses, indecision reflected in her eyes, and you can tell that you just have to push a little harder to get her to help. “Please,” you plead, “I don’t have any other options”. She reluctantly agrees on the condition that you inform her of your situation. You tell her of your purpose and mission, and she doesn’t seem to believe you, and you change the subject by asking her to turn the music up, and immersing yourself in the sensations of the distant past, sticking your head out the window to feel the wind rush past your face, and reminiscing to your childhood years.", 3, 0, 2);

        Page B12 = new Page(new ArrayList<Page>() {{
            add(B2);
            add(B21);
        }}, cole, "Continue", "You are rendered helpless by the blinding agony that fills you, and the world shifts again. This time you find yourself in an alleyway, naked and helpless, with a persistent aching pain in your knee. Bracing yourself on the wall of a nearby building, you force yourself to stand. You spot in a store window that Dr. Railly from the asylum is holding a lecture, and tear down the poster to mark the location. You collect clothes again from different hobos, including a crude mask made from part of a sweater. You wait outside the lecture hall for Dr. Railly, and follow her to her car. You hesitate here. Should you force her to assist you on your quest, or should you try your best to persuade her to help you?", 0, 0);
        Page B11 = new Page(new ArrayList<Page>() {{
            add(B12);
        }}, cole, "Continue", "You are loaded into the time machine much like the first time, but this time are sent into a battlefield, filled with explosions and soldiers falling like flies around you in the smoke filled air. You are completely naked this time, bereft of any protection. A group of soldiers wearing gas masks notice you and one points a gun at your head while shouting something in an incomprehensible language at you. You shout back that you don’t understand, voice growing hoarse with desperation. Suddenly, you spot your fellow inmate and “volunteer”, Jose, with his face matted with blood and carried away on stretchers. You crawl towards him, trying to find an anchor in this chaotic world, but then the soldier shoots you in the knee.", 2, 0);

        Page B1 = new Page(new ArrayList<Page>() {{
            add(B11);
        }}, cole, "Continue", "You find yourself lying on a bed in a place with familiar lighting. A mysterious voice sounds from the ceiling, briefly conversing with you about your situation, and telling you that time travel isn’t an exact science, though the scientists are improving. You are subsequently summoned before the scientists again, who then question you about your activities. You protest that you had been sent to the wrong year, and they seem to accept your efforts, and offer you another chance.", 0, 0);

        // end of act 1 pages
        Page A41 = new Page(new ArrayList<Page>() {{
            add(B1);
        }}, cole, "Make the call now", "You borrow a phone off a kindly old grandmother who takes pity at your visibly destitute state. Dialing the number that had been provided you to beforehand, you connect to what appears to be a busy family, one that does not seem to be aware of your existence at all. Frustrated, you pace along the streets, and make eye contact with a beautiful and hauntingly familiar woman. You try to recall where you’ve seen her before, but then suddenly feel the world shift.", 0, 0);
        Page A42 = new Page(new ArrayList<Page>() {{
            add(B1);
        }}, cole, "Stay in the present", "As you are reveling in your newfound freedom, a growing sense of unease pervades you. Later that day, as you are taking in the fresh new sights, you make eye contact with a beautiful and hauntingly familiar woman. You try to recall where you’ve seen her before, but then suddenly feel the world shift.", 0, 0);
        Page A6 = new Page(new ArrayList<Page>() {{
            add(B1);
        }}, cole, "Act Sane", "Taking a deep breath, you weave an elaborate tale of intrigue, painting yourself as a tragic kidnap victim from another state, who masterfully escaped during transport after a long duration of imprisonment, and while disoriented, mistook the policemen as your captors while they were trying to detain you. The psychiatrists nod sympathetically, with the exception of Railly, the woman who questioned you before, who remains skeptical. You are released for duress, and discharged back into the streets with the clothes on your back. You ask to use the phone before leaving and call the number you were told of, but reach a busy family which does not match what was described to you. You scour the streets for information about the virus, but nobody seems to know what’s happening. You are frustrated when you feel your surroundings shift, and find yourself back in the future, away from the gloriously fresh breathable air and blue sky above ground.", 0, 3);
        Page A11 = new Page(new ArrayList<Page>() {{
            add(B1);
        }}, cole, "Continue", "After you had been sedated, the nurses allow you back into the commons room. Suddenly the insane patient from the day before pops up before you, handing to a key. Out of nowhere he then begins to scream, shouting about some monkey business, riling up the other patients. As this continues, the nurses begin to notice, and a couple rush into the room to attempt to contain him. Before they could though, he grabs you and pushes you towards the door. Due to the sedation, you feel as if your strength is gone but clinging onto the railings you manage to make it out the door. As you move through the building, an alarm is triggered, and nurses pour into the room. One initially presents himself in a kindly, helpful manager. With your overwhelming need to accomplish your mission, you decide that the best course of action is attempting to escape and you kick him across the face. As soon as he drops though, the remaining nurses rush at you and sedate you. You awaken, chained to a table and delirious with the sedatives. You lie there for a while, and feel the world shift.", 0, 0);

        Page A10 = new Page(new ArrayList<Page>() {{
            add(A11);
        }}, cole, "Continue", "You sit awake, and see the one insane patient from earlier today sitting up as well, fidgeting. He notices that you are awake as well, and crawls over several other sleeping patients to converse. He starts rambling as you spot a spider crawling by the side of your bed. You capture it and look around for a suitable container to keep it in to bring it back to the future, but see nothing convenient at hand with the insane person so close. With no other option, you decide to swallow the spider. It feels spiky and struggles as it slides down your throat. You mention that you aren’t crazy and want to escape, and he agrees with you, but tells you his dad is a famous scientist and will move him to a better location. He starts making a ruckus, yelling at the security guards and ripping open a pillow, throwing fluff everywhere. You watch the guards drag him away, and eventually go back to a fitful sleep.", 0, 0);

        Page A8 = new Page(new ArrayList<Page>() {{
            add(A10);
        }}, cole, "Trash", "You struggle against your captors, desperately trying to make sense of what is happening, but are eventually overcome by superior numbers. One of them them injects you with a fluid. Your vision begins  to blur then darken, and when you wake up, you are surrounded by other sleeping patients and it is nighttime.", 0, 0);
        Page A9 = new Page(new ArrayList<Page>() {{
            add(A10);
        }}, cole, "Stay Calm", "You struggle against your captors, desperately trying to make sense of what is happening, but are eventually overcome by superior numbers. One of them them injects you with a fluid. Your vision begins  to blur then darken, and when you wake up, you are surrounded by other sleeping patients and it is nighttime.", 0, 1);

        Page A7 = new Page(new ArrayList<Page>() {{
            add(A8);
            add(A9);
        }}, cole, "Convince them of your glorious mission", "You eloquently divulge the contents of your objective, to find information about the path of the virus in 1996 to try to reclaim the surface in the future. The scientists and doctors look at you strangely, and you begin fading out, knowing that your argument is not understood. You make one last plea to make a phone call, which is agreed to by Dr. Railly. Upon making the call, you reach what sounds like a busy family which does not match what you were expecting. Panic starts to rise in you, and you begin to recall what one of the mental patients said about being mentally divergent. Do you give in to the panic and strike out at the men around you, or do you force yourself to stay calm, and accept the possibility that you may be trapped in this unfamiliar time for the next 6 years?", 0, 0);

        Page A4 = new Page(new ArrayList<Page>() {{
            add(A41);
            add(A42);
        }}, cole, "Duck into alley", "You duck into the nearby alley, away from the prying eyes of people around you. You find a sleeping hobo, who you quickly take clothes from and change into, leaving him with the plastic bodysuit. Stepping into the street again, you still draw stares due to your poor attire, but much less compared to before. You are about to discreetly ask for information about the virus from people around you until the date on a newspaper from a nearby newspaper stand catches your eye. 1990. 6 years in the past compared to when you were supposed to be sent. You check more newspaper stands and confirm that the date is the same. Distressed with the current situation, you think about contacting the future for further instructions, but before making the call a thought hits you: instead of making the call and possibly have them pull you back to the future, you could enjoy the fresh air and warm sun for another 6 years, and find information when the time is right.", 0, 2);
        Page A5 = new Page(new ArrayList<Page>() {{
            add(A6);
            add(A7);
        }}, cole, "Question people", "You ask the people around you about the virus that will soon spread, hoping to glean information from their responses. People around you start backing up slowly, and one of them pulls a phone out of a pocket and makes a call. You become gradually more insistent with your questioning, frustrated at their lack of response. Soon, a group of men in uniforms surrounds you, and attempt to coax you into a car. Your frustration peaks, and you strike at them, furious at their lack of understanding of how important your mission is. You struggle valiantly, but to no avail: you are eventually beaten into submission, forced into restraints, and hauled onto the vehicle. You are soon injected with a large amount of sedatives and are confined in a dirty cell. An old man asks you a few questions, and is soon replaced with a young, stunningly beautiful, and hauntingly familiar woman. You don’t pay much attention to what she’s saying until you learn that you’re in the year 1990 instead of 1996. That catches your attention. After a few more questions that you answer with perfect correctness, you are then escorted to an institution that appears to be for the mentally ill. You are cleaned roughly, then shown around by a man that looks unstable. You request for a telephone to make a call back to the future, but are rejected. Soon, you stand before a group of intellectuals. Doctors and psychiatrists, who seem have power here. They remind you of the scientists of the future that sent you back here. Maybe they’d understand how important your mission is? You could either try to convince them to assist you on your mission and send you on your way, or pretend to be sane to leave the institution to continue your search.", 0, 0);


        Page A3 = new Page(new ArrayList<Page>() {{
            add(A4);
            add(A5);
        }}, cole, "Continue", "You have a choice to make here: you could question the people around you about the state of the world as it is; you are doing this for their own good after all. Alternatively, you could avoid confrontation by ducking into the nearby alley and preparing yourself for the surroundings first.", 0, 0);
        Page A2 = new Page(new ArrayList<Page>() {{
            add(A3);
        }}, cole, "Continue", "You take in your surroundings, mind reeling from the overload of information from the new world. You are surrounded by sights and sounds that you had almost forgotten, a distant remnant of your childhood. More importantly, you notice yourself wearing a transparent bodysuit, completely different from other people around you, who are now staring at you in shock. You can see one man already reaching for his pocket, presumably for a phone. You also notice a dark alley nearby, one that you can quickly duck into to avoid the piercing gaze of the crowd around you.", 0, 0);
        Page A1 = new Page(new ArrayList<Page>() {{
            add(A2);
        }}, cole, "", "You wake up in your cell like usual, but this time you are informed that you have been selected for “volunteer” duty to collect specimens on the surface world by your nearby cellmate. You suit up in a vacuum sealed suit to protect you from the deadly pathogens that fill the surface air and embark on the trek that will take you through the sewer system, to the surface. On arrival, you look around for places that likely hold specimens you could bring back and spot a bear a short distance away, ambling towards you. You immediately scramble back with the knowledge that the slightest puncture on your suit could mean your demise, painfully aware of how sharp the bear’s claws are. Your once ungainly suit now feels insubstantially thin against your skin. You make a desperate ploy and shine your flashlight towards the bear, and thankfully it wanders off, unaccustomed to the sensation of being blinded by light. Your collection of specimens goes without further excitement, except that you discover a sign that appears spray painted with a strange rendition of monkeys with the caption “we did it”. Upon your return, your body is cleansed, and you are escorted to scientists that offer you the opportunity to reduce your sentence: to travel into the past and make observations about the state of the disease. You enter the machine naked, hooked up with an uncountable number of wires attached to your body, and experience a brief disconnect with reality as you are hurled to the past.", 0, 0);

        currentPage = A1;

        currentPage.makeOptions();

    }


    private static List<String> wrap(String str) {

        List<String> myList = new ArrayList<String>(Arrays.asList(str.split(" ")));

        int i = 0;
        int i2 = 13;
        List<String> strs = new ArrayList<>();
        int u = myList.size() / 13;
        for (int y = 0; y < u; y++) {
            strs.add(String.join(" ", myList.subList(i, i2)));
            i = i + 13;
            i2 = i2 + 13;
        }

        strs.add(String.join(" ", myList.subList(i, myList.size())));

        return strs;

    }

    private static void cites() {
        System.out.println("https://metalporsiempre.deviantart.com/art/Monkey-Silhouette-495437909");
    }

}
