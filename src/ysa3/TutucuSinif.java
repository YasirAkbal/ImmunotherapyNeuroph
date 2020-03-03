/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ysa3;

/**
 *
 * @author YASIR
 */
public class TutucuSinif {
    
    double sex;
    double age;
    double time;
    double now;
    double type;
    double area;
    double induration;
    double result;
    

    public TutucuSinif(double sex, double age, double time, double now, double type, double area, double induration, double result) {
        this.sex = sex;
        this.age = age;
        this.time = time;
        this.now = now;
        this.type = type;
        this.area = area;
        this.induration = induration;
        this.result = result;
    }

    @Override
    public String toString() {
        return "TutucuSinif{" + "sex=" + sex + ", age=" + age + ", time=" + time + ", now=" + now + ", type=" + type + ", area=" + area + ", induration=" + induration + ", result=" + result + '}';
    }
    
    
    
}
