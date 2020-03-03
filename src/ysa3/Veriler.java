/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ysa3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.data.norm.MaxMinNormalizer;
import org.neuroph.util.data.sample.Sampling;
import org.neuroph.util.data.sample.SubSampling;

/**
 *
 * @author YASIR
 */
    class Veriler{
        private static final String dosyaAd = "DataSet.txt";
        private static final ArrayList<TutucuSinif> diziGirdi = okuVeAta(dosyaAd);
        private static final double egitimYuzdeVeri = 0.7;
        private static final double testYuzdeVeri = 0.3;
        
        //egitim icin ayrilan veriler icin
        private double maxAge;
        private double minAge;
        private double maxTime;
        private double minTime;
        private double maxNow;
        private double minNow;
        private double maxType;
        private double minType;
        
        private Sampling sampling;
        private DataSet tumu;
        private DataSet egitim;
        private DataSet test;      
        
        public Veriler()
        {
            sampling = new SubSampling(egitimYuzdeVeri,testYuzdeVeri);
            tumu = txtToDataSet();
            rastgeleOlustur();    
            normalizeEt();
        }
        
        public void rastgeleOlustur()
        {
            DataSet[] temp = tumu.sample(sampling);
            egitim = temp[0];
            test = temp[1];                  
        }
        
        private void normalizeEt()
        {
            MaxMinNormalizer mmn = new MaxMinNormalizer(egitim);
            maxAge = mmn.getMaxIn()[0];
            maxTime = mmn.getMaxIn()[1];
            maxNow = mmn.getMaxIn()[2];
            maxType = mmn.getMaxIn()[3];      
            minAge = mmn.getMinIn()[0];
            minTime = mmn.getMinIn()[1];
            minNow = mmn.getMinIn()[2];
            minType = mmn.getMinIn()[3];
            
            mmn.normalize(egitim);
            mmn.normalize(test);
        } 

        public static ArrayList<TutucuSinif> okuVeAta(String dosyaAdi)
        {
            ArrayList<TutucuSinif> tempDizi = new ArrayList<TutucuSinif>(); 

            FileReader fileReader = null;
            try {
                String line;
                File file = null;
                try {
                    file = new File(YSA3.class.getResource(dosyaAdi).toURI());
                } catch (URISyntaxException ex) {
                    Logger.getLogger(YSA3.class.getName()).log(Level.SEVERE, null, ex);
                }
                fileReader = new FileReader(file);
                BufferedReader br = new BufferedReader(fileReader);
                try {
                    while ((line = br.readLine()) != null) {    
                        String[] ayrac = line.split("\\s+");
                        TutucuSinif temp = new TutucuSinif(Double.parseDouble(ayrac[0]),Double.parseDouble(ayrac[1]),Double.parseDouble(ayrac[2]),
                                Double.parseDouble(ayrac[3]),Double.parseDouble(ayrac[4]),Double.parseDouble(ayrac[5]),Double.parseDouble(ayrac[6]),Double.parseDouble(ayrac[7]));
                        tempDizi.add(temp);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(YSA3.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(YSA3.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fileReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(YSA3.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            return tempDizi;
        }
        
        private DataSet txtToDataSet() 
        {   
            DataSet egitim = new DataSet(4, 1);

            for(TutucuSinif t : diziGirdi)
            {
                double[] inputs = new double[]{t.age,t.time,t.now,t.type};        
                double[] outputs = new double[]{t.result};
                DataSetRow satir = new DataSetRow(inputs,outputs);
                egitim.add(satir);      
            }                 
           return egitim;
       }
    
        public double getMaxAge() {
            return maxAge;
        }

        public double getMinAge() {
            return minAge;
        }

        public double getMaxTime() {
            return maxTime;
        }

        public double getMinTime() {
            return minTime;
        }

        public double getMaxNow() {
            return maxNow;
        }

        public double getMinNow() {
            return minNow;
        }

        public double getMaxType() {
            return maxType;
        }

        public double getMinType() {
            return minType;
        }

        public Sampling getSampling() {
            return sampling;
        }

        public DataSet getTumu() {
            return tumu;
        }

        public DataSet getEgitim() {
            return egitim;
        }

        public DataSet getTest() {
            return test;
        }
        
        public double[] minMax(double... x)
        {
            double[] sonuclar = new double[4];
            sonuclar[0] = (x[0]-minAge) / (maxAge-minAge);
            sonuclar[1] = (x[1]-minTime) / (maxTime-minTime);
            sonuclar[2] = (x[2]-minNow) / (maxNow-minNow);
            sonuclar[3] = (x[3]-minType) / (maxType-minType);    
            
            return sonuclar;
        }
    }