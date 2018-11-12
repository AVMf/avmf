package org.avmframework.examples.inputdatageneration.line;

public class Line {

  double x1;
  double y1;
  double x2;
  double y2;

  public Line(double x1, double y1, double x2, double y2) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }

  public boolean intersect(Line line1, Line line2) {

    double u1t = (line2.x2 - line2.x1) * (line1.y1 - line2.y1)
        - (line2.y2 - line2.y1) * (line1.x1 - line2.x1);
    double u2t = (line1.x2 - line1.x1) * (line1.y1 - line2.y1)
        - (line1.y2 - line1.y1) * (line1.x1 - line2.x1);
    double u2 = (line2.y2 - line2.y1) * (line1.x2 - line1.x1)
        - (line2.x2 - line2.x1) * (line1.y2 - line1.y1);

    if (u2 != 0) {
      double u1 = u1t / u2;
      double u02 = u2t / u2;

      if (0 <= u1) {
        if (u1 <= 1) {
          if (0 <= u02) {
            if (u02 <= 1) {
              return true;
            }
          }
        }
      }
      return false;

    } else {
      if (u1t == 0) {
        return true;
      }

      if (u2t == 0) {
        return true;
      }

      return false;
    }
  }
}
