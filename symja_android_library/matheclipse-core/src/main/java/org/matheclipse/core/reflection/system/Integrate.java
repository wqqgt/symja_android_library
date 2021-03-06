package org.matheclipse.core.reflection.system;

import static org.matheclipse.core.expression.F.Divide;
import static org.matheclipse.core.expression.F.Integrate;
import static org.matheclipse.core.expression.F.List;
import static org.matheclipse.core.expression.F.Log;
import static org.matheclipse.core.expression.F.Plus;
import static org.matheclipse.core.expression.F.Power;
import static org.matheclipse.core.expression.F.Times;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.matheclipse.core.basic.Config;
import org.matheclipse.core.builtin.Algebra;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.exception.AbortException;
import org.matheclipse.core.eval.exception.RecursionLimitExceeded;
import org.matheclipse.core.eval.interfaces.AbstractFunctionEvaluator;
import org.matheclipse.core.expression.ASTSeriesData;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.generic.Predicates;
import org.matheclipse.core.integrate.rubi.UtilityFunctionCtors;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IASTAppendable;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.IPattern;
import org.matheclipse.core.interfaces.ISymbol;
import org.matheclipse.core.patternmatching.RulesData;

import com.google.common.cache.CacheBuilder;

import edu.jas.arith.BigInteger;
import edu.jas.arith.BigRational;
import edu.jas.poly.ExpVector;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.Monomial;

/**
 * <pre>
 * Integrate(f, x)
 * </pre>
 * 
 * <blockquote>
 * <p>
 * integrates <code>f</code> with respect to <code>x</code>. The result does not contain the additive integration
 * constant.
 * </p>
 * </blockquote>
 * 
 * <pre>
 * Integrate(f, {x,a,b})
 * </pre>
 * 
 * <blockquote>
 * <p>
 * computes the definite integral of <code>f</code> with respect to <code>x</code> from <code>a</code> to
 * <code>b</code>.
 * </p>
 * </blockquote>
 * <p>
 * See: <a href="https://en.wikipedia.org/wiki/Integral">Wikipedia: Integral</a>
 * </p>
 * <h3>Examples</h3>
 * 
 * <pre>
 * &gt;&gt; Integrate(x^2, x)
 * x^3/3
 * 
 * &gt;&gt; Integrate(Tan(x) ^ 5, x)
 * -Log(Cos(x))-Tan(x)^2/2+Tan(x)^4/4
 * </pre>
 */
public class Integrate extends AbstractFunctionEvaluator {
	public static RulesData INTEGRATE_RULES_DATA;
	/**
	 * Constructor for the singleton
	 */
	public final static Integrate CONST = new Integrate();

	/**
	 * Check if the internal rules are already initialized
	 */
	public static boolean INITIALIZED = false;
	public final static Set<ISymbol> INT_RUBI_FUNCTIONS = new HashSet<ISymbol>();

	public final static Set<IExpr> DEBUG_EXPR = new HashSet<IExpr>(64);

	private static boolean INTEGRATE_RULES_READ = false;

	public Integrate() {
	}

	@Override
	public IExpr evaluate(final IAST holdallAST, EvalEngine engine) {
		boolean evaled = false;
		IExpr result;
		boolean numericMode = engine.isNumericMode();
		try {
			engine.setNumericMode(false);
			if (holdallAST.size() < 3) {
				return F.NIL;
			}
			IExpr arg1 = engine.evaluateNull(holdallAST.arg1());
			if (arg1.isPresent()) {
				evaled = true;
			} else {
				arg1 = holdallAST.arg1();
			}
			if (arg1.isIndeterminate()) {
				return F.Indeterminate;
			}
			if (holdallAST.size() > 3) {
				// reduce arguments by folding Integrate[fxy, x, y] to
				// Integrate[Integrate[fxy, y], x] ...
				return holdallAST.foldRight((x, y) -> engine.evaluate(F.Integrate(x, y)), arg1, 2);
			}

			IExpr arg2 = engine.evaluateNull(holdallAST.arg2());
			if (arg2.isPresent()) {
				evaled = true;
			} else {
				arg2 = holdallAST.arg2();
			}
			if (arg2.isList()) {
				IAST xList = (IAST) arg2;
				if (xList.isVector() == 3) {
					// Integrate[f[x], {x,a,b}]
					IAST copy = holdallAST.setAtCopy(2, xList.arg1());
					IExpr temp = engine.evaluate(copy);
					if (temp.isFreeAST(F.Integrate)) {
						// F(b)-F(a)
						IExpr Fb = engine.evaluate(F.Limit(temp, F.Rule(xList.arg1(), xList.arg3())));
						IExpr Fa = engine.evaluate(F.Limit(temp, F.Rule(xList.arg1(), xList.arg2())));
						if (!Fb.isFree(F.DirectedInfinity, true) || !Fb.isFree(F.Indeterminate, true)) {
							engine.printMessage(
									"Not integrable: " + temp + " for limit " + xList.arg1() + " -> " + xList.arg3());
							return F.NIL;
						}
						if (!Fa.isFree(F.DirectedInfinity, true) || !Fa.isFree(F.Indeterminate, true)) {
							engine.printMessage(
									"Not integrable: " + temp + " for limit " + xList.arg1() + " -> " + xList.arg2());
							return F.NIL;
						}
						if (Fb.isAST() && Fa.isAST()) {
							IExpr bDenominator = F.Denominator.of(engine, Fb);
							IExpr aDenominator = F.Denominator.of(engine, Fa);
							if (bDenominator.equals(aDenominator)) {
								return F.Divide(F.Subtract(F.Numerator(Fb), F.Numerator(Fa)), bDenominator);
							}
						}
						return F.Subtract(Fb, Fa);
					}
				}
				return F.NIL;
			}
			if (arg1.isList() && arg2.isSymbol()) {
				return mapIntegrate((IAST) arg1, arg2);
			}

			final IASTAppendable ast = holdallAST.setAtClone(1, arg1);
			ast.set(2, arg2);
			final IExpr x = ast.arg2();

			if (arg1.isNumber()) {
				// Integrate[x_?NumberQ,y_Symbol] -> x*y
				return Times(arg1, x);
			}
			if (arg1 instanceof ASTSeriesData) {
				ASTSeriesData series = ((ASTSeriesData) arg1);
				if (series.getX().equals(x)) {
					final IExpr temp = ((ASTSeriesData) arg1).integrate(x);
					if (temp != null) {
						return temp;
					}
				}
				return F.NIL;
			}
			if (arg1.isFree(x, true)) {
				// Integrate[x_,y_Symbol] -> x*y /; FreeQ[x,y]
				return Times(arg1, x);
			}
			if (arg1.equals(x)) {
				// Integrate[x_,x_Symbol] -> x^2 / 2
				return Times(F.C1D2, Power(arg1, F.C2));
			}
			boolean showSteps = false;
			if (showSteps) {
				System.out.println("\nINTEGRATE: " + arg1.toString());
				if (DEBUG_EXPR.contains(arg1)) {
					// System.exit(-1);
				}
				DEBUG_EXPR.add(arg1);
			}
			if (arg1.isAST()) {
				final IAST fx = (IAST) arg1;
				if (fx.topHead().equals(x)) {
					// issue #91
					return F.NIL;
				}
				if (arg1.isTimes()) {
					IAST[] temp = ((IAST) arg1).filter((Predicate<IExpr>) arg -> arg.isFree(x));
					IExpr free = temp[0].oneIdentity1();
					if (!free.isOne()) {
						IExpr rest = temp[1].oneIdentity1();
						// Integrate[free_ * rest_,x_Symbol] -> free*Integrate[rest, x] /; FreeQ[free,x]
						return Times(free, Integrate(rest, x));
					}
				}

				if (fx.isPower()) {
					// base ^ exponent
					IExpr base = fx.base();
					IExpr exponent = fx.exponent();
					if (base.equals(x) && exponent.isFree(x)) {
						if (exponent.isMinusOne()) {
							// Integrate[ 1 / x_ , x_ ] -> Log[x]
							return Log(x);
						}
						// Integrate[ x_ ^n_ , x_ ] -> x^(n+1)/(n+1) /; FreeQ[n, x]
						IExpr temp = Plus(F.C1, exponent);
						return Divide(Power(x, temp), temp);
					}
					if (exponent.equals(x) && base.isFree(x)) {
						if (base.isE()) {
							// E^x
							return arg1;
						}
						// a^x / Log(a)
						return F.Divide(fx, F.Log(base));
					}
				}
				result = integrateByRubiRules(fx, x, ast);
				if (result.isPresent()) {
					return result;
				}

				result = callRestIntegrate(fx, x, engine);
				if (result.isPresent()) {
					return result;
				}

			}
			return evaled ? ast : F.NIL;
		} finally {
			engine.setNumericMode(numericMode);
		}
	}

	private static IExpr callRestIntegrate(IAST arg1, final IExpr x, final EvalEngine engine) {
		IExpr fxExpanded = F.expand(arg1, false, false, false);
		if (fxExpanded.isAST()) {
			if (fxExpanded.isPlus()) {
				return mapIntegrate((IAST) fxExpanded, x);
			}

			final IAST arg1AST = (IAST) fxExpanded;
			if (arg1AST.isTimes()) {
				// Integrate[a_*y_,x_Symbol] -> a*Integrate[y,x] /; FreeQ[a,x]
				IASTAppendable filterCollector = F.TimesAlloc(arg1AST.size());
				IASTAppendable restCollector = F.TimesAlloc(arg1AST.size());
				arg1AST.filter(filterCollector, restCollector, new Predicate<IExpr>() {
					@Override
					public boolean test(IExpr input) {
						return input.isFree(x, true);
					}
				});
				if (filterCollector.size() > 1) {
					if (restCollector.size() > 1) {
						filterCollector.append(F.Integrate(restCollector.oneIdentity0(), x));
					}
					return filterCollector;
				}

//				IExpr temp = integrateTimesTrigFunctions(arg1AST, x);
//				if (temp.isPresent()) {
//					return temp;
//				}
			}

			if (arg1AST.size() >= 3 && arg1AST.isFree(F.Integrate) && arg1AST.isPlusTimesPower()) {
				if (!arg1AST.isEvalFlagOn(IAST.IS_DECOMPOSED_PARTIAL_FRACTION) && x.isSymbol()) {
					IExpr[] parts = Algebra.fractionalParts(arg1, true);
					if (parts != null) {
						IExpr temp = Algebra.partsApart(parts, x, engine);
						if (temp.isPlus()) {
							return mapIntegrate((IAST) temp, x);
						}
						// return Algebra.partialFractionDecompositionRational(new
						// PartialFractionIntegrateGenerator(x),parts, x);
					}
				}
			}
		}
		if (arg1.isTrigFunction()) {
			// https://github.com/RuleBasedIntegration/Rubi/issues/12
			IExpr temp = engine.evaluate(F.TrigToExp(arg1));
			return engine.evaluate(F.Integrate(temp, x));
		}
		return F.NIL;
	}

	/**
	 * Map <code>Integrate</code> on <code>ast</code>. Examples:
	 * <ul>
	 * <li><code>Integrate[{a_, b_,...},x_] -> {Integrate[a,x], Integrate[b,x], ...}</code> or</li>
	 * <li><code>Integrate[a_+b_+...,x_] -> Integrate[a,x]+Integrate[b,x]+...</code></li>
	 * </ul>
	 * 
	 * @param ast
	 *            a <code>List(...)</code> or <code>Plus(...)</code> ast
	 * @param x
	 *            the integ ration veariable
	 * @return
	 */
	private static IExpr mapIntegrate(IAST ast, final IExpr x) {
		return ast.mapThread(F.Integrate(null, x), 1);
	}

	// private IExpr integrate1ArgumentFunctions(final IExpr head, final IExpr x) {
	// if (head.equals(F.ArcCos)) {
	// // x*ArcCos(x) - Sqrt(1-x^2)
	// return F.Subtract(F.Times(x, F.ArcCos(x)), F.Sqrt(F.Subtract(F.C1, F.Sqr(x))));
	// }
	// if (head.equals(F.ArcCosh)) {
	// // x*ArcCosh(x) - Sqrt(x+1) * Sqrt(x-1)
	// return F.Subtract(F.Times(x, F.ArcCosh(x)), F.Times(F.Sqrt(F.Plus(x, F.C1)), F.Sqrt(F.Plus(x, F.CN1))));
	// }
	// if (head.equals(F.ArcCot)) {
	// // x*ArcCot(x) + (1/2 * Log(1+x^2))
	// return F.Plus(F.Times(x, F.ArcCot(x)), F.Times(F.C1D2, F.Log(F.Plus(F.C1, F.Sqr(x)))));
	// }
	// if (head.equals(F.ArcCoth)) {
	// // x*ArcCoth(x) - (1/2 * Log(1-x^2))
	// return F.Plus(F.Times(x, F.ArcCoth(x)), F.Times(F.C1D2, F.Log(F.Subtract(F.C1, F.Sqr(x)))));
	// }
	// if (head.equals(F.ArcCsc)) {
	// // x*ArcCsc(x) + (Sqrt(1 - x^(-2))*x*Log(x + Sqrt(-1 + x^2))) /
	// // Sqrt(-1 + x^2)
	// return Plus(Times(x, F.ArcCsc(x)), Times(F.Sqrt(Plus(C1, Negate(Power(x, F.CN2)))), x,
	// Log(Plus(x, F.Sqrt(Plus(CN1, Power(x, C2))))), Power(F.Sqrt(Plus(CN1, Power(x, C2))), CN1)));
	// }
	// if (head.equals(F.ArcCsch)) {
	// // x*(ArcCsch(x) + (Sqrt(1 + x^(-2))*ArcSinh(x))/Sqrt(1 + x^2))
	// return Times(x, Plus(F.ArcCsch(x),
	// Times(Sqrt(Plus(C1, Power(x, CN2))), F.ArcSinh(x), Power(Sqrt(Plus(C1, Power(x, C2))), CN1))));
	// }
	// if (head.equals(F.ArcSec)) {
	// // x*ArcSec(x) - (Sqrt(1 - x^(-2))*x*Log(x + Sqrt(-1 +
	// // x^2)))/Sqrt(-1 + x^2)
	// return Plus(Times(x, F.ArcSec(x)), Times(CN1, Sqrt(Plus(C1, Times(CN1, Power(x, CN2)))), x,
	// Log(Plus(x, Sqrt(Plus(CN1, Power(x, C2))))), Power(Sqrt(Plus(CN1, Power(x, C2))), CN1)));
	// }
	// if (head.equals(F.ArcSech)) {
	// // x*ArcSech(x) - (2*Sqrt((1 - x)/(1 + x))*Sqrt(1 -
	// // x^2)*ArcSin(Sqrt(1 + x)/Sqrt(2)))/(-1 + x)
	// return Plus(Times(x, F.ArcSech(x)),
	// Times(CN1, C2, Sqrt(Times(Plus(C1, Times(CN1, x)), Power(Plus(C1, x), CN1))),
	// Sqrt(Plus(C1, Times(CN1, Power(x, C2)))),
	// F.ArcSin(Times(Sqrt(Plus(C1, x)), Power(Sqrt(C2), CN1))), Power(Plus(CN1, x), CN1)));
	// }
	// if (head.equals(F.ArcSin)) {
	// // x*ArcSin(x) + Sqrt(1-x^2)
	// return F.Plus(F.Times(x, F.ArcSin(x)), F.Sqrt(F.Subtract(F.C1, F.Sqr(x))));
	// }
	// if (head.equals(F.ArcSinh)) {
	// // x*ArcSinh(x) - Sqrt(1+x^2)
	// return F.Subtract(F.Times(x, F.ArcSinh(x)), F.Sqrt(F.Plus(F.C1, F.Sqr(x))));
	// }
	// if (head.equals(F.ArcTan)) {
	// // x*ArcTan(x) - (1/2 * Log(1+x^2))
	// return F.Subtract(F.Times(x, F.ArcTan(x)), F.Times(F.C1D2, F.Log(F.Plus(F.C1, F.Sqr(x)))));
	// }
	// if (head.equals(F.ArcTanh)) {
	// // x*ArcTanh(x) + (1/2 * Log(1-x^2))
	// return F.Plus(F.Times(x, F.ArcTanh(x)), F.Times(F.C1D2, F.Log(F.Subtract(F.C1, F.Sqr(x)))));
	// }
	// if (head.equals(F.Cos)) {
	// // Sin(x)
	// return F.Sin(x);
	// }
	// if (head.equals(F.Cosh)) {
	// // Sinh(x)
	// return F.Sinh(x);
	// }
	// if (head.equals(F.Cot)) {
	// // Log(Sin(x))
	// return F.Log(F.Sin(x));
	// }
	// if (head.equals(F.Coth)) {
	// // Log(Sinh(x))
	// return F.Log(F.Sinh(x));
	// }
	// if (head.equals(F.Csc)) {
	// // Log(Sin(x/2))-Log(Cos(x/2))
	// return F.Subtract(F.Log(F.Sin(F.Times(F.C1D2, x))), F.Log(F.Cos(F.Times(F.C1D2, x))));
	// }
	// if (head.equals(F.Csch)) {
	// // -Log(Cosh(x/2)) + Log(Sinh[x/2))
	// return Plus(Times(CN1, Log(F.Cosh(Times(C1D2, x)))), Log(F.Sinh(Times(C1D2, x))));
	// }
	// if (head.equals(F.Log)) {
	// // x*Log(x)-x
	// return F.Subtract(F.Times(x, F.Log(x)), x);
	// }
	// if (head.equals(F.Sec)) {
	// // Log( Sin(x/2)+Cos(x/2) ) - Log( Cos(x/2)-Sin(x/2) )
	// return F.Subtract(F.Log(F.Plus(F.Sin(F.Times(F.C1D2, x)), F.Cos(F.Times(F.C1D2, x)))),
	// F.Log(F.Subtract(F.Cos(F.Times(F.C1D2, x)), F.Sin(F.Times(F.C1D2, x)))));
	// }
	// if (head.equals(F.Sech)) {
	// // 2*ArcTan(Tanh(x/2))
	// return Times(C2, ArcTan(F.Tanh(Times(C1D2, x))));
	// }
	// if (head.equals(F.Sin)) {
	// // -Cos(x)
	// return F.Times(F.CN1, F.Cos(x));
	// }
	// if (head.equals(F.Sinh)) {
	// // Cosh(x)
	// return F.Cosh(x);
	// }
	// if (head.equals(F.Tan)) {
	// // -Log(Cos(x))
	// return F.Times(F.CN1, F.Log(F.Cos(x)));
	// }
	// if (head.equals(F.Tanh)) {
	// // Log(Cosh(x))
	// return F.Log(F.Cosh(x));
	// }
	// return F.NIL;
	// }

	/**
	 * Try using the <code>TrigReduce</code> function to get a <code>Plus(...)</code> expression which could be
	 * integrated.
	 * 
	 * @param timesAST
	 *            an IAST which is a <code>Times(...)</code> expression
	 * @param arg2
	 *            the symbol to get the indefinite integral for.
	 * @return <code>F.NIL</code> if no trigonometric funtion could be found.
	 */
//	private static IExpr integrateTimesTrigFunctions(final IAST timesAST, IExpr arg2) {
//		Predicate<IExpr> isTrigFunction = Predicates.isAST(new ISymbol[] { F.Cos, F.Sin });
//		if (timesAST.has(isTrigFunction, false)) {
//			IExpr fx = F.eval(F.TrigReduce(timesAST));
//			if (fx.isPlus()) {
//				ISymbol dummy = F.Dummy("dummy");
//				IPattern dummy_ = F.$p(dummy);
//				// Collect arguments for x
//				// Sin(x_) -> Sin(Collect(x, arg2))
//				fx = F.eval(F.ReplaceAll(fx, F.List(F.RuleDelayed(F.Sin(dummy_), F.Sin(F.Collect(dummy, arg2))),
//						F.RuleDelayed(F.Cos(dummy_), F.Cos(F.Collect(dummy, arg2))))));
//				return mapIntegrate((IAST) fx, arg2);
//			}
//		}
//		return F.NIL;
//	}

	/**
	 * Check if the polynomial has maximum degree 2 in 1 variable and return the coefficients.
	 * 
	 * @param poly
	 * @return <code>false</code> if the polynomials degree > 2 and number of variables <> 1
	 */
	public static boolean isQuadratic(GenPolynomial<BigRational> poly, BigRational[] result) {
		if (poly.degree() <= 2 && poly.numberOfVariables() == 1) {
			result[0] = BigRational.ZERO;
			result[1] = BigRational.ZERO;
			result[2] = BigRational.ZERO;
			for (Monomial<BigRational> monomial : poly) {
				BigRational coeff = monomial.coefficient();
				ExpVector exp = monomial.exponent();
				for (int i = 0; i < exp.length(); i++) {
					result[(int) exp.getVal(i)] = coeff;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Check if the polynomial has maximum degree 2 in 1 variable and return the coefficients.
	 * 
	 * @param poly
	 * @return <code>false</code> if the polynomials degree > 2 and number of variables <> 1
	 */
	public static boolean isQuadratic(GenPolynomial<BigInteger> poly, BigInteger[] result) {
		if (poly.degree() <= 2 && poly.numberOfVariables() == 1) {
			result[0] = BigInteger.ZERO;
			result[1] = BigInteger.ZERO;
			result[2] = BigInteger.ZERO;
			for (Monomial<BigInteger> monomial : poly) {
				BigInteger coeff = monomial.coefficient();
				ExpVector exp = monomial.exponent();
				for (int i = 0; i < exp.length(); i++) {
					result[(int) exp.getVal(i)] = coeff;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns an AST with head <code>Plus</code>, which contains the partial fraction decomposition of the numerator
	 * and denominator parts.
	 * 
	 * @param parts
	 * @param variableList
	 * @return <code>F.NIL</code> if the partial fraction decomposition wasn't constructed
	 */
	// private static IAST integrateByPartialFractions(IExpr[] parts, ISymbol x)
	// {
	// try {
	// IAST variableList = F.List(x);
	// IExpr exprNumerator = F.expandAll(parts[0], true, false);
	// IExpr exprDenominator = F.expandAll(parts[1], true, false);
	// ASTRange r = new ASTRange(variableList, 1);
	// List<IExpr> varList = r.toList();
	//
	// String[] varListStr = new String[1];
	// varListStr[0] = variableList.arg1().toString();
	// JASConvert<BigRational> jas = new JASConvert<BigRational>(varList,
	// BigRational.ZERO);
	// GenPolynomial<BigRational> numerator = jas.expr2JAS(exprNumerator,
	// false);
	// GenPolynomial<BigRational> denominator = jas.expr2JAS(exprDenominator,
	// false);
	//
	// // get factors
	// FactorAbstract<BigRational> factorAbstract =
	// FactorFactory.getImplementation(BigRational.ZERO);
	// SortedMap<GenPolynomial<BigRational>, Long> sfactors =
	// factorAbstract.baseFactors(denominator);
	//
	// List<GenPolynomial<BigRational>> D = new
	// ArrayList<GenPolynomial<BigRational>>(sfactors.keySet());
	//
	// SquarefreeAbstract<BigRational> sqf =
	// SquarefreeFactory.getImplementation(BigRational.ZERO);
	// List<List<GenPolynomial<BigRational>>> Ai =
	// sqf.basePartialFraction(numerator, sfactors);
	// // returns [ [Ai0, Ai1,..., Aie_i], i=0,...,k ] with A/prod(D) =
	// // A0 + sum( sum ( Aij/di^j ) ) with deg(Aij) < deg(di).
	//
	// if (Ai.size() > 0) {
	// IAST result = F.Plus();
	// IExpr temp;
	// if (!Ai.get(0).get(0).isZERO()) {
	// temp = F.eval(jas.poly2Expr(Ai.get(0).get(0)));
	// if (temp.isAST()) {
	// ((IAST) temp).addEvalFlags(IAST.IS_DECOMPOSED_PARTIAL_FRACTION);
	// }
	// result.add(F.Integrate(temp, x));
	// }
	// for (int i = 1; i < Ai.size(); i++) {
	// List<GenPolynomial<BigRational>> list = Ai.get(i);
	// long j = 0L;
	// for (GenPolynomial<BigRational> genPolynomial : list) {
	// if (!genPolynomial.isZERO()) {
	// BigRational[] numer = new BigRational[3];
	// BigRational[] denom = new BigRational[3];
	// boolean isDegreeLE2 = D.get(i - 1).degree() <= 2;
	// if (isDegreeLE2 && j == 1L) {
	// Object[] objects = jas.factorTerms(genPolynomial);
	// java.math.BigInteger gcd = (java.math.BigInteger) objects[0];
	// java.math.BigInteger lcm = (java.math.BigInteger) objects[1];
	// GenPolynomial<edu.jas.arith.BigInteger> genPolynomial2 =
	// ((GenPolynomial<edu.jas.arith.BigInteger>) objects[2])
	// .multiply(edu.jas.arith.BigInteger.valueOf(gcd));
	// GenPolynomial<BigRational> Di_1 = D.get(i -
	// 1).multiply(BigRational.valueOf(lcm));
	// if (genPolynomial2.isONE()) {
	// isQuadratic(Di_1, denom);
	// IFraction a = F.fraction(denom[2].numerator(), denom[2].denominator());
	// IFraction b = F.fraction(denom[1].numerator(), denom[1].denominator());
	// IFraction c = F.fraction(denom[0].numerator(), denom[0].denominator());
	// if (a.isZero()) {
	// // JavaForm[Log[b*x+c]/b]
	// result.add(Times(Log(Plus(c, Times(b, x))), Power(b, CN1)));
	// } else {
	// // compute b^2-4*a*c from
	// // (a*x^2+b*x+c)
	// BigRational cmp = denom[1].multiply(denom[1]).subtract(
	// BigRational.valueOf(4L).multiply(denom[2]).multiply(denom[0]));
	// int cmpTo = cmp.compareTo(BigRational.ZERO);
	// // (2*a*x+b)
	// IExpr ax2Plusb = F.Plus(F.Times(F.C2, a, x), b);
	// if (cmpTo == 0) {
	// // (-2) / (2*a*x+b)
	// result.add(F.Times(F.integer(-2L), F.Power(ax2Plusb, F.CN1)));
	// } else if (cmpTo > 0) {
	// // (b^2-4ac)^(1/2)
	// temp = F.eval(F.Power(F.Subtract(F.Sqr(b), F.Times(F.C4, a, c)),
	// F.C1D2));
	// result.add(F.Times(F.Power(temp, F.CN1),
	// F.Log(F.Times(F.Subtract(ax2Plusb, temp),
	// Power(F.Plus(ax2Plusb, temp), F.CN1)))));
	// } else {
	// // (4ac-b^2)^(1/2)
	// temp = F.eval(F.Power(F.Subtract(F.Times(F.C4, a, c), F.Sqr(b)),
	// F.CN1D2));
	// result.add(F.Times(F.C2, temp, F.ArcTan(Times(ax2Plusb, temp))));
	// }
	// }
	// } else {
	// isQuadratic(genPolynomial, numer);
	// IFraction A = F.fraction(numer[1].numerator(), numer[1].denominator());
	// IFraction B = F.fraction(numer[0].numerator(), numer[0].denominator());
	// isQuadratic(D.get(i - 1), denom);
	// IFraction p = F.fraction(denom[1].numerator(), denom[1].denominator());
	// IFraction q = F.fraction(denom[0].numerator(), denom[0].denominator());
	// if (A.isZero()) {
	// // JavaForm[B*Log[p*x+q]/p]
	// temp = Times(B, Log(Plus(q, Times(p, x))), Power(p, CN1));
	// } else {
	// //
	// JavaForm[A/2*Log[x^2+p*x+q]+(2*B-A*p)/(4*q-p^2)^(1/2)*ArcTan[(2*x+p)/(4*q-p^2)^(1/2)]]
	// temp = Plus(
	// Times(C1D2, A, Log(Plus(q, Times(p, x), Power(x, C2)))),
	// Times(ArcTan(Times(Plus(p, Times(C2, x)),
	// Power(Plus(Times(CN1, Power(p, C2)), Times(C4, q)), CN1D2))),
	// Plus(Times(C2, B), Times(CN1, A, p)),
	// Power(Plus(Times(CN1, Power(p, C2)), Times(C4, q)), CN1D2)));
	// }
	// result.add(F.eval(temp));
	//
	// // edu.jas.arith.BigInteger[] numer2 = new
	// // edu.jas.arith.BigInteger[3];
	// // isQuadratic(genPolynomial2, numer2);
	// // IInteger A =
	// // F.integer(numer2[1].getVal());
	// // IInteger B =
	// // F.integer(numer2[0].getVal());
	// // isQuadratic(Di_1, denom);
	// // IFraction p =
	// // F.fraction(denom[1].numerator(),
	// // denom[1].denominator());
	// // IFraction q =
	// // F.fraction(denom[0].numerator(),
	// // denom[0].denominator());
	// // if (A.isZero()) {
	// // // JavaForm[B*Log[p*x+q]/p]
	// // temp = Times(B, Log(Plus(q, Times(p,
	// // x))), Power(p, CN1));
	// // } else {
	// // //
	// //
	// JavaForm[A/2*Log[x^2+p*x+q]+(2*B-A*p)/(4*q-p^2)^(1/2)*ArcTan[(2*x+p)/(4*q-p^2)^(1/2)]]
	// // temp = Plus(
	// // Times(C1D2, A, Log(Plus(q, Times(p, x),
	// // Power(x, C2)))),
	// // Times(ArcTan(Times(Plus(p, Times(C2, x)),
	// // Power(Plus(Times(CN1, Power(p, C2)),
	// // Times(C4, q)), CN1D2))),
	// // Plus(Times(C2, B), Times(CN1, A, p)),
	// // Power(Plus(Times(CN1, Power(p, C2)),
	// // Times(C4, q)), CN1D2)));
	// // }
	// // result.add(F.eval(temp));
	// }
	// } else if (isDegreeLE2 && j > 1L) {
	// isQuadratic(genPolynomial, numer);
	// IFraction A = F.fraction(numer[1].numerator(), numer[1].denominator());
	// IFraction B = F.fraction(numer[0].numerator(), numer[0].denominator());
	// isQuadratic(D.get(i - 1), denom);
	// IFraction a = F.fraction(denom[2].numerator(), denom[2].denominator());
	// IFraction b = F.fraction(denom[1].numerator(), denom[1].denominator());
	// IFraction c = F.fraction(denom[0].numerator(), denom[0].denominator());
	// IInteger k = F.integer(j);
	// if (A.isZero()) {
	// // JavaForm[B*((2*a*x+b)/((k-1)*(4*a*c-b^2)*(a*x^2+b*x+c)^(k-1))+
	// // (4*k*a-6*a)/((k-1)*(4*a*c-b^2))*Integrate[(a*x^2+b*x+c)^(-k+1),x])]
	// temp = Times(
	// B,
	// Plus(Times(
	// Integrate(
	// Power(Plus(c, Times(b, x), Times(a, Power(x, C2))),
	// Plus(C1, Times(CN1, k))), x),
	// Plus(Times(F.integer(-6L), a), Times(C4, a, k)), Power(Plus(CN1, k),
	// CN1),
	// Power(Plus(Times(CN1, Power(b, C2)), Times(C4, a, c)), CN1)),
	// Times(Plus(b, Times(C2, a, x)),
	// Power(Plus(CN1, k), CN1),
	// Power(Plus(Times(CN1, Power(b, C2)), Times(C4, a, c)), CN1),
	// Power(Plus(c, Times(b, x), Times(a, Power(x, C2))),
	// Times(CN1, Plus(CN1, k))))));
	// } else {
	// //
	// JavaForm[(-A)/(2*a*(k-1)*(a*x^2+b*x+c)^(k-1))+(B-A*b/(2*a))*Integrate[(a*x^2+b*x+c)^(-k),x]]
	// temp = Plus(
	// Times(Integrate(Power(Plus(c, Times(b, x), Times(a, Power(x, C2))),
	// Times(CN1, k)), x),
	// Plus(B, Times(CN1D2, A, Power(a, CN1), b))),
	// Times(CN1D2, A, Power(a, CN1), Power(Plus(CN1, k), CN1),
	// Power(Plus(c, Times(b, x), Times(a, Power(x, C2))), Times(CN1, Plus(CN1,
	// k)))));
	// }
	// result.add(F.eval(temp));
	// } else {
	// temp = F.eval(F.Times(jas.poly2Expr(genPolynomial),
	// F.Power(jas.poly2Expr(D.get(i - 1)), F.integer(j * (-1L)))));
	// if (!temp.equals(F.C0)) {
	// if (temp.isAST()) {
	// ((IAST) temp).addEvalFlags(IAST.IS_DECOMPOSED_PARTIAL_FRACTION);
	// }
	// result.add(F.Integrate(temp, x));
	// }
	// }
	// }
	// j++;
	// }
	//
	// }
	// return result;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } catch (JASConversionException e) {
	// if (Config.DEBUG) {
	// e.printStackTrace();
	// }
	// }
	// return F.NIL;
	// }

	/**
	 * See <a href="http://en.wikipedia.org/wiki/Integration_by_parts">Wikipedia- Integration by parts</a>
	 * 
	 * @param ast
	 *            TODO - not used
	 * @param arg1
	 * @param symbol
	 * 
	 * @return
	 */
	private static IExpr integratePolynomialByParts(IAST ast, final IAST arg1, IExpr symbol) {
		IASTAppendable fTimes = F.TimesAlloc(arg1.size());
		IASTAppendable gTimes = F.TimesAlloc(arg1.size());
		collectPolynomialTerms(arg1, symbol, gTimes, fTimes);
		IExpr g = gTimes.oneIdentity1();
		IExpr f = fTimes.oneIdentity1();
		// confLICTS WITH RUBI 4.5 INTEGRATION RULES
		// ONLY call integrateBy Parts for simple Times() expression
		if (f.isOne() || g.isOne()) {
			return F.NIL;
		}
		return integrateByParts(f, g, symbol);
	}

	/**
	 * Use the <a href="http://www.apmaths.uwo.ca/~arich/">Rubi - Symbolic Integration Rules</a> to integrate the
	 * expression.
	 * 
	 * @param ast
	 * @return
	 */
	private static IExpr integrateByRubiRules(IAST arg1, IExpr x, IAST ast) {
		EvalEngine engine = EvalEngine.get();
		int limit = engine.getRecursionLimit();
		boolean quietMode = engine.isQuietMode();
		ISymbol head = arg1.topHead();

		if ((head.getAttributes() & ISymbol.NUMERICFUNCTION) == ISymbol.NUMERICFUNCTION
				|| INT_RUBI_FUNCTIONS.contains(head) || head.getSymbolName().startsWith("§")) {

			boolean newCache = false;
			try {

				if (engine.REMEMBER_AST_CACHE != null) {
					IExpr result = engine.REMEMBER_AST_CACHE.getIfPresent(ast);
					if (result != null) {// &&engine.getRecursionCounter()>0) {
						if (result.isPresent()) {
							return result;
						}
						IExpr temp = callRestIntegrate(arg1, x, engine);
						if (temp.isPresent()) {
							return temp;
						}
						// RecursionLimitExceeded.throwIt(engine.getRecursionCounter(), ast);
						return F.NIL;
					}
				} else {
					newCache = true;
					engine.REMEMBER_AST_CACHE = CacheBuilder.newBuilder().maximumSize(50).build();
				}
				try {
					engine.setQuietMode(true);
					if (limit <= 0 || limit > Config.INTEGRATE_RUBI_RULES_RECURSION_LIMIT) {
						engine.setRecursionLimit(Config.INTEGRATE_RUBI_RULES_RECURSION_LIMIT);
					}

					// System.out.println(ast.toString());
					engine.REMEMBER_AST_CACHE.put(ast, F.NIL);
					IExpr temp = F.Integrate.evalDownRule(EvalEngine.get(), ast);
					if (temp.isPresent()) {
						engine.REMEMBER_AST_CACHE.put(ast, temp);
						return temp;
					}
				} catch (RecursionLimitExceeded rle) {
					// engine.printMessage("Integrate(Rubi recursion): " + Config.INTEGRATE_RUBI_RULES_RECURSION_LIMIT
					// + " exceeded: " + ast.toString());
					engine.printMessage("Integrate(Rubi recursion): " + rle.getMessage());
					engine.setRecursionLimit(limit);
				} catch (RuntimeException rex) {
					if (Config.SHOW_STACKTRACE) {
						rex.printStackTrace();
					}
					engine.printMessage("Integrate Rubi recursion limit " + Config.INTEGRATE_RUBI_RULES_RECURSION_LIMIT
							+ " RuntimeException: " + ast.toString());
					engine.setRecursionLimit(limit);

				}

			} catch (AbortException ae) {
				if (Config.DEBUG) {
					ae.printStackTrace();
				}
			} finally {
				engine.setRecursionLimit(limit);
				if (newCache) {
					engine.REMEMBER_AST_CACHE = null;
				}
				engine.setQuietMode(quietMode);
			}
		}
		return F.NIL;
	}

	/**
	 * <p>
	 * Integrate by parts rule: <code>Integrate(f'(x) * g(x), x) = f(x) * g(x) - Integrate(f(x) * g'(x),x )</code> .
	 * </p>
	 * 
	 * See <a href="http://en.wikipedia.org/wiki/Integration_by_parts">Wikipedia- Integration by parts</a>
	 * 
	 * @param f
	 *            <code>f(x)</code>
	 * @param g
	 *            <code>g(x)</code>
	 * @param x
	 * @return <code>f(x) * g(x) - Integrate(f(x) * g'(x),x )</code>
	 */
	private static IExpr integrateByParts(IExpr f, IExpr g, IExpr x) {
		EvalEngine engine = EvalEngine.get();
		int limit = engine.getRecursionLimit();
		try {
			if (limit <= 0 || limit > Config.INTEGRATE_BY_PARTS_RECURSION_LIMIT) {
				engine.setRecursionLimit(Config.INTEGRATE_BY_PARTS_RECURSION_LIMIT);
			}
			IExpr firstIntegrate = F.eval(F.Integrate(f, x));
			if (!firstIntegrate.isFreeAST(Integrate)) {
				return F.NIL;
			}
			IExpr gDerived = F.eval(F.D(g, x));
			IExpr second2Integrate = F.eval(F.Integrate(F.Times(gDerived, firstIntegrate), x));
			if (!second2Integrate.isFreeAST(Integrate)) {
				return F.NIL;
			}
			return F.eval(F.Subtract(F.Times(g, firstIntegrate), second2Integrate));
		} catch (RecursionLimitExceeded rle) {
			engine.setRecursionLimit(limit);
		} finally {
			engine.setRecursionLimit(limit);
		}
		return F.NIL;
	}

	/**
	 * Collect all found polynomial terms into <code>polyTimes</code> and the rest into <code>restTimes</code>.
	 * 
	 * @param timesAST
	 *            an AST representing a <code>Times[...]</code> expression.
	 * @param symbol
	 * @param polyTimes
	 *            the polynomial terms part
	 * @param restTimes
	 *            the non-polynomil terms part
	 */
	private static void collectPolynomialTerms(final IAST timesAST, IExpr symbol, IASTAppendable polyTimes,
			IASTAppendable restTimes) {
		IExpr temp;
		for (int i = 1; i < timesAST.size(); i++) {
			temp = timesAST.get(i);
			if (temp.isFree(symbol, true)) {
				polyTimes.append(temp);
				continue;
			} else if (temp.equals(symbol)) {
				polyTimes.append(temp);
				continue;
			} else if (temp.isPolynomial(List(symbol))) {
				polyTimes.append(temp);
				continue;
			}
			restTimes.append(temp);
		}
	}

	/**
	 * Get the rules defined for Integrate function. These rules are loaded, if the Integrate function is used the first
	 * time.
	 * 
	 * @see AbstractFunctionEvaluator#setUp(ISymbol)()
	 */
	@Override
	public IAST getRuleAST() {
		// long start = System.currentTimeMillis();

		// if (!Config.LOAD_SERIALIZED_RULES) {

		// return getRuleASTStatic();

		// if (Config.SHOW_STACKTRACE) {
		// long end = System.currentTimeMillis();
		// System.out.println(end - start);
		// }
		// return ast;
		return null;
	}

	public static synchronized void getRuleASTStatic() {
		if (!INTEGRATE_RULES_READ) {
			INTEGRATE_RULES_READ = true;
			INTEGRATE_RULES_DATA = F.Integrate.createRulesData(new int[] { 0, 7000 });
			getRuleASTRubi45();

			// RulesData rd = F.Integrate.getRulesData();
			// Set<IPatternMatcher> set = rd.getPatternDownRules();
			// IPatternMatcher last = null;
			// for (IPatternMatcher matcher : set) {
			// System.out.print(matcher.getLHSPriority());
			// System.out.println(" - " +matcher.determinePatterns());
			// }

			// INT_FUNCTIONS.add(F.Cos);
			// INT_FUNCTIONS.add(F.Cot);
			// INT_FUNCTIONS.add(F.Csc);
			// INT_FUNCTIONS.add(F.Sec);
			// INT_FUNCTIONS.add(F.Sin);
			// INT_FUNCTIONS.add(F.Tan);
			//
			// INT_FUNCTIONS.add(F.ArcCos);
			// INT_FUNCTIONS.add(F.ArcCot);
			// INT_FUNCTIONS.add(F.ArcCsc);
			// INT_FUNCTIONS.add(F.ArcSec);
			// INT_FUNCTIONS.add(F.ArcSin);
			// INT_FUNCTIONS.add(F.ArcTan);
			//
			// INT_FUNCTIONS.add(F.Cosh);
			// INT_FUNCTIONS.add(F.Coth);
			// INT_FUNCTIONS.add(F.Csch);
			// INT_FUNCTIONS.add(F.Sech);
			// INT_FUNCTIONS.add(F.Sinh);
			// INT_FUNCTIONS.add(F.Tanh);
			//
			// INT_FUNCTIONS.add(F.ArcCosh);
			// INT_FUNCTIONS.add(F.ArcCoth);
			// INT_FUNCTIONS.add(F.ArcCsc);
			// INT_FUNCTIONS.add(F.ArcSec);
			// INT_FUNCTIONS.add(F.ArcSinh);
			// INT_FUNCTIONS.add(F.ArcTanh);

			ISymbol[] rubiSymbols = { F.Derivative, F.D };
			for (int i = 0; i < rubiSymbols.length; i++) {
				INT_RUBI_FUNCTIONS.add(rubiSymbols[i]);
			}
		}
	}

	private static void getRuleASTRubi45() {
		IAST init;
		init = org.matheclipse.core.integrate.rubi.IntRules0.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules1.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules2.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules3.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules4.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules5.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules6.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules7.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules8.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules9.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules10.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules11.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules12.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules13.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules14.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules15.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules16.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules17.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules18.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules19.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules20.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules21.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules22.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules23.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules24.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules25.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules26.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules27.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules28.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules29.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules30.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules31.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules32.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules33.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules34.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules35.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules36.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules37.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules38.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules39.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules40.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules41.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules42.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules43.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules44.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules45.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules46.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules47.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules48.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules49.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules50.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules51.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules52.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules53.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules54.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules55.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules56.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules57.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules58.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules59.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules60.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules61.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules62.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules63.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules64.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules65.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules66.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules67.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules68.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules69.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules70.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules71.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules72.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules73.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules74.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules75.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules76.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules77.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules78.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules79.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules80.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules81.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules82.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules83.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules84.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules85.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules86.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules87.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules88.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules89.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules90.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules91.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules92.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules93.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules94.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules95.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules96.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules97.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules98.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules99.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules100.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules101.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules102.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules103.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules104.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules105.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules106.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules107.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules108.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules109.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules110.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules111.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules112.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules113.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules114.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules115.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules116.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules117.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules118.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules119.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules120.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules121.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules122.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules123.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules124.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules125.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules126.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules127.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules128.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules129.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules130.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules131.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules132.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules133.RULES;
		init = org.matheclipse.core.integrate.rubi.IntRules134.RULES;
	}

	/**
	 * Get the rules defined for Integrate utility functions. These rules are loaded on system startup.
	 * 
	 * @see AbstractFunctionEvaluator#setUp(ISymbol)()
	 */
	public static void getUtilityFunctionsRuleAST() {
		getUtilityFunctionsRuleASTRubi45();
	}

	private static void getUtilityFunctionsRuleASTRubi45() {
		IAST ast = org.matheclipse.core.integrate.rubi.UtilityFunctions0.RULES;
		ast = org.matheclipse.core.integrate.rubi.UtilityFunctions1.RULES;
		ast = org.matheclipse.core.integrate.rubi.UtilityFunctions2.RULES;
		ast = org.matheclipse.core.integrate.rubi.UtilityFunctions3.RULES;
		ast = org.matheclipse.core.integrate.rubi.UtilityFunctions4.RULES;
		ast = org.matheclipse.core.integrate.rubi.UtilityFunctions5.RULES;
		ast = org.matheclipse.core.integrate.rubi.UtilityFunctions6.RULES;
		ast = org.matheclipse.core.integrate.rubi.UtilityFunctions7.RULES;
		ast = org.matheclipse.core.integrate.rubi.UtilityFunctions8.RULES;

		// org.matheclipse.core.integrate.rubi.UtilityFunctions.init();
	}

	@Override
	public void setUp(final ISymbol newSymbol) {
		newSymbol.setAttributes(ISymbol.HOLDALL);
		super.setUp(newSymbol);

		// if (Config.LOAD_SERIALIZED_RULES) {
		// initSerializedRules(symbol);
		// }

		// hack for TimeConstrained time limit:

		// F.ISet(F.$s("§timelimit"), F.integer(12));

		F.ISet(F.$s("§simplifyflag"), F.False);

		F.ISet(F.$s("§$timelimit"), F.ZZ(Config.INTEGRATE_RUBI_TIMELIMIT));
		F.ISet(F.$s("§$showsteps"), F.False);
		UtilityFunctionCtors.ReapList.setAttributes(ISymbol.HOLDFIRST);
		F.ISet(F.$s("§$trigfunctions"), F.List(F.Sin, F.Cos, F.Tan, F.Cot, F.Sec, F.Csc));
		F.ISet(F.$s("§$hyperbolicfunctions"), F.List(F.Sinh, F.Cosh, F.Tanh, F.Coth, F.Sech, F.Csch));
		F.ISet(F.$s("§$inversetrigfunctions"), F.List(F.ArcSin, F.ArcCos, F.ArcTan, F.ArcCot, F.ArcSec, F.ArcCsc));
		F.ISet(F.$s("§$inversehyperbolicfunctions"),
				F.List(F.ArcSinh, F.ArcCosh, F.ArcTanh, F.ArcCoth, F.ArcSech, F.ArcCsch));
		F.ISet(F.$s("§$calculusfunctions"), F.List(F.D, Integrate, F.Sum, F.Product, F.Integrate,
				F.$rubi("Unintegrable"), F.$rubi("CannotIntegrate"), F.$rubi("Dif"), F.$rubi("Subst")));
		F.ISet(F.$s("§$stopfunctions"), F.List(F.Hold, F.HoldForm, F.Defer, F.Pattern, F.If, F.Integrate,
				F.$rubi("Unintegrable"), F.$rubi("CannotIntegrate")));
		F.ISet(F.$s("§$heldfunctions"), F.List(F.Hold, F.HoldForm, F.Defer, F.Pattern));

		F.ISet(UtilityFunctionCtors.IntegerPowerQ, //
				F.Function(F.And(F.SameQ(F.Head(F.Slot1), F.Power), F.IntegerQ(F.Part(F.Slot1, F.C2)))));

		F.ISet(UtilityFunctionCtors.FractionalPowerQ, //
				F.Function(
						F.And(F.SameQ(F.Head(F.Slot1), F.Power), F.SameQ(F.Head(F.Part(F.Slot1, F.C2)), F.Rational))));
	}

	/**
	 * Initialize the serialized Rubi integration rules from ressource <code>/ser/integrate.ser</code>.
	 * 
	 * @param symbol
	 */
	// privaze static void initSerializedRules(final ISymbol symbol) {
	// if (!INITIALIZED) {
	// INITIALIZED = true;
	// AbstractFunctionEvaluator.initSerializedRules(symbol);
	// }
	// }

}