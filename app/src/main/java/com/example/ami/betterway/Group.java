package com.example.ami.betterway;

/* Group class
 * This class create and manage a group
 * It is used in MainActivity to register
 * a new group on the FireBase db
 */

public class Group {
    final static String DEFAULT_PIN = "0000";

    private String groupName;  //name given by register section
    private int people;  //people in group
    private Boolean children;  //there are in children in group? True = yes
    private Boolean handicap;  //there are handicap people? True = yes
    private String pin;  //secret key to access to group page(DEFAULT_PIN by default)
    private int room;  //room1 or room2 inside LADISPE
    private boolean exitReached;
    private boolean needHelp;

    Group(String name,int count, Boolean child, Boolean handicap){
        groupName = name;
        people = count;
        children = child;
        this.handicap = handicap;
        this.pin = DEFAULT_PIN;
        exitReached=false;
        needHelp=false;
        room=0; //unknow
    }

    public String getGroupName() {
        return groupName;
    }

    public int getPeople() {
        return people;
    }

    public Boolean getChildren() {
        return children;
    }

    public Boolean getHandicap() {
        return handicap;
    }

    String getPin() {
        return pin;
    }

    public int getRoom() {
        return room;
    }

    public boolean isExitReached() {
        return exitReached;
    }

    public boolean isNeedHelp() {
        return needHelp;
    }
}
