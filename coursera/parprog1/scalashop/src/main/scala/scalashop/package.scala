import common.task

package object scalashop {

  /** The value of every pixel is represented as a 32 bit integer. */
  type RGBA = Int

  /** Returns the red component. */
  def red(c: RGBA): Int = (0xff000000 & c) >>> 24

  /** Returns the green component. */
  def green(c: RGBA): Int = (0x00ff0000 & c) >>> 16

  /** Returns the blue component. */
  def blue(c: RGBA): Int = (0x0000ff00 & c) >>> 8

  /** Returns the alpha component. */
  def alpha(c: RGBA): Int = (0x000000ff & c) >>> 0

  /** Used to create an RGBA value from separate components. */
  def rgba(r: Int, g: Int, b: Int, a: Int): RGBA = {
    (r << 24) | (g << 16) | (b << 8) | (a << 0)
  }

  /** Restricts the integer into the specified range. */
  def clamp(v: Int, min: Int, max: Int): Int = {
    if (v < min) min
    else if (v > max) max
    else v
  }

  /** Image is a two-dimensional matrix of pixel values. */
  class Img(val width: Int, val height: Int, private val data: Array[RGBA]) {
    def this(w: Int, h: Int) = this(w, h, new Array(w * h))

    def inside(x: Int, y: Int) = 0 <= x && x < width && 0 <= y && y < height

    def apply(x: Int, y: Int): RGBA = data(y * width + x)

    def update(x: Int, y: Int, c: RGBA): Unit = data(y * width + x) = c
  }

  /** Computes the blurred RGBA value of a single pixel of the input image. */
  def boxBlurKernel(src: Img, x: Int, y: Int, radius: Int): RGBA = {

    if (radius == 0) return src(x, y)

    var r, g, b, a = 0
    var i, k = 0
    val diameter = (radius + 1) * 2 - 1
    val n = diameter * diameter

    while (i < n) {
      val (xi, yi) = (
        x - radius + i % diameter,
        y - radius + i / diameter
      )

      if (src.inside(xi, yi)) {
        val p = src(xi, yi)
        r += red(p)
        g += green(p)
        b += blue(p)
        a += alpha(p)
        k += 1
      }


      i += 1
    }

    rgba(r / k, g / k, b / k, a / k)
  }

  def parallelSeq[A](tasks: Seq[() => A]): Seq[A] = {
    tasks.map(t => task { t() }).map(_.join())
  }

  def splitToRanges(from: Int, to: Int, num: Int): Seq[(Int, Int)] = {
    val size = to - from
    val number = if (size >= num) num else size
    val len = math.ceil(size.toDouble / number).toInt
    (from until to).grouped(len).map(i => i.min -> (i.max + 1)).toList
  }

}
