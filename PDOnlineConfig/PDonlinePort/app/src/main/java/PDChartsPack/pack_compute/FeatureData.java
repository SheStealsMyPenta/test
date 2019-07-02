/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PDChartsPack.pack_compute;

/**
 *
 * @author hr018
 */
public class FeatureData {
    /*
     * variables for any
     */
    private float fQp;
    private float fQm;
    private float fF1;
    private float fF2;

    /*
     * variables for TEV
     */
    private float fPeriodPulse;
    private float f2sPulse;
    private float fDischarge;

    /*
     *  constructor
     */
    public FeatureData(){
        fQp = 0.0f;
        fQm = 0.0f;
        fF1 = 0.0f;
        fF2 = 0.0f;

        fPeriodPulse = 0.0f;
        f2sPulse = 0.0f;
        fDischarge = 0.0f;
    }

    /*
     *  setter and getter
     */
    public float getTev2sPulse() {
        return f2sPulse;
    }

    public void setTev2sPulse(float f2sPulse) {
        this.f2sPulse = f2sPulse;
    }

    public float getTevDischarge() {
        return fDischarge;
    }

    public void setTevDischarge(float fDischarge) {
        this.fDischarge = fDischarge;
    }

    public float getF1() {
        return fF1;
    }

    public void setF1(float fF1) {
        this.fF1 = fF1;
    }

    public float getF2() {
        return fF2;
    }

    public void setF2(float fF2) {
        this.fF2 = fF2;
    }

    public float getTevPeriodPulse() {
        return fPeriodPulse;
    }

    public void setTevPeriodPulse(float fPeriodPulse) {
        this.fPeriodPulse = fPeriodPulse;
    }

    public float getQm() {
        return fQm;
    }

    public void setQm(float fQm) {
        this.fQm = fQm;
    }

    public float getQp() {
        return fQp;
    }

    public void setQp(float fQp) {
        this.fQp = fQp;
    }
}
