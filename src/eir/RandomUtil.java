/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eir;

import java.util.Random;

public class RandomUtil {

    private static Random random = new Random();

    public static int N(int n) {
        return random.nextInt(n);
    }

    public static double getRandomDouble(double d) {
        return d * random.nextDouble();
    }

    public static float getRandomFloat(float d) {
        return d * random.nextFloat();
    }

    /**
     * Normal distribution around mean
     *
     * @param mean
     * @param sigma
     * @return
     */
    public static float STD(float mean, float sigma) {
        return mean + sigma * (float) random.nextGaussian();
    }

    /**
     * Normal distribution around mean
     *
     * @param mean
     * @param sigma
     * @return
     */
    public static double STD(double mean, double sigma) {
        return mean + sigma * random.nextGaussian();
    }

    /**
     * Uniform distribution around mean
     *
     * @param mean
     * @param span
     * @return
     */
    public static double U(double mean, double span) {
        return mean + random.nextDouble() * 2 * span - span;
    }

    /**
     * 1/num probability of true
     *
     * @param num
     * @return
     */
    public static boolean oneOf(int num) {
        return N(num) == 0;
    }

    /**
     * Uniform random number from 0 to p
     *
     * @param p
     * @return
     */
    public static boolean P(float p) {
        assert p >= 0 && p <= 1;
        return random.nextFloat() <= p;
    }

    public static float R(float d) {
        return d * random.nextFloat();
    }
}