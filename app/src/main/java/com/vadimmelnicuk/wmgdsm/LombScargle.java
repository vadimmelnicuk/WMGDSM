package com.vadimmelnicuk.wmgdsm;

import java.util.ArrayList;

/**
 * Created by vadimmelnicuk on 11/05/2017.
 */

public class LombScargle {
    private ArrayList<Double> samples;
    private int norm = 0;

    public LombScargle(ArrayList<Double> samples) {
        this.samples = samples;
    }

    public ArrayList<Double[]> calculate() {
        double A = 1.2;
        double w = 1.0;
        double phi = 0.5*Math.PI;
        double fracPoint = 0.9;
        int size = samples.size();
        ArrayList<Double> X2 = new ArrayList<>();
        ArrayList<Double> Y = new ArrayList<>();
        ArrayList<Double> F = new ArrayList<>();

        ArrayList<Double> X1 = linspace(0.01, 10*Math.PI, size);

        for(int n = 0; n < size; n++) {
            if(samples.get(n) >= fracPoint) {
                X2.add(X1.get(n));
            }
        }

        norm = X2.size();

        for(int n = 0; n < norm; n++) {
            Y.add(A * Math.sin(w * X2.get(n) + phi));
        }

        F = linspace(0.01, 10, size*100);

        return scargle(X2, Y, F);
    }

    private ArrayList<Double> linspace(double v, double v1, int size) {
        ArrayList<Double> lin = new ArrayList<>();
        double step = (v1 - v)/(size - 1);
        lin.add(v);

        for(int n = 1; n < size; n++) {
            lin.add(lin.get(n-1) + step);
        }

        return lin;
    }

    private ArrayList<Double[]> scargle(ArrayList<Double> X, ArrayList<Double> Y, ArrayList<Double> F) {
        double c, s, xc, xs, cc, ss, cs;
        double tau, c_tau, s_tau, c_tau2, s_tau2, cs_tau;
        ArrayList<Double> periodogram = new ArrayList<>();
        ArrayList<Double[]> scargle = new ArrayList<>();

        for(int i = 0; i < F.size(); i++) {
            xc = 0.0;
            xs = 0.0;
            cc = 0.0;
            ss = 0.0;
            cs = 0.0;

            for (int j = 0; j < X.size(); j++) {

                c = Math.cos(F.get(i) * X.get(j));
                s = Math.sin(F.get(i) * X.get(j));

                xc += Y.get(j) * c;
                xs += Y.get(j) * s;
                cc += c * c;
                ss += s * s;
                cs += c * s;
            }

            tau = Math.atan2(2 * cs, cc - ss) / (2 * F.get(i));
            c_tau = Math.cos(F.get(i) * tau);
            s_tau = Math.sin(F.get(i) * tau);
            c_tau2 = c_tau * c_tau;
            s_tau2 = s_tau * s_tau;
            cs_tau = 2 * c_tau * s_tau;

            double cp = 0.5 * ((Math.pow(c_tau * xc + s_tau * xs, 2) / (c_tau2 * cc + cs_tau * cs + s_tau2 * ss)) + (Math.pow(c_tau * xs - s_tau * xc, 2) / (c_tau2 * ss - cs_tau * cs + s_tau2 * cc)));

            periodogram.add(cp);

            scargle.add(new Double[]{periodogram.get(i), Math.sqrt(4 * periodogram.get(i)/norm)});
        }
        return scargle;
    }
}
