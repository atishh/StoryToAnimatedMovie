/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Assumes UTF-8 encoding. JDK 7+.
 */
public class DesignUnit {

    public static void main(String... aArgs) throws IOException {
        DesignUnit parserObj = new DesignUnit("C:\\Users\\atsingh\\Projects\\nlp\\source\\outfile.txt");
        parserObj.processLineByLine();
        log("Done.");
    }
    public CDFNode mRootCDFNode = null;
    public CDFNode mCurrCDFNode = null;
    public ActorNode[] mFemaleActors;
    public int mNoOfFemaleActors = 0;
    public ActorNode[] mMaleActors;
    public int mNoOfMaleActors = 0;
    //Other Actors are those actors which are not identified by nlp.
    //Lets keep the maximum value to be 10.
    public ActorNode[] mOtherActors = new ActorNode[50];
    public int mNoOfOtherActors = 0;
    //Background 
    public int mCounter = 0;

    public enum ParserState {

        STATE_INITIAL, STATE_NOOFFEMALE, STATE_FEMALE, STATE_FEMALEATTRIBUTE,
        STATE_NOOFMALE, STATE_MALE, STATE_MALEATTRIBUTE,
        STATE_ACTION
    };

    public enum ActionInternalState {

        STATE_ACTOR1, STATE_ACTOR2, STATE_SAY, STATE_NOTHING
    };
    public ParserState myParserState = ParserState.STATE_NOOFFEMALE;
    public ActionInternalState myActionInternalState = ActionInternalState.STATE_ACTOR1;
    public CDFNode mNextCDFNodeTypeAction = null;

    public void populateBackground() {
        CDFNode CDFNodeTemp = mRootCDFNode;
        BackgroundNode PrevBackgroundNode = null;
        while (CDFNodeTemp != null) {
            if (CDFNodeTemp.cdfType == CDFType.CDF_BACKGROUND) {
                if (SupportedBackground.IsSupported(CDFNodeTemp.label)) {
                    if ((PrevBackgroundNode != null) && PrevBackgroundNode.Name.equalsIgnoreCase(CDFNodeTemp.label)) {
                        //This means previous background was the same
                        CDFNodeTemp.Background1 = PrevBackgroundNode;
                    } else {
                        CDFNodeTemp.Background1 = new BackgroundNode(CDFNodeTemp.label, 1);
                        PrevBackgroundNode = CDFNodeTemp.Background1;
                    }
                } else {
                    //Not supported.. what to do
                    if (PrevBackgroundNode != null) {
                        CDFNodeTemp.Background1 = PrevBackgroundNode;
                    } else {
                        //Create a lake background to be on safe side
                        CDFNodeTemp.Background1 = new BackgroundNode("lake", 1);
                        PrevBackgroundNode = CDFNodeTemp.Background1;
                    }
                }
            }
            CDFNodeTemp = CDFNodeTemp.children;
        }
    }

    public void linkBackgroundToAction() {
        CDFNode CDFNodeTemp = mRootCDFNode;
        BackgroundNode PrevBackgroundNode = null;
        while (CDFNodeTemp != null) {
            if (CDFNodeTemp.cdfType == CDFType.CDF_BACKGROUND) {
                if (CDFNodeTemp.Background1 != null) {
                    PrevBackgroundNode = CDFNodeTemp.Background1;
                }
            } else if (CDFNodeTemp.cdfType == CDFType.CDF_ACTION) {
                if (CDFNodeTemp.Background1 == null) {
                    CDFNodeTemp.Background1 = PrevBackgroundNode;
                }
                CDFNodeTemp.CreatePassiveActors();
            }
            CDFNodeTemp = CDFNodeTemp.children;
        }

    }

    public CDFNode getNextCDFNodeTypeAction() {
        if (mNextCDFNodeTypeAction == null) {
            mNextCDFNodeTypeAction = mRootCDFNode;
        } else {
            mNextCDFNodeTypeAction = mNextCDFNodeTypeAction.children;
        }
        while ((mNextCDFNodeTypeAction != null) && mNextCDFNodeTypeAction.cdfType != CDFType.CDF_ACTION) {
            mNextCDFNodeTypeAction = mNextCDFNodeTypeAction.children;
        }

        if (mNextCDFNodeTypeAction != null) {
            System.out.println("Function getNextCDFNodeTypeAction returns " + mNextCDFNodeTypeAction.label);
        }
        return mNextCDFNodeTypeAction;
    }

    public ActorNode findActorNode(String token, CDFNode CDFNodeObj) {
        if (token == null) {
            return null;
        }

        for (int i = 0; i < mNoOfFemaleActors; i++) {
            if (mFemaleActors[i].Name.equals(token)) {
                System.out.println("Found Actor for token " + token);
                return mFemaleActors[i];
            }
        }
        for (int i = 0; i < mNoOfMaleActors; i++) {
            if (mMaleActors[i].Name.equals(token)) {
                System.out.println("Found Actor for token " + token);
                return mMaleActors[i];
            }
        }

        for (int i = 0; i < mNoOfOtherActors; i++) {
            if (mOtherActors[i].Name.equals(token)) {
                System.out.println("Found Actor in otherActor for token " + token);
                return mOtherActors[i];
            }
        }
        System.out.println("No Actor found for token " + token + "creating new actor");
        ActorNode myActorNode = new ActorNode(token, mNoOfOtherActors, true, CDFNodeObj);
        mOtherActors[mNoOfOtherActors] = myActorNode;
        mNoOfOtherActors++;
        return myActorNode;
    }

    public void CreateCDFEdge(CDFNode newCDFNode) {
        if (mCurrCDFNode == null) {
            //This means it is root node.
            mCurrCDFNode = newCDFNode;
            mRootCDFNode = mCurrCDFNode;
        } else {
            //create EDGE
            newCDFNode.parent = mCurrCDFNode;
            mCurrCDFNode.children = newCDFNode;
            mCurrCDFNode = newCDFNode;
        }
    }

    public void ProcessForBackground(String token) {
        System.out.println("Processing Background");

        boolean bCdfNodeCreated = false;
        CDFNode newCDFNode = null;
        Scanner scanner = new Scanner(token);
        //scanner.useDelimiter(" ");
        //scanner.next();

        while (scanner.hasNext()) {
            //assumes the line has a certain structure
            String token1 = scanner.next();
            log("Background found : " + token1.trim());
            if (bCdfNodeCreated == false) {
                newCDFNode = new CDFNode(token1, 1);
                newCDFNode.cdfType = CDFType.CDF_BACKGROUND;
                //newCDFNode.Background1 = new BackgroundNode(token1, 1);
                bCdfNodeCreated = true;
            }
            newCDFNode.attribute += " " + token1;
        }
        CreateCDFEdge(newCDFNode);
    }

    public void ProcessForMood(String token) {
        System.out.println("Processing Mood");

        boolean bCdfNodeCreated = false;
        CDFNode newCDFNode = null;
        Scanner scanner = new Scanner(token);
        //scanner.useDelimiter(" ");
        //scanner.next();

        while (scanner.hasNext()) {
            //assumes the line has a certain structure
            String token1 = scanner.next();
            log("Mood found : " + token1.trim());
            if (bCdfNodeCreated == false) {
                newCDFNode = new CDFNode(token1, 1);
                newCDFNode.cdfType = CDFType.CDF_MOOD;
            }
            newCDFNode.attribute += " " + token1;
        }
        CreateCDFEdge(newCDFNode);
    }

    public void ProcessForAction(String token) {
        System.out.println("Processing Action");

        boolean bCdfNodeCreated = false;
        CDFNode newCDFNode = null;
        Scanner scanner = new Scanner(token);
        scanner.useDelimiter(":");
        scanner.next(); //Ignore keyword Action in Action:walk

        while (scanner.hasNext()) {
            //assumes the line has a certain structure
            String token1 = scanner.next();
            log("Action found : " + token1.trim());
            if (bCdfNodeCreated == false) {
                newCDFNode = new CDFNode(token1, 1);
                newCDFNode.cdfType = CDFType.CDF_ACTION;
            }
            newCDFNode.attribute += " " + token1;
        }
        CreateCDFEdge(newCDFNode);
        myActionInternalState = ActionInternalState.STATE_ACTOR1;
    }

    public void ProcessForActionInternal(String token) {
        System.out.println("Processing ActionInternal");
        Scanner scanner = new Scanner(token);
        String token1;
        switch (myActionInternalState) {
            case STATE_ACTOR1:
                token1 = scanner.hasNext() ? scanner.next() : null;
                mCurrCDFNode.Actor1 = findActorNode(token1, mCurrCDFNode);
                myActionInternalState = ActionInternalState.STATE_ACTOR2;
                break;
            case STATE_ACTOR2:
                token1 = scanner.hasNext() ? scanner.next() : null;
                mCurrCDFNode.Actor2 = findActorNode(token1, mCurrCDFNode);
                myActionInternalState = ActionInternalState.STATE_SAY;
                break;

            case STATE_SAY:
                mCurrCDFNode.TalkString = token;
                myActionInternalState = ActionInternalState.STATE_NOTHING;
                break;

            default:
                System.out.println("Midweek days are so-so.");
                break;
        }
    }

    /**
     * Constructor.
     *
     * @param aFileName full name of an existing, readable file.
     */
    public DesignUnit(String aFileName) {
        fFilePath = Paths.get(aFileName);
    }

    /**
     * Template method that calls {@link #processLine(String)}.
     */
    public final void processLineByLine() throws IOException {
        try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
            while (scanner.hasNextLine()) {
                processLine(scanner.nextLine());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create Pi Face Device", e);
        }
        populateBackground();
        linkBackgroundToAction();
        ActorNode.PopulateTotalActorNodeInThisNode();
    }

    /**
     * Overridable method for processing lines in different ways.
     *
     * <P>This simple default implementation expects simple name-value pairs,
     * separated by an '=' sign. Examples of valid input:
     * <tt>height = 167cm</tt>
     * <tt>mass = 65kg</tt>
     * <tt>disposition = "grumpy"</tt>
     * <tt>this is the name = this is the value</tt>
     */
    protected void processLine(String aLine) {
        log("Line is : " + aLine);
        //use a second Scanner to parse the content of each line 
        //Scanner scanner = new Scanner(aLine);
        //scanner.useDelimiter("=");
        //if (scanner.hasNext()) {
        //assumes the line has a certain structure
        //String token = scanner.hasNext() ? scanner.next() : "";
        String token = aLine;
        log("Name is : " + token.trim());
        switch (myParserState) {
            case STATE_INITIAL:
                System.out.println("Unreachable state. Just for fun");
                myParserState = ParserState.STATE_NOOFFEMALE;
                break;

            case STATE_NOOFFEMALE:
                mNoOfFemaleActors = Integer.parseInt(token);
                System.out.println("No of female Actors are " + mNoOfFemaleActors);
                myParserState = ParserState.STATE_FEMALE;
                mCounter = 0;
                mFemaleActors = new ActorNode[mNoOfFemaleActors];
                break;
            case STATE_FEMALE:
                mFemaleActors[mCounter] = new ActorNode(token, mCounter, false, null);
                if (mCounter < (mNoOfFemaleActors)) {
                    myParserState = ParserState.STATE_FEMALEATTRIBUTE;
                } else {
                    myParserState = ParserState.STATE_NOOFMALE;
                }
                System.out.println("Female Actor " + mCounter + " is " + token);
                break;
            case STATE_FEMALEATTRIBUTE:
                mFemaleActors[mCounter].attribute += token;
                if (mCounter < (mNoOfFemaleActors - 1)) {
                    mCounter++;
                    myParserState = ParserState.STATE_FEMALE;
                } else {
                    myParserState = ParserState.STATE_NOOFMALE;
                }
                System.out.println("Female Actor  " + mCounter + " Attribute  is " + token);
                break;
            case STATE_NOOFMALE:
                mNoOfMaleActors = Integer.parseInt(token);
                System.out.println("No of Male Actors are " + mNoOfMaleActors);
                myParserState = ParserState.STATE_MALE;
                mCounter = 0;
                mMaleActors = new ActorNode[mNoOfMaleActors];
                break;
            case STATE_MALE:
                //TODO change it
                mMaleActors[mCounter] = new ActorNode(token, mCounter, false, null);
                if (mCounter < (mNoOfMaleActors)) {
                    myParserState = ParserState.STATE_MALEATTRIBUTE;
                } else {
                    myParserState = ParserState.STATE_ACTION;
                }
                System.out.println("Male Actor " + mCounter + " is " + token);
                break;
            case STATE_MALEATTRIBUTE:
                mMaleActors[mCounter].attribute += token;
                if (mCounter < (mNoOfMaleActors - 1)) {
                    mCounter++;
                    myParserState = ParserState.STATE_MALE;
                } else {
                    myParserState = ParserState.STATE_ACTION;
                }
                System.out.println("Male Actor  " + mCounter + " Attribute  is " + token);
                break;

            case STATE_ACTION:
                if (token.contains("Background:")) {
                    ProcessForBackground(token);
                } else if (token.contains("Mood:")) {
                    ProcessForMood(token);
                } else if (token.contains("Action:")) {
                    ProcessForAction(token);
                } else {
                    ProcessForActionInternal(token);
                }

            default:
                System.out.println("Midweek days are so-so.");
                break;
        }
        // }

        /*
         if (scanner.hasNext()) {
         //assumes the line has a certain structure
         String name = scanner.next();
         //String value = scanner.next();
         log("Name is : " + name.trim());
         } else {
         log("Empty or invalid line. Unable to process.");
         }
         * */
    }
    // PRIVATE 
    private final Path fFilePath;
    private final static Charset ENCODING = StandardCharsets.UTF_8;

    private static void log(Object aObject) {
        System.out.println(String.valueOf(aObject));
    }
}
