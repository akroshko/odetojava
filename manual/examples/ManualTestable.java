import java.util.Vector;
import java.util.Arrays;

import org.jscience.mathematics.vectors.Float64Vector;
import org.jscience.mathematics.numbers.Float64;

import org.odeToJava.testSuite.Testable;
import org.odeToJava.scheme.ERKButcherTableau;
import org.odeToJava.scheme.IMEXESDIRKButcherTableau;

public class ManualTestable {
    public static void main(String[] args) throws Exception {
        Vector STEPSIZES = new Vector(Arrays.asList(1e-2, 3.16277e-3, 1e-3, 3.16277e-4,1e-4));
        Vector ORBIT_RTOLS = new Vector(Arrays.asList(1e-4, 3.16277e-5, 1e-5, 3.16277e-6, 1e-6, 3.16277e-7, 1e-7, 3.16277e-8, 1e-8));
        Vector ORBIT_ATOLS = new Vector(Arrays.asList(1e-8, 3.16277e-9, 1e-9, 3.16277e-10, 1e-10, 3.16277e-11, 1e-11, 3.16277e-12, 1e-12));
        Vector MOL_RTOLS = new Vector(Arrays.asList(1e-2, 3.16277e-3, 1e-3, 3.16277e-4, 1e-4, 3.16277e-5, 1e-5));
        Vector MOL_ATOLS = new Vector(Arrays.asList(1e-2, 3.16277e-3, 1e-3, 3.16277e-4, 1e-4, 3.16277e-5, 1e-5));

        Testable orbit = new Testable(new OrbitArenstorfODE(),"orbitReference.txt");
        Testable mol = new Testable(new BurgersMOLODE(127,Float64.valueOf(1./126.),Float64.valueOf(0.01)),"burgersMOLReference.txt");

        orbit.test_const(ERKButcherTableau.get_DormandPrince54_tableau(),STEPSIZES);
        orbit.test_embedded(ERKButcherTableau.get_DormandPrince54_tableau(),ORBIT_RTOLS,ORBIT_ATOLS);
        orbit.test_sd(ERKButcherTableau.get_DormandPrince54_tableau(),ORBIT_RTOLS,ORBIT_ATOLS);

        mol.test_embedded(IMEXESDIRKButcherTableau.get_KC32_tableau(),MOL_RTOLS,MOL_ATOLS);
        mol.test_embedded(IMEXESDIRKButcherTableau.get_KC43_tableau(),MOL_RTOLS,MOL_ATOLS);
    }
}
