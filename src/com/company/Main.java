package com.company;

import java.util.Random;

        public class Main {
            public static int bossHealth = 3000;
            public static int bossDamage = 50;
            public static String bossDefenceType;
            public static int[] heroesHealth = {280, 270, 250, 600, 200, 240, 520, 300};
            public static int[] heroesDamage = {10, 15, 20, 5, 10, 15, 25, 0};
            public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic",
                    "Golem", "Lucky", "Berserk", "Thor", "Medical"};
            public static int round_number = 0;

            public static void main(String[] args) {
                printStatistics();
                while (!isGameFinished()) {
                    round();
                }
            }

            public static void chooseDefence() {
                Random random = new Random();
                int randomIndex = random.nextInt(heroesAttackType.length); // 0, 1, 2
                bossDefenceType = heroesAttackType[randomIndex];
            }

            public static void round() {
                round_number++;
                chooseDefence();
                bossHits();
                medicHealing();
                heroesHit();
                printStatistics();
            }



            public static void bossHits() {
                if (thorWithoutHummer()) {
                    bossDamage = golemAction();
                    for (int i = 0; i < heroesHealth.length; i++) {
                        if (heroesHealth[i] > 0 && bossHealth > 0) {
                            if (heroesHealth[i] - bossDamage < 0) {
                                heroesHealth[i] = 0;
                            } else {
                                if (heroesAttackType[i].equals("Lucky")){
                                    luckyAction(bossDamage);
                                }else if (heroesAttackType[i].equals("Berserk")){
                                    berserkActing(bossDamage, i);
                                }
                                heroesHealth[i] = heroesHealth[i] -bossDamage; // heroesHealth[i] -= bossDamage;
                            }
                        }
                    }
                }
                bossDamage=50;
            }

            private static boolean thorWithoutHummer() {
                if (heroesHealth[6]>0) {
                    Random random = new Random();
                    boolean noHummer = random.nextBoolean(); random.nextBoolean();
                    random.nextBoolean();
                    if (!noHummer) {
                        System.out.println("Thor ударом молний из своего молота парализовал Boss на один раунд");
                    }else {
                        System.out.println("Молот Thorа подвел");
                    }
                    return noHummer;
                }
                return true;
            }

            private static void berserkActing(int bossDamage, int i) {
                Random random = new Random();
                int randomIndexHeroes = random.nextInt(heroesAttackType.length)+1;
                int defence = bossDamage/randomIndexHeroes;
                if (bossHealth>0 && bossHealth-defence>0){
                    bossHealth -= defence;
                    System.out.println("Berserk отразил и возвратил "+defence+" + к своему урона");
                    heroesHealth[i] += defence;
                }
            }

            private static void luckyAction(int bossDamage) {
                Random random = new Random();
                int randomIndexHeroes = random.nextInt(heroesAttackType.length);
                if (randomIndexHeroes == 4){ // Lucky is a lucky one
                    System.out.println("Lucky уклонился от атаки");
                    heroesHealth[4] += bossDamage;
                }
            }

            private static int golemAction() {
                int totalBossDamage=0;
                if (heroesHealth[3]>0){
                    totalBossDamage= bossDamage-10;
                    System.out.println("Я Golem! Golem прикрыл собой часть урона");
                }else {
                    totalBossDamage = 50;
                }
                return totalBossDamage;
            }

            public static void medicHealing(){
                if (heroesHealth[heroesHealth.length-1]>0){
                    Random random = new Random();
                    int randomIndexHeroes = random.nextInt(heroesAttackType.length-1); // 0, 1, 2
                    int coeffMedic = random.nextInt(4)+1; // 2,3,4,5,6,7,8,9,10
                    int hell = heroesHealth[randomIndexHeroes];
                    if (heroesHealth[randomIndexHeroes]<100){
                        if (heroesHealth[randomIndexHeroes]>0) {
                            heroesHealth[randomIndexHeroes] = hell * coeffMedic;
                            System.out.println();
                            System.out.println("Medic вылечил героя " + heroesAttackType[randomIndexHeroes]
                                    + " используя коэффицент " + coeffMedic + " и восстановил здоровья герою на " + hell * coeffMedic + " единиц");
                        }else {
                            System.out.println("Medic попытылся вылечить героя " + heroesAttackType[randomIndexHeroes]
                                    + " используя коэффицент " + coeffMedic + " но было слишком поздно");
                        }
                    }else {
                        System.out.println("Medic попытылся вылечить героя "+heroesAttackType[randomIndexHeroes]
                                +" используя коэффицент "+coeffMedic+" ,но наш герой гордо отказался от помощи");
                    }
                    System.out.println();
                }
            }

            public static void heroesHit() {
                for (int i = 0; i < heroesDamage.length; i++) {
                    if (heroesHealth[i] > 0 && bossHealth > 0) {
                        if (bossDefenceType == heroesAttackType[i]) {
                            Random random = new Random();
                            int coeff = random.nextInt(12) + 2; // 2,3,4,5,6,7,8,9,10
                            if (bossHealth - heroesDamage[i] * coeff < 0) {
                                bossHealth = 0;
                            } else {
                                bossHealth = bossHealth - heroesDamage[i] * coeff;
                            }
                            System.out.println("Critical damage: " + heroesDamage[i] * coeff);
                        } else {
                            if (bossHealth - heroesDamage[i] < 0) {
                                bossHealth = 0;
                            } else {
                                bossHealth = bossHealth - heroesDamage[i];
                            }
                        }
                    }
                }
            }

            public static void printStatistics() {
                System.out.println(round_number + " ROUND --------------------");
                System.out.println("Boss health: " + bossHealth + " (" + bossDamage + "); " + bossDefenceType);
                for (int i = 0; i < heroesHealth.length; i++) {
                    System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i]
                            + " (" + heroesDamage[i] + ")");
                }
            }

            public static boolean isGameFinished() {
                if (bossHealth <= 0) {
                    System.out.println("Heroes won!!!");
                    return true;
                }
        /*if (heroesHealth[0] <= 0 && heroesHealth[1] <= 0 && heroesHealth[2] <= 0) {
            System.out.println("Boss won!!!");
            return true;
        }*/
                boolean allHeroesDead = true;
                for (int i = 0; i < heroesHealth.length; i++) {
                    if (heroesHealth[i] > 0) {
                        allHeroesDead = false;
                        break;
                    }
                }
                if (allHeroesDead) {
                    System.out.println("Boss won!!!");
                }
                return allHeroesDead;
            }
        }

