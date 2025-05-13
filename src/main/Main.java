/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import com.ppstudios.footballmanager.api.contracts.team.IFormation;
import model.team.Formation;

/**
 *
 * @author diogo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        IFormation f1 = new Formation(4, 4, 2); // 4-4-2
        IFormation f2 = new Formation(4, 3, 3); // 4-3-3 → penalidade

        System.out.println("F1: " + f1.getDisplayName());
        System.out.println("F2: " + f2.getDisplayName());
        System.out.println("Vantagem Tática F1 vs F2: " + f1.getTacticalAdvantage(f2));
    }

}
