/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ysa3;


import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author YASIR
 */
public class SinirAgi{
    ysa3.MomentumBackpropagation mbp;  
    private Veriler veriler;
    private MultiLayerPerceptron sinirselAg;
    private NeuralNetwork egitilmisAg;
    private double mse;  
    private double[] hatalar;
    private final double learningRate = 0.2;
    private final double momentum = 0.7;
    private final int maksEpoch = 80000;
    private final int[] katmanlar = new int[]{4,4,1};
    
    public SinirAgi()
    {
        mbp = new ysa3.MomentumBackpropagation();
        sinirselAg = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, katmanlar); 
        mbp.setLearningRate(learningRate); 
        mbp.setMaxIterations(maksEpoch);
        mbp.setMomentum(momentum);  
        sinirselAg.setLearningRule(mbp);
        mbp.birKereCagir();
        mse = 0;
        hatalar = new double[maksEpoch];      
    }   
    
    public void egit()
    {      
        veriler = new Veriler();
        XYSeries xy = new XYSeries("Total Network Error");
        
        for(int i=0;i<maksEpoch;i++)
        {        
            sinirselAg.getLearningRule().doOneLearningIteration(veriler.getEgitim());
            if(i == 0){
                hatalar[i] = 1;
                continue;
            }
            else
            {
                hatalar[i] = sinirselAg.getLearningRule().getPreviousEpochError();
                xy.add(i, hatalar[i]);
            }    
            if(i%250 == 0)
            {
                System.out.println("i =" + i + ", Error = " + hatalar[i]);
            }
        }

        System.out.println("Total err = " + sinirselAg.getLearningRule().getTotalNetworkError());
        sinirselAg.save("ogrenilenVeri.nnet"); 
        System.out.println("Egitim Tamamlandi"); 
        
        XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(xy);
        grafikOlustur(dataset);
    }
    
    public void hatalariYazdir()
    {
         for(int i=0;i<hatalar.length;i++)
        {
            System.out.println(i+1 + ".epoch = " + hatalar[i]);
        }
    }
    
    public void grafikOlustur(XYSeriesCollection dataset)
    {
        JFreeChart xylineChart = ChartFactory.createXYLineChart("Grafik","Epoch","Hata",dataset,PlotOrientation.VERTICAL, 
        true, true, false);
               
        int width = 640;   /* Width of the image */
        int height = 480;  /* Height of the image */ 
        File XYChart = new File( "XYLineChart.jpeg" ); 
        try {
            ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
        } catch (IOException ex) {
            Logger.getLogger(SinirAgi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public void testEt()
    {
        mse = 0;
        egitilmisAg = NeuralNetwork.createFromFile("ogrenilenVeri.nnet");
        for(DataSetRow t : veriler.getTest())
        {
            double[] inputs = t.getInput();
            double[] outputs = t.getDesiredOutput();
            double hesaplanan = sonucHesaplaNorm(inputs[0], inputs[1], inputs[2], inputs[3]);
            mse += Math.pow(outputs[0]-hesaplanan, 2);
            System.out.println("Gercek cikti = " + outputs[0] + ", Hesaplanan cikti = " + hesaplanan);
        }
         
        mse /= veriler.getTest().size();
        System.out.println("MSE = " + mse);
    }
    
    private double sonucHesaplaNorm(double age, double time, double now, double type) //halihazırda normalizasyon yapılmış veriler için. Sadece sınıf içinden
    {    
        sinirselAg.setInput(age,time,now,type); 
        sinirselAg.calculate();
        return sinirselAg.getOutput()[0];
    }
     
    public double sonucHesaplaNormsuz(double age, double time, double now, double type)
    {
        egitilmisAg = NeuralNetwork.createFromFile("ogrenilenVeri.nnet");
        egitilmisAg.setInput(veriler.minMax(age,time,now,type));
        return sinirselAg.getOutput()[0];
    }

    public Veriler getVeriler() {
        return veriler;
    }
        
}
