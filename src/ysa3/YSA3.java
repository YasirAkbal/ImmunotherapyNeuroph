/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ysa3;

import java.util.Scanner;

/**
 *
 * @author YASIR
 */
public class YSA3 {

    public static void main(String[] args) {
        
        Scanner girdi = new Scanner(System.in);
        int secim = 0;
        double age,time,now,type;
        Scanner scanner = new Scanner(System.in);
        SinirAgi ag = new SinirAgi();
        Veriler veriler = null;
        double tekSonuc;
        boolean kontrol = false;
        
        do
        {
            System.out.println("1- Ağı Eğit\n2- Ağı Test Et\n3- Tek Veri ile Test Et\n4- Çıkış");
            System.out.print(">>");
            secim = girdi.nextInt();
            switch(secim)
            {
                case 1:
                    ag.egit();
                    veriler = ag.getVeriler();
                    kontrol = true;
                    break;
                case 2:
                    if(kontrol)
                        ag.testEt();
                    else
                        System.out.println("Once egitimi gerceklestirin.");
                    break;
                case 3:      
                    if(kontrol)
                    {
                        System.out.printf("Age(%.2f-%.2f) arası = ",veriler.getMinAge(),veriler.getMaxAge());
                        age = scanner.nextDouble();
                        System.out.printf("Time(%.2f-%.2f) arası = ",veriler.getMinTime(),veriler.getMaxTime());  
                        time = scanner.nextDouble();
                        System.out.printf("Number of warts(%.2f-%.2f) arası = ",veriler.getMinNow(),veriler.getMaxNow());
                        now = scanner.nextDouble();
                        System.out.printf("Type(%d-%d) arası(tamsayi) = ",(int)veriler.getMinType(),(int)veriler.getMaxType());
                        type = scanner.nextDouble();  
                        tekSonuc = ag.sonucHesaplaNormsuz(age, time, now, type);
                        System.out.println("Sonuc = " + tekSonuc);
                    }
                    else
                        System.out.println("Once egitimi gerceklestirin.");
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Hatali secim");
                    break;
            }
        }while(secim != 4);     
    }    
}
