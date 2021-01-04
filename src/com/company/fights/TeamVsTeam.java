package com.company.fights;

import com.company.droids.*;
import com.company.output.Output;
import com.sun.org.apache.bcel.internal.generic.DDIV;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TeamVsTeam {
    private ArrayList<Droid> firstTeam;
    private ArrayList<Droid> secondTeam;
    private Droid assaulter;
    private Droid target;
    private int cooldownAbilityFirstTeam = 2;
    private int cooldownAbilitySecondTeam = 2;
    private int choose = 2;
    Scanner input = new Scanner(System.in);

    public ArrayList<Droid> getFirstTeam() {
        return firstTeam;
    }

    public void setFirstTeam(ArrayList<Droid> firstTeam) {
        this.firstTeam = firstTeam;
    }

    public ArrayList<Droid> getSecondTeam() {
        return secondTeam;
    }

    public void setSecondTeam(ArrayList<Droid> secondTeam) {
        this.secondTeam = secondTeam;
    }

    public Droid getAssaulter() {
        return assaulter;
    }

    public void setAssaulter(Droid assaulter) {
        this.assaulter = assaulter;
    }

    public Droid getTarget() {
        return target;
    }

    public void setTarget(Droid target) {
        this.target = target;
    }

    public void sleepTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sleepTime(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int firstPunch(int max) {
        int firstPunch = (int) (Math.random() * ++max);
        if (firstPunch == 1) {
            System.out.println("Випав Орел, перша б'є команда №1\n");
        }
        if (firstPunch == 0) {
            System.out.println("Випала Решка, перша б'є команда №2\n");
        }
        return firstPunch;
    }

    public void setOpponents(int i, int j, int punch) {
        if (punch == 1) {
            setAssaulter(firstTeam.get(i));
            setTarget(secondTeam.get(j));
        } else {
            setAssaulter(secondTeam.get(j));
            setTarget(firstTeam.get(i));
        }
    }

    /**
     * чий удар
     */
    public static int whoPunch(int punch) {
        if (punch == 0) {
            punch = 1;
        } else {
            punch = 0;
        }
        return punch;
    }

    public ArrayList<Droid> createTeam() {
        ArrayList<Droid> team = new ArrayList<>(2);
        Scanner input = new Scanner(System.in);
        int countOfCharacters = 0;
        while (countOfCharacters < 2) {
            System.out.println("1 - Dragon\n2 - Bastion\n3 - Healer\n4 - Wizard");
            int choose = input.nextInt();
            System.out.println("\n");
            switch (choose) {
                case 1: {
                    team.add(new Dragon());
                    countOfCharacters++;
                    break;
                }
                case 2: {
                    team.add(new Bastion());
                    countOfCharacters++;
                    break;
                }
                case 3: {
                    team.add(new Healer());
                    countOfCharacters++;
                    break;
                }
                case 4: {
                    team.add(new Wizard());
                    countOfCharacters++;
                    break;
                }
                default: {
                    System.out.println("Введене невірне значення. Повторіть спробу!");
                    break;
                }
            }
        }
        return team;
    }

    public void youCanUseTeamAbility(Droid assaulter, int cooldownTeamAbility) {
        if ((cooldownTeamAbility == 0) && (assaulter.getCanUseAbility() > 0)) {
            System.out.println("\n" + assaulter.getName() + " може використати командну суперздібність! Бажаєте використати? 1 - Так 2 - Ні");
            choose = input.nextInt();
        }
    }

    public void useTeamAbility(Droid assaulter, ArrayList<Droid> team) {
        assaulter.useTeamIndividualAbility(team);
        sleepTime();

        choose = 2;
    }

    public void makeHit(Droid assaulter, Droid target) {
        Output output = new Output();
        if (target.getArmor() <= 0) {// удар по хп
            output.whoMakeHit(assaulter);
            assaulter.makeHit(target);
            output.loseHealthOrArmor(target);
            sleepTime();
        } else {  //удар по броні
            output.whoMakeHit(assaulter);
            target.loseArmor(assaulter);
            output.loseHealthOrArmor(target);
            sleepTime();
        }
    }

    public void leaveFighting(Droid target, ArrayList<Droid> team) {
        if (target.getHealth() == 0) {
            System.out.println("\nДроїд " + target.getName() + " покидає поле бою!");
            sleepTime(2000);
            team.remove(target);
        }
    }

    public void outputWinner(ArrayList<Droid> firstTeam, ArrayList<Droid> secondTeam) {
        Output output = new Output();
        if (firstTeam.size() > 0) {
            output.teamWin(firstTeam);
        } else if (secondTeam.size() > 0) {
            output.teamWin(secondTeam);
        }
    }

    public void fight() {
        Output output = new Output();
        System.out.println("Виберіть персонажів для першої команди:");
        setFirstTeam(createTeam());
        System.out.println("Виберіть персонажів для другої команди:");
        setSecondTeam(createTeam());

        int droidFirstTeam, droidSecondTeam;
        int punch = firstPunch(1);

        sleepTime(2500);
        do {
            droidFirstTeam = new Random().nextInt(firstTeam.size());
            droidSecondTeam = new Random().nextInt(secondTeam.size());
            setOpponents(droidFirstTeam, droidSecondTeam, punch);
            if (punch == 1) {
                youCanUseTeamAbility(assaulter, cooldownAbilityFirstTeam);
                switch (choose) {
                    case 1: {
                        useTeamAbility(assaulter, secondTeam);
                        cooldownAbilityFirstTeam = 2;
                        break;
                    }
                    case 2: {
                        makeHit(assaulter, target);
                        if (cooldownAbilityFirstTeam > 0) { // зменшення кд
                            cooldownAbilityFirstTeam = cooldownAbilityFirstTeam - 1;
                        }
                        break;
                    }
                    default: {
                        output.exception();
                        break;
                    }
                }
                leaveFighting(target, secondTeam);

            } else {
                youCanUseTeamAbility(assaulter, cooldownAbilitySecondTeam);
                switch (choose) {
                    case 1: {
                        useTeamAbility(assaulter, firstTeam);
                        cooldownAbilitySecondTeam = 2;
                        break;
                    }
                    case 2: {
                        makeHit(assaulter, target);

                        if (cooldownAbilityFirstTeam > 0) { // зменшення кд
                            cooldownAbilitySecondTeam = cooldownAbilitySecondTeam - 1;
                        }
                        break;
                    }
                    default: {
                        output.exception();
                        break;
                    }
                }
                leaveFighting(target, firstTeam);
            }
            punch = whoPunch(punch);
        } while ((firstTeam.size() > 0) && (secondTeam.size() > 0));
        outputWinner(firstTeam, secondTeam);
    }
}