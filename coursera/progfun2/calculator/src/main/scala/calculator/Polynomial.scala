package calculator

object Polynomial {
  def computeDelta(a: Signal[Double], b: Signal[Double],
      c: Signal[Double]): Signal[Double] = {

    Var(b() * b()  - 4 * a() * c() )
  }

  def computeSolutions(a: Signal[Double], b: Signal[Double],
      c: Signal[Double], delta: Signal[Double]): Signal[Set[Double]] = {

    val divBy = Var(2 * a())
    val sqrt = Var(math.sqrt(delta()))
    Var(Set((-b() - sqrt())/divBy(), (-b() + sqrt())/divBy()))
  }
}
