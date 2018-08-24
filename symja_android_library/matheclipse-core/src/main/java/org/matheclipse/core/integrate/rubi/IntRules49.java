package org.matheclipse.core.integrate.rubi;


import static org.matheclipse.core.expression.F.*;
import static org.matheclipse.core.integrate.rubi.UtilityFunctionCtors.*;
import static org.matheclipse.core.integrate.rubi.UtilityFunctions.*;
import org.matheclipse.core.interfaces.IAST;

/** 
 * IndefiniteIntegrationRules from the <a href="http://www.apmaths.uwo.ca/~arich/">Rubi -
 * rule-based integrator</a>.
 *  
 */
public class IntRules49 { 
  public static IAST RULES = List( 
IIntegrate(2451,Int(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_),x_Symbol),
    Condition(With(List(Set(k,Denominator(n))),Dist(k,Subst(Int(Times(Power(x,Plus(k,Negate(C1))),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,Times(k,n)))),p))))),q)),x),x,Power(x,Power(k,-1))),x)),And(FreeQ(List(a,b,c,d,e,p,q),x),FractionQ(n)))),
IIntegrate(2452,Int(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_),x_Symbol),
    Condition(Unintegrable(Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),q),x),FreeQ(List(a,b,c,d,e,n,p,q),x))),
IIntegrate(2453,Int(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(v_,p_DEFAULT))),b_DEFAULT)),q_DEFAULT),x_Symbol),
    Condition(Int(Power(Plus(a,Times(b,Log(Times(c,Power(ExpandToSum(v,x),p))))),q),x),And(FreeQ(List(a,b,c,p,q),x),BinomialQ(v,x),Not(BinomialMatchQ(v,x))))),
IIntegrate(2454,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(x_,m_DEFAULT)),x_Symbol),
    Condition(Dist(Power(n,-1),Subst(Int(Times(Power(x,Plus(Simplify(Times(Plus(m,C1),Power(n,-1))),Negate(C1))),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,x)),p))))),q)),x),x,Power(x,n)),x),And(FreeQ(List(a,b,c,d,e,m,n,p,q),x),IntegerQ(Simplify(Times(Plus(m,C1),Power(n,-1)))),Or(GtQ(Times(Plus(m,C1),Power(n,-1)),C0),IGtQ(q,C0)),Not(And(EqQ(q,C1),ILtQ(n,C0),IGtQ(m,C0)))))),
IIntegrate(2455,Int(Times(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),Power(Times(f_DEFAULT,x_),m_DEFAULT)),x_Symbol),
    Condition(Plus(Simp(Times(Power(Times(f,x),Plus(m,C1)),Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),Power(Times(f,Plus(m,C1)),-1)),x),Negate(Dist(Times(b,e,n,p,Power(Times(f,Plus(m,C1)),-1)),Int(Times(Power(x,Plus(n,Negate(C1))),Power(Times(f,x),Plus(m,C1)),Power(Plus(d,Times(e,Power(x,n))),-1)),x),x))),And(FreeQ(List(a,b,c,d,e,f,m,n,p),x),NeQ(m,CN1)))),
IIntegrate(2456,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(Times(f_,x_),m_)),x_Symbol),
    Condition(Dist(Times(Power(Times(f,x),m),Power(Power(x,m),-1)),Int(Times(Power(x,m),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),q)),x),x),And(FreeQ(List(a,b,c,d,e,f,m,n,p,q),x),IntegerQ(Simplify(Times(Plus(m,C1),Power(n,-1)))),Or(GtQ(Times(Plus(m,C1),Power(n,-1)),C0),IGtQ(q,C0))))),
IIntegrate(2457,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_),Power(Times(f_DEFAULT,x_),m_DEFAULT)),x_Symbol),
    Condition(Plus(Simp(Times(Power(Times(f,x),Plus(m,C1)),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),q),Power(Times(f,Plus(m,C1)),-1)),x),Negate(Dist(Times(b,e,n,p,q,Power(Times(Power(f,n),Plus(m,C1)),-1)),Int(Times(Power(Times(f,x),Plus(m,n)),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),Plus(q,Negate(C1))),Power(Plus(d,Times(e,Power(x,n))),-1)),x),x))),And(FreeQ(List(a,b,c,d,e,f,m,p),x),IGtQ(q,C1),IntegerQ(n),NeQ(m,CN1)))),
IIntegrate(2458,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_),Power(x_,m_DEFAULT)),x_Symbol),
    Condition(With(List(Set(k,Denominator(n))),Dist(k,Subst(Int(Times(Power(x,Plus(Times(k,Plus(m,C1)),Negate(C1))),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,Times(k,n)))),p))))),q)),x),x,Power(x,Power(k,-1))),x)),And(FreeQ(List(a,b,c,d,e,m,p,q),x),FractionQ(n)))),
IIntegrate(2459,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(Times(f_,x_),m_)),x_Symbol),
    Condition(Dist(Times(Power(Times(f,x),m),Power(Power(x,m),-1)),Int(Times(Power(x,m),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),q)),x),x),And(FreeQ(List(a,b,c,d,e,f,m,p,q),x),FractionQ(n)))),
IIntegrate(2460,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(Times(f_DEFAULT,x_),m_DEFAULT)),x_Symbol),
    Condition(Unintegrable(Times(Power(Times(f,x),m),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),q)),x),FreeQ(List(a,b,c,d,e,f,m,n,p,q),x))),
IIntegrate(2461,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(v_,p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(Times(f_DEFAULT,x_),m_DEFAULT)),x_Symbol),
    Condition(Int(Times(Power(Times(f,x),m),Power(Plus(a,Times(b,Log(Times(c,Power(ExpandToSum(v,x),p))))),q)),x),And(FreeQ(List(a,b,c,f,m,p,q),x),BinomialQ(v,x),Not(BinomialMatchQ(v,x))))),
IIntegrate(2462,Int(Times(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),Power(Plus(f_DEFAULT,Times(g_DEFAULT,x_)),-1)),x_Symbol),
    Condition(Plus(Simp(Times(Log(Plus(f,Times(g,x))),Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),Power(g,-1)),x),Negate(Dist(Times(b,e,n,p,Power(g,-1)),Int(Times(Power(x,Plus(n,Negate(C1))),Log(Plus(f,Times(g,x))),Power(Plus(d,Times(e,Power(x,n))),-1)),x),x))),And(FreeQ(List(a,b,c,d,e,f,g,n,p),x),RationalQ(n)))),
IIntegrate(2463,Int(Times(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),Power(Plus(f_DEFAULT,Times(g_DEFAULT,x_)),r_DEFAULT)),x_Symbol),
    Condition(Plus(Simp(Times(Power(Plus(f,Times(g,x)),Plus(r,C1)),Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),Power(Times(g,Plus(r,C1)),-1)),x),Negate(Dist(Times(b,e,n,p,Power(Times(g,Plus(r,C1)),-1)),Int(Times(Power(x,Plus(n,Negate(C1))),Power(Plus(f,Times(g,x)),Plus(r,C1)),Power(Plus(d,Times(e,Power(x,n))),-1)),x),x))),And(FreeQ(List(a,b,c,d,e,f,g,n,p,r),x),Or(IGtQ(r,C0),RationalQ(n)),NeQ(r,CN1)))),
IIntegrate(2464,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(Plus(f_DEFAULT,Times(g_DEFAULT,x_)),r_DEFAULT)),x_Symbol),
    Condition(Unintegrable(Times(Power(Plus(f,Times(g,x)),r),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),q)),x),FreeQ(List(a,b,c,d,e,f,g,n,p,q,r),x))),
IIntegrate(2465,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(v_,p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(u_,r_DEFAULT)),x_Symbol),
    Condition(Int(Times(Power(ExpandToSum(u,x),r),Power(Plus(a,Times(b,Log(Times(c,Power(ExpandToSum(v,x),p))))),q)),x),And(FreeQ(List(a,b,c,p,q,r),x),LinearQ(u,x),BinomialQ(v,x),Not(And(LinearMatchQ(u,x),BinomialMatchQ(v,x)))))),
IIntegrate(2466,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(x_,m_DEFAULT),Power(Plus(f_DEFAULT,Times(g_DEFAULT,x_)),r_DEFAULT)),x_Symbol),
    Condition(Int(ExpandIntegrand(Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),q),Times(Power(x,m),Power(Plus(f,Times(g,x)),r)),x),x),And(FreeQ(List(a,b,c,d,e,f,g,n,p,q),x),IntegerQ(m),IntegerQ(r)))),
IIntegrate(2467,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_DEFAULT))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(Times(h_DEFAULT,x_),m_),Power(Plus(f_DEFAULT,Times(g_DEFAULT,x_)),r_DEFAULT)),x_Symbol),
    Condition(With(List(Set(k,Denominator(m))),Dist(Times(k,Power(h,-1)),Subst(Int(Times(Power(x,Plus(Times(k,Plus(m,C1)),Negate(C1))),Power(Plus(f,Times(g,Power(x,k),Power(h,-1))),r),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,Times(k,n)),Power(Power(h,n),-1))),p))))),q)),x),x,Power(Times(h,x),Power(k,-1))),x)),And(FreeQ(List(a,b,c,d,e,f,g,h,p,r),x),FractionQ(m),IntegerQ(n),IntegerQ(r)))),
IIntegrate(2468,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(Times(h_DEFAULT,x_),m_DEFAULT),Power(Plus(f_DEFAULT,Times(g_DEFAULT,x_)),r_DEFAULT)),x_Symbol),
    Condition(Unintegrable(Times(Power(Times(h,x),m),Power(Plus(f,Times(g,x)),r),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),q)),x),FreeQ(List(a,b,c,d,e,f,g,h,m,n,p,q,r),x))),
IIntegrate(2469,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(v_,p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(u_,r_DEFAULT),Power(Times(h_DEFAULT,x_),m_DEFAULT)),x_Symbol),
    Condition(Int(Times(Power(Times(h,x),m),Power(ExpandToSum(u,x),r),Power(Plus(a,Times(b,Log(Times(c,Power(ExpandToSum(v,x),p))))),q)),x),And(FreeQ(List(a,b,c,h,m,p,q,r),x),LinearQ(u,x),BinomialQ(v,x),Not(And(LinearMatchQ(u,x),BinomialMatchQ(v,x)))))),
IIntegrate(2470,Int(Times(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),Power(Plus(f_,Times(g_DEFAULT,Sqr(x_))),-1)),x_Symbol),
    Condition(With(List(Set(u,IntHide(Power(Plus(f,Times(g,Sqr(x))),-1),x))),Plus(Simp(Times(u,Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p)))))),x),Negate(Dist(Times(b,e,n,p),Int(Times(u,Power(x,Plus(n,Negate(C1))),Power(Plus(d,Times(e,Power(x,n))),-1)),x),x)))),And(FreeQ(List(a,b,c,d,e,f,g,n,p),x),IntegerQ(n)))),
IIntegrate(2471,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(Plus(f_,Times(g_DEFAULT,Power(x_,s_))),r_DEFAULT)),x_Symbol),
    Condition(With(List(Set(t,ExpandIntegrand(Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),q),Power(Plus(f,Times(g,Power(x,s))),r),x))),Condition(Int(t,x),SumQ(t))),And(FreeQ(List(a,b,c,d,e,f,g,n,p,q,r,s),x),IntegerQ(n),IGtQ(q,C0),IntegerQ(r),IntegerQ(s),Or(EqQ(q,C1),And(GtQ(r,C0),GtQ(s,C1)),And(LtQ(s,C0),LtQ(r,C0)))))),
IIntegrate(2472,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(Plus(f_,Times(g_DEFAULT,Power(x_,s_))),r_DEFAULT)),x_Symbol),
    Condition(With(List(Set(k,Denominator(n))),Condition(Dist(k,Subst(Int(Times(Power(x,Plus(k,Negate(C1))),Power(Plus(f,Times(g,Power(x,Times(k,s)))),r),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,Times(k,n)))),p))))),q)),x),x,Power(x,Power(k,-1))),x),IntegerQ(Times(k,s)))),And(FreeQ(List(a,b,c,d,e,f,g,n,p,q,r,s),x),FractionQ(n)))),
IIntegrate(2473,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(Plus(f_,Times(g_DEFAULT,Power(x_,s_))),r_DEFAULT)),x_Symbol),
    Condition(Unintegrable(Times(Power(Plus(f,Times(g,Power(x,s))),r),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),q)),x),FreeQ(List(a,b,c,d,e,f,g,n,p,q,r,s),x))),
IIntegrate(2474,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(v_,p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(u_,r_DEFAULT)),x_Symbol),
    Condition(Int(Times(Power(ExpandToSum(u,x),r),Power(Plus(a,Times(b,Log(Times(c,Power(ExpandToSum(v,x),p))))),q)),x),And(FreeQ(List(a,b,c,p,q,r),x),BinomialQ(List(u,v),x),Not(BinomialMatchQ(List(u,v),x))))),
IIntegrate(2475,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(x_,m_DEFAULT),Power(Plus(f_,Times(g_DEFAULT,Power(x_,s_))),r_DEFAULT)),x_Symbol),
    Condition(Dist(Power(n,-1),Subst(Int(Times(Power(x,Plus(Simplify(Times(Plus(m,C1),Power(n,-1))),Negate(C1))),Power(Plus(f,Times(g,Power(x,Times(s,Power(n,-1))))),r),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,x)),p))))),q)),x),x,Power(x,n)),x),And(FreeQ(List(a,b,c,d,e,f,g,m,n,p,q,r,s),x),IntegerQ(r),IntegerQ(Times(s,Power(n,-1))),IntegerQ(Simplify(Times(Plus(m,C1),Power(n,-1)))),Or(GtQ(Times(Plus(m,C1),Power(n,-1)),C0),IGtQ(q,C0))))),
IIntegrate(2476,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(x_,m_DEFAULT),Power(Plus(f_,Times(g_DEFAULT,Power(x_,s_))),r_DEFAULT)),x_Symbol),
    Condition(Int(ExpandIntegrand(Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),q),Times(Power(x,m),Power(Plus(f,Times(g,Power(x,s))),r)),x),x),And(FreeQ(List(a,b,c,d,e,f,g,m,n,p,q,r,s),x),IGtQ(q,C0),IntegerQ(m),IntegerQ(r),IntegerQ(s)))),
IIntegrate(2477,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(x_,m_DEFAULT),Power(Plus(f_,Times(g_DEFAULT,Power(x_,s_))),r_DEFAULT)),x_Symbol),
    Condition(Dist(Power(n,-1),Subst(Int(Times(Power(x,Plus(m,Power(n,-1),Negate(C1))),Power(Plus(f,Times(g,Power(x,Times(s,Power(n,-1))))),r),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,x)),p))))),q)),x),x,Power(x,n)),x),And(FreeQ(List(a,b,c,d,e,f,g,m,n,p,q,r,s),x),FractionQ(n),IntegerQ(Power(n,-1)),IntegerQ(Times(s,Power(n,-1)))))),
IIntegrate(2478,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_DEFAULT))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(Times(h_DEFAULT,x_),m_),Power(Plus(f_DEFAULT,Times(g_DEFAULT,Power(x_,s_DEFAULT))),r_DEFAULT)),x_Symbol),
    Condition(With(List(Set(k,Denominator(m))),Dist(Times(k,Power(h,-1)),Subst(Int(Times(Power(x,Plus(Times(k,Plus(m,C1)),Negate(C1))),Power(Plus(f,Times(g,Power(x,Times(k,s)),Power(Power(h,s),-1))),r),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,Times(k,n)),Power(Power(h,n),-1))),p))))),q)),x),x,Power(Times(h,x),Power(k,-1))),x)),And(FreeQ(List(a,b,c,d,e,f,g,h,p,r),x),FractionQ(m),IntegerQ(n),IntegerQ(s)))),
IIntegrate(2479,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(Times(h_DEFAULT,x_),m_DEFAULT),Power(Plus(f_,Times(g_DEFAULT,Power(x_,s_))),r_DEFAULT)),x_Symbol),
    Condition(Unintegrable(Times(Power(Times(h,x),m),Power(Plus(f,Times(g,Power(x,s))),r),Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),q)),x),FreeQ(List(a,b,c,d,e,f,g,h,m,n,p,q,r,s),x))),
IIntegrate(2480,Int(Times(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(v_,p_DEFAULT))),b_DEFAULT)),q_DEFAULT),Power(u_,r_DEFAULT),Power(Times(h_DEFAULT,x_),m_DEFAULT)),x_Symbol),
    Condition(Int(Times(Power(Times(h,x),m),Power(ExpandToSum(u,x),r),Power(Plus(a,Times(b,Log(Times(c,Power(ExpandToSum(v,x),p))))),q)),x),And(FreeQ(List(a,b,c,h,m,p,q,r),x),BinomialQ(List(u,v),x),Not(BinomialMatchQ(List(u,v),x))))),
IIntegrate(2481,Int(Times(Power(Log(Times(f_DEFAULT,Power(x_,q_DEFAULT))),m_DEFAULT),Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),Power(x_,-1)),x_Symbol),
    Condition(Plus(Simp(Times(Power(Log(Times(f,Power(x,q))),Plus(m,C1)),Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),Power(Times(q,Plus(m,C1)),-1)),x),Negate(Dist(Times(b,e,n,p,Power(Times(q,Plus(m,C1)),-1)),Int(Times(Power(x,Plus(n,Negate(C1))),Power(Log(Times(f,Power(x,q))),Plus(m,C1)),Power(Plus(d,Times(e,Power(x,n))),-1)),x),x))),And(FreeQ(List(a,b,c,d,e,f,m,n,p,q),x),NeQ(m,CN1)))),
IIntegrate(2482,Int(Times(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(x_,n_))),p_DEFAULT))),b_DEFAULT)),Power($(F_,Times(f_DEFAULT,x_)),m_DEFAULT)),x_Symbol),
    Condition(With(List(Set(u,IntHide(Power(F(Times(f,x)),m),x))),Plus(Dist(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),u,x),Negate(Dist(Times(b,e,n,p),Int(SimplifyIntegrand(Times(u,Power(x,Plus(n,Negate(C1))),Power(Plus(d,Times(e,Power(x,n))),-1)),x),x),x)))),And(FreeQ(List(a,b,c,d,e,f,p),x),MemberQ(List($s("ArcSin"),$s("ArcCos"),$s("ArcSinh"),$s("ArcCosh")),FSymbol),IGtQ(m,C0),IGtQ(n,C1)))),
IIntegrate(2483,Int(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(Plus(f_DEFAULT,Times(g_DEFAULT,x_)),n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),x_Symbol),
    Condition(Dist(Power(g,-1),Subst(Int(Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(x,n))),p))))),q),x),x,Plus(f,Times(g,x))),x),And(FreeQ(List(a,b,c,d,e,f,g,n,p),x),IGtQ(q,C0),Or(EqQ(q,C1),IntegerQ(n))))),
IIntegrate(2484,Int(Power(Plus(a_DEFAULT,Times(Log(Times(c_DEFAULT,Power(Plus(d_,Times(e_DEFAULT,Power(Plus(f_DEFAULT,Times(g_DEFAULT,x_)),n_))),p_DEFAULT))),b_DEFAULT)),q_DEFAULT),x_Symbol),
    Condition(Unintegrable(Power(Plus(a,Times(b,Log(Times(c,Power(Plus(d,Times(e,Power(Plus(f,Times(g,x)),n))),p))))),q),x),FreeQ(List(a,b,c,d,e,f,g,n,p,q),x))),
IIntegrate(2485,Int(Times(Power(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT))),s_DEFAULT),u_DEFAULT),x_Symbol),
    Condition(Int(Times(u,Power(Log(Times(e,Power(Times(Power(b,p),f,Power(Plus(c,Times(d,x)),Plus(p,q)),Power(Power(d,p),-1)),r))),s)),x),And(FreeQ(List(a,b,c,d,e,f,p,q,r,s),x),EqQ(Plus(Times(b,c),Times(CN1,a,d)),C0),IntegerQ(p)))),
IIntegrate(2486,Int(Power(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT))),s_DEFAULT),x_Symbol),
    Condition(Plus(Simp(Times(Plus(a,Times(b,x)),Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),s),Power(b,-1)),x),Dist(Times(q,r,s,Plus(Times(b,c),Times(CN1,a,d)),Power(b,-1)),Int(Times(Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Plus(s,Negate(C1))),Power(Plus(c,Times(d,x)),-1)),x),x)),And(FreeQ(List(a,b,c,d,e,f,p,q,r,s),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0),EqQ(Plus(p,q),C0),IGtQ(s,C0)))),
IIntegrate(2487,Int(Power(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT))),s_DEFAULT),x_Symbol),
    Condition(Plus(Simp(Times(Plus(a,Times(b,x)),Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),s),Power(b,-1)),x),Dist(Times(q,r,s,Plus(Times(b,c),Times(CN1,a,d)),Power(b,-1)),Int(Times(Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Plus(s,Negate(C1))),Power(Plus(c,Times(d,x)),-1)),x),x),Negate(Dist(Times(r,s,Plus(p,q)),Int(Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Plus(s,Negate(C1))),x),x))),And(FreeQ(List(a,b,c,d,e,f,p,q,r,s),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0),NeQ(Plus(p,q),C0),IGtQ(s,C0),LtQ(s,C4)))),
IIntegrate(2488,Int(Times(Power(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT))),s_DEFAULT),Power(Plus(g_DEFAULT,Times(h_DEFAULT,x_)),-1)),x_Symbol),
    Condition(Plus(Negate(Simp(Times(Log(Times(CN1,Plus(Times(b,c),Times(CN1,a,d)),Power(Times(d,Plus(a,Times(b,x))),-1))),Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),s),Power(h,-1)),x)),Dist(Times(p,r,s,Plus(Times(b,c),Times(CN1,a,d)),Power(h,-1)),Int(Times(Log(Times(CN1,Plus(Times(b,c),Times(CN1,a,d)),Power(Times(d,Plus(a,Times(b,x))),-1))),Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Plus(s,Negate(C1))),Power(Times(Plus(a,Times(b,x)),Plus(c,Times(d,x))),-1)),x),x)),And(FreeQ(List(a,b,c,d,e,f,g,h,p,q,r,s),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0),EqQ(Plus(p,q),C0),EqQ(Plus(Times(b,g),Times(CN1,a,h)),C0),IGtQ(s,C0)))),
IIntegrate(2489,Int(Times(Power(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT))),s_),Power(Plus(g_DEFAULT,Times(h_DEFAULT,x_)),-1)),x_Symbol),
    Condition(Plus(Dist(Times(d,Power(h,-1)),Int(Times(Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),s),Power(Plus(c,Times(d,x)),-1)),x),x),Negate(Dist(Times(Plus(Times(d,g),Times(CN1,c,h)),Power(h,-1)),Int(Times(Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),s),Power(Times(Plus(c,Times(d,x)),Plus(g,Times(h,x))),-1)),x),x))),And(FreeQ(List(a,b,c,d,e,f,g,h,p,q,r,s),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0),EqQ(Plus(p,q),C0),NeQ(Plus(Times(b,g),Times(CN1,a,h)),C0),NeQ(Plus(Times(d,g),Times(CN1,c,h)),C0),IGtQ(s,C1)))),
IIntegrate(2490,Int(Times(Power(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT))),s_DEFAULT),Power(Plus(g_DEFAULT,Times(h_DEFAULT,x_)),-2)),x_Symbol),
    Condition(Plus(Simp(Times(Plus(a,Times(b,x)),Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),s),Power(Times(Plus(Times(b,g),Times(CN1,a,h)),Plus(g,Times(h,x))),-1)),x),Negate(Dist(Times(p,r,s,Plus(Times(b,c),Times(CN1,a,d)),Power(Plus(Times(b,g),Times(CN1,a,h)),-1)),Int(Times(Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Plus(s,Negate(C1))),Power(Times(Plus(c,Times(d,x)),Plus(g,Times(h,x))),-1)),x),x))),And(FreeQ(List(a,b,c,d,e,f,g,h,p,q,r,s),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0),EqQ(Plus(p,q),C0),NeQ(Plus(Times(b,g),Times(CN1,a,h)),C0),IGtQ(s,C0)))),
IIntegrate(2491,Int(Times(Power(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT))),s_),Power(Plus(g_DEFAULT,Times(h_DEFAULT,x_)),-3)),x_Symbol),
    Condition(Plus(Dist(Times(d,Power(Plus(Times(d,g),Times(CN1,c,h)),-1)),Int(Times(Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),s),Power(Plus(g,Times(h,x)),-2)),x),x),Negate(Dist(Times(h,Power(Plus(Times(d,g),Times(CN1,c,h)),-1)),Int(Times(Plus(c,Times(d,x)),Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),s),Power(Plus(g,Times(h,x)),-3)),x),x))),And(FreeQ(List(a,b,c,d,e,f,g,h,p,q,r,s),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0),EqQ(Plus(p,q),C0),EqQ(Plus(Times(b,g),Times(CN1,a,h)),C0),NeQ(Plus(Times(d,g),Times(CN1,c,h)),C0),IGtQ(s,C0)))),
IIntegrate(2492,Int(Times(Power(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT))),s_DEFAULT),Power(Plus(g_DEFAULT,Times(h_DEFAULT,x_)),m_DEFAULT)),x_Symbol),
    Condition(Plus(Simp(Times(Power(Plus(g,Times(h,x)),Plus(m,C1)),Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),s),Power(Times(h,Plus(m,C1)),-1)),x),Negate(Dist(Times(p,r,s,Plus(Times(b,c),Times(CN1,a,d)),Power(Times(h,Plus(m,C1)),-1)),Int(Times(Power(Plus(g,Times(h,x)),Plus(m,C1)),Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Plus(s,Negate(C1))),Power(Times(Plus(a,Times(b,x)),Plus(c,Times(d,x))),-1)),x),x))),And(FreeQ(List(a,b,c,d,e,f,g,h,m,p,q,r,s),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0),EqQ(Plus(p,q),C0),IGtQ(s,C0),NeQ(m,CN1)))),
IIntegrate(2493,Int(Times(Power(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT))),-1),Power(Plus(g_DEFAULT,Times(h_DEFAULT,x_)),-2)),x_Symbol),
    Condition(Simp(Times(b,Plus(c,Times(d,x)),Power(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r)),Power(Times(p,r),-1)),ExpIntegralEi(Times(CN1,Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Power(Times(p,r),-1))),Power(Times(h,p,r,Plus(Times(b,c),Times(CN1,a,d)),Plus(g,Times(h,x))),-1)),x),And(FreeQ(List(a,b,c,d,e,f,g,h,p,q,r),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0),EqQ(Plus(p,q),C0),EqQ(Plus(Times(b,g),Times(CN1,a,h)),C0)))),
IIntegrate(2494,Int(Times(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT))),Power(Plus(g_DEFAULT,Times(h_DEFAULT,x_)),-1)),x_Symbol),
    Condition(Plus(Simp(Times(Log(Plus(g,Times(h,x))),Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Power(h,-1)),x),Negate(Dist(Times(b,p,r,Power(h,-1)),Int(Times(Log(Plus(g,Times(h,x))),Power(Plus(a,Times(b,x)),-1)),x),x)),Negate(Dist(Times(d,q,r,Power(h,-1)),Int(Times(Log(Plus(g,Times(h,x))),Power(Plus(c,Times(d,x)),-1)),x),x))),And(FreeQ(List(a,b,c,d,e,f,g,h,p,q,r),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0)))),
IIntegrate(2495,Int(Times(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT))),Power(Plus(g_DEFAULT,Times(h_DEFAULT,x_)),m_DEFAULT)),x_Symbol),
    Condition(Plus(Simp(Times(Power(Plus(g,Times(h,x)),Plus(m,C1)),Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Power(Times(h,Plus(m,C1)),-1)),x),Negate(Dist(Times(b,p,r,Power(Times(h,Plus(m,C1)),-1)),Int(Times(Power(Plus(g,Times(h,x)),Plus(m,C1)),Power(Plus(a,Times(b,x)),-1)),x),x)),Negate(Dist(Times(d,q,r,Power(Times(h,Plus(m,C1)),-1)),Int(Times(Power(Plus(g,Times(h,x)),Plus(m,C1)),Power(Plus(c,Times(d,x)),-1)),x),x))),And(FreeQ(List(a,b,c,d,e,f,g,h,m,p,q,r),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0),NeQ(m,CN1)))),
IIntegrate(2496,Int(Times(Sqr(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT)))),Power(Plus(g_DEFAULT,Times(h_DEFAULT,x_)),-1)),x_Symbol),
    Condition(Plus(Int(Times(Sqr(Plus(Log(Power(Plus(a,Times(b,x)),Times(p,r))),Log(Power(Plus(c,Times(d,x)),Times(q,r))))),Power(Plus(g,Times(h,x)),-1)),x),Simp(Times(Plus(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Negate(Log(Power(Plus(a,Times(b,x)),Times(p,r)))),Negate(Log(Power(Plus(c,Times(d,x)),Times(q,r))))),Plus(Times(C2,Int(Times(Log(Power(Plus(c,Times(d,x)),Times(q,r))),Power(Plus(g,Times(h,x)),-1)),x)),Int(Times(Plus(Log(Power(Plus(a,Times(b,x)),Times(p,r))),Negate(Log(Power(Plus(c,Times(d,x)),Times(q,r)))),Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r)))),Power(Plus(g,Times(h,x)),-1)),x))),x)),And(FreeQ(List(a,b,c,d,e,f,g,h,p,q,r),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0),EqQ(Plus(Times(b,g),Times(CN1,a,h)),C0)))),
IIntegrate(2497,Int(Times(Sqr(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT)))),Power(Plus(g_DEFAULT,Times(h_DEFAULT,x_)),-1)),x_Symbol),
    Condition(Plus(Simp(Times(Log(Plus(g,Times(h,x))),Sqr(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r)))),Power(h,-1)),x),Negate(Dist(Times(C2,b,p,r,Power(h,-1)),Int(Times(Log(Plus(g,Times(h,x))),Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Power(Plus(a,Times(b,x)),-1)),x),x)),Negate(Dist(Times(C2,d,q,r,Power(h,-1)),Int(Times(Log(Plus(g,Times(h,x))),Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Power(Plus(c,Times(d,x)),-1)),x),x))),And(FreeQ(List(a,b,c,d,e,f,g,h,p,q,r),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0)))),
IIntegrate(2498,Int(Times(Power(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT))),s_),Power(Plus(g_DEFAULT,Times(h_DEFAULT,x_)),m_DEFAULT)),x_Symbol),
    Condition(Plus(Simp(Times(Power(Plus(g,Times(h,x)),Plus(m,C1)),Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),s),Power(Times(h,Plus(m,C1)),-1)),x),Negate(Dist(Times(b,p,r,s,Power(Times(h,Plus(m,C1)),-1)),Int(Times(Power(Plus(g,Times(h,x)),Plus(m,C1)),Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Plus(s,Negate(C1))),Power(Plus(a,Times(b,x)),-1)),x),x)),Negate(Dist(Times(d,q,r,s,Power(Times(h,Plus(m,C1)),-1)),Int(Times(Power(Plus(g,Times(h,x)),Plus(m,C1)),Power(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Plus(s,Negate(C1))),Power(Plus(c,Times(d,x)),-1)),x),x))),And(FreeQ(List(a,b,c,d,e,f,g,h,m,p,q,r,s),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0),IGtQ(s,C0),NeQ(m,CN1)))),
IIntegrate(2499,Int(Times(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT))),Power(Plus(s_DEFAULT,Times(Log(Times(i_DEFAULT,Power(Plus(g_DEFAULT,Times(h_DEFAULT,x_)),n_DEFAULT))),t_DEFAULT)),m_DEFAULT),Power(Plus(j_DEFAULT,Times(k_DEFAULT,x_)),-1)),x_Symbol),
    Condition(Plus(Simp(Times(Power(Plus(s,Times(t,Log(Times(i,Power(Plus(g,Times(h,x)),n))))),Plus(m,C1)),Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Power(Times(k,n,t,Plus(m,C1)),-1)),x),Negate(Dist(Times(b,p,r,Power(Times(k,n,t,Plus(m,C1)),-1)),Int(Times(Power(Plus(s,Times(t,Log(Times(i,Power(Plus(g,Times(h,x)),n))))),Plus(m,C1)),Power(Plus(a,Times(b,x)),-1)),x),x)),Negate(Dist(Times(d,q,r,Power(Times(k,n,t,Plus(m,C1)),-1)),Int(Times(Power(Plus(s,Times(t,Log(Times(i,Power(Plus(g,Times(h,x)),n))))),Plus(m,C1)),Power(Plus(c,Times(d,x)),-1)),x),x))),And(FreeQ(List(a,b,c,d,e,f,g,h,i,j,k,s,t,m,n,p,q,r),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0),EqQ(Plus(Times(h,j),Times(CN1,g,k)),C0),IGtQ(m,C0)))),
IIntegrate(2500,Int(Times(Log(Times(e_DEFAULT,Power(Times(f_DEFAULT,Power(Plus(a_DEFAULT,Times(b_DEFAULT,x_)),p_DEFAULT),Power(Plus(c_DEFAULT,Times(d_DEFAULT,x_)),q_DEFAULT)),r_DEFAULT))),Plus(s_DEFAULT,Times(Log(Times(i_DEFAULT,Power(Plus(g_DEFAULT,Times(h_DEFAULT,x_)),n_DEFAULT))),t_DEFAULT)),Power(Plus(j_DEFAULT,Times(k_DEFAULT,x_)),-1)),x_Symbol),
    Condition(Plus(Dist(Plus(Log(Times(e,Power(Times(f,Power(Plus(a,Times(b,x)),p),Power(Plus(c,Times(d,x)),q)),r))),Negate(Log(Power(Plus(a,Times(b,x)),Times(p,r)))),Negate(Log(Power(Plus(c,Times(d,x)),Times(q,r))))),Int(Times(Plus(s,Times(t,Log(Times(i,Power(Plus(g,Times(h,x)),n))))),Power(Plus(j,Times(k,x)),-1)),x),x),Int(Times(Log(Power(Plus(a,Times(b,x)),Times(p,r))),Plus(s,Times(t,Log(Times(i,Power(Plus(g,Times(h,x)),n))))),Power(Plus(j,Times(k,x)),-1)),x),Int(Times(Log(Power(Plus(c,Times(d,x)),Times(q,r))),Plus(s,Times(t,Log(Times(i,Power(Plus(g,Times(h,x)),n))))),Power(Plus(j,Times(k,x)),-1)),x)),And(FreeQ(List(a,b,c,d,e,f,g,h,i,j,k,s,t,n,p,q,r),x),NeQ(Plus(Times(b,c),Times(CN1,a,d)),C0))))
  );
}