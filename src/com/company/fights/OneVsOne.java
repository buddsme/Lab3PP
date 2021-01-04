package com.company.fights;

import com.company.droids.*;
import com.company.output.Output;

import java.util.Scanner;

public class OneVsOne {

    private final Scanner in = new Scanner(System.in);
    private Droid firstOpponent;
    private Droid secondOpponent;
    private int choose = 2;

    public Droid getFirstOpponent() {
        return firstOpponent;
    }

    public void setFirstOpponent(Droid firstOpponent) {
        this.firstOpponent = firstOpponent;
    }

    public Droid getSecondOpponent() {
        return secondOpponent;
    }

    public void setSecondOpponent(Droid secondOpponent) {
        this.secondOpponent = secondOpponent;
    }


    public void sleepTime(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Droid chooseCharacter() {
        Scanner input = new Scanner(System.in);
        int choose;
        System.out.println("Оберіть персонажа:");
        System.out.println("\n1 - Dragon\n2 - Bastion\n3 - Healer\n4 - Wizard");
        choose = input.nextInt();
        while(choose >= 1 && choose <= 4) {
            switch (choose) {
                case 1: {
                    return new Dragon();
                }
                case 2: {
                    return new Bastion();
                }
                case 3: {
                    return new Healer();
                }
                case 4: {
                    return new Wizard();
                }
                default: {
                    System.out.println("Ви нічого не обрали!");
                    break;
                }
            }
            choose = input.nextInt();
        }
        return null;
    }

    public void useAbility(Droid first, Droid second){
        Output output = new Output();
        output.useAbility(first);
        first.useIndividualAbility(second);
        sleepTime(1000);
        first.setCooldown(3);
        choose = 2;
    }

    public void makeHit(Droid first, Droid second) {
        Output output = new Output();
        if (second.getArmor() <= 0) {// удар по хп
            output.whoMakeHit(first);
            first.makeHit(second);
            output.loseHealthOrArmor(second);
            sleepTime(1000);
        } else {  //удар по броні
            output.whoMakeHit(first);
            second.loseArmor(first);
            output.loseHealthOrArmor(second);
            sleepTime(1000);
        }
        if (first.getCooldown() > 0) { // зменшення кд
            first.setCooldown(first.getCooldown() - 1);
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

    /**
     * Хто вдарить перший
     */
    public static int firstPunch(int max, Droid droid1, Droid droid2) {
        int firstPunch = (int) (Math.random() * ++max);
        if (firstPunch == 1) {
            System.out.println("Випав Орел, перший б'є " + droid1.getName() + "\n");
        }
        if (firstPunch == 0) {
            System.out.println("Випала Решка, перший б'є " + droid2.getName() + "\n");
        }
        return firstPunch;
    }

    public void doYouWantUseAbility(Droid droid){
        Output output = new Output();
        if ((droid.getCooldown() == 0) && (droid.getCanUseAbility() > 0)) {   // Якщо кд на суперздібність == 0 && can use > 0
            output.youHaveAbility(droid);
            choose = in.nextInt();
        }
    }

    public void outputWinner(Droid first, Droid second){
        Output output = new Output();
        if (secondOpponent.getHealth() <= 0) {
            output.winRound(firstOpponent);
        }
        if (firstOpponent.getHealth() <= 0) {
            output.winRound(secondOpponent);
        }
    }



    public void FightOneVsOne() {

        Output output = new Output();
        firstOpponent = chooseCharacter();
        secondOpponent = chooseCharacter();
        int punch = firstPunch(1, firstOpponent, secondOpponent);
        sleepTime(3000);
        do {
            /** Б'Є ПЕРШИЙ ДРОЇД */
            if (punch == 1) {
                doYouWantUseAbility(firstOpponent);
                switch (choose) {
                    case 1: {  // використання суперздібності
                        useAbility(firstOpponent, secondOpponent);
                        break;
                    }
                    case 2: {  //звичайний удар
                        makeHit(firstOpponent, secondOpponent);
                        break;
                    }
                    default: { // якшо нічого не обрали
                        output.exception();
                        break;
                    }
                }

                /** Б'Є ДРУГИЙ ДРОЇД */
            } else {
                doYouWantUseAbility(secondOpponent);
                switch (choose) {
                    case 1: {     // використання суперздібності
                        useAbility(secondOpponent, firstOpponent);
                        break;
                    }
                    case 2: {    //звичайний удар
                        makeHit(secondOpponent, firstOpponent);
                        break;
                    }
                    default:   // якшо нічого не обрали
                        output.exception();
                        break;
                }
            }
            punch = whoPunch(punch);  // зміна нападника
        } while ((firstOpponent.getHealth() > 0) && (secondOpponent.getHealth() > 0));

        outputWinner(firstOpponent,secondOpponent);

        sleepTime(5000);
    }
}

