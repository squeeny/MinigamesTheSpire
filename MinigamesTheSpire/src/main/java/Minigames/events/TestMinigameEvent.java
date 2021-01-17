package Minigames.events;

import Minigames.games.AbstractMinigame;
import Minigames.games.beatpress.BeatPress;
import Minigames.games.blackjack.BlackjackMinigame;
import Minigames.games.gremlinFlip.gremlinFlip;
import Minigames.games.mastermind.MastermindMinigame;
import Minigames.games.test.TestMinigame;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.EventStrings;

import static Minigames.Minigames.makeID;
import static Minigames.Minigames.srcMinigameList;

public class TestMinigameEvent extends AbstractMinigameEvent {
    public static final String ID = makeID("Test");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private int chosenMinigame;

    public TestMinigameEvent() {
        super(NAME, DESCRIPTIONS[0], null);

        // add All Minigames, regardless of condition (Used for testing!)
        for(AbstractMinigame m : srcMinigameList){ imageEventText.setDialogOption(m.getOption()); }
    }

    @Override
    protected void buttonEffect(int buttonPressed)
    {
        switch (screenNum) {
            case 0:
                //use earlier cases to do the event's flavor stuff
            case 1:
                //screen with choice
                chosenMinigame = buttonPressed;
                if (srcMinigameList.get(chosenMinigame).hasInstructionScreen)
                {
                    screenNum = 2;

                    this.imageEventText.clearAllDialogs();

                    srcMinigameList.get(chosenMinigame).setupInstructionScreen(this.imageEventText);
                }
                else
                {
                    startGame(srcMinigameList.get(chosenMinigame));
                }
                break;
            case 2:
                if (srcMinigameList.get(chosenMinigame).instructionsButtonPressed(buttonPressed, this.imageEventText)) {
                    startGame(srcMinigameList.get(chosenMinigame));
                }
                break;
            case 3:
                if (srcMinigameList.get(chosenMinigame).postgameButtonPressed(buttonPressed, this.imageEventText)) {
                    endOfEvent();
                }
                break;
            default:
                openMap();
                break;
        }
    }

    @Override
    public void finishGame() {
        super.finishGame();

        if (srcMinigameList.get(chosenMinigame).hasPostgameScreen) {
            screenNum = 3;

            this.imageEventText.clearAllDialogs();

            srcMinigameList.get(chosenMinigame).setupPostgameScreen(this.imageEventText);
        }
        else
        {
            endOfEvent();
        }
    }

    @Override
    public void endOfEvent() {
        this.imageEventText.clearAllDialogs();

        this.imageEventText.updateBodyText("hmmmm");
        this.imageEventText.setDialogOption("I guess it's over?");

        screenNum = 4;
    }
}
