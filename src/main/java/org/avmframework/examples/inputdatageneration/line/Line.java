package org.avmframework.examples.inputdatageneration.line;

public class Line {

  double x1, y1, x2, y2;

  public Line(double x1, double y1, double x2, double y2) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }

  public boolean intersect(Line a, Line b) {

    double ua_t = (b.x2 - b.x1) * (a.y1 - b.y1) - (b.y2 - b.y1) * (a.x1 - b.x1);
    double ub_t = (a.x2 - a.x1) * (a.y1 - b.y1) - (a.y2 - a.y1) * (a.x1 - b.x1);
    double u_b = (b.y2 - b.y1) * (a.x2 - a.x1) - (b.x2 - b.x1) * (a.y2 - a.y1);

    if (u_b != 0) {
      double ua = ua_t / u_b;
      double ub = ub_t / u_b;

      if (0 <= ua) {
        if (ua <= 1) {
          if (0 <= ub) {
            if (ub <= 1) {
              return true;
            }
          }
        }
      }
      return false;

    } else {
      if (ua_t == 0) {
        return true;
      }

      if (ub_t == 0) {
        return true;
      }

      return false;
    }
  }
}
